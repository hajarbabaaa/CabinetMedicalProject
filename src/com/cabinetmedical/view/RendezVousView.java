package com.cabinetmedical.view;

import com.cabinetmedical.service.RendezVousService;
import com.cabinetmedical.service.PatientService;
import com.cabinetmedical.service.MedecinService;
import com.cabinetmedical.model.RendezVous;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class RendezVousView extends Application {

    private RendezVousService rendezVousService;
    private PatientService patientService;
    private MedecinService medecinService;
    private TableView<RendezVous> tableRDV;
    private ObservableList<RendezVous> rdvData;
    private ObservableList<Patient> patientsList;
    private ObservableList<Medecin> medecinsList;

    private DatePicker dpDate;
    private ComboBox<Integer> cbHeure, cbMinute;
    private ComboBox<Patient> cbPatient;
    private ComboBox<Medecin> cbMedecin;
    private TextField tfRecherche;

    @Override
    public void start(Stage primaryStage) {
        rendezVousService = new RendezVousService();
        patientService = new PatientService();
        medecinService = new MedecinService();

        primaryStage.setTitle("📅 Gestion des Rendez-vous");

        // Layout principal
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Panneau supérieur
        VBox topPanel = new VBox(10);

        Label titleLabel = new Label("GESTION DES RENDEZ-VOUS");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox searchBox = new HBox(10);
        Label searchLabel = new Label("Filtrer:");
        tfRecherche = new TextField();
        tfRecherche.setPromptText("Nom patient ou médecin...");
        Button btnAujourdhui = new Button("📅 Aujourd'hui");
        btnAujourdhui.setOnAction(e -> filtrerAujourdhui());
        Button btnTous = new Button("📋 Tous les RDV");
        btnTous.setOnAction(e -> chargerTousLesRDV());

        searchBox.getChildren().addAll(searchLabel, tfRecherche, btnAujourdhui, btnTous);
        topPanel.getChildren().addAll(titleLabel, searchBox);

        // Tableau des rendez-vous
        tableRDV = new TableView<>();
        rdvData = FXCollections.observableArrayList();
        tableRDV.setItems(rdvData);

        // Colonnes du tableau
        TableColumn<RendezVous, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idRdv"));

        TableColumn<RendezVous, String> colDate = new TableColumn<>("Date/Heure");
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateRdv"));

        TableColumn<RendezVous, String> colPatient = new TableColumn<>("Patient");
        colPatient.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));

        TableColumn<RendezVous, String> colMedecin = new TableColumn<>("Médecin");
        colMedecin.setCellValueFactory(new PropertyValueFactory<>("nomMedecin"));

        tableRDV.getColumns().addAll(colId, colDate, colPatient, colMedecin);

        // Formulaire
        VBox formPanel = new VBox(10);
        formPanel.setPadding(new Insets(10));
        formPanel.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 5;");

        Label formLabel = new Label("Planifier un Rendez-vous");
        formLabel.setStyle("-fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        // Date
        formGrid.add(new Label("Date:"), 0, 0);
        dpDate = new DatePicker();
        dpDate.setValue(LocalDate.now());
        formGrid.add(dpDate, 1, 0);

        // Heure
        formGrid.add(new Label("Heure:"), 0, 1);
        HBox timeBox = new HBox(5);
        cbHeure = new ComboBox<>();
        cbMinute = new ComboBox<>();
        for (int i = 8; i <= 18; i++) cbHeure.getItems().add(i);
        for (int i = 0; i < 60; i += 15) cbMinute.getItems().add(i);
        cbHeure.setValue(9);
        cbMinute.setValue(0);
        timeBox.getChildren().addAll(cbHeure, new Label("h"), cbMinute);
        formGrid.add(timeBox, 1, 1);

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

        // Vérifier disponibilité
        Button btnVerifier = new Button("✓ Vérifier disponibilité");
        btnVerifier.setOnAction(e -> verifierDisponibilite());
        formGrid.add(btnVerifier, 1, 4);

        // Boutons d'action
        HBox buttonBox = new HBox(10);
        Button btnAjouter = new Button("➕ Planifier");
        btnAjouter.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnAjouter.setOnAction(e -> ajouterRDV());

        Button btnModifier = new Button("✏️ Modifier");
        btnModifier.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        btnModifier.setOnAction(e -> modifierRDV());

        Button btnSupprimer = new Button("🗑️ Annuler RDV");
        btnSupprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnSupprimer.setOnAction(e -> supprimerRDV());

        Button btnVider = new Button("🧹 Vider");
        btnVider.setOnAction(e -> viderFormulaire());

        buttonBox.getChildren().addAll(btnAjouter, btnModifier, btnSupprimer, btnVider);

        formPanel.getChildren().addAll(formLabel, formGrid, buttonBox);

        // Gestionnaire de sélection
        tableRDV.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    afficherRDVDansFormulaire(newValue);
                }
            });

        // Assemblage
        mainLayout.setTop(topPanel);
        mainLayout.setCenter(tableRDV);
        mainLayout.setBottom(formPanel);

        // Charger les données
        chargerTousLesRDV();

        // Scène
        Scene scene = new Scene(mainLayout, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void chargerTousLesRDV() {
        List<RendezVous> rdvs = rendezVousService.getAllRendezVous();
        rdvData.clear();
        rdvData.addAll(rdvs);
    }

    private void filtrerAujourdhui() {
        rdvData.clear();
        List<RendezVous> tousRDV = rendezVousService.getAllRendezVous();
        LocalDate aujourdhui = LocalDate.now();

        for (RendezVous rdv : tousRDV) {
            if (rdv.getDateRdv().toLocalDate().equals(aujourdhui)) {
                rdvData.add(rdv);
            }
        }
    }

    private void chargerPatientsComboBox() {
        patientsList = FXCollections.observableArrayList(patientService.getAllPatients());
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
        medecinsList = FXCollections.observableArrayList(medecinService.getAllMedecins());
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

    private void verifierDisponibilite() {
        LocalDateTime dateTime = getDateTimeFromForm();
        Medecin medecin = cbMedecin.getValue();

        if (medecin == null) {
            showAlert("Erreur", "Veuillez sélectionner un médecin.");
            return;
        }

        boolean disponible = rendezVousService.verifierDisponibilite(dateTime, medecin.getIdMedecin());

        if (disponible) {
            showAlert("Disponible", "✅ Le créneau est disponible pour le Dr. " + medecin.getNom());
        } else {
            showAlert("Indisponible", "❌ Le créneau est déjà occupé pour ce médecin.");
        }
    }

    private void ajouterRDV() {
        LocalDateTime dateTime = getDateTimeFromForm();
        Patient patient = cbPatient.getValue();
        Medecin medecin = cbMedecin.getValue();

        if (patient == null || medecin == null) {
            showAlert("Erreur", "Veuillez sélectionner un patient et un médecin.");
            return;
        }

        boolean success = rendezVousService.ajouterRendezVous(dateTime,
            patient.getIdPatient(), medecin.getIdMedecin());

        if (success) {
            showAlert("Succès", "Rendez-vous planifié avec succès!");
            chargerTousLesRDV();
            viderFormulaire();
        } else {
            showAlert("Erreur", "Échec de la planification du rendez-vous.");
        }
    }

    private void modifierRDV() {
        RendezVous rdvSelectionne = tableRDV.getSelectionModel().getSelectedItem();

        if (rdvSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un rendez-vous à modifier.");
            return;
        }

        LocalDateTime dateTime = getDateTimeFromForm();
        Patient patient = cbPatient.getValue();
        Medecin medecin = cbMedecin.getValue();

        if (patient == null || medecin == null) {
            showAlert("Erreur", "Veuillez sélectionner un patient et un médecin.");
            return;
        }

        boolean success = rendezVousService.modifierRendezVous(
            rdvSelectionne.getIdRdv(), dateTime, patient.getIdPatient(), medecin.getIdMedecin());

        if (success) {
            showAlert("Succès", "Rendez-vous modifié avec succès!");
            chargerTousLesRDV();
            viderFormulaire();
        } else {
            showAlert("Erreur", "Échec de la modification du rendez-vous.");
        }
    }

    private void supprimerRDV() {
        RendezVous rdvSelectionne = tableRDV.getSelectionModel().getSelectedItem();

        if (rdvSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un rendez-vous à annuler.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Annulation du rendez-vous");
        confirmation.setContentText("Êtes-vous sûr de vouloir annuler le rendez-vous #" +
            rdvSelectionne.getIdRdv() + " du " + rdvSelectionne.getDateRdv() + "?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            boolean success = rendezVousService.supprimerRendezVous(rdvSelectionne.getIdRdv());

            if (success) {
                showAlert("Succès", "Rendez-vous annulé avec succès!");
                chargerTousLesRDV();
                viderFormulaire();
            } else {
                showAlert("Erreur", "Échec de l'annulation du rendez-vous.");
            }
        }
    }

    private LocalDateTime getDateTimeFromForm() {
        LocalDate date = dpDate.getValue();
        int heure = cbHeure.getValue();
        int minute = cbMinute.getValue();
        return LocalDateTime.of(date, LocalTime.of(heure, minute));
    }

    private void afficherRDVDansFormulaire(RendezVous rdv) {
        // Trouver le patient et médecin correspondants
        Patient patient = null;
        for (Patient p : patientsList) {
            if (p.getIdPatient() == rdv.getIdPatient()) {
                patient = p;
                break;
            }
        }

        Medecin medecin = null;
        for (Medecin m : medecinsList) {
            if (m.getIdMedecin() == rdv.getIdMedecin()) {
                medecin = m;
                break;
            }
        }

        if (patient != null) cbPatient.setValue(patient);
        if (medecin != null) cbMedecin.setValue(medecin);

        dpDate.setValue(rdv.getDateRdv().toLocalDate());
        cbHeure.setValue(rdv.getDateRdv().getHour());
        cbMinute.setValue(rdv.getDateRdv().getMinute());
    }

    private void viderFormulaire() {
        dpDate.setValue(LocalDate.now());
        cbHeure.setValue(9);
        cbMinute.setValue(0);
        cbPatient.setValue(null);
        cbMedecin.setValue(null);
        tableRDV.getSelectionModel().clearSelection();
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