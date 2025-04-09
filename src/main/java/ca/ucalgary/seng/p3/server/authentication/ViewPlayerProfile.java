//package ca.ucalgary.seng.p3.server.authentication;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ViewPlayerProfile {
//    private static final String ACCOUNTS_FILE = "accounts.csv";
//    private String username;
//    private String email;
//    private MatchHistory matchHistory;
//
//    public ViewPlayerProfile(String username, MatchHistory matchHistory) {
//        this.username = username;
//        this.matchHistory = matchHistory;
//        loadProfile();
//    }
//
//    private void loadProfile() {
//        Map<String, String[]> accounts = new HashMap<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 3) {
//                    accounts.put(parts[0], new String[]{parts[1], parts[2]});
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Error loading account data: " + e.getMessage());
//            return;
//        }
//
//        if (accounts.containsKey(username)) {
//            this.email = accounts.get(username)[1];
//        } else {
//            System.out.println("User not found in the database.");
//        }
//    }
//
//    public void displayProfile() {
//        if (email == null) {
//            System.out.println("Profile not available for user: " + username);
//            return;
//        }
//
//        System.out.println("\n=== Player Profile ===");
//        System.out.println("Username: " + username);
//        System.out.println("Email: " + email);
//        System.out.println("=====================");
//
//        // Create a temporary GameStats object to represent the player for match history lookup
//        GameStats playerStats = new GameStats(username);
//        matchHistory.displayMatchHistoryForPlayer(playerStats);
//    }
//
//    // Inner class to represent player stats for match history lookup
//    private static class GameStats {
//        private String username;
//
//        public GameStats(String username) {
//            this.username = username;
//        }
//
//        public String getUsername() {
//            return username;
//        }
//
//        // Dummy implementations for match history compatibility
//        public int getELO() {
//            return 0; // Actual implementation would fetch from database
//        }
//
//        public int getGamesPlayed() {
//            return 0; // Actual implementation would fetch from database
//        }
//
//        public int getGamesWon() {
//            return 0; // Actual implementation would fetch from database
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (this == obj) return true;
//            if (obj == null || getClass() != obj.getClass()) return false;
//            GameStats gameStats = (GameStats) obj;
//            return username.equals(gameStats.username);
//        }
//
//        @Override
//        public int hashCode() {
//            return username.hashCode();
//        }
//    }
//    public class Main {
//        public static void main(String[] args) {
//            // Initialize match history
//            MatchHistory matchHistory = new MatchHistory();
//
//            // Create and display profile for a player
//            ViewPlayerProfile profile = new ViewPlayerProfile("someUsername", matchHistory);
//            profile.displayProfile();
//        }
//    }
//}