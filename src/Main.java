package src;

// Import the classes we need
import src.entities.Drug;
import src.entities.Patient;
import src.io.FileHandler;
import src.io.DatabaseHandler;
import src.menu.Menu;

import java.util.List;

public class Main {
    public static void main(String[] args) {
System.out.println();
        // Makes sure database tables are created if they dont exist
        DatabaseHandler.createTablesIfNotExist();

        Menu.clearConsole();

    
        // Load drugs from the file (drugs.txt)
        List<Drug> drugs = FileHandler.readDrugsFromFile();
        System.out.println("Loaded " + drugs.size() + " drugs from txt file.");

        // Load patients from the database (PostgreSQL)
        List<Patient> patients = FileHandler.readPatientsFromDB();
        System.out.println("Loaded " + patients.size() + " patients from database.");
        Menu.enterToContinue();
        // Shows the main menu
        Menu.mainMenu(drugs, patients);

        // Save drugs back to the file
        System.out.println("Saving drugs to file...");
        FileHandler.saveAllDrugs(drugs);

        // Save patients back to the database
        System.out.println("Saving patients to database...");
        FileHandler.saveAllPatientsToDB(patients);

        // Final message to show that everything was saved and the program is closing
        System.out.println("All data has been saved. Program is closing. Goodbye!");
    }
}
