package com.fhc.alcatrazclientspring.net;

import com.fhc.alcatrazclientspring.config.ClientConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistrationClient {

    private final ClientConfig config;
    private final RestTemplate restTemplate;

    public RegistrationClient(ClientConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    private String getRegistryNode() {
        return config.getRegistry().getNodes().getFirst();
    }

    public void register() {
        String url = getRegistryNode() + "/players/register";
        restTemplate.postForEntity(url,
                new RegistrationRequest(
                        config.getSelf().getName(),
                        config.getSelf().getCallbackBaseUrl()
                ),
                Void.class);
        System.out.println("Registered at server: " + url);
    }

    public void requestStart() {
        String url = getRegistryNode() + "/players/game/start";
        restTemplate.postForEntity(url, null, Void.class);
        System.out.println("Requested game start.");
    }

    public void requestUnregister() {
        String name = config.getSelf().getName();
        String url = getRegistryNode() + "/players/unregister/" + name;
        restTemplate.delete(url);
        System.out.println("Unregistered.");
        System.exit(0);
    }
}
