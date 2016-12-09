package net.gutsoft.cardgame.websocket.coders;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gutsoft.cardgame.websocket.messages.BattleMessage;
import net.gutsoft.cardgame.websocket.messages.PreBattleMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class BattleMessageEncoder implements Encoder.Text<BattleMessage> {
    @Override
    public String encode(BattleMessage message) throws EncodeException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
