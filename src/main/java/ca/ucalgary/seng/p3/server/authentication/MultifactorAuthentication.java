package ca.ucalgary.seng.p3.server.authentication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * A MultifactorAuthentication class that:
 * 1. Looks up the user's email in "accounts.csv"
 * 2. Generates a one-time code valid for 1 hour
 * 3. Simulates sending the code to the user's email
 * 4. Verifies user input (up to 3 attempts)
 *
 */
public class MultifactorAuthentication {

    // Name of the CSV file used as the account database
    private static final String FILE_NAME = "accounts.csv";

    // MFA code validity: 1 hour
    private static final long CODE_VALIDITY_DURATION_MS = TimeUnit.HOURS.toMillis(1);

    // Maximum number of user attempts to enter the MFA code
    private static final int MAX_ATTEMPTS = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();

        String email = fetchUserEmail(username);
        if (email == null) {
            System.out.println("No account found for user: " + username);
        } else {
            System.out.println("A one‑time code has been sent to your registered email: " + email);
            boolean passed = startMFAProcess(scanner, username);
            System.out.println(passed
                    ? "MFA passed. Access granted!"
                    : "MFA failed. Access denied.");
        }

        scanner.close();
    }


    /**
     * Initiates the MFA process by:
     * 1. Looking up the user's email in the CSV file.
     * 2. Generating a one-time code.
     * 3. Simulating sending the code to the user's email.
     * 4. Verifying the user input against the generated code.
     *
     * @param scanner  A Scanner for user input
     * @param username The username of the player who needs MFA verification
     * @return true if MFA succeeds, false otherwise
     */
    public static boolean startMFAProcess(Scanner scanner, String username) {
        // Retrieve the email for this username
        String email = fetchUserEmail(username);
        if (email == null) {
            System.out.println("No registered email found for user: " + username);
            return false;
        }

        // Generate a random 6-digit one-time code
        String mfaCode = generateOneTimeCode(6);
        long codeGenerationTime = System.currentTimeMillis();

        // Simulate sending the code to the user's email
        System.out.println("\nA one-time MFA code has been (simulated) sent to " + email + ".");
        // For demonstration, show the code here
        System.out.println("Your MFA code is: " + mfaCode);

        // Give the user up to MAX_ATTEMPTS to input the correct code
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.print("\nEnter the MFA code (attempt " + attempt + " of " + MAX_ATTEMPTS + "): ");
            String userInput = scanner.nextLine().trim();

            // Check if the code has expired
            if (System.currentTimeMillis() - codeGenerationTime > CODE_VALIDITY_DURATION_MS) {
                System.out.println("The MFA code has expired. Please request a new code.");
                return false;
            }

            // Check if user input matches the generated code
            if (userInput.equals(mfaCode)) {
                System.out.println("MFA successful for user " + username + "!");
                return true;
            } else {
                System.out.println("Incorrect code. Please try again.");
            }
        }

        // If we reach here, the user did not enter the correct code within allowed attempts
        return false;
    }

    /**
     * Fetches the user's email address from "accounts.csv" based on the username.
     *
     * @param username The username whose email we want
     * @return The email as a string if found, otherwise null
     */
    private static String fetchUserEmail(String username) {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("Error: " + FILE_NAME + " not found. Cannot retrieve user email.");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // CSV lines: username,password,email
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Basic validation: 3 fields -> username, password, email
                if (parts.length >= 3 && parts[0].equals(username)) {
                    return parts[2]; // The email is the third entry
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the account database: " + e.getMessage());
        }

        // Null if not found
        return null;
    }

    /**
     * Generates a random numeric code of a specified length (e.g., 6 digits).
     *
     * @param length Number of digits for the code
     * @return A string representing the numeric code
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
