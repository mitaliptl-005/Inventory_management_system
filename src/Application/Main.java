package Application;

import Application.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView(stage);
        loginView.show(); // start with login page
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//package Application;
//
//import Application.controller.InventoryController;
//import Application.view.InventoryView;
//import javafx.application.Application;
//import javafx.stage.Stage;
//
//public class Main extends Application {
//    @Override
//    public void start(Stage stage) {
//        InventoryController controller = new InventoryController();
//        InventoryView view = new InventoryView(controller);
//        view.show(stage);
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}



