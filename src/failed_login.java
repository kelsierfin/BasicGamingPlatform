package authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages authentication attempts and account locking for the game platform.
 * Handles the Failed Login Attempt use case by tracking unsuccessful login attempts
 * and locking accounts when necessary.
 */
public class AuthenticationManager {
    // Database connection would be here in production
    // For now using a stub/mock of user credentials
    private Map<String, String> userCredentials = new HashMap<>();
    
    // Track login attempts for each user
    private Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    
    // Track locked accounts with lock expiry time
    private Map<String, Long> lockedAccounts = new ConcurrentHashMap<>();
    
    // Configuration parameters
    private static final int MAX_ALLOWED_ATTEMPTS = 5;
    private static final long LOCK_DURATION_MS = 15 * 60 * 1000; // 15 minutes
    
    /**
     * Constructor - initialize with some stub data
     */
    public AuthenticationManager() {
        // Mock user data - in real implementation this would connect to a database
        userCredentials.put("player1", "password123");
        userCredentials.put("player2", "securePass456");
    }
    
    /**
     * Attempts to authenticate a user with provided credentials.
     * Implements the Failed Login Attempt use case logic.
     * 
     * @param username The username entered by the player
     * @param password The password entered by the player
     * @return AuthResult containing success status and relevant message
     */
    public AuthResult authenticate(String username, String password) {
        // Check if account is locked
        if (isAccountLocked(username)) {
            long remainingLockTime = (lockedAccounts.get(username) - System.currentTimeMillis()) / 1000;
            return new AuthResult(false, "Account is temporarily locked. Try again in " + 
                    remainingLockTime + " seconds.");
        }
        
        // Check if user exists
        if (!userCredentials.containsKey(username)) {
            return new AuthResult(false, "Invalid username or password.");
        }
        
        // Validate password
        if (userCredentials.get(username).equals(password)) {
            // Successful login - reset failed attempts
            failedAttempts.remove(username);
            return new AuthResult(true, "Login successful.");
        } else {
            // Failed login - increment attempt counter
            int attempts = failedAttempts.getOrDefault(username, 0) + 1;
            failedAttempts.put(username, attempts);
            
            // Check if account should be locked
            if (attempts >= MAX_ALLOWED_ATTEMPTS) {
                lockAccount(username);
                return new AuthResult(false, "Too many failed attempts. Account locked for 15 minutes.");
            }
            
            int remainingAttempts = MAX_ALLOWED_ATTEMPTS - attempts;
            return new AuthResult(false, "Invalid username or password. " + 
                    remainingAttempts + " attempts remaining before account is locked.");
        }
    }
    
    /**
     * Checks if a user account is currently locked
     * 
     * @param username The username to check
     * @return true if account is locked, false otherwise
     */
    private boolean isAccountLocked(String username) {
        if (!lockedAccounts.containsKey(username)) {
            return false;
        }
        
        long lockExpiryTime = lockedAccounts.get(username);
        if (System.currentTimeMillis() > lockExpiryTime) {
            // Lock has expired
            lockedAccounts.remove(username);
            failedAttempts.remove(username);
            return false;
        }
        
        return true;
    }
    
    /**
     * Locks a user account for the configured duration
     * 
     * @param username The username to lock
     */
    private void lockAccount(String username) {
        long lockExpiryTime = System.currentTimeMillis() + LOCK_DURATION_MS;
        lockedAccounts.put(username, lockExpiryTime);
    }
    
    /**
     * Manually reset a locked account (would be used by admin)
     * 
     * @param username The username to unlock
     * @return true if account was unlocked, false if it wasn't locked
     */
    public boolean unlockAccount(String username) {
        if (lockedAccounts.containsKey(username)) {
            lockedAccounts.remove(username);
            failedAttempts.remove(username);
            return true;
        }
        return false;
    }
    
    /**
     * Result object for authentication attempts
     */
    public static class AuthResult {
        private boolean success;
        private String message;
        
        public AuthResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
}