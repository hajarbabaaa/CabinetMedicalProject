package com.cabinetmedical.service;

import com.cabinetmedical.dao.RendezVousDAO;
import com.cabinetmedical.model.RendezVous;
import java.time.LocalDateTime;
import java.util.List;

public class RendezVousService {
    private RendezVousDAO rendezVousDAO;
    
    public RendezVousService() {
        this.rendezVousDAO = new RendezVousDAO();
    }
    
    public boolean ajouterRendezVous(LocalDateTime dateRdv, int idPatient, int idMedecin) {
        try {
            RendezVous rdv = new RendezVous(dateRdv, idPatient, idMedecin);
            return rendezVousDAO.ajouterRendezVous(rdv);
        } catch (Exception e) {
            System.err.println("Erreur dans le service RendezVous: " + e.getMessage());
            return false;
        }
    }
    
    public List<RendezVous> getAllRendezVous() {
        return rendezVousDAO.getAllRendezVous();
    }
    
    public List<RendezVous> getRendezVousByPatient(int idPatient) {
        return rendezVousDAO.getRendezVousByPatient(idPatient);
    }
    
    public List<RendezVous> getRendezVousByMedecin(int idMedecin) {
        return rendezVousDAO.getRendezVousByMedecin(idMedecin);
    }
    
    public boolean modifierRendezVous(int idRdv, LocalDateTime dateRdv, int idPatient, int idMedecin) {
        try {
            RendezVous rdv = rendezVousDAO.getRendezVousById(idRdv);
            if (rdv != null) {
                rdv.setDateRdv(dateRdv);
                rdv.setIdPatient(idPatient);
                rdv.setIdMedecin(idMedecin);
                
                return rendezVousDAO.modifierRendezVous(rdv);
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de la modification: " + e.getMessage());
            return false;
        }
    }
    
    public boolean supprimerRendezVous(int idRdv) {
        return rendezVousDAO.supprimerRendezVous(idRdv);
    }
    
    public boolean verifierDisponibilite(LocalDateTime dateTime, int idMedecin) {
        return rendezVousDAO.isCreneauDisponible(dateTime, idMedecin);
    }
    
    public RendezVous getRendezVousById(int id) {
        return rendezVousDAO.getRendezVousById(id);
    }
}