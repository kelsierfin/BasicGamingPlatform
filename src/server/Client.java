package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Client {
    private static String username;
    private static Connection connection;

    public static void main(String[] args) {
        System.out.println("Starting network test...");
        
        // Get username from user
        username = getUsername();
        System.out.println("Welcome, " + username + "!");
        
        // Get connection type from user
        Connection.ProtocolType protocolType = getConnectionType();
        
        NetworkManager manager = new NetworkManager();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("\nShutting down gracefully...");
                manager.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        try (Scanner scanner = new Scanner(System.in)) {
            // Create connection based on user choice
            connection = setupConnection(manager, protocolType);

            if (connection == null) {
                System.err.println("Failed to establish connection. Exiting...");
                return;
            }

            System.out.println("\nConnected using " + protocolType + ". Start sending messages!");
            System.out.println("Type 'exit' to quit or 'change' to switch connection type.\n");

            // Message loop
            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine().trim();

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting...");
                    break;
                } else if (message.equalsIgnoreCase("change")) {
                    connection.disconnect();
                    protocolType = getConnectionType();
                    connection = setupConnection(manager, protocolType);
                    if (connection == null) {
                        System.err.println("Failed to establish new connection. Exiting...");
                        break;
                    }
                    continue;
                }

                if (!message.isEmpty()) {
                    sendMessage(manager, connection, protocolType, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                manager.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getUsername() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter your username: ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Username cannot be empty. Please try again.");
        }
    }

    private static Connection.ProtocolType getConnectionType() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nChoose connection type:");
            System.out.println("1. TCP");
            System.out.println("2. UDP");
            System.out.print("Enter choice (1-2): ");
            
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    return Connection.ProtocolType.TCP;
                case "2":
                    return Connection.ProtocolType.UDP;
                default:
                    System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }

    private static Connection setupConnection(NetworkManager manager, Connection.ProtocolType protocolType) {
        try {
            int port = protocolType == Connection.ProtocolType.TCP ? 8080 : 9090;
            InetSocketAddress address = new InetSocketAddress("localhost", port);
            Connection conn = manager.createConnection(address, protocolType);
            
            System.out.println("Attempting " + protocolType + " connection...");
            manager.establishConnection(conn);
            System.out.println(protocolType + " connection established!");
            
            // Set up receiver
            manager.receiveAsync(conn, data -> {
                System.out.println("\nServer response: " + new String(data).trim());
                System.out.print("Enter message: ");
            });
            
            return conn;
        } catch (Exception e) {
            System.err.println(protocolType + " Error: " + e.getMessage());
            return null;
        }
    }

    private static void sendMessage(NetworkManager manager, Connection connection,
                                  Connection.ProtocolType protocolType, String message) {
        try {
            String formattedMessage = String.format("[%s]: %s", username, message);
            if (protocolType == Connection.ProtocolType.TCP) {
                formattedMessage += "\n"; // Add newline for TCP
            }
            
            manager.sendAsync(connection, formattedMessage.getBytes());
            System.out.println("Message sent!");
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}