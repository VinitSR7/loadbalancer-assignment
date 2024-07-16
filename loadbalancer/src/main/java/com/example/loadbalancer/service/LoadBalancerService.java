package com.example.loadbalancer.service;

import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.strategy.LoadBalancingStrategy;
import com.example.loadbalancer.util.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class LoadBalancerService {

    private final CopyOnWriteArrayList<Server> servers;

    private final RestTemplate restTemplate;
    private LoadBalancingStrategy strategy;

    private static final Logger logger = LoggerFactory.getLogger(LoadBalancerService.class);

    public LoadBalancerService(CopyOnWriteArrayList<Server> servers, LoadBalancingStrategy strategy, RestTemplate restTemplate) {
        this.servers = servers;
        this.strategy = strategy;
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<String> forward() {
        Server server = this.getNextServer();
        String url = server.getUrl() + "/hello";
        try {
            logger.info("Forwarding request to {}", url);
            String response = String.valueOf(restTemplate.getForEntity(url, String.class));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to forward request to backend server after retries: {}", e.getMessage());
            return ResponseEntity.status(500).body("Failed to forward request to backend server");
        }

    }

    Server getNextServer() {
        try {
            logger.info("Selecting next server using {}", strategy.getClass().getSimpleName());
            Server server = strategy.chooseServer(servers);
            logger.info("Selected backend server: {}", server.getUrl());
            return server;
        } catch (Exception e) {
            logger.error("Error selecting backend server", e);
            throw new RuntimeException("Exception occured in selecting server", e);
        }
    }


    public void addServer(Server server) {
        servers.add(server);
    }

    public void removeServer(Server server) {
        servers.remove(server);
    }

    public void setStrategy(LoadBalancingStrategy loadBalancingStrategy) {
        this.strategy = loadBalancingStrategy;
    }
}
