package models;

import java.sql.Date;

public class Livraison {

    private int id;
    private String adresse_liv;
    private String description;
    private String etat;
    private Date date_liv;
    private int livreur_id;
    private String livreur;
    private String matricule;
    private int reference_id;
//    private boolean sent;

    public Livraison() {
    }

    public Livraison(int id, String adresse_liv, String description, String etat, Date date_liv, int livreur_id, String livreur, String matricule, int reference_id) {
        this.id = id;
        this.adresse_liv = adresse_liv;
        this.description = description;
        this.etat = etat;
        this.date_liv = date_liv;
        this.livreur_id = livreur_id;
        this.livreur = livreur;
        this.matricule = matricule;
        this.reference_id = reference_id;
    }

    public Livraison(int id, String adresse_liv, String description, String etat, Date date_liv, int livreur_id, String matricule, int reference_id) {
        this.id = id;
        this.adresse_liv = adresse_liv;
        this.description = description;
        this.etat = etat;
        this.date_liv = date_liv;
        this.livreur_id = livreur_id;
        this.matricule = matricule;
        this.reference_id = reference_id;
    }

    public Livraison(int id, String adresse_liv, String description, String etat, Date date_liv, String  matricule) {
        this.id = id;
        this.adresse_liv = adresse_liv;
        this.description = description;
        this.etat = etat;
        this.date_liv = date_liv;
        this.matricule = matricule;
    }

    public Livraison(int id, String adresse_liv, String description, String etat, Date date_liv, User livreur, String  matricule, boolean sent) {
        this.id = id;
        this.adresse_liv = adresse_liv;
        this.description = description;
        this.etat = etat;
        this.date_liv = date_liv;
//        this.livreur = livreur;
        this.matricule = matricule;
//        this.sent = sent;
    }

    public Livraison(String adresse_liv, String description, String etat, Date date_liv, User livreur, String  matricule, boolean sent) {
        this.adresse_liv = adresse_liv;
        this.description = description;
        this.etat = etat;
        this.date_liv = date_liv;
//        this.livreur = livreur;
        this.matricule = matricule;
//        this.sent = sent;
    }

    public Livraison(int id, Date date_liv, String adresse_liv, String description, String etat) {
        this.id = id;
        this.date_liv = date_liv;
        this.adresse_liv = adresse_liv;
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
    public String  getMatricule() {
        return matricule;
    }

    public void setMatricule(String  matricule) {
        this.matricule = matricule;
    }

    public int getLivreur_id() {
        return livreur_id;
    }

    public void setLivreur_id(int livreur_id) {
        this.livreur_id = livreur_id;
    }

    public String getLivreur() {
        return livreur;
    }

    public void setLivreur(String livreur) {
        this.livreur = livreur;
    }

    public int getReference_id() {
        return reference_id;
    }

    public void setReference_id(int reference_id) {
        this.reference_id = reference_id;
    }
    @Override
    public String toString() {
        return "Livraison{" +
                "id=" + id +
                ", adresse_liv='" + adresse_liv + '\'' +
                ", description='" + description + '\'' +
                ", etat='" + etat + '\'' +
                ", date_liv=" + date_liv +
                ", livreur=" + livreur_id +
                ", matricule=" + matricule +
                ", reference=" + reference_id +
//               ", sent=" + sent +
                '}';
    }
}
