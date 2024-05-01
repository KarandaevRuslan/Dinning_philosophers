package program.entities;

import javafx.application.Platform;
import program.businessLogic.MessageService;

import java.util.concurrent.ThreadLocalRandom;

/** A thread trying to obtain seat and forks */
public class Philosopher extends Thread {

  private final int number;

  /** If seat is null then philosopher is thinking */
  private Seat seat;

  public Philosopher(int number) {
    this.number = number;
  }

  /**
   * Takes free random seat -> waits until both forks simultaneously will be free -> simultaneously
   * takes forks -> eats for stated time -> lays simultaneously forks on table -> lets out seat ->
   * thinks -> do the same things again.; Also updates data for canvas about taken or vacated seat
   */
  public void run() {
    var waiter = Waiter.getInstance();
    while (!isInterrupted()) {
      if (seat == null) {
        try {
          seat = waiter.takeFreeRandomSeat();
        } catch (Exception e) {
          Platform.runLater(() -> MessageService.getInstance().showWarning(e.getMessage()));
        }
        if (!waiter.isIgnoreMessages()) {
          waiter.updateSeatStatus(seat.getNumber(), number);
        }
      }

      if (waiter.notifyAndTryGrabBothForks(seat, number)) {
        eat();
        waiter.notifyAndReleaseBothForks(seat, number);
        if (!waiter.isIgnoreMessages()) {
          waiter.updateSeatStatus(seat.getNumber(), 0);
        }
        seat.setFree(true);
        seat = null;
        think();
      }
    }
  }

  /** Eat spaghetti. The method invokes after grabbing both the forks */
  private void eat() {
    try {
      Thread.sleep(Waiter.getInstance().getEatTime());
    } catch (InterruptedException e) {
      interrupt();
    }
  }

  /** Think about something. The method invokes after releasing both the forks */
  private void think() {
    try {
      var thinkTime = ThreadLocalRandom.current().nextInt(0, Waiter.getInstance().getEatTime() * 2);
      Thread.sleep(thinkTime);
    } catch (InterruptedException e) {
      interrupt();
    }
  }

  /** Returns the number of philosopher. Starts from 1 */
  public int getNumber() {
    return number;
  }

  /** Returns seat taken up by philosopher if philosopher is not thinking otherwise returns null */
  public Seat getSeat() {
    return seat;
  }
}
