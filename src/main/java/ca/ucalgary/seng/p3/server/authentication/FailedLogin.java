package ca.ucalgary.seng.p3.server.authentication;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class FailedLogin {

    private static FailedLogin instance;

    public static FailedLogin getInstance() {
        if (instance == null) {
            instance = new FailedLogin();
        }
        return instance;
    }

    public static void main(String username) throws IOException {
        FailedLogin manager = new FailedLogin();
        Scanner scanner = new Scanner(System.in);
        AuthResult result;
        boolean loggedIn = false;

        // Loop until user successfully logs in or reaches max attempts
        while (!loggedIn) {
//            System.out.print("Username: ");
//            String user = scanner.nextLine();
            System.out.print("Password: ");
            String pass = scanner.nextLine();


            result = manager.authenticate(username, pass);
            System.out.println(result.getMessage());

            // Check if login was successful
            if (result.isSuccess()) loggedIn = true;
            else if (result.getMessage().contains("Account locked")) {
                // If account is locked, exit the loop
                break;
            }
        }

        scanner.close();
    }

//    private final Map<String, String> userCredentials = new HashMap<>();
    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    private final Map<String, Long> lockedAccounts = new ConcurrentHashMap<>();

    private static final int MAX_ALLOWED_ATTEMPTS = 5;
    private static final long LOCK_DURATION_MS = 15 * 60 * 1000L;

    public FailedLogin() {
//        try {
//            Map<String, String[]> accounts = AccountRegistrationCSV.loadAccounts();
//            for (Map.Entry<String, String[]> entry : accounts.entrySet()) {
//                userCredentials.put(entry.getKey(), entry.getValue()[0]);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Unable to load accounts database", e);
//        }
    }

    public AuthResult authenticate(String username, String password) throws IOException {
        if (isAccountLocked(username)) {
            long secondsLeft = (lockedAccounts.get(username) - System.currentTimeMillis()) / 1000;
            return new AuthResult(false, "Account locked. Try again in " + secondsLeft + " seconds.");
        }
        String storedPassword = AccountRegistrationCSV.loadAccounts().get(username)[0];
        if (!AccountRegistrationCSV.loadAccounts().containsKey(username) || !PasswordHasher.verifyPassword(password,storedPassword)) {
            int attempts = failedAttempts.merge(username, 1, Integer::sum);
            if (attempts >= MAX_ALLOWED_ATTEMPTS) {
                lockAccount(username);
                return new AuthResult(false, "Too many failed attempts. Account locked for 15 minutes.");
            }
            return new AuthResult(false, "Invalid credentials. " +
                    (MAX_ALLOWED_ATTEMPTS - attempts) + " attempt(s) left.");
        }
        failedAttempts.remove(username);
        return new AuthResult(true, "Login successful.");
    }

    private boolean isAccountLocked(String username) {
        Long expiry = lockedAccounts.get(username);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            lockedAccounts.remove(username);
            failedAttempts.remove(username);
            return false;
        }
        return true;
    }

    private void lockAccount(String username) {
        lockedAccounts.put(username, System.currentTimeMillis() + LOCK_DURATION_MS);
    }

    public boolean unlockAccount(String username) {
        if (lockedAccounts.remove(username) != null) {
            failedAttempts.remove(username);
            return true;
        }
        return false;
    }


    public static class AuthResult {
        private final boolean success;
        private final String message;

        public AuthResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }



}