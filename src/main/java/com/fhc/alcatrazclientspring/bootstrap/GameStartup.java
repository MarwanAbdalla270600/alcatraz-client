package com.fhc.alcatrazclientspring.bootstrap;

import at.falb.games.alcatraz.api.Alcatraz;
import at.falb.games.alcatraz.api.Player;
import at.falb.games.alcatraz.api.Prisoner;
import com.fhc.alcatrazclientspring.config.PlayerConfig;
import com.fhc.alcatrazclientspring.service.MoveBroadcastService;
import com.fhc.alcatrazclientspring.service.PlayerMoveService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
public class GameStartup implements CommandLineRunner {

    private final PlayerConfig playerConfig;
    private final PlayerMoveService moveService;
    private final MoveBroadcastService broadcastService;
    private final Alcatraz game;

    public GameStartup(PlayerConfig playerConfig,
                       PlayerMoveService moveService,
                       MoveBroadcastService broadcastService,
                       Alcatraz game) {
        this.playerConfig = playerConfig;
        this.moveService = moveService;
        this.broadcastService = broadcastService;
        this.game = game;
    }

    @Override
    public void run(String... args) {

        String selfName = playerConfig.getSelf().getName();

        // Spielerpool erstellen (eigener Name + konfigurierte Players)
        ArrayList<String> players = new ArrayList<>();
        players.add(selfName);
        players.addAll(playerConfig.getPlayers().keySet());

        // Alphabetisch sortieren -> deterministische Reihenfolge auf ALLEN Clients
        players.sort(String::compareToIgnoreCase);

        int noPlayers = players.size();
        int myPlayerId = players.indexOf(selfName);

        System.out.println("🎮 Starting Alcatraz UI for " + selfName + " → PlayerID = " + myPlayerId + "/" + noPlayers);

        // Jetzt init mit korrekter ID
        game.init(noPlayers, myPlayerId);

        // Namen in derselben alphabetischen Reihenfolge setzen
        for (int i = 0; i < players.size(); i++) {
            game.getPlayer(i).setName(players.get(i));
        }

        // Fenster-Titel setzen
        game.getWindow().setTitle("Alcatraz – " + selfName);

        // Listener für eigene Moves
        game.addMoveListener(new at.falb.games.alcatraz.api.MoveListener() {
            @Override
            public void moveDone(Player player, Prisoner prisoner, int rowOrCol, int row, int col) {
                moveService.onLocalMove(player, prisoner, rowOrCol, row, col);
            }

            @Override
            public void gameWon(Player player) {
                System.out.println("🏁 Winner: " + player.getName());
            }
        });

        // UI anzeigen
        game.showWindow();
        game.start();
    }
}
