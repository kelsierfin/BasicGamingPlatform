package ca.ucalgary.seng.p3.network;

public class Response {
    private boolean success;
    private String message;
    private String username;
    private String extra; // Used for MFA or any extra data

    public Response(boolean success, String message, String username, String extra) {
        this.success = success;
        this.message = message;
        this.username = username;
        this.extra = extra;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {  // Getter for message
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getExtra() {
        return extra;
    }
}
