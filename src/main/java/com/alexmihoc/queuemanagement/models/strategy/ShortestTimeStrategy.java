package com.alexmihoc.queuemanagement.models.strategy;

import com.alexmihoc.queuemanagement.models.Client;
import com.alexmihoc.queuemanagement.models.ServiceQueue;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ShortestTimeStrategy implements QueueAssignmentStrategy {

    @Override
    public int assignClientToQueue(List<ServiceQueue> queues, Client client) {
        Optional<ServiceQueue> optMinQueue = queues.stream()
                .min(Comparator.comparingInt(ServiceQueue::getWaitingTime));

        if (optMinQueue.isPresent()) {
            ServiceQueue minQueue = optMinQueue.get();
            minQueue.addClient(client);
            return queues.indexOf(minQueue) + 1;
        } else {
            return -1;
        }
    }
}

