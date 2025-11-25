package com.fhc.alcatrazclientspring.game;

import at.falb.games.alcatraz.api.Alcatraz;
import at.falb.games.alcatraz.api.MoveListener;
import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;
import com.fhc.alcatrazclientspring.ClientApplication;
import com.fhc.alcatrazclientspring.config.ClientConfig;
import com.fhc.alcatrazclientspring.game.PlayerMoveService;
import com.fhc.alcatrazclientspring.net.PlayerDirectory;
import com.fhc.alcatrazclientspring.net.PlayerInfo;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameSessionService {

    private final Alcatraz game = ClientApplication.GAME;
    private final ClientConfig config;
    private final PlayerDirectory dir;
    private final PlayerMoveService moveService;

    public GameSessionService(ClientConfig cfg, PlayerDirectory dir, PlayerMoveService ms) {
        this.config = cfg;
        this.dir = dir;
        this.moveService = ms;
    }

    public void start(List<PlayerInfo> infos) {

        String self = config.getSelf().getName();

        Map<String, String> map = new HashMap<>();
        for (PlayerInfo i : infos) {
            map.put(i.getPlayerName(), i.getCallbackUrl());
        }

        dir.updatePlayers(map, self);

        List<String> players = new ArrayList<>();
        players.add(self);
        players.addAll(map.keySet());
        players.sort(String::compareToIgnoreCase);

        int n = players.size();
        int myId = players.indexOf(self);

        // GUI IM EDT ERZEUGEN!
        SwingUtilities.invokeLater(() -> {
            try {
                game.init(n, myId);

                for (int i = 0; i < players.size(); i++) {
                    game.getPlayer(i).setName(players.get(i));
                }

                game.addMoveListener(new MoveListener() {
                    @Override
                    public void moveDone(Player player, Prisoner prisoner,
                                         int rowOrCol, int row, int col) {
                        moveService.onMove(player, prisoner, rowOrCol, row, col);
                    }

                    @Override
                    public void gameWon(Player player) {
                        System.out.println("Winner: " + player.getName());
                    }
                });

                game.getWindow().setTitle("Alcatraz – " + self);
                game.showWindow();
                game.start();

            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println("GUI init failed for " + self);
            }
        });
    }

    public Alcatraz getGame() {
        return game;
    }
}
