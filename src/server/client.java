import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) {
        try {
            // Connect to the server at localhost on port 12345
            Socket socket = new Socket("localhost", 49152);

            // Create input and output streams for communication
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;

            // Keep reading user input and sending it to the server
            while (true) {
                System.out.print("Enter message to server: ");
                message = input.readLine();
                
                // Send message to server
                output.println(message);
                
                // Receive and print server's response
                String serverMessage = serverInput.readLine();
                System.out.println("Server: " + serverMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
