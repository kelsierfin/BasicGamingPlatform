//package ca.ucalgary.seng.p3.client.game_client;
//
//import ca.ucalgary.seng.p3.client.ClientNetworkManager;
//import ca.ucalgary.seng.p3.client.controllers.*;
//
//public class GameGuiFactory {
//    public static GameController<?> createGUI(String gameType, boolean playerOneLocal, ClientNetworkManager network) {
//        return switch (gameType.toLowerCase()) {
//            case "chess" -> new ChessController(playerOneLocal, network);
//            case "connect4" -> new Connect4Controller(playerOneLocal, network);
//            case "tictactoe" -> new TicTacToeController(playerOneLocal, network);
//            case "go" -> new GoController(playerOneLocal, network);
//            default -> throw new IllegalArgumentException("Unknown game type: " + gameType);
//        };
//    }
//}
