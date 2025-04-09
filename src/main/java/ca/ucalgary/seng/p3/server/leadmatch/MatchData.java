package ca.ucalgary.seng.p3.server.leadmatch;

// Rough code where the purpose is to get the code from the GameStats and store it to use might be reduce to redundancy in the future

public class MatchData {

    private String playerUsername;
    private String opponentUsername;
    private String gameType;
    private String outcome;
    private int turns;

    public MatchData(String playerUsername, String opponentUsername, String gameType,  String outcome, int turns) {
        this.playerUsername = playerUsername;
        this.opponentUsername = opponentUsername;
        this.gameType = gameType;
        this.outcome = outcome;
        this.turns = turns;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
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
