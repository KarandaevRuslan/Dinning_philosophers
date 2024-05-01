package program.businessLogic;

/** Shows some message */
public interface IMessageService {
  void showMessage(String message);

  void showWarning(String warning);

  void showError(String error);
}
