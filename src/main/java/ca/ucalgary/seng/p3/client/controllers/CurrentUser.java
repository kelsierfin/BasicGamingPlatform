package ca.ucalgary.seng.p3.client.controllers;

public class CurrentUser {

    private static CurrentUser instance;

    String username;

    public static CurrentUser getInstance(String username) {
        if (instance == null) {
            instance = new CurrentUser(username);
        }
        return instance;
    }

    public CurrentUser(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}