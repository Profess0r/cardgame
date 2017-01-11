package net.gutsoft.cardgame.websocket;


import net.gutsoft.cardgame.websocket.coders.BattleMessageDecoder;
import net.gutsoft.cardgame.websocket.coders.BattleMessageEncoder;
import net.gutsoft.cardgame.websocket.coders.PreBattleMessageDecoder;
import net.gutsoft.cardgame.websocket.coders.PreBattleMessageEncoder;
import net.gutsoft.cardgame.websocket.messages.BattleMessage;
import net.gutsoft.cardgame.websocket.messages.PreBattleMessage;
import net.gutsoft.cardgame.entity.Battle;
import net.gutsoft.cardgame.util.ServletContextHolder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint(value = "/battle/{battleId}",
        decoders = BattleMessageDecoder.class,
        encoders = BattleMessageEncoder.class)
public class BattleEndpoint {

    // <battleId, list<>>
    private static Dictionary<Integer, List<BattleEndpoint>> battleToPlayerEndpointTable = new Hashtable<>();
    private Session session;
    private int accountId;

    public static Dictionary<Integer, List<BattleEndpoint>> getBattleToPlayerEndpointTable() {
        return battleToPlayerEndpointTable;
    }


    @OnError
    public void onError(Session session, Throwable t, @PathParam("battleId") int battleId) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        t.printStackTrace();
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig, @PathParam("battleId") int battleId) {
        this.session = session;

        List<BattleEndpoint> playerEndpoints = battleToPlayerEndpointTable.get(battleId);
        if (playerEndpoints == null) {
            playerEndpoints = new CopyOnWriteArrayList<>();
            battleToPlayerEndpointTable.put(battleId, playerEndpoints);
        }
        playerEndpoints.add(this);

    }

    @OnClose
    public void onClose(Session session, CloseReason reason, @PathParam("battleId") int battleId) throws Exception {
        List<BattleEndpoint> playerEndpoints = battleToPlayerEndpointTable.get(battleId);
        if (playerEndpoints == null) {
            throw new Exception("Expected a valid room");
        }
        playerEndpoints.remove(this);
        if (playerEndpoints.size() == 0) {
            battleToPlayerEndpointTable.remove(playerEndpoints);
        }
    }

    @OnMessage
    public void onMessage(BattleMessage message, @PathParam("battleId") int battleId) {
        System.out.println("message received " + message.getUserName() + message.getType());

        if (message.getType() == MessagesTypes.PLAYERTURN) {
            Map<Integer, Battle> battleMap = (Map<Integer, Battle>)ServletContextHolder.getServletContext().getAttribute("battles");
            Battle battle = battleMap.get(battleId);

            battle.applyPlayerTurn(message); //return true => turnEnd (battleEnd)

            // apply
            // check all ready
                //yes => battle.executeTurn()
            //check result
            // send individual messages
            // создать отдельный (статический?) метод для этих целей (в battle?), чтобы это не выполнялось в ендпойнте отдельного игрока


//            if (battle.isTurnEnded()) {
//                broadcast(new BattleMessage(MessagesTypes.TURNEND, "", battle.getTurnLogAsString()), battleId);
//                battle.clearTurnLog();
//            }
//
//            if (battle.isBattleEnded()) {
////                battleMap.remove(battleId, battle);
//                broadcast(new BattleMessage(MessagesTypes.BATTLEEND, "", ""), battleId);
//            }

        } else if (message.getType() == MessagesTypes.MESSAGE) {
            broadcast(message, battleId);
        } else if (message.getType() == MessagesTypes.USERJOIN) {
            this.accountId = message.getAccountId();
        } else if (message.getType() == MessagesTypes.USERLEAVE) {
            Map<Integer, Battle> battleMap = (Map<Integer, Battle>)ServletContextHolder.getServletContext().getAttribute("battles");
            Battle battle = battleMap.get(battleId);

            battle.removePlayer(message.getAccountId());
            // remove и так происходит в LeaveBattleController,
            // но здесь он тоже пока остается на случай если удаление не успеет осуществиться до resetTurn()
            battle.resetTurn();

            broadcast(message, battleId);

            if (battle.getPlayerList().size() < 2) {
//                battleMap.remove(battleId, battle);
                broadcast(new BattleMessage(MessagesTypes.BATTLEEND, "", ""), battleId);
            }
        } else {
            System.out.println(">>> unknown message received");
        }

    }


    public void broadcast(BattleMessage message, int battleId) {
        for (BattleEndpoint player: battleToPlayerEndpointTable.get(battleId)) {
            try {
                player.session.getBasicRemote().sendObject(message);
                System.out.println("message sent to client");
            } catch (IOException e) {
                e.printStackTrace();
                // обработка Exception-а нужна, но над ней еще лучше подумать и еще посмотреть какой именно Exception вылетит
//                battleToPlayerEndpointTable.remove(this);
//                try {
//                    player.session.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
            } catch (EncodeException e) {
                e.printStackTrace();
            }
        }
    }

    // отправка индивидуальных сообщений
    public void sendMessage(BattleMessage message, int battleId, int accountId) {
        for (BattleEndpoint player: battleToPlayerEndpointTable.get(battleId)) {
            if (player.accountId == accountId) {
                try {
                    player.session.getBasicRemote().sendObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EncodeException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        System.out.println("no such accountId");
    }


}
