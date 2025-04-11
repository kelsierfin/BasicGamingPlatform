package ca.ucalgary.seng.p3.network;

import ca.ucalgary.seng.p3.server.authentication.PasswordHasher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NetworkAuthenticationService {


    public boolean login(String username, String password) {
        String filePath = "src/main/resources/database/accounts.csv";
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("Account file not found: " + filePath);
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                String storedUsername = parts[0].trim();
                String storedPassword = parts[1].trim();

                if (storedUsername.equals(username)) {
                    return PasswordHasher.verifyPassword(password, storedPassword);
                }
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
