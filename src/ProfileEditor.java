//package authentication;

import java.io.*;
import java.util.*;

public class ProfileEditor {
    private static final String FILE_NAME = "accounts.csv";
    private Scanner scanner;

    public ProfileEditor() {
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        ProfileEditor editor = new ProfileEditor();
        editor.startProfileEditor();
    }

    public void startProfileEditor() {
        System.out.println("=== Profile Editor ===");

        try {
            // Verify user first
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            String[] details = getAccountDetails(username);
            if (details == null) {
                System.out.println("Username not found!");
                return;
            }

            // Display current profile with masked sensitive info
            System.out.println("\nCurrent Profile:");
            System.out.println("Username: " + username);
            System.out.println("Password: " + getPassword());
            //System.out.println("Email: " + getEmail(details[1]));

            // Menu for editing
            while (true) {
                System.out.println("\nWhat would you like to edit?");
                System.out.println("1. Change Username");
                System.out.println("2. Change Email");
                System.out.println("3. Change Password");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.print("Enter new username: ");
                        String newUsername = scanner.nextLine();
                        //add method to update username
                            System.out.println("Username updated successfully!");
                            username = newUsername;
                        } else {
                            System.out.println("Error: Username already exists!");
                        }
                        break;

                    case "2":
                        System.out.print("Enter new email: ");
                        String newEmail = scanner.nextLine();
                        //add method to update email
                        System.out.println("Email updated successfully!");
                        break;

                    case "3":
                        System.out.print("Enter new password: ");
                        String newPassword = scanner.nextLine();
                        //add method to update pw
                        System.out.println("Password updated successfully!");
                        break;

                    case "4":
                        System.out.println("Exiting profile editor...");
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    /**
     * Returns masked password (****)
     */
    public String getPassword() {
        return "****";
    }

    }