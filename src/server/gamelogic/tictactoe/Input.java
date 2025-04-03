import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Input {
    private final Scanner input;
    private final Print print;

    public Input(Scanner input, Print print) {
        this.input = input;
        this.print = print;
    }

    public int validateSelection(List<Integer> options) {
        int selection = getInteger();
        if (options.contains(selection)) {
            return selection;
        } else {
            print.invalidSelection();
            return validateSelection(options);
        }
    }
    public List<Integer> validSelection(List<? extends Menu> options) {
        List<Integer> selection = new ArrayList<>();
        for (int i = 1; i <= options.size() + 1; i++) {
            selection.add(i);
        }
        return selection;
    }
    int getInteger() {
        try {
            return input.nextInt();
        } catch (InputMismatchException e) {
            print.invalidSelection();
            input.nextLine();
            return getInteger();
        }
    }
}
