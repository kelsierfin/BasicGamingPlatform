package commonfiles.commands;

public abstract class Command {
    public final String context;
    public final String type;

    public Command(String context, String type) {
        this.context = context;
        this.type = type;
    }
}
