package net.gutsoft.cardgame.controller.account;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.entity.Deck;
import net.gutsoft.cardgame.hibernate.DataBaseManager;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateNewDeckController extends DependencyInjectionServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // get account
        Account account = (Account) req.getSession().getAttribute("account");

        // create new deck
        Deck newDeck = new Deck(account);

        // link new deck with current account
        account.getDeckList().add(newDeck);

        // persist to DB
        DataBaseManager.persistEntity(newDeck);

        System.out.println("deck persisted");

        Account updatedAccount = DataBaseManager.updateEntity(account);

        System.out.println("account updated");

        // set updated entities to session
        req.getSession().setAttribute("account", updatedAccount);

        // redirect
        req.getRequestDispatcher("myDecks.jsp").forward(req, resp);

    }
}
