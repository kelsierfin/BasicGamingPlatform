package commonfiles.commands;

public abstract class GameCommand extends Command {
    public final String game;

    public GameCommand(String type, String game) {
        super("GAME", type);
        this.game = game;
    }
}
