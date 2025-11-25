package com.fhc.alcatrazclientspring.net;

import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class PlayerDirectory {

    private String selfName;
    private final Map<String, String> players = new HashMap<>();

    public String getSelfName() { return selfName; }

    public Map<String, String> getOtherPlayers() {
        return Collections.unmodifiableMap(players);
    }

    public void updatePlayers(Map<String, String> newPlayers, String selfName) {
        this.selfName = selfName;
        players.clear();
        players.putAll(newPlayers);
        players.remove(selfName);
    }
}
