package ca.ucalgary.seng.p3.client.game_to_gui_commands.chess;

import java.util.ArrayList;

public class Select implements GameToGuiCommand {
    public String squareToDeselect;      // Ex: b6, h4       May not be necessary if GUI keeps track of selected square
    public ArrayList<String> moveOptions;       // Squares to highlight as move options for the newly selected piece
}
