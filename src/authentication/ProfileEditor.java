package authentication;

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
            System.out.println("Email: " + getEmail(details[1]));

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
                        if //(updateUsername(username, newUsername)) { need to complete
                            System.out.println("Username updated successfully!");
                            username = newUsername;
                        } else {
                            System.out.println("Error: Username already exists!");
                        }
                        break;

                    case "2":
                        boolean emailUpdated = false;
                        while (!emailUpdated) {
                            System.out.print("Enter new email: ");
                            String newEmail = scanner.nextLine();

                            try {
                                //updateEmail(username, newEmail); // need to finish
                                System.out.println("Email updated successfully!");
                                emailUpdated = true;
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                                System.out.println("Please try again.");
                            }
                        }
                        break;

                    case "3":
                        boolean passwordUpdated = false;
                        while (!passwordUpdated) {
                            System.out.println("Password requirements:");
                            System.out.println("- At least 8 characters");
                            System.out.println("- At least one number");
                            System.out.println("- At least one capital letter");
                            System.out.println("- No spaces");
                            System.out.print("Enter new password: ");
                            String newPassword = scanner.nextLine();

                            try {
                                //updatePassword(username, newPassword); //need to create method
                                System.out.println("Password updated successfully!");
                                passwordUpdated = true;
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                                System.out.println("Please try again.");
                            }
                        }
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


    public String getPassword() {
        return "****";
    }

    public String getEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "";
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return "*";
        }

        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.length() == 1) {
            localPart = localPart.charAt(0) + "****";
        } else if (localPart.length() > 1) {
            char first = localPart.charAt(0);
            char last = localPart.charAt(localPart.length() - 1);
            localPart = first + "****" + last;
        }

        return localPart + "@" + domain;
    }

}
