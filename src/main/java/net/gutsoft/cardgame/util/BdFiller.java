package net.gutsoft.cardgame.util;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.entity.Card;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BdFiller {
    public static void main(String[] args) throws IOException, SQLException {

        // creating cards
        Card card1 = new Card();
        card1.setName("warrior");
        card1.setRank(1);
        card1.setMaxHealth(10);
//        card1.setCurrentHealth(10);
        card1.setTargetClass("11000");
        card1.setMultiusable(true);
        card1.setPower(3);
        card1.setDefence(2);
        card1.setDescription("mighty warrior");
        card1.setPrice(80);
//        card1.setImage(ImageIO.read(new File("D:\\java\\images\\warrior.png")));

        Card card2 = new Card();
        card2.setName("archer");
        card2.setRank(1);
        card2.setMaxHealth(7);
//        card2.setCurrentHealth(7);
        card2.setTargetClass("11000");
        card2.setMultiusable(true);
        card2.setPower(4);
        card2.setDefence(1);
        card2.setDescription("fast archer");
        card2.setPrice(80);
//        card2.setImage(ImageIO.read(new File("D:\\java\\images\\archer.png")));

        Card card3 = new Card();
        card3.setName("mage");
        card3.setRank(1);
        card3.setMaxHealth(5);
//        card3.setCurrentHealth(5);
        card3.setTargetClass("11000");
        card3.setMultiusable(true);
        card3.setPower(6);
        card3.setDefence(1);
        card3.setDescription("intelligent mage");
        card3.setPrice(80);
//        card3.setImage(ImageIO.read(new File("D:\\java\\images\\mage.png")));

        Card card4 = new Card();
        card4.setName("heal");
        card4.setRank(1);
        card4.setMaxHealth(1);
//        card4.setCurrentHealth(1);
        card4.setTargetClass("01110");
        card4.setMultiusable(false);
        card4.setPower(-5);
        card4.setDefence(0);
        card4.setDescription("restore health of player or card");
        card4.setPrice(20);
//        card4.setImage(ImageIO.read(new File("D:\\java\\images\\heal.png")));

        Card card5 = new Card();
        card5.setName("shot");
        card5.setRank(1);
        card5.setMaxHealth(1);
//        card5.setCurrentHealth(1);
        card5.setTargetClass("10000");
        card5.setMultiusable(false);
        card5.setPower(3);
        card5.setDefence(0);
        card5.setDescription("cause direct damage to player or card");
        card5.setPrice(20);
//        card5.setImage(ImageIO.read(new File("D:\\java\\images\\shot.png")));

        Card card6 = new Card();
        card6.setName("wall");
        card6.setRank(1);
        card6.setMaxHealth(15);
//        card6.setCurrentHealth(15);
        card6.setTargetClass("01000");
        card6.setMultiusable(false);
        card6.setPower(0);
        card6.setDefence(2);
        card6.setDescription("defence structure");
        card6.setPrice(30);
//        card6.setImage(ImageIO.read(new File("D:\\java\\images\\wall.png")));

        Card card7 = new Card();
        card7.setName("tower");
        card7.setRank(1);
        card7.setMaxHealth(10);
//        card7.setCurrentHealth(10);
        card7.setTargetClass("01000");
        card7.setMultiusable(false);
        card7.setPower(4);
        card7.setDefence(3);
        card7.setDescription("defence structure, deal damage to attacking card");
        card7.setPrice(50);
//        card7.setImage(ImageIO.read(new File("D:\\java\\images\\tower.png")));


//        Card card7 = new Card();
//        card7.setName("upgrade");
//        card7.setRank(1);
//        card7.setCurrentHealth(0);
//        card7.setPower(0);
//        card7.setDefence(0);
//        card7.setDescription("upgrade other card to next (2) rank");
//        card7.setPrice(100);

        // filling cardList
        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        cardList.add(card5);
        cardList.add(card6);
        cardList.add(card7);


        // creating account
        Account account = new Account();
        account.setLogin("shop");
        account.setPassword("shop");
        account.setEmail("shop");
        account.setMoney(500);
        account.setCardList(cardList);

        // saving account to BD
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("mySqlPersistenceUnit");
            entityManager = entityManagerFactory.createEntityManager();

            entityManager.getTransaction().begin();
            entityManager.persist(card1);
            entityManager.persist(card2);
            entityManager.persist(card3);
            entityManager.persist(card4);
            entityManager.persist(card5);
            entityManager.persist(card6);
            entityManager.persist(card7);
//            entityManager.getTransaction().commit();
//
//            // entityManager здесь не закрывается, возможно это и позволяет связать обьекты (суть вопроса - ниже) - видимо нет
//
//            entityManager.getTransaction().begin();
            entityManager.persist(account);
            entityManager.getTransaction().commit();

            // забавно то, что мы создаем новые карты и добавляем в list и затем в аккаунт без id,
            // но при сохранении аккаунта у них есть id, несмотря на то, что мы их не перевытаскивали из БД
            // похоже, что hibernate генерирует id и дописывает к уже существующим обьектам

        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }
}
