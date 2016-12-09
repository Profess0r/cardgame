package net.gutsoft.cardgame.websocket.messages;

public class BattleMessage extends Message {

    private String card;
    private String target;

    public BattleMessage() {
    }

    public BattleMessage(int type, String userName) {
        this.type = type;
        this.userName = userName;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
