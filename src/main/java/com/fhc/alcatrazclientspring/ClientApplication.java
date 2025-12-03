package com.fhc.alcatrazclientspring;

import at.falb.games.alcatraz.api.Alcatraz;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {
    public static Alcatraz GAME;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        GAME = new Alcatraz();   // erzeugt AWT früh genug im Main
        SpringApplication.run(ClientApplication.class, args);
    }
}
