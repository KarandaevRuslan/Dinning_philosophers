package program.views;

import java.util.List;
import java.util.Map;

/** Interface for access to CanvasPaneForTaskVisualization methods */
public interface ICanvasForTaskVisualization {
  /** Clears canvas */
  void clear();

  /** Draws round table in the area near the center of the canvas */
  void drawTable();

  /** Draws plates for 5 philosophers */
  void drawPlates(Map<Integer, Integer> platesStatuses);

  /** Draws forks for 5 philosophers */
  void drawForks(Map<Integer, Integer> forksStatuses);

  /** Draws non-eating philosophers close to the bottom of the canvas */
  void drawThinkingPhilosophers(List<Integer> philosophersNumbers);
}
