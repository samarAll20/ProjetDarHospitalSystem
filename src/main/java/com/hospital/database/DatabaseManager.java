package com.hospital.database;

import com.hospital.model.Patient;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    // CHEMIN CORRIGÉ : ./data/hospital_db
    private static final String URL = "jdbc:h2:./data/hospital_db";
    private static final String USER = "admin";
    private static final String PASS = "admin123";

    static {
        try {
            System.out.println("🗄️ Initialisation base H2...");
            Class.forName("org.h2.Driver");
            initializeDatabase();
        } catch (Exception e) {
            System.err.println("❌ Erreur initialisation H2: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initializeDatabase() throws SQLException {
        // 1. Créer le dossier data s'il n'existe pas
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdir();
            if (created) {
                System.out.println("📁 Dossier 'data' créé avec succès");
            } else {
                System.err.println("❌ Impossible de créer le dossier 'data'");
                // Essayer dans le répertoire utilisateur
                String userHome = System.getProperty("user.home");
                System.err.println("   Essayez: " + userHome + "/hospital_data");
            }
        }

        // 2. Se connecter et créer les tables
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            System.out.println("✅ Connexion H2 établie: " + conn.getMetaData().getURL());

            // Création des tables
            stmt.execute("CREATE TABLE IF NOT EXISTS patients (" +
                    "id VARCHAR(20) PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "age INT, " +
                    "emergency_type VARCHAR(50), " +
                    "severity INT, " +
                    "arrival_time TIMESTAMP)");

            stmt.execute("CREATE TABLE IF NOT EXISTS alerts (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "message VARCHAR(500), " +
                    "timestamp TIMESTAMP, " +
                    "priority INT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS hospitals (" +
                    "id VARCHAR(20) PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "location VARCHAR(100), " +
                    "available_beds INT, " +
                    "specialties VARCHAR(500))");

            stmt.execute("CREATE TABLE IF NOT EXISTS medical_history (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "patient_id VARCHAR(20), " +
                    "diagnosis VARCHAR(200), " +
                    "treatment VARCHAR(200), " +
                    "doctor_name VARCHAR(100), " +
                    "record_date TIMESTAMP)");

            System.out.println("✅ Tables créées avec succès");

            // Insérer des données de test si les tables sont vides
            insertSampleData(conn);

        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL: " + e.getMessage());
            System.err.println("   Code erreur: " + e.getErrorCode());
            throw e;
        }
    }

    private static void insertSampleData(Connection conn) throws SQLException {
        // Vérifier si la table hospitals est vide
        ResultSet rs = conn.createStatement().executeQuery(
                "SELECT COUNT(*) FROM hospitals");
        rs.next();
        if (rs.getInt(1) == 0) {
            // Insérer des hôpitaux de test
            String[] hospitals = {
                    "INSERT INTO hospitals VALUES ('H1', 'Hôpital Central', 'Centre-Ville', 50, 'Cardiology,Trauma,Surgery')",
                    "INSERT INTO hospitals VALUES ('H2', 'Hôpital Nord', 'Nord', 30, 'Cardiology,Pediatrics,Emergency')"
            };

            for (String sql : hospitals) {
                conn.createStatement().execute(sql);
            }
            System.out.println("🏥 Données de test insérées: 2 hôpitaux");
        }
    }

    public static void savePatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getId());
            pstmt.setString(2, patient.getName());
            pstmt.setInt(3, patient.getAge());
            pstmt.setString(4, patient.getEmergencyType());
            pstmt.setInt(5, patient.getSeverityLevel());
            pstmt.setTimestamp(6, new Timestamp(patient.getArrivalTime().getTime()));

            pstmt.executeUpdate();
            System.out.println("💾 Patient sauvegardé dans H2: " + patient.getName());

        } catch (SQLException e) {
            System.err.println("❌ Erreur sauvegarde patient: " + e.getMessage());
            throw e;
        }
    }

    public static List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY arrival_time DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient p = new Patient(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("emergency_type"),
                        rs.getInt("severity")
                );
                patients.add(p);
            }
        }
        return patients;
    }

    // Méthode utilitaire pour tester la connexion
    public static void testConnection() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("✅ Connexion H2 réussie!");
            System.out.println("   URL: " + conn.getMetaData().getURL());
            System.out.println("   Driver: " + conn.getMetaData().getDriverName());
            System.out.println("   Version: " + conn.getMetaData().getDriverVersion());

            // Vérifier les tables
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"});
            System.out.println("   Tables disponibles:");
            while (tables.next()) {
                System.out.println("     - " + tables.getString("TABLE_NAME"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur connexion H2: " + e.getMessage());
        }
    }
}