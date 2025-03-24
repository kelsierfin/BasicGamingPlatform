import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    private static int nextPort = 49153;  // Starting port for assigning to players

    public static void main(String[] args) {
        try {
            //Create a server socket bound to port 49152.
            ServerSocket serverSocket = new ServerSocket(49152);
            System.out.println("Server is online, waiting for client connection......");

            while (true) {
                // Wait for two clients to connect
                Socket player1Socket = serverSocket.accept();
                System.out.println("Player 1 connected.");

                Socket player2Socket = serverSocket.accept();
                System.out.println("Player 2 connected.");

                // Assign a unique port for the match between Player 1 and Player 2
                int matchPort = nextPort++;
                System.out.println("Assigning match port: " + matchPort);

                // Create threads to handle each player's communication
                new Thread(new ClientHandler(player1Socket, matchPort)).start();
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}