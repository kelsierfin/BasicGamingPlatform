public class Board {
    int numberOfRows;
    int totalSpaces;
    int offset;
    List<String> spaces;

    public Board(int numberOfRows) {
        this.numberOfRows = numberOfRows;
        this.totalSpaces = numberOfRows * numberOfRows;
        this.spaces = new ArrayList<>(totalSpaces);
        this.offset = 1;
        assignValuesToSpaces();
    }
}