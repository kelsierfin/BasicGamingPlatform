package server;

import java.io.*;
import java.net.*;

public class UDPConnection extends Connection {
    private DatagramSocket socket;
    private volatile boolean closing = false;

    public UDPConnection(SocketAddress remoteAddress) {
        super(remoteAddress, ProtocolType.UDP);
    }

    @Override
    public synchronized void connect() throws IOException {
        if (isConnected || closing) return;
        this.socket = new DatagramSocket();
        this.socket.setSoTimeout(5000); // 5 second timeout
        this.socket.connect(remoteAddress);
        this.isConnected = true;
    }

    @Override
    public synchronized void disconnect() throws IOException {
        if (!isConnected || closing) return;
        closing = true;
        try {
            if (socket != null) socket.close();
        } finally {
            isConnected = false;
            closing = false;
        }
    }

    @Override
    public synchronized void send(byte[] data) throws IOException {
        checkConnectionState();
        DatagramPacket packet = new DatagramPacket(data, data.length);
        socket.send(packet);
    }

    @Override
    public synchronized byte[] receive() throws IOException {
        checkConnectionState();
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return packet.getData();
    }

    private void checkConnectionState() throws IOException {
        if (!isConnected || closing || socket.isClosed()) {
            throw new IOException("Connection is not active");
        }
    }
}