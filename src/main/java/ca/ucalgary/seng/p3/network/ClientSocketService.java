package ca.ucalgary.seng.p3.network;

import ca.ucalgary.seng.p3.network.Request;
import ca.ucalgary.seng.p3.network.Response;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class ClientSocketService {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private final Gson gson = new Gson();

    /**
     * Sends any Request object to the server and returns the Response.
     */
    public Response sendRequest(Request request) {
        try (
                Socket socket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Send JSON-formatted request
            out.println(gson.toJson(request));

            // Read JSON-formatted response
            String jsonResponse = in.readLine();
            if (jsonResponse == null || jsonResponse.isEmpty()) {
                return new Response(false, "Server returned empty response", "", "");
            }
            return gson.fromJson(jsonResponse, Response.class);

        } catch (IOException e) {
            e.printStackTrace();
            // Return a response with the required fields: success, message, username (empty), extra (empty)
            return new Response(false, "Connection error: " + e.getMessage(), "", "");
        }
    }

    private static ClientSocketService instance = null;

    private ClientSocketService() {}

    public static ClientSocketService getInstance() {
        if (instance == null) {
            instance = new ClientSocketService();
        }
        return instance;
    }





}
