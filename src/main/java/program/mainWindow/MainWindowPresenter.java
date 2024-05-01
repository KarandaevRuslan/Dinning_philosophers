package program.mainWindow;

import javafx.event.ActionEvent;
import org.xml.sax.SAXException;
import program.businessLogic.IMessageService;
import program.businessLogic.IXMLManager;
import program.businessLogic.MessageService;
import program.entities.Waiter;
import program.mainWindow.threads.LogCanvasThread;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** The presenter over MainWindow */
public class MainWindowPresenter {
  private final String CONFIG_PATH = "config.cfg";
  private final String EAT_TIME = "eatTime";
  private final String PROPERTIES = "properties";
  private final IMainWindow view;
  private final IXMLManager manager;
  private final IMessageService messageService;
  private final Waiter waiter = Waiter.getInstance();
  private Map<String, String> properties = new HashMap<>();

  public MainWindowPresenter(IMainWindow view, IXMLManager manager) {
    this.view = view;
    this.manager = manager;
    this.messageService = MessageService.getInstance();
    properties.put(EAT_TIME, String.valueOf(view.getEatTimeSliderValue()));
    waiter.setEatTime(view.getEatTimeSliderValue());
    var logCanvasThread = new LogCanvasThread(view);
    logCanvasThread.setDaemon(true);
    logCanvasThread.start();

    initHandlers();
  }

  /** Initializes view handlers */
  private void initHandlers() {
    view.setEatTimeSliderChangedEH(this::onEatTimeSliderChanged);

    view.setCfgSavedEH(this::onCfgSaved);
    view.setCfgLoadedEH(this::onCfgLoaded);
    view.setStartEH(this::onStart);
    view.setPauseEH(this::onPause);
    view.setContinueEH(this::onContinue);
    view.setStopEH(this::onStop);
    view.setClearLogsEH(this::onClearLogs);
  }

  private void onEatTimeSliderChanged(ActionEvent actionEvent) {
    view.setEatTimeLblsValue(view.getEatTimeSliderValue());
    properties.put(EAT_TIME, String.valueOf(view.getEatTimeSliderValue()));
    waiter.setEatTime(view.getEatTimeSliderValue());
  }

  private void onCfgSaved(ActionEvent actionEvent) {
    try {
      manager.writeXML(CONFIG_PATH, properties, PROPERTIES);
    } catch (ParserConfigurationException | TransformerException | FileNotFoundException e) {
      messageService.showWarning("Can not save config");
    }
  }

  private void onCfgLoaded(ActionEvent actionEvent) {
    try {
      properties = manager.readXML(CONFIG_PATH);
    } catch (ParserConfigurationException | IOException | SAXException e) {
      messageService.showWarning("Can not load config");
    }
    view.setEatTimeSliderValue(Integer.parseInt(properties.get(EAT_TIME)));
  }

  /**
   * Starts philosophers threads, changes their status in the bottom panel to active and activation
   * of some of the 4 on the left buttons
   */
  private void onStart(ActionEvent actionEvent) {
    waiter.start();
    view.deactivateStartBtn();
    view.activatePauseBtn();
    view.activateStopBtn();
    try {
      view.setStatus(Status.ACTIVE);
    } catch (Exception e) {
      messageService.showWarning(e.getMessage());
    }
  }

  /**
   * Pauses philosophers threads, changes their status in the bottom panel to paused and activation
   * of some of the 4 on the left buttons
   */
  private void onPause(ActionEvent actionEvent) {
    try {
      waiter.pause();
    } catch (InterruptedException e) {
      messageService.showWarning(e.getMessage());
    }
    view.deactivatePauseBtn();
    view.activateContinueBtn();
    try {
      view.setStatus(Status.PAUSED);
    } catch (Exception e) {
      messageService.showWarning(e.getMessage());
    }
  }

  /**
   * Continues philosophers threads, changes their status in the bottom panel to active and
   * activation of some of the 4 on the left buttons
   */
  private void onContinue(ActionEvent actionEvent) {
    waiter.c0ntinue();
    view.deactivateContinueBtn();
    view.activatePauseBtn();
    try {
      view.setStatus(Status.ACTIVE);
    } catch (Exception e) {
      messageService.showWarning(e.getMessage());
    }
  }

  /**
   * Stops philosophers threads, changes their status in the bottom panel to inactive and activation
   * of some of the 4 on the left buttons
   */
  private void onStop(ActionEvent actionEvent) {
    try {
      waiter.stop(view.getStatus().equals(Status.PAUSED));
    } catch (Exception e) {
      messageService.showWarning(e.getMessage());
    }
    view.deactivateStopBtn();
    view.deactivatePauseBtn();
    view.deactivateContinueBtn();
    view.activateStartBtn();
    try {
      view.setStatus(Status.INACTIVE);
    } catch (Exception e) {
      messageService.showWarning(e.getMessage());
    }
  }

  /** Deactivates clear logs button and clears logs */
  private void onClearLogs(ActionEvent actionEvent) {
    view.deactivateClearLogsBtn();
    try {
      view.clearLogs();
    } catch (InterruptedException e) {
      messageService.showWarning(e.getMessage());
    }
  }
}
