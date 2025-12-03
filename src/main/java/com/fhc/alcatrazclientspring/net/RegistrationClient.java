package com.fhc.alcatrazclientspring.net;

import com.fhc.alcatrazclientspring.config.ClientConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class RegistrationClient {

    private final ClientConfig config;
    private final RestTemplate rest;

    public RegistrationClient(ClientConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.rest = restTemplate;
    }

    // -------------------------------------------------------
    //  Helper: führt einen POST auf allen Nodes aus
    // -------------------------------------------------------
    private boolean tryPostOnAllNodes(String path, Object body) {

        for (String node : config.getRegistry().getNodes()) {

            String endpoint = node + path;
            System.out.println("Trying: " + endpoint);

            try {
                rest.postForEntity(endpoint, body, Void.class);
                System.out.println("SUCCESS at: " + endpoint);
                return true;
            } catch (Exception ex) {
                System.out.println("FAILED at: " + endpoint + " → " + ex.getMessage());
            }
        }

        return false;
    }

    // -------------------------------------------------------
    //  Helper: führt einen DELETE auf allen Nodes aus
    // -------------------------------------------------------
    private boolean tryDeleteOnAllNodes(String path) {

        for (String node : config.getRegistry().getNodes()) {

            String endpoint = node + path;
            System.out.println("Trying DELETE: " + endpoint);

            try {
                rest.delete(endpoint);
                System.out.println("SUCCESS at: " + endpoint);
                return true;
            } catch (Exception ex) {
                System.out.println("FAILED at: " + endpoint + " → " + ex.getMessage());
            }
        }

        return false;
    }

    // -------------------------------------------------------
    //  Registrierung
    // -------------------------------------------------------
    public void register() {

        String name = config.getSelf().getName();
        String url  = config.getSelf().getCallbackBaseUrl();

        RegistrationRequest req = new RegistrationRequest(name, url);

        boolean ok = tryPostOnAllNodes("/players/register", req);

        if (!ok) {
            throw new IllegalStateException("Could not register at ANY registry node!");
        }
    }

    // -------------------------------------------------------
    //  Spiel starten (Lobby)
    // -------------------------------------------------------
    public void requestStart() {

        boolean ok = tryPostOnAllNodes("/players/game/start", null);

        if (!ok) {
            throw new IllegalStateException("Could not start game at ANY registry node!");
        }
    }

    // -------------------------------------------------------
    //  Unregister
    // -------------------------------------------------------
    public void requestUnregister() {

        String name = config.getSelf().getName();

        boolean ok = tryDeleteOnAllNodes("/players/unregister/" + name);

        if (!ok) {
            throw new IllegalStateException("Could not unregister at ANY registry node!");
        }

        System.exit(0);
    }
}
