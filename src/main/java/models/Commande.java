package models;

import java.time.LocalDateTime;
import models.Panier ;
public class Commande {
    private int id;
    private Boolean sent;
    private Panier panier;
    private int totale;
    private Personne user;
    private LocalDateTime createdAt;
    private Panier pani;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean isSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public int getTotale() {
        return totale;
    }

    public void setTotale(int totale) {
        this.totale = totale;
    }

    public Personne getUser() {
        return user;
    }

    public void setUser(Personne user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Panier getPani() {
        return pani;
    }

    public void setPani(Panier pani) {
        this.pani = pani;
    }
}
