package program.views;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Map;

/** Resizable canvas for task (dinning philosophers problem) visualisation */
public class CanvasPaneForTaskVisualization extends CanvasPane
    implements ICanvasForTaskVisualization {
  /**
   * Coefficient representing how close table with forks and plates to the center of the canvas by
   * the Y axis. If A<1 table is higher than the center
   */
  private final double A = 0.8;

  /** Ratio of the distance between centers of table and plates to table radius */
  private final double B = 0.75;

  /** Ratio of a plates radius to table radius */
  private final double C = 0.15;

  /** Coefficient demonstrating how close thinking philosophers circles to the bottom of canvas */
  private final double D = 0.85;

  /** Ratio of a thinking philosopher circle radius to the round table radius */
  private final double E = 0.25;

  /** Coefficient demonstrating how much is distance between thinking philosophers circles */
  private final double F = 0.1;

  /**
   * Colors for each of 5 philosophers. If you want to use more than 5 philosophers (you can set
   * their number in Waiter class) add additional colors
   */
  private Color[] colors =
      new Color[] {
        Color.web("#0000FF"),
        Color.web("#FFFF00"),
        Color.web("#800080"),
        Color.web("#FFA500"),
        Color.web("#FFFFFF")
      };

  private GraphicsContext context = canvas.getGraphicsContext2D();

  public CanvasPaneForTaskVisualization(double width, double height) {
    super(width, height);
    context.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17));
  }

  @Override
  public void clear() {
    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  @Override
  public void drawTable() {
    context.setFill(Color.DARKGRAY);
    context.fillOval(
        getCenter().getX() - getTableRadius(),
        getCenter().getY() * A - getTableRadius(),
        2 * getTableRadius(),
        2 * getTableRadius());
  }

  @Override
  public void drawPlates(Map<Integer, Integer> seatsStatuses) {
    var amount = seatsStatuses.size();
    var a = 2 * Math.PI / amount;
    seatsStatuses.forEach(
        (plateNumber, philosopherNumber) -> {
          var philosopherIndex = philosopherNumber - 1;
          var plateIndex = plateNumber - 1;
          var angle = a / 2 + plateIndex * a;
          if (philosopherIndex == -1) context.setFill(Color.GREEN);
          else context.setFill(Color.RED);

          var x = getCenter().getX() + getDistanceBetweenTableAndPlateCenters() * Math.cos(angle);
          var y =
              getCenter().getY() * A + getDistanceBetweenTableAndPlateCenters() * Math.sin(angle);

          context.fillOval(
              x - getPlateRadius(),
              y - getPlateRadius(),
              2 * getPlateRadius(),
              2 * getPlateRadius());
          if (philosopherIndex >= 0)
            drawText(String.valueOf(philosopherNumber), colors[philosopherIndex], x, y);
        });
  }

  @Override
  public void drawForks(Map<Integer, Integer> forksStatuses) {
    var amount = forksStatuses.size();
    var a = 2 * Math.PI / amount;
    forksStatuses.forEach(
        (forkNumber, philosopherNumber) -> {
          var philosopherIndex = philosopherNumber - 1;
          var forkIndex = forkNumber - 1;
          var angle = forkIndex * a;
          if (philosopherIndex == -1) context.setStroke(Color.GREEN);
          else context.setStroke(Color.RED);

          var x = getCenter().getX() + getDistanceBetweenTableAndPlateCenters() * Math.cos(angle);
          var y =
              getCenter().getY() * A + getDistanceBetweenTableAndPlateCenters() * Math.sin(angle);
          context.setLineWidth(5);
          context.strokeLine(
              x - getPlateRadius() * Math.cos(angle),
              y - getPlateRadius() * Math.sin(angle),
              x + getPlateRadius() * Math.cos(angle),
              y + getPlateRadius() * Math.sin(angle));

          if (philosopherIndex >= 0)
            drawText(String.valueOf(philosopherNumber), colors[philosopherIndex], x, y);
        });
  }

  @Override
  public void drawThinkingPhilosophers(List<Integer> philosophersNumbers) {
    var philosophersNumber = philosophersNumbers.size();
    var radius = getPhilosopherRadius();
    var diameter = 2 * radius;
    var space = getSpaceLength();
    var length = philosophersNumber * diameter + philosophersNumber * space - space;
    for (int i = 0; i < philosophersNumber; i++) {
      var philosopherNumber = philosophersNumbers.get(i);
      var philosopherIndex = philosopherNumber - 1;
      context.setFill(colors[philosopherIndex]);
      var x = getCenter().getX() - length / 2 + radius + i * (space + diameter);
      var y = canvas.getHeight() * D;
      context.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
      drawText(String.valueOf(philosopherNumber), Color.BLACK, x, y);
    }
  }

  private Point2D getCenter() {
    return new Point2D(canvas.getWidth() / 2., canvas.getHeight() / 2.);
  }

  private double getTableRadius() {
    return getCenter().getY() / 2.;
  }

  private double getDistanceBetweenTableAndPlateCenters() {
    return getTableRadius() * B;
  }

  private double getPlateRadius() {
    return getTableRadius() * C;
  }

  private double getPhilosopherRadius() {
    return getTableRadius() * E;
  }

  private double getSpaceLength() {
    return getCenter().getX() * F;
  }

  private void drawText(String txt, Color fill, double x, double y) {
    context.setFill(fill);
    context.fillText(txt, x, y);
  }
}
