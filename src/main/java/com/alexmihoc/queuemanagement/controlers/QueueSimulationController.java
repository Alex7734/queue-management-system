package com.alexmihoc.queuemanagement.controlers;

import com.alexmihoc.queuemanagement.config.ClientGenerationConfig;
import com.alexmihoc.queuemanagement.config.SimulationConfig;
import com.alexmihoc.queuemanagement.models.QueueManager;
import com.alexmihoc.queuemanagement.models.strategy.ShortestQueueStrategy;
import com.alexmihoc.queuemanagement.models.strategy.ShortestTimeStrategy;
import com.alexmihoc.queuemanagement.utils.LogHandler;
import com.alexmihoc.queuemanagement.utils.SimulationLogger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.scene.control.ComboBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class QueueSimulationController {
    @FXML
    private TextField numQueuesField;
    @FXML
    private TextField totalClientsField;
    @FXML
    private TextField simulationTimeField;
    @FXML
    private TextField minArrivalTimeField;
    @FXML
    private TextField maxArrivalTimeField;
    @FXML
    private TextField minServiceTimeField;
    @FXML
    private TextField maxServiceTimeField;
    @FXML
    private ComboBox<String> strategyComboBox;

    @FXML
    private TextArea logArea;
    QueueManager manager;

    public void initialize() {
        LogHandler guiLogHandler = message -> Platform.runLater(() -> logArea.appendText(message + "\n"));
        SimulationLogger.addLogHandler(guiLogHandler);
        SimulationLogger.init();

        strategyComboBox.getItems().addAll("Shortest Queue", "Shortest Time");
        strategyComboBox.setValue("Shortest Queue");

        SimulationConfig.setStrategy(new ShortestQueueStrategy());

        strategyComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            switch (newValue) {
                case "Shortest Queue":
                    SimulationConfig.setStrategy(new ShortestQueueStrategy());
                    logArea.appendText("Strategy set to Shortest Queue\n");
                    break;
                case "Shortest Time":
                    SimulationConfig.setStrategy(new ShortestTimeStrategy());
                    logArea.appendText("Strategy set to Shortest Time\n");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + newValue);
            }
        });
    }

    @FXML
    protected void onStartSimulationClick() {
        int numQueues = Integer.parseInt(numQueuesField.getText());
        int totalClients = Integer.parseInt(totalClientsField.getText());
        int simulationTime = Integer.parseInt(simulationTimeField.getText());
        int minArrivalTime = Integer.parseInt(minArrivalTimeField.getText());
        int maxArrivalTime = Integer.parseInt(maxArrivalTimeField.getText());
        int minServiceTime = Integer.parseInt(minServiceTimeField.getText());
        int maxServiceTime = Integer.parseInt(maxServiceTimeField.getText());

        ClientGenerationConfig.setMinArrivalTime(minArrivalTime);
        ClientGenerationConfig.setMaxArrivalTime(maxArrivalTime);
        ClientGenerationConfig.setMinServiceTime(minServiceTime);
        ClientGenerationConfig.setMaxServiceTime(maxServiceTime);

        this.manager = new QueueManager(numQueues, totalClients);
        manager.startSimulation(simulationTime, SimulationConfig.CLIENT_GENERATION_INTERVAL_MS);
        logArea.appendText("Simulation started...\n");
    }

    @FXML
    protected void onResetSimulationClick() {
        if (this.manager == null) {
            logArea.appendText("No simulation to reset...\n");
            return;
        }

        this.manager.stopSimulation();
        this.manager = new QueueManager(Integer.parseInt(numQueuesField.getText()), Integer.parseInt(totalClientsField.getText()));
        logArea.clear();
        logArea.appendText("Simulation reset...\n");
    }

    @FXML
    protected void onShowLogFileClick() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setInitialFileName("simulation_log.txt");

        File file = fileChooser.showSaveDialog(logArea.getScene().getWindow());

        if (file != null) {
            try {
                Path logFilePath = Paths.get("simulation_log.txt");
                Files.copy(logFilePath, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Log file saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to save the log file: " + e.getMessage());
            }
        }
    }
}
