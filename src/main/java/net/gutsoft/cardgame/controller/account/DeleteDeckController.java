package net.gutsoft.cardgame.controller.account;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.entity.Deck;
import net.gutsoft.cardgame.hibernate.DataBaseManager;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DeleteDeckController extends DependencyInjectionServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int deckId = Integer.parseInt(req.getParameter("deckId"));

        // get account
        Account account = (Account) req.getSession().getAttribute("account");

        for (Deck currentDeck: account.getDeckList()) {
            if (currentDeck.getId() == deckId) {
                account.getDeckList().remove(currentDeck);
                break;
            }
        }

        // persist to DB
        Account updatedAccount = DataBaseManager.updateEntity(account);

        // set updated entities to session
        req.getSession().setAttribute("account", updatedAccount);

        // redirect
        req.getRequestDispatcher("myDecks.jsp").forward(req, resp);

    }
}
