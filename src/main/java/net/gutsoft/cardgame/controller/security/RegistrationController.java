package net.gutsoft.cardgame.controller.security;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.hibernate.DataBaseManager;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;
import net.gutsoft.cardgame.validator.AccountValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RegistrationController extends DependencyInjectionServlet {
    public static final String PARAM_LOGIN = "login";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_EMAIL = "email";

    //    public static final String ATTRIBUTE_MODEL_TO_VIEW = "quiz";
    public static final String PAGE_REGISTERED = "main.jsp";
    public static final String PAGE_MORE_INFO = "registration.jsp";

//    public static final Logger logger = Logger.getLogger(getCurrentClassName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String login = req.getParameter(PARAM_LOGIN);
        String password = req.getParameter(PARAM_PASSWORD);
        final String email = req.getParameter(PARAM_EMAIL);

        if (login == null && password == null && email == null) {
            req.getRequestDispatcher(PAGE_MORE_INFO).forward(req, resp);
            return;
        }

        // TODO: 30.09.2016 changeable validator (like in example) ?
        final Account newAccount = new Account(login, password, email);
        final Map<String, String> errorMap = AccountValidator.validate(newAccount);

        if (errorMap.isEmpty()) {
            try {
                List<Account> accounts;

                Account account = new Account();

                accounts = DataBaseManager.getEntityListByParameter(account, "login", login);
                if (!accounts.isEmpty()) {
                    errorMap.put("login", "Such login exists!");
                }

                accounts = DataBaseManager.getEntityListByParameter(account, "email", email);
                if (!accounts.isEmpty()) {
                    errorMap.put("email", "Such email exists!");
                }

                if (errorMap.isEmpty()) {
                    DataBaseManager.persistEntity(newAccount);
                    account = DataBaseManager.getEntityByParameter(account, "login", login);
                }

                if (errorMap.isEmpty()) {
                    req.setAttribute("account", account); // зачем - ?
                    req.getSession(true).setAttribute("account", account);
                    req.getRequestDispatcher(PAGE_REGISTERED).forward(req, resp);
                    return;
                }
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }

        req.setAttribute("login", login);
        req.setAttribute("password", password);
        req.setAttribute("email", email);
        req.setAttribute("errorMap", errorMap);

        req.getRequestDispatcher(PAGE_MORE_INFO).forward(req, resp);
    }
}
