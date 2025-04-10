package ca.ucalgary.seng.p3.server.authentication;

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
                System.out.println("4. Delete Account");
                System.out.println("5. Enable Multifactor Authentication");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");

                String choice = scanner.nextLine().trim();
                Integer choiceInt = Integer.parseInt(choice);

                if (choice.equals("6") || (choiceInt > 5) || (choiceInt < 1)) {
                    System.out.println("Goodbye!");
                    return;
                }

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
                        System.out.println("\n=== ACCOUNT DELETION ===");
                        System.out.println("WARNING: This action is irreversible!");
                        System.out.print("Type 'CONFIRM' to proceed: ");
                        String confirmation = scanner.nextLine().trim();

                        if (!confirmation.equals("CONFIRM")) {
                            System.out.println("Deletion cancelled.");
                            break;
                        }

                        System.out.println(("Please Enter Your Password: "));
                        String finalPassword = scanner.nextLine();


                        MultifactorAuthentication mfa = new MultifactorAuthentication();
                        if (!mfa.startMFAProcess(scanner, username)) {
                            System.out.println("MFA verification failed.");
                            break;
                        }

                            boolean deleted = AccountDeletion.deleteAccount(username, finalPassword);
                        if (deleted) {
                            System.out.println("Account deleted. Goodbye!");
                            UserLogout.logout();
                            return;
                        } else {
                            System.out.println("Deletion failed. Account not found.");
                        }
                        break;

                    case "5":
                        String currentMfaStatus = details.length > 2 ? details[2] : "disabled";

                        System.out.println("\nCurrent MFA Status: " +
                                (currentMfaStatus.equalsIgnoreCase("enabled") ? "ENABLED" : "DISABLED"));

                        System.out.println("Would you like to:");
                        System.out.println("1. Enable MFA");
                        System.out.println("2. Disable MFA");
                        System.out.println("3. Cancel");
                        System.out.print("Choose an option: ");

                        String mfaChoice = scanner.nextLine();

                        switch (mfaChoice) {
                            case "1":
                                if (currentMfaStatus.equalsIgnoreCase("enabled")) {
                                    System.out.println("MFA is already enabled!");
                                } else {
                                    MultifactorAuthentication mfa2 = new MultifactorAuthentication();
                                    if (mfa2.startMFAProcess(scanner, username)) {
                                        Map<String, String[]> accounts = loadAccounts();
                                        String[] userDetails = accounts.get(username);

                                        if (userDetails.length < 3) {
                                            userDetails = Arrays.copyOf(userDetails, 3);
                                        }

                                        userDetails[2] = "enabled";
                                        accounts.put(username, userDetails);
                                        writeAllAccounts(accounts);

                                        System.out.println("MFA has been successfully enabled!");
                                        details = userDetails; // Update local details
                                    } else {
                                        System.out.println("MFA setup failed. Please try again.");
                                    }
                                }
                                break;

                            case "2":
                                if (!currentMfaStatus.equalsIgnoreCase("enabled")) {
                                    System.out.println("MFA is already disabled!");
                                } else {
                                    MultifactorAuthentication mfa2 = new MultifactorAuthentication();
                                    if (mfa2.startMFAProcess(scanner, username)) {
                                        Map<String, String[]> accounts = loadAccounts();
                                        String[] userDetails = accounts.get(username);
                                        userDetails[2] = "disabled";
                                        accounts.put(username, userDetails);
                                        writeAllAccounts(accounts);

                                        System.out.println("MFA has been successfully disabled!");
                                        details = userDetails; // Update local details
                                    } else {
                                        System.out.println("Verification failed. MFA remains enabled.");
                                    }
                                }
                                break;

                            case "3":
                                System.out.println("MFA settings unchanged.");
                                break;

                            default:
                                System.out.println("Invalid choice. MFA settings unchanged.");
                        }
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    // Masking methods
    public String getPassword() {
        return "********";
    }

    public String getEmail(String email) {
        if (email == null || email.isEmpty()) return "";

        String[] parts = email.split("@");
        if (parts.length != 2) return "****";

        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.length() == 1) {
            return localPart.charAt(0) + "****@" + domain;
        } else if (localPart.length() > 1) {
            return localPart.charAt(0) + "****" +
                    localPart.charAt(localPart.length() - 1) + "@" + domain;
        }
        return "****";
    }

    // Account operations
    public String[] getAccountDetails(String username) throws IOException {
        Map<String, String[]> accounts = loadAccounts();
        return accounts.get(username);
    }

    public boolean updateUsername(String currentUsername, String newUsername) throws IOException {
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

    public void updateEmail(String username, String newEmail) throws IOException {
        if (!isValidEmail(newEmail)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        Map<String, String[]> accounts = loadAccounts();
        String[] details = accounts.get(username);
        if (details == null) throw new IllegalArgumentException("User not found");

        details[1] = newEmail;
        writeAllAccounts(accounts);
    }

    public void updatePassword(String username, String newPassword) throws IOException {
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Password doesn't meet requirements");
        }

        Map<String, String[]> accounts = loadAccounts();
        String[] details = accounts.get(username);
        if (details == null) throw new IllegalArgumentException("User not found");

        details[0] = PasswordHasher.generateStorablePassword(newPassword);
        writeAllAccounts(accounts);
    }

    // File operations
    public void writeAllAccounts(Map<String, String[]> accounts) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, String[]> entry : accounts.entrySet()) {
                String username = entry.getKey();
                String[] details = entry.getValue();
                // Ensure we have at least 3 elements (password, email, mfa)
                if (details.length < 3) {
                    details = Arrays.copyOf(details, 3);
                    details[2] = "disabled"; // default MFA value
                }
                bw.write(String.join(",",
                        username,
                        details[0], // password
                        details[1], // email
                        details[2]  // mfa status
                ));
                bw.newLine();
            }
        }
    }

    public void writeAllStats(String oldUsername, String newUsername) throws IOException {
        // Read the current stats file and store the updated lines
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("stats.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                String username = details[0];

                // Check if the line corresponds to the old username
                if (username.equals(oldUsername)) {
                    // Replace the old username with the new one
                    details[0] = newUsername;
                    line = String.join(",", details);
                }

                updatedLines.add(line);
            }
        }

        // Write the updated content back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("stats.csv"))) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine);
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
                    String username = parts[0];
                    String[] details = new String[]{
                            parts[1], // password
                            parts[2],  // email
                            parts.length > 3 ? parts[3] : "disabled" // mfa status
                    };
                    accounts.put(username, details);
                }
            }
        }
        return accounts;
    }

    // Validation methods
    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        String[] parts = email.split("@");
        return parts.length == 2 &&
                !parts[0].isEmpty() &&
                !parts[1].isEmpty();
    }

    public boolean isValidPassword(String password) {
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