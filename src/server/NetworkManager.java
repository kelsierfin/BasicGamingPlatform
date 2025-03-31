package server;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.*;

public class NetworkManager {
    private final Set<Connection> activeConnections = ConcurrentHashMap.newKeySet();
    private final ExecutorService connectionPool = Executors.newCachedThreadPool();
    private volatile boolean shuttingDown = false;

    public Connection createConnection(SocketAddress remoteAddress, Connection.ProtocolType protocolType) {
        if (shuttingDown) {
            throw new IllegalStateException("Manager is shutting down");
        }
        
        Connection connection = protocolType == Connection.ProtocolType.TCP ?
                new TCPConnection(remoteAddress) : new UDPConnection(remoteAddress);
        activeConnections.add(connection);
        return connection;
    }

    public void establishConnection(Connection connection) throws IOException {
        if (shuttingDown) {
            throw new IllegalStateException("Manager is shutting down");
        }
        connection.connect();
    }

    public void closeConnection(Connection connection) throws IOException {
        if (connection != null) {
            connection.disconnect();
            activeConnections.remove(connection);
        }
    }

    public void sendAsync(Connection connection, byte[] data) {
        if (shuttingDown) return;
        
        connectionPool.execute(() -> {
            try {
                if (connection.isConnected()) {
                    connection.send(data);
                }
            } catch (IOException e) {
                handleConnectionError(connection, e, "send");
            }
        });
    }

    public void receiveAsync(Connection connection, DataCallback callback) {
        if (shuttingDown) return;
        
        connectionPool.execute(() -> {
            try {
                if (connection.isConnected()) {
                    byte[] data = connection.receive();
                    if (data != null && data.length > 0) {
                        callback.onDataReceived(data);
                    }
                    // Continue receiving if still connected
                    if (connection.isConnected() && !shuttingDown) {
                        receiveAsync(connection, callback);
                    }
                }
            } catch (SocketTimeoutException e) {
                if (connection.isConnected() && !shuttingDown) {
                    receiveAsync(connection, callback);
                }
            } catch (IOException e) {
                handleConnectionError(connection, e, "receive");
            }
        });
    }

    private void handleConnectionError(Connection connection, IOException e, String operation) {
        System.err.println("Error during " + operation + ": " + e.getMessage());
        try {
            closeConnection(connection);
        } catch (IOException ex) {
            System.err.println("Error closing connection: " + ex.getMessage());
        }
    }

    public synchronized void shutdown() throws IOException {
        shuttingDown = true;
        
        // Shutdown thread pool
        connectionPool.shutdown();
        try {
            if (!connectionPool.awaitTermination(2, TimeUnit.SECONDS)) {
                connectionPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            connectionPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        // Close all connections
        for (Connection connection : activeConnections) {
            try {
                connection.disconnect();
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        activeConnections.clear();
    }

    public interface DataCallback {
        void onDataReceived(byte[] data);
    }
}