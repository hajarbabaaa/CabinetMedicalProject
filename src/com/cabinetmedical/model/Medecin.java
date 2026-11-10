package com.cabinetmedical.model;

public class Medecin {
    private int idMedecin;
    private String nom;
    private String specialite;
    private String telephone;
    
    // Constructeurs
    public Medecin() {}
    
    public Medecin(String nom, String specialite, String telephone) {
        this.nom = nom;
        this.specialite = specialite;
        this.telephone = telephone;
    }
    
    public Medecin(int idMedecin, String nom, String specialite, String telephone) {
        this.idMedecin = idMedecin;
        this.nom = nom;
        this.specialite = specialite;
        this.telephone = telephone;
    }
    
    // Getters et Setters
    public int getIdMedecin() { return idMedecin; }
    public void setIdMedecin(int idMedecin) { this.idMedecin = idMedecin; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    @Override
    public String toString() {
        return "Dr. " + nom + " (" + specialite + ") - Tel: " + telephone;
    }
}