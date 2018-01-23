package no.pederyo.dataaccess;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Connect {

    static EntityManager em;
    static EntityManagerFactory emf;
    public static void opprettForbindelse() {
        emf = Persistence.createEntityManagerFactory("hibernate");
        em = emf.createEntityManager();
    }
}
