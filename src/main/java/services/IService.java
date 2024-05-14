package services;


import models.reclamation;
import models.reponse;


import java.sql.SQLException;
import java.util.List;

public interface IService<T> {


    void ajouter(T t) throws SQLException;
    void modifierT(T t) throws SQLException;




    void ajouterT(T t, int livreur_id) throws SQLException;

    void modifierT(T t, int livreur_id) throws SQLException;
    void supprimerT(int id) throws SQLException;
    List<T> recupererT(int livreur_id) throws SQLException;
    List<T> recupererT() throws SQLException;

    List<String> Matriculescombobox(int livreur_id) throws SQLException;

   // void ajouter(T t) throws SQLException;

    void modifier(T t) throws SQLException;

    void supprimer(int id) throws SQLException;
    List<T> recuperer() throws SQLException;
}
