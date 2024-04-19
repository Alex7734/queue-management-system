package com.alexmihoc.queuemanagement.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SimulationLogger {
    private static final List<LogHandler> handlers = new ArrayList<>();
    private static PrintWriter fileOut = null;
    private static final String FILENAME = "simulation_log.txt";

    public static void init() {
        try {
            fileOut = new PrintWriter(new BufferedWriter(new FileWriter(FILENAME, false)));
            addLogHandler(SimulationLogger::logToFile);
        } catch (IOException e) {
            System.err.println("Error initializing log: " + e.getMessage());
        }
    }

    public static void addLogHandler(LogHandler handler) {
        if (!handlers.contains(handler)) {
            handlers.add(handler);
        }
    }

    public static void log(String message) {
        for (LogHandler handler : handlers) {
            handler.log(message);
        }
    }

    private static void logToFile(String message) {
        if (fileOut != null) {
            fileOut.println(message);
            fileOut.flush();
        }
    }

    public static void close() {
        if (fileOut != null) {
            fileOut.close();
        }
    }
}
