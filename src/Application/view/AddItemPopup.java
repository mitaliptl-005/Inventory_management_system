package Application.view;

import Application.model.Collectible;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddItemPopup {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static Collectible show() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add New Collectible");
        window.setMinWidth(400);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // --- INPUT FIELDS ---
        TextField idField = new TextField();
        idField.setPromptText("Unique ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField typeField = new TextField();
        typeField.setPromptText("Type");
        TextField ownerField = new TextField();
        ownerField.setPromptText("Owner");
        TextField occupationField = new TextField();
        occupationField.setPromptText("Occupation");
        TextField autographedField = new TextField();
        autographedField.setPromptText("Autographed (Yes/No)");
        TextField dateField = new TextField();
        dateField.setPromptText("Purchase Date (dd-MM-yyyy)");
        TextField conditionField = new TextField();
        conditionField.setPromptText("Condition");
        TextField priceField = new TextField();
        priceField.setPromptText("Starting Price");

        // --- ADD TO GRID ---
        grid.add(new Label("Unique ID:"), 0, 0); grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1); grid.add(nameField, 1, 1);
        grid.add(new Label("Type:"), 0, 2); grid.add(typeField, 1, 2);
        grid.add(new Label("Owner:"), 0, 3); grid.add(ownerField, 1, 3);
        grid.add(new Label("Occupation:"), 0, 4); grid.add(occupationField, 1, 4);
        grid.add(new Label("Autographed:"), 0, 5); grid.add(autographedField, 1, 5);
        grid.add(new Label("Purchase Date:"), 0, 6); grid.add(dateField, 1, 6);
        grid.add(new Label("Condition:"), 0, 7); grid.add(conditionField, 1, 7);
        grid.add(new Label("Starting Price:"), 0, 8); grid.add(priceField, 1, 8);

        Button addBtn = new Button("Add");
        Button cancelBtn = new Button("Cancel");
        grid.add(addBtn, 0, 9);
        grid.add(cancelBtn, 1, 9);

        final Collectible[] result = {null};

        addBtn.setOnAction(e -> {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String type = typeField.getText().trim();
                String owner = ownerField.getText().trim();
                String occupation = occupationField.getText().trim();

                String autoText = autographedField.getText().trim().toLowerCase();
                if (!autoText.equals("yes") && !autoText.equals("no")) {
                    showAlert("Invalid Input", "Autographed must be 'Yes' or 'No'.");
                    return;
                }
                boolean autographed = autoText.equals("yes");

                String dateStr = dateField.getText().trim();
                LocalDate.parse(dateStr, formatter); // validate date

                String condition = conditionField.getText().trim();

                double price = Double.parseDouble(priceField.getText().trim());
                if (price < 0) throw new NumberFormatException();

                result[0] = new Collectible(id, name, type, owner, occupation, autographed, dateStr, condition, price);
                window.close();

            } catch (DateTimeParseException ex) {
                showAlert("Invalid Date", "Date must be in dd-MM-yyyy format.");
            } catch (NumberFormatException ex) {
                showAlert("Invalid Price", "Starting Price must be a positive number.");
            } catch (Exception ex) {
                showAlert("Invalid Input", "Please fill all fields correctly.");
            }
        });

        cancelBtn.setOnAction(e -> window.close());

        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.showAndWait();

        return result[0];
    }

    private static void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
