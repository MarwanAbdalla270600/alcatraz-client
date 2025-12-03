package com.fhc.alcatrazclientspring.game;

import com.fhc.alcatrazclientspring.net.PlayerDirectory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MoveBroadcastService {

    private final PlayerDirectory directory;
    private final LocalGameState local;
    private final RestTemplate rest;

    public MoveBroadcastService(PlayerDirectory dir, LocalGameState local, RestTemplate rest) {
        this.directory = dir;
        this.local = local;
        this.rest = rest;
    }

    public void broadcast(MoveDTO move) {
        local.apply(move);

        for (Map.Entry<String,String> entry : directory.getOtherPlayers().entrySet()) {
            String url = entry.getValue() + "/move";
            try {
                rest.postForEntity(url, move, Void.class);
            } catch (Exception ignored) {}
        }
    }

    public void broadcastDisconnected(String name) {
        for (Map.Entry<String, String> e : directory.getOtherPlayers().entrySet()) {
            try {
                rest.postForEntity(e.getValue() + "/event/disconnected", name, Void.class);
            } catch (Exception ignored) {}
        }
    }

    public void broadcastReconnected(String name) {
        for (Map.Entry<String, String> e : directory.getOtherPlayers().entrySet()) {
            try {
                rest.postForEntity(e.getValue() + "/event/reconnected", name, Void.class);
            } catch (Exception ignored) {}
        }
    }
}
