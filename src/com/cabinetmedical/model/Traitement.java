package com.cabinetmedical.model;

import java.math.BigDecimal;

public class Traitement {
    private int idTraitement;
    private String description;
    private BigDecimal cout;
    private int idPatient;
    private int idMedecin;
    private String nomPatient;
    private String nomMedecin;
    
    // Champs optionnels (si nécessaire)
    private String medicaments;
    private String duree;
    
    // Constructeurs
    public Traitement() {}
    
    public Traitement(String description, BigDecimal cout, int idPatient, int idMedecin) {
        this.description = description;
        this.cout = cout;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
    }
    
    public Traitement(int idTraitement, String description, BigDecimal cout, 
                      int idPatient, int idMedecin) {
        this.idTraitement = idTraitement;
        this.description = description;
        this.cout = cout;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
    }
    
    // Getters et Setters
    public int getIdTraitement() { return idTraitement; }
    public void setIdTraitement(int idTraitement) { this.idTraitement = idTraitement; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getCout() { return cout; }
    public void setCout(BigDecimal cout) { this.cout = cout; }
    
    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }
    
    public int getIdMedecin() { return idMedecin; }
    public void setIdMedecin(int idMedecin) { this.idMedecin = idMedecin; }
    
    public String getNomPatient() { return nomPatient; }
    public void setNomPatient(String nomPatient) { this.nomPatient = nomPatient; }
    
    public String getNomMedecin() { return nomMedecin; }
    public void setNomMedecin(String nomMedecin) { this.nomMedecin = nomMedecin; }
    
    // Méthodes optionnelles (si vous en avez besoin)
    public String getMedicaments() { return medicaments; }
    public void setMedicaments(String medicaments) { this.medicaments = medicaments; }
    
    public String getDuree() { return duree; }
    public void setDuree(String duree) { this.duree = duree; }
    
    @Override
    public String toString() {
        return "Traitement #" + idTraitement + ": " + description + 
               " (" + cout + " €)";
    }
}