package server;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * Base abstract class for all network connections
 * Defines the common interface for TCP and UDP connections
 */
public abstract class Connection {
    protected SocketAddress remoteAddress;
    protected boolean isConnected = false;
    protected ProtocolType protocolType;

    public enum ProtocolType {
        TCP,
        UDP
    }

    public Connection(SocketAddress remoteAddress, ProtocolType protocolType) {
        this.remoteAddress = remoteAddress;
        this.protocolType = protocolType;
    }

    public abstract void connect() throws IOException;
    public abstract void disconnect() throws IOException;
    public abstract void send(byte[] data) throws IOException;
    public abstract byte[] receive() throws IOException;

    public boolean isConnected() {
        return isConnected;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }
}
