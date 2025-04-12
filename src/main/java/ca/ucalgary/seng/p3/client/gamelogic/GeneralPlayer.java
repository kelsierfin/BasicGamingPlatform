package ca.ucalgary.seng.p3.client.gamelogic;

import ca.ucalgary.seng.p3.client.ClientNetworkManager;
import ca.ucalgary.seng.p3.client.controllers.GameController;

import org.json.JSONObject;

public interface GeneralPlayer<T extends GameController> {

    void setNetwork(ClientNetworkManager network);

    void setGUI(T gui);

    /**
     * Updates the game state with the new move.
     * Returns true if this move ends the game.
     */
    boolean updateGame(JSONObject move);
}