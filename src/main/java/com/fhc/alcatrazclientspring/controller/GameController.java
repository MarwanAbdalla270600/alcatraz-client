package com.fhc.alcatrazclientspring.controller;

import at.falb.games.alcatraz.api.Alcatraz;
import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;
import com.fhc.alcatrazclientspring.entities.GameStateDTO;
import com.fhc.alcatrazclientspring.entities.MoveDTO;
import com.fhc.alcatrazclientspring.service.LocalGameState;
import com.fhc.alcatrazclientspring.util.TurnCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

@RestController
public class GameController {

    private static final Logger log = LoggerFactory.getLogger(GameController.class);

    private final LocalGameState localGameState;
    private final Alcatraz alcatraz;
    private final TurnCounter turnCounter; // <-- wichtig

    public GameController(LocalGameState localGameState,
                          Alcatraz alcatraz,
                          TurnCounter turnCounter) {
        this.localGameState = localGameState;
        this.alcatraz = alcatraz;
        this.turnCounter = turnCounter;
    }

    @PostMapping("/move")
    public ResponseEntity<Void> receiveMove(@RequestBody MoveDTO move) {
        System.out.println(move);
        int currentTurn = turnCounter.current();

        // 🛑 Doppelzug oder veralteter Zug -> ignorieren
        if (move.getTurn() <= currentTurn) {
            log.info("Ignoring already known move (turn {} <= {}). {}", move.getTurn(), currentTurn, move);
            return ResponseEntity.ok().build();
        }

        // 🧩 Move speichern
        localGameState.applyMove(move);

        // 🔄 Turncounter aktualisieren
        turnCounter.setTo(move.getTurn());

        log.info("Applying remote move (new turn = {}): {}", move.getTurn(), move);

        SwingUtilities.invokeLater(() -> {
            try {
                Player p = alcatraz.getPlayer(move.getPlayerId());
                Prisoner pr = alcatraz.getPrisoner(move.getPrisonerId());
                alcatraz.doMove(p, pr, move.getRowOrCol(), move.getRow(), move.getCol());
            } catch (Exception e) {
                log.warn("Illegal remote move ignored: {}", move);
            }
        });

        return ResponseEntity.ok().build();
    }

    @GetMapping("/state")
    public GameStateDTO getState(@RequestParam("fromTurn") int fromTurn) {
        return new GameStateDTO(localGameState.getMissing(fromTurn));
    }
}
