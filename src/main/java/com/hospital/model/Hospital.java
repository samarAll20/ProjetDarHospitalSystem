package com.hospital.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hospital implements Serializable {
    private String id;
    private String name;
    private String location;
    private int availableBeds;
    private List<String> specialties; // ["Cardiology", "Trauma"]
    private List<Patient> currentPatients;

    public Hospital(String id, String name, String location,
                    int beds, List<String> specialties) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.availableBeds = beds;
        this.specialties = specialties;
        this.currentPatients = new ArrayList<>();
    }

    // Méthode simple pour admettre un patient
    public boolean admitPatient(Patient patient) {
        if (availableBeds > 0) {
            currentPatients.add(patient);
            availableBeds--;
            return true;
        }
        return false; // Plus de lits
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAvailableBeds() { return availableBeds; }
    public List<String> getSpecialties() { return specialties; }

    public String getStatus() {
        return name + " - Lits disponibles: " + availableBeds +
                " - Patients: " + currentPatients.size();
    }
}