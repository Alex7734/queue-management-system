package com.alexmihoc.queuemanagement.models;

import com.alexmihoc.queuemanagement.config.SimulationConfig;
import com.alexmihoc.queuemanagement.utils.SimulationLogger;

import java.util.*;
import java.util.stream.IntStream;

public class QueueManager {
    private final List<ServiceQueue> queues;
    private final List<Client> allClients;
    private final HashSet<Client> processedClients;
    private int simulationTime = 1;
    private int totalSimulationTime;
    private long totalServiceTime = 0;
    private int peakHour = 0;
    private int maxClientsInHour = 0;
    private Timer timer = new Timer();

    public QueueManager(int numQueues, int totalClients) {
        this.queues = new ArrayList<>();
        this.allClients = new ArrayList<>();
        this.processedClients = new HashSet<>();
        IntStream.range(0, numQueues).forEach(i -> {
            ServiceQueue queue = new ServiceQueue(i + 1, processedClients);
            queue.start();
            queues.add(queue);
        });
        IntStream.range(0, totalClients).forEach(j -> allClients.add(Client.generateRandomClient()));
    }

    public void startSimulation(int totalSimulationTime, int clientGenerationInterval) {
        timerGuard();
        this.timer = new Timer();
        this.totalSimulationTime = totalSimulationTime;
        SimulationLogger.init();
        TimerTask task = new SimulationTask(this);
        this.timer.scheduleAtFixedRate(task, 0, clientGenerationInterval);
    }

    public void printSimulationStatus() {
        int clientsLeft = allClients.size() - processedClients.size();
        SimulationLogger.log("Simulation Time: " + simulationTime);
        SimulationLogger.log("Processed clients: " + processedClients.size());
        SimulationLogger.log("Clients left: " + clientsLeft);
    }


    public void clientAssignment() {
        for (Client client : allClients) {
            if (client.getArrivalTime() == simulationTime && !processedClients.contains(client)) {
                int queue = SimulationConfig.getStrategy().assignClientToQueue(queues, client);
                if (queue == -1) {
                    SimulationLogger.log("Client ID: " + client.getId() + " could not be assigned to any queue");
                    return;
                }
                SimulationLogger.log("Assigning Client ID: " + client.getId() + " to a queue " + queue);
                totalServiceTime += client.getServiceTime();
            }
        }
    }

    public void printQueueState() {
        for (ServiceQueue queue : queues) {
            SimulationLogger.log("Queue " + queue.getQueueNumber() + ": " + queue.getQueueState());
        }
    }

    public void stopSimulation() {
        timerGuard();
        SimulationLogger.close();
    }

    public void statistics() {
        long totalWaitingTime = 0;
        for (Client client : processedClients) {
            totalWaitingTime += client.getWaitingTime();
        }
        long averageWaitingTime =  totalWaitingTime / processedClients.size();
        long averageServiceTime =  totalServiceTime / processedClients.size();
        SimulationLogger.log("Average waiting time: " + averageWaitingTime);
        SimulationLogger.log("Average service time: " + averageServiceTime);
        SimulationLogger.log("Peak hour: " + peakHour);
    }

    public boolean allClientsProcessed() {
        return processedClients.size() == allClients.size();
    }

    public boolean isSimulationTimeOver() {
        return simulationTime >= totalSimulationTime;
    }

    public void incrementSimulationTime() {
        simulationTime++;
    }

    public void increaseWaitingTimeForAllPresentClients() {
        for (Client client : allClients) {
            if (client.getArrivalTime() < simulationTime && !processedClients.contains(client)) {
                client.increaseWaitingTime();
            }
        }
    }

    public void calculatePeakHour() {
        int clientsInHour = 0;
        for (ServiceQueue queue : queues) {
            clientsInHour += queue.getCurrentQueueSize();
        }
        if (clientsInHour > maxClientsInHour) {
            maxClientsInHour = clientsInHour;
            peakHour = simulationTime - 1;
        }
    }

    private void timerGuard() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer.purge();
        }
    }
}
