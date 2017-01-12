package net.gutsoft.cardgame.entity;

import net.gutsoft.cardgame.hibernate.DataBaseManager;
import net.gutsoft.cardgame.util.ServletContextHolder;
import net.gutsoft.cardgame.websocket.BattleEndpoint;
import net.gutsoft.cardgame.websocket.MessagesTypes;
import net.gutsoft.cardgame.websocket.messages.BattleMessage;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Battle {

    // возможно, обьекты этого класса (отдельные поля, методы) нужно сделать synchronized

    private int id;
    private String name;
    private int playerCount;
    private int maxPlayers;
    private int maxLevel;
    private List<Player> playerList = new CopyOnWriteArrayList<>(); //concurrent ArrayList
    private Map<Integer, Integer> accountExperienceMap = new Hashtable<>();
    private boolean started = false;
    private boolean turnEnded = false;
    private boolean battleEnded = false;
    private List<String> turnLog = new ArrayList<>();
    private Thread timerThread;
    private long createTime;

    public Battle() {
        this.createTime = System.currentTimeMillis();
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

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        // максимум - 10 игроков
        if (maxPlayers > 10) {
            maxPlayers = 10;
        }
        // минимум - 2 игрока
        if (maxPlayers < 2) {
            maxPlayers = 2;
        }
        this.maxPlayers = maxPlayers;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void addPlayer(Player player) {
        playerList.add(player);

        // оповещение всех игроков о изменении состава участников
    }

    // если сделать playerList => Map<playerId, player>, то можно избежать перебора
    public void removePlayer(int accountId) {
        for (Player player: playerList) {
            if (player.getAccountId() == accountId){
                accountExperienceMap.put(player.getAccountId(), player.getBattleExperience());
                playerList.remove(player);
//                return player.getBattleExperience();
            }
        }
//        return 0; // throw Exception

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

    public List<String> getTurnLog() {
        return turnLog;
    }

    public void setTurnLog(List<String> turnLog) {
        this.turnLog = turnLog;
    }

    public Map<Integer, Integer> getAccountExperienceMap() {
        return accountExperienceMap;
    }

    public void setAccountExperienceMap(Map<Integer, Integer> accountExperienceMap) {
        this.accountExperienceMap = accountExperienceMap;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTurnLogAsString() {
        StringBuilder builder = new StringBuilder();
        for (String string: turnLog) {
            builder.append("\n").append(string);
        }
        System.out.println(builder.toString());
        return builder.toString();
    }

    public void clearTurnLog() {
        turnLog.clear();
    }

    public void start() {
        this.started = true;
        this.playerCount = playerList.size();

        // сдать карты каждому игроку
        for (Player player: playerList) {
            player.dealCards();
        }

        startTimer();
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
                currentPlayer.executeSelfTurn(turnLog);
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
                    currentPlayer.attackPlayer(targetPlayer, 1, turnLog);
                } else if (!targetPlayer.getTarget().split(" ")[0].equals("player")) {
                    // если целью атакуемого не является игрок, то сначала выполняется ход атакуемого игрока
                    System.out.println(targetPlayer.getLogin() + " - target - " + targetPlayer.getTarget());
                    System.out.println(targetPlayer.getLogin() + " - execute self turn");
                    targetPlayer.executeSelfTurn(turnLog);
                    targetPlayer.setTurnExecuted(true);

                    // и потом выполняется ход текущего игрока
                    System.out.println(currentPlayer.getLogin() + " attack " + targetPlayer.getLogin());
                    currentPlayer.attackPlayer(targetPlayer, 1, turnLog);
                } else if (playerList.get(Integer.parseInt(targetPlayer.getTarget().split(" ")[1])).equals(currentPlayer)) {
                    // цель атакуемого игрока - текущий игрок => производится столкновение карт
                    System.out.println(targetPlayer.getLogin() + " - target - " + targetPlayer.getTarget());
                    System.out.println("execute card engage");
                    currentPlayer.engage(targetPlayer, turnLog);

                    System.out.println(targetPlayer.getLogin() + " - set turn executed");
                    targetPlayer.setTurnExecuted(true);
                } else {
                    // иначе цель атакуемого игрока - не текущий игрок => производится ход текущего игрока
                    System.out.println(currentPlayer.getLogin() + " attack " + targetPlayer.getLogin());
                    currentPlayer.attackPlayer(targetPlayer, 1, turnLog);
                }

            }

            System.out.println(currentPlayer.getLogin() + " - set turn executed");
            currentPlayer.setTurnExecuted(true);
        }
        // ходы всех игроков просчитаны и дальше контроль возвращается в battle.applyTurn
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
        } else if (message.getCard().split(" ")[0].equals("drop")) {
            System.out.println("pull from drop");
            target = "pull";
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


        // часть идущих ниже проверок и действий можно (и скорее даже нужно) вынести в методы обьекта Battle

        // проверяем все ли игроки похдили
        for (Player player: playerList) {
            if(!player.isTurnReady()) {
                // если нашолся хоть один неготовый, то заканчиваем выполнение метода
                return;
            }
        }

        // если все игроки походили, то выполняем просчет
        System.out.println("Execute turn...");
//        this.timerThread.notify();
        this.executeTurn();
        this.checkTurnResults();
    }

    public void checkTurnResults() {
        // имеет смысл исключять из списка поверженного игрока, начислять положенные деньги? и опыт? и отпускать его (отправлять на страницу арены)
        for (Player player: playerList) {
            if (player.isDefeated()) {
                accountExperienceMap.put(player.getAccountId(), player.getBattleExperience());
                playerList.remove(player);
            }
        }

        turnEnded = true;
        System.out.println("turn ended - true");

        // если игрок остался один, то конец битвы
        if (playerList.size() < 2) {
            stopTimer();
            battleEnded = true;
            Player winner = playerList.get(0);
            winner.addWinExperience(playerCount * 5);
            this.removePlayer(winner.getAccountId());
            notifyPlayers();
            return;
        }

        // сдать карты каждому игроку
        for (Player player: playerList) {
            player.dealCards();
        }
        notifyPlayers();
        startTimer();
    }

    public void resetTurn() {
        for (Player player: playerList) {
            player.setTurnReady(false);
        }
        startTimer();
    }

    private void startTimer() {
        if (timerThread != null) {
            timerThread.interrupt();
        }
        timerThread = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("thread start sleeping");
                    Thread.sleep(100000); // 1 min 40 sec
                    System.out.println("thread awake");
                    executeTurn();
                    checkTurnResults();
                } catch (InterruptedException e) {
                    System.out.println("sleeping thread was interrupted due to turn execution or battle end");
                }
            }
        };
        timerThread.start();
    }

    private void stopTimer() {
        if (timerThread != null) {
            timerThread.interrupt();
        }
    }

    private void notifyPlayers() {
        if (this.isTurnEnded()) {
            BattleEndpoint.getBattleToPlayerEndpointTable().get(this.id).get(0).broadcast(new BattleMessage(MessagesTypes.TURNEND, "", this.getTurnLogAsString()), this.id);
            this.clearTurnLog();
        }

        // задержка нужна для того, чтобы клиент успел обновить данные о сражении,
        // иначе возникает дополнительный ложный визов alert
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (this.isBattleEnded()) {
            BattleEndpoint.getBattleToPlayerEndpointTable().get(this.id).get(0).broadcast(new BattleMessage(MessagesTypes.BATTLEEND, "", ""), this.id);
        }
    }


    // это можно использовать при прохождении BattleGarbageCollector
    // для принудительного удаления битвы и начислении опыта
    // однако нет возможности положить обновленную версию аккаунта в сессию,
    // что может привести к некоторым нестыковкам
    private void applyBattleResults() {
        for (Map.Entry entry: accountExperienceMap.entrySet()) {
            int accountId = (int) entry.getKey();
            int exp = (int) entry.getValue();
            Account account = new Account();
            account = DataBaseManager.getEntityById(account, accountId);
            account.applyExperience(exp);
            account.applyMoney(exp/10);
        }
    }

    public void forcedEndBattle() {
        stopTimer();
        applyBattleResults();
    }
}
