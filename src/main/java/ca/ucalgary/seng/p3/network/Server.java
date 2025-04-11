package ca.ucalgary.seng.p3.network;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server has started for our Game Platform and is running on port 5000...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New game client just connected: " + clientSocket);

                // ⬇ Start a new thread for each client
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
