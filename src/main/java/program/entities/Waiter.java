package program.entities;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Singleton class that takes control over philosophers, is in charge of creating logs for log
 * window on the right and the data (seats, forks and free philosophers) for canvas
 */
public class Waiter {
  private static final Waiter instance = new Waiter();
  private final Queue<String> logs = new LinkedBlockingQueue<>();

  private final Map<Integer, Integer> seatsStatuses = new ConcurrentHashMap<>();
  private final Map<Integer, Integer> forksStatuses = new ConcurrentHashMap<>();
  private final int philosophersNumber = 5;
  private volatile long startTime;
  private volatile int eatTime;

  private volatile boolean ignoreMessages = false;
  private List<Seat> seats;

  private List<Philosopher> philosophers;

  static {
    instance.updateForksSeatsPhilosophers();
  }

  private Waiter() {}

  public static Waiter getInstance() {
    return instance;
  }

  /**
   * A philosopher tries to take both forks simultaneously. Returns true if success or false else.
   * Updates data for canvas about acquired forks and adds logs about it
   */
  public synchronized boolean notifyAndTryGrabBothForks(Seat seat, int philosopherNumber) {
    if (seat.getLeftFork().isFree() && seat.getRightFork().isFree()) {
      seat.getLeftFork().grab();
      seat.getRightFork().grab();

      var message =
          String.format(
              "Both forks (%d, %d) were grabbed by philosopher %d",
              seat.getNumber(), seat.getNumber(), philosopherNumber);
      if (!ignoreMessages) {
        logs.add(message);
        updateForkStatus(seat.getNumber(), philosopherNumber);
        updateForkStatus(seat.getNumber(), philosopherNumber);
      }

      return true;
    }
    return false;
  }

  /**
   * A philosopher vacates both forks simultaneously. Updates data for canvas about released forks
   * and adds logs about it
   */
  public void notifyAndReleaseBothForks(Seat seat, int philosopherNumber) {

    var message =
        String.format(
            "Both forks (%d, %d) were released by philosopher %d",
            seat.getNumber(), seat.getNumber(), philosopherNumber);
    if (!ignoreMessages) {
      logs.add(message);
      updateForkStatus(seat.getNumber(), 0);
      updateForkStatus(seat.getNumber(), 0);
    }

    seat.getLeftFork().release();
    seat.getRightFork().release();
  }

  /** A philosopher takes free random seat. Returns this seat */
  public synchronized Seat takeFreeRandomSeat() throws Exception {
    var packedFreeSeat = seats.stream().filter(Seat::isFree).findAny();
    if (packedFreeSeat.isPresent()) {
      var unpackedSeat = packedFreeSeat.get();
      unpackedSeat.setFree(false);

      return unpackedSeat;

    } else throw new Exception("There are not free seats");
  }

  /** Refreshes forks, seats and philosophers */
  private void updateForksSeatsPhilosophers() {
    var forks =
        getRange(philosophersNumber)
            .map(
                i -> {
                  forksStatuses.put(i + 1, 0);
                  return new Fork(i + 1);
                })
            .toList();
    seats =
        getRange(philosophersNumber)
            .map(
                i -> {
                  seatsStatuses.put(i + 1, 0);
                  return new Seat(forks.get(i), forks.get((i + 1) % philosophersNumber));
                })
            .toList();
    philosophers =
        getRange(philosophersNumber)
            .map(
                i -> {
                  var philosopher = new Philosopher(i + 1);
                  philosopher.setDaemon(true);
                  return philosopher;
                })
            .toList();
  }

  /** Starts philosophers */
  public void start() {
    ignoreMessages = false;
    philosophers.forEach(Thread::start);
    startTime = System.currentTimeMillis();
    logs.add("Threads were started");
  }

  /** Pauses philosophers */
  public void pause() throws InterruptedException {
    for (Thread philosopher : philosophers) {
      //noinspection removal
      philosopher.suspend();
    }
    logs.add("Threads were paused");
  }

  /** Resumes philosophers */
  public void c0ntinue() {
    //noinspection removal
    philosophers.forEach(Thread::resume);
    logs.add("Threads were continued");
  }

  /** Shutdowns philosophers */
  public void stop(boolean paused) {
    ignoreMessages = true;
    if (paused) c0ntinue();
    philosophers.forEach(Thread::interrupt);
    logs.add("Threads were stopped");
    updateForksSeatsPhilosophers();
  }

  /** Updates seats data for canvas */
  public void updateSeatStatus(int seatNumber, int philosophersNumber) {
    seatsStatuses.put(seatNumber, philosophersNumber);
  }

  /** Updates forks data for canvas */
  public void updateForkStatus(int forkNumber, int philosophersNumber) {
    forksStatuses.put(forkNumber, philosophersNumber);
  }

  /** Returns range (integer stream) from 0 inclusive to "to" exclusive */
  private Stream<Integer> getRange(int to) {
    return IntStream.range(0, to).boxed();
  }

  public int getEatTime() {
    return eatTime;
  }

  public void setEatTime(int eatTime) {
    this.eatTime = eatTime;
  }

  /** Returns and removes the first log in logs query */
  public String getLog() {
    return logs.poll();
  }

  /** Returns time elapsed after invoking "start" function */
  public double getElapsedTime() {
    var delta = System.currentTimeMillis() - startTime;
    return delta / 1000.;
  }

  /** Returns map of seats numbers and corresponding philosophers numbers (0 if he's null) */
  public Map<Integer, Integer> getSeatsStatuses() {
    return seatsStatuses;
  }

  /** Returns map of forks numbers and corresponding philosophers numbers (0 if he's null) */
  public Map<Integer, Integer> getForksStatuses() {
    return forksStatuses;
  }

  public List<Integer> getThinkingPhilosophersNumbers() {
    return philosophers.stream()
        .filter(x -> x.getSeat() == null || x.getSeat().isFree())
        .map(Philosopher::getNumber)
        .toList();
  }

  /** Defines if either program should ignore new log messages and canvas data updates */
  public boolean isIgnoreMessages() {
    return ignoreMessages;
  }
}
