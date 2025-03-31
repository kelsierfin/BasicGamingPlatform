package authentication;

import java.io.*;
import java.util.*;

public class UserLogout {
    private static final String SESSION_FILE = "session.csv"; // stores active user sessions

    public static void main(String[] args) {
        logout(); // Initiates logout process
    }

    /**
     * Handles the logout process.
     */
    public static void logout() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to log out? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equalsIgnoreCase("no")) { // If the user chooses not to log out, it cancels the process
            System.out.println("Logout canceled.");
            return;
        }

        List<String> loggedInUsers = getSessionUsers(); // Retrieves currently logged-in users from the session file
        if (loggedInUsers.isEmpty()) {
            System.out.println("No active sessions found. You are not logged in.");
            return;
        }

        System.out.print("Enter your username to confirm logout: ");
        String inputUsername = scanner.nextLine().trim();

        if (!loggedInUsers.contains(inputUsername)) { // Validates if the username exists in the session
            System.out.println("Username does not match any logged-in user. Logout failed.");
            return;
        }

        System.out.println("Logging out " + inputUsername);
        clearSession(inputUsername);
        System.out.println("You have been logged out successfully.");
        redirectToLogin();
    }

    /**
     * Retrieves the list of currently logged-in users from the session file.
     * @return List of usernames currently logged in.
     */
    private static List<String> getSessionUsers() {
        List<String> sessionUsers = new ArrayList<>();
        File file = new File(SESSION_FILE);

        if (!file.exists()) {
            return sessionUsers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    sessionUsers.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading session file: " + e.getMessage());
        }

        return sessionUsers;
    }

    /**
     * Removes the specified user from the session file, effectively logging them out.
     * @param username The username to be removed from the session.
     */
    private static void clearSession(String username) {
        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            return;
        }

        List<String> users = getSessionUsers();
        if (!users.contains(username)) {
            System.out.println("No matching session found for the given username.");
            return;
        }

        users.remove(username);

        try (FileWriter writer = new FileWriter(file, false)) {
            for (String user : users) {
                writer.write(user + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error updating session file: " + e.getMessage());
        }
    }

    private static void redirectToLogin() {
        System.out.println("Redirecting to the login page");
    }
}
