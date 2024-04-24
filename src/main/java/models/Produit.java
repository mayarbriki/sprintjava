package models;

public class Produit {
    private int id , quantite ;
    private String nom , description , image , prix ;
    private double rating;  // Add a rating field
    public Produit(int quantite, String nom, String description , String image) {
        this.quantite = quantite;
        this.nom = nom;
        this.description = description;
        this.image=image;
    }

    public Produit(int id, int quantite, String nom, String description, String image, String prix) {
        this.id = id;
        this.quantite = quantite;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.prix = prix;
    }
    public Produit() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantite() {return quantite;}

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public Produit(int quantite, String nom, String description, String image, String prix) {
        this.quantite = quantite;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.prix = prix;
    }
    public Produit(int id, int quantite, String nom, String description, String image, String prix, double rating) {
        this.id = id;
        this.quantite = quantite;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.prix = prix;
        this.rating = rating;
    }
    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", quantite=" + quantite +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", prix='" + prix + '\'' +
                '}';
    }
    public double getRating() {
        return rating;
    }

}
