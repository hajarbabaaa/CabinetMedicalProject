package com.cabinetmedical.dao;

import com.cabinetmedical.config.DatabaseConnection;
import com.cabinetmedical.model.RendezVous;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDAO {
    private Connection connection;
    
    public RendezVousDAO() {
         this.connection = DatabaseConnection.getConnection();
    }
    
    // CREATE - Ajouter un rendez-vous
    public boolean ajouterRendezVous(RendezVous rdv) {
        String sql = "INSERT INTO RendezVous (date_rdv, id_patient, id_medecin) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(rdv.getDateRdv()));
            stmt.setInt(2, rdv.getIdPatient());
            stmt.setInt(3, rdv.getIdMedecin());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        rdv.setIdRdv(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du rendez-vous: " + e.getMessage());
            return false;
        }
    }
    
    // READ ALL - Récupérer tous les rendez-vous avec noms
    public List<RendezVous> getAllRendezVous() {
        List<RendezVous> rendezVous = new ArrayList<>();
        String sql = "SELECT r.*, p.nom as patient_nom, p.prenom as patient_prenom, "
                   + "m.nom as medecin_nom FROM RendezVous r "
                   + "JOIN Patients p ON r.id_patient = p.id_patient "
                   + "JOIN Medecins m ON r.id_medecin = m.id_medecin "
                   + "ORDER BY r.date_rdv DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                RendezVous rdv = new RendezVous(
                    rs.getInt("id_rdv"),
                    rs.getTimestamp("date_rdv").toLocalDateTime(),
                    rs.getInt("id_patient"),
                    rs.getInt("id_medecin")
                );
                rdv.setNomPatient(rs.getString("patient_prenom") + " " + rs.getString("patient_nom"));
                rdv.setNomMedecin("Dr. " + rs.getString("medecin_nom"));
                rendezVous.add(rdv);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des rendez-vous: " + e.getMessage());
        }
        return rendezVous;
    }
    
    // READ BY ID - Récupérer un rendez-vous par ID
    public RendezVous getRendezVousById(int id) {
        String sql = "SELECT r.*, p.nom as patient_nom, p.prenom as patient_prenom, "
                   + "m.nom as medecin_nom FROM RendezVous r "
                   + "JOIN Patients p ON r.id_patient = p.id_patient "
                   + "JOIN Medecins m ON r.id_medecin = m.id_medecin "
                   + "WHERE r.id_rdv = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    RendezVous rdv = new RendezVous(
                        rs.getInt("id_rdv"),
                        rs.getTimestamp("date_rdv").toLocalDateTime(),
                        rs.getInt("id_patient"),
                        rs.getInt("id_medecin")
                    );
                    rdv.setNomPatient(rs.getString("patient_prenom") + " " + rs.getString("patient_nom"));
                    rdv.setNomMedecin("Dr. " + rs.getString("medecin_nom"));
                    return rdv;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du rendez-vous: " + e.getMessage());
        }
        return null;
    }
    
    // GET BY PATIENT - Récupérer les rendez-vous d'un patient
    public List<RendezVous> getRendezVousByPatient(int idPatient) {
        List<RendezVous> rendezVous = new ArrayList<>();
        String sql = "SELECT r.*, p.nom as patient_nom, p.prenom as patient_prenom, "
                   + "m.nom as medecin_nom FROM RendezVous r "
                   + "JOIN Patients p ON r.id_patient = p.id_patient "
                   + "JOIN Medecins m ON r.id_medecin = m.id_medecin "
                   + "WHERE r.id_patient = ? ORDER BY r.date_rdv DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPatient);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RendezVous rdv = new RendezVous(
                        rs.getInt("id_rdv"),
                        rs.getTimestamp("date_rdv").toLocalDateTime(),
                        rs.getInt("id_patient"),
                        rs.getInt("id_medecin")
                    );
                    rdv.setNomPatient(rs.getString("patient_prenom") + " " + rs.getString("patient_nom"));
                    rdv.setNomMedecin("Dr. " + rs.getString("medecin_nom"));
                    rendezVous.add(rdv);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des RDV par patient: " + e.getMessage());
        }
        return rendezVous;
    }
    
    // GET BY MEDECIN - Récupérer les rendez-vous d'un médecin
    public List<RendezVous> getRendezVousByMedecin(int idMedecin) {
        List<RendezVous> rendezVous = new ArrayList<>();
        String sql = "SELECT r.*, p.nom as patient_nom, p.prenom as patient_prenom, "
                   + "m.nom as medecin_nom FROM RendezVous r "
                   + "JOIN Patients p ON r.id_patient = p.id_patient "
                   + "JOIN Medecins m ON r.id_medecin = m.id_medecin "
                   + "WHERE r.id_medecin = ? ORDER BY r.date_rdv DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedecin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RendezVous rdv = new RendezVous(
                        rs.getInt("id_rdv"),
                        rs.getTimestamp("date_rdv").toLocalDateTime(),
                        rs.getInt("id_patient"),
                        rs.getInt("id_medecin")
                    );
                    rdv.setNomPatient(rs.getString("patient_prenom") + " " + rs.getString("patient_nom"));
                    rdv.setNomMedecin("Dr. " + rs.getString("medecin_nom"));
                    rendezVous.add(rdv);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des RDV par médecin: " + e.getMessage());
        }
        return rendezVous;
    }
    
    // UPDATE - Modifier un rendez-vous
    public boolean modifierRendezVous(RendezVous rdv) {
        String sql = "UPDATE RendezVous SET date_rdv = ?, id_patient = ?, id_medecin = ? WHERE id_rdv = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(rdv.getDateRdv()));
            stmt.setInt(2, rdv.getIdPatient());
            stmt.setInt(3, rdv.getIdMedecin());
            stmt.setInt(4, rdv.getIdRdv());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du rendez-vous: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Supprimer un rendez-vous
    public boolean supprimerRendezVous(int idRdv) {
        String sql = "DELETE FROM RendezVous WHERE id_rdv = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idRdv);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du rendez-vous: " + e.getMessage());
            return false;
        }
    }
    
    // Vérifier si un créneau est disponible
    public boolean isCreneauDisponible(LocalDateTime dateTime, int idMedecin) {
        String sql = "SELECT COUNT(*) FROM RendezVous WHERE id_medecin = ? "
                   + "AND date_rdv BETWEEN ? AND DATE_ADD(?, INTERVAL 30 MINUTE)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedecin);
            stmt.setTimestamp(2, Timestamp.valueOf(dateTime));
            stmt.setTimestamp(3, Timestamp.valueOf(dateTime));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du créneau: " + e.getMessage());
        }
        return false;
    }
}