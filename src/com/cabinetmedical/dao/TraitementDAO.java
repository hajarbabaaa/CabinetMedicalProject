package com.cabinetmedical.dao;

import com.cabinetmedical.config.DatabaseConnection;
import com.cabinetmedical.model.Traitement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TraitementDAO {
    private Connection connection;
    
    public TraitementDAO() {
         this.connection = DatabaseConnection.getConnection();
    }
    
    // CREATE - Ajouter un traitement
    public boolean ajouterTraitement(Traitement traitement) {
        String sql = "INSERT INTO Traitements (description, medicaments, duree, id_patient, id_medecin) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, traitement.getDescription());
            stmt.setString(2, traitement.getMedicaments());
            stmt.setString(3, traitement.getDuree());
            stmt.setInt(4, traitement.getIdPatient());
            stmt.setInt(5, traitement.getIdMedecin());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        traitement.setIdTraitement(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du traitement: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // READ ALL - Récupérer tous les traitements
    public List<Traitement> getAllTraitements() {
        List<Traitement> traitements = new ArrayList<>();
        String sql = "SELECT * FROM Traitements ORDER BY id_traitement DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Traitement traitement = new Traitement();
                traitement.setIdTraitement(rs.getInt("id_traitement"));
                traitement.setDescription(rs.getString("description"));
                traitement.setMedicaments(rs.getString("medicaments"));
                traitement.setDuree(rs.getString("duree"));
                traitement.setIdPatient(rs.getInt("id_patient"));
                traitement.setIdMedecin(rs.getInt("id_medecin"));
                traitements.add(traitement);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des traitements: " + e.getMessage());
            e.printStackTrace();
        }
        return traitements;
    }
    
    // GET BY PATIENT - Récupérer les traitements d'un patient
    public List<Traitement> getTraitementsByPatient(int idPatient) {
        List<Traitement> traitements = new ArrayList<>();
        String sql = "SELECT * FROM Traitements WHERE id_patient = ? ORDER BY id_traitement DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPatient);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Traitement traitement = new Traitement();
                    traitement.setIdTraitement(rs.getInt("id_traitement"));
                    traitement.setDescription(rs.getString("description"));
                    traitement.setMedicaments(rs.getString("medicaments"));
                    traitement.setDuree(rs.getString("duree"));
                    traitement.setIdPatient(rs.getInt("id_patient"));
                    traitement.setIdMedecin(rs.getInt("id_medecin"));
                    traitements.add(traitement);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des traitements par patient: " + e.getMessage());
            e.printStackTrace();
        }
        return traitements;
    }
    
    // GET BY ID - Récupérer un traitement par ID
    public Traitement getTraitementById(int id) {
        String sql = "SELECT * FROM Traitements WHERE id_traitement = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Traitement traitement = new Traitement();
                    traitement.setIdTraitement(rs.getInt("id_traitement"));
                    traitement.setDescription(rs.getString("description"));
                    traitement.setMedicaments(rs.getString("medicaments"));
                    traitement.setDuree(rs.getString("duree"));
                    traitement.setIdPatient(rs.getInt("id_patient"));
                    traitement.setIdMedecin(rs.getInt("id_medecin"));
                    return traitement;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du traitement: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    // UPDATE - Modifier un traitement
    public boolean modifierTraitement(Traitement traitement) {
        String sql = "UPDATE Traitements SET description = ?, medicaments = ?, duree = ?, id_patient = ?, id_medecin = ? WHERE id_traitement = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, traitement.getDescription());
            stmt.setString(2, traitement.getMedicaments());
            stmt.setString(3, traitement.getDuree());
            stmt.setInt(4, traitement.getIdPatient());
            stmt.setInt(5, traitement.getIdMedecin());
            stmt.setInt(6, traitement.getIdTraitement());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du traitement: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE - Supprimer un traitement
    public boolean supprimerTraitement(int idTraitement) {
        String sql = "DELETE FROM Traitements WHERE id_traitement = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idTraitement);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du traitement: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}