package net.gutsoft.cardgame.entity;

import net.gutsoft.cardgame.util.ServletContextHolder;
import net.gutsoft.cardgame.websocket.messages.BattleMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Battle {

    // для начала стоит установить максимальное количество участников = 10
    // пока проверки на количество игроков вообще нигде нет

    // возможно, обьекты этого класса (отдельные поля, методы) нужно сделать synchronized

    private int id;
    private List<Player> playerList = new CopyOnWriteArrayList<>(); //concurrent ArrayList
    private boolean started = false;
    private boolean turnEnded = false;
    private boolean battleEnded = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addPlayer(Player player) {
        playerList.add(player);

        // оповещение всех игроков о изменении состава участников
    }

    // если сделать playerList => Map<playerId, player>, то можно избежать перебора
    public void removePlayer(int accountId) {
        for (Player player: playerList) {
            if (player.getAccountId() == accountId){
                playerList.remove(player);
            }
        }

        // оповещение всех игроков о изменении состава участников
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isTurnEnded() {
        return turnEnded;
    }

    public void setTurnEnded(boolean turnEnded) {
        this.turnEnded = turnEnded;
    }

    public boolean isBattleEnded() {
        return battleEnded;
    }

    public void setBattleEnded(boolean battleEnded) {
        this.battleEnded = battleEnded;
    }

    public void start() {
        this.started = true;

        // сдать карты каждому игроку
        for (Player player: playerList) {
            player.dealCards();
        }
    }


    // метод сложен для понимания, неплохо бы упростить (уже чуть лучше, но до совершенства еще далеко :-) )
    public void executeTurn() {

        // просчет хода выполняется для каждого игрока
        for (Player currentPlayer: playerList) {

            System.out.println(currentPlayer.getLogin() + "'s turn");
            // если ход для этого игрока уже был выполнен => перейти к следующему игроку
            // или если нет цели для хода => отметить ход как выполненный и перейти к следующему игроку
            if (currentPlayer.isTurnExecuted() || currentPlayer.getTarget() == null) {
                System.out.println(currentPlayer.getLogin() + " - turn was executed early or have no target");
                currentPlayer.setTurnExecuted(true);
                continue;
            }


            // (нужно бы еще рассмотреть вариант уничтожения цели раньше другим игроком)


            if (!currentPlayer.getTarget().split(" ")[0].equals("player")) {
                // если цель текущего игрока - не другой игрок => просто выполнить ход
                System.out.println(currentPlayer.getLogin() + " - target - not a player => execute self turn");
                currentPlayer.executeSelfTurn();
            } else {
                // цель текущего игрока - другой игрок
                System.out.println(currentPlayer.getLogin() + " - target - player");

                int targetPlayerIndex = Integer.parseInt(currentPlayer.getTarget().split(" ")[1]);
                Player targetPlayer = playerList.get(targetPlayerIndex);

                // рассматриваем возможные варианты по цели атакуемого игрока:
                if (targetPlayer.isTurnExecuted() || targetPlayer.getTarget() == null) {
                    // если ход атакуемого игрока был выполнен ранее или у него нет цели, то выполняется ход текущего игрока
                    System.out.println(targetPlayer.getLogin() + " - turn was executed early or have no target");
                    System.out.println(currentPlayer.getLogin() + " attack " + targetPlayer.getLogin());
                    currentPlayer.attackPlayer(targetPlayer, 1);
                } else if (!targetPlayer.getTarget().split(" ")[0].equals("player")) {
                    // если целью атакуемого не является игрок, то сначала выполняется ход атакуемого игрока
                    System.out.println(targetPlayer.getLogin() + " - target - " + targetPlayer.getTarget());
                    System.out.println(targetPlayer.getLogin() + " - execute self turn");
                    targetPlayer.executeSelfTurn();
                    targetPlayer.setTurnExecuted(true);

                    // и потом выполняется ход текущего игрока
                    System.out.println(currentPlayer.getLogin() + " attack " + targetPlayer.getLogin());
                    currentPlayer.attackPlayer(targetPlayer, 1);
                } else if (playerList.get(Integer.parseInt(targetPlayer.getTarget().split(" ")[1])).equals(currentPlayer)) {
                    // цель атакуемого игрока - текущий игрок => производится столкновение карт
                    System.out.println(targetPlayer.getLogin() + " - target - " + targetPlayer.getTarget());
                    System.out.println("execute card engage");
                    currentPlayer.engage(targetPlayer);

                    System.out.println(targetPlayer.getLogin() + " - set turn executed");
                    targetPlayer.setTurnExecuted(true);
                } else {
                    // иначе цель атакуемого игрока - не текущий игрок => производится ход текущего игрока
                    System.out.println(currentPlayer.getLogin() + " attack " + targetPlayer.getLogin());
                    currentPlayer.attackPlayer(targetPlayer, 1);
                }

            }
//            // если использованная карта одноразовая и еще не была сброшена - сбросить ее в отбой
//            if (!currentPlayer.getCardsInHand().get(currentPlayer.getUsedCardIndex()).isMultiusable() &&
//                    currentPlayer.getCardsInHand().get(currentPlayer.getUsedCardIndex()).getCurrentHealth() > 0) {
//                currentPlayer.dropCard(currentPlayer.getCardsInHand().remove(currentPlayer.getUsedCardIndex()));
//            }
            System.out.println(currentPlayer.getLogin() + " - set turn executed");
            currentPlayer.setTurnExecuted(true);
        }
        // ходы всех игроков просчитаны и дальше контроль возвращается в MakeTurnController
    }



    // методы для расчета через сообщения websocket без MakeTurnController

    public void applyPlayerTurn(BattleMessage message) {

        // все это еще надо будет завернуть в try !!!

        turnEnded = false;

        // получить игрока
        // это уже второй перебор playerList, что говорит в пользу Map
        int accountId = message.getAccountId();
        Player currentPlayer = null;
        for (Player player: playerList) {
            if (player.getAccountId() == accountId){
                currentPlayer = player;
            }
        }

        // получить данные о ходе игрока

        int usedCardIndex = -1;
        String target;

        // если карта не определена - ход "do nothing" (можно сделать и через == null)
        if (message.getCard().split(" ")[0].equals("undefined")) {
            System.out.println("card undefined");
            target = null;
        } else {
            // также, если для хода указана недопустимая цель - "do nothing"
            // возможно эту проверку лучше делать не здесь
            // другие варианты: на странице (JS), при выполнении хода
            usedCardIndex = Integer.parseInt(message.getCard().split(" ")[1]);
            Card usedCard = currentPlayer.getCardsInHand().get(usedCardIndex);
            target = message.getTarget();

            if (!usedCard.isTargetAvailable(target.split(" ")[0])) {
                System.out.println("target not available for this card");
                target = null;
            }
        }


        // записать ход игрока
        currentPlayer.setUsedCardIndex(usedCardIndex);
        currentPlayer.setTarget(target);
        System.out.println(currentPlayer.getLogin() + " card: " + usedCardIndex);
        System.out.println(currentPlayer.getLogin() + " target: " + target);


        // отмечаем игрока как походившего
        currentPlayer.setTurnReady(true);
        System.out.println(currentPlayer.getLogin() + " - turn ready");


        // часть идущих ниже проверок и действий можно (и скорее даже нужно) вынести вметоды обьекта Battle

        // проверяем все ли игроки похдили
        for (Player player: playerList) {
            if(!player.isTurnReady()) {
                // если нашолся хоть один неготовый, то заканчиваем выполнение метода
                return;
            }
        }

        // если все игроки походили, то выполняем просчет
        System.out.println("Execute turn...");
        this.executeTurn();

        // имеет смысл исключять из списка поверженного игрока, начислять положенные деньги? и опыт? и отпускать его (отправлять на страницу арены)
        for (Player player: playerList) {
            if (player.isDefeated()) {
                playerList.remove(player);
            }
        }

        turnEnded = true;
        System.out.println("turn ended - true");

        // если игрок остался один, то конец битвы
        if (playerList.size() < 2) {
            battleEnded = true;
            return;
        }

        // сдать карты каждому игроку
        for (Player player: playerList) {
            player.dealCards();
        }

    }

}
