// NetworkTestServer.java
package server;

import java.io.*;
import java.net.*;

public class NetworkTestServer {
    public static void main(String[] args) throws IOException {
        // Start TCP Server
        new Thread(() -> {
            try (ServerSocket tcpSocket = new ServerSocket(8080)) {
                System.out.println("TCP Server ready on port 8080");
                while (true) {
                    Socket client = tcpSocket.accept();
                    new Thread(() -> {
                        try {
                            BufferedReader in = new BufferedReader(
                                new InputStreamReader(client.getInputStream()));
                            PrintWriter out = new PrintWriter(
                                client.getOutputStream(), true);
                            
                            String input;
                            while ((input = in.readLine()) != null) {
                                System.out.println("TCP Received: " + input);
                                out.println("TCP Response: " + input);
                            }
                        } catch (IOException e) {
                            System.err.println("TCP Client handling error: " + e.getMessage());
                        } finally {
                            try { client.close(); } catch (IOException e) {}
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Start UDP Server
        new Thread(() -> {
            try (DatagramSocket udpSocket = new DatagramSocket(9090)) {
                System.out.println("UDP Server ready on port 9090");
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    udpSocket.receive(packet);
                    
                    String received = new String(
                        packet.getData(), 0, packet.getLength());
                    System.out.println("UDP Received: " + received);
                    
                    byte[] response = ("UDP Response: " + received).getBytes();
                    udpSocket.send(new DatagramPacket(
                        response, response.length, 
                        packet.getAddress(), packet.getPort()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}