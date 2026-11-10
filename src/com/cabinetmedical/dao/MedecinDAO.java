package com.cabinetmedical.dao;

import com.cabinetmedical.config.DatabaseConnection;
import com.cabinetmedical.model.Medecin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO {
    private Connection connection;
    
    public MedecinDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    // CREATE - Ajouter un médecin
    public boolean ajouterMedecin(Medecin medecin) {
        String sql = "INSERT INTO Medecins (nom, specialite, telephone) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, medecin.getNom());
            stmt.setString(2, medecin.getSpecialite());
            stmt.setString(3, medecin.getTelephone());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        medecin.setIdMedecin(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du médecin: " + e.getMessage());
            return false;
        }
    }
    
    // READ ALL - Récupérer tous les médecins
    public List<Medecin> getAllMedecins() {
        List<Medecin> medecins = new ArrayList<>();
        String sql = "SELECT * FROM Medecins ORDER BY nom";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Medecin medecin = new Medecin(
                    rs.getInt("id_medecin"),
                    rs.getString("nom"),
                    rs.getString("specialite"),
                    rs.getString("telephone")
                );
                medecins.add(medecin);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des médecins: " + e.getMessage());
        }
        return medecins;
    }
    
    // READ BY ID - Récupérer un médecin par ID
    public Medecin getMedecinById(int id) {
        String sql = "SELECT * FROM Medecins WHERE id_medecin = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Medecin(
                        rs.getInt("id_medecin"),
                        rs.getString("nom"),
                        rs.getString("specialite"),
                        rs.getString("telephone")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du médecin: " + e.getMessage());
        }
        return null;
    }
    
    // UPDATE - Modifier un médecin
    public boolean modifierMedecin(Medecin medecin) {
        String sql = "UPDATE Medecins SET nom = ?, specialite = ?, telephone = ? WHERE id_medecin = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, medecin.getNom());
            stmt.setString(2, medecin.getSpecialite());
            stmt.setString(3, medecin.getTelephone());
            stmt.setInt(4, medecin.getIdMedecin());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du médecin: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Supprimer un médecin
    public boolean supprimerMedecin(int idMedecin) {
        String sql = "DELETE FROM Medecins WHERE id_medecin = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedecin);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du médecin: " + e.getMessage());
            return false;
        }
    }
    
    // Rechercher médecins par spécialité
    public List<Medecin> rechercherParSpecialite(String specialite) {
        List<Medecin> medecins = new ArrayList<>();
        String sql = "SELECT * FROM Medecins WHERE specialite LIKE ? ORDER BY nom";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + specialite + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Medecin medecin = new Medecin(
                        rs.getInt("id_medecin"),
                        rs.getString("nom"),
                        rs.getString("specialite"),
                        rs.getString("telephone")
                    );
                    medecins.add(medecin);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par spécialité: " + e.getMessage());
        }
        return medecins;
    }
    
    // Compter le nombre de médecins
    public int countMedecins() {
        String sql = "SELECT COUNT(*) FROM Medecins";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des médecins: " + e.getMessage());
        }
        return 0;
    }
}