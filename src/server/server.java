import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    public static void main(String[] args) {
        try {
            //Create a server socket bound to port 49152.
            ServerSocket serverSocket = new ServerSocket(49152);
            System.out.println("Server is online, waiting for client connection......");

            //Client connection await.
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected, start communication......");

            //Input and output stream.
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream());

            //Read the message from the client.
            String clientMessage = input.readLine();
            System.out.println("Client message: " + clientMessage);

            //Close the connection.
            input.close();
            output.close();
            clientSocket.close();
            serverSocket.close();
            System.out.println("Server closed!");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}