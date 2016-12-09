package net.gutsoft.cardgame.controller.account;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.entity.Card;
import net.gutsoft.cardgame.entity.Deck;
import net.gutsoft.cardgame.hibernate.DataBaseManager;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageDeckController extends DependencyInjectionServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // get needed deck id
        int deckId = Integer.parseInt(req.getParameter("deckId"));

        // get account
        Account account = (Account) req.getSession().getAttribute("account");

        // search needed deck from account
        for (Deck currentDeck: account.getDeckList()) {
            if (currentDeck.getId() == deckId) {
                // get account card list
                List<Card> availableCards = new ArrayList<>(account.getCardList());
                // remove cards that already in deck from availableCards
                for (Card cardInDeck: currentDeck.getCardList()) {
                    for (Card currentCard: availableCards) {
                        if (cardInDeck.getId() == currentCard.getId()) {
                            availableCards.remove(currentCard);
                            break;
                        }
                    }
                }

                // set deck to response
                req.getSession().setAttribute("managedDeck", currentDeck);
                // set cards to response
                req.getSession().setAttribute("availableCards", availableCards);
                // redirect
                req.getRequestDispatcher("manageDeck.jsp").forward(req, resp);
                return;
            }
        }


        throw new RuntimeException("No deck with id=" + deckId);
        // TODO: 10.10.2016 переделать по-человечески (через errorList)
    }
}
