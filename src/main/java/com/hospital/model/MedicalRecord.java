package com.hospital.model;

import java.io.Serializable;
import java.io.IOException;
import java.util.*;

public class MedicalRecord implements Serializable {
    private String recordId;
    private Patient patient;
    private List<MedicalHistory> history;
    private Map<String, String> allergies; // Médicament → Réaction
    private List<String> currentMedications;
    private EmergencyContact emergencyContact;
    private String bloodType;
    private String insuranceInfo;

    // Constructeur
    public MedicalRecord(String recordId, Patient patient) {
        this.recordId = recordId;
        this.patient = patient;
        this.history = new ArrayList<>();
        this.allergies = new HashMap<>();
        this.currentMedications = new ArrayList<>();
        this.emergencyContact = new EmergencyContact();
        this.bloodType = "Unknown";
        this.insuranceInfo = "None";

        // Ajout d'un historique par défaut pour la démo
        addDefaultHistory();
    }

    private void addDefaultHistory() {
        if (patient.getAge() > 40) {
            history.add(new MedicalHistory(
                    "Hypertension checkup",
                    "Prescribed medication",
                    "Dr. Smith"
            ));
        }
        if (patient.getEmergencyType().equals("Cardiology")) {
            history.add(new MedicalHistory(
                    "Cardiac evaluation",
                    "ECG monitoring",
                    "Dr. Johnson"
            ));
        }
    }

    // Méthodes pour gérer l'historique
    public void addHistoryEntry(String diagnosis, String treatment, String doctor) {
        MedicalHistory entry = new MedicalHistory(diagnosis, treatment, doctor);
        history.add(entry);
    }

    public void addAllergy(String medication, String reaction) {
        allergies.put(medication, reaction);
    }

    public void addMedication(String medication) {
        currentMedications.add(medication);
    }

    // Vérification d'interaction médicamenteuse (simplifiée)
    public boolean hasDrugInteraction(String newMedication) {
        // Liste de médicaments qui interagissent entre eux
        Map<String, List<String>> interactions = new HashMap<>();
        interactions.put("Warfarin", Arrays.asList("Aspirin", "Ibuprofen"));
        interactions.put("Aspirin", Arrays.asList("Warfarin", "Naproxen"));

        for (String med : currentMedications) {
            if (interactions.containsKey(med) &&
                    interactions.get(med).contains(newMedication)) {
                return true;
            }
        }
        return false;
    }

    // Getters et Setters
    public String getRecordId() { return recordId; }
    public Patient getPatient() { return patient; }
    public List<MedicalHistory> getHistory() { return history; }
    public Map<String, String> getAllergies() { return allergies; }
    public List<String> getCurrentMedications() { return currentMedications; }
    public EmergencyContact getEmergencyContact() { return emergencyContact; }
    public String getBloodType() { return bloodType; }
    public String getInsuranceInfo() { return insuranceInfo; }

    public void setEmergencyContact(EmergencyContact contact) {
        this.emergencyContact = contact;
    }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    public void setInsuranceInfo(String insuranceInfo) { this.insuranceInfo = insuranceInfo; }

    // Sérialisation personnalisée
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        System.out.println("📁 Sérialisation du dossier: " + recordId +
                " pour " + patient.getName());
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        System.out.println("📁 Désérialisation du dossier: " + recordId);
    }

    @Override
    public String toString() {
        return "Dossier " + recordId + " - " + patient.getName() +
                " (Historique: " + history.size() + " entrées, " +
                "Allergies: " + allergies.size() + ", " +
                "Médicaments: " + currentMedications.size() + ")";
    }
}