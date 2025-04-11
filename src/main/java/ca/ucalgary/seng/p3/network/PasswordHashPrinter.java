package ca.ucalgary.seng.p3.network;
import ca.ucalgary.seng.p3.server.authentication.PasswordHasher;

public class PasswordHashPrinter {
    public static void main(String[] args) {
        String username = "Lianne";
        String password = "1234";
        String email = "lianne@example.com";
        String status = "enabled";

        // Generate salt:hash from your hasher
        String hashed = PasswordHasher.generateStorablePassword(password);

        // Format as a full CSV entry
        String csvLine = username + "," + hashed + "," + email + "," + status;

        // Print it out so you can copy-paste
        System.out.println("Paste this line into accounts.csv:");
        System.out.println(csvLine);
    }
}
