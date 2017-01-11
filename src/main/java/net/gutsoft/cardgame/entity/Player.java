package net.gutsoft.cardgame.entity;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int accountId; // or just Account account
    private String login;
    private int avatar;
    private int level;
    private int maximumHealth; // карты лечения не должны превышать максимум
    private int currentHealth;
    private int battleExperience; // начисляется за урон по игроку и победу в сражении
    private Deck deck;
    private Card defenceCard; // or List<Card> defendCardList
    private List<Card> cardsInHand;
    private List<Card> cardsInStock;
    private List<Card> cardsInDrop;

    private boolean turnReady;
    private boolean readyForBattle;

    private boolean defeated;
    private boolean turnExecuted;

    private int usedCardIndex;
    private String target;

//    private List<PlayerModifier> playerModifierList;


    public Player() {
    }

    public Player(Account account) {
        this.accountId = account.getId();
        this.login = account.getLogin();
        this.avatar = account.getAvatar();
        this.level = account.getLevel();
        this.maximumHealth = account.getHealth();
        this.currentHealth = account.getHealth();
        this.deck = account.getActiveDeck();
        this.cardsInStock = new ArrayList<>();
        this.cardsInHand = new ArrayList<>();
        this.cardsInDrop = new ArrayList<>();
        this.turnReady = false;
        this.readyForBattle = false;
        this.defeated = false;

//        this.cardsInStock = new ArrayList<>(deck.getCardList()); - данный способ приводит к тому,
        // что карты которые должны быть разными, на самом деле ссылаются на один обьект,
        // в следствии чего все одинаковые карты теряют жизни если атакована лишь одна из них

        for (Card currentCard: deck.getCardList()) {
            this.cardsInStock.add(new Card(currentCard));
        }
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaximumHealth() {
        return maximumHealth;
    }

    public void setMaximumHealth(int maximumHealth) {
        this.maximumHealth = maximumHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getBattleExperience() {
        return battleExperience;
    }

    public void setBattleExperience(int battleExperience) {
        this.battleExperience = battleExperience;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Card getDefenceCard() {
        return defenceCard;
    }

    public void setDefenceCard(Card defenceCard) {
        this.defenceCard = defenceCard;
    }

    public List<Card> getCardsInHand() {
        return cardsInHand;
    }

    public void setCardsInHand(List<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public List<Card> getCardsInStock() {
        return cardsInStock;
    }

    public void setCardsInStock(List<Card> cardsInStock) {
        this.cardsInStock = cardsInStock;
    }

    public List<Card> getCardsInDrop() {
        return cardsInDrop;
    }

    public void setCardsInDrop(List<Card> cardsInDrop) {
        this.cardsInDrop = cardsInDrop;
    }

    public boolean isTurnReady() {
        return turnReady;
    }

    public void setTurnReady(boolean turnReady) {
        this.turnReady = turnReady;
    }

    public boolean isReadyForBattle() {
        return readyForBattle;
    }

    public void setReadyForBattle(boolean readyForBattle) {
        this.readyForBattle = readyForBattle;
    }

    public boolean isDefeated() {
        return defeated;
    }

    public boolean isTurnExecuted() {
        return turnExecuted;
    }

    public void setTurnExecuted(boolean turnExecuted) {
        this.turnExecuted = turnExecuted;
    }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }

    public int getUsedCardIndex() {
        return usedCardIndex;
    }

    public void setUsedCardIndex(int usedCardIndex) {
        this.usedCardIndex = usedCardIndex;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }



    public void dealCards() {
        while (this.cardsInStock.size() > 0 && this.cardsInHand.size() < 4){
            pullCard();
        }

        this.usedCardIndex = -1;
        this.target = null;
        this.turnExecuted = false;
        this.turnReady = false;
        System.out.println(login + " - dealCards end, size: " + cardsInHand.size());
    }

    public void pullCard() {
        int index = (int) (Math.random() * cardsInStock.size());
        cardsInHand.add(cardsInStock.remove(index));
    }

    public void pullCardFromDrop() {
        if (cardsInDrop.size() > 0) {
            int index = (int) (Math.random() * cardsInDrop.size());
            cardsInHand.add(cardsInDrop.remove(index));
            System.out.println("card pulled from drop");
        } else {
            System.out.println("no cards in drop");
        }
    }

    public void dropCard(Card card) {
        card.setCurrentHealth(card.getMaxHealth());
        cardsInDrop.add(card);
    }

    public void addWinExperience(int amount) {
        battleExperience += amount;
    }

    public void executeSelfTurn(List<String> turnLog) {
        // этот метод выполняет ход если целью игрока не является другой игрок

        System.out.println(login + " - self turn execution...");

        // разобрать, что является целью
        String targetType = target.split(" ")[0];

        if (targetType.equals("def")) {
            System.out.println(login + " - card: " + usedCardIndex + ", size: " + cardsInHand.size());

            if (defenceCard == null){
                defenceCard = cardsInHand.remove(usedCardIndex);
                turnLog.add(login + " set defence card to " + defenceCard.getName());
            } else {
                // здеь нужно предусмотреть и другие особые эффекты на карты, но пока только лечение и замена карты
                Card usedCard = cardsInHand.get(usedCardIndex);
                if (usedCard.isTargetAvailable("card")) {
                    int resultHealth = defenceCard.getCurrentHealth() - usedCard.getPower();
                    defenceCard.setCurrentHealth(resultHealth > defenceCard.getMaxHealth() ? defenceCard.getMaxHealth() : resultHealth);

                    if (!usedCard.isMultiusable()) {
                        cardsInHand.remove(usedCardIndex);
                        dropCard(usedCard);
                    }

                    turnLog.add(login + " used one of his card on defence card");
                } else {
                    dropCard(defenceCard);
                    cardsInHand.remove(usedCardIndex);
                    defenceCard = usedCard;
                    turnLog.add(login + " changed defence card to " + usedCard.getName());
                }
            }
            System.out.println("defCard setting");
        } else if (targetType.equals("drop")) {
            dropCard(cardsInHand.remove(usedCardIndex));
            turnLog.add(login + " dropped one card");
            System.out.println("card dropped");
        } else if (targetType.equals("card")) {
            // где-то нужно проверять, может ли в конкретном случае одна карта применятся на другую
            // или расчитать возможность применения любых карт на любые
            // также рассмотреть применение карты на себя или предотвратить возможность такого действия

            int targetCardIndex = Integer.parseInt(target.split(" ")[1]);
            Card targetCard = cardsInHand.get(targetCardIndex);
            Card usedCard = cardsInHand.get(usedCardIndex);

            if (usedCard.isTargetAvailable("card")){
                // подсчет результирующего количества жизней
                int resultHealth = targetCard.getCurrentHealth() - usedCard.getPower();
                System.out.println("resultHealth = " + resultHealth);

                if (!usedCard.isMultiusable()) {
                    cardsInHand.remove(usedCardIndex);
                    dropCard(usedCard);
                }

                // вариант превышения максимального количества жизней при лечении
                if (resultHealth > targetCard.getMaxHealth()) {
                    targetCard.setCurrentHealth(targetCard.getMaxHealth());
                } else {
                    targetCard.setCurrentHealth(resultHealth);
                }

                turnLog.add(login + " used one of his card on another");
            } else {
                turnLog.add(login + " tried to use one of his card on another, but failed");
            }

            System.out.println("current health = " + targetCard.getCurrentHealth());
            System.out.println("another card affected");

        } else if (targetType.equals("me")) {
            Card usedCard = cardsInHand.get(usedCardIndex);

            if (usedCard.isTargetAvailable("me")) {
                int resultHealth = currentHealth - usedCard.getPower();
                currentHealth = resultHealth > maximumHealth ? maximumHealth : resultHealth;

                if (!usedCard.isMultiusable()) {
                    cardsInHand.remove(usedCardIndex);
                    dropCard(usedCard);
                }

                turnLog.add(login + " used one of his card on himself");

                System.out.println("player affected");
            } else {
                turnLog.add(login + " tried to use one of his card on himself, but failed");
            }
        } else if (targetType.equals("pull")) {
            for (int i = 0; i < 2; i++) {
                if (cardsInHand.size() < 4) {
                    pullCardFromDrop();
                }
            }
            turnLog.add(login + " pulled cards from drop");
        }
        turnExecuted = true;

        System.out.println(login + " self turn executed");
    }

    public void engage(Player targetPlayer, List<String> turnLog) {
        // этот метод расчитывает столкновение карт

        Card currentPlayerCard = cardsInHand.get(usedCardIndex);
        Card targetPlayerCard = targetPlayer.getCardsInHand().get(targetPlayer.getUsedCardIndex());

        turnLog.add(login + "'s " + currentPlayerCard.getName() + " and " + targetPlayer.getLogin() + "'s " + targetPlayerCard.getName() + " - engage");

        boolean targetPlayerCardIsDestroyed = false;
        boolean currentPlayerCardIsDestroyed = false;

        if (currentPlayerCard.getInitiative() > targetPlayerCard.getInitiative()) {
            System.out.println(currentPlayerCard.getName() + " - attack " + targetPlayerCard.getName());
            targetPlayerCardIsDestroyed = currentPlayerCard.attackCard(targetPlayerCard, 1, turnLog);

            if (!targetPlayerCardIsDestroyed) {
                System.out.println(targetPlayerCard.getName() + " - attack " + currentPlayerCard.getName());
                currentPlayerCardIsDestroyed = targetPlayerCard.attackCard(currentPlayerCard, 1, turnLog);
            }
        } else if (currentPlayerCard.getInitiative() == targetPlayerCard.getInitiative()) {
            System.out.println(currentPlayerCard.getName() + " - attack " + targetPlayerCard.getName());
            targetPlayerCardIsDestroyed = currentPlayerCard.attackCard(targetPlayerCard, 1, turnLog);

            System.out.println(targetPlayerCard.getName() + " - attack " + currentPlayerCard.getName());
            currentPlayerCardIsDestroyed = targetPlayerCard.attackCard(currentPlayerCard, 1, turnLog);
        } else {
            System.out.println(targetPlayerCard.getName() + " - attack " + currentPlayerCard.getName());
            currentPlayerCardIsDestroyed = targetPlayerCard.attackCard(currentPlayerCard, 1, turnLog);

            if (!currentPlayerCardIsDestroyed) {
                System.out.println(currentPlayerCard.getName() + " - attack " + targetPlayerCard.getName());
                targetPlayerCardIsDestroyed = currentPlayerCard.attackCard(targetPlayerCard, 1, turnLog);
            }
        }

        if (!targetPlayerCard.isMultiusable()) {
            targetPlayerCardIsDestroyed = true;
        }

        if (!currentPlayerCard.isMultiusable()) {
            currentPlayerCardIsDestroyed = true;
        }

        System.out.println("targetPlayerCardIsDestroyed - " + targetPlayerCardIsDestroyed);
        System.out.println("currentPlayerCardIsDestroyed - " + currentPlayerCardIsDestroyed);

        if (targetPlayerCardIsDestroyed) {
            // убрать карту с рук игрока
            System.out.println(targetPlayer.getLogin() + " drop card");
            targetPlayer.dropCard(targetPlayer.getCardsInHand().remove(targetPlayer.getUsedCardIndex()));

            // при уничтожении карты противника и при условии, что атакующая карта не уничтожена,
            // нужно наносить урон игроку или защитной карте (например в половину силы атакующей карты)
            if (!currentPlayerCardIsDestroyed) {
                System.out.println(login + " - cause damage to" + targetPlayer.getLogin());
                this.attackPlayer(targetPlayer, 2, turnLog);
            }
        }

        if (currentPlayerCardIsDestroyed) {
            // убрать карту с рук игрока
            System.out.println(login + " drop card");
            dropCard(cardsInHand.remove(usedCardIndex));

            // при уничтожении карты противника и при условии, что атакующая карта не уничтожена,
            // нужно наносить урон игроку или защитной карте (например в половину силы атакующей карты)
            if (!targetPlayerCardIsDestroyed) {
                System.out.println(targetPlayer.getLogin() + " - cause damage to " + login);
                targetPlayer.attackPlayer(this, 2, turnLog);
            }
        }

    }

    public void attackPlayer(Player targetPlayer, int attackModifier, List<String> turnLog) {

        Card currentPlayerCard = cardsInHand.get(usedCardIndex);
        Card targetPlayerCard = targetPlayer.getDefenceCard();

        System.out.println("let's check defCard");
        // есть ли карта защиты
        if (targetPlayerCard != null) {
            System.out.println(targetPlayer.getLogin() + " defCard - " + targetPlayerCard.getName());

            boolean targetPlayerCardIsDestroyed = false;
            boolean currentPlayerCardIsDestroyed = false;

            if (currentPlayerCard.getInitiative() > targetPlayerCard.getInitiative()) {
                System.out.println(currentPlayerCard.getName() + " - attack " + targetPlayerCard.getName());
                targetPlayerCardIsDestroyed = currentPlayerCard.attackCard(targetPlayerCard, attackModifier, turnLog);

                if (!targetPlayerCardIsDestroyed) {
                    System.out.println(targetPlayerCard.getName() + " - attack " + currentPlayerCard.getName());
                    currentPlayerCardIsDestroyed = targetPlayerCard.attackCard(currentPlayerCard, 1, turnLog);
                }
            } else if (currentPlayerCard.getInitiative() == targetPlayerCard.getInitiative()) {
                System.out.println(currentPlayerCard.getName() + " - attack " + targetPlayerCard.getName());
                targetPlayerCardIsDestroyed = currentPlayerCard.attackCard(targetPlayerCard, attackModifier, turnLog);

                System.out.println(targetPlayerCard.getName() + " - attack " + currentPlayerCard.getName());
                currentPlayerCardIsDestroyed = targetPlayerCard.attackCard(currentPlayerCard, 1, turnLog);
            } else {
                System.out.println(targetPlayerCard.getName() + " - attack " + currentPlayerCard.getName());
                currentPlayerCardIsDestroyed = targetPlayerCard.attackCard(currentPlayerCard, 1, turnLog);

                if (!currentPlayerCardIsDestroyed) {
                    System.out.println(currentPlayerCard.getName() + " - attack " + targetPlayerCard.getName());
                    targetPlayerCardIsDestroyed = currentPlayerCard.attackCard(targetPlayerCard, attackModifier, turnLog);
                }
            }

            if (!currentPlayerCard.isMultiusable()) {
                currentPlayerCardIsDestroyed = true;
            }

            System.out.println("targetPlayerCardIsDestroyed - " + targetPlayerCardIsDestroyed);
            System.out.println("currentPlayerCardIsDestroyed - " + currentPlayerCardIsDestroyed);

            if (targetPlayerCardIsDestroyed) {
                // убрать карту с рук игрока
                System.out.println("drop card " + targetPlayerCard.getName());
                targetPlayer.dropCard(targetPlayer.getDefenceCard());
                targetPlayer.setDefenceCard(null);

                // при уничтожении карты противника и при условии, что атакующая карта не уничтожена,
                // нужно наносить урон игроку или защитной карте (например в половину силы атакующей карты)
                if (!currentPlayerCardIsDestroyed) {
                    // наносим урон игроку
                    System.out.println("cause damage to " + targetPlayer.getLogin());
                    this.causeDamageToPlayer(targetPlayer, attackModifier*2, turnLog);
                }
            }

            if (currentPlayerCardIsDestroyed) {
                // убрать карту с рук игрока
                System.out.println("drop card " + currentPlayerCard.getName());
                dropCard(cardsInHand.remove(usedCardIndex));
            }

        } else {
            System.out.println(targetPlayer.getLogin() + " defCard - " + targetPlayerCard);

            //если нет карты защиты => наносим урон игроку
            System.out.println("cause damage to " + targetPlayer.getLogin());
            this.causeDamageToPlayer(targetPlayer, attackModifier, turnLog);
        }
    }

    public void causeDamageToPlayer(Player targetPlayer, int attackModifier, List<String> turnLog) {

        int damage = cardsInHand.get(usedCardIndex).getPower()/attackModifier;
        if (damage < 1) {
            damage = 1;
        }

        int targetPlayerHealthResult = targetPlayer.getCurrentHealth() - damage;
        System.out.println(login + " cause " + cardsInHand.get(usedCardIndex).getPower() + " damage to " + targetPlayer.getLogin());
        System.out.println(targetPlayer.getLogin() + " health = " + targetPlayerHealthResult);

        battleExperience += (double)damage * targetPlayer.getLevel() / level;

        if (targetPlayerHealthResult < 1) {
            targetPlayer.setCurrentHealth(targetPlayerHealthResult);
            targetPlayer.setDefeated(true);
            turnLog.add(cardsInHand.get(usedCardIndex).getName() + " deal " + damage + " points of damage. "  + targetPlayer.getLogin() + " is defeated");
            System.out.println(targetPlayer.getLogin() + " - defeated");

            battleExperience += (double)targetPlayer.getMaximumHealth() * targetPlayer.getLevel() / level / 2;

        } else {
            targetPlayer.setCurrentHealth(targetPlayerHealthResult);
            turnLog.add(cardsInHand.get(usedCardIndex).getName() + " deal " + damage + " points of damage to "  + targetPlayer.getLogin());
        }

        if (!cardsInHand.get(usedCardIndex).isMultiusable()) {
            dropCard(cardsInHand.remove(usedCardIndex));
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return accountId == player.accountId;
    }
}
