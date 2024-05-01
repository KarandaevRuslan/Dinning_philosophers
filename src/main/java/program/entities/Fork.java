package program.entities;

import java.util.concurrent.Semaphore;

/** Resource in dinning philosophers problem */
public class Fork {
  private final Semaphore mutex = new Semaphore(1);
  private final int number;

  public Fork(int number) {
    this.number = number;
  }

  /** Returns the number of fork on the table. Starts from 1 */
  public int getNumber() {
    return number;
  }

  /** Grabs the fork */
  void grab() {
    try {
      mutex.acquire();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  /** Releases the fork */
  void release() {
    mutex.release();
  }

  /** Checks if the fork is free or not */
  boolean isFree() {
    return mutex.availablePermits() > 0;
  }
}
