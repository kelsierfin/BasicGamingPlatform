package ca.ucalgary.seng.p3.client;

import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ClientNetworkManager {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread listenerThread;

    public void connect(String host, int port, Consumer<JSONObject> messageHandler) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("Connected to server at " + host + ":" + port);

        // Start listening for server responses
        listenerThread = new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    JSONObject json = new JSONObject(response);
                    messageHandler.accept(json);
                }
            } catch (IOException e) {
                if (!socket.isClosed()) {
                    System.err.println("Connection error: " + e.getMessage());
                }
            }
        });
        listenerThread.start();
    }

    public void sendMessage(JSONObject message) throws IOException {
        if (out == null) {
            throw new IOException("Not connected to server");
        }
        out.println(message.toString());
    }

    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (listenerThread != null) {
                listenerThread.interrupt();
            }
            System.out.println("Disconnected from server");
        } catch (IOException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }
}
