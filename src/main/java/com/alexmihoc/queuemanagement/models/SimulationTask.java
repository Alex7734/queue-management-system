package com.alexmihoc.queuemanagement.models;

import com.alexmihoc.queuemanagement.utils.SimulationLogger;

import java.util.TimerTask;

public class SimulationTask extends TimerTask {
    private static SimulationTask instance;
    private final QueueManager queueManager;

    public SimulationTask(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    @Override
    public void run() {
        queueManager.clientAssignment();
        queueManager.printSimulationStatus();

        if (queueManager.allClientsProcessed()) {
            SimulationLogger.log("Simulation ended. All clients processed.");
            this.cancel();
            queueManager.stopSimulation();
            queueManager.statistics();
        }

        if (queueManager.isSimulationTimeOver()) {
            SimulationLogger.log("Simulation ended. Total simulation time reached.");
            this.cancel();
            queueManager.stopSimulation();
            queueManager.statistics();
        }

        if (!queueManager.allClientsProcessed() && !queueManager.isSimulationTimeOver()) {
            queueManager.printQueueState();
            queueManager.incrementSimulationTime();
            queueManager.increaseWaitingTimeForAllPresentClients();
            queueManager.calculatePeakHour();
        }
    }
}

