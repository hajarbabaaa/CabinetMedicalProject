package com.cabinetmedical.service;

import com.cabinetmedical.dao.MedecinDAO;
import com.cabinetmedical.model.Medecin;
import java.util.List;

public class MedecinService {
    private MedecinDAO medecinDAO;
    
    public MedecinService() {
        this.medecinDAO = new MedecinDAO();
    }
    
    public boolean ajouterMedecin(String nom, String specialite, String telephone) {
        try {
            Medecin medecin = new Medecin(nom, specialite, telephone);
            return medecinDAO.ajouterMedecin(medecin);
        } catch (Exception e) {
            System.err.println("Erreur dans le service Medecin: " + e.getMessage());
            return false;
        }
    }
    
    public List<Medecin> getAllMedecins() {
        return medecinDAO.getAllMedecins();
    }
    
    public Medecin getMedecinById(int id) {
        return medecinDAO.getMedecinById(id);
    }
    
    public boolean modifierMedecin(int id, String nom, String specialite, String telephone) {
        try {
            Medecin medecin = medecinDAO.getMedecinById(id);
            if (medecin != null) {
                medecin.setNom(nom);
                medecin.setSpecialite(specialite);
                medecin.setTelephone(telephone);
                
                return medecinDAO.modifierMedecin(medecin);
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de la modification: " + e.getMessage());
            return false;
        }
    }
    
    public boolean supprimerMedecin(int id) {
        return medecinDAO.supprimerMedecin(id);
    }
    
    public List<Medecin> rechercherParSpecialite(String specialite) {
        return medecinDAO.rechercherParSpecialite(specialite);
    }
    
    public int getNombreMedecins() {
        return medecinDAO.countMedecins();
    }
}