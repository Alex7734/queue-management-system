package com.alexmihoc.queuemanagement.config;

public class ClientGenerationConfig {
    private static int minArrivalTime;
    private static int maxArrivalTime;
    private static int minServiceTime;
    private static int maxServiceTime;

    public static void setMinArrivalTime(int min) {
        minArrivalTime = min;
    }

    public static void setMaxArrivalTime(int max) {
        maxArrivalTime = max;
    }

    public static void setMinServiceTime(int min) {
        minServiceTime = min;
    }

    public static void setMaxServiceTime(int max) {
        maxServiceTime = max;
    }

    public static int getMinArrivalTime() {
        return minArrivalTime;
    }

    public static int getMaxArrivalTime() {
        return maxArrivalTime;
    }

    public static int getMinServiceTime() {
        return minServiceTime;
    }

    public static int getMaxServiceTime() {
        return maxServiceTime;
    }
}
