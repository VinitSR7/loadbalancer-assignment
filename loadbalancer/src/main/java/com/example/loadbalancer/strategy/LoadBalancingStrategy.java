package com.example.loadbalancer.strategy;

import com.example.loadbalancer.model.Server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public interface LoadBalancingStrategy {
    Server chooseServer(CopyOnWriteArrayList<Server> servers);
}