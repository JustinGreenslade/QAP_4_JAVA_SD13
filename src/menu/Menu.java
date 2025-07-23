package src.menu;

import src.entities.Drug;
import src.entities.Patient;
import src.io.FileHandler;

import java.util.List;
import java.util.Scanner;

public class Menu {

    // Scanner for reading user input
    private static Scanner scanner = new Scanner(System.in);

    // Lists to store drugs and patients
    private static List<Drug> drugs;
    private static List<Patient> patients;

    // Main menu method
    public static void mainMenu(List<Drug> drugList, List<Patient> patientList) {
        drugs = drugList;
        patients = patientList;

        while (true) {
            clearConsole();
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Drug Menu");
            System.out.println("2. Patient Menu");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    drugMenu();
                    break;
                case "2":
                    patientMenu();
                    break;
                case "3":
                    System.out.println("Exiting program. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            enterToContinue();
            }
        }
    }

    // Drug menu
    private static void drugMenu() {
        while (true) {
            clearConsole();
            System.out.println("\n--- DRUG MENU ---");
            System.out.println("1. Add Drug");
            System.out.println("2. Edit Drug");
            System.out.println("3. Delete Drug");
            System.out.println("4. View Drugs");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1": clearConsole();addDrug(); break;
                case "2": clearConsole();editDrug(); break;
                case "3": clearConsole();deleteDrug(); break;
                case "4": clearConsole();viewDrugs(); break;
                case "5": return;
                default:
                    System.out.println("Invalid option. Please try again.");
                            enterToContinue();
            }
        }
    }

    private static void addDrug() {
        try {
            int id = FileHandler.getNextDrugId(drugs);

            System.out.print("Enter drug name: ");
            String name = scanner.nextLine();

            System.out.print("Enter drug cost: ");
            double cost = Double.parseDouble(scanner.nextLine());

            System.out.print("Enter dosage: ");
            String dosage = scanner.nextLine();

            Drug newDrug = new Drug(id, name, cost, dosage);
            drugs.add(newDrug);
            FileHandler.insertDrugToFile(newDrug);
            System.out.println("Drug added with ID: " + id);
            enterToContinue();
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Try again.");
            enterToContinue();
        }
    }

    private static void editDrug() {
        Drug drugToEdit = null;

        System.out.print("Enter Drug ID (or press Enter to search by name): ");
        String input = scanner.nextLine().trim();

        if (!input.isEmpty()) {
            try {
                int id = Integer.parseInt(input);
                for (Drug d : drugs) {
                    if (d.getDrugId() == id) {
                        drugToEdit = d;
                        break;
                    }
                }
            // if no drug match to the ID
            if (drugToEdit == null) {
                System.out.println("No drug found with ID: " + id);
                enterToContinue();
                return;
            }
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID.");
                enterToContinue();
                return;
            }

        } else {
            System.out.print("Enter Drug Name: ");
            String name = scanner.nextLine().trim();
            for (Drug d : drugs) {
                if (d.getDrugName().equalsIgnoreCase(name)) {
                    drugToEdit = d;
                    break;
                }
            }
        }

        if (drugToEdit == null) {
            System.out.println("Drug not found.");
            enterToContinue();
            return;
        }

        System.out.println("Editing Drug: " + drugToEdit.getDrugName());

        System.out.print("Enter new name (" + drugToEdit.getDrugName() + "): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) drugToEdit.setDrugName(name);

        System.out.print("Enter new cost (" + drugToEdit.getDrugCost() + "): ");
        String costStr = scanner.nextLine();
        if (!costStr.isBlank()) drugToEdit.setDrugCost(Double.parseDouble(costStr));

        System.out.print("Enter new dosage (" + drugToEdit.getDosage() + "): ");
        String dosage = scanner.nextLine();
        if (!dosage.isBlank()) drugToEdit.setDosage(dosage);

        FileHandler.updateDrugInFile(drugToEdit);
        System.out.println("Drug updated.");
            enterToContinue();
    }

    private static void deleteDrug() {
        Drug drugToDelete = null;

        System.out.print("Enter Drug ID (or press Enter to search by name): ");
        String input = scanner.nextLine().trim();

        if (!input.isEmpty()) {
            try {
                int id = Integer.parseInt(input);
                for (Drug d : drugs) {
                    if (d.getDrugId() == id) {
                        drugToDelete = d;
                        break;
                    }
                }
            // if no drug match to the ID
            if (drugToDelete == null) {
                System.out.println("No drug found with ID: " + id);
                enterToContinue();
                return;
            }
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID.");
            enterToContinue();
                return;
            }
        } else {
            System.out.print("Enter Drug Name: ");
            String name = scanner.nextLine().trim();
            for (Drug d : drugs) {
                if (d.getDrugName().equalsIgnoreCase(name)) {
                    drugToDelete = d;
                    break;
                }
            }
        }

        if (drugToDelete == null) {
            System.out.println("Drug not found.");
            enterToContinue();
            return;
        }

        // Display selected drug details before confirmation
        System.out.printf("Selected Drug: [ ID: %d, Name: %s, Cost: %.2f, Dosage: %s ]%n",
                drugToDelete.getDrugId(),
                drugToDelete.getDrugName(),
                drugToDelete.getDrugCost(),
                drugToDelete.getDosage());

        System.out.print("Are you sure you want to delete " + drugToDelete.getDrugName() + "? (y/n): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("y")) {
            drugs.remove(drugToDelete);
            FileHandler.deleteDrugFromFile(drugToDelete.getDrugId());
            System.out.println("Drug deleted.");
            enterToContinue();
        } else {
            System.out.println("Deletion cancelled.");
            enterToContinue();
        }
    }

    private static void viewDrugs() {
        if (drugs.isEmpty()) {
            System.out.println("No drugs available.");
            enterToContinue();
        } else {
            System.out.println("Drugs List:");
            for (Drug d : drugs) {
                System.out.printf("ID: %d, Name: %s, Cost: %.2f, Dosage: %s%n",
                        d.getDrugId(), d.getDrugName(), d.getDrugCost(), d.getDosage());
            }
            enterToContinue();
        }
    }

    private static void patientMenu() {
        while (true) {
            clearConsole();
            System.out.println("\n--- PATIENT MENU ---");
            System.out.println("1. Add Patient");
            System.out.println("2. Edit Patient");
            System.out.println("3. Delete Patient");
            System.out.println("4. View Patients");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":  clearConsole();addPatient(); break;
                case "2":  clearConsole();editPatient(); break;
                case "3":  clearConsole();deletePatient(); break;
                case "4":  clearConsole();viewPatients(); break;
                case "5": return;
                default:
                    System.out.println("Invalid option. Try again.");
            enterToContinue();
            }
        }
    }

    private static void addPatient() {
        try {
            int id = FileHandler.getNextPatientId(patients);

            System.out.print("Enter first name: ");
            String first = scanner.nextLine();

            System.out.print("Enter last name: ");
            String last = scanner.nextLine();

            System.out.print("Enter age: ");
            int age = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter condition: ");
            String condition = scanner.nextLine();

            Patient newPatient = new Patient(id, first, last, age, condition);
            patients.add(newPatient);
            FileHandler.insertPatientToDB(newPatient);
        System.out.printf("Patient added: [ ID: %d, Name: %s %s, Age: %d, Condition: %s ]%n",
                newPatient.getPatientId(),
                newPatient.getFirstName(),
                newPatient.getLastName(),
                newPatient.getAge(),
                newPatient.getCondition());
            enterToContinue();
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            enterToContinue();
        }
    }

    private static void editPatient() {
        Patient patientToEdit = null;

        System.out.print("Enter Patient ID (or press Enter to search by name): ");
        String input = scanner.nextLine().trim();

        if (!input.isEmpty()) {
            try {
                int id = Integer.parseInt(input);
                for (Patient p : patients) {
                    if (p.getPatientId() == id) {
                        patientToEdit = p;
                        break;
                    }
                }
                            // if no patient match to the ID
            if (patientToEdit == null) {
                System.out.println("No patient found with ID: " + id);
                enterToContinue();
                return;
            }
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID.");
            enterToContinue();
                return;
            }
        } else {
            System.out.print("Enter First Name: ");
            String first = scanner.nextLine().trim();
            System.out.print("Enter Last Name: ");
            String last = scanner.nextLine().trim();
            for (Patient p : patients) {
                if (p.getFirstName().equalsIgnoreCase(first) && p.getLastName().equalsIgnoreCase(last)) {
                    patientToEdit = p;
                    break;
                }
            }
        }

        if (patientToEdit == null) {
            System.out.println("Patient not found.");
            enterToContinue();
            return;
        }

        System.out.print("Enter new first name (" + patientToEdit.getFirstName() + "): ");
        String first = scanner.nextLine();
        if (!first.isBlank()) patientToEdit.setFirstName(first);

        System.out.print("Enter new last name (" + patientToEdit.getLastName() + "): ");
        String last = scanner.nextLine();
        if (!last.isBlank()) patientToEdit.setLastName(last);

        System.out.print("Enter new age (" + patientToEdit.getAge() + "): ");
        String ageStr = scanner.nextLine();
        if (!ageStr.isBlank()) patientToEdit.setAge(Integer.parseInt(ageStr));

        System.out.print("Enter new condition (" + patientToEdit.getCondition() + "): ");
        String condition = scanner.nextLine();
        if (!condition.isBlank()) patientToEdit.setCondition(condition);

        FileHandler.updatePatientInDB(patientToEdit);
        System.out.println("Patient updated.");
            enterToContinue();
    }

    private static void deletePatient() {
        Patient patientToDelete = null;

        System.out.print("Enter Patient ID (or press Enter to search by name): ");
        String input = scanner.nextLine().trim();

        if (!input.isEmpty()) {
            try {
                int id = Integer.parseInt(input);
                for (Patient p : patients) {
                    if (p.getPatientId() == id) {
                        patientToDelete = p;
                        break;
                    }
                }
                // if no patient match to the ID
            if (patientToDelete == null) {
                System.out.println("No patient found with ID: " + id);
                enterToContinue();
                return;
            }
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID.");
            enterToContinue();
                return;
            }
        } else {
            System.out.print("Enter First Name: ");
            String first = scanner.nextLine().trim();
            System.out.print("Enter Last Name: ");
            String last = scanner.nextLine().trim();
            for (Patient p : patients) {
                if (p.getFirstName().equalsIgnoreCase(first) && p.getLastName().equalsIgnoreCase(last)) {
                    patientToDelete = p;
                    break;
                }
            }
        }

        if (patientToDelete == null) {
            System.out.println("Patient not found.");
            enterToContinue();
            return;
        }

        // Display selected patient details before confirmation
        System.out.printf("Selected Patient: [ ID: %d, Name: %s %s, Age: %d, Condition: %s ]%n",
                patientToDelete.getPatientId(),
                patientToDelete.getFirstName(),
                patientToDelete.getLastName(),
                patientToDelete.getAge(),
                patientToDelete.getCondition());

        System.out.print("Are you sure you want to delete " + patientToDelete.getFirstName() + " " + patientToDelete.getLastName() + "? (y/n): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("y")) {
            patients.remove(patientToDelete);
            FileHandler.deletePatientFromDB(patientToDelete.getPatientId());
            System.out.println("Patient deleted.");
            enterToContinue();
        } else {
            System.out.println("Deletion cancelled.");
            enterToContinue();
        }
    }

    private static void viewPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients available.");
            enterToContinue();
        } else {
            System.out.println("Patients List:");
            for (Patient p : patients) {
                System.out.printf("ID: %d, Name: %s %s, Age: %d, Condition: %s%n",
                        p.getPatientId(), p.getFirstName(), p.getLastName(), p.getAge(), p.getCondition());
            }
            enterToContinue();
        }
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    public static void enterToContinue(){
                System.out.print("Press Enter to continue...");
                scanner.nextLine();
    }

}
