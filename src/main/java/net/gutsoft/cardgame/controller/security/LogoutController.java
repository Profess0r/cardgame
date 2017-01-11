package net.gutsoft.cardgame.controller.security;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.apache.log4j.Logger.getLogger;

public class LogoutController extends DependencyInjectionServlet {

    public static final String ATTRIBUTE_NAME_ACCOUNT = "account";
    public static final String PAGE_ACCEPT = "index.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        Account account = (Account) session.getAttribute(ATTRIBUTE_NAME_ACCOUNT);
        session.removeAttribute(ATTRIBUTE_NAME_ACCOUNT);

        logger.info("User " + account.getLogin() + ", id=" + account.getId() + " logged out");

        request.getRequestDispatcher(PAGE_ACCEPT).forward(request, response);
    }

}
