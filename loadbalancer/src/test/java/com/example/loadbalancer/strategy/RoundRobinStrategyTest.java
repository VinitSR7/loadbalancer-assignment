package com.example.loadbalancer.strategy;

import com.example.loadbalancer.model.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundRobinStrategyTest {

    private RoundRobinStrategy roundRobinStrategy;
    private CopyOnWriteArrayList<Server> servers;

    @BeforeEach
    void setUp() {
        roundRobinStrategy = new RoundRobinStrategy();
        servers = new CopyOnWriteArrayList<>();
        servers.add(new Server("http://localhost:8081"));
        servers.add(new Server("http://localhost:8082"));
        servers.add(new Server("http://localhost:8083"));
    }

    @Test
    void testChooseServer() {
        Server server1 = roundRobinStrategy.chooseServer(servers);
        Server server2 = roundRobinStrategy.chooseServer(servers);
        Server server3 = roundRobinStrategy.chooseServer(servers);
        Server server4 = roundRobinStrategy.chooseServer(servers);

        assertEquals("http://localhost:8081", server1.getUrl());
        assertEquals("http://localhost:8082", server2.getUrl());
        assertEquals("http://localhost:8083", server3.getUrl());
        assertEquals("http://localhost:8081", server4.getUrl());
    }

    @Test
    void testRoundRobinResetsIndex() {
        for (int i = 0; i < 6; i++) {
            roundRobinStrategy.chooseServer(servers);
        }

        Server server = roundRobinStrategy.chooseServer(servers);
        assertEquals("http://localhost:8081", server.getUrl());
    }
}
