package com.hospital.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            System.out.println("🏥 Démarrage du serveur RMI Hospitalier...");

            // 1. Créer le registry sur le port 1099
            LocateRegistry.createRegistry(1099);
            System.out.println("✅ Registry RMI démarré sur port 1099");

            // 2. Créer l'instance de service
            HospitalService service = new HospitalServiceImpl();

            // 3. Enregistrer le service
            Naming.rebind("rmi://localhost:1099/HospitalService", service);
            System.out.println("✅ Service Hospitalier enregistré");
            System.out.println("✅ Serveur RMI prêt! Attente des clients...");

        } catch (Exception e) {
            System.err.println("❌ Erreur serveur RMI: " + e);
            e.printStackTrace();
        }
    }
}