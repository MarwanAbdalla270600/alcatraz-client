package com.fhc.alcatrazclientspring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Wird über application.yml konfiguriert.
 * Beispiel:
 * alcatraz:
 *   self:
 *     name: "Alice"
 *   players:
 *     Bob: "http://localhost:8082"
 *     Carol: "http://localhost:8083"
 */

@Component
@ConfigurationProperties(prefix = "alcatraz")
public class PlayerConfig {
    private Self self = new Self();
    private Map<String, String> players = new HashMap<>();

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }

    public Map<String, String> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, String> players) {
        this.players = players;
    }

    public static class Self {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
