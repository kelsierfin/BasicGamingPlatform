package server;

import java.io.*;
import java.net.*;

public class TCPConnection extends Connection {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private volatile boolean closing = false;

    public TCPConnection(SocketAddress remoteAddress) {
        super(remoteAddress, ProtocolType.TCP);
    }

    @Override
    public synchronized void connect() throws IOException {
        if (isConnected || closing) return;
        this.socket = new Socket();
        this.socket.setSoTimeout(5000); // 5 second timeout
        this.socket.connect(remoteAddress);
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.isConnected = true;
    }

    @Override
    public synchronized void disconnect() throws IOException {
        if (!isConnected || closing) return;
        closing = true;
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } finally {
            isConnected = false;
            closing = false;
        }
    }

    @Override
    public synchronized void send(byte[] data) throws IOException {
        checkConnectionState();
        outputStream.write(data);
        outputStream.flush();
    }

    @Override
    public synchronized byte[] receive() throws IOException {
        checkConnectionState();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        int bytesRead;
        
        while ((bytesRead = inputStream.read(temp)) != -1) {
            buffer.write(temp, 0, bytesRead);
            if (inputStream.available() == 0) break;
        }
        return buffer.toByteArray();
    }

    private void checkConnectionState() throws IOException {
        if (!isConnected || closing || socket.isClosed()) {
            throw new IOException("Connection is not active");
        }
    }
}