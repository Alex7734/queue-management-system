package com.alexmihoc.queuemanagement.models.strategy;


import com.alexmihoc.queuemanagement.models.Client;
import com.alexmihoc.queuemanagement.models.ServiceQueue;
import java.util.List;

public interface QueueAssignmentStrategy {
    int assignClientToQueue(List<ServiceQueue> queues, Client client);
}
