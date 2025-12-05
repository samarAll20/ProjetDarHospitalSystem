package com.hospital.main;
import com.hospital.rmi.MedicalRecordClient;
import com.hospital.rmi.RMIClient;
import com.hospital.jms.AlertProducer;
import com.hospital.model.Patient;
import java.util.Scanner;

public class HospitalSystemMain {
    public static void main(String[] args) {
        System.out.println("==================================");
        System.out.println("   🏥 SYSTÈME HOSPITALIER V1.0");
        System.out.println("==================================");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMENU PRINCIPAL:");
            System.out.println("1. 📋 Enregistrer une urgence (RMI)");
            System.out.println("2. 📤 Envoyer une alerte (JMS)");
            System.out.println("3. 🐍 Lancer service CORBA Python");
            System.out.println("4. 📁 Gérer dossiers médicaux (RMI Avancé)"); // NOUVEAU
            System.out.println("5. 🚪 Quitter");
            System.out.print("Votre choix: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                // ... cases 1-3 existants ...

                case 4:
                    System.out.println("\n--- DOSSIERS MÉDICAUX AVANCÉS ---");
                    System.out.println("Lancement du client spécialisé...");
                    MedicalRecordClient.main(new String[]{});
                    break;

                case 5:
                    System.out.println("Au revoir! 👋");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("❌ Choix invalide!");
            }
        }
    }
}