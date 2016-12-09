package net.gutsoft.cardgame.websocket.coders;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.gutsoft.cardgame.websocket.messages.PreBattleMessage;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;

public class PreBattleMessageDecoder implements Decoder.Text<PreBattleMessage> {

    @Override
    public PreBattleMessage decode(String msg) throws DecodeException {
        PreBattleMessage message = null;

        System.out.println("coded message received " + msg);

        if (willDecode(msg)) {
            try {
                JsonObject obj = Json.createReader(new StringReader(msg)).readObject();
                ObjectMapper mapper = new ObjectMapper();

                int type = obj.getInt("type");

                message = mapper.readValue(msg, PreBattleMessage.class);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("message was decoded " + message);

        return message;
    }

    @Override
    public boolean willDecode(String msg) {
        try {
            System.out.println("willDecode execute " + msg);
            System.out.println(msg.getClass());
            Json.createReader(new StringReader(msg));
            System.out.println("true");
            return true;
        } catch (JsonException e) {
            System.out.println("false");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
