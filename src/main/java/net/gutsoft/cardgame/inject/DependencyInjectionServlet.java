package net.gutsoft.cardgame.inject;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.util.ClassName;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.lang.reflect.Field;
import java.util.List;

public class DependencyInjectionServlet extends HttpServlet {
    public static final Logger logger = Logger.getLogger(ClassName.getCurrentClassName());

    @Override
    public void init() throws ServletException {

        // !!! following code prevents from:
        // java.sql.SQLException:
        // No suitable driver found for
        // jdbc:mysql://127.0.0.1:3306/cardgame_db
        // должно вызываться перед любым обращением к jdbc-драйверу
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//
//        } catch (ClassNotFoundException ex) {
//            ex.printStackTrace();
//        }
        // or
//            try {
//                DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }


//        // !!! following code actually must be executed only once on server loading:
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mySqlPersistenceUnit");
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        try {
//            Account shop = (Account) entityManager.createQuery("select a from Account a where login = :login").setParameter("login", "shop").getSingleResult();
//            getServletContext().setAttribute("shop", shop);
//        } catch (NoResultException e) {
//            throw new RuntimeException("Error - shop does not exist");
//        } finally {
//            entityManager.close();
//            entityManagerFactory.close();
//        }



        try {
            // than inject from AppContext to all marked by @inject fields
            List<Field> allFields = FieldReflector.collectFieldsUpToClass(this.getClass(), DependencyInjectionServlet.class);
            List<Field> injectFields = FieldReflector.filterInjectableFields(allFields);

            for (Field field: injectFields) {
                field.setAccessible(true);
                Inject annotation = field.getAnnotation(Inject.class);
                logger.debug("I find method marked by @Inject: " + field);
                String beanName = annotation.value();
                logger.debug("I must instantiate and inject '" + beanName + "'");
                Object bean = AppContext.getInstance().getBean(beanName);
                logger.debug("Instantiation - OK: '" + beanName + "'");
                if (bean == null) {
                    throw new ServletException("There isn't bean with name '" + beanName + "' in application context only for ...");
                }
                field.set(this, bean);
            }
        } catch (Exception e) {
            throw new ServletException("Can't inject bean", e);
        }
    }
}
