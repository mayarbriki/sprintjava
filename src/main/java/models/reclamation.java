package models;

import java.util.Date;

public class reclamation {

    private int id;
    private String titre, description_r, status_r;
    private Date date_r;

    public reclamation(int id, String titre, String description_r, String status_r, Date date_r) {
        this.id = id;
        this.titre = titre;
        this.description_r = description_r;
        this.status_r = status_r;
        this.date_r = date_r;
    }

    public reclamation() {
    }

    public reclamation(String titre, String description_r, String status_r, Date date_r) {
        this.titre = titre;
        this.description_r = description_r;
        this.status_r = status_r;
        this.date_r = date_r;
    }

    public  int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public  String getDescription_r() {
        return description_r;
    }

    public void setDescription_r(String description_r) {
        this.description_r = description_r;
    }

    public  String getStatus_r() {
        return status_r;
    }

    public void setStatus_r(String status_r) {
        this.status_r = status_r;
    }

    public  java.sql.Date getDate_r() {
        return (java.sql.Date) date_r;
    }

    public void setDate_r(Date date_r) {
        this.date_r = date_r;
    }

    @Override
    public String toString() {
        return "reclamation{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description_r='" + description_r + '\'' +
                ", status_r='" + status_r + '\'' +
                ", date_r=" + date_r +
                '}';
    }
}

