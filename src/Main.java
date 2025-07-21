package src;
import src.io.DatabaseHandler;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        Connection conn = DatabaseHandler.getConnection();
        if (conn != null) {
            System.out.println("Connection successful!");
        } else {
            System.out.println("Failed to connect.");
        }
    }
}
