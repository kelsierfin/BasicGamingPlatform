package ca.ucalgary.seng.p3.server;

import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ServerNetworkManager {
    private ServerSocket serverSocket;
    private ExecutorService clientHandlerPool;
    private boolean isRunning = false;

    public void start(int port, Consumer<JSONObject> messageHandler) throws IOException {
        serverSocket = new ServerSocket(port);
        clientHandlerPool = Executors.newCachedThreadPool();
        isRunning = true;

        System.out.println("Server started on port " + port);

        // Accept incoming connections on a dedicated thread.
        new Thread(() -> {
            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clientHandlerPool.submit(() -> handleClient(clientSocket, messageHandler));
                } catch (IOException e) {
                    if (isRunning) {
                        System.err.println("Server accept error: " + e.getMessage());
                    }
                }
            }
        }).start();
    }

    private void handleClient(Socket clientSocket, Consumer<JSONObject> messageHandler) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                JSONObject message = new JSONObject(inputLine);
                JSONObject wrapper = new JSONObject();
                wrapper.put("socket", clientSocket);
                wrapper.put("data", message);
                messageHandler.accept(wrapper);
            }
        } catch (IOException e) {
            System.err.println("Client handling error (" + clientSocket.getRemoteSocketAddress() + "): " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (clientHandlerPool != null) {
                clientHandlerPool.shutdown();
            }
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }
}
