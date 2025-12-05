package com.hospital.main;

import com.hospital.database.DatabaseManager;
import com.hospital.model.Patient;

public class TestH2Database {
    public static void main(String[] args) {
        System.out.println("🧪 TEST BASE DE DONNÉES H2");
        System.out.println("===========================");

        // 1. Tester la connexion
        System.out.println("\n1. Test de connexion:");
        DatabaseManager.testConnection();

        // 2. Tester l'ajout d'un patient
        System.out.println("\n2. Test d'ajout patient:");
        try {
            Patient testPatient = new Patient(
                    "TEST001",
                    "Patient Test",
                    30,
                    "Cardiology",
                    2
            );

            DatabaseManager.savePatient(testPatient);
            System.out.println("✅ Patient ajouté avec succès!");

        } catch (Exception e) {
            System.err.println("❌ Erreur ajout patient: " + e.getMessage());
            e.printStackTrace();
        }

        // 3. Vérifier le fichier créé
        System.out.println("\n3. Vérification fichier:");
        java.io.File dataDir = new java.io.File("data");
        System.out.println("Dossier 'data' existe: " + dataDir.exists());

        if (dataDir.exists()) {
            String[] files = dataDir.list();
            System.out.println("Fichiers dans 'data':");
            if (files != null) {
                for (String file : files) {
                    java.io.File f = new java.io.File("data/" + file);
                    System.out.println("  - " + file + " (" + f.length() + " octets)");
                }
            }
        }

        System.out.println("\n✅ Test H2 terminé!");
    }
}