package ca.ucalgary.seng.p3.server.authentication;

import java.io.*;
import java.util.*;

public class UserLogin {
    public static final String FILE_NAME = "accounts.csv"; // Stores registered user credentials
    public static final String SESSION_FILE = "session.csv"; // Stores session data

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
            System.out.println("Choose an option: \nLogin \nGuest Login \nRegister \nForget Password or Username \nExit"); // Displays menu options to the user
            String choice = scanner.nextLine().trim().toLowerCase();
            switch (choice) {
                case "login":
                    login(scanner);
                    break;
                case "guest login":
                    guestLogin();
                    break;
                case "register":
                    try {
                        AccountRegistrationCSV.createNewAccount(scanner);
                    } catch (IOException e) {
                        System.out.println("Registration failed due to an error: " + e.getMessage());
                    }
                    break;
                case "forget password or username":
                    ResetCredentials.main(new String[]{}); // Calls ResetCredentials to handle resetting
                    break;
                case "exit":
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
    public static void login(Scanner scanner) {
        Map<String, String> accounts = loadAccounts();

        if (accounts.isEmpty()) {
            System.out.println("No registered accounts found. Please register first."); //what do i even say?
            return;
        }
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            if (!accounts.containsKey(username)) {
                System.out.println("Invalid username. Please try again.");
                return;
            }
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            if (accounts.containsKey(username) && PasswordHasher.verifyPassword(password, accounts.get(username))) { // Checks if credentials match
                if (isMFAEnabled(username)) {
                    if (MultifactorAuthentication.startMFAProcess(scanner, username)){
                        System.out.println("Login successful! Welcome, " + username + ".");
                        saveSession(username); // Saves the session
                    }
                    else {
                        System.out.println("Login failed.");
                    }
                }
                else {
                    System.out.println("Login successful! Welcome, " + username + ".");
                    saveSession(username); // Saves the session
                }
            } else {
                // calls FailedLogin
                System.out.println("Invalid password. Please try again.");
                FailedLogin.main(username);
            }
    }

    // Handles guest login by saving a session under the name "Guest" + randomNumber.
    public static void guestLogin() {
        Random random = new Random();
        StringBuilder randomNumbers = new StringBuilder();
        for (int i = 0; i < 5; i++) { // Generates 5 random numbers and store them in a string
            randomNumbers.append(random.nextInt(9) + 1); // Generates numbers from 1 to 9
        }
        saveSession("Guest" + randomNumbers); // Save session with guest details
        System.out.println("Welcome! Guest" + randomNumbers + "!");
    }

    /**
     * Loads user accounts from the accounts.csv file into a HashMap.
     * @return Map containing usernames as keys and passwords as values.
     */
    public static Map<String, String> loadAccounts() {
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
     * @param username: The username to be saved in the session.
     */
    public static void saveSession(String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SESSION_FILE, true))) {
            writer.write(username + "\n");
        } catch (IOException e) {
            System.out.println("Error saving session: " + e.getMessage());
        }
    }
    /**
     * Checks if MFA is enabled for a given username.
     * @param username: The username for which MFA id enabled or disabled.
     */
    public static boolean isMFAEnabled(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 3 && parts[0].equals(username)) {
                    return parts[3].trim().equalsIgnoreCase("enabled");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the account database: " + e.getMessage());
        }
        return false; // Defaults to no MFA if there's an error or no match
    }
}