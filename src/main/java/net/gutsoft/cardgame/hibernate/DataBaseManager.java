package net.gutsoft.cardgame.hibernate;

import net.gutsoft.cardgame.entity.Account;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {

    // не логидно создавать EntityManager в каждом методе, возможные решения:
    // можно инжектить EntityManager Spring-ом (как в примере)
    // либо сделать класс бином и создавать EntityManager при создании бина (можна также инжектить как поле)
    // и закрывать в destroy-method-е close()
    // сам DataBaseManager также можно инжектить как бин в классы, которые его используют,
    // в таком случае, вероятно, методы можно/нужно делать не статическими

    // при появлении необходимости в новых видах запросов (фильтр карт по параметрам)
    // будет необходимо дописывать новые методы на каждый вид запроса с перекопмиляцией проекта
    // возможно, имеет смысл сделать методы менее универсальными, но с передачей в метод запроса в виде строки
    // тогда запросы можно будет хранить в текстовом (xml) файле

    // существует нерешенная проблема с выборкой сущностей по параметру:
    // если параметр - Integer, то его нужно непосредственно конкатинировать в строку запроса,
    // если же параметр - String, то его нужно устанавливать с помощью setParameter,
    // иначе возникают ошибки

    // можно создать публичный енум с возможными параметрами
    public static <T> T getEntityByParameter(T entity, String parameterName, Object parameterValue) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mySqlPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            if (parameterValue instanceof Integer) {
                entity = (T) entityManager.createQuery("select e from " + entity.getClass().getSimpleName() + " e where " + parameterName + " = " + (Integer)parameterValue)
                        .getSingleResult();
            } else {
                entity = (T) entityManager.createQuery("select e from " + entity.getClass().getSimpleName() + " e where " + parameterName + " = (?1)")
                        .setParameter(1, (String)parameterValue)
                        .getSingleResult();
            }
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return entity;
    }

    public static <T> List<T> getEntityListByParameter(T entity, String parameterName, Object parameterValue) {
        List<T> entityList;

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mySqlPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            if (parameterValue instanceof Integer) {
                entityList = (List<T>) entityManager.createQuery("select e from " + entity.getClass().getSimpleName() + " e where " + parameterName + " = " + (Integer)parameterValue)
                        .getResultList();
            } else {
                entityList = (List<T>) entityManager.createQuery("select e from " + entity.getClass().getSimpleName() + " e where " + parameterName + " = (?1)")
                        .setParameter(1, (String)parameterValue)
                        .getResultList();
            }
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return entityList;
    }

    public static <T> T getEntityById(T entity, int id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mySqlPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entity = (T) entityManager.find(entity.getClass(), id);
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return entity;
    }

    public static <T> T updateEntity(T entity) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mySqlPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entity = entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return entity;
    }

    public static <T> void persistEntity(T entity) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mySqlPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }


    // попробовать удаление с незакрытым entityManager - успешно => таки нужно пересмотреть работу с entityManager
    public static <T> void deleteEntity(T entity, int id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mySqlPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entity = (T) entityManager.find(entity.getClass(), id);
            System.out.println(entity);
            entityManager.remove(entity);
            entityManager.flush();
            System.out.println("after remove entity");
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

}
