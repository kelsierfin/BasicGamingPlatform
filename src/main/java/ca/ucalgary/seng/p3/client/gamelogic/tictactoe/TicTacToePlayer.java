package ca.ucalgary.seng.p3.client.gamelogic.tictactoe;

import ca.ucalgary.seng.p3.client.ClientNetworkManager;
import ca.ucalgary.seng.p3.client.controllers.GameController;
import ca.ucalgary.seng.p3.client.controllers.TicTacToeController;
import ca.ucalgary.seng.p3.client.gamelogic.GeneralPlayer;
import org.json.JSONObject;

/** For testing GameClient code.
 * Not actual implementation
 */
public class TicTacToePlayer implements GeneralPlayer<TicTacToeController> {
    TicTacToeGame game;
    ClientNetworkManager network;
    TicTacToeController gui;

    public void setNetwork(ClientNetworkManager network) {
        this.network = network;
    }

    public void setGUI(TicTacToeController gui) {
        this.gui = gui;
    }

    public boolean updateGame(JSONObject update) {
        // Update game and gui
        return game.gameOver;
    }
}
