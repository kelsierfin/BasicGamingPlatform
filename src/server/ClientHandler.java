package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Set up input and output streams for client communication
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Ask the client for a username
            out.println("Enter your username: ");
            this.username = in.readLine();

            // Check if the username is unique
            synchronized (TCPserver.clients) {
                for (String client : TCPserver.usernames) {
                    if (client.equals(this.username)) {
                        out.println("Username already taken. Please choose a different one.");
                        socket.close();  // Reject the connection if username is taken
                        return;
                    }
                }
                // If username is unique, add this client to the list
                TCPserver.clients.add(this);
                TCPserver.usernames.add(username);
            }

            // Notify the client that they have successfully joined
            out.println("Welcome " + username + "! You can start chatting.");
            TCPserver.broadcast(username + " has joined the chat!", this);

            // Handle messages from the client
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                TCPserver.broadcast(username + ": " + message, this); // Prefix messages with the username
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void closeConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            TCPserver.removeClient(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
