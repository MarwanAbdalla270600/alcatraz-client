package com.fhc.alcatrazclientspring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "alcatraz")
public class ClientConfig {

    private final Self self = new Self();
    private final Registry registry = new Registry();

    public Self getSelf() { return self; }
    public Registry getRegistry() { return registry; }

    public static class Self {
        private String name;
        private String callbackBaseUrl;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCallbackBaseUrl() { return callbackBaseUrl; }
        public void setCallbackBaseUrl(String callbackBaseUrl) { this.callbackBaseUrl = callbackBaseUrl; }
    }

    public static class Registry {
        private List<String> nodes = new ArrayList<>();

        public List<String> getNodes() { return nodes; }
        public void setNodes(List<String> nodes) { this.nodes = nodes; }
    }
}
