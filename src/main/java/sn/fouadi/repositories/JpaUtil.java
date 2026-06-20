package sn.fouadi.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static EntityManagerFactory emf;

    public static final String PERSISTENCE_UNIT_NAME = "bookPU";
    public static void init() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
    }

    public static EntityManager getEntityManager() {
        if (emf == null || !emf.isOpen()) {
            init();
        }
        return emf.createEntityManager();
    }

    //fermeture de la connexion
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

}
