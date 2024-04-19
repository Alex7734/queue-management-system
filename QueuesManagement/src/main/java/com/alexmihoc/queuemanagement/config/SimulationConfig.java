package com.alexmihoc.queuemanagement.config;


import com.alexmihoc.queuemanagement.models.strategy.QueueAssignmentStrategy;
import com.alexmihoc.queuemanagement.models.strategy.ShortestQueueStrategy;

public class SimulationConfig {
    public static int CLIENT_GENERATION_INTERVAL_MS = 1000;

    private static QueueAssignmentStrategy strategy = new ShortestQueueStrategy();

    public static QueueAssignmentStrategy getStrategy() {
        return strategy;
    }

    public static void setStrategy(QueueAssignmentStrategy strategy) {
        SimulationConfig.strategy = strategy;
    }
}

