package ca.ucalgary.seng.p3.server.authentication.reflection;

import ca.ucalgary.seng.p3.server.authentication.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central authentication service that directly implements all authentication functionality.
 * This class is used by the reflection invoker to perform authentication operations.
 */
public class AuthenticationService {



    /**
     * Attempts to log in a user
     *
     * @param username Username to log in
     * @param password Password to verify
     * @return LoginResult containing login status and message
     */
    public LoginResult login(String username, String password) {
        try {
            Map<String, String> accounts = UserLogin.loadAccounts();

            System.out.println("Loaded Accounts: ");
            for (Map.Entry<String, String> entry : accounts.entrySet()) {
                System.out.println("Username: " + entry.getKey() + ", Password: " + entry.getValue());
            }

            if (accounts.isEmpty()) {
                return new LoginResult(false, "No registered accounts found. Please register first.", null, false);
            }

            if (!accounts.containsKey(username)) {
                return new LoginResult(false, "Invalid username. Please try again.", null, false);
            }

            if (accounts.containsKey(username) && PasswordHasher.verifyPassword(password, accounts.get(username))) {
                boolean mfaEnabled = UserLogin.isMFAEnabled(username);
                if (!mfaEnabled) {
                    UserLogin.saveSession(username);

                    return new LoginResult(true, "Login successful! Welcome, " + username + ".", username, false);
                } else {
                    return new LoginResult(false, "MFA required", username, true);
                }
            } else {
                // Call FailedLogin to handle the attempts counter
                FailedLogin loginManager = FailedLogin.getInstance();
                FailedLogin.AuthResult result = loginManager.authenticate(username, password);
                return new LoginResult(false, result.getMessage(), null, false);
            }
        } catch (Exception e) {
            return new LoginResult(false, "Login error: " + e.getMessage(), null, false);
        }
    }

    /**
     * Guest login method
     * @return LoginResult with guest username
     */
    public LoginResult guestLogin() {
        String guestUsername = UserLogin.guestLogin();
        return new LoginResult(true, "Welcome! " + guestUsername + "!", guestUsername, false);
    }

    /**
     * Register a new user
     */
    public RegistrationResult register(String username, String password, String confirmPassword, String email) {
        try {
            Map<String, String[]> accounts = AccountRegistrationCSV.loadAccounts();

            // Validate inputs
            StringBuilder errorMessage = new StringBuilder();
            boolean isValid = true;

            // Check username
            if (username.isEmpty()) {
                errorMessage.append(" Missing Username.");
                isValid = false;
            } else if (accounts.containsKey(username)) {
                errorMessage.append(" Username already exists.");
                isValid = false;
            }

            // Check email
            if (email.isEmpty()) {
                errorMessage.append(" Missing Email.");
                isValid = false;
            } else if (!AccountRegistrationCSV.isValidEmail(email)) {
                errorMessage.append(" Invalid Email.");
                isValid = false;
            }

            // Check password
            if (!AccountRegistrationCSV.isProperPassword(password)) {
                errorMessage.append(" Password must include at least 8 characters, 1 number, 1 capital letter, and no spaces.");
                isValid = false;
            } else if (!password.equals(confirmPassword)) {
                errorMessage.append(" Passwords do not match.");
                isValid = false;
            }

            // If valid, create account
            if (isValid) {
                AccountRegistrationCSV.saveAccount(username, password, email);
                return new RegistrationResult(true, "Account created successfully!");
            } else {
                return new RegistrationResult(false, errorMessage.toString());
            }
        } catch (IOException e) {
            return new RegistrationResult(false, "Registration error: " + e.getMessage());
        }
    }

    /**
     * Generate an MFA code
     */
    public MfaCodeResult generateMfaCode(String username) {
        String code = MultifactorAuthentication.generateOneTimeCode(6);
        long generationTime = System.currentTimeMillis();

        String email = MultifactorAuthentication.fetchUserEmail(username);
        if (email == null) {
            return new MfaCodeResult(false, "No email found for user", null, 0);
        }

        return new MfaCodeResult(true, "MFA code generated", code, generationTime);
    }

    /**
     * Verify an MFA code
     */
    public MfaVerifyResult verifyMfaCode(String username, String code, String expectedCode, long generationTime) {
        // Check if code has expired (1 hour timeout)
        if (System.currentTimeMillis() - generationTime > 3600000) {
            return new MfaVerifyResult(false, "The MFA code has expired. Please request a new code.");
        }

        // Check if code matches
        if (code.equals(expectedCode)) {
            UserLogin.saveSession(username);
            return new MfaVerifyResult(true, "MFA successful for user " + username + "!");
        } else {
            return new MfaVerifyResult(false, "Incorrect code. Please try again.");
        }
    }

