package Application.view;

import Application.controller.InventoryController;
import Application.model.Collectible;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class InventorySummaryView {

    private final InventoryController controller;
    private final VBox layout;

    private final Label lblTotal;
    private final Label lblAutographed;
    private final Label lblAverage;
    private final Label lblTotalValue;
    private final Label lblOldest;
    private final Label lblNewest;
    private final Label lblMostExpensive;
    private final Label lblLeastExpensive;
    private final Label lblPriceRange;
    private final Label lblTopOwner;
    private final Label lblUniqueOwners;
    private final Label lblTopAutographed;

    public InventorySummaryView(InventoryController controller) {
        this.controller = controller;

        lblTotal = new Label();
        lblAutographed = new Label();
        lblAverage = new Label();
        lblTotalValue = new Label();
        lblOldest = new Label();
        lblNewest = new Label();
        lblMostExpensive = new Label();
        lblLeastExpensive = new Label();
        lblPriceRange = new Label();
        lblTopOwner = new Label();
        lblUniqueOwners = new Label();
        lblTopAutographed = new Label();

        for (Label lbl : new Label[]{lblTotal, lblAutographed, lblAverage, lblTotalValue, lblOldest, lblNewest,
                lblMostExpensive, lblLeastExpensive, lblPriceRange, lblTopOwner, lblUniqueOwners, lblTopAutographed}) {
            lbl.setFont(Font.font("Segoe UI", 16));
        }

        layout = createSummaryLayout();
        updateSummary();

        // Dynamically update summary when inventory changes
        controller.getCollectibles().addListener((ListChangeListener<Collectible>) c -> updateSummary());
    }

    public VBox getLayout() {
        return layout;
    }

    private VBox createSummaryLayout() {
        Label lblTitle = new Label("📊 Inventory Summary");
        lblTitle.setFont(Font.font("Segoe UI Semibold", 20));
        lblTitle.setTextFill(Color.web("#2E4053"));

        VBox vbox = new VBox(10, lblTitle, new Separator(),
                lblTotal, lblAutographed, lblAverage, lblTotalValue,
                lblOldest, lblNewest, lblMostExpensive, lblLeastExpensive,
                lblPriceRange,lblTopOwner, lblUniqueOwners, lblTopAutographed);

        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_LEFT);

        // Print Summary Button at bottom right
        Button btnPrint = new Button("Print Summary");
        btnPrint.setFont(Font.font("Segoe UI", 14));
        btnPrint.setStyle("-fx-background-color: #2C3E50; -fx-text-fill: white; -fx-padding: 6 12; -fx-background-radius: 4;");
        btnPrint.setOnMouseEntered(e -> btnPrint.setStyle("-fx-background-color: #1B2B3A; -fx-text-fill: white; -fx-padding: 6 12; -fx-background-radius: 4;"));
        btnPrint.setOnMouseExited(e -> btnPrint.setStyle("-fx-background-color: #2C3E50; -fx-text-fill: white; -fx-padding: 6 12; -fx-background-radius: 4;"));
        btnPrint.setOnAction(e -> printSummary());

        HBox btnBox = new HBox(btnPrint);
        btnBox.setAlignment(Pos.BOTTOM_RIGHT);
        btnBox.setPadding(new Insets(10, 0, 0, 0));

        vbox.getChildren().add(btnBox);

        return vbox;
    }

    private void updateSummary() {
        var list = controller.getCollectibles();

        // Basic metrics
        lblTotal.setText("Total Items: " + list.size());

        long autographedCount = list.stream().filter(Collectible::isAutographed).count();
        double autographedPercent = list.isEmpty() ? 0 : autographedCount * 100.0 / list.size();
        lblAutographed.setText(String.format("Autographed Items: %d (%.1f%%)", autographedCount, autographedPercent));

        lblAverage.setText(String.format("Average Starting Price: $%.2f", list.stream()
                .mapToDouble(Collectible::getStartingPrice).average().orElse(0)));

        double totalValue = list.stream().mapToDouble(Collectible::getStartingPrice).sum();
        lblTotalValue.setText(String.format("Total Inventory Value: $%.2f", totalValue));

        // Oldest & Newest Items
        lblOldest.setText("Oldest Item: " + list.stream()
                .min(Comparator.comparing(Collectible::getPurchaseDate))
                .map(c -> c.getName() + " (" + c.getPurchaseDate() + ")")
                .orElse("N/A"));

        lblNewest.setText("Newest Item: " + list.stream()
                .max(Comparator.comparing(Collectible::getPurchaseDate))
                .map(c -> c.getName() + " (" + c.getPurchaseDate() + ")")
                .orElse("N/A"));

        // Most & Least Expensive Items
        lblMostExpensive.setText("Most Expensive Item: " + list.stream()
                .max(Comparator.comparing(Collectible::getStartingPrice))
                .map(c -> c.getName() + " ($" + c.getStartingPrice() + ")")
                .orElse("N/A"));

        lblLeastExpensive.setText("Least Expensive Item: " + list.stream()
                .min(Comparator.comparing(Collectible::getStartingPrice))
                .map(c -> c.getName() + " ($" + c.getStartingPrice() + ")")
                .orElse("N/A"));

        // Price Range
        double maxPrice = list.stream().mapToDouble(Collectible::getStartingPrice).max().orElse(0);
        double minPrice = list.stream().mapToDouble(Collectible::getStartingPrice).min().orElse(0);
        lblPriceRange.setText(String.format("Price Range: $%.2f - $%.2f", minPrice, maxPrice));

        // Owner with Most Items
        var ownerCounts = list.stream().collect(Collectors.groupingBy(Collectible::getOwner, Collectors.counting()));
        String topOwner = ownerCounts.entrySet().stream()
                .max(Comparator.comparingLong(e -> e.getValue()))
                .map(e -> e.getKey() + " (" + e.getValue() + ")")
                .orElse("N/A");
        lblTopOwner.setText("Owner with Most Items: " + topOwner);

        // Unique Owners
        Set<String> owners = list.stream().map(Collectible::getOwner).collect(Collectors.toSet());
        lblUniqueOwners.setText("Unique Owners: " + owners.size());

        // Highest Priced Autographed Item
        String topAutographed = list.stream()
                .filter(Collectible::isAutographed)
                .max(Comparator.comparing(Collectible::getStartingPrice))
                .map(c -> c.getName() + " ($" + c.getStartingPrice() + ")")
                .orElse("N/A");
        lblTopAutographed.setText("Highest Priced Autographed Item: " + topAutographed);
    }

    private void printSummary() {
        var list = controller.getCollectibles();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = dtf.format(LocalDateTime.now());

        StringBuilder sb = new StringBuilder();
        sb.append("\n===== INVENTORY SUMMARY =====\n");
        sb.append("Generated On: ").append(timestamp).append("\n\n");

        sb.append(lblTotal.getText()).append("\n");
        sb.append(lblAutographed.getText()).append("\n");
        sb.append(lblAverage.getText()).append("\n");
        sb.append(lblTotalValue.getText()).append("\n");
        sb.append(lblOldest.getText()).append("\n");
        sb.append(lblNewest.getText()).append("\n");
        sb.append(lblMostExpensive.getText()).append("\n");
        sb.append(lblLeastExpensive.getText()).append("\n");
        sb.append(lblPriceRange.getText()).append("\n");
        sb.append(lblTopOwner.getText()).append("\n");
        sb.append(lblUniqueOwners.getText()).append("\n");
        sb.append(lblTopAutographed.getText()).append("\n");

        sb.append("=============================\n");

        // Print to console
        System.out.println(sb.toString());

        // Save to file
        String filename = "Inventory_summary.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
