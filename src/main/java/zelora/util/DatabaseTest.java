package zelora.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/zelora";
        String user = "root";
        String password = "";
        //This is solely just for testing whether ive connected to the correct database.
        //Its of no use now since ive correctly added database connection to the main app file.


        System.out.println("Test for database connection");

        try (Connection connection = DriverManager.getConnection(url, user, password)){
            System.out.println("Geat success: Connected to database successfully");
            System.out.println("DB: " + connection.getMetaData().getDatabaseProductName());
        }
        catch (Exception e){
            System.out.println("Failed to connect to database");
        }
    }
}
