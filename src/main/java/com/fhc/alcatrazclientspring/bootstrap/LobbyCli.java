package com.fhc.alcatrazclientspring.bootstrap;

import com.fhc.alcatrazclientspring.net.RegistrationClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class LobbyCli implements CommandLineRunner {

    private final RegistrationClient reg;

    public LobbyCli(RegistrationClient r) {
        this.reg = r;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Connecting to registry...");
        reg.register();

        System.out.println("Waiting for game start...");
        System.out.println("Type 'start' to request game start.");

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("start")) {
                reg.requestStart();
            }
        }
    }
}
