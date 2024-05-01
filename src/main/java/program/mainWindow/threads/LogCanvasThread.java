package program.mainWindow.threads;

import javafx.application.Platform;
import program.businessLogic.MessageService;
import program.entities.Waiter;
import program.mainWindow.IMainWindow;

/** The thread is in charge of showing log messages and updating canvas */
public class LogCanvasThread extends Thread {
  private final IMainWindow view;
  private final long DELAY_BETWEEN_FRAMES = 100;
  private long lastUpdateTime = 0;

  public LogCanvasThread(IMainWindow view) {
    this.view = view;
  }

  @Override
  public void run() {
    var waiter = Waiter.getInstance();
    while (!isInterrupted()) {
      var log = waiter.getLog();
      if (log != null)
        Platform.runLater(
            () -> {
              try {
                view.addLog(log, waiter.getElapsedTime());
              } catch (InterruptedException e) {
                MessageService.getInstance().showWarning(e.getMessage());
              }
              view.activateClearLogsBtn();
            });
      var currentTime = System.currentTimeMillis();
      if (view.getCanvas() != null && currentTime - lastUpdateTime >= DELAY_BETWEEN_FRAMES) {
        Platform.runLater(
            () -> {
              var canvas = view.getCanvas();
              canvas.clear();
              canvas.drawTable();
              canvas.drawPlates(waiter.getSeatsStatuses());
              canvas.drawForks(waiter.getForksStatuses());
              canvas.drawThinkingPhilosophers(waiter.getThinkingPhilosophersNumbers());
            });
        lastUpdateTime = currentTime;
      }
    }
  }
}
