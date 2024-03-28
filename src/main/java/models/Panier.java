package models;
import utils.MyDatabase;

import java.util.ArrayList;
import java.util.List;

public class Panier {
    private int id;
    private List<Produit> produits;
    private Personne owner;
    private Personne user;
    private Commande commande;
    private List<Commande> commandes;

    public Panier() {
        this.produits = new ArrayList<>();
        this.commandes = new ArrayList<>();
    }
    public void addProduit(Produit produit) {
        if (produits == null) {
            produits = new ArrayList<>();
        }
        produits.add(produit);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Produit> getProduits() {
        return produits;
    }



    public Personne getOwner() {
        return owner;
    }

    public void setOwner(Personne owner) {
        this.owner = owner;
    }


    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        if (this.commande != null && this.commande != commande) {
            this.commande.setPanier(null);
        }
        this.commande = commande;
        if (commande != null) {
            commande.setPanier(this);
        }
    }

    public List<Commande> getCommandes() {
        return commandes;
    }



    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
