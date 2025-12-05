package com.hospital.rmi;

import com.hospital.model.MedicalRecord;
import com.hospital.model.Patient;
import java.rmi.Naming;
import java.util.List;

public class MedicalRecordClient {
    public static void main(String[] args) {
        try {
            System.out.println("👨‍⚕️ Client des Dossiers Médicaux");
            System.out.println("===============================");

            // Connexion au service
            HospitalService service = (HospitalService)
                    Naming.lookup("rmi://localhost:1099/HospitalService");

            // 1. Créer un nouveau patient et son dossier
            System.out.println("\n1. Création d'un nouveau dossier:");
            Patient newPatient = new Patient("P999", "Jean Test", 35, "Trauma", 2);
            service.registerEmergency(newPatient);

            MedicalRecord record = service.createMedicalRecord(newPatient);
            System.out.println("✅ Dossier créé: " + record);

            // 2. Ajouter des informations au dossier
            System.out.println("\n2. Ajout d'informations médicales:");
            System.out.println(service.addHistoryToRecord("P999",
                    "Fracture bras droit", "Plâtre", "Dr. Martin"));

            System.out.println(service.addAllergyToRecord("P999",
                    "Penicillin", "Éruption cutanée"));

            // 3. Récupérer et afficher le dossier
            System.out.println("\n3. Consultation du dossier:");
            MedicalRecord retrieved = service.getMedicalRecord("P999");
            if (retrieved != null) {
                System.out.println("📋 Dossier complet:");
                System.out.println("   Patient: " + retrieved.getPatient());
                System.out.println("   Groupe sanguin: " + retrieved.getBloodType());
                System.out.println("   Historique:");
                retrieved.getHistory().forEach(h ->
                        System.out.println("     - " + h));
                System.out.println("   Allergies:");
                retrieved.getAllergies().forEach((med, react) ->
                        System.out.println("     - " + med + ": " + react));
            }

            // 4. Recherche de dossiers
            System.out.println("\n4. Recherche de dossiers:");
            List<MedicalRecord> searchResults = service.searchRecords("Curie");
            System.out.println("🔍 Résultats pour 'Curie': " + searchResults.size());
            searchResults.forEach(r ->
                    System.out.println("   - " + r.getPatient().getName()));

            // 5. Test d'interaction médicamenteuse
            System.out.println("\n5. Test d'interaction médicamenteuse:");
            System.out.println(service.addAllergyToRecord("P100", "Aspirin", "Test"));
            // Devrait afficher un avertissement car Marie Curie prend déjà Aspirin

            // 6. Tous les dossiers
            System.out.println("\n6. Tous les dossiers médicaux:");
            List<MedicalRecord> allRecords = service.getAllMedicalRecords();
            System.out.println("📚 Total: " + allRecords.size() + " dossiers");
            allRecords.forEach(r ->
                    System.out.println("   - " + r));

            System.out.println("\n✅ Tests des dossiers médicaux terminés!");

        } catch (Exception e) {
            System.err.println("❌ Erreur client: " + e);
            e.printStackTrace();
        }
    }
}