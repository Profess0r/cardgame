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
import java.util.List;

public class SaveDeckController extends DependencyInjectionServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Deck managedDeck = (Deck) req.getSession().getAttribute("managedDeck");
        Account account = (Account) req.getSession().getAttribute("account");

        // set new name
        managedDeck.setName(req.getParameter("name"));

        // set activeness
        int deckId = managedDeck.getId();
        List<Deck> accountDeckList = account.getDeckList();

        boolean active = false;
        if ("true".equals(req.getParameter("active"))) {
            active = true;
        }
        if (active) {
            for (Deck deck: accountDeckList) {
                deck.setActive(false);
            }
        }
        managedDeck.setActive(active);


        // set cards

        managedDeck.getCardList().clear();

        List<Card> availableCards = new ArrayList<>(account.getCardList());

        String[] cardIds = req.getParameter("cards").split(" ");

        System.out.println(req.getParameter("name"));
        System.out.println(active);
        System.out.println(cardIds.length);

        int[] cardIdArray = new int[cardIds.length];
        for (int i = 0; i < cardIds.length; i++) {
            System.out.println(cardIds[i]);
            cardIdArray[i] = Integer.parseInt(cardIds[i]);
        }

        for (int cardId: cardIdArray) {
            int i = 0;
            while (cardId != availableCards.get(i).getId()) {
                i++;
            }
            Card card = availableCards.remove(i);
            managedDeck.getCardList().add(card);
        }


        // search needed deck from account
        // не учтен вариант отсуцтвия колоды с указанным id (как и во многих других местах)
        int i = 0;
        while (accountDeckList.get(i).getId() != deckId) {
            i++;
        }
        accountDeckList.set(i, managedDeck);

        // нужно ли отдельно апдейтить колоду? - если в классе Account на поле deckList выставлено свойство CASCADE, то не нужно!!!
//        DataBaseManager.updateEntity(managedDeck);

        // update account
        account = DataBaseManager.updateEntity(account);

        req.getSession().setAttribute("account", account);

        req.getSession().removeAttribute("managedDeck");
        req.getSession().removeAttribute("availableCards");
        req.getRequestDispatcher("myDecks.jsp").forward(req, resp);
    }
}
