package ca.ucalgary.seng.p3.server.authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {

    // Generate a random salt for each password
    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // Hash the password with SHA-256 and the provided salt
    public static String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Combine the salt and hashed password for storage
    public static String generateStorablePassword(String password) {
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        String saltString = Base64.getEncoder().encodeToString(salt);
        return saltString + ":" + hashedPassword;
    }

    // Verify a password against a stored hash
    public static boolean verifyPassword(String password, String storedPassword) {
        String[] parts = storedPassword.split(":");
        if (parts.length != 2) return false;

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        String hashedPassword = hashPassword(password, salt);
        return hashedPassword.equals(parts[1]);
    }
}