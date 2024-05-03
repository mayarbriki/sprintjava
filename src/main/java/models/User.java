package models;

public class User {

    private int id, age;
    private String nom, prenom, password , role;

    public User(int id, int age, String nom,String prenom, String password, String role) {
        this.id = id;
        this.age = age;
        this.nom = nom;
        this.prenom = prenom;
        this.password= password;
        this.role = role;
    }

    public User(int age, String nom, String prenom, String password, String role) {
        this.age = age;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.role = role;
    }

    public User(int age, String nom, String prenom) {
        this.age = age;
        this.nom = nom;
        this.prenom = prenom;
        this.password=password;
    }


    public User() {

    }


    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", age=" + age +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
