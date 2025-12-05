package com.hospital.rmi;

import com.hospital.model.Patient;
import com.hospital.model.Hospital;
import java.rmi.Naming;
import java.util.List;

public class RMIClient {
    public static void main(String[] args) {
        try {
            System.out.println("👨‍⚕️ Connexion au service RMI Hospitalier...");

            // 1. Chercher le service distant
            HospitalService service = (HospitalService)
                    Naming.lookup("rmi://localhost:1099/HospitalService");

            System.out.println("✅ Connecté au service RMI");

            // 2. Tester quelques opérations
            // Créer un patient
            Patient patient = new Patient("P001", "Jean Dupont",
                    45, "Cardiology", 2);

            // Enregistrer l'urgence
            Patient registered = service.registerEmergency(patient);
            System.out.println("✅ Patient enregistré: " + registered);

            // Trouver un hôpital approprié
            List<Hospital> hospitals = service.findHospitalForPatient(patient);
            System.out.println("🏥 Hôpitaux disponibles:");
            for (Hospital h : hospitals) {
                System.out.println("  - " + h.getStatus());
            }

            // Statut système
            System.out.println("📊 " + service.getSystemStatus());

        } catch (Exception e) {
            System.err.println("❌ Erreur client RMI: " + e);
            e.printStackTrace();
        }
    }
}