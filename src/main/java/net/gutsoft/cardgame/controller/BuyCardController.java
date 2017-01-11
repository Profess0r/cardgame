package net.gutsoft.cardgame.controller;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.entity.Card;
import net.gutsoft.cardgame.hibernate.DataBaseManager;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;
import net.gutsoft.cardgame.validator.AccountValidator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BuyCardController extends DependencyInjectionServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final Map<String, String> errorMap = new HashMap<>();

        // get card id
        int cardId = Integer.parseInt(req.getParameter("cardId"));

        // get card from db
        Card card = new Card();
        card = DataBaseManager.getEntityById(card, cardId);

        // check money
        Account account = (Account) req.getSession().getAttribute("account");

        if (card.getPrice() > account.getMoney()) {
            errorMap.put("money", "Not enough money");
            req.setAttribute("errorMap", errorMap);
            req.getRequestDispatcher("shop.jsp").forward(req, resp);
            return;
        }

        // add card to account, remove money
        account.getCardList().add(card);
        account.setMoney(account.getMoney() - card.getPrice());

        //save changes to bd
        account = DataBaseManager.updateEntity(account);
        req.getSession().setAttribute("account", account);
        // если не производить перезапись обьекта в сессию, то при новом апдейте
        // уже занесенные в БД данные распознаются как новые и записываются повторно

        req.getRequestDispatcher("shop.jsp").forward(req, resp);

    }
}
