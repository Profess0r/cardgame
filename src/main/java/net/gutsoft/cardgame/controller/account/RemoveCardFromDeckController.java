package net.gutsoft.cardgame.controller.account;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.entity.Card;
import net.gutsoft.cardgame.entity.Deck;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RemoveCardFromDeckController extends DependencyInjectionServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int cardId = Integer.parseInt(req.getParameter("cardId"));

        Deck managedDeck = (Deck) req.getSession().getAttribute("managedDeck");
        List<Card> availableCards = (List<Card>) req.getSession().getAttribute("availableCards");

        int i = 0;
        while (cardId != managedDeck.getCardList().get(i).getId()) {
            i++;
        }
        Card card = managedDeck.getCardList().remove(i);
        availableCards.add(card);

        req.getSession().setAttribute("managedDeck", managedDeck);
        req.getSession().setAttribute("availableCards", availableCards);
        req.getRequestDispatcher("manageDeck.jsp").forward(req, resp);
    }
}
