package services;

<<<<<<< HEAD
import models.reclamation;
import models.reponse;

=======
>>>>>>> 3d3f5bdb2be7119eff6aa42a3dabc8a45d1b7048
import java.sql.SQLException;
import java.util.List;

public interface IService<T> {

<<<<<<< HEAD
    void ajouter(T t) throws SQLException;
    void modifierT(T t) throws SQLException;



=======
    void ajouterT(T t, int livreur_id) throws SQLException;

    void modifierT(T t, int livreur_id) throws SQLException;
    void supprimerT(int id) throws SQLException;
    List<T> recupererT(int livreur_id) throws SQLException;
    List<T> recupererT() throws SQLException;

    List<String> Matriculescombobox(int livreur_id) throws SQLException;

    void ajouter(T t) throws SQLException;

    void modifier(T t) throws SQLException;
>>>>>>> 3d3f5bdb2be7119eff6aa42a3dabc8a45d1b7048
    void supprimer(int id) throws SQLException;
    List<T> recuperer() throws SQLException;
}
