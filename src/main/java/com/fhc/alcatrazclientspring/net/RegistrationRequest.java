package com.fhc.alcatrazclientspring.net;

public class RegistrationRequest {

    private String playerName;
    private String callbackUrl;

    public RegistrationRequest() {}

    public RegistrationRequest(String playerName, String callbackUrl) {
        this.playerName = playerName;
        this.callbackUrl = callbackUrl;
    }

    public String getPlayerName() { return playerName; }
    public String getCallbackUrl() { return callbackUrl; }

    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setCallbackUrl(String callbackUrl) { this.callbackUrl = callbackUrl; }
}
