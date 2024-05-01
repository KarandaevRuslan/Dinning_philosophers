module tasks.practical.java {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.xml;

  opens program to
      javafx.fxml;

  exports program;
  exports program.mainWindow;

  opens program.mainWindow to
      javafx.fxml;

  exports program.entities;

  opens program.entities to
      javafx.fxml;

  exports program.views;

  opens program.views to
      javafx.fxml;
}
