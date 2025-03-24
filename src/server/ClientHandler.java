import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket playerSocket;
    private int matchPort;

    public ClientHandler(Socket playerSocket, int matchPort) {
        this.playerSocket = playerSocket;
        this.matchPort = matchPort;
    }

    @Override
    public void run() {
        try {
            // Create input and output streams for communication
            BufferedReader input = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            PrintWriter output = new PrintWriter(playerSocket.getOutputStream(), true);

            // Send the assigned match port to the player
            output.println("You are connected to the match on port: " + matchPort);
            
            String playerMessage;
            while ((playerMessage = input.readLine()) != null) {
                System.out.println("Message from player on match port " + matchPort + ": " + playerMessage);
                // You can send messages back to the player or handle match logic here
                output.println("Received: " + playerMessage);
            }

            // Close the connections after communication ends
            input.close();
            output.close();
            playerSocket.close();
        } catch (IOException e) {
            System.out.println("Error with player connection: " + e.getMessage());
        }
    }
}
