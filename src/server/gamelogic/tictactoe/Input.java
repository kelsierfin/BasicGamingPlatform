import java.util.List;

public class Input {
    private Integer selectedMove = null;
    private String message = "";

    public void setSelectedMove(int move) {
        this.selectedMove = move;
    }

    public int validateSelection(List<Integer> validOptions) {
        while (selectedMove == null || !validOptions.contains(selectedMove)) {
            Thread.onSpinWait();
        }
        int move = selectedMove;
        selectedMove = null;
        return move;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
