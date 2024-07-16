package com.example.loadbalancer.controller;

import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.service.LoadBalancerService;
import com.example.loadbalancer.strategy.RandomStrategy;
import com.example.loadbalancer.strategy.RoundRobinStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.loadbalancer.util.Constants.RANDOM;
import static com.example.loadbalancer.util.Constants.ROUND_ROBIN;

@RestController
@RequestMapping("/api/loadbalancer")
public class LoadBalancerController {

    private static final Logger logger = LoggerFactory.getLogger(LoadBalancerController.class);
    private final LoadBalancerService loadBalancerService;

    @Autowired
    public LoadBalancerController(LoadBalancerService loadBalancerService) {
        this.loadBalancerService = loadBalancerService;
    }

    @GetMapping("/forward")
    public ResponseEntity<String> forwardRequest() {
        return loadBalancerService.forward();
    }

    @PostMapping("/addServer")
    public ResponseEntity<Void> addServer(@RequestBody Server server) {
        logger.info("Received request to add server: {}", server.getUrl());
        loadBalancerService.addServer(server);
        logger.info("Server added successfully: {}", server.getUrl());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/removeServer")
    public ResponseEntity<Void> removeServer(@RequestBody Server server) {
        logger.info("Received request to remove server: {}", server.getUrl());
        loadBalancerService.removeServer(server);
        logger.info("Server removed successfully: {}", server.getUrl());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/setStrategy")
    public ResponseEntity<Void> setStrategy(@RequestParam(name = "strategy") String strategy) {
        switch (strategy.toLowerCase()) {
            case ROUND_ROBIN:
                loadBalancerService.setStrategy(new RoundRobinStrategy());
                break;
            case RANDOM:
                loadBalancerService.setStrategy(new RandomStrategy());
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        logger.info("Load balancing strategy updated to: {}", strategy);
        return ResponseEntity.ok().build();
    }
}
