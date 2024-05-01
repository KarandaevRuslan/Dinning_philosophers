package program.mainWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import program.views.ICanvasForTaskVisualization;
import program.views.IView;

/** The interface for "MainWindow" control */
public interface IMainWindow extends IView {
  /** Returns canvas for task visualization */
  ICanvasForTaskVisualization getCanvas();

  /** Returns philosophers threads status */
  Status getStatus() throws Exception;

  /** Sets philosophers threads status in the bottom panel */
  void setStatus(Status status) throws Exception;

  /** Makes start philosophers threads button active */
  void activateStartBtn();

  /** Makes start philosophers threads button inactive */
  void deactivateStartBtn();

  /** Makes pause philosophers threads button active */
  void activatePauseBtn();

  /** Makes pause philosophers threads button inactive */
  void deactivatePauseBtn();

  /** Makes continue philosophers threads button active */
  void activateContinueBtn();

  /** Makes continue philosophers threads button inactive */
  void deactivateContinueBtn();

  /** Makes stop philosophers threads button active */
  void activateStopBtn();

  /** Makes stop philosophers threads button inactive */
  void deactivateStopBtn();

  /** Makes clear logs button active */
  void activateClearLogsBtn();

  /** Makes clear logs button inactive */
  void deactivateClearLogsBtn();

  /** Sets eat time value on corresponding labels on the right bottom and right center sides */
  void setEatTimeLblsValue(int value);

  /** Gets eat time value from current slider value */
  int getEatTimeSliderValue();

  /** Sets eat time value to the slider and updates eat time labels */
  void setEatTimeSliderValue(int value);

  /** Clears logs list. Corresponding button can be inactive */
  void clearLogs() throws InterruptedException;

  /** Adds logs to the list */
  void addLog(String message, Double elapsedTime) throws InterruptedException;

  /** Sets handler handling eat time slider changes */
  void setEatTimeSliderChangedEH(EventHandler<ActionEvent> eatTimeSliderChangedEH);

  /** Sets handler handling config saving */
  void setCfgSavedEH(EventHandler<ActionEvent> cfgSavedEH);

  /** Sets handler handling config loading */
  void setCfgLoadedEH(EventHandler<ActionEvent> cfgLoadedEH);

  /** Sets handler handling start philosopher threads button click */
  void setStartEH(EventHandler<ActionEvent> startEH);

  /** Sets handler handling pause philosopher threads button click */
  void setPauseEH(EventHandler<ActionEvent> pauseEH);

  /** Sets handler handling continue philosopher threads button click */
  void setContinueEH(EventHandler<ActionEvent> continueEH);

  /** Sets handler handling stop philosopher threads button click */
  void setStopEH(EventHandler<ActionEvent> stopEH);

  /** Sets handler handling clear logs button click */
  void setClearLogsEH(EventHandler<ActionEvent> clearLogsEH);
}
