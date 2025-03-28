package authentication;

import java.io.*;
import java.util.*;

public class UserLogin {
    private static final String FILE_NAME = "accounts.csv"; // Stores registered user credentials
    private static final String SESSION_FILE = "session.csv"; // Stores session data

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
            System.out.println("Choose an option: \nLogin \nGuest Login \nRegister \nExit"); // Displays menu options to the user
            String choice = scanner.nextLine();
            switch (choice) {
                case "Login":
                    login(scanner);
                    break;
                case "Guest Login":
                    guestLogin();
                    break;
                case "Register": //still need to add
                    try {
                        AccountRegistrationCSV.createNewAccount(scanner);
                    } catch (IOException e) {
                        System.out.println("Registration failed due to an error: " + e.getMessage());
                    }
                    break;
                case "Exit":
                    System.out.println("Exiting. See you again!");
                    scanner.close();
                    return; // Exit the program
                default:
                    System.out.println("Invalid. Please try again.");
        }
    }

    /**
     * Handles user login by verifying credentials from the accounts.csv file.
     * @param scanner Scanner object for user input.
     */
    private static void login(Scanner scanner) {
        Map<String, String> accounts = loadAccounts();

        if (accounts.isEmpty()) {
            System.out.println("No registered accounts found. Please register first."); //what do i even say?
            return;
        }

        while (true) { // Loops until valid credentials are entered
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            if (accounts.containsKey(username) && accounts.get(username).equals(password)) { // Checks if credentials match
                System.out.println("Login successful! Welcome, " + username + ".");
                saveSession(username); // Saves the session
                return;
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
    }

    // Handles guest login by saving a session under the name "Guest".
    private static void guestLogin() {
        System.out.println("Guest login successful!");
        saveSession("Guest");//temp
    }

    /**
     * Loads user accounts from the accounts.csv file into a HashMap.
     * @return Map containing usernames as keys and passwords as values.
     */
    private static Map<String, String> loadAccounts() {
        Map<String, String> accounts = new HashMap<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) { // Checks if the file exists
            System.out.println("Error: accounts.csv file not found.");
            return accounts;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) { // Reads the file and populate the HashMap
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    accounts.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the account database: " + e.getMessage());
        }

        return accounts;
    }

    /**
     * Saves the username of the logged-in user to the session file.
     * @param username The username to be saved in the session.
     */
    private static void saveSession(String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SESSION_FILE, true))) {
            writer.write(username + "\n");
        } catch (IOException e) {
            System.out.println("Error saving session: " + e.getMessage());
        }
    }
}