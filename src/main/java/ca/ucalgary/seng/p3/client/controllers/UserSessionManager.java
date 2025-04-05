package ca.ucalgary.seng.p3.client.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserSessionManager {
    private static final List<String> onlineUsers = new CopyOnWriteArrayList<>();
    private static String currentUser = null;

    // Add a user to the online list and set as current user
    public static void addUser(String username) {
        if (!onlineUsers.contains(username)) {
            onlineUsers.add(username);
            currentUser = username; // Set the current user to the logged-in user
        }
    }

    // Remove a user from the online list and clear current user if they are logged out
    public static void removeUser(String username) {
        if (onlineUsers.contains(username)) {
            onlineUsers.remove(username);
            if (currentUser != null && currentUser.equals(username)) {
                currentUser = null; // Clear current user if they logged out
            }
        }
    }

    // Check if a user is currently online
    public static boolean isUserOnline(String username) {
        return onlineUsers.contains(username);
    }

    // Get the list of online users
    public static List<String> getOnlineUsers() {
        return new ArrayList<>(onlineUsers); // return a copy to avoid external modification
    }

    // Clear all online users (used for example when closing the application)
    public static void clearAllUsers() {
        onlineUsers.clear();
        currentUser = null;
    }

    // Get the currently logged-in user
    public static String getCurrentUser() {
        return currentUser;
    }

    // Set the current user (used if needed in some logic)
    public static void setCurrentUser(String username) {
        currentUser = username;
    }
}