package zelora.app;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import zelora.data.DatabaseConnection;
import zelora.util.ZeloraBanner;

import java.sql.ResultSet;
import java.sql.SQLOutput;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;
import com.github.javafaker.Faker;


public class ZeloraApp {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        ZeloraBanner.printBanner();

        DatabaseConnection.doConnection();
        System.out.println("Database connection: true");

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

        //Possibly add the following
        //Optionally displaying any associated orders.

    }

    private static void dynamicProductQuery() throws Exception {
        System.out.println("\nToDo: Create Dynamic Query\n");
    }

    private static void displayAllCustomers() throws Exception {
        System.out.println("\nToDo: Display all Customers\n");

        Statement stmt = DatabaseConnection.connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM customers");

        int count = 0;

        while (rs.next()){
            count++;
            System.out.println(" ");
            System.out.println(count + "******************");
            System.out.println("First Name: " + rs.getString("first_name"));
            System.out.println("Last Name: " + rs.getString("last_name"));
            System.out.println("Address: " + rs.getString("address"));
            System.out.println("Email: " + rs.getString("email"));
            System.out.println("Phone: " + rs.getString("phone_number"));
            System.out.println("Date of Birth: " + rs.getString("date_of_birth"));
            System.out.println("------------------");
        }

        System.out.println("Total Customers: " + count);
        System.out.println();

        rs.close();
        stmt.close();

        //Plan to add like a menu right after when all the customers are finished displaying saying something on the lines of "Do you wish to sort by x,y,z" if no then it would just bring the user back to the default option menu.

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

}

