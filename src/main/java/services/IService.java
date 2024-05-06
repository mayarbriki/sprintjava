package services;

import models.reclamation;
import models.reponse;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {

    void ajouter(T t) throws SQLException;
    void modifierT(T t) throws SQLException;



    void supprimer(int id) throws SQLException;
    List<T> recuperer() throws SQLException;
}
