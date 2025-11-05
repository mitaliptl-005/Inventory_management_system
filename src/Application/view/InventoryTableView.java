package Application.view;

import Application.controller.InventoryController;
import Application.model.Collectible;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.converter.DoubleStringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InventoryTableView {

    private final InventoryController controller;
    private final TableView<Collectible> table;
    private final VBox layout;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public InventoryTableView(InventoryController controller) {
        this.controller = controller;
        this.table = new TableView<>();
        this.layout = createInventoryPage();
        applyTableStyles();
    }

    public VBox getLayout() { return layout; }
    public TableView<Collectible> getTable() { return table; }

    private VBox createInventoryPage() {
        table.setEditable(true);
        table.setItems(controller.getCollectibles());

        // === COLUMNS ===
        TableColumn<Collectible, String> colId = createEditableColumn("UniqueId", c -> c.getUniqueId(), (c, v) -> c.setUniqueId(v));
        TableColumn<Collectible, String> colName = createEditableColumn("Name", c -> c.getName(), (c, v) -> c.setName(v));
        TableColumn<Collectible, String> colType = createEditableColumn("Type", c -> c.getType(), (c, v) -> c.setType(v));
        TableColumn<Collectible, String> colOwner = createEditableColumn("Owner", c -> c.getOwner(), (c, v) -> c.setOwner(v));
        TableColumn<Collectible, String> colOccupation = createEditableColumn("Occupation", c -> c.getOccupation(), (c, v) -> c.setOccupation(v));
        TableColumn<Collectible, String> colCondition = createEditableColumn("Condition", c -> c.getCondition(), (c, v) -> c.setCondition(v));

        TableColumn<Collectible, String> colAutographed = new TableColumn<>("Autographed");
        colAutographed.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isAutographed() ? "Yes" : "No"));
        colAutographed.setCellFactory(TextFieldTableCell.forTableColumn());
        colAutographed.setOnEditCommit(e -> {
            String input = e.getNewValue().trim().toLowerCase();
            if (input.equals("yes") || input.equals("no")) {
                e.getRowValue().setAutographed(input.equals("yes"));
            } else {
                showAlert("Invalid Input", "Autographed must be 'Yes' or 'No'.");
                table.refresh();
            }
        });

        TableColumn<Collectible, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedDate()));
        colDate.setCellFactory(TextFieldTableCell.forTableColumn());
        colDate.setOnEditCommit(e -> {
            try {
                LocalDate.parse(e.getNewValue(), formatter);
                e.getRowValue().setPurchaseDate(e.getNewValue());
            } catch (DateTimeParseException ex) {
                showAlert("Invalid Date", "Date must be in dd-MM-yyyy format.");
                table.refresh();
            }
        });

        TableColumn<Collectible, Double> colPrice = new TableColumn<>("StartingPrice");
        colPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getStartingPrice()).asObject());
        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPrice.setOnEditCommit(e -> {
            double value = e.getNewValue();
            if (value >= 0) e.getRowValue().setStartingPrice(value);
            else {
                showAlert("Invalid Price", "Price must be positive.");
                table.refresh();
            }
        });

        table.getColumns().addAll(colId, colName, colType, colOwner, colOccupation,
                colAutographed, colDate, colCondition, colPrice);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // === HEADER LABEL ===
        Label title = new Label("📦 Inventory Table");
        title.setFont(Font.font("Segoe UI Semibold", 20));
        title.setTextFill(Color.web("#2E4053"));
        HBox header = new HBox(title);
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER_LEFT);

        // === BUTTONS ===
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(200);

            searchField.setStyle("""
        -fx-background-color: #FFFFFF;
        -fx-border-color: #2C3E50;
        -fx-border-radius: 6;
        -fx-background-radius: 6;
        -fx-padding: 6 10;
        -fx-font-size: 14px;
        -fx-prompt-text-fill: #7F8C8D;
    """);

        Button searchBtn = createButton("Search");
        Button refreshBtn = createButton("Refresh");
        Button addBtn = createButton("Add Item");
        Button deleteBtn = createButton("Delete");
        Button saveBtn = createButton("Save");

        searchBtn.setOnAction(e -> searchInventory(searchField));
        refreshBtn.setOnAction(e -> table.setItems(controller.getCollectibles()));
        addBtn.setOnAction(e -> addItem());
        deleteBtn.setOnAction(e -> deleteItem());
        saveBtn.setOnAction(e -> saveInventory());

        HBox leftBox = new HBox(10, searchField, searchBtn, refreshBtn);
        HBox rightBox = new HBox(10, addBtn, deleteBtn, saveBtn);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(leftBox, spacer, rightBox);
        topBar.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox vbox = new VBox(header, topBar, scrollPane);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        return vbox;
    }

    // Helper to create editable String column
    private TableColumn<Collectible, String> createEditableColumn(String title, java.util.function.Function<Collectible, String> getter, java.util.function.BiConsumer<Collectible, String> setter) {
        TableColumn<Collectible, String> col = new TableColumn<>(title);
        col.setCellValueFactory(data -> new SimpleStringProperty(getter.apply(data.getValue())));
        col.setCellFactory(TextFieldTableCell.forTableColumn());
        col.setOnEditCommit(e -> setter.accept(e.getRowValue(), e.getNewValue()));
        return col;
    }

    private Button createButton(String text) {
        Button b = new Button(text);
        String normal = "-fx-background-color: #2C3E50; -fx-text-fill: white; -fx-font-weight: 600; -fx-background-radius: 4; -fx-padding: 6 14; -fx-cursor: hand;";
        String hover = "-fx-background-color: #1B2B3A; -fx-text-fill: white; -fx-font-weight: 600; -fx-background-radius: 4; -fx-padding: 6 14; -fx-cursor: hand;";
        b.setStyle(normal);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(normal));
        return b;
    }

    private void applyTableStyles() {
        table.setStyle("""
            -fx-control-inner-background: #F4F6F7;
            -fx-background-color: #F4F6F7;
            -fx-border-color: #2C3E50;
            -fx-border-width: 0.5;
            -fx-table-cell-border-color: #D5DBDB;
            -fx-table-header-background: #2C3E50;
            -fx-table-header-border-color: #2C3E50;
            -fx-font-weight: bold;
            -fx-text-fill: white;
            -fx-selection-bar: #AED6F1;
            -fx-selection-bar-non-focused: #D6EAF8;
        """);

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Collectible item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setStyle("");
                else if (getIndex() % 2 == 0) setStyle("-fx-background-color: #FFFFFF;");
                else setStyle("-fx-background-color: #F4F6F7;");

                hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                    if (isNowHovered) setStyle("-fx-background-color: #D6EAF8;");
                    else if (getIndex() % 2 == 0) setStyle("-fx-background-color: #FFFFFF;");
                    else setStyle("-fx-background-color: #F4F6F7;");
                });
            }
        });
    }

    private void searchInventory(TextField searchField) {
        String keyword = searchField.getText().trim();
        table.setItems(keyword.isEmpty() ? controller.getCollectibles() : controller.search(keyword));
    }

    private void addItem() {
        Collectible newItem = AddItemPopup.show();
        if (newItem != null) {
            controller.addItem(newItem);
            showAlert("Success", "Item added successfully.");
        }
    }

    private void deleteItem() {
        Collectible selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait().ifPresent(type -> {
                if (type == ButtonType.YES) {
                    controller.deleteItem(selected);
                    showAlert("Deleted", "Item deleted successfully.");
                }
            });
        } else showAlert("No Selection", "Please select an item to delete.");
    }

    private void saveInventory() {
        controller.saveToCSV();
        showAlert("Saved", "Changes saved to CSV.");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
