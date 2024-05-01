package program;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/** Singleton class which method(s) load(s) fxml and return(s) view(s) */
public class ComponentFactory {

  private static final ComponentFactory instance = new ComponentFactory();

  private ComponentFactory() {}

  public static ComponentFactory getInstance() {
    return instance;
  }

  public Parent createMainWindow() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemResource("MainWindow.fxml"));
    return fxmlLoader.load();
  }
}
