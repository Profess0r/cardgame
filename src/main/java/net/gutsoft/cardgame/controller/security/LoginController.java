package net.gutsoft.cardgame.controller.security;


import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.hibernate.DataBaseManager;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginController extends DependencyInjectionServlet {
    private static final String PARAMETER_LOGIN = "login";
    private static final String PARAMETER_PASSWORD = "password";
    private static final String ACCOUNT = "account";

    public static final String PAGE_DENY = "login.jsp";
    public static final String PAGE_ACCEPT = "main.jsp";

//    public static final Logger logger = getLogger(getCurrentClassName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        final String login = request.getParameter(PARAMETER_LOGIN);
        String password = request.getParameter(PARAMETER_PASSWORD);

        if (login != null && password != null) {

            Account account = new Account();
            try {
                account = DataBaseManager.getEntityByParameter(account, "login", login);
            } catch (NoResultException e){
                System.out.println("NoResultException");
                request.getRequestDispatcher(PAGE_DENY).forward(request, response);
                return;
            }

            if (account.getPassword().equals(password)) {
                HttpSession session = request.getSession(true);
                session.setAttribute(ACCOUNT, account);
                request.getRequestDispatcher(PAGE_ACCEPT).forward(request, response);
                return;
            }
        }
        request.getRequestDispatcher(PAGE_DENY).forward(request, response);
    }
}

