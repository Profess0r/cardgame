package net.gutsoft.cardgame.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Deck")
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(
            name = "DeckCard",
            joinColumns = {@JoinColumn(name = "deck_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "card_id", referencedColumnName = "id")}
    )
    private List<Card> cardList;
    // дублирование карт в колоде - через дублирование записей или через поле "количество" - ?

    @Column(name = "active")
    private boolean active;

    // TODO: 03.10.2016 принцип привязки конкретного модификатора к конкретной карте - ?
    // ManyToMany через составной ключ deck_id+card_id (если в колоде есть одинаковые карты - ?)
    //private List<CardModifier> cardModifierList;


    public Deck() {
        this.name = "new deck";
        this.active = false;
    }

    public Deck(Account account) {
        this.name = "new deck";
        this.active = false;
        this.account = account;
        this.cardList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
