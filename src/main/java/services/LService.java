package services;

import java.sql.SQLException;
import java.util.List;

public interface LService<L> {

    void ajouterL(L l, int reference_id, int c) throws SQLException;

    void modifierL(L L) throws SQLException;
    void modifier_L(L L) throws SQLException;
    void supprimerL(int id) throws SQLException;
    List<L> recupererL() throws SQLException;
    List<L> recupererL(int livreur_id) throws SQLException;
}
