package com.fhc.alcatrazclientspring.service;

import com.fhc.alcatrazclientspring.entities.GameStateDTO;
import com.fhc.alcatrazclientspring.entities.MoveDTO;
import com.fhc.alcatrazclientspring.exception.ResyncException;
import com.fhc.alcatrazclientspring.net.PlayerDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MoveBroadcastService {

    private static final Logger log = LoggerFactory.getLogger(MoveBroadcastService.class);

    private final LocalGameState localGameState;
    private final PlayerDirectory playerDirectory;
    private final RestTemplate restTemplate;

    public MoveBroadcastService(LocalGameState localGameState,
                                PlayerDirectory playerDirectory,
                                RestTemplate restTemplate) {
        this.localGameState = localGameState;
        this.playerDirectory = playerDirectory;
        this.restTemplate = restTemplate;
    }

    public void broadcastMove(MoveDTO move) {
        localGameState.applyMove(move);

        for (Map.Entry<String, String> entry : playerDirectory.getOtherPlayers().entrySet()) {
            String url = entry.getValue() + "/move";

            // Verhindern: eigener Move soll nicht an uns zurückgehen
            if (entry.getKey().equals(playerDirectory.getSelfName())) {
                continue;
            }

            try {
                restTemplate.postForEntity(url, move, Void.class);
                System.out.println(url + " " + move);
            } catch (Exception ex) {
                log.warn("Failed to send move {} to {}", move, url);
            }
        }
    }

    public void resyncFrom(String otherBaseUrl) {
        int lastTurn = localGameState.getLastTurn();
        try {
            GameStateDTO state = restTemplate.getForObject(
                    otherBaseUrl + "/state?fromTurn=" + lastTurn, GameStateDTO.class
            );
            if (state != null && state.getMoves() != null) {
                state.getMoves().forEach(localGameState::applyMove);
            }
        } catch (Exception e) {
            throw new ResyncException("Resync failed from: " + otherBaseUrl, e);
        }
    }
}
