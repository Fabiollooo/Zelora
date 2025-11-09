package zelora.app;

import zelora.data.DatabaseConnection;
import zelora.util.ZeloraBanner;

import java.util.Scanner;


public class ZeloraApp {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        ZeloraBanner.printBanner();

        DatabaseConnection.doConnection();
        System.out.println("Database connection: true");

        boolean running = true;
        while (running) {
            System.out.println("""
                    1) Register Customer
                    2) Display Customer Profile
                    3) Dynamic Product Query & Export
                    4) Display All Customers
                    5) Seed Database
                    0) Exit
                    """);
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> registerCustomer();
                    case "2" -> displayCustomerProfile();
                    case "3" -> dynamicProductQuery();
                    case "4" -> displayAllCustomers();
                    case "5" -> seedDatabase();
                    case "0" -> running = false;
                    default -> System.out.println("\nUnknown choice.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        DatabaseConnection.closeConnection(); //Closing the connection when closing program (clicks EXIT).
        System.out.println("Database connection closed.");
        System.out.println("\nGoodbye.");
    }

    private static void registerCustomer() throws Exception {
        System.out.println("\nToDo: Register a Customer\n");

        //"Cin" all information about the new customer.
        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.println("Surname: ");
        String surname = sc.nextLine();

        System.out.println("Email: ");
        String email = sc.nextLine();

        System.out.println("Password: ");
        String password = sc.nextLine();
        //Implement password encryption at some stage

        System.out.println("Address: ");
        String address = sc.nextLine();

        System.out.println("City: ");
        String city = sc.nextLine();

        System.out.println("Phone: ");
        String phone = sc.nextLine();

        System.out.println("Date of Birth: (YYYY-MM-DD)");
        String date = sc.nextLine();

        //Insert to db
        try {
            int result = DatabaseConnection.runner.update(
                    DatabaseConnection.connection,
                    "INSERT INTO customers (first_name, last_name, email, password, address, city, phone_number, date_of_birth) VALUES (?,?,?,?,?,?,?,?)",
                    name, surname, email, password, address, city, phone, date
            );

            if (result > 0) {
                System.out.println("Customer added successfully!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        //NOTE: Since we're not incrementing "customer_id" in the code i just set customer_id to auto increment in the database itself
        //SQL Query BELOW;
        //ALTER TABLE customers MODIFY customer_id INT AUTO_INCREMENT;

    }

    private static void displayCustomerProfile() throws Exception {
        System.out.println("\nToDo: Display Customer Profile\n");
    }

    private static void dynamicProductQuery() throws Exception {
        System.out.println("\nToDo: Create Dynamic Query\n");
    }

    private static void displayAllCustomers() throws Exception {
        System.out.println("\nToDo: Display all Customers\n");
    }

    private static void seedDatabase() throws Exception {
        System.out.println("\nToDo: Seed Database\n");
    }

}
