package ca.ucalgary.seng.p3.server.authentication;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountDeletion {
    private static final String CSV_FILE_PATH = "accounts.csv";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean deleted = deleteAccount(username, password);

        if (deleted) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Account not found or credentials incorrect.");
        }


    }

    /**
     * Deletes an account from the CSV file if username and password match.
     *
     * @param username the username to match
     * @param password the password to match
     * @return true if account was found and deleted, false otherwise
     */
    public static boolean deleteAccount(String username, String password) {
        List<String> remainingAccounts = new ArrayList<>();
        boolean accountDeleted = false;

        try {
            // Read all lines from the CSV file
            List<String> lines = Files.readAllLines(Paths.get(CSV_FILE_PATH));

            // Process the header

            // Check each line for the account to delete
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                // Assuming CSV format is: username,password,email
                if (parts.length >= 2 &&
                        parts[0].equals(username) &&
                        PasswordHasher.verifyPassword(password,parts[1])) {
                    // Found the account to delete, don't add it to remainingAccounts
                    accountDeleted = true;
                } else {
                    // Keep all other accounts
                    remainingAccounts.add(line);
                }
            }

            // Write the remaining accounts back to the CSV file
            if (accountDeleted) {
                Files.write(Paths.get(CSV_FILE_PATH), remainingAccounts, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            }

        } catch (IOException e) {
            System.err.println("Error accessing the CSV file: " + e.getMessage());
            return false;
        }

        return accountDeleted;
    }
}