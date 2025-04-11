package ca.ucalgary.seng.p3.server.leadmatch.reflection;

import ca.ucalgary.seng.p3.server.leadmatch.MatchmakingLogic;
import java.io.IOException;
import java.util.List;

/**
 * Server-side service for handling matchmaking operations.
 * This class is the implementation that will be called via reflection from the ClientHandler.
 */
public class MatchmakingService {

    /**
     * Match two players for a game.
     *
     * @param player1 First player's username
     * @param player2 Second player's username
     * @param gameType Type of game to match
     * @return MatchResult containing success status and message
     */
    public MatchResult matchPlayers(String player1, String player2, String gameType) {
        try {
            MatchmakingLogic matchmaker = new MatchmakingLogic(gameType);
            // Add both players to queue first to ensure they're in the system
            matchmaker.addPlayer(player1);
            matchmaker.addPlayer(player2);

            // Match the players
            matchmaker.matchPlayers(player1, player2);
            return new MatchResult(true, "Players matched successfully for " + gameType);
        } catch (IOException e) {
            return new MatchResult(false, "Error matching players: " + e.getMessage());
        }
    }

    /**
     * Queue a player for matchmaking.
     *
     * @param player Player looking for a match
     * @param gameType Type of game to match
     * @return QueueResult containing success status, message, and matched players if any
     */
    public QueueResult queueForGame(String player, String gameType) {
        try {
            MatchmakingLogic matchmaker = new MatchmakingLogic(gameType);
            matchmaker.playerQueuesForGame(player);

            // Note: In the actual implementation, this won't know if matching succeeded
            // because matchPlayers is void and there's no return value from playerQueuesForGame
            // In a real system, we'd need a callback or polling mechanism

            return new QueueResult(true, "Player queued for " + gameType + ". You will be matched when an opponent is found.");
        } catch (Exception e) {
            return new QueueResult(false, "Error queueing for game: " + e.getMessage());
        }
    }

    /**
     * Find a match for a player based on ELO.
     *
     * @param player Player looking for a match
     * @param gameType Type of game to match
     * @return FindMatchResult containing success status, message, and matched player if found
     */
    public FindMatchResult findMatch(String player, String gameType) {
        try {
            MatchmakingLogic matchmaker = new MatchmakingLogic(gameType);
            matchmaker.addPlayer(player);
            List<String> matchedPlayers = matchmaker.findMatch(player);

            if (matchedPlayers != null && matchedPlayers.size() == 2) {
                // A match was found
                String player1 = matchedPlayers.get(0);
                String player2 = matchedPlayers.get(1);

                // Match the players
                matchmaker.matchPlayers(player1, player2);

                return new FindMatchResult(true, "Match found", player1, player2);
            } else {
                return new FindMatchResult(false, "No suitable match found", null, null);
            }
        } catch (Exception e) {
            return new FindMatchResult(false, "Error finding match: " + e.getMessage(), null, null);
        }
    }

    // Result classes for typed responses

    public static class MatchResult {
        private final boolean success;
        private final String message;

        public MatchResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    public static class QueueResult {
        private final boolean success;
        private final String message;

        public QueueResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    public static class FindMatchResult {
        private final boolean success;
        private final String message;
        private final String player1;
        private final String player2;

        public FindMatchResult(boolean success, String message, String player1, String player2) {
            this.success = success;
            this.message = message;
            this.player1 = player1;
            this.player2 = player2;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getPlayer1() { return player1; }
        public String getPlayer2() { return player2; }
    }
}