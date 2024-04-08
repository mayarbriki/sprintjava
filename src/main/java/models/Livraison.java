package models;

import java.sql.Date;

public class Livraison {

    private int id;
    private String adresseLiv;
    private String description;
    private String etat;
    private Date dateLiv;
//    private User livreur;
    private String  matricule;
//  private Commande reference;
//    private boolean sent;

    public Livraison() {
    }

    public Livraison(int id, String aresseLiv, String description, String etat, Date dateLiv, String  matricule) {
        this.id = id;
        this.adresseLiv = aresseLiv;
        this.description = description;
        this.etat = etat;
        this.dateLiv = dateLiv;
        this.matricule = matricule;
    }

    public Livraison(int id, String aresseLiv, String description, String etat, Date dateLiv, User livreur, String  matricule, boolean sent) {
        this.id = id;
        this.adresseLiv = aresseLiv;
        this.description = description;
        this.etat = etat;
        this.dateLiv = dateLiv;
//        this.livreur = livreur;
        this.matricule = matricule;
//        this.sent = sent;
    }

    public Livraison(String aresseLiv, String description, String etat, Date dateLiv, User livreur, String  matricule, boolean sent) {
        this.adresseLiv = aresseLiv;
        this.description = description;
        this.etat = etat;
        this.dateLiv = dateLiv;
//        this.livreur = livreur;
        this.matricule = matricule;
//        this.sent = sent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdresseLiv() {
        return adresseLiv;
    }

    public void setAdresseLiv(String aresseLiv) {
        this.adresseLiv = aresseLiv;
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
        return dateLiv;
    }

    public void setDateLiv(Date dateLiv) {
        this.dateLiv = dateLiv;
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
                ", aresseLiv='" + adresseLiv + '\'' +
                ", description='" + description + '\'' +
                ", etat='" + etat + '\'' +
                ", dateLiv=" + dateLiv +
//                ", livreur=" + livreur +
                ", matricule=" + matricule +
//                ", sent=" + sent +
                '}';
    }
}
