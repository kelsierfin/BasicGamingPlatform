package authentication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * MultifactorAuthentication class for handling
 * a second-factor code verification process.
 */
public class MultifactorAuthentication {

    // Name of the CSV file used as the account database
    private static final String FILE_NAME = "accounts.csv";

    // How long the one-time code stays valid (2 minutes by default)
    private static final long CODE_VALIDITY_DURATION_MS = TimeUnit.MINUTES.toMillis(2);

    // Maximum number of attempts the user can make before access is denied
    private static final int MAX_ATTEMPTS = 3;

    /**
     * Initiates the MFA process by:
     * 1. Looking up the user's email.
     * 2. Generating a one-time 6-digit code.
     * 3. Simulating its delivery to the user's email.
     * 4. Verifying user input against the generated code.
     *
     * @param scanner  A Scanner for user input.
     * @param username The username of the player who needs MFA verification.
     * @return true if MFA succeeds, false otherwise.
     */
    public static boolean startMFAProcess(Scanner scanner, String username) {
        // Retrieve the email associated with this username
        String email = fetchUserEmail(username);
        if (email == null) {
            System.out.println("No registered email found for user: " + username);
            return false;
        }

        // Generate a random 6-digit one-time code
        String mfaCode = generateOneTimeCode(6);
        long codeGenerationTime = System.currentTimeMillis();

        // Simulate sending the code to the user's email
        System.out.println("\nA one-time MFA code has been sent to the email associated "
                         + "with the account (" + email + ").");
        System.out.println("For demonstration purposes, your MFA code is: " + mfaCode);

        // Allow the user up to MAX_ATTEMPTS to input the correct code
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.print("Enter the MFA code (attempt " + attempt + " of "
                    + MAX_ATTEMPTS + "): ");
            String userInput = scanner.nextLine().trim();

            // Check if the code has expired
            if (System.currentTimeMillis() - codeGenerationTime > CODE_VALIDITY_DURATION_MS) {
                System.out.println("The MFA code has expired. Please request a new code.");
                return false;
            }

            // Check if the user input matches the code
            if (userInput.equals(mfaCode)) {
                System.out.println("MFA successful! Welcome, " + username + "!");
                return true;
            } else {
                System.out.println("Incorrect code. Please try again.");
            }
        }

        // If the code wasn't verified in the allowed attempts
        System.out.println("MFA failed. You have exhausted all attempts. Access denied.");
        return false;
    }
    /**
     * Fetches the user's email from the CSV file based on the provided username.
     *
     * @param username The username for which we want to retrieve email
     * @return The email address if found, or null if not found
     */
    private static String fetchUserEmail(String username) {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("Error: " + FILE_NAME + " not found. "
                             + "Cannot retrieve user email.");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Each line in the CSV is: username,password,email
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Basic validation: at least username, password, email
                if (parts.length >= 3 && parts[0].equals(username)) {
                    return parts[2]; // The email is in the third column
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the account database: " + e.getMessage());
        }

        // Return null if not found
        return null;
    }

    /**
     * Generates a random numeric code of a specified length.
     *
     * @param length The number of digits in the code (e.g., 6 for a 6-digit code)
     * @return A random numeric code as a String
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