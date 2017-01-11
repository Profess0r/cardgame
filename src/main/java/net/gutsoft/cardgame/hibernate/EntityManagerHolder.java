package net.gutsoft.cardgame.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerHolder {

    private EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;


    public void init() {
        System.out.println("EntityManagerHolder - init");
        entityManagerFactory = Persistence.createEntityManagerFactory("mySqlPersistenceUnit");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public void close(){
        System.out.println("EntityManagerHolder - close");
        entityManager.close();
        entityManagerFactory.close();
    }

}
