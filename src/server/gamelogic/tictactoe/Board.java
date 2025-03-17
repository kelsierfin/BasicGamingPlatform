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

    public void placeMarker(int space, String marker) {
        if (isMoveAllowed(space)) {
            getSpaces().set(space - offset, marker);
        } else {
            throw new IllegalMoveException();
        }
    }
    
    public void resetSpaces(int space) {
        spaces.set(space - 1, String.valueOf(space));
    }
    public List<String> getSpaces() {
        return spaces;
    }
    
    public List<Integer> getAvailableSpaces() {
        List<Integer> availableSpaces = new ArrayList<>();
        for (int i = offset; i < getSpaces().size() + offset; i++) {
            if (isSpaceAvailable(i)) {
                availableSpaces.add(i);
            }
        }
        return availableSpaces;
    }
}