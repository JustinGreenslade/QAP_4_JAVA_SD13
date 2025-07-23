package src.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

    private static final String URL = "jdbc:postgresql://localhost:5432/java";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Keyin2024";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // run my create_tables.sql file
    public static void createTablesIfNotExist() {
        String sqlFilePath = "sql/create_tables.sql";


        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(sqlFilePath))) {

            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sqlBuilder.append(line);
                sqlBuilder.append("\n");
            }

            // Split SQL statements by semicolon to run individually
            String[] sqlStatements = sqlBuilder.toString().split(";");

            for (String sql : sqlStatements) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }

            System.out.println("Database tables created/verified successfully.");

        } catch (SQLException e) {
            System.err.println("SQL error running create_tables.sql: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading SQL file: " + e.getMessage());
        }
    }
}

