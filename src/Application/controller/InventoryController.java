package Application.controller;

import Application.model.Collectible;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryController {

    private final String csvPath = "memorabilia_items.csv";
    private final String backupPath = "backup_memorabilia_items.csv";

    private boolean isCSVCorrupted = false;

    private final ObservableList<Collectible> collectibles;

    public InventoryController() {
        collectibles = FXCollections.observableArrayList();
        loadCSVWithFallback();
    }

    public ObservableList<Collectible> getCollectibles() {
        return collectibles;
    }

    public void addItem(Collectible item) {
        collectibles.add(item);
    }

    public void deleteItem(Collectible item) {
        collectibles.remove(item);
    }

    public int getTotalItems() {
        return collectibles.size();
    }

    public long getAutographedCount() {
        return collectibles.stream().filter(Collectible::isAutographed).count();
    }

    public double getAverageStartingPrice() {
        return collectibles.stream().mapToDouble(Collectible::getStartingPrice).average().orElse(0);
    }

    // ===== SEARCH FUNCTION =====
    public ObservableList<Collectible> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return collectibles;

        String lowerKeyword = keyword.toLowerCase();
        List<Collectible> results = collectibles.stream()
                .filter(c -> c.getUniqueId().toLowerCase().contains(lowerKeyword)
                        || c.getName().toLowerCase().contains(lowerKeyword)
                        || c.getType().toLowerCase().contains(lowerKeyword)
                        || c.getOwner().toLowerCase().contains(lowerKeyword)
                        || c.getOccupation().toLowerCase().contains(lowerKeyword)
                        || c.getCondition().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());

        return FXCollections.observableArrayList(results);
    }

    // ===== MAIN FALLBACK LOADING METHOD =====
    public void loadCSVWithFallback() {

        showPopup("Loading Inventory File",
                "Attempting to load:\n" + csvPath,
                Alert.AlertType.INFORMATION);

        loadCSV(csvPath);

        if (isCSVCorrupted) {

            showPopup("⚠ CORRUPTED FILE DETECTED ⚠",
                    "This file is corrupted:\n\n" + csvPath +
                            "\n\nAttempting to load backup instead:\n" + backupPath,
                    Alert.AlertType.WARNING);

            loadCSV(backupPath);

            if (!isCSVCorrupted) {
                showPopup("Backup Loaded",
                        "Successfully loaded backup file:\n" + backupPath,
                        Alert.AlertType.INFORMATION);
            } else {
                showPopup("FATAL ERROR",
                        "Both files are corrupted.\n" +
                                "Unable to load inventory.\n\n" +
                                "Main file: " + csvPath + "\n" +
                                "Backup file: " + backupPath,
                        Alert.AlertType.ERROR);
            }
        }
    }

    // ===== CSV READER WITH CORRUPTION CHECK =====
    public void loadCSV(String filePath) {
        collectibles.clear();
        isCSVCorrupted = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String header = br.readLine();
            if (header == null || !header.contains(",")) {
                throw new IOException("Header is missing or invalid.");
            }

            String line;
            while ((line = br.readLine()) != null) {

                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (data.length < 9)
                    throw new IOException("Missing fields → " + line);

                double price;
                try {
                    price = Double.parseDouble(data[8].trim());
                } catch (NumberFormatException e) {
                    throw new IOException("Starting price is not numeric → " + data[8]);
                }

                collectibles.add(new Collectible(
                        data[0].trim(),
                        data[1].replaceAll("^\"|\"$", "").trim(),
                        data[2].replaceAll("^\"|\"$", "").trim(),
                        data[3].replaceAll("^\"|\"$", "").trim(),
                        data[4].replaceAll("^\"|\"$", "").trim(),
                        data[5].equalsIgnoreCase("Yes") || data[5].equalsIgnoreCase("true"),
                        data[6].trim(),
                        data[7].replaceAll("^\"|\"$", "").trim(),
                        price
                ));
            }

        } catch (Exception e) {
            isCSVCorrupted = true;
            logError("Corrupted CSV detected: " + filePath, e);

            showPopup("ERROR — CORRUPTED FILE",
                    "The file being loaded is corrupted:\n\n" +
                            filePath +
                            "\n\nCause:\n" + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    // ===== SAVE CSV =====
    public void saveToCSV() {
        backupCSV();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath))) {
            writer.write("UniqueId,Name,Type,Owner,Occupation,Autographed,Date,Condition,StartingPrice");
            writer.newLine();

            for (Collectible c : collectibles) {
                writer.write(formatAsCSV(c));
                writer.newLine();
            }

        } catch (IOException e) {
            logError("Failed saving CSV", e);
        }
    }

    private String formatAsCSV(Collectible c) {
        return String.format("%s,\"%s\",\"%s\",\"%s\",\"%s\",%s,%s,\"%s\",%.2f",
                c.getUniqueId(), c.getName(), c.getType(), c.getOwner(), c.getOccupation(),
                c.isAutographed() ? "Yes" : "No", c.getFormattedDate(), c.getCondition(), c.getStartingPrice());
    }

    // ===== BACKUP CREATION =====
    private void backupCSV() {
        File original = new File(csvPath);
        if (!original.exists()) return;

        File backup = new File(backupPath);
        try (InputStream in = new FileInputStream(original);
             OutputStream out = new FileOutputStream(backup)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

        } catch (IOException e) {
            logError("Backup failed", e);
        }
    }

    // ===== POPUP METHOD =====
    private void showPopup(String title, String message, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // ===== ERROR LOGGING =====
    private void logError(String msg, Exception e) {
        try (BufferedWriter log = new BufferedWriter(new FileWriter("inventory_errors.log", true))) {
            log.write(LocalDateTime.now() + " - " + msg + " - " + e.getMessage());
            log.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
