package com.fhc.alcatrazclientspring.service;

import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;
import com.fhc.alcatrazclientspring.entities.MoveDTO;
import com.fhc.alcatrazclientspring.net.PlayerDirectory;
import com.fhc.alcatrazclientspring.util.TurnCounter;
import org.springframework.stereotype.Service;


/**
 * Service, der von der Alcatraz-UI aufgerufen wird, wenn ein Spieler
 * lokal einen Zug über die GUI durchgeführt hat.
 *
 * Dieser Service wandelt das UI-Move-Ereignis in ein MoveDTO um
 * und verteilt den Zug über MoveBroadcastService an alle anderen Clients.
 */
@Service
public class PlayerMoveService {

    private final MoveBroadcastService broadcastService;
    private final PlayerDirectory playerDirectory;
    private final TurnCounter turnCounter;

    public PlayerMoveService(MoveBroadcastService broadcastService,
                             PlayerDirectory playerDirectory,
                             TurnCounter turnCounter) {
        this.broadcastService = broadcastService;
        this.playerDirectory = playerDirectory;
        this.turnCounter = turnCounter;
    }

    /**
     * Wird von der UI aufgerufen (MoveListener),
     * wenn ein lokaler Spieler einen Zug gemacht hat.
     */
    public void onLocalMove(Player player,
                            Prisoner prisoner,
                            int rowOrCol,
                            int row,
                            int col) {

        int turn = turnCounter.nextTurn();

        MoveDTO move = new MoveDTO(
                turn,
                player.getId(),
                prisoner.getId(),
                rowOrCol,
                row,
                col
        );

        broadcastService.broadcastMove(move);
    }

    public TurnCounter getTurnCounter() {
        return turnCounter;
    }
}