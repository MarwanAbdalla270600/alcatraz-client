package com.fhc.alcatrazclientspring.game;

import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;
import org.springframework.stereotype.Service;

@Service
public class PlayerMoveService {

    private final MoveBroadcastService broadcaster;
    private final TurnCounter counter;

    public PlayerMoveService(MoveBroadcastService b, TurnCounter c) {
        this.broadcaster = b;
        this.counter = c;
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
    }
}
