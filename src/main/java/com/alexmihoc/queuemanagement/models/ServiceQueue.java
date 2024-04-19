package com.alexmihoc.queuemanagement.models;

import com.alexmihoc.queuemanagement.config.SimulationConfig;

import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServiceQueue extends Thread {
    private final ConcurrentLinkedQueue<Client> queue;
    private int waitingTime;
    private boolean running;
    private final Number queueNumber;
    private final HashSet<Client> processedClients;

    public ServiceQueue(Number queueNumber, HashSet<Client> processedClients) {
        this.queue = new ConcurrentLinkedQueue<>();
        this.waitingTime = 0;
        this.running = true;
        this.queueNumber = queueNumber;
        this.processedClients = processedClients;
    }

    public void run() {
        while (running) {
            try {
                processClients();
                Thread.sleep(SimulationConfig.CLIENT_GENERATION_INTERVAL_MS);
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }

    private synchronized void processClients() {
        if (!queue.isEmpty()) {
            Client client = queue.peek();
            if (client != null && client.getServiceTime() > 0) {
                client.decreaseServiceTime();
                waitingTime -= 1;
            }

            if (client != null && client.getServiceTime() == 0) {
                queue.poll();
                processedClients.add(client);
            }
        }
    }
    public String getQueueState() {
        StringBuilder sb = new StringBuilder();
        for (Client client : queue) {
            sb.append(client.toString()).append("; ");
        }
        return sb.toString();
    }

    public synchronized void addClient(Client client) {
        this.queue.offer(client);
        this.waitingTime += client.getServiceTime();
    }

    public synchronized int getWaitingTime() {
        return this.waitingTime;
    }

    public Number getQueueNumber() {
        return this.queueNumber;
    }

    public int getCurrentQueueSize() {
        return queue.size();
    }
}
