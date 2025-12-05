package com.hospital.rmi;

import com.hospital.model.Patient;
import com.hospital.model.Hospital;
import com.hospital.model.MedicalRecord;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class HospitalServiceImpl extends UnicastRemoteObject
        implements HospitalService {

    private List<Hospital> hospitals = new ArrayList<>();
    private List<Patient> emergencies = new ArrayList<>();
    private Map<String, MedicalRecord> medicalRecords = new HashMap<>();
    private int recordCounter = 1;

    // Constructeur
    public HospitalServiceImpl() throws RemoteException {
        super();
        // Ajout de quelques hôpitaux par défaut
        hospitals.add(new Hospital("H1", "Hôpital Central", "Centre-Ville",
                50, Arrays.asList("Cardiology", "Trauma", "General", "Surgery")));
        hospitals.add(new Hospital("H2", "Hôpital Nord", "Nord",
                30, Arrays.asList("Cardiology", "Trauma", "Pediatrics", "Emergency")));

        // Ajouter quelques dossiers médicaux de démo
        initializeDemoRecords();
    }

    private void initializeDemoRecords() {
        Patient demoPatient1 = new Patient("P100", "Marie Curie", 55, "Cardiology", 2);
        MedicalRecord record1 = new MedicalRecord("REC-001", demoPatient1);
        record1.addAllergy("Penicillin", "Rash");
        record1.addMedication("Aspirin");
        record1.setBloodType("O+");
        record1.getEmergencyContact().setName("Pierre Curie");
        record1.getEmergencyContact().setPhone("123456789");
        medicalRecords.put("P100", record1);

        Patient demoPatient2 = new Patient("P101", "Albert Einstein", 76, "General", 1);
        MedicalRecord record2 = new MedicalRecord("REC-002", demoPatient2);
        record2.addAllergy("Ibuprofen", "Stomach pain");
        record2.addMedication("Warfarin");
        record2.setBloodType("A-");
        medicalRecords.put("P101", record2);

        recordCounter = 3;
    }

    // ========== MÉTHODES EXISTANTES ==========
    @Override
    public String registerHospital(Hospital hospital) throws RemoteException {
        hospitals.add(hospital);
        return "Hôpital " + hospital.getName() + " enregistré!";
    }

    @Override
    public Patient registerEmergency(Patient patient) throws RemoteException {
        emergencies.add(patient);
        System.out.println("🚨 Urgence enregistrée: " + patient);

        // Créer automatiquement un dossier médical si inexistant
        if (!medicalRecords.containsKey(patient.getId())) {
            createMedicalRecord(patient);
        }

        return patient;
    }

    @Override
    public List<Hospital> findHospitalForPatient(Patient patient) throws RemoteException {
        List<Hospital> suitableHospitals = new ArrayList<>();

        for (Hospital h : hospitals) {
            if (h.getAvailableBeds() > 0) {
                if (patient.getEmergencyType() != null) {
                    for (String specialty : h.getSpecialties()) {
                        if (specialty.equalsIgnoreCase(patient.getEmergencyType())) {
                            suitableHospitals.add(h);
                            break;
                        }
                    }
                } else {
                    suitableHospitals.add(h);
                }
            }
        }
        return suitableHospitals;
    }

    @Override
    public String getSystemStatus() throws RemoteException {
        return "Système RMI actif - Hôpitaux: " + hospitals.size() +
                " - Urgences: " + emergencies.size() +
                " - Dossiers: " + medicalRecords.size();
    }

    // ========== NOUVELLES MÉTHODES POUR DOSSIERS MÉDICAUX ==========

    @Override
    public MedicalRecord createMedicalRecord(Patient patient) throws RemoteException {
        String recordId = "REC-" + String.format("%03d", recordCounter++);
        MedicalRecord record = new MedicalRecord(recordId, patient);
        medicalRecords.put(patient.getId(), record);

        System.out.println("📁 Nouveau dossier créé: " + record);
        return record;
    }

    @Override
    public MedicalRecord getMedicalRecord(String patientId) throws RemoteException {
        MedicalRecord record = medicalRecords.get(patientId);
        if (record == null) {
            System.out.println("⚠️ Dossier non trouvé pour: " + patientId);
            return null;
        }
        System.out.println("📁 Consultation dossier: " + record);
        return record;
    }

    @Override
    public void updateMedicalRecord(MedicalRecord record) throws RemoteException {
        if (record != null && record.getPatient() != null) {
            medicalRecords.put(record.getPatient().getId(), record);
            System.out.println("📁 Dossier mis à jour: " + record);
        }
    }

    @Override
    public List<MedicalRecord> searchRecords(String keyword) throws RemoteException {
        List<MedicalRecord> results = new ArrayList<>();
        keyword = keyword.toLowerCase();

        for (MedicalRecord record : medicalRecords.values()) {
            Patient patient = record.getPatient();

            // Recherche dans le nom
            if (patient.getName().toLowerCase().contains(keyword)) {
                results.add(record);
                continue;
            }

            // Recherche dans le diagnostic
            for (com.hospital.model.MedicalHistory history : record.getHistory()) {
                if (history.getDiagnosis().toLowerCase().contains(keyword)) {
                    results.add(record);
                    break;
                }
            }

            // Recherche dans les allergies
            for (String medication : record.getAllergies().keySet()) {
                if (medication.toLowerCase().contains(keyword)) {
                    results.add(record);
                    break;
                }
            }
        }

        System.out.println("🔍 Recherche '" + keyword + "' : " + results.size() + " résultats");
        return results;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() throws RemoteException {
        return new ArrayList<>(medicalRecords.values());
    }

    @Override
    public String addHistoryToRecord(String patientId,
                                     String diagnosis,
                                     String treatment,
                                     String doctor) throws RemoteException {
        MedicalRecord record = medicalRecords.get(patientId);
        if (record == null) {
            return "❌ Dossier non trouvé pour: " + patientId;
        }

        record.addHistoryEntry(diagnosis, treatment, doctor);
        return "✅ Historique ajouté au dossier de " + record.getPatient().getName();
    }

    @Override
    public String addAllergyToRecord(String patientId,
                                     String medication,
                                     String reaction) throws RemoteException {
        MedicalRecord record = medicalRecords.get(patientId);
        if (record == null) {
            return "❌ Dossier non trouvé pour: " + patientId;
        }

        // Vérifier interaction médicamenteuse
        if (record.hasDrugInteraction(medication)) {
            return "⚠️ ATTENTION: Interaction médicamenteuse détectée avec " + medication;
        }

        record.addAllergy(medication, reaction);
        return "✅ Allergie ajoutée pour " + record.getPatient().getName();
    }
}