package com.fhc.alcatrazclientspring.game;

import at.falb.games.alcatraz.api.Alcatraz;
import at.falb.games.alcatraz.api.MoveListener;
import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;
import com.fhc.alcatrazclientspring.ClientApplication;
import com.fhc.alcatrazclientspring.config.ClientConfig;
import com.fhc.alcatrazclientspring.net.PlayerDirectory;
import com.fhc.alcatrazclientspring.net.PlayerInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.util.*;

@Service
public class GameSessionService {

    private final Alcatraz game = ClientApplication.GAME;
    private final ClientConfig config;
    private final PlayerDirectory dir;
    private final PlayerMoveService moveService;
    private final LocalGameState local;
    private final TurnCounter turns;
    private final RestTemplate rest;

    public GameSessionService(
            ClientConfig cfg,
            PlayerDirectory dir,
            PlayerMoveService ms,
            LocalGameState local,
            TurnCounter turns,
            RestTemplate rest
    ) {
        this.config = cfg;
        this.dir = dir;
        this.moveService = ms;
        this.local = local;
        this.turns = turns;
        this.rest = rest;
    }

    public void start(List<PlayerInfo> infos) {
        System.out.println("Players from server: " + infos);

        String self = config.getSelf().getName();
        String selfUrl = config.getSelf().getCallbackBaseUrl();

        // --- 1) Players vom Server in Map packen ---
        Map<String, String> map = new HashMap<>();
        for (PlayerInfo i : infos) {
            map.put(i.getPlayerName(), i.getCallbackUrl());
        }

        // Directory aktualisieren: enthält self + others
        dir.updatePlayers(map, self, selfUrl);

        // --- 2) Spieler-Liste erstellen (self + others) ---
        List<String> players = new ArrayList<>();
        players.add(self);            // <- eigener Spieler
        players.addAll(map.keySet()); // <- andere Spieler
        players.sort(String::compareToIgnoreCase);

        int n = players.size();
        int myId = players.indexOf(self);

        if (n < 2) {
            System.err.println("FATAL: Not enough players (n=" + n + ")");
            return;
        }

        // --- 3) Verpasste Züge nachladen (Reconnect) ---
        try {
            int missingFrom = turns.current();
            GameStateDTO dto = rest.getForObject(
                    selfUrl + "/state?fromTurn=" + missingFrom,
                    GameStateDTO.class
            );

            if (dto != null && dto.getMoves() != null) {

                dto.getMoves().forEach(local::apply);

                for (MoveDTO m : dto.getMoves()) {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            Player pl = game.getPlayer(m.getPlayerId());
                            Prisoner pr = game.getPrisoner(m.getPrisonerId());
                            game.doMove(pl, pr, m.getRowOrCol(),
                                    m.getRow(), m.getCol());
                        } catch (Exception ignored) {}
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Reconnect State Load failed: " + e.getMessage());
        }

        // --- 4) GUI + Game Engine starten ---
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
