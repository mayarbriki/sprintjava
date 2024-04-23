package test;

import services.ServiceReclamation;

import java.sql.SQLException;

public class Test {

    public static void main(String[] args) {
        ServiceReclamation sp = new ServiceReclamation();
        try {
//            sp.modifier(new Reclamation(1,"titre","Ben"));
//            System.out.println(sp.recuperer());
            sp.supprimer(2);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
}
