package com.cabinetmedical.view;

import com.cabinetmedical.model.Patient;
import com.cabinetmedical.service.PatientService;
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

public class PatientView extends Application {

    private PatientService patientService;
    private TableView<Patient> tablePatients;
    private ObservableList<Patient> patientData;
    private TextField tfNom, tfPrenom, tfDateNaissance, tfAdresse, tfTelephone, tfRecherche;

    @Override
    public void start(Stage primaryStage) {
        patientService = new PatientService();
        primaryStage.setTitle("👥 Gestion des Patients");

        // Layout principal
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Panneau supérieur - Titre et recherche
        VBox topPanel = new VBox(10);

        Label titleLabel = new Label("GESTION DES PATIENTS");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox searchBox = new HBox(10);
        Label searchLabel = new Label("Rechercher:");
        tfRecherche = new TextField();
        tfRecherche.setPromptText("Nom, prénom ou téléphone...");
        Button btnRechercher = new Button("🔍 Rechercher");
        btnRechercher.setOnAction(e -> rechercherPatients());
        Button btnTous = new Button("📋 Afficher tous");
        btnTous.setOnAction(e -> chargerTousLesPatients());

        searchBox.getChildren().addAll(searchLabel, tfRecherche, btnRechercher, btnTous);
        topPanel.getChildren().addAll(titleLabel, searchBox);

        // Tableau des patients
        tablePatients = new TableView<>();
        patientData = FXCollections.observableArrayList();
        tablePatients.setItems(patientData);

        // Colonnes du tableau
        TableColumn<Patient, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idPatient"));

        TableColumn<Patient, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Patient, String> colPrenom = new TableColumn<>("Prénom");
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        TableColumn<Patient, String> colDateNaiss = new TableColumn<>("Date Naissance");
        colDateNaiss.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));

        TableColumn<Patient, String> colTelephone = new TableColumn<>("Téléphone");
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        TableColumn<Patient, String> colAdresse = new TableColumn<>("Adresse");
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        tablePatients.getColumns().addAll(colId, colNom, colPrenom, colDateNaiss, colTelephone, colAdresse);

        // Formulaire d'ajout/modification
        VBox formPanel = new VBox(10);
        formPanel.setPadding(new Insets(10));
        formPanel.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 5;");

        Label formLabel = new Label("Formulaire Patient");
        formLabel.setStyle("-fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        // Champs du formulaire
        formGrid.add(new Label("Nom:"), 0, 0);
        tfNom = new TextField();
        formGrid.add(tfNom, 1, 0);

        formGrid.add(new Label("Prénom:"), 0, 1);
        tfPrenom = new TextField();
        formGrid.add(tfPrenom, 1, 1);

        formGrid.add(new Label("Date Naissance (AAAA-MM-JJ):"), 0, 2);
        tfDateNaissance = new TextField();
        tfDateNaissance.setPromptText("2000-01-31");
        formGrid.add(tfDateNaissance, 1, 2);

        formGrid.add(new Label("Téléphone:"), 0, 3);
        tfTelephone = new TextField();
        formGrid.add(tfTelephone, 1, 3);

        formGrid.add(new Label("Adresse:"), 0, 4);
        tfAdresse = new TextField();
        formGrid.add(tfAdresse, 1, 4);

        // Boutons d'action
        HBox buttonBox = new HBox(10);
        Button btnAjouter = new Button("➕ Ajouter");
        btnAjouter.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnAjouter.setOnAction(e -> ajouterPatient());

        Button btnModifier = new Button("✏️ Modifier");
        btnModifier.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        btnModifier.setOnAction(e -> modifierPatient());

        Button btnSupprimer = new Button("🗑️ Supprimer");
        btnSupprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnSupprimer.setOnAction(e -> supprimerPatient());

        Button btnVider = new Button("🧹 Vider");
        btnVider.setOnAction(e -> viderFormulaire());

        buttonBox.getChildren().addAll(btnAjouter, btnModifier, btnSupprimer, btnVider);

        formPanel.getChildren().addAll(formLabel, formGrid, buttonBox);

        // Gestionnaire de sélection dans le tableau
        tablePatients.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    afficherPatientDansFormulaire(newValue);
                }
            });

        // Assemblage du layout
        mainLayout.setTop(topPanel);
        mainLayout.setCenter(tablePatients);
        mainLayout.setBottom(formPanel);

        // Charger les données initiales
        chargerTousLesPatients();

        // Configurer la scène
        Scene scene = new Scene(mainLayout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void chargerTousLesPatients() {
        List<Patient> patients = patientService.getAllPatients();
        patientData.clear();
        patientData.addAll(patients);
    }

    private void rechercherPatients() {
        String critere = tfRecherche.getText();
        if (!critere.isEmpty()) {
            List<Patient> patients = patientService.rechercherPatients(critere);
            patientData.clear();
            patientData.addAll(patients);
        } else {
            chargerTousLesPatients();
        }
    }

    private void ajouterPatient() {
        try {
            String nom = tfNom.getText();
            String prenom = tfPrenom.getText();
            String dateNaissance = tfDateNaissance.getText();
            String adresse = tfAdresse.getText();
            String telephone = tfTelephone.getText();

            if (nom.isEmpty() || prenom.isEmpty() || dateNaissance.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir les champs obligatoires (Nom, Prénom, Date de naissance)");
                return;
            }

            boolean success = patientService.ajouterPatient(nom, prenom, dateNaissance, adresse, telephone);

            if (success) {
                showAlert("Succès", "Patient ajouté avec succès!");
                chargerTousLesPatients();
                viderFormulaire();
            } else {
                showAlert("Erreur", "Échec de l'ajout du patient.");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Format de date invalide. Utilisez AAAA-MM-JJ");
        }
    }

    private void modifierPatient() {
        Patient patientSelectionne = tablePatients.getSelectionModel().getSelectedItem();

        if (patientSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un patient à modifier.");
            return;
        }

        try {
            String nom = tfNom.getText();
            String prenom = tfPrenom.getText();
            String dateNaissance = tfDateNaissance.getText();
            String adresse = tfAdresse.getText();
            String telephone = tfTelephone.getText();

            boolean success = patientService.modifierPatient(
                patientSelectionne.getIdPatient(), nom, prenom, dateNaissance, adresse, telephone);

            if (success) {
                showAlert("Succès", "Patient modifié avec succès!");
                chargerTousLesPatients();
                viderFormulaire();
            } else {
                showAlert("Erreur", "Échec de la modification.");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Format de date invalide.");
        }
    }

    private void supprimerPatient() {
        Patient patientSelectionne = tablePatients.getSelectionModel().getSelectedItem();

        if (patientSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un patient à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Suppression du patient");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer " +
            patientSelectionne.getPrenom() + " " + patientSelectionne.getNom() + " ?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            boolean success = patientService.supprimerPatient(patientSelectionne.getIdPatient());

            if (success) {
                showAlert("Succès", "Patient supprimé avec succès!");
                chargerTousLesPatients();
                viderFormulaire();
            } else {
                showAlert("Erreur", "Échec de la suppression.");
            }
        }
    }

    private void afficherPatientDansFormulaire(Patient patient) {
        tfNom.setText(patient.getNom());
        tfPrenom.setText(patient.getPrenom());
        tfDateNaissance.setText(patient.getDateNaissance().toString());
        tfAdresse.setText(patient.getAdresse());
        tfTelephone.setText(patient.getTelephone());
    }

    private void viderFormulaire() {
        tfNom.clear();
        tfPrenom.clear();
        tfDateNaissance.clear();
        tfAdresse.clear();
        tfTelephone.clear();
        tablePatients.getSelectionModel().clearSelection();
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