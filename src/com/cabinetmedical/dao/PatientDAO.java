package com.cabinetmedical.dao;

import com.cabinetmedical.model.Patient;
import com.cabinetmedical.config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    private Connection connection;
    
    public PatientDAO() {
        // CORRECTION ICI : Utilisez DatabaseConnection.getConnection() directement
        this.connection = DatabaseConnection.getConnection();
    }
    // CREATE - Ajouter un patient
    public boolean ajouterPatient(Patient patient) {
        String sql = "INSERT INTO Patients (nom, prenom, date_naissance, adresse, telephone) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setDate(3, Date.valueOf(patient.getDateNaissance()));
            stmt.setString(4, patient.getAdresse());
            stmt.setString(5, patient.getTelephone());
            
            int rowsAffected = stmt.executeUpdate();
            
            // Récupérer l'ID généré
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        patient.setIdPatient(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du patient: " + e.getMessage());
            return false;
        }
    }
    
    // READ ALL - Récupérer tous les patients
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patients ORDER BY nom, prenom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("id_patient"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getDate("date_naissance").toLocalDate(),
                    rs.getString("adresse"),
                    rs.getString("telephone")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des patients: " + e.getMessage());
        }
        return patients;
    }
    
    // READ BY ID - Récupérer un patient par ID
    public Patient getPatientById(int id) {
        String sql = "SELECT * FROM Patients WHERE id_patient = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                        rs.getInt("id_patient"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getString("adresse"),
                        rs.getString("telephone")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du patient: " + e.getMessage());
        }
        return null;
    }
    
    // UPDATE - Modifier un patient
    public boolean modifierPatient(Patient patient) {
        String sql = "UPDATE Patients SET nom = ?, prenom = ?, date_naissance = ?, adresse = ?, telephone = ? WHERE id_patient = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setDate(3, Date.valueOf(patient.getDateNaissance()));
            stmt.setString(4, patient.getAdresse());
            stmt.setString(5, patient.getTelephone());
            stmt.setInt(6, patient.getIdPatient());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du patient: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Supprimer un patient
    public boolean supprimerPatient(int idPatient) {
        String sql = "DELETE FROM Patients WHERE id_patient = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPatient);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du patient: " + e.getMessage());
            return false;
        }
    }
    
    // SEARCH - Rechercher des patients
    public List<Patient> rechercherPatients(String critere) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patients WHERE nom LIKE ? OR prenom LIKE ? OR telephone LIKE ? ORDER BY nom, prenom";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String likeCritere = "%" + critere + "%";
            stmt.setString(1, likeCritere);
            stmt.setString(2, likeCritere);
            stmt.setString(3, likeCritere);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient(
                        rs.getInt("id_patient"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("date_naissance").toLocalDate(),
                        rs.getString("adresse"),
                        rs.getString("telephone")
                    );
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des patients: " + e.getMessage());
        }
        return patients;
    }
    
    // Compter le nombre total de patients
    public int countPatients() {
        String sql = "SELECT COUNT(*) FROM Patients";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des patients: " + e.getMessage());
        }
        return 0;
    }
}