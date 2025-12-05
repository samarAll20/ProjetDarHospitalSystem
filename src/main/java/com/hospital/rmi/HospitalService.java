package com.hospital.rmi;

import com.hospital.model.Patient;
import com.hospital.model.Hospital;
import com.hospital.model.MedicalRecord;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface HospitalService extends Remote {

    // Méthodes existantes
    String registerHospital(Hospital hospital) throws RemoteException;
    Patient registerEmergency(Patient patient) throws RemoteException;
    List<Hospital> findHospitalForPatient(Patient patient) throws RemoteException;
    String getSystemStatus() throws RemoteException;

    // NOUVELLES MÉTHODES POUR LES DOSSIERS MÉDICAUX
    MedicalRecord createMedicalRecord(Patient patient) throws RemoteException;
    MedicalRecord getMedicalRecord(String patientId) throws RemoteException;
    void updateMedicalRecord(MedicalRecord record) throws RemoteException;
    List<MedicalRecord> searchRecords(String keyword) throws RemoteException;
    List<MedicalRecord> getAllMedicalRecords() throws RemoteException;

    // Méthodes pour gérer l'historique médical
    String addHistoryToRecord(String patientId,
                              String diagnosis,
                              String treatment,
                              String doctor) throws RemoteException;

    String addAllergyToRecord(String patientId,
                              String medication,
                              String reaction) throws RemoteException;
}