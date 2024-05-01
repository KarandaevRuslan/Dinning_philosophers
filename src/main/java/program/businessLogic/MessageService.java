package program.businessLogic;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import program.Main;

/** Singleton class showing some message using Alert */
public class MessageService implements IMessageService {
  private static MessageService instance = new MessageService();

  private MessageService() {}

  public static MessageService getInstance() {
    return instance;
  }

  /** Displays message */
  @Override
  public void showMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
    setIcon(alert, Main.PATH_TO_ICON);
    alert.showAndWait();
  }

  /** Displays warning */
  @Override
  public void showWarning(String warning) {
    Alert alert = new Alert(Alert.AlertType.WARNING, warning, ButtonType.OK);
    setIcon(alert, Main.PATH_TO_ICON);
    alert.showAndWait();
  }

  /** Displays error and closes application after hitting OK button */
  @Override
  public void showError(String error) {
    Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
    setIcon(alert, Main.PATH_TO_ICON);
    alert.showAndWait();
    if (alert.getResult() == ButtonType.OK) {
      Platform.exit();
      System.exit(-1);
    }
  }

  /** Sets icon to alert */
  private void setIcon(Alert alert, String pathToIcon) {
    var stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.getIcons().add(new Image(pathToIcon));
  }
}
