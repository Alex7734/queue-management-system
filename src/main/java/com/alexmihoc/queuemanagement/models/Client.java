package com.alexmihoc.queuemanagement.models;

import com.alexmihoc.queuemanagement.config.ClientGenerationConfig;

import java.util.Random;

public class Client {
    private static int nextId = 1;
    private final int id;
    private final int arrivalTime;
    private int serviceTime;
    private int waitingTime;

    public Client(int arrivalTime, int serviceTime) {
        this.id = nextId++;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.waitingTime = 0;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public static Client generateRandomClient() {
        Random rand = new Random();
        int arrivalTime = rand.nextInt(ClientGenerationConfig.getMaxArrivalTime() - ClientGenerationConfig.getMinArrivalTime() + 1) + ClientGenerationConfig.getMinArrivalTime();
        int serviceTime = rand.nextInt(ClientGenerationConfig.getMaxServiceTime() - ClientGenerationConfig.getMinServiceTime() + 1) + ClientGenerationConfig.getMinServiceTime();
        return new Client(arrivalTime, serviceTime);
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void increaseWaitingTime() {
        waitingTime++;
    }

    @Override
    public String toString() {
        return String.format("((Client %d) A.T: %d, S.T: %d)", id, arrivalTime, serviceTime);
    }

    public void decreaseServiceTime() throws IllegalStateException {
        if (this.serviceTime > 0) {
            this.serviceTime -= 1;
        } else {
            throw new IllegalStateException("Service time cannot be negative");
        }
    }
}
