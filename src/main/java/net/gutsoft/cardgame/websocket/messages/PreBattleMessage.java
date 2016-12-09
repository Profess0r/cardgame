package net.gutsoft.cardgame.websocket.messages;

public class PreBattleMessage extends Message {

    private int userLevel;

    public PreBattleMessage() {
    }

    public PreBattleMessage(int type, String userName) {
        this.type = type;
        this.userName = userName;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

}
