package com.fhc.alcatrazclientspring.net;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PlayerDirectory {

    private String selfName;

    // Name → Callback-URL
    private final Map<String, String> players = new HashMap<>();

    // Name → Online-Status
    private final Map<String, Boolean> online = new HashMap<>();

    public void updatePlayers(Map<String, String> newPlayers,
                              String selfName,
                              String selfUrl) {

        this.selfName = selfName;

        players.clear();
        players.putAll(newPlayers);
        players.put(selfName, selfUrl);

        // Online-Status initial setzen
        online.clear();
        for (String p : players.keySet()) {
            online.put(p, true);
        }
    }

    public int getTotalPlayers() {
        return players.size();
    }

    public List<String> getSortedPlayers() {
        return players.keySet().stream()
                .sorted(String::compareToIgnoreCase)
                .toList();
    }

    public String getPlayerUrl(String name) {
        return players.get(name);
    }

    public Map<String,String> getOtherPlayers() {
        Map<String,String> m = new HashMap<>(players);
        m.remove(selfName);
        return m;
    }

    // Online-Status
    public boolean isOnline(String name) {
        return online.getOrDefault(name, true);
    }

    public void markOnline(String name) {
        online.put(name, true);
    }

    public void markOffline(String name) {
        online.put(name, false);
    }
}
