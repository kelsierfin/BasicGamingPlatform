package ca.ucalgary.seng.p3.client.game_to_gui_commands.chess;

public class Deselect implements GameToGuiCommand {
    public String squareToDeselect;      // Ex: b6, h4       May not be necessary if GUI keeps track of selected square

    // Deselect the selected square if there is one. Do nothing otherwise.
}
