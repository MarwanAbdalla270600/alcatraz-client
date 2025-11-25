package com.fhc.alcatrazclientspring.controller;

import at.falb.games.alcatraz.api.Alcatraz;
import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;
import com.fhc.alcatrazclientspring.game.*;
import com.fhc.alcatrazclientspring.net.PlayerInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

@RestController
public class GameController {

    private final LocalGameState local;
    private final TurnCounter turns;
    private final GameSessionService session;

    public GameController(LocalGameState local, TurnCounter t, GameSessionService s) {
        this.local = local;
        this.turns = t;
        this.session = s;
    }

    @PostMapping("/move")
    public ResponseEntity<Void> move(@RequestBody MoveDTO move) {

        if (move.getTurn() <= turns.current()) return ResponseEntity.ok().build();

        local.apply(move);
        turns.setTo(move.getTurn());

        SwingUtilities.invokeLater(() -> {
            try {
                Alcatraz game = session.getGame();    // ← hier holen!
                Player p = game.getPlayer(move.getPlayerId());
                Prisoner pr = game.getPrisoner(move.getPrisonerId());
                game.doMove(p, pr, move.getRowOrCol(), move.getRow(), move.getCol());
            } catch (Exception ignored) {}
        });

        return ResponseEntity.ok().build();
    }

    @PostMapping("/start")
    public ResponseEntity<Void> start(@RequestBody java.util.List<PlayerInfo> players) {
        session.start(players);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/state")
    public GameStateDTO state(@RequestParam int fromTurn) {
        return new GameStateDTO(local.getMissing(fromTurn));
    }
}
