//package ca.ucalgary.seng.p3.client.game_client;
//
//import ca.ucalgary.seng.p3.client.gamelogic.GameInterface;
//import ca.ucalgary.seng.p3.client.gamelogic.chess.ChessGame;
//import ca.ucalgary.seng.p3.client.gamelogic.connect4.Connect4Game;
//import ca.ucalgary.seng.p3.client.gamelogic.go.GoGame;
//import ca.ucalgary.seng.p3.client.gamelogic.tictactoe.TicTacToeGame;
//
//public class GameFactory {
//    public static GameInterface createGame(String gameType) {
//        return switch (gameType.toLowerCase()) {
//            case "chess" -> new ChessGame();
//            case "connect4" -> new Connect4Game();
//            case "tictactoe" -> new TicTacToeGame();
//            case "go" -> new GoGame();
//            default -> throw new IllegalArgumentException("Unknown game type: " + gameType);
//        };
//    }
//}
