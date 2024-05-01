package program;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/** Application start location */
public class Main extends Application {

  public static final String PATH_TO_ICON = "philosophy.png";

  @Override
  public void start(Stage stage) throws IOException {
    Parent root = null;
    root = ComponentFactory.getInstance().createMainWindow();
    stage.setScene(new Scene(root));
    stage.setTitle("Dinning philosophers");
    stage.getIcons().add(new Image(PATH_TO_ICON));
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
