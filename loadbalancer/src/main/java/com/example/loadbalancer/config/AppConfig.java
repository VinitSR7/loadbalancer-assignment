package com.example.loadbalancer.config;

import com.example.loadbalancer.model.Server;
import com.example.loadbalancer.strategy.LoadBalancingStrategy;
import com.example.loadbalancer.strategy.RoundRobinStrategy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
public class AppConfig {

    @Bean
    public LoadBalancingStrategy loadBalancingAlgorithm() {
        return new RoundRobinStrategy(); // Default algorithm
    }

    @Bean
    public CopyOnWriteArrayList<Server> backendServers() {
        CopyOnWriteArrayList<Server> servers = new CopyOnWriteArrayList<>();
        Server server1 = new Server("http://localhost:8082");
        Server server2 = new Server("http://localhost:8083");
        Server server3 = new Server("http://localhost:8084");
        servers.add(server1);
        servers.add(server2);
        servers.add(server3);

        return servers;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().build();
    }

}
