package no.pederyo.klient;

import no.pederyo.modell.Bruker;
import no.pederyo.modell.Email;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

public class BrukerTest {
    static EntityManagerFactory emf;
    static EntityManager em;
    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("hibernate");
        em = emf.createEntityManager();
        Bruker b = new Bruker();
        b.setName("pederen");

        try {
            em.getTransaction().begin();
            em.persist(b);
            em.getTransaction().commit();
        } catch (RollbackException e) {
            em.getTransaction().rollback();
        }

        Bruker personDB = em.find(Bruker.class,1);
        System.out.println(personDB.getName());
        for(Email email : personDB.getEmailsById()){
            System.out.println(email.getUsername());
        }
        em.close();
    }
}
