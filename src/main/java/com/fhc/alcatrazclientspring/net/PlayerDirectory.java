package com.fhc.alcatrazclientspring.net;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class PlayerDirectory {

    private String selfName;
    private String selfUrl;

    private final Map<String, String> players = new HashMap<>();

    public void updatePlayers(Map<String, String> others, String selfName, String selfUrl) {
        this.selfName = selfName;
        this.selfUrl = selfUrl;

        players.clear();

        // eigene URL hinzufügen
        players.put(selfName, selfUrl);

        // andere Spieler hinzufügen
        players.putAll(others);
    }

    public int getTotalPlayers() {
        return players.size();
    }

    public List<String> getSortedPlayers() {
        return players.keySet().stream()
                .sorted(String::compareToIgnoreCase)
                .toList();
    }

    public String getPlayerName(int index) {
        return getSortedPlayers().get(index);
    }

    public String getPlayerUrl(String name) {
        return players.get(name);
    }

    public Map<String, String> getOtherPlayers() {
        Map<String, String> others = new HashMap<>(players);
        others.remove(selfName);
        return others;
    }
}
