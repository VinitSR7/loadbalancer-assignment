package com.example.loadbalancer.service;

import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.strategy.LoadBalancingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class LoadBalancerServiceTest {

    @Mock
    private LoadBalancingStrategy strategy;

    private CopyOnWriteArrayList<Server> servers;

    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private LoadBalancerService loadBalancerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servers = new CopyOnWriteArrayList<>();
        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        loadBalancerService = new LoadBalancerService(servers, strategy, restTemplate);
    }

    @Test
    void testForwardSuccess() {
        Server server = new Server("http://localhost:8082");
        servers.add(server);
        when(strategy.chooseServer(servers)).thenReturn(server);

        mockServer.expect(requestTo("http://localhost:8082/hello"))
                .andRespond(withSuccess("Hello from Server 1", MediaType.TEXT_PLAIN));

        ResponseEntity<String> response = loadBalancerService.forward();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Hello from Server 1"));
        mockServer.verify();
    }

    @Test
    void testForwardFailure() {
        Server server = new Server("http://localhost:8082");
        servers.add(server);
        when(strategy.chooseServer(servers)).thenReturn(server);

        mockServer.expect(requestTo("http://localhost:8082/hello"))
                .andRespond(withServerError());

        ResponseEntity<String> response = loadBalancerService.forward();

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Failed to forward request to backend server", response.getBody());
        mockServer.verify();
    }

    @Test
    void testGetNextServer() {
        Server server1 = new Server("http://localhost:8082");
        Server server2 = new Server("http://localhost:8083");
        servers.add(server1);
        servers.add(server2);

        when(strategy.chooseServer(servers)).thenReturn(server1).thenReturn(server2);

        assertEquals(server1, loadBalancerService.getNextServer());
        assertEquals(server2, loadBalancerService.getNextServer());
    }

    @Test
    void testAddServer() {
        Server server = new Server("http://localhost:8082");
        loadBalancerService.addServer(server);

        assertTrue(servers.contains(server));
    }

    @Test
    void testRemoveServer() {
        Server server = new Server("http://localhost:8082");
        servers.add(server);

        loadBalancerService.removeServer(server);

        assertFalse(servers.contains(server));
    }

    @Test
    void testSetStrategy() {
        LoadBalancingStrategy newStrategy = mock(LoadBalancingStrategy.class);
        loadBalancerService.setStrategy(newStrategy);

    }
}