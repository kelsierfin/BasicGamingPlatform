package ca.ucalgary.seng.p3.client.gamelogic.chess;

import java.util.Random;

public class PlayerPair {
    final ChessPlayer whitePlayer;
    final ChessPlayer blackPlayer;
    ChessPlayer otherPlayer;

    public PlayerPair(ChessGame game) {
        this.whitePlayer = new ChessPlayer(game, true);
        this.blackPlayer = new ChessPlayer(game, false);
        whitePlayer.opponent = blackPlayer;
        blackPlayer.opponent = whitePlayer;
    }

    public ChessPlayer getRandomPlayer() {
        Random random = new Random();
        if (random.nextBoolean()) {
            otherPlayer = blackPlayer;
            return whitePlayer;
        }
        else {
            otherPlayer = whitePlayer;
            return blackPlayer;
        }
    }

    public ChessPlayer getOtherPlayer() {
        return otherPlayer;
    }
}
