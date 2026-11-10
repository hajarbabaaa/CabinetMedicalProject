package com.cabinetmedical.view;

import com.cabinetmedical.service.MedecinService;
import com.cabinetmedical.model.Medecin;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class MedecinView extends Application {

    private MedecinService medecinService;
    private TableView<Medecin> tableMedecins;
    private ObservableList<Medecin> medecinData;
    private TextField tfNom, tfSpecialite, tfTelephone, tfRecherche;

    @Override
    public void start(Stage primaryStage) {
        medecinService = new MedecinService();
        primaryStage.setTitle("👨‍⚕️ Gestion des Médecins");

        // Layout principal
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Panneau supérieur
        VBox topPanel = new VBox(10);

        Label titleLabel = new Label("GESTION DES MÉDECINS");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox searchBox = new HBox(10);
        Label searchLabel = new Label("Recherche par spécialité:");
        tfRecherche = new TextField();
        tfRecherche.setPromptText("Spécialité...");
        Button btnRechercher = new Button("🔍 Rechercher");
        btnRechercher.setOnAction(e -> rechercherMedecins());
        Button btnTous = new Button("📋 Tous les médecins");
        btnTous.setOnAction(e -> chargerTousLesMedecins());

        searchBox.getChildren().addAll(searchLabel, tfRecherche, btnRechercher, btnTous);
        topPanel.getChildren().addAll(titleLabel, searchBox);

        // Tableau des médecins
        tableMedecins = new TableView<>();
        medecinData = FXCollections.observableArrayList();
        tableMedecins.setItems(medecinData);

        // Colonnes du tableau
        TableColumn<Medecin, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idMedecin"));

        TableColumn<Medecin, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Medecin, String> colSpecialite = new TableColumn<>("Spécialité");
        colSpecialite.setCellValueFactory(new PropertyValueFactory<>("specialite"));

        TableColumn<Medecin, String> colTelephone = new TableColumn<>("Téléphone");
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        tableMedecins.getColumns().addAll(colId, colNom, colSpecialite, colTelephone);

        // Formulaire
        VBox formPanel = new VBox(10);
        formPanel.setPadding(new Insets(10));
        formPanel.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 5;");

        Label formLabel = new Label("Formulaire Médecin");
        formLabel.setStyle("-fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        formGrid.add(new Label("Nom:"), 0, 0);
        tfNom = new TextField();
        formGrid.add(tfNom, 1, 0);

        formGrid.add(new Label("Spécialité:"), 0, 1);
        tfSpecialite = new TextField();
        formGrid.add(tfSpecialite, 1, 1);

        formGrid.add(new Label("Téléphone:"), 0, 2);
        tfTelephone = new TextField();
        formGrid.add(tfTelephone, 1, 2);

        HBox buttonBox = new HBox(10);
        Button btnAjouter = new Button("➕ Ajouter");
        btnAjouter.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnAjouter.setOnAction(e -> ajouterMedecin());

        Button btnModifier = new Button("✏️ Modifier");
        btnModifier.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        btnModifier.setOnAction(e -> modifierMedecin());

        Button btnSupprimer = new Button("🗑️ Supprimer");
        btnSupprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnSupprimer.setOnAction(e -> supprimerMedecin());

        Button btnVider = new Button("🧹 Vider");
        btnVider.setOnAction(e -> viderFormulaire());

        buttonBox.getChildren().addAll(btnAjouter, btnModifier, btnSupprimer, btnVider);

        formPanel.getChildren().addAll(formLabel, formGrid, buttonBox);

        // Gestionnaire de sélection
        tableMedecins.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    afficherMedecinDansFormulaire(newValue);
                }
            });

        // Assemblage
        mainLayout.setTop(topPanel);
        mainLayout.setCenter(tableMedecins);
        mainLayout.setBottom(formPanel);

        // Charger les données
        chargerTousLesMedecins();

        // Scène
        Scene scene = new Scene(mainLayout, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void chargerTousLesMedecins() {
        List<Medecin> medecins = medecinService.getAllMedecins();
        medecinData.clear();
        medecinData.addAll(medecins);
    }

    private void rechercherMedecins() {
        String specialite = tfRecherche.getText();
        if (!specialite.isEmpty()) {
            List<Medecin> medecins = medecinService.rechercherParSpecialite(specialite);
            medecinData.clear();
            medecinData.addAll(medecins);
        } else {
            chargerTousLesMedecins();
        }
    }

    private void ajouterMedecin() {
        String nom = tfNom.getText();
        String specialite = tfSpecialite.getText();
        String telephone = tfTelephone.getText();

        if (nom.isEmpty() || specialite.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir les champs obligatoires (Nom et Spécialité)");
            return;
        }

        boolean success = medecinService.ajouterMedecin(nom, specialite, telephone);

        if (success) {
            showAlert("Succès", "Médecin ajouté avec succès!");
            chargerTousLesMedecins();
            viderFormulaire();
        } else {
            showAlert("Erreur", "Échec de l'ajout du médecin.");
        }
    }

    private void modifierMedecin() {
        Medecin medecinSelectionne = tableMedecins.getSelectionModel().getSelectedItem();

        if (medecinSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un médecin à modifier.");
            return;
        }

        String nom = tfNom.getText();
        String specialite = tfSpecialite.getText();
        String telephone = tfTelephone.getText();

        boolean success = medecinService.modifierMedecin(
            medecinSelectionne.getIdMedecin(), nom, specialite, telephone);

        if (success) {
            showAlert("Succès", "Médecin modifié avec succès!");
            chargerTousLesMedecins();
            viderFormulaire();
        } else {
            showAlert("Erreur", "Échec de la modification.");
        }
    }

    private void supprimerMedecin() {
        Medecin medecinSelectionne = tableMedecins.getSelectionModel().getSelectedItem();

        if (medecinSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un médecin à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Suppression du médecin");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer le Dr. " +
            medecinSelectionne.getNom() + " (" + medecinSelectionne.getSpecialite() + ") ?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            boolean success = medecinService.supprimerMedecin(medecinSelectionne.getIdMedecin());

            if (success) {
                showAlert("Succès", "Médecin supprimé avec succès!");
                chargerTousLesMedecins();
                viderFormulaire();
            } else {
                showAlert("Erreur", "Échec de la suppression.");
            }
        }
    }

    private void afficherMedecinDansFormulaire(Medecin medecin) {
        tfNom.setText(medecin.getNom());
        tfSpecialite.setText(medecin.getSpecialite());
        tfTelephone.setText(medecin.getTelephone());
    }

    private void viderFormulaire() {
        tfNom.clear();
        tfSpecialite.clear();
        tfTelephone.clear();
        tableMedecins.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}