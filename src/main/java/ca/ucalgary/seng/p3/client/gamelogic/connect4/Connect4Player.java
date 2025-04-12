package ca.ucalgary.seng.p3.client.gamelogic.connect4;

import ca.ucalgary.seng.p3.client.ClientNetworkManager;
import ca.ucalgary.seng.p3.client.controllers.Connect4Controller;
import ca.ucalgary.seng.p3.client.gamelogic.GeneralPlayer;
import org.json.JSONObject;

/** For testing GameClient code.
 * Not actual implementation
 */
public class Connect4Player implements GeneralPlayer<Connect4Controller> {
    Connect4Game game;
    ClientNetworkManager network;
    Connect4Controller gui;

    public void setNetwork(ClientNetworkManager network) {
        this.network = network;
    }

    public void setGUI(Connect4Controller gui) {
        this.gui = gui;
    }

    public boolean updateGame(JSONObject update) {
        // Update game and gui
        return game.gameOver;
    }
}
