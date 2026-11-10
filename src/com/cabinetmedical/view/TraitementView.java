package com.cabinetmedical.view;

import com.cabinetmedical.service.TraitementService;
import com.cabinetmedical.service.PatientService;
import com.cabinetmedical.service.MedecinService;
import com.cabinetmedical.model.Traitement;
import com.cabinetmedical.model.Patient;
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
import java.math.BigDecimal;
import java.util.List;

public class TraitementView extends Application {

    private TraitementService traitementService;
    private PatientService patientService;
    private MedecinService medecinService;
    private TableView<Traitement> tableTraitements;
    private ObservableList<Traitement> traitementData;
    private ObservableList<Patient> patientsList;
    private ObservableList<Medecin> medecinsList;

    private TextArea taDescription;
    private TextField tfCout, tfRecherche;
    private ComboBox<Patient> cbPatient;
    private ComboBox<Medecin> cbMedecin;

    @Override
    public void start(Stage primaryStage) {
        traitementService = new TraitementService();
        patientService = new PatientService();
        medecinService = new MedecinService();

        primaryStage.setTitle("💊 Gestion des Traitements");

        // Layout principal
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Panneau supérieur
        VBox topPanel = new VBox(10);

        Label titleLabel = new Label("GESTION DES TRAITEMENTS");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox searchBox = new HBox(10);
        Label searchLabel = new Label("Filtrer par patient:");
        tfRecherche = new TextField();
        tfRecherche.setPromptText("ID patient...");
        Button btnRechercher = new Button("🔍 Filtrer");
        btnRechercher.setOnAction(e -> filtrerParPatient());
        Button btnTous = new Button("📋 Tous les traitements");
        btnTous.setOnAction(e -> chargerTousLesTraitements());
        Button btnStats = new Button("📊 Statistiques");
        btnStats.setOnAction(e -> afficherStatistiques());

        searchBox.getChildren().addAll(searchLabel, tfRecherche, btnRechercher, btnTous, btnStats);
        topPanel.getChildren().addAll(titleLabel, searchBox);

        // Tableau des traitements
        tableTraitements = new TableView<>();
        traitementData = FXCollections.observableArrayList();
        tableTraitements.setItems(traitementData);

        // Colonnes du tableau
        TableColumn<Traitement, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idTraitement"));

        TableColumn<Traitement, String> colDescription = new TableColumn<>("Description");
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.setPrefWidth(200);

        // CORRECTION : Utiliser PropertyValueFactory avec le bon type
        TableColumn<Traitement, BigDecimal> colCout = new TableColumn<>("Coût (€)");
        colCout.setCellValueFactory(new PropertyValueFactory<>("cout"));

        // CORRECTION : Utiliser PropertyValueFactory pour les autres colonnes
        TableColumn<Traitement, String> colPatient = new TableColumn<>("Patient");
        colPatient.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));

        TableColumn<Traitement, String> colMedecin = new TableColumn<>("Médecin");
        colMedecin.setCellValueFactory(new PropertyValueFactory<>("nomMedecin"));

        tableTraitements.getColumns().addAll(colId, colDescription, colCout, colPatient, colMedecin);

        // Formulaire
        VBox formPanel = new VBox(10);
        formPanel.setPadding(new Insets(10));
        formPanel.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 5;");

        Label formLabel = new Label("Enregistrer un Traitement");
        formLabel.setStyle("-fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        // Description
        formGrid.add(new Label("Description:"), 0, 0);
        taDescription = new TextArea();
        taDescription.setPromptText("Détails du traitement...");
        taDescription.setPrefRowCount(3);
        formGrid.add(taDescription, 1, 0);

        // Coût
        formGrid.add(new Label("Coût (€):"), 0, 1);
        tfCout = new TextField();
        tfCout.setPromptText("Ex: 150.50");
        formGrid.add(tfCout, 1, 1);

        // Patient
        formGrid.add(new Label("Patient:"), 0, 2);
        cbPatient = new ComboBox<>();
        chargerPatientsComboBox();
        formGrid.add(cbPatient, 1, 2);

        // Médecin
        formGrid.add(new Label("Médecin:"), 0, 3);
        cbMedecin = new ComboBox<>();
        chargerMedecinsComboBox();
        formGrid.add(cbMedecin, 1, 3);

        // Boutons d'action
        HBox buttonBox = new HBox(10);
        Button btnAjouter = new Button("➕ Ajouter");
        btnAjouter.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnAjouter.setOnAction(e -> ajouterTraitement());

        Button btnModifier = new Button("✏️ Modifier");
        btnModifier.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        btnModifier.setOnAction(e -> modifierTraitement());

        Button btnSupprimer = new Button("🗑️ Supprimer");
        btnSupprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnSupprimer.setOnAction(e -> supprimerTraitement());

        Button btnVider = new Button("🧹 Vider");
        btnVider.setOnAction(e -> viderFormulaire());

        buttonBox.getChildren().addAll(btnAjouter, btnModifier, btnSupprimer, btnVider);

        formPanel.getChildren().addAll(formLabel, formGrid, buttonBox);

        // Gestionnaire de sélection
        tableTraitements.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    afficherTraitementDansFormulaire(newValue);
                }
            });

        // Assemblage
        mainLayout.setTop(topPanel);
        mainLayout.setCenter(tableTraitements);
        mainLayout.setBottom(formPanel);

        // Charger les données
        chargerTousLesTraitements();

        // Scène
        Scene scene = new Scene(mainLayout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void chargerTousLesTraitements() {
        List<Traitement> traitements = traitementService.getAllTraitements();
        if (traitements != null) {
            traitementData.clear();
            traitementData.addAll(traitements);
        }
    }

    private void filtrerParPatient() {
        String idPatientStr = tfRecherche.getText();
        if (!idPatientStr.isEmpty()) {
            try {
                int idPatient = Integer.parseInt(idPatientStr);
                List<Traitement> traitements = traitementService.getTraitementsByPatient(idPatient);
                traitementData.clear();
                if (traitements != null) {
                    traitementData.addAll(traitements);
                }
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer un ID patient valide.");
            }
        } else {
            chargerTousLesTraitements();
        }
    }

    private void afficherStatistiques() {
        Object[] stats = traitementService.getStatistiques();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📊 Statistiques des Traitements");
        alert.setHeaderText("Résumé financier");

        String content = "📈 **Statistiques des Traitements**\n\n"
                       + "• Nombre total de traitements: " + stats[0] + "\n"
                       + "• Coût total des traitements: " + stats[1] + " €\n"
                       + "• Coût moyen par traitement: " + stats[2] + " €\n\n"
                       + "📋 **Informations générales**\n"
                       + "• Nombre de patients: " + patientService.getNombrePatients() + "\n"
                       + "• Nombre de médecins: " + medecinService.getNombreMedecins();

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void chargerPatientsComboBox() {
        List<Patient> patients = patientService.getAllPatients();
        patientsList = FXCollections.observableArrayList();
        if (patients != null) {
            patientsList.addAll(patients);
        }
        cbPatient.setItems(patientsList);
        cbPatient.setCellFactory(param -> new ListCell<Patient>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getIdPatient() + " - " + item.getPrenom() + " " + item.getNom());
                }
            }
        });
        cbPatient.setButtonCell(new ListCell<Patient>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getIdPatient() + " - " + item.getPrenom() + " " + item.getNom());
                }
            }
        });
    }

    private void chargerMedecinsComboBox() {
        List<Medecin> medecins = medecinService.getAllMedecins();
        medecinsList = FXCollections.observableArrayList();
        if (medecins != null) {
            medecinsList.addAll(medecins);
        }
        cbMedecin.setItems(medecinsList);
        cbMedecin.setCellFactory(param -> new ListCell<Medecin>() {
            @Override
            protected void updateItem(Medecin item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getIdMedecin() + " - Dr. " + item.getNom() + " (" + item.getSpecialite() + ")");
                }
            }
        });
        cbMedecin.setButtonCell(new ListCell<Medecin>() {
            @Override
            protected void updateItem(Medecin item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getIdMedecin() + " - Dr. " + item.getNom() + " (" + item.getSpecialite() + ")");
                }
            }
        });
    }

    private void ajouterTraitement() {
        String description = taDescription.getText();
        String coutStr = tfCout.getText();
        Patient patient = cbPatient.getValue();
        Medecin medecin = cbMedecin.getValue();

        if (description.isEmpty() || coutStr.isEmpty() || patient == null || medecin == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            BigDecimal cout = new BigDecimal(coutStr);
            boolean success = traitementService.ajouterTraitement(description, cout,
                patient.getIdPatient(), medecin.getIdMedecin());

            if (success) {
                showAlert("Succès", "Traitement enregistré avec succès!");
                chargerTousLesTraitements();
                viderFormulaire();
            } else {
                showAlert("Erreur", "Échec de l'enregistrement du traitement.");
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format de coût invalide. Utilisez un nombre (ex: 150.50)");
        }
    }

    private void modifierTraitement() {
        Traitement traitementSelectionne = tableTraitements.getSelectionModel().getSelectedItem();

        if (traitementSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un traitement à modifier.");
            return;
        }

        String description = taDescription.getText();
        String coutStr = tfCout.getText();
        Patient patient = cbPatient.getValue();
        Medecin medecin = cbMedecin.getValue();

        if (description.isEmpty() || coutStr.isEmpty() || patient == null || medecin == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            BigDecimal cout = new BigDecimal(coutStr);
            boolean success = traitementService.modifierTraitement(
                traitementSelectionne.getIdTraitement(), description, cout,
                patient.getIdPatient(), medecin.getIdMedecin());

            if (success) {
                showAlert("Succès", "Traitement modifié avec succès!");
                chargerTousLesTraitements();
                viderFormulaire();
            } else {
                showAlert("Erreur", "Échec de la modification du traitement.");
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format de coût invalide.");
        }
    }

    private void supprimerTraitement() {
        Traitement traitementSelectionne = tableTraitements.getSelectionModel().getSelectedItem();

        if (traitementSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un traitement à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Suppression du traitement");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer le traitement #" +
            traitementSelectionne.getIdTraitement() + "?\n\n" +
            traitementSelectionne.getDescription() + "\nCoût: " + traitementSelectionne.getCout() + " €");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            boolean success = traitementService.supprimerTraitement(traitementSelectionne.getIdTraitement());

            if (success) {
                showAlert("Succès", "Traitement supprimé avec succès!");
                chargerTousLesTraitements();
                viderFormulaire();
            } else {
                showAlert("Erreur", "Échec de la suppression du traitement.");
            }
        }
    }

    private void afficherTraitementDansFormulaire(Traitement traitement) {
        taDescription.setText(traitement.getDescription());
        tfCout.setText(traitement.getCout() != null ? traitement.getCout().toString() : "");

        // Trouver le patient et médecin correspondants
        Patient patient = null;
        if (patientsList != null) {
            for (Patient p : patientsList) {
                if (p.getIdPatient() == traitement.getIdPatient()) {
                    patient = p;
                    break;
                }
            }
        }

        Medecin medecin = null;
        if (medecinsList != null) {
            for (Medecin m : medecinsList) {
                if (m.getIdMedecin() == traitement.getIdMedecin()) {
                    medecin = m;
                    break;
                }
            }
        }

        if (patient != null) cbPatient.setValue(patient);
        if (medecin != null) cbMedecin.setValue(medecin);
    }

    private void viderFormulaire() {
        taDescription.clear();
        tfCout.clear();
        cbPatient.setValue(null);
        cbMedecin.setValue(null);
        tableTraitements.getSelectionModel().clearSelection();
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