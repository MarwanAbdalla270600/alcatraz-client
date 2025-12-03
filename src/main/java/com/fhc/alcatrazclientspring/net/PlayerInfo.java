package com.fhc.alcatrazclientspring.net;

public class PlayerInfo {
    private String playerName;
    private String callbackUrl;

    public PlayerInfo() {}
    public PlayerInfo(String playerName, String callbackUrl) {
        this.playerName = playerName;
        this.callbackUrl = callbackUrl;
    }

    public String getPlayerName() { return playerName; }
    public String getCallbackUrl() { return callbackUrl; }

    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "playerName='" + playerName + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                '}';
    }
}
