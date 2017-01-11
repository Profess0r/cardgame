package net.gutsoft.cardgame.util;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.entity.Battle;
import net.gutsoft.cardgame.hibernate.DataBaseManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Initializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        // это property для log4j
        // (если разместить это дальше, то log4j успевает загрузится с rootPath по умолчанию)
        System.out.println(sce.getServletContext().getRealPath("/"));
        System.setProperty("rootPath", sce.getServletContext().getRealPath("/"));

        // !!! following code prevents from:
        // java.sql.SQLException:
        // No suitable driver found for
        // jdbc:mysql://127.0.0.1:3306/cardgame_db
        // должно вызываться перед любым обращением к jdbc-драйверу
        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        // or
//            try {
//                DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }

        DataBaseManager.open();

        Account shop = new Account();
        try {
            shop = DataBaseManager.getEntityByParameter(shop,"login", "shop");
        } catch (NoResultException e) {
            System.out.println("shop not exist");
            e.printStackTrace();
        }
        sce.getServletContext().setAttribute("shop", shop);

        Map<Integer, Battle> battleMap = new HashMap<>(); // ConcurrencyHashMap<>();
        sce.getServletContext().setAttribute("battles", battleMap);

        // немного странный способ, но как иначе получить доступ к ServletContext из простого класса, я не знаю
        ServletContextHolder.setServletContext(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DataBaseManager.close();
    }
}
