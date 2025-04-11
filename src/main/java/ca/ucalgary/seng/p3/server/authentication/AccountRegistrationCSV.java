package ca.ucalgary.seng.p3.server.authentication;

import java.io.*;
import java.util.*;

public class AccountRegistrationCSV {
    // Name of the CSV file used as the account database
    private static final String FILE_NAME = "src/main/resources/database/accounts.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome!");

        // Loop to allow multiple account creations

            System.out.println("Do you want to create a new account? (yes/no)");
            String response = scanner.nextLine();

            if (response.equalsIgnoreCase("yes")) {
                try {
                    // Attempt to create a new account
                    createNewAccount(scanner);
                } catch (IOException e) {
                    System.out.println("Error accessing the database: " + e.getMessage());
                }
            } else {
                System.out.println("Exiting. Goodbye!");
                ; // Exit the loop if the user doesn't want to register
            }


        scanner.close(); // Close the scanner resource
    }

    /**
     * Handles the process of creating a new account, including user input and validation.
     */
    public static void createNewAccount(Scanner scanner) throws IOException {
        // Load existing accounts from the CSV file into a map
        Map<String, String[]> accounts = loadAccounts();

            System.out.print("Enter Username: ");
            String username = scanner.nextLine();


            System.out.println("Password Requirements:");
            System.out.println("- 8+ characters");
            System.out.println("- 1 number, 1 capital letter");
            System.out.println("- No spaces");
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            System.out.print("Verify Password: ");
            String verifyPassword = scanner.nextLine();

            System.out.print("Enter Email that includes @: ");
            String email = scanner.nextLine();

            // Save the new account to the CSV file
        if( !accounts.containsKey(username) && isValidEmail(email) && isProperPassword(password) && password.equals(verifyPassword) ) {
            saveAccount(username, password, email);
            System.out.println("Account created successfully! Please login.");
        } else if (accounts.containsKey(username) && username != null) {
            System.out.println("Error: Username already exists. Try again.");
        } else if (!isValidEmail(email) ) {
            System.out.println("Error: Invalid Email Format. Try again.");
        } else if (!password.equals(verifyPassword)) {
            System.out.println("Error: Passwords Don't Match. Try again.");
        } else if(!isProperPassword(password)){
            System.out.println("Error: Please Input Password that matches requirements");
        }
        }
        // Exit the loop after successful account creation

    public static boolean isProperPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[A-Z].*")
                && !password.contains(" ") && password != null;
    }

    /**
     * Reads the accounts from the CSV file and stores them in a map.
     * @return A map of usernames to password/email arrays.
     */
    public static Map<String, String[]> loadAccounts() throws IOException {
        Map<String, String[]> accounts = new HashMap<>();
        File file = new File(FILE_NAME);

        // If the file doesn't exist, create an empty one
        if (!file.exists()) {
            file.createNewFile();
            return accounts;
        }

        // Read each line from the file and split by commas
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    // Map username to password and email
                    accounts.put(parts[0], new String[]{parts[1], parts[2]});
                }
            }
        }

        return accounts;
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        String[] parts = email.split("@");
        return parts.length == 2 &&
                !parts[0].isEmpty() &&
                !parts[1].isEmpty();
    }


    /**
     * Appends a new account to the CSV file.
     * @param username The user's username
     * @param password The user's password (not encrypted here)
     * @param email The user's email address
     */
    public static void saveAccount(String username, String password, String email) throws IOException {
        // Open the file in append mode and write the account details
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String hashed_password= PasswordHasher.generateStorablePassword(password);
            bw.write(username + "," + hashed_password + "," + email);
            bw.newLine(); // Move to the next line for the next entry
        }
    }


}
