package models;

import javafx.beans.property.*;

public class Commande {
    private IntegerProperty id;
    private BooleanProperty sent;
    private ObjectProperty<Panier> panier;
    private IntegerProperty totale;
    private int userId;

    public Commande(int id, int totale, Panier panier) {
        this.id = new SimpleIntegerProperty(id);
        this.totale = new SimpleIntegerProperty(totale);
        this.panier = new SimpleObjectProperty<>(panier);
        this.sent = new SimpleBooleanProperty(false); // Default value
    }

    public Commande(int id, int totale, int userId, Panier panier) {
        this.id = new SimpleIntegerProperty(id);
        this.totale = new SimpleIntegerProperty(totale);
        this.userId = userId;
        this.panier = new SimpleObjectProperty<>(panier);
        this.sent = new SimpleBooleanProperty(false); // Default value
    }

    // Getters and setters for id, sent, panier, totale, and userId
    // These methods are omitted for brevity

    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty totalProperty() {
        return totale;
    }

    public BooleanProperty sentProperty() {
        return sent;
    }

    public ObjectProperty<Panier> panierProperty() {
        return panier;
    }

    public Panier getPanier() {
        return panier.get();
    }

    public void setPanier(Panier panier) {
        this.panier.set(panier);
    }

}
