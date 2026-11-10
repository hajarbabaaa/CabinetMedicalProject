package com.cabinetmedical.service;

import com.cabinetmedical.dao.PatientDAO;
import com.cabinetmedical.model.Patient;
import java.util.List;

public class PatientService {
    private PatientDAO patientDAO;
    
    public PatientService() {
        this.patientDAO = new PatientDAO();
    }
    
    public boolean ajouterPatient(String nom, String prenom, String dateNaissance, 
                                  String adresse, String telephone) {
        try {
            Patient patient = new Patient();
            patient.setNom(nom);
            patient.setPrenom(prenom);
            patient.setDateNaissance(java.time.LocalDate.parse(dateNaissance));
            patient.setAdresse(adresse);
            patient.setTelephone(telephone);
            
            return patientDAO.ajouterPatient(patient);
        } catch (Exception e) {
            System.err.println("Erreur dans le service Patient: " + e.getMessage());
            return false;
        }
    }
    
    public List<Patient> getAllPatients() {
        return patientDAO.getAllPatients();
    }
    
    public Patient getPatientById(int id) {
        return patientDAO.getPatientById(id);
    }
    
    public boolean modifierPatient(int id, String nom, String prenom, String dateNaissance,
                                   String adresse, String telephone) {
        try {
            Patient patient = patientDAO.getPatientById(id);
            if (patient != null) {
                patient.setNom(nom);
                patient.setPrenom(prenom);
                patient.setDateNaissance(java.time.LocalDate.parse(dateNaissance));
                patient.setAdresse(adresse);
                patient.setTelephone(telephone);
                
                return patientDAO.modifierPatient(patient);
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors de la modification: " + e.getMessage());
            return false;
        }
    }
    
    public boolean supprimerPatient(int id) {
        return patientDAO.supprimerPatient(id);
    }
    
    public List<Patient> rechercherPatients(String critere) {
        return patientDAO.rechercherPatients(critere);
    }
    
    public int getNombrePatients() {
        return patientDAO.countPatients();
    }
}