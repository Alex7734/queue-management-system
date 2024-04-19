module com.alexmihoc.queuemanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.alexmihoc.queuemanagement to javafx.fxml;
    exports com.alexmihoc.queuemanagement;
    exports com.alexmihoc.queuemanagement.controlers;
    opens com.alexmihoc.queuemanagement.controlers to javafx.fxml;
}