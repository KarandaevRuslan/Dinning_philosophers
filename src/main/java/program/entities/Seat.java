package program.entities;

/** 5 Seats that can be taken up by philosopher. Contains 2 forks */
public class Seat {
  private volatile boolean isFree = true;

  private final Fork leftFork;
  private final Fork rightFork;

  public Seat(Fork leftFork, Fork rightFork) {
    this.leftFork = leftFork;
    this.rightFork = rightFork;
  }

  public Fork getLeftFork() {
    return leftFork;
  }

  public Fork getRightFork() {
    return rightFork;
  }

  /** Points is seat taken up by philosopher or not */
  public boolean isFree() {
    return isFree;
  }

  public void setFree(boolean free) {
    isFree = free;
  }

  public int getNumber() {
    return leftFork.getNumber();
  }
}
