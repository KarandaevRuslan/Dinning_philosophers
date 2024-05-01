package program.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

/** Resizable canvas */
public class CanvasPane extends Pane {

  protected final Canvas canvas;

  public CanvasPane(double width, double height) {
    setWidth(width);
    setHeight(height);
    setStyle("-fx-background-color: grey;");
    canvas = new Canvas(width, height);
    getChildren().add(canvas);
    canvas.widthProperty().bind(this.widthProperty());
    canvas.heightProperty().bind(this.heightProperty());
  }
}
