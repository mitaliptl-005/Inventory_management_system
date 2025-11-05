package Application.view;

import Application.controller.InventoryController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView {

    private Stage stage;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Logo
        ImageView logoView;
        try {
            Image logo = new Image(getClass().getResourceAsStream("/Application/Image/logo.png"));
            logoView = new ImageView(logo);
            logoView.setFitWidth(80);
            logoView.setFitHeight(80);
        } catch (Exception e) {
            logoView = new ImageView(); // placeholder if image not found
            System.out.println("Logo not found: " + e.getMessage());
        }

        // Company name label
        Label companyLabel = new Label("AssetTrack Innovations Pvt. Ltd.");
        companyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28)); // company name bold
        companyLabel.setTextFill(Color.web("#ffffff")); // White text

        // Combine logo + text horizontally
        HBox headerBox = new HBox(15, logoView, companyLabel);
        headerBox.setAlignment(Pos.CENTER);

        // Labels and fields
        Label usernameLabel = new Label("Username:");
        usernameLabel.setTextFill(Color.web("#ffffff"));
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setFont(Font.font("Arial", 16)); // normal, not bold
        usernameField.setStyle(
                "-fx-background-color: #2C3E50; " +
                        "-fx-text-fill: #ecf0f1; " +
                        "-fx-prompt-text-fill: #bdc3c7; " +
                        "-fx-background-radius: 6; " +
                        "-fx-border-radius: 6; " +
                        "-fx-border-color: #34495E; " +
                        "-fx-border-width: 2; " +
                        "-fx-font-size: 16px;"
        );

        Label passwordLabel = new Label("Password:");
        passwordLabel.setTextFill(Color.web("#ffffff"));
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setFont(Font.font("Arial", 16)); // normal, not bold
        passwordField.setStyle(
                "-fx-background-color: #2C3E50; " +
                        "-fx-text-fill: #ecf0f1; " +
                        "-fx-prompt-text-fill: #bdc3c7; " +
                        "-fx-background-radius: 6; " +
                        "-fx-border-radius: 6; " +
                        "-fx-border-color: #34495E; " +
                        "-fx-border-width: 2; " +
                        "-fx-font-size: 16px;"
        );

        // Focus effect (glow) for text fields
        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                usernameField.setStyle(
                        "-fx-background-color: #2C3E50; " +
                                "-fx-text-fill: #ecf0f1; " +
                                "-fx-prompt-text-fill: #bdc3c7; " +
                                "-fx-background-radius: 6; " +
                                "-fx-border-radius: 6; " +
                                "-fx-border-color: #1ABC9C; " +  // teal glow
                                "-fx-border-width: 2;" +
                                "-fx-font-size: 16px;"
                );
            } else {
                usernameField.setStyle(
                        "-fx-background-color: #2C3E50; " +
                                "-fx-text-fill: #ecf0f1; " +
                                "-fx-prompt-text-fill: #bdc3c7; " +
                                "-fx-background-radius: 6; " +
                                "-fx-border-radius: 6; " +
                                "-fx-border-color: #34495E; " +
                                "-fx-border-width: 2;" +
                                "-fx-font-size: 16px;"
                );
            }
        });

        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordField.setStyle(
                        "-fx-background-color: #2C3E50; " +
                                "-fx-text-fill: #ecf0f1; " +
                                "-fx-prompt-text-fill: #bdc3c7; " +
                                "-fx-background-radius: 6; " +
                                "-fx-border-radius: 6; " +
                                "-fx-border-color: #1ABC9C; " +
                                "-fx-border-width: 2;" +
                                "-fx-font-size: 16px;"
                );
            } else {
                passwordField.setStyle(
                        "-fx-background-color: #2C3E50; " +
                                "-fx-text-fill: #ecf0f1; " +
                                "-fx-prompt-text-fill: #bdc3c7; " +
                                "-fx-background-radius: 6; " +
                                "-fx-border-radius: 6; " +
                                "-fx-border-color: #34495E; " +
                                "-fx-border-width: 2;" +
                                "-fx-font-size: 16px;"
                );
            }
        });

        // Buttons
        Button loginButton = new Button("Login");
        Button clearButton = new Button("Clear");

        loginButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 16; " +
                        "-fx-background-radius: 6;"
        );
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                "-fx-background-color: #45A049; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 16; " +
                        "-fx-background-radius: 6;"
        ));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 16; " +
                        "-fx-background-radius: 6;"
        ));

        clearButton.setStyle(
                "-fx-background-color: #f44336; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 16; " +
                        "-fx-background-radius: 6;"
        );
        clearButton.setOnMouseEntered(e -> clearButton.setStyle(
                "-fx-background-color: #e53935; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 16; " +
                        "-fx-background-radius: 6;"
        ));
        clearButton.setOnMouseExited(e -> clearButton.setStyle(
                "-fx-background-color: #f44336; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 16; " +
                        "-fx-background-radius: 6;"
        ));

        // Message label
        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.web("#ff5555"));
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Form layout
        GridPane formGrid = new GridPane();
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.add(usernameLabel, 0, 0);
        formGrid.add(usernameField, 1, 0);
        formGrid.add(passwordLabel, 0, 1);
        formGrid.add(passwordField, 1, 1);

        // Buttons below password
        HBox buttonBox = new HBox(20, loginButton, clearButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        formGrid.add(buttonBox, 1, 2);
        GridPane.setMargin(buttonBox, new Insets(10, 20, 0, 20));

        // Combine everything vertically
        VBox vbox = new VBox(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(headerBox, formGrid, messageLabel);

        // Background
        StackPane root = new StackPane(vbox);
        root.setStyle("-fx-background-color: #34495E;");
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("AssetTrack Innovations Pvt. Ltd. - Login");
        stage.show();

        // Login logic
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.equals("admin") && password.equals("password123")) {
                openInventory();
            } else {
                messageLabel.setText("Invalid username or password!");
                usernameField.clear();
                passwordField.clear();
            }
        });

        clearButton.setOnAction(e -> {
            usernameField.clear();
            passwordField.clear();
            messageLabel.setText("");
        });

        passwordField.setOnAction(e -> loginButton.fire());
    }

    private void openInventory() {
        InventoryController controller = new InventoryController();
        InventoryView view = new InventoryView(controller);
        view.show(stage);
    }
}
