package net.gutsoft.cardgame.websocket;


import net.gutsoft.cardgame.websocket.coders.PreBattleMessageDecoder;
import net.gutsoft.cardgame.websocket.coders.PreBattleMessageEncoder;
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

@ServerEndpoint(value = "/prebattle/{battleId}",
        decoders = PreBattleMessageDecoder.class,
        encoders = PreBattleMessageEncoder.class)
public class PreBattleEndpoint {

    // <battleId, list<>>
    private static Dictionary<Integer, List<PreBattleEndpoint>> battleToPlayerEndpointTable = new Hashtable<>();
//    private static Dictionary<Integer, List<String>> battleToHistoryTable = new Hashtable<>();
    Session session;

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

        List<PreBattleEndpoint> playerEndpoints = battleToPlayerEndpointTable.get(battleId);
        if (playerEndpoints == null) {
            playerEndpoints = new CopyOnWriteArrayList<>();
            battleToPlayerEndpointTable.put(battleId, playerEndpoints);
        }
        playerEndpoints.add(this);

//        System.out.println("websocket opened, endpoint for player added: " + playerEndpoints);

        // отправить всем игрокам обновление о добавлении этого игрока
        // при подключении игрок отправляет joimmessage, который при обработке рассылается другим игрокам
    }

    @OnClose
    public void onClose(Session session, CloseReason reason, @PathParam("battleId") int battleId) throws Exception {
        List<PreBattleEndpoint> playerEndpoints = battleToPlayerEndpointTable.get(battleId);
        if (playerEndpoints == null) {
            throw new Exception("Expected a valid room");
        }
        playerEndpoints.remove(this);
        if (playerEndpoints.size() == 0) {
            battleToPlayerEndpointTable.remove(playerEndpoints);
        }
    }

    @OnMessage
    public void onMessage(PreBattleMessage message, @PathParam("battleId") int battleId) {
        System.out.println("message received " + message.getUserName() + message.getType());

        if (message.getType() == MessagesTypes.BATTLESTART) {
            Map<Integer, Battle> battleMap = (Map<Integer, Battle>)ServletContextHolder.getServletContext().getAttribute("battles");
            Battle battle = battleMap.get(battleId);
            battle.start();
        }

        broadcast(message, battleId);
    }


    // полученное сообщение рассылается всем участникам битвы
    // однако при выходе из битвы сообщение также отправляется вышедшему игроку,
    // так как соединение еще не успевает закрыться и удалиться из списка соединений
    // тем не менее, проблем (ошибок) это на данный момент, похоже, не вызывает
    private void broadcast(PreBattleMessage message, int battleId) {
        for (PreBattleEndpoint player: battleToPlayerEndpointTable.get(battleId)) {
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

}
