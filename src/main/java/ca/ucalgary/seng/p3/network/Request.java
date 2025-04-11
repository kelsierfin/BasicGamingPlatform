package ca.ucalgary.seng.p3.network;

public class Request {
    private String action;
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String extra;

    // Constructor for sign-up
    public Request(String action, String username, String password, String confirmPassword, String email) {
        this.action = action;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
    }

    // Constructor for most operations
    public Request(String action, String username, String password) {
        this.action = action;
        this.username = username;
        this.password = password;
    }

    // Constructor with extra data field
    public Request(String action, String username, String password, String extra) {
        this.action = action;
        this.username = username;
        this.password = password;
        this.extra = extra;
    }

    public String getAction() {
        return action;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getExtra() {
        return extra;
    }
}