package authentication;

import java.io.*;
import java.util.*;

public class ProfileEditor {
    private final String fileName;
    private Scanner scanner;

    public ProfileEditor() {
        this("accounts.csv"); // default filename
    }


    ProfileEditor(String fileName) {
        this.fileName = fileName;
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        ProfileEditor editor = new ProfileEditor();
        editor.startProfileEditor();
    }

    public void startProfileEditor() {
        System.out.println("=== Profile Editor ===");

        try {
            String username = null;
            String[] details = null;

            while (details == null) {
                System.out.print("Enter your username (or 'exit' to quit): ");
                username = scanner.nextLine();

                if (username.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye!");
                    return;
                }

                details = getAccountDetails(username);
                if (details == null) {
                    System.out.println("Username not found! Please try again.");
                }
            }

            System.out.println("\nCurrent Profile:");
            System.out.println("Username: " + username);
            System.out.println("Password: " + getPassword());
            System.out.println("Email: " + getEmail(details[1]));

            while (true) {
                System.out.println("\nEdit Options:");
                System.out.println("1. Change Username");
                System.out.println("2. Change Email");
                System.out.println("3. Change Password");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.print("Enter new username: ");
                        String newUsername = scanner.nextLine();
                        if (updateUsername(username, newUsername)) {
                            System.out.println("Username updated!");
                            username = newUsername;
                        } else {
                            System.out.println("Username already exists!");
                        }
                        break;

                    case "2":
                        boolean emailUpdated = false;
                        while (!emailUpdated) {
                            System.out.print("Enter new email: ");
                            String newEmail = scanner.nextLine();

                            try {
                                updateEmail(username, newEmail);
                                System.out.println("Email updated!");
                                emailUpdated = true;
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        break;

                    case "3":
                        boolean passwordUpdated = false;
                        while (!passwordUpdated) {
                            System.out.println("Password Requirements:");
                            System.out.println("- 8+ characters");
                            System.out.println("- 1 number, 1 capital letter");
                            System.out.println("- No spaces");
                            System.out.print("Enter new password: ");
                            String newPassword = scanner.nextLine();

                            try {
                                updatePassword(username, newPassword);
                                System.out.println("Password updated!");
                                passwordUpdated = true;
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        break;

                    case "4":
                        System.out.println("Goodbye!");
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

    // Masking methods
    String getPassword() {
        return "****";
    }

    String getEmail(String email) {
        if (email == null || email.isEmpty()) return "";

        String[] parts = email.split("@");
        if (parts.length != 2) return "****";

        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.length() == 1) {
            return localPart.charAt(0) + "****@" + domain;
        } else if (localPart.length() > 1) {
            return localPart.charAt(0) + "****" +
                    localPart.charAt(localPart.length()-1) + "@" + domain;
        }
        return "****";
    }

    // Account operations
    String[] getAccountDetails(String username) throws IOException {
        Map<String, String[]> accounts = loadAccounts();
        return accounts.get(username);
    }

    boolean updateUsername(String currentUsername, String newUsername) throws IOException {
        Map<String, String[]> accounts = loadAccounts();

        if (accounts.containsKey(newUsername) && !currentUsername.equals(newUsername)) {
            return false;
        }

        String[] details = accounts.remove(currentUsername);
        if (details == null) return false;

        accounts.put(newUsername, details);
        writeAllAccounts(accounts);
        return true;
    }

    void updateEmail(String username, String newEmail) throws IOException {
        if (!isValidEmail(newEmail)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        Map<String, String[]> accounts = loadAccounts();
        String[] details = accounts.get(username);
        if (details == null) throw new IllegalArgumentException("User not found");

        details[1] = newEmail;
        writeAllAccounts(accounts);
    }

    void updatePassword(String username, String newPassword) throws IOException {
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Password doesn't meet requirements");
        }

        Map<String, String[]> accounts = loadAccounts();
        String[] details = accounts.get(username);
        if (details == null) throw new IllegalArgumentException("User not found");

        details[0] = newPassword;
        writeAllAccounts(accounts);
    }

    // File operations
    void writeAllAccounts(Map<String, String[]> accounts) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, String[]> entry : accounts.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue()[0] + "," + entry.getValue()[1]);
                bw.newLine();
            }
        }
    }

    Map<String, String[]> loadAccounts() throws IOException {
        Map<String, String[]> accounts = new HashMap<>();
        File file = new File(fileName);

        if (!file.exists()) {
            file.createNewFile();
            return accounts;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    accounts.put(parts[0], new String[]{parts[1], parts[2]});
                }
            }
        }
        return accounts;
    }

    // Validation methods
    boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        String[] parts = email.split("@");
        return parts.length == 2 &&
                !parts[0].isEmpty() &&
                !parts[1].isEmpty();
    }

    boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasNumber = false;
        boolean hasCapital = false;
        boolean hasSpace = false;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasNumber = true;
            else if (Character.isUpperCase(c)) hasCapital = true;
            else if (Character.isWhitespace(c)) hasSpace = true;
        }

        return hasNumber && hasCapital && !hasSpace;
    }
}