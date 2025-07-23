package src.io;

import src.entities.Drug;
import src.entities.Patient;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {

    private static final String DRUG_FILE = "resources/data.txt";

    // Read drugs from file, return empty list if file missing or error
    public static List<Drug> readDrugsFromFile() {
        List<Drug> drugs = new ArrayList<>();
        File file = new File(DRUG_FILE);
        if (!file.exists()) return drugs;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length == 4) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        double cost = Double.parseDouble(parts[2].trim());
                        String dosage = parts[3].trim();
                        drugs.add(new Drug(id, name, cost, dosage));
                    } catch (NumberFormatException ignored) { }
                }
            }
        } catch (FileNotFoundException ignored) { }
        return drugs;
    }

    // Write all drugs to file (overwrite)
    public static void saveAllDrugs(List<Drug> drugs) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DRUG_FILE, false))) {
            for (Drug d : drugs) {
                writer.printf("%d,%s,%.2f,%s%n", d.getDrugId(), d.getDrugName(), d.getDrugCost(), d.getDosage());
            }
        } catch (IOException e) {
            System.out.println("Error saving drugs: " + e.getMessage());
        }
    }

    // Append single drug to file
    public static void insertDrugToFile(Drug drug) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DRUG_FILE, true))) {
            writer.printf("%d,%s,%.2f,%s%n", drug.getDrugId(), drug.getDrugName(), drug.getDrugCost(), drug.getDosage());
        } catch (IOException e) {
            System.out.println("Error writing drug: " + e.getMessage());
        }
    }

    // Update one drug by ID
    public static void updateDrugInFile(Drug updatedDrug) {
        List<Drug> drugs = readDrugsFromFile();
        boolean found = false;
        for (int i = 0; i < drugs.size(); i++) {
            if (drugs.get(i).getDrugId() == updatedDrug.getDrugId()) {
                drugs.set(i, updatedDrug);
                found = true;
                break;
            }
        }
        if (found) saveAllDrugs(drugs);
        else System.out.println("Drug not found.");
    }

    // Delete drug by ID
    public static void deleteDrugFromFile(int id) {
        List<Drug> drugs = readDrugsFromFile();
        boolean removed = drugs.removeIf(d -> d.getDrugId() == id);
        if (removed) saveAllDrugs(drugs);
        else System.out.println("Drug not found.");
    }

    // Get next available drug ID
    public static int getNextDrugId(List<Drug> drugs) {
        int max = 0;
        for (Drug d : drugs) if (d.getDrugId() > max) max = d.getDrugId();
        return max + 1;
    }

    // Read all patients from DB
    public static List<Patient> readPatientsFromDB() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT patient_id, first_name, last_name, age, condition FROM patients";

        try (Connection conn = DatabaseHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getString("condition")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error reading patients: " + e.getMessage());
        }
        return patients;
    }

    // Overwrite all patients in DB
    public static void saveAllPatientsToDB(List<Patient> patients) {
        String deleteSQL = "DELETE FROM patients";
        String insertSQL = "INSERT INTO patients (patient_id, first_name, last_name, age, condition) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHandler.getConnection();
             Statement delStmt = conn.createStatement();
             PreparedStatement insStmt = conn.prepareStatement(insertSQL)) {

            delStmt.executeUpdate(deleteSQL);
            for (Patient p : patients) {
                insStmt.setInt(1, p.getPatientId());
                insStmt.setString(2, p.getFirstName());
                insStmt.setString(3, p.getLastName());
                insStmt.setInt(4, p.getAge());
                insStmt.setString(5, p.getCondition());
                insStmt.addBatch();
            }
            insStmt.executeBatch();

        } catch (SQLException e) {
            System.out.println("Error saving patients: " + e.getMessage());
        }
    }

    // Insert new patient to DB
    public static void insertPatientToDB(Patient p) {
        String sql = "INSERT INTO patients (patient_id, first_name, last_name, age, condition) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, p.getPatientId());
            stmt.setString(2, p.getFirstName());
            stmt.setString(3, p.getLastName());
            stmt.setInt(4, p.getAge());
            stmt.setString(5, p.getCondition());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting patient: " + e.getMessage());
        }
    }

    // Update patient in DB
    public static void updatePatientInDB(Patient p) {
        String sql = "UPDATE patients SET first_name = ?, last_name = ?, age = ?, condition = ? WHERE patient_id = ?";
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getFirstName());
            stmt.setString(2, p.getLastName());
            stmt.setInt(3, p.getAge());
            stmt.setString(4, p.getCondition());
            stmt.setInt(5, p.getPatientId());
            int updated = stmt.executeUpdate();
            if (updated == 0) System.out.println("No patient found with that ID.");

        } catch (SQLException e) {
            System.out.println("Error updating patient: " + e.getMessage());
        }
    }

    // Delete patient from DB by ID
    public static void deletePatientFromDB(int id) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            if (deleted == 0) System.out.println("No patient found with that ID.");

        } catch (SQLException e) {
            System.out.println("Error deleting patient: " + e.getMessage());
        }
    }

    // Get next available patient ID
    public static int getNextPatientId(List<Patient> patients) {
        int max = 0;
        for (Patient p : patients) if (p.getPatientId() > max) max = p.getPatientId();
        return max + 1;
    }
}
