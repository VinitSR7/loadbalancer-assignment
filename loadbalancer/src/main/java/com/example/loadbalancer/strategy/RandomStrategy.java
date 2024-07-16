package com.example.loadbalancer.strategy;

import com.example.loadbalancer.model.Server;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RandomStrategy implements LoadBalancingStrategy {

    private final Random random = new Random();

    @Override
    public Server chooseServer(CopyOnWriteArrayList<Server> servers) {
        int randomIndex = random.nextInt(servers.size());
        return servers.get(randomIndex);
    }
}