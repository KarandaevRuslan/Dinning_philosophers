package program.mainWindow;

import java.util.concurrent.Semaphore;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import program.businessLogic.XMLManager;
import program.views.CanvasPane;
import program.views.CanvasPaneForTaskVisualization;
import program.views.ICanvasForTaskVisualization;

/** View that demonstrates solved dinning philosophers problem */
public class MainWindow implements IMainWindow {
  private final String ACTIVE = "Active";
  private final String PAUSED = "Paused";
  private final String INACTIVE = "Inactive";
  private Semaphore mutexForLogs = new Semaphore(1);

  @FXML private SplitPane splitPaneCenter;
  @FXML private Circle statusIcon;
  @FXML private Label statusLbl;
  @FXML private Button startBtn;
  @FXML private Button pauseBtn;
  @FXML private Button continueBtn;
  @FXML private Button stopBtn;
  @FXML private Label eatTime1Lbl;
  @FXML private Label eatTime2Lbl;
  @FXML private Slider eatTimeSlider;
  @FXML private TextArea logsTxtArea;
  @FXML private Button clearLogsBtn;
  private ICanvasForTaskVisualization canvas;

  private EventHandler<ActionEvent> eatTimeSliderChangedEH;
  private EventHandler<ActionEvent> cfgSavedEH;
  private EventHandler<ActionEvent> cfgLoadedEH;
  private EventHandler<ActionEvent> startEH;
  private EventHandler<ActionEvent> pauseEH;
  private EventHandler<ActionEvent> continueEH;
  private EventHandler<ActionEvent> stopEH;
  private EventHandler<ActionEvent> clearLogsEH;

  @FXML
  private void initialize() {
    var presenter = new MainWindowPresenter(this, new XMLManager());
    statusLbl.setText(INACTIVE);
    eatTimeSlider
        .valueProperty()
        .addListener(x -> eatTimeSliderChangedEH.handle(new ActionEvent()));
    canvas = new CanvasPaneForTaskVisualization(100, 100);
    splitPaneCenter.getItems().add(0, (CanvasPane) canvas);
    splitPaneCenter.setDividerPositions(0.6f, 0.4f);
  }

  @FXML
  private void onSaveCfg(ActionEvent actionEvent) {
    cfgSavedEH.handle(actionEvent);
  }

  @FXML
  private void onLoadCfg(ActionEvent actionEvent) {
    cfgLoadedEH.handle(actionEvent);
  }

  @FXML
  private void onStart(ActionEvent actionEvent) {
    startEH.handle(actionEvent);
  }

  @FXML
  private void onPause(ActionEvent actionEvent) {
    pauseEH.handle(actionEvent);
  }

  @FXML
  private void onStop(ActionEvent actionEvent) {
    stopEH.handle(actionEvent);
  }

  @FXML
  private void onContinue(ActionEvent actionEvent) {
    continueEH.handle(actionEvent);
  }

  @FXML
  public void onClearLogs(ActionEvent actionEvent) {
    clearLogsEH.handle(actionEvent);
  }

  @Override
  public ICanvasForTaskVisualization getCanvas() {
    return canvas;
  }

  @Override
  public Status getStatus() throws Exception {
    return switch (statusLbl.getText()) {
      case INACTIVE -> Status.INACTIVE;
      case PAUSED -> Status.PAUSED;
      case ACTIVE -> Status.ACTIVE;
      default -> throw new Exception("Invalid status '" + statusLbl.getText() + "'");
    };
  }

  @Override
  public void setStatus(Status status) throws Exception {
    switch (status) {
      case INACTIVE:
        statusIcon.setStroke(Color.RED);
        statusLbl.setText(INACTIVE);
        break;
      case PAUSED:
        statusIcon.setStroke(Color.ORANGE);
        statusLbl.setText(PAUSED);
        break;
      case ACTIVE:
        statusIcon.setStroke(Color.GREEN);
        statusLbl.setText(ACTIVE);
        break;
      default:
        throw new Exception("Invalid status '" + status.name() + "'");
    }
  }

  @Override
  public void activateStartBtn() {
    startBtn.setDisable(false);
  }

  @Override
  public void deactivateStartBtn() {
    startBtn.setDisable(true);
  }

  @Override
  public void activatePauseBtn() {
    pauseBtn.setDisable(false);
  }

  @Override
  public void deactivatePauseBtn() {
    pauseBtn.setDisable(true);
  }

  @Override
  public void activateContinueBtn() {
    continueBtn.setDisable(false);
  }

  @Override
  public void deactivateContinueBtn() {
    continueBtn.setDisable(true);
  }

  @Override
  public void activateStopBtn() {
    stopBtn.setDisable(false);
  }

  @Override
  public void deactivateStopBtn() {
    stopBtn.setDisable(true);
  }

  @Override
  public void activateClearLogsBtn() {
    clearLogsBtn.setDisable(false);
  }

  @Override
  public void deactivateClearLogsBtn() {
    clearLogsBtn.setDisable(true);
  }

  @Override
  public void setEatTimeLblsValue(int value) {
    eatTime1Lbl.setText(String.valueOf(value));
    eatTime2Lbl.setText(String.valueOf(value));
  }

  @Override
  public int getEatTimeSliderValue() {
    return (int) eatTimeSlider.getValue();
  }

  @Override
  public void setEatTimeSliderValue(int value) {
    eatTimeSlider.setValue((double) value);
  }

  @Override
  public void clearLogs() throws InterruptedException {
    mutexForLogs.acquire();
    logsTxtArea.setText("");
    mutexForLogs.release();
  }

  @Override
  public void addLog(String message, Double elapsedTime) throws InterruptedException {
    mutexForLogs.acquire();
    logsTxtArea.appendText(String.format("* %s;     Elapsed time %.2f s\n", message, elapsedTime));
    mutexForLogs.release();
  }

  @Override
  public void setEatTimeSliderChangedEH(EventHandler<ActionEvent> eatTimeSliderChangedEH) {
    this.eatTimeSliderChangedEH = eatTimeSliderChangedEH;
  }

  @Override
  public void setCfgSavedEH(EventHandler<ActionEvent> cfgSavedEH) {
    this.cfgSavedEH = cfgSavedEH;
  }

  @Override
  public void setCfgLoadedEH(EventHandler<ActionEvent> cfgLoadedEH) {
    this.cfgLoadedEH = cfgLoadedEH;
  }

  @Override
  public void setStartEH(EventHandler<ActionEvent> startEH) {
    this.startEH = startEH;
  }

  @Override
  public void setPauseEH(EventHandler<ActionEvent> pauseEH) {
    this.pauseEH = pauseEH;
  }

  @Override
  public void setContinueEH(EventHandler<ActionEvent> continueEH) {
    this.continueEH = continueEH;
  }

  @Override
  public void setStopEH(EventHandler<ActionEvent> stopEH) {
    this.stopEH = stopEH;
  }

  @Override
  public void setClearLogsEH(EventHandler<ActionEvent> clearLogsEH) {
    this.clearLogsEH = clearLogsEH;
  }
}
