package models;

public class fournisseur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private int telephone;
    private String ville ;

    public fournisseur(String email) {
        this.email = email;
    }

    public fournisseur(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
