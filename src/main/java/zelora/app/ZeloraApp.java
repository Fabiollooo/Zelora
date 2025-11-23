package zelora.app;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import zelora.data.DatabaseConnection;
import zelora.model.Product;
import zelora.util.ZeloraBanner;

import java.io.FileWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLOutput;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;
import com.github.javafaker.Faker;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ZeloraApp {
    private static final Scanner sc = new Scanner(System.in);
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";

    private static ExecutorService executor = Executors.newCachedThreadPool();
    private static int previousCustomerCount = 0;

    public static void main(String[] args) throws Exception {
        ZeloraBanner.printBanner();

        DatabaseConnection.doConnection();
        System.out.println("Database connection: true");

        startCustomerCounter();
        startNewCustomerTracker();
        startLowStockAlert();

        boolean running = true;
        while (running) {
            System.out.println(" ");
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

        shutdownExecutor();
        DatabaseConnection.closeConnection(); //Closing the Connection when closing program (clicks EXIT).
        System.out.println("Database connection closed.");
        System.out.println("\nGoodbye.");
    }

    private static void registerCustomer() throws Exception {
        System.out.println("\nToDo: Register a Customer\n");

        //"Cin" all information about the new customer.
            System.out.println("Please enter the following information");

        //Input for name
            String name;
                while(true) {
                    System.out.print("First name: ");
                    name = sc.nextLine();

                    if(!name.isEmpty()){
                        break;
                    }
                    System.out.println("You didn't type in anything, please try again.");

                }


        //Input for Surname
            String surname;
                while(true) {
                    System.out.println("Surname: ");
                    surname = sc.nextLine();

                    if(!surname.isEmpty()){
                        break;
                    }
                    System.out.println("You didn't type in anything, please try again.");

                }


        //Input for email
            String email;
                while(true) {
                    System.out.println("Email: ");
                    email = sc.nextLine();

                    while (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
                        System.out.println("Invalid email. Try again:");
                        email = sc.nextLine();
                    }

                    String checkEmailsql = "SELECT email FROM customers WHERE email = ? ";
                    List<String> emails = DatabaseConnection.runner.query(DatabaseConnection.connection, checkEmailsql, new ColumnListHandler<String>(), email);

                    if(!email.isEmpty()){
                        break;
                    }
                    System.out.println("You didn't type in anything, please try again.");

                }

        //Input for password
            String password;
                while(true) {
                    System.out.println("Password: ");
                    password = sc.nextLine();

                    if (password.length() < 5){
                        System.out.println("Password must be less than 5 characters.");
                        continue;
                    }

                    break;
                }
                String hashedpassword = BCrypt.hashpw(password, BCrypt.gensalt());


        //Input for address
            String address;
                while(true) {
                    System.out.println("Address: ");
                    address = sc.nextLine();

                    if (!address.isEmpty()) {
                        break;
                    }
                    System.out.println("You didn't type in anything, please try again.");

                }

        //Input for city
            String city;
                while(true) {
                    System.out.println("City: ");
                    city = sc.nextLine();

                    if(!city.isEmpty()){
                       break;
                    }
                    System.out.println("You didn't type in anything, please try again.");
                }


        //Input for phone
            String phone;
                while(true){
                    System.out.println("Phone: ");
                    phone = sc.nextLine();

                    if(phone.isEmpty()){
                        System.out.println("Please enter a valid phone number.");
                    }

                    boolean valid = true;

                    for (int i = 0; i < phone.length(); i++) {
                        if (!Character.isDigit(phone.charAt(i))) {
                            valid = false;
                            break;
                        }
                    }

                    if (valid && phone.length() >= 7) {
                        break;
                    } else if (!valid) {
                        System.out.println("Invalid phone number! Only digits are allowed. Try again.");
                    } else {
                        System.out.println("Phone number too short! Must be at least 7 digits. Try again.");
                    }

                }

        //Input for DOB
            String date;
            do {
                System.out.println("Date of Birth (YYYY-MM-DD):");
                date = sc.nextLine();
            } while (!date.contains("-"));



        //Insertion to db
            try {
                int result = DatabaseConnection.runner.update(
                        DatabaseConnection.connection,
                        "INSERT INTO customers (first_name, last_name, email, password, address, city, phone_number, date_of_birth) VALUES (?,?,?,?,?,?,?,?)",
                        name, surname, email, hashedpassword, address, city, phone, date
                );

                if (result > 0) {
                    System.out.println("Customer added successfully!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        //NOTE: Since we're not incrementing "customer_id" in the code I just set customer_id to auto increment in the database itself
        //SQL Query BELOW;
        //ALTER TABLE customers MODIFY customer_id INT AUTO_INCREMENT;

    }

    private static void displayCustomerProfile() throws Exception {
        System.out.println("\nToDo: Display Customer Profile\n");


        System.out.println("Please enter your customer ID: ");
        int customerID = sc.nextInt();
        sc.nextLine();

        String fetchUserData = "SELECT first_name, last_name, email, phone_number, date_of_birth, city, communication_preferences, date_joined FROM customers WHERE customer_id = ?";
        var stmt = DatabaseConnection.connection.prepareStatement(fetchUserData);
        stmt.setInt(1, customerID);
        var result = stmt.executeQuery();

        String reviewSQL = "SELECT * FROM reviews WHERE customer_id = ?";
        var reviewStmt = DatabaseConnection.connection.prepareStatement(reviewSQL);
        reviewStmt.setInt(1, customerID);
        var reviewResult = reviewStmt.executeQuery();

        //Check if the user was found or not
        if (result.next()) {
            System.out.println("Name: " + result.getString("first_name"));
            System.out.println("Last Name: " + result.getString("last_name"));
            System.out.println("Email: " + result.getString("email"));
            System.out.println("Phone: " + result.getString("phone_number"));
            System.out.println(" ");
            System.out.println("Date of Birth: " + result.getString("date_of_birth") + "  (yyyy-mm-dd)");
            System.out.println("City: " + result.getString("city"));
            System.out.println("Communication preferance: " + result.getString("communication_preferences"));
            System.out.println("Date joined: " + result.getString("date_joined"));

            System.out.println("");
            System.out.println("*************************");

            System.out.println("--- Customer Reviews ---");
            if (reviewResult.next()) {
                do {
                    System.out.println("Product ID: " + reviewResult.getString("product_id"));
                    System.out.println("Review: " + reviewResult.getString("review_text"));
                    System.out.println("Rating: " + reviewResult.getInt("rating") + "/5");
                    System.out.println("Date: " + reviewResult.getString("review_date"));
                    System.out.println("-------------------");
                } while (reviewResult.next());
            } else {
                System.out.println("No reviews found for this customer.");
            }


        } else {
            System.out.println("Customer not found!");
        }

        result.close();
        stmt.close();

    }

    private static void dynamicProductQuery() throws Exception {
        System.out.println("\nToDo: Create Dynamic Query\n");

        System.out.println("Available columns: product_id, product_name, description, category_id, price, size, colour, material, sustainability_rating, manufacturer, release_date, discounted_price, supplier_Id");
        System.out.println("Enter columns that you want to display (seperate each by comma please)");
        String columnsInput =  sc.nextLine().trim();

        String columns = columnsInput.isEmpty() ? "product_id, product_name, price" : columnsInput;

        String sql = "SELECT " + columns + " FROM products";

        var stmt = DatabaseConnection.connection.createStatement();
        var rs = stmt.executeQuery(sql);

        System.out.println("\nSearch Results:");
        while (rs.next()) {
            String[] selectedColumns = columns.split(",");
            for (String columnName : selectedColumns) {
                String cleanColumnName = columnName.trim();
                System.out.print(cleanColumnName + ": " + rs.getString(cleanColumnName) + " | ");
            }
            System.out.println();
        }

        System.out.print("\nExport to CSV? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {

            try (FileWriter csvWriter = new FileWriter("products.csv")) {
                csvWriter.write(columns + "\n");
                var resultSetForExport = stmt.executeQuery(sql);
                while (resultSetForExport.next()) {
                    String[] exportColumns = columns.split(",");

                    for (int columnIndex = 0; columnIndex < exportColumns.length; columnIndex++) {
                        String currentColumn = exportColumns[columnIndex].trim();
                        csvWriter.write(resultSetForExport.getString(currentColumn));
                        if (columnIndex < exportColumns.length - 1) csvWriter.write(",");
                    }
                    csvWriter.write("\n");
                }
            }
            System.out.println("Exported to products.csv");
        }

        stmt.close();

    }

    private static void displayAllCustomers() throws Exception {
        System.out.println("\nToDo: Display all Customers\n");

        System.out.println("Do you wish to Sort by:");
        System.out.println("1) Default order");
        System.out.println("2) Alphabetically (by first name)");
        System.out.println("3) VIP Status");
        System.out.print("Choose: ");
        String sortChoice = sc.nextLine();

        String sql = "SELECT * FROM customers";
        switch (sortChoice) {
            case "2":
                sql += " ORDER BY first_name ASC";
                break;
            case "3":
                sql += " ORDER BY vip_status ASC";
                break;

        }

        Statement stmt = DatabaseConnection.connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        int count = 0;

        while (rs.next()){
            count++;
            System.out.println(" ");
            System.out.println(count + "******************");
            System.out.println("First Name: " + rs.getString("first_name"));
            System.out.println("Last Name: " + rs.getString("last_name"));
            System.out.println("Customer ID: " + rs.getString("customer_id"));
            System.out.println("Address: " + rs.getString("address"));
            System.out.println("Email: " + rs.getString("email"));
            System.out.println("Phone: " + rs.getString("phone_number"));
            System.out.println("VIP status: " + rs.getString("vip_status"));
            System.out.println("Date of Birth: " + rs.getString("date_of_birth"));
            System.out.println("------------------");
        }

        System.out.println("Total Customers: " + count);
        System.out.println();

        rs.close();
        stmt.close();



    }

    private static void seedDatabase() throws Exception {
        System.out.println("\nToDo: Seed Database\n");

        Faker faker = new Faker();
        System.out.println("How many Users would you like to add to the database ?");
        int noOfNewCustomers = sc.nextInt();
        sc.nextLine();

        int newCustomercount = 1;
        for (int i = 0; i < noOfNewCustomers; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String password = faker.internet().password();
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            String address = faker.address().streetAddress();
            String city = faker.address().city();
            String phone = faker.numerify("### #### ###");

            String year = String.valueOf(faker.number().numberBetween(1950, 2005));
            String month = String.format("%02d", faker.number().numberBetween(1, 12));
            String day = String.format("%02d", faker.number().numberBetween(1, 28));
            String dob = year + "-" + month + "-" + day;

            String[] commPref = {"SMS", "Email", "Facebook", "Whatsapp", "Snapchat", "Instagram"};
            String communicationPref = commPref[faker.number().numberBetween(0, 5)];

            String joinYear = String.valueOf(faker.number().numberBetween(1999, 2024));
            String joinMonth = String.format("%02d", faker.number().numberBetween(1, 12));
            String joinDay = String.format("%02d", faker.number().numberBetween(1, 28));
            String dateJoined = joinYear + "-" + joinMonth + "-" + joinDay;

            String[] vipLevels = {"Bronze", "Silver", "Gold", "Platinum"};
            String vipStatus = vipLevels[faker.number().numberBetween(0, 3)];

            String[] paymentMethods = {"PayPal", "Credit Card", "Stripe", "Debit Card"};
            String paymentInfo = paymentMethods[faker.number().numberBetween(0, 3)];

            System.out.println("");
                System.out.println(newCustomercount + "--------------------");
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Password: " + password);
                System.out.println("Address: " + address);
                System.out.println("City: " + city);
                System.out.println("Phone: " + phone);
                System.out.println("Date of Birth: " + dob);
                System.out.println("Communication Pref: " + communicationPref);
                System.out.println("Date Joined: " + dateJoined);
                System.out.println("VIP Status: " + vipStatus);
                System.out.println("Payment Info: " + paymentInfo);

                newCustomercount++;
                System.out.println("");
            System.out.println("***********************");

            DatabaseConnection.runner.update(
                    DatabaseConnection.connection,
                    "INSERT INTO customers (first_name, last_name, email, password, address, city, phone_number, date_of_birth, communication_preferences, date_joined, vip_status, payment_info) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
                    firstName, lastName, email, hashedPassword, address, city, phone, dob, communicationPref, dateJoined, vipStatus, paymentInfo
            );
        }
        System.out.println("Database seeded with " + noOfNewCustomers + " sample customers!");

    }

    private static void startCustomerCounter() {
        executor.submit(() -> {
            while (true) {
                try {
                    Thread.sleep(60000);
                    String sql = "SELECT COUNT(*) FROM customers";
                    Long currentCount = DatabaseConnection.runner.query(DatabaseConnection.connection, sql, new ScalarHandler<Long>());

                    System.out.println(YELLOW + "\n[Background Task 1] Total customers: " + currentCount + " - " + new java.util.Date() + RESET);


                    previousCustomerCount = currentCount.intValue();

                } catch (Exception e) {
                    System.out.println("Customer counter error: " + e.getMessage());
                }
            }
        });
    }

    private static void startNewCustomerTracker() {
        executor.submit(() -> {
            while (true) {
                try {
                    Thread.sleep(120000);
                    String sql = "SELECT COUNT(*) FROM customers";
                    Long currentCount = DatabaseConnection.runner.query(DatabaseConnection.connection, sql, new ScalarHandler<Long>());


                    int newCustomers = 0;
                    if (previousCustomerCount > 0) {
                        newCustomers = currentCount.intValue() - previousCustomerCount;
                    }

                    if (newCustomers > 0) {
                        System.out.println("\n\n**********Very Important and classified information**********");
                        System.out.println(YELLOW + "[Background Task 2] New customers joined: +" + newCustomers + " - " + new java.util.Date() + RESET);
                    }

                } catch (Exception e) {
                    System.out.println("New customer tracker error: " + e.getMessage());
                }
            }
        });
    }

    private static void startLowStockAlert() {
        executor.submit(() -> {
            while (true) {
                try {
                    Thread.sleep(120000);

                    var stmt = DatabaseConnection.connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT inventory_id, product_id ,quantity_in_stock FROM inventory WHERE quantity_in_stock < 10");

                    boolean foundLowStock = false;
                    while (rs.next()) {
                        if (!foundLowStock) {
                            System.out.println(YELLOW + "\n[Background Task 3] LOW STOCK ALERT!" + RESET);
                            foundLowStock = true;
                        }
                        String product = rs.getString("product_id");
                        int stock = rs.getInt("quantity_in_stock");
                        System.out.println("   - " + "Product ID" + product + ": Only " + stock + " left!");
                    }

                    if (!foundLowStock) {
                        System.out.println("\n[Background Task 3] All products are well stocked!");
                    }

                    rs.close();
                    stmt.close();

                } catch (Exception e) {
                    System.out.println("Stock alert error: " + e.getMessage());
                }
            }
        });
    }

    private static void shutdownExecutor() {
        try {
            System.out.println("Attempting to shutdown background tasks...");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Tasks interrupted: " + e);
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("Cancelling unfinished background tasks");
            }
            executor.shutdownNow();
            System.out.println("Background tasks shutdown complete");
        }
    }
}

