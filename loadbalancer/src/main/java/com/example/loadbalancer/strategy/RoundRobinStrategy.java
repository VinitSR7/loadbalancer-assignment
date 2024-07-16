package com.example.loadbalancer.strategy;

import com.example.loadbalancer.model.Server;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinStrategy implements LoadBalancingStrategy {

    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public Server chooseServer(CopyOnWriteArrayList<Server> servers) {
        int currentIndex = Math.abs(index.getAndIncrement() % servers.size());
        return servers.get(currentIndex);
    }
}