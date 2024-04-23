package models;

import javafx.beans.property.*;

public class Commande {
    private int panierId;
    private IntegerProperty id;
    private IntegerProperty pani_id;  // Updated property name
    private IntegerProperty total;
    private IntegerProperty userId;
    private Panier panier;

    public Commande(int id, int pani_id, int total, int userId) {
        this.id = new SimpleIntegerProperty(id);
        this.pani_id = new SimpleIntegerProperty(pani_id);
        this.total = new SimpleIntegerProperty(total);
        this.userId = new SimpleIntegerProperty(userId);
    }

    // Getters and setters for properties
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getPani_id() {  // Updated getter method name
        return pani_id.get();
    }

    public IntegerProperty pani_idProperty() {
        return pani_id;
    }

    public void setPani_id(int pani_id) {  // Updated setter method name
        this.pani_id.set(pani_id);
    }

    public int getTotal() {
        return total.get();
    }

    public IntegerProperty totalProperty() {
        return total;
    }

    public void setTotal(int total) {
        this.total.set(total);
    }
    public int getPanierId() {
        return panierId;
    }

    public int getUserId() {
        return userId.get();
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public Panier getPanier() {
        return this.panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public Commande() {
    }
}
