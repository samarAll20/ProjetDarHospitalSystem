package com.hospital.model;

import java.io.Serializable;
import java.util.Date;

// TRÈS IMPORTANT : Serializable pour RMI
public class Patient implements Serializable {
    private String id;
    private String name;
    private int age;
    private String emergencyType; // "Cardiac", "Trauma", etc.
    private int severityLevel; // 1-3 (1=critique)
    private Date arrivalTime;

    // Constructeur
    public Patient(String id, String name, int age,
                   String emergencyType, int severityLevel) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.emergencyType = emergencyType;
        this.severityLevel = severityLevel;
        this.arrivalTime = new Date();
    }

    // Getters et Setters (simples)
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmergencyType() { return emergencyType; }
    public int getSeverityLevel() { return severityLevel; }
    public Date getArrivalTime() { return arrivalTime; }

    public void setSeverityLevel(int level) {
        this.severityLevel = level;
    }

    @Override
    public String toString() {
        return name + " (" + age + " ans) - " +
                emergencyType + " - Niveau " + severityLevel;
    }
}