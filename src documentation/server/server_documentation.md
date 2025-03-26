# Server-Side Network Integration

## Overview
- **Purpose**: TCP server for client connections and message exchange  
- **Scope**: Network communication layer  
- **Audience**: Networking Team, Integration Team  

## Architecture
```mermaid
flowchart LR
    Client-->|TCP|ServerSocket
    ServerSocket-->|Port 49152|Server
```

## Integration Points

### Client Connection
```java
ServerSocket serverSocket = new ServerSocket(49152);
Socket clientSocket = serverSocket.accept();
```
- **Port**: 49152 (IANA dynamic/private range)
- **Behavior**: Blocks until client connects

### Message Handling
```java
BufferedReader input = new BufferedReader(
    new InputStreamReader(clientSocket.getInputStream()));
String message = input.readLine();
```
- **Protocol**: Text-based (UTF-8)
- **Limitation**: Single message per connection

## Dependencies
- `java.net.ServerSocket`
- `java.net.Socket`

## Testing
```bash
# Manual test with netcat
nc localhost 49152
> Test message
```

| Test Case          | Expected Result          |
|--------------------|--------------------------|
| Successful connect | "Client connected" log   |
| Message received   | Server logs message      |

## Troubleshooting
| Error               | Solution                 |
|---------------------|--------------------------|
| `BindException`     | Change port number       |
| Connection drops    | Add timeout handling     |
