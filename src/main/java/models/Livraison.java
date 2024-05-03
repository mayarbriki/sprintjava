package models;

import java.sql.Date;

public class Livraison {

    private int id;
    private String adresse_liv;
    private String description;
    private String etat;
    private Date date_liv;
//    private User livreur;
    private String  matricule;
//  private Commande reference;
//    private boolean sent;

    public Livraison() {
    }

    public Livraison(int id, String aresseLiv, String description, String etat, Date date_liv, String  matricule) {
        this.id = id;
        this.adresse_liv = aresseLiv;
        this.description = description;
        this.etat = etat;
        this.date_liv = date_liv;
        this.matricule = matricule;
    }

    public Livraison(int id, String aresseLiv, String description, String etat, Date date_liv, User livreur, String  matricule, boolean sent) {
        this.id = id;
        this.adresse_liv = aresseLiv;
        this.description = description;
        this.etat = etat;
        this.date_liv = date_liv;
//        this.livreur = livreur;
        this.matricule = matricule;
//        this.sent = sent;
    }

    public Livraison(String aresseLiv, String description, String etat, Date date_liv, User livreur, String  matricule, boolean sent) {
        this.adresse_liv = aresseLiv;
        this.description = description;
        this.etat = etat;
        this.date_liv = date_liv;
//        this.livreur = livreur;
        this.matricule = matricule;
//        this.sent = sent;
    }

    public Livraison(int id, Date date_liv, String adresseLiv, String description, String etat) {
        this.id = id;
        this.date_liv = date_liv;
        this.adresse_liv = adresseLiv;
        this.description = description;
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdresseLiv() {
        return adresse_liv;
    }

    public void setAdresseLiv(String aresseLiv) {
        this.adresse_liv = aresseLiv;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Date getDateLiv() {
        return date_liv;
    }

    public void setDateLiv(Date date_liv) {
        this.date_liv = date_liv;
    }

//    public User getLivreur() {
//        return livreur;
//    }
//
//    public void setLivreur(User livreur) {
//        this.livreur = livreur;
//    }

    public String  getMatricule() {
        return matricule;
    }

    public void setMatricule(String  matricule) {
        this.matricule = matricule;
    }

//    public boolean isSent() {
//        return sent;
//    }
//
//    public void setSent(boolean sent) {
//        this.sent = sent;
//    }

    @Override
    public String toString() {
        return "Livraison{" +
                "id=" + id +
                ", aresseLiv='" + adresse_liv + '\'' +
                ", description='" + description + '\'' +
                ", etat='" + etat + '\'' +
                ", date_liv=" + date_liv +
//                ", livreur=" + livreur +
                ", matricule=" + matricule +
//                ", sent=" + sent +
                '}';
    }
}
