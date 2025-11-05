package Application.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Collectible {

    private String uniqueId;
    private String name;
    private String type;
    private String owner;
    private String occupation;
    private boolean autographed;
    private LocalDate purchaseDate;
    private String condition;
    private double startingPrice;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Collectible(String uniqueId, String name, String type, String owner, String occupation,
                       boolean autographed, String purchaseDate, String condition, double startingPrice) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.occupation = occupation;
        this.autographed = autographed;
        this.purchaseDate = LocalDate.parse(purchaseDate, formatter);
        this.condition = condition;
        this.startingPrice = startingPrice;
    }

    // --- Getters ---
    public String getUniqueId() { return uniqueId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getOwner() { return owner; }
    public String getOccupation() { return occupation; }
    public boolean isAutographed() { return autographed; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public String getCondition() { return condition; }
    public double getStartingPrice() { return startingPrice; }

    // --- Setters ---
    public void setUniqueId(String uniqueId) { this.uniqueId = uniqueId; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public void setAutographed(boolean autographed) { this.autographed = autographed; }
    public void setPurchaseDate(String purchaseDate) { this.purchaseDate = LocalDate.parse(purchaseDate, formatter); }
    public void setCondition(String condition) { this.condition = condition; }
    public void setStartingPrice(double startingPrice) { this.startingPrice = startingPrice; }

    public String getFormattedDate() {
        return purchaseDate.format(formatter);
    }
}
