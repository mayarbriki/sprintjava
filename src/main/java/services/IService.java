package services;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {

    void ajouterT(T t) throws SQLException;

    void modifierT(T t) throws SQLException;
    void supprimerT(int id) throws SQLException;
    List<T> recupererT() throws SQLException;
}