    /**
     * Logout a user
     */
    public LogoutResult logout(String username) {
        List<String> loggedInUsers = UserLogout.getSessionUsers();

        if (loggedInUsers.isEmpty()) {
            return new LogoutResult(false, "No active sessions found. You are not logged in.");
        }

        if (!loggedInUsers.contains(username)) {
            return new LogoutResult(false, "Username does not match any logged-in user. Logout failed.");
        }

        UserLogout.clearSession(username);
        return new LogoutResult(true, "You have been logged out successfully.");
    }

    /**
     * Delete a user account
     */
    public AccountDeletionResult deleteAccount(String username, String password) {
        boolean deleted = AccountDeletion.deleteAccount(username, password);

        if (deleted) {
            UserLogout.clearSession(username);
            return new AccountDeletionResult(true, "Account deleted successfully.");
        } else {
            return new AccountDeletionResult(false, "Account not found or credentials incorrect.");
        }
    }

    /**
     * Get a user's profile
     */
    public ProfileResult getUserProfile(String username) {
        try {
            ProfileEditor profileEditor = new ProfileEditor();
            String[] accountDetails = profileEditor.getAccountDetails(username);

            if (accountDetails == null) {
                return new ProfileResult(false, "User not found", null, null, false);
            }

            String password = "********"; // Masked for security
            String email = accountDetails[1];
            boolean mfaEnabled = accountDetails.length > 2 &&
                    accountDetails[2].equalsIgnoreCase("enabled");

            return new ProfileResult(true, "Profile retrieved", email, password, mfaEnabled);
        } catch (IOException e) {
            return new ProfileResult(false, "Error retrieving profile: " + e.getMessage(), null, null, false);
        }
    }

    /**
     * Check if an account is locked
     */
    public AccountLockResult checkAccountLock(String username) {
        if (username == null || username.isEmpty()) {
            return new AccountLockResult(false, "");
        }

        FailedLogin loginManager = FailedLogin.getInstance();
        try {
            FailedLogin.AuthResult result = loginManager.authenticate(username, "");

            if (!result.isSuccess() && result.getMessage().contains("locked")) {
                return new AccountLockResult(true, result.getMessage());
            }
        } catch (IOException e) {
            // Log the error
            System.err.println("Error checking account lock status: " + e.getMessage());
            // Return a generic error message
            return new AccountLockResult(false, "Error checking account status");
        }

        return new AccountLockResult(false, "");
    }

    // Result classes to provide typed responses

    public static class LoginResult {
        private final boolean success;
        private final String message;
        private final String username;
        private final boolean requiresMfa;

        public LoginResult(boolean success, String message, String username, boolean requiresMfa) {
            this.success = success;
            this.message = message;
            this.username = username;
            this.requiresMfa = requiresMfa;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getUsername() { return username; }
        public boolean requiresMfa() { return requiresMfa; }
    }

    public static class RegistrationResult {
        private final boolean success;
        private final String message;

        public RegistrationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    public static class MfaCodeResult {
        private final boolean success;
        private final String message;
        private final String code;
        private final long generationTime;

        public MfaCodeResult(boolean success, String message, String code, long generationTime) {
            this.success = success;
            this.message = message;
            this.code = code;
            this.generationTime = generationTime;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getCode() { return code; }
        public long getGenerationTime() { return generationTime; }
    }

    public static class MfaVerifyResult {
        private final boolean success;
        private final String message;

        public MfaVerifyResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    public static class LogoutResult {
        private final boolean success;
        private final String message;

        public LogoutResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    public static class AccountDeletionResult {
        private final boolean success;
        private final String message;

        public AccountDeletionResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    public static class ProfileResult {
        private final boolean success;
        private final String message;
        private final String email;
        private final String password;
        private final boolean mfaEnabled;

        public ProfileResult(boolean success, String message, String email,
                             String password, boolean mfaEnabled) {
            this.success = success;
            this.message = message;
            this.email = email;
            this.password = password;
            this.mfaEnabled = mfaEnabled;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public boolean isMfaEnabled() { return mfaEnabled; }
    }

    public static class AccountLockResult {
        private final boolean locked;
        private final String message;

        public AccountLockResult(boolean locked, String message) {
            this.locked = locked;
            this.message = message;
        }

        public boolean isLocked() { return locked; }
        public String getMessage() { return message; }
    }
}