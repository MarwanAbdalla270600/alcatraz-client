package com.fhc.alcatrazclientspring.net;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PlayerDirectory {

    private String selfName;
    private String selfUrl;

    private final Map<String, String> players = new HashMap<>();

    public void updatePlayers(Map<String, String> newPlayers, String selfName, String selfUrl) {
        this.selfName = selfName;
        this.selfUrl  = selfUrl;

        players.clear();

        // alle vom Server
        players.putAll(newPlayers);

        // self *manuell* hinzufügen (Server sendet ihn NICHT!)
        players.put(selfName, selfUrl);
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

    public Map<String,String> getOtherPlayers() {
        Map<String,String> m = new HashMap<>(players);
        m.remove(selfName);
        return m;
    }
}
