package com.cabinetmedical.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🏥 Gestion Cabinet Médical");
        
        // Layout principal
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");
        
        // Titre
        Label titleLabel = new Label("SYSTÈME DE GESTION MÉDICALE");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Sous-titre
        Label subtitleLabel = new Label("Gestion intégrée des patients, médecins, rendez-vous et traitements");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        
        // Boutons de navigation
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(15);
        buttonGrid.setVgap(15);
        buttonGrid.setPadding(new Insets(20, 0, 20, 0));
        
        // Bouton Patients
        Button btnPatients = createMenuButton("👥 PATIENTS", "#3498db");
        btnPatients.setOnAction(e -> {
            try {
                new PatientView().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Impossible d'ouvrir la vue Patients");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });
        
        // Bouton Médecins
        Button btnMedecins = createMenuButton("👨‍⚕️ MÉDECINS", "#2ecc71");
        btnMedecins.setOnAction(e -> {
            try {
                new MedecinView().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Impossible d'ouvrir la vue Médecins");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });
        
        // Bouton Rendez-vous
        Button btnRendezVous = createMenuButton("📅 RENDEZ-VOUS", "#e74c3c");
        btnRendezVous.setOnAction(e -> {
            try {
                new RendezVousView().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Impossible d'ouvrir la vue Rendez-vous");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });
        
        // Bouton Traitements
        Button btnTraitements = createMenuButton("💊 TRAITEMENTS", "#9b59b6");
        btnTraitements.setOnAction(e -> {
            try {
                new TraitementView().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Impossible d'ouvrir la vue Traitements");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });
        
        // Bouton Statistiques
        Button btnStats = createMenuButton("📊 STATISTIQUES", "#f39c12");
        btnStats.setOnAction(e -> showStatistics());
        
        // Bouton Quitter
        Button btnQuitter = createMenuButton("🚪 QUITTER", "#95a5a6");
        btnQuitter.setOnAction(e -> primaryStage.close());
        
        // Ajouter les boutons au grid
        buttonGrid.add(btnPatients, 0, 0);
        buttonGrid.add(btnMedecins, 1, 0);
        buttonGrid.add(btnRendezVous, 0, 1);
        buttonGrid.add(btnTraitements, 1, 1);
        buttonGrid.add(btnStats, 0, 2);
        buttonGrid.add(btnQuitter, 1, 2);
        
        // Barre de statut
        HBox statusBar = new HBox(10);
        statusBar.setPadding(new Insets(10));
        statusBar.setStyle("-fx-background-color: #34495e; -fx-background-radius: 5;");
        
        Label statusLabel = new Label("Système prêt - Cabinet Médical v1.0");
        statusLabel.setStyle("-fx-text-fill: white;");
        statusBar.getChildren().add(statusLabel);
        
        // Ajouter tous les composants au layout principal
        mainLayout.getChildren().addAll(titleLabel, subtitleLabel, buttonGrid, statusBar);
        
        // Configurer la scène
        Scene scene = new Scene(mainLayout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Crée un bouton de menu stylisé
     * @param text Le texte du bouton
     * @param color La couleur de fond
     * @return Le bouton configuré
     */
    private Button createMenuButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefSize(250, 80);
        String baseStyle = "-fx-font-size: 16px; -fx-font-weight: bold; "
                         + "-fx-background-color: " + color + "; "
                         + "-fx-text-fill: white; -fx-background-radius: 10; "
                         + "-fx-padding: 15;";
        button.setStyle(baseStyle);
        
        // Effet hover
        button.setOnMouseEntered(e -> {
            button.setStyle(baseStyle + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 0);");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(baseStyle);
        });
        
        return button;
    }
    
    private void showStatistics() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📊 Statistiques du Cabinet");
        alert.setHeaderText("Aperçu des données du système");
        
        // Ici, vous ajouterez les vraies statistiques
        String content = "Fonctionnalité à implémenter:\n"
                       + "• Nombre de patients\n"
                       + "• Nombre de médecins\n"
                       + "• Rendez-vous du jour\n"
                       + "• Revenus totaux\n"
                       + "\nCette fonctionnalité sera disponible dans la prochaine version.";
        
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}