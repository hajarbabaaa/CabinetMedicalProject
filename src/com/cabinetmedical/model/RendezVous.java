package com.cabinetmedical.model;

import java.time.LocalDateTime;

public class RendezVous {
    private int idRdv;
    private LocalDateTime dateRdv;
    private int idPatient;
    private int idMedecin;
    private String nomPatient;
    private String nomMedecin;
    
    // Constructeurs
    public RendezVous() {}
    
    public RendezVous(LocalDateTime dateRdv, int idPatient, int idMedecin) {
        this.dateRdv = dateRdv;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
    }
    
    public RendezVous(int idRdv, LocalDateTime dateRdv, int idPatient, int idMedecin) {
        this.idRdv = idRdv;
        this.dateRdv = dateRdv;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
    }
    
    // Getters et Setters
    public int getIdRdv() { return idRdv; }
    public void setIdRdv(int idRdv) { this.idRdv = idRdv; }
    
    public LocalDateTime getDateRdv() { return dateRdv; }
    public void setDateRdv(LocalDateTime dateRdv) { this.dateRdv = dateRdv; }
    
    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }
    
    public int getIdMedecin() { return idMedecin; }
    public void setIdMedecin(int idMedecin) { this.idMedecin = idMedecin; }
    
    public String getNomPatient() { return nomPatient; }
    public void setNomPatient(String nomPatient) { this.nomPatient = nomPatient; }
    
    public String getNomMedecin() { return nomMedecin; }
    public void setNomMedecin(String nomMedecin) { this.nomMedecin = nomMedecin; }
    
    @Override
    public String toString() {
        return "RDV #" + idRdv + " - " + dateRdv + 
               " | Patient: " + nomPatient + " | Médecin: " + nomMedecin;
    }
}