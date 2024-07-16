package com.example.loadbalancer.controller;

import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.service.LoadBalancerService;
import com.example.loadbalancer.strategy.RandomStrategy;
import com.example.loadbalancer.strategy.RoundRobinStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoadBalancerController.class)
public class LoadBalancerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoadBalancerService loadBalancerService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testForwardRequest() throws Exception {
        when(loadBalancerService.forward()).thenReturn(ResponseEntity.ok("Forwarded"));

        mockMvc.perform(get("/api/loadbalancer/forward"))
                .andExpect(status().isOk())
                .andExpect(content().string("Forwarded"));
    }

    @Test
    public void testAddServer() throws Exception {
        Server server = new Server("http://localhost:8082");

        mockMvc.perform(post("/api/loadbalancer/addServer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(server)))
                .andExpect(status().isOk());

        verify(loadBalancerService, times(1)).addServer(server);
    }

    @Test
    public void testRemoveServer() throws Exception {
        Server server = new Server("http://localhost:8082");

        mockMvc.perform(delete("/api/loadbalancer/removeServer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(server)))
                .andExpect(status().isOk());

        verify(loadBalancerService, times(1)).removeServer(server);
    }

    @Test
    public void testSetStrategyRoundRobin() throws Exception {
        mockMvc.perform(post("/api/loadbalancer/setStrategy")
                        .param("strategy", "roundrobin"))
                .andExpect(status().isOk());

        verify(loadBalancerService, times(1)).setStrategy(any(RoundRobinStrategy.class));
    }

    @Test
    public void testSetStrategyRandom() throws Exception {
        mockMvc.perform(post("/api/loadbalancer/setStrategy")
                        .param("strategy", "random"))
                .andExpect(status().isOk());

        verify(loadBalancerService, times(1)).setStrategy(any(RandomStrategy.class));
    }

    @Test
    public void testSetStrategyInvalid() throws Exception {
        mockMvc.perform(post("/api/loadbalancer/setStrategy")
                        .param("strategy", "invalid_strategy"))
                .andExpect(status().isBadRequest());
    }
}