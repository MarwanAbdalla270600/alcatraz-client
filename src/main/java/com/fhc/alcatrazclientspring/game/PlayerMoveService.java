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

        // eigenen Zug broadcasten
        broadcaster.broadcast(move);

        // warten, bis nächster Spieler online ist
        waitForNextPlayerIfOffline();
    }

    private void waitForNextPlayerIfOffline() {
        int turn = counter.current();
        int totalPlayers = directory.getTotalPlayers();

        if (totalPlayers == 0) return; // Sicherheit

        int nextPlayerIndex = turn % totalPlayers;

        String nextPlayer = directory.getPlayerName(nextPlayerIndex);
        String nextUrl    = directory.getPlayerUrl(nextPlayer);

        while (!isOnline(nextUrl)) {
            System.out.println("Warte auf Spieler " + nextPlayer + "...");
            try { Thread.sleep(1500); } catch (Exception ignored) {}
        }

        System.out.println("Spieler " + nextPlayer + " ist wieder online.");
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
