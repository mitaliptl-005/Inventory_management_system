package Application.view;

import Application.controller.InventoryController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;


public class InventoryView {

    private final InventoryController controller;

    public InventoryView(InventoryController controller) {
        this.controller = controller;
    }

    public void show(Stage stage) {
        stage.setTitle("Auction House Inventory - DASHBOARD");
        stage.getIcons().add(new Image(getClass().getResource("/Application/Image/logo.png").toExternalForm()));

        // =========================
        // HEADER
        // =========================
        ImageView userIcon = new ImageView(new Image("Application/Image/user.png"));
        userIcon.setFitWidth(26);
        userIcon.setFitHeight(26);

        Label userLabel = new Label("User: Mitali Patel (ADMINISTRATOR)");
        userLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        userLabel.setTextFill(Color.web("#2E4053"));

        HBox userInfoBox = new HBox(10, userIcon, userLabel);
        userInfoBox.setAlignment(Pos.CENTER_LEFT);

        ImageView signOutIcon = new ImageView(new Image("Application/Image/logout.png"));
        signOutIcon.setFitWidth(25);
        signOutIcon.setFitHeight(25);

        Button signOutBtn = new Button();
        signOutBtn.setGraphic(signOutIcon);
        signOutBtn.setPrefSize(40, 40);
        signOutBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        signOutBtn.setOnAction(e -> {

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Sign Out Confirmation");
            confirm.setHeaderText("Are you sure you want to sign out?");
            confirm.setContentText("Click YES to return to Login screen.");

            ButtonType yesBtn = new ButtonType("Yes");
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            confirm.getButtonTypes().setAll(yesBtn, cancelBtn);

            confirm.showAndWait().ifPresent(response -> {
                if (response == yesBtn) {
                    // Send back to LoginView
                    LoginView loginView = new LoginView(stage);
                    loginView.show();
                }
            });
        });


        BorderPane header = new BorderPane();
        header.setLeft(userInfoBox);
        header.setRight(signOutBtn);
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #D5D8DC;
            -fx-effect: dropshadow(two-pass-box, rgba(0,0,0,0.08), 8, 0, 0, 2);
        """);

        // =========================
        // SIDEBAR
        // =========================
        Button btnHome = createSidebarButton("🏠 Home");
        Button btnInventory = createSidebarButton("📦 Inventory Table");
        Button btnSummary = createSidebarButton("📊 Summary");

        VBox sidebar = new VBox(15, btnHome, btnInventory, btnSummary);
        sidebar.setPadding(new Insets(25));
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #2E4053;");
        sidebar.setAlignment(Pos.TOP_LEFT);

        // =========================
        // MAIN PAGES
        // =========================
        StackPane mainContent = new StackPane();
        mainContent.setPadding(new Insets(15));

        // Home page
        VBox homePage = createHomePage();

        // Inventory page
        InventoryTableView inventoryTableView = new InventoryTableView(controller);
        VBox inventoryPage = inventoryTableView.getLayout();

        // Summary page
        InventorySummaryView summaryView = new InventorySummaryView(controller);
        VBox summaryPage = summaryView.getLayout();

        mainContent.getChildren().addAll(homePage, inventoryPage, summaryPage);
        inventoryPage.setVisible(false);
        summaryPage.setVisible(false);

        // =========================
        // NAVIGATION WITH ACTIVE HIGHLIGHT
        // =========================
        btnHome.setOnAction(e -> {
            switchPage(homePage, inventoryPage, summaryPage);
            setActiveButton(btnHome, btnInventory, btnSummary);
        });
        btnInventory.setOnAction(e -> {
            switchPage(inventoryPage, homePage, summaryPage);
            setActiveButton(btnInventory, btnHome, btnSummary);
        });
        btnSummary.setOnAction(e -> {
            switchPage(summaryPage, homePage, inventoryPage);
            setActiveButton(btnSummary, btnHome, btnInventory);
        });

        setActiveButton(btnHome, btnInventory, btnSummary);

        // =========================
        // ROOT LAYOUT
        // =========================
        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setLeft(sidebar);
        root.setCenter(mainContent);
        root.setStyle("-fx-background-color: #c3c4d1;");

        Scene scene = new Scene(root, 1200, 650);
        stage.setScene(scene);
        stage.show();
    }

    // =========================
    // HOME PAGE
    // =========================
    private VBox createHomePage() {
        Image logoImage = new Image("Application/Image/logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(40);
        logoView.setFitHeight(40);

        Label homeTitle = new Label("Welcome to Auction Inventory Dashboard");
        homeTitle.setGraphic(logoView);
        homeTitle.setGraphicTextGap(10);
        homeTitle.setFont(Font.font("Segoe UI Semibold", 24));
        homeTitle.setTextFill(Color.web("#2E4053"));

        Label homeDesc = new Label("""
            Manage all your auction items, view inventory details, and analyze statistics—all in one place.
        """);
        homeDesc.setFont(Font.font("Segoe UI", 14));
        homeDesc.setTextFill(Color.web("#2E4053"));
        homeDesc.setWrapText(true);
        homeDesc.setMaxWidth(600);
        homeDesc.setAlignment(Pos.CENTER);

        VBox homePage = new VBox(15, homeTitle, homeDesc);
        homePage.setAlignment(Pos.CENTER);
        return homePage;
    }

    // =========================
    // UTILITIES
    // =========================
    private Button createSidebarButton(String text) {
        Button b = new Button(text);
        b.setPrefWidth(150);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-font-weight: bold;
            -fx-cursor: hand;
        """);
        return b;
    }

    private void switchPage(VBox show, VBox hide1, VBox hide2) {
        show.setVisible(true);
        hide1.setVisible(false);
        hide2.setVisible(false);
    }

    private void setActiveButton(Button active, Button... others) {
        active.setStyle("""
            -fx-background-color: #34495E;
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-font-weight: bold;
            -fx-cursor: hand;
        """);
        for (Button b : others) {
            b.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: white;
                -fx-font-size: 14;
                -fx-font-weight: bold;
            """);
        }
    }
}
