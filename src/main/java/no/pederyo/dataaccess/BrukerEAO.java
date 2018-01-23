package no.pederyo.dataaccess;

import no.pederyo.modell.Bruker;

import javax.persistence.*;
import java.sql.*;
import java.util.List;

public class BrukerEAO {
    public static final String DRIVER = "jdbc:sqlite:";

    public static void opprettForbindelse(EntityManager emm, EntityManagerFactory emff) {
        //emf = Persistence.createEntityManagerFactory("hibernate");
        //em = emf.createEntityManager();

    }

    private static Connection connect() {
        // SQLite connection string
        String url = DRIVER + "mailmanager.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void insertBruker(EntityManager em, Bruker bruker) {
        try {
            em.persist(bruker);
            em.getTransaction().commit();
        } catch (RollbackException e) {
            em.getTransaction().rollback();
        }
    }

    public static Bruker getBruker(EntityManager em, Integer name){
        Bruker b = em.find(Bruker.class, name);
        return b;
    }
    public static Bruker getBruker(EntityManager em, String name){
        TypedQuery<Bruker> b = em.createQuery(
                "SELECT b from Bruker b WHERE b.name = :username", Bruker.class).
                setParameter("username", name);
        if(b.getResultList().isEmpty()){
            return null;
        }
        return b.getSingleResult();
    }
}
