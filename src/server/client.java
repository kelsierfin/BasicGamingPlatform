import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) {
        try {
            // Connect to the server at localhost on port 49152
            Socket socket = new Socket("localhost", 49152);

            // Create input and output streams for communication
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Receive the match port from the server
            String serverMessage = serverInput.readLine();
            System.out.println(serverMessage);

            // Now that the match port is received, connect to that port (dynamic connection)
            int matchPort = Integer.parseInt(serverMessage.split(": ")[1]);
            socket = new Socket("localhost", matchPort);  // Connect to the match port

            // Communicate on the match port
            BufferedReader matchInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter matchOutput = new PrintWriter(socket.getOutputStream(), true);

            String message;
            while (true) {
                System.out.print("Enter message to server: ");
                message = input.readLine();
                
                // Send message to server
                matchOutput.println(message);

                // Receive and print server's response
                String matchMessage = matchInput.readLine();
                System.out.println("Match: " + matchMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
