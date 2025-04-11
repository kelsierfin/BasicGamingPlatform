package ca.ucalgary.seng.p3.network;

import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;

public class SimpleClient {
    public static void main(String[] args) {
        Gson gson = new Gson();

        try (
                Socket socket = new Socket("localhost", 5000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // TEST SCENARIO 1: Successful Login
            System.out.println("Testing lock");
            Request req = new Request("login", "Lianne", "1234");  // Change username/password as needed

            // Send login request
            String jsonRequest = gson.toJson(req);
            out.println(jsonRequest);

            // Receive response from the server
            String jsonResponse = in.readLine();
            Response response = gson.fromJson(jsonResponse, Response.class);

            // Print the server response
            System.out.println("Server response: " + response.getMessage());

            // Check if the response indicates successful login
            // Check if login was successful
            if (response.isSuccess()) {
                System.out.println("Login was successful.");
            } else {
                System.out.println("Login failed.");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
