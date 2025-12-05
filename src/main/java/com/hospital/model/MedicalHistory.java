package com.hospital.model;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class MedicalHistory implements Serializable {
    private Date date;
    private String diagnosis;
    private String treatment;
    private String doctorName;
    private List<String> attachedFiles; // Noms des fichiers scannés

    public MedicalHistory() {
        this.date = new Date();
        this.attachedFiles = new ArrayList<>();
    }

    public MedicalHistory(String diagnosis, String treatment, String doctorName) {
        this();
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.doctorName = doctorName;
    }

    // Getters et Setters
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public List<String> getAttachedFiles() { return attachedFiles; }
    public void addAttachedFile(String fileName) {
        this.attachedFiles.add(fileName);
    }

    @Override
    public String toString() {
        return "[" + date + "] " + diagnosis + " - Traité par: " + doctorName;
    }
}