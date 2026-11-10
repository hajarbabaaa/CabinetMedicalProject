// src/com/cabinetmedical/service/TraitementService.java
package com.cabinetmedical.service;

import com.cabinetmedical.model.Traitement;
import java.math.BigDecimal;
import java.util.List;

public class TraitementService {
    
    public List<Traitement> getAllTraitements() {
        // Implémentez cette méthode
        return null;
    }
    
    public List<Traitement> getTraitementsByPatient(int idPatient) {
        // Implémentez cette méthode
        return null;
    }
    
    public Object[] getStatistiques() {
        // Retourner un tableau d'objets avec les statistiques
        Object[] stats = new Object[3];
        stats[0] = 0; // Nombre total de traitements
        stats[1] = "0.00"; // Coût total
        stats[2] = "0.00"; // Coût moyen
        return stats;
    }
    
    public boolean ajouterTraitement(String description, BigDecimal cout, int idPatient, int idMedecin) {
        // Implémentez cette méthode
        return false;
    }
    
    public boolean modifierTraitement(int idTraitement, String description, BigDecimal cout, int idPatient, int idMedecin) {
        // Implémentez cette méthode
        return false;
    }
    
    public boolean supprimerTraitement(int idTraitement) {
        // Implémentez cette méthode
        return false;
    }
}