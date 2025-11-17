package com.fhc.alcatrazclientspring.net;

import com.fhc.alcatrazclientspring.config.PlayerConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Kennt alle Spieler (Name -> Base-URL) sowie den eigenen Namen.
 * Wird initial aus PlayerConfig befüllt –
 * später könnt ihr das auch dynamisch vom Registrierungsserver setzen.
 */
@Component
public class PlayerDirectory {
    private final PlayerConfig config;

    private final Map<String, String> players = new HashMap<>();
    private String selfName;

    public PlayerDirectory(PlayerConfig config) {
        this.config = config;
    }

    @PostConstruct
    public void initFromConfig() {
        this.selfName = config.getSelf().getName();
        this.players.clear();
        this.players.putAll(config.getPlayers());
        // Eigener Spieler darf NICHT in der Liste stehen. Falls doch, rauswerfen:
        if (selfName != null) {
            this.players.remove(selfName);
        }
    }

    public String getSelfName() {
        return selfName;
    }

    /**
     * Alle anderen Spieler (ohne mich selbst).
     */
    public Map<String, String> getOtherPlayers() {
        return Collections.unmodifiableMap(players);
    }

    /**
     * Zum späteren Aktualisieren, falls der Registrierungsserver
     * euch zur Laufzeit eine neue Liste gibt.
     */
    public void updatePlayers(Map<String, String> newPlayers, String selfName) {
        this.selfName = selfName;
        this.players.clear();
        this.players.putAll(newPlayers);
        this.players.remove(selfName);
    }

}
