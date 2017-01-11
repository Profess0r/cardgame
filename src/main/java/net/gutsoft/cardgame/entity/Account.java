package net.gutsoft.cardgame.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "AccountCard",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "card_id", referencedColumnName = "id")}
    )
    private List<Card> cardList;
    // дублирование карт в колоде - через дублирование записей или через поле "количество" - ?


//    @ManyToMany
//    @JoinTable(
//            name = "accCardMap",
//            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id"),
//                            @JoinColumn(name = "quantity", )},
//            inverseJoinColumns = {@JoinColumn(name = "card_id", referencedColumnName = "id")},
//            schema =
//
//    )
//    @MapKey
//    private Map<Card, Integer> cardMap;

    // при fetch = FetchType.LAZY (и в случае с cardList тоже) возникает следующая ошибка:
    // org.hibernate.LazyInitializationException:
    // failed to lazily initialize a collection of role:
    // net.gutsoft.cardgame.entity.Account.deckList,
    // could not initialize proxy - no Session
    //
    // возможно, во избежание этого необходимо каким-либо образом оставлять открытую сессию с БД
    // (возможно, для этого нужно не закрывать EntityManager)
    //
    // свойство cascade позволяет по цепочке обновлять deckList при изменении аккаунта
    // (иначе для каждой колоды необходимо отдельно вызывать merge())
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Deck> deckList;

    // картинка и ее формат - ?
//    @Column(name = "image")
//    private Image image;

    @Column(name = "money")
    private int money;

    @Column(name = "health")
    private int health;

    @Column(name = "experience")
    private int experience;

    @Column(name = "level")
    private int level;

    @Column(name = "avatar")
    private int avatar;

//    @Column(name = "gameClass")
//    private String gameClass;  // или скорее значение из enum-a
//
//    @Column(name = "specialization")
//    private String specialization;  // или скорее значение из enum-a

    public Account() {
    }

    public Account(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.cardList = new ArrayList<>();
        this.deckList = new ArrayList<>();
        this.money = 500;
        this.health = 30;
        this.experience = 0;
        this.level = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public List<Deck> getDeckList() {
        return deckList;
    }

    public void setDeckList(List<Deck> deckList) {
        this.deckList = deckList;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void applyMoney(int money) {
        this.money += money;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void applyExperience(int exp) {
        this.experience += exp;

        if (this.experience > (500 + 500 * this.level)) {
            this.levelUp();
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void levelUp() {
        this.experience = 0;
        this.health += 10;
        this.money += 100 * this.level;
        this.level++;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public Deck getActiveDeck() {
        try {
            for (Deck currentDeck : deckList) {
                if (currentDeck.isActive()) {
                    return currentDeck;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Active deck is not set");
        }
        throw new RuntimeException("Active deck is not set");
    }



    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
