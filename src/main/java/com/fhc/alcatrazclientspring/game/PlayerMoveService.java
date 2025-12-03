package com.fhc.alcatrazclientspring.game;

import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;
import com.fhc.alcatrazclientspring.net.PlayerDirectory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PlayerMoveService {

    private final MoveBroadcastService broadcaster;
    private final TurnCounter counter;
    private final PlayerDirectory directory;
    private final RestTemplate rest;

    public PlayerMoveService(
            MoveBroadcastService broadcaster,
            TurnCounter counter,
            PlayerDirectory directory,
            RestTemplate rest
    ) {
        this.broadcaster = broadcaster;
        this.counter = counter;
        this.directory = directory;
        this.rest = rest;
    }

    public void onMove(Player p, Prisoner pr, int rowOrCol, int row, int col) {
        MoveDTO move = new MoveDTO(
                counter.nextTurn(),
                p.getId(),
                pr.getId(),
                rowOrCol,
                row,
                col
        );

        broadcaster.broadcast(move);

        waitUntilCurrentPlayerOnline();
    }

    private void waitUntilCurrentPlayerOnline() {

        while (true) {
            int turn = counter.current();
            int idx = turn % directory.getTotalPlayers();

            String player = directory.getSortedPlayers().get(idx);
            String url = directory.getPlayerUrl(player);

            // Spieler erreichbar?
            if (isOnline(url)) {

                // wenn vorher offline → reconnect event
                if (!directory.isOnline(player)) {
                    directory.markOnline(player);
                    broadcaster.broadcastReconnected(player);
                }
                return; // weiter spielen
            }

            // Spieler jetzt offline geworden?
            if (directory.isOnline(player)) {
                directory.markOffline(player);
                broadcaster.broadcastDisconnected(player);
            }

            System.out.println("Warte auf Spieler " + player + "...");
            try { Thread.sleep(1500); } catch (Exception ignored) {}
        }
    }

    private boolean isOnline(String baseUrl) {
        try {
            rest.postForEntity(baseUrl + "/health", null, Void.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
