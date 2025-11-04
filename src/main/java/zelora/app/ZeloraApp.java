package zelora.app;

import zelora.util.ZeloraBanner;

import java.util.Scanner;


public class ZeloraApp {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        ZeloraBanner.printBanner();

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
        System.out.println("\nGoodbye.");
    }

    private static void registerCustomer() throws Exception {
        System.out.println("\nToDo: Register a Customer\n");
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
