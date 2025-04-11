package ca.ucalgary.seng.p3.client.models;

/**
 * Client-side representation of match data.
 * This class mirrors the server-side MatchData but lives on the client.
 */
public class ClientMatchData {
    private String playerUsername;
    private String opponentUsername;
    private String gameType;
    private String outcome;
    private int turns;

    public ClientMatchData(String playerUsername, String opponentUsername, String gameType, String outcome, int turns) {
        this.playerUsername = playerUsername;
        this.opponentUsername = opponentUsername;
        this.gameType = gameType;
        this.outcome = outcome;
        this.turns = turns;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }
}