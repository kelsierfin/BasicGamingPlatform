package authentication;

import java.io.*;
import java.util.*;

public class UserLogin {
    private static final String FILE_NAME = "accounts.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose an option: \nLogin \nGuest Login \nRegister \nExit");
            String choice = scanner.nextLine();
            switch (choice) {
                case "Login":
                    login(scanner);
                    break;
                case "Guest Login":
                    guestLogin();
                    break;
                case "Register": //still need to add
                    break;
                case "Exit":
                    System.out.println("Exiting. See you again!");
                    scanner.close();
                    return; // Exit the program
                default:
                    System.out.println("Invalid. Please try again.");
            }
        }
    }
    private static void login(Scanner scanner) {
        Map<String, String> accounts = loadAccounts();

        if (accounts.isEmpty()) {
            System.out.println("No registered accounts found. Please register first."); //what do i even say?
            return;
        }

        while (true) {
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            if (accounts.containsKey(username) && accounts.get(username).equals(password)) {
                System.out.println("Login successful! Welcome, " + username + ".");
                return;
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
    }

    private static void guestLogin() {
        System.out.println("Guest login successful!"); //temp
    }

    private static Map<String, String> loadAccounts() {
        Map<String, String> accounts = new HashMap<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("Error: accounts.csv file not found.");
            return accounts;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    accounts.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the account database: " + e.getMessage());
        }

        return accounts;
    }
}


