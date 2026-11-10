package com.cabinetmedical.view;

import com.cabinetmedical.config.DatabaseConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.ResultSet;

public class StatistiquesView {
    
    public static void show(Stage primaryStage) {
        // Créer une nouvelle fenêtre
        Stage stage = new Stage();
        stage.setTitle("Statistiques du Cabinet");
        
        // Layout principal
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f4f6f9;");
        
        // Titre
        Label title = new Label("📊 Statistiques du Cabinet");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #2c3e50;");
        
        // Grille pour les cartes
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        
        try {
            // 1. Nombre de patients
            ResultSet rs1 = DatabaseConnection.executeQuery("SELECT COUNT(*) FROM Patients");
            if (rs1 != null && rs1.next()) {
                addStatCard(grid, 0, 0, "👥 Patients", rs1.getString(1), "#3498db");
                rs1.getStatement().close();
            }
            
            // 2. Nombre de médecins
            ResultSet rs2 = DatabaseConnection.executeQuery("SELECT COUNT(*) FROM Medecins");
            if (rs2 != null && rs2.next()) {
                addStatCard(grid, 1, 0, "👨‍⚕️ Médecins", rs2.getString(1), "#2ecc71");
                rs2.getStatement().close();
            }
            
            // 3. Rendez-vous du jour
            ResultSet rs3 = DatabaseConnection.executeQuery(
                "SELECT COUNT(*) FROM RendezVous WHERE DATE(date_rdv) = CURDATE()"
            );
            if (rs3 != null && rs3.next()) {
                addStatCard(grid, 0, 1, "📅 RDV Aujourd'hui", rs3.getString(1), "#e74c3c");
                rs3.getStatement().close();
            }
            
            // 4. Revenus totaux
            ResultSet rs4 = DatabaseConnection.executeQuery(
                "SELECT COALESCE(SUM(cout), 0) FROM Traitements"
            );
            if (rs4 != null && rs4.next()) {
                addStatCard(grid, 1, 1, "💰 Revenus (DH)", rs4.getString(1), "#9b59b6");
                rs4.getStatement().close();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Label errorLabel = new Label("Erreur lors du chargement des statistiques.");
            errorLabel.setStyle("-fx-text-fill: red;");
            grid.add(errorLabel, 0, 0, 2, 1);
        }
        
        // Bouton Fermer
        Button closeBtn = new Button("Fermer");
        closeBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        closeBtn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> stage.close());
        
        // Ajouter tout au layout
        root.getChildren().addAll(title, grid, closeBtn);
        
        // Afficher la fenêtre
        Scene scene = new Scene(root, 550, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    // Méthode helper pour créer une carte statistique
    private static void addStatCard(GridPane grid, int col, int row, String label, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setPrefHeight(120);
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        
        Label lbl = new Label(label);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lbl.setStyle("-fx-text-fill: white;");
        
        Label val = new Label(value);
        // ✅ HADI HIYA L'LIGNE L'MSS77A7A DYAL FONT
        val.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        val.setStyle("-fx-text-fill: white;");
        
        card.getChildren().addAll(lbl, val);
        grid.add(card, col, row);
    }
}