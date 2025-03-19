public class Board {
    Stone[][] matrix = new Stone[19][19];
    //If the Game can still continue,state is 0
    Stone blank = new Stone(0);
    Stone black = new Stone(1);
    Stone white = new Stone(2);
    Stone black_eaten = new Stone(3);
    Stone white_eaten = new Stone(4);
    int state = 0;
    private void initialize(Board b){
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                matrix[i][j] = blank;
            }
        }
    }
    //This method used to check if there are group of stones surrounded by enemy stones
    //If so, clear and replace them with eaten stones(occupied block but shown empty).
    private void checkSurround() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (matrix[i][j].getColor() == 1 || matrix[i][j].getColor() == 2) {
                    if (isSurrounded(i, j, matrix[i][j].getColor())) {
                        clearGroup(i, j, matrix[i][j].getColor());
                    }
                }
            }
        }
    }

    private boolean isSurrounded(int x, int y, int color) {
        // Implement flood fill or similar algorithm to check if the group is surrounded
        // This is a simplified version, you may need to implement a more complex logic
        boolean[][] visited = new boolean[19][19];
        return dfs(x, y, color, visited);
    }
    //Asked AI for help, this method was really hard and I struggled for more than 1 hour
    //with no result.This method is used to check if a block of stones got surrounded by enemies.
    //If so, clarify the area and make the area forbidden to be placed.
    private boolean dfs(int x, int y, int color, boolean[][] visited) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19 || visited[x][y]) {
            return true;
        }
        if (matrix[x][y].getColor() == 0) {
            return false;
        }
        if (matrix[x][y].getColor() != color) {
            return true;
        }
        visited[x][y] = true;
        return dfs(x + 1, y, color, visited) &&
                dfs(x - 1, y, color, visited) &&
                dfs(x, y + 1, color, visited) &&
                dfs(x, y - 1, color, visited);
    }

    private void clearGroup(int x, int y, int color) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19 || matrix[x][y].getColor() != color) {
            return;
        }
        matrix[x][y] = (color == 1) ? black_eaten : white_eaten;
        clearGroup(x + 1, y, color);
        clearGroup(x - 1, y, color);
        clearGroup(x, y + 1, color);
        clearGroup(x, y - 1, color);
    }

    //This method should be done each time when a player placed a stone
    private void checkState(Board b){

    }
    private void showAvailablePlacement(Player p){
        
    }
    public void placeStone(int x, int y, int color) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19 || matrix[x][y].getColor() != 0) {
            System.out.println("Invalid move");
            return;
        }
        matrix[x][y] = (color == 1) ? black : white;
        checkSurround();
        checkState(this);
    }
    public void surrender(int surrenderingTeam) {
        state = 3;
        int winner = (surrenderingTeam == 1) ? 2 : 1;
        System.out.println("player " + surrenderingTeam + " surrendered.");
    }
}
