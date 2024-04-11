package models;

import com.mysql.cj.conf.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class Panier {
    private Produit[] cart ;

    private int id;
    private String produitNom ;
    private int produitPrix ;
    private String produitImage; // String for image path or URL
    private int quantite ;

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Panier(int id, String produitNom, int produitPrix , String produitImage ) {
        this.id = id;
        this.produitNom = produitNom;
        this.produitPrix = produitPrix;
        this.produitImage = produitImage;

    }
    public String getProduitImage() {
        return produitImage;
    }

    public String getProduitNom() {
        return produitNom;
    }

    public int getProduitPrix() {
        return produitPrix;
    }

    public void setProduitPrix(int produitPrix) {
        this.produitPrix = produitPrix;
    }

    private List<Produit> produits;
    private Personne owner;
    private Personne user;
    private Commande commande;
    private List<Commande> commandes;

    public Panier() {
        this.produits = new ArrayList<>();
        this.commandes = new ArrayList<>();
    }

    public Panier(Produit[] cart) {
        this.cart = cart;
    }

    public Panier(int size){
        cart = new Produit[size];
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

    private final ArrayList<Produit> list = new ArrayList<>();

    public void add(Produit produit) {
        list.add(produit);
    }

    public void remove(Produit produit) {
        list.remove(produit);
    }

    public Iterator<Produit> iterator() {
        return list.iterator();
    }

    public int panierSize() {
        return list.size();
    }

    public void setProduitNom(String produitNom) {

    }

    public void setProduitPrix(double produitPrix) {
    }
}
