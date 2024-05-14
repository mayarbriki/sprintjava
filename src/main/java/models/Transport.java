package models;

import java.sql.Date;

public class Transport {

    private int id;
    private String type, marque, matricule, etat;
    private Date anneefab;
    private int livreur_id;
    private String livreur;
    private Livraison livraison;


    public Transport() {
    }

    public Transport(int id, String type, String marque, String matricule, String etat, Date anneefab, int livreur_id, String livreur, Livraison livraison) {
        this.id = id;
        this.type = type;
        this.marque = marque;
        this.matricule = matricule;
        this.etat = etat;
        this.anneefab = anneefab;
        this.livreur_id = livreur_id;
        this.livreur = livreur;
        this.livraison = livraison;
    }

    public Transport(int id, String type, String marque, String matricule, String etat, Date anneefab, int livreur_id) {
        this.id = id;
        this.type = type;
        this.marque = marque;
        this.matricule = matricule;
        this.etat = etat;
        this.anneefab = anneefab;
        this.livreur_id = livreur_id;
    }

    public Transport(int id, String type, String marque, String matricule, String etat, Date anneefab) {
        this.id = id;
        this.type = type;
        this.marque = marque;
        this.matricule = matricule;
        this.etat = etat;
        this.anneefab = anneefab;
    }

    public Transport(String type, String marque, String matricule, String etat, Date anneefab) {
        this.type = type;
        this.marque = marque;
        this.matricule = matricule;
        this.etat = etat;
        this.anneefab = anneefab;
    }

    public Transport(String type, String marque, String matricule, String etat, Date anneefab, int livreur_id) {
        this.type = type;
        this.marque = marque;
        this.matricule = matricule;
        this.etat = etat;
        this.anneefab = anneefab;
        this.livreur_id = livreur_id;
    }

    public Transport(String transportMatricule) {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Date getAnneefab() {
        return anneefab;
    }

    public void setAnneefab(Date anneefab) {
        this.anneefab = anneefab;
    }

    public String getLivreur() {
        return livreur;
    }

    public void setLivreur(String livreur) {
        this.livreur = livreur;
    }

    public int getLivreur_id() {
        return livreur_id;
    }

    public void setLivreur(int livreur_id) {
        this.livreur_id = livreur_id;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", marque='" + marque + '\'' +
                ", matricule='" + matricule + '\'' +
                ", etat='" + etat + '\'' +
                ", anneefab=" + anneefab +
                ", livreur=" + livreur_id +
                '}';
    }
}
