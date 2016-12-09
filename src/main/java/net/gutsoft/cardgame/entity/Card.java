package net.gutsoft.cardgame.entity;

import javax.persistence.*;

@Entity
@Table(name = "Card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private /*final*/ int id;
    @Column(name = "name")
    private String name;
    @Column(name = "rank")
    private int rank;
    @Column(name = "maxHealth")
    private int maxHealth;
    @Transient
    private int currentHealth;
    @Column(name = "power")
    private int power;
    @Column(name = "defence")
    private int defence;
    @Column(name = "description")
    private String description;

    // на чем может быть использована карта в формате 01010
    // значения разрядов:
    // 1 - другой игрок 2 - защита 3 - другая карта 4 - сам игрок (5 - сама карта)
    @Column(name = "targetClass")
    private String targetClass;

    @Column(name = "multiusable")
    private boolean multiusable;

    //временные статусы (усиление, слабость, регенерация, яд...)
//    @Transient
//    private CardStatus status;

    // слоты под модификаторы - количество
//    @Column(name = "slots")
//    private int slotCount;

    // special ability (from enum of special abilities)
//    @Column(name = "ability")
//    private Ability ability;

    // принадлежность или тип защиты (стихия, элемент, кожа, метал, укрепление...) (enum?)
//    @Column(name = "element")
//    private Element element;

    // тип атаки (стихия, элемент, колющая, режущая, осадная...) (enum?)
//    @Column(name = "attackType")
//    private Element attackType;

    // стандартная цена
    @Column(name = "price")
    private int price;

    public Card() {
    }

    public Card(Card templateCard) {
        this.name = templateCard.getName();
        this.rank = templateCard.getRank();
        this.maxHealth = templateCard.getMaxHealth();
        this.currentHealth = this.maxHealth;
        this.power = templateCard.getPower();
        this.defence = templateCard.getDefence();
        this.description = templateCard.getDescription();
        this.targetClass = templateCard.getTargetClass();
        this.multiusable = templateCard.isMultiusable();
        this.price = templateCard.getPrice();
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int health) {
        this.currentHealth = health;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public boolean isMultiusable() {
        return multiusable;
    }

    public void setMultiusable(boolean multiusable) {
        this.multiusable = multiusable;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rank=" + rank +
                ", health=" + maxHealth +
                ", power=" + power +
                ", defence=" + defence +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }



    public boolean isTargetAvailable(String target) {
        switch (target) {
            case "player":
                return targetClass.charAt(0) != '0';
            case "def":
                return targetClass.charAt(1) != '0';
            case "card":
                return targetClass.charAt(2) != '0';
            case "me":
                return targetClass.charAt(3) != '0';
            case "self":
                return targetClass.charAt(4) != '0';
            case "drop":
                return true;
            default:
                return false;
        }
    }

    public boolean attackCard(Card targetCard, int attackModifier) {
        // возвращаемое значение: true - атакуемая карта уничтожена, false - атакуемая карта НЕ уничтожена

        int cardAttackDamage = power/attackModifier - targetCard.getDefence();
        if (cardAttackDamage < 1) {
            cardAttackDamage = 1;
        }
        int targetCardHealthResult = targetCard.getCurrentHealth() - cardAttackDamage;

        System.out.println(targetCard.getName() + " card health = " + targetCard.getCurrentHealth());
        System.out.println(" damage = " + power + "/" + attackModifier + " - " + targetCard.getDefence() + " = " + cardAttackDamage);
        System.out.println(targetCard.getName() + " result health = " + targetCardHealthResult);

        if (targetCardHealthResult < 1) {
            return true;
        } else {
            targetCard.setCurrentHealth(targetCardHealthResult);
            return false;
        }
    }

    // этот метод предназначен для просчета воздействия одной катры на другую,
    // обобщает нанесение урона, лечение, наложение статуса, замещение в обороне...
    public void affectCard(Card targetCard) {

    }
}
