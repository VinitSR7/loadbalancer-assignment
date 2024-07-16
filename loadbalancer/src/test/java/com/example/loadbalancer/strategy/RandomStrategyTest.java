package com.example.loadbalancer.strategy;

import com.example.loadbalancer.model.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RandomStrategyTest {

    private RandomStrategy randomStrategy;
    private CopyOnWriteArrayList<Server> servers;

    @BeforeEach
    void setUp() {
        randomStrategy = new RandomStrategy();
        servers = new CopyOnWriteArrayList<>();
        servers.add(new Server("http://localhost:8081"));
        servers.add(new Server("http://localhost:8082"));
        servers.add(new Server("http://localhost:8083"));
    }

    @Test
    void testChooseServer() {
        Server server = randomStrategy.chooseServer(servers);

        assertNotNull(server);
    }

    @Test
    void testChooseServerWithMock() {
        RandomStrategy mockedRandomStrategy = mock(RandomStrategy.class);
        when(mockedRandomStrategy.chooseServer(servers)).thenReturn(servers.get(1));

        Server server = mockedRandomStrategy.chooseServer(servers);

        assertNotNull(server);
        assert(server.getUrl().equals("http://localhost:8082"));
    }
}
