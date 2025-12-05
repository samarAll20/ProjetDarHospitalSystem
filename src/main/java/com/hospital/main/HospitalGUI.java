package com.hospital.main;

import com.hospital.database.DatabaseManager;
import com.hospital.jms.AlertProducer;
import com.hospital.rmi.RMIClient;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HospitalGUI extends Application {

    private TextArea logArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🏥 Système Hospitalier Intelligent");
        DatabaseManager.testConnection();
        // Création des onglets
        TabPane tabPane = new TabPane();

        // Onglet 1 : Urgences
        Tab emergencyTab = new Tab("🚑 Urgences");
        emergencyTab.setContent(createEmergencyTab());
        emergencyTab.setClosable(false);

        // Onglet 2 : Alertes
        Tab alertTab = new Tab("📢 Alertes");
        alertTab.setContent(createAlertTab());
        alertTab.setClosable(false);

        // Onglet 3 : Statistiques
        Tab statsTab = new Tab("📊 Statistiques");
        statsTab.setContent(createStatsTab());
        statsTab.setClosable(false);

        tabPane.getTabs().addAll(emergencyTab, alertTab, statsTab);

        // Zone de log
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(150);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(tabPane, new Label("Journal:"), logArea);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createEmergencyTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Formulaire
        grid.add(new Label("Nom:"), 0, 0);
        TextField nameField = new TextField();
        grid.add(nameField, 1, 0);

        grid.add(new Label("Âge:"), 0, 1);
        TextField ageField = new TextField();
        grid.add(ageField, 1, 1);

        grid.add(new Label("Type d'urgence:"), 0, 2);
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Cardiology", "Trauma", "Pediatrics", "General");
        grid.add(typeCombo, 1, 2);

        grid.add(new Label("Niveau de gravité:"), 0, 3);
        Slider severitySlider = new Slider(1, 3, 2);
        severitySlider.setShowTickLabels(true);
        severitySlider.setShowTickMarks(true);
        severitySlider.setMajorTickUnit(1);
        severitySlider.setMinorTickCount(0);
        severitySlider.setSnapToTicks(true);
        grid.add(severitySlider, 1, 3);

        Button registerBtn = new Button("📋 Enregistrer l'urgence");
        registerBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
        registerBtn.setOnAction(e -> {
            log("🚨 Nouvelle urgence: " + nameField.getText());
            // Ici appeler votre service RMI
        });

        vbox.getChildren().addAll(grid, registerBtn);
        return vbox;
    }

    private VBox createAlertTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField alertField = new TextField();
        alertField.setPromptText("Entrez votre message d'alerte...");

        Button sendBtn = new Button("📤 Envoyer l'alerte");
        sendBtn.setStyle("-fx-background-color: #44aa44; -fx-text-fill: white;");
        sendBtn.setOnAction(e -> {
            AlertProducer.sendAlert(alertField.getText());
            log("📤 Alerte envoyée: " + alertField.getText());
            alertField.clear();
        });

        vbox.getChildren().addAll(
                new Label("Message d'alerte:"),
                alertField,
                sendBtn
        );

        return vbox;
    }

    private VBox createStatsTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        // Simuler des statistiques
        Label statsLabel = new Label("📈 Statistiques en temps réel");
        statsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ProgressBar bedOccupancy = new ProgressBar(0.7);
        bedOccupancy.setPrefWidth(300);

        vbox.getChildren().addAll(
                statsLabel,
                new Label("Taux d'occupation des lits:"),
                bedOccupancy,
                new Label("70% des lits occupés"),
                new Separator(),
                new Label("🚑 Urgences aujourd'hui: 12"),
                new Label("🏥 Hôpitaux connectés: 2"),
                new Label("📨 Alertes envoyées: 24")
        );

        return vbox;
    }

    private void log(String message) {
        logArea.appendText(new java.util.Date() + " - " + message + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }}