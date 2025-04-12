package ca.ucalgary.seng.p3.client.gamelogic.go;

import ca.ucalgary.seng.p3.client.ClientNetworkManager;
import ca.ucalgary.seng.p3.client.controllers.GoController;
import ca.ucalgary.seng.p3.client.gamelogic.GeneralPlayer;
import org.json.JSONObject;

/** For testing GameClient code.
 * Not actual implementation
 */
public class GoPlayer implements GeneralPlayer<GoController> {
    GoGame game;
    ClientNetworkManager network;
    GoController gui;

    public void setNetwork(ClientNetworkManager network) {
        this.network = network;
    }

    public void setGUI(GoController gui) {
        this.gui = gui;
    }

    public boolean updateGame(JSONObject update) {
        // Update game and gui
        return game.gameOver;
    }
}
