package authentication;

import java.io.*;
import java.util.*;

/**
 * This class allows users to reset their username or password.
 * User data is stored in a CSV file named "accounts.csv".
 */
public class ResetCredentials {
    private static final String FILE_NAME = "accounts.csv"; // Contains user account data

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to reset your password or username?");
        String choice = scanner.nextLine().trim().toLowerCase();

        switch (choice) { // Determines which credential to reset
            case "password":
                resetPassword(scanner);
                break;
            case "username":
                resetUsername(scanner);
                break;
            default:
                System.out.println("Invalid choice.");
        }
        scanner.close();
    }

    /**
     * Resets the user's password after verifying their email.
     */
    private static void resetPassword(Scanner scanner) {
        List<String[]> accounts = loadAccounts();

        System.out.print("Enter your registered email: ");
        String email = scanner.nextLine().trim();

        String[] account = findAccountByEmail(accounts, email);
        if (account == null) {
            System.out.println("Email not found.");
            return;
        }

        if (!verifyEmailWithCode(scanner, email)) { // Email verification process
            System.out.println("Email verification failed.");
            return;
        }

        String newPassword;
        do {
            System.out.print("Enter a new password: "); // Prompta user for a new password
            newPassword = scanner.nextLine();

            if (!isValidPassword(newPassword)) {
                System.out.println("Password must be at least 8 characters long, contain at least one number, one uppercase letter, and have no spaces.");
            }
        } while (!isValidPassword(newPassword));

        account[1] = newPassword;  // Update password in memory
        saveAccounts(accounts); // Saves updated account information
        System.out.println("Password successfully reset. You can now log in with your new password.");
    }

    /**
     * Resets the user's username after verifying their email.
     */
    private static void resetUsername(Scanner scanner) {
        List<String[]> accounts = loadAccounts();

        System.out.print("Enter your registered email: ");
        String email = scanner.nextLine().trim();

        String[] account = findAccountByEmail(accounts, email);
        if (account == null) {
            System.out.println("Email not found.");
            return;
        }

        if (!verifyEmailWithCode(scanner, email)) {
            System.out.println("Email verification failed."); // Email verification process
            return;
        }

        String newUsername;
        do {
            System.out.print("Enter a new username: "); // Prompts user for a valid new username
            newUsername = scanner.nextLine().trim();

            if (isUsernameTaken(accounts, newUsername)) {
                System.out.println("This username is already taken. Please choose a different one.");
            }
        } while (isUsernameTaken(accounts, newUsername));

        account[0] = newUsername;  // Update username in memory
        saveAccounts(accounts); // Saves updated account information
        System.out.println("Username successfully changed. You can now log in with your new username.");
    }

    private static boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[A-Z].*") && !password.contains(" ");
    }

    /**
     * Checks if a username is already taken.
     */
    private static boolean isUsernameTaken(List<String[]> accounts, String username) {
        for (String[] account : accounts) {
            if (account[0].equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Loads user accounts from the CSV file.
     */
    private static List<String[]> loadAccounts() {
        List<String[]> accounts = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("Error: accounts.csv file not found.");
            return accounts;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    accounts.add(parts);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading account database: " + e.getMessage());
        }

        return accounts;
    }

    /**
     * Saves user accounts back to the CSV file.
     */
    private static void saveAccounts(List<String[]> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String[] account : accounts) {
                writer.write(String.join(",", account) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    /**
     * Finds a user account based on the provided email.
     */
    private static String[] findAccountByEmail(List<String[]> accounts, String email) {
        for (String[] account : accounts) {
            if (account[2].equalsIgnoreCase(email)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Simulates email verification by generating a one-time code and prompting the user to enter it.
     */
    private static boolean verifyEmailWithCode(Scanner scanner, String email) {
        String code = generateOneTimeCode(6);
        System.out.println("A one-time verification code has been sent to: " + email);
        System.out.println("(For simulation purposes, your code is: " + code + ")");

        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.print("Enter the verification code (Attempt " + attempt + " of 3): ");
            String userInput = scanner.nextLine().trim();

            if (userInput.equals(code)) {
                System.out.println("Email verified successfully.");
                return true;
            } else {
                System.out.println("Incorrect code. Please try again.");
            }
        }

        System.out.println("Too many failed attempts. Verification failed.");
        return false;
    }

    /**
     * Generates a random numeric one-time code of a specified length.
     */
    private static String generateOneTimeCode(int length) {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            codeBuilder.append(random.nextInt(10));
        }
        return codeBuilder.toString();
    }
}
