/**
 * Displays the leaderboard for a specific game type.
 *
 * @param gameType The type of game to display the leaderboard for (e.g., "chess", "tictactoe", "connectfour", "go")
 * TODO: Update implementation when Authentication team has uploaded the Profile class
 */
public void displayLeaderboard(String gameType) {
    // Check if parameter is valid
    if (gameType == null || gameType.isEmpty()) {
        throw new IllegalArgumentException("Game type cannot be null or empty");
    }

    // Filter rankings by game type
    List<Profile> gameTypeRankings = new ArrayList<>();
    for (Profile profile : rankings) {
        // TODO: Implement filtering by game type once Profile class is available
        // This might involve checking the profile's match history for matches of this game type
        // For now, we'll include all profiles as a placeholder
        gameTypeRankings.add(profile);
    }

    // Check if there are any rankings to display for this game type
    if (gameTypeRankings.isEmpty()) {
        System.out.println("No rankings available for game type: " + gameType);
        return;
    }

    // Sort the game-specific rankings
    // TODO: Update sorting logic once Profile class is available
    Collections.sort(gameTypeRankings, (p1, p2) -> {
        // Sort logic should be based on game-specific stats
        return 0; // Placeholder
    });

    // Display header
    System.out.println("=== Leaderboard for " + gameType + " ===");
    System.out.println("Rank\tUsername\tWins\tLosses\tWin Rate");
    System.out.println("--------------------------------------------");

    // Display each player in the rankings
    for (int i = 0; i < gameTypeRankings.size(); i++) {
        Profile profile = gameTypeRankings.get(i);
        // TODO: Once User and Profile classes are available, uncomment and adjust:
        // User user = profile.getUser();
        // String username = user.getUsername();
        // int wins = profile.getWinsForGameType(gameType);    // You'll need methods like these
        // int losses = profile.getLossesForGameType(gameType);
        // double winRate = wins > 0 ? (double)wins / (wins + losses) * 100 : 0;

        // Display with placeholder values for now
        System.out.println((i + 1) + "\t" + "Player" + i + "\t" + "0\t0\t0.0%");
    }

    System.out.println("--------------------------------------------");
}