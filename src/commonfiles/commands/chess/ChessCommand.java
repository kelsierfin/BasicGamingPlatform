package commonfiles.commands.chess;

import commonfiles.commands.GameCommand;

public abstract class ChessCommand extends GameCommand {
    public ChessCommand(String type) {
        super(type, "CHESS");
    }
}
