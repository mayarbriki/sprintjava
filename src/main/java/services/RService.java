package services;

import java.sql.SQLException;
import java.util.List;

public interface RService<T> {

    void ajouterR(T t) throws SQLException;
    void modifierTR(T t) throws SQLException;



    void supprimerR(int id) throws SQLException;
    List<T> recupererR() throws SQLException;
}
