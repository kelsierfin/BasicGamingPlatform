package ca.ucalgary.seng.p3.client.gamelogic.go;

import java.util.ArrayList;
import java.util.List;

public class Board {
    Stone[][] matrix = new Stone[19][19];
    //If the Game can still continue,state is 0
    Stone blank = new Stone(0);
    Stone black = new Stone(1);
    Stone white = new Stone(2);
    Stone black_eaten = new Stone(3);
    Stone white_eaten = new Stone(4);

    int state = 0; // 0=game ongoing 1=black win 2=white win 3=surrender.
    int consecutivePass = 0;
    public void initialize(Board b){
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                matrix[i][j] = blank;
            }
        }
    }
    //This method used to check if there are group of stones surrounded by enemy stones
    //If so, clear and replace them with eaten stones(occupied block but shown empty).
    public void checkSurround() {
        boolean[][] checked = new boolean[19][19];
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                int color = matrix[i][j].getColor();
                if ((color == 1 || color == 2) && !checked[i][j]) {
                    boolean[][] visited = new boolean[19][19];
                    if (!hasLiberty(i, j, color, visited)) {
                        clearGroup(i, j, color);
                        checked[i][j] = true;
                    }
                }
            }
        }
    }

    private boolean isSurrounded(int x, int y, int color) {
        boolean[][] visited = new boolean[19][19];
        return dfs(x, y, color, visited);
    }

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

    //Clear the stones which got surrounded.
    private void clearGroup(int x, int y, int color) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19) return;
        if (matrix[x][y].getColor() != color) return;

        matrix[x][y] = (color == 1) ? black_eaten : white_eaten;
        clearGroup(x+1, y, color);
        clearGroup(x-1, y, color);
        clearGroup(x, y+1, color);
        clearGroup(x, y-1, color);
    }
//This method is used to show all available position for a player to place stone.Implement it when designing GUI.
    public List<int[]> getAvailablePositions() {
        List<int[]> available = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (matrix[i][j].getColor() == 0) {
                    available.add(new int[]{i, j});
                }
            }
        }
        return available;
    }

    // New methods written by AI.Calculate area and check winner
    private int calculateWinner() {
        int blackTerritory = 0;
        int whiteTerritory = 0;
        boolean[][] visited = new boolean[19][19];
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                int color = matrix[i][j].getColor();
                if (color == 1) blackTerritory++;
                else if (color == 2) whiteTerritory++;
                else if (color == 3) blackTerritory++;
                else if (color == 4) whiteTerritory++;
            }
        }

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (matrix[i][j].getColor() == 0 && !visited[i][j]) {
                    TerritoryResult result = calculateTerritory(i, j, visited);
                    if (result.isBlackTerritory) blackTerritory += result.area;
                    if (result.isWhiteTerritory) whiteTerritory += result.area;
                }
            }
        }

        System.out.println("Black area: " + blackTerritory + ",White area: " + whiteTerritory);
        return (blackTerritory > whiteTerritory) ? 1 : 2;
    }

    private class TerritoryResult {
        int area = 0;
        boolean isBlackTerritory = false;
        boolean isWhiteTerritory = false;
    }
    private TerritoryResult calculateTerritory(int x, int y, boolean[][] globalVisited) {
        boolean[][] localVisited = new boolean[19][19];
        TerritoryResult result = new TerritoryResult();
        dfsTerritory(x, y, localVisited, result);
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (localVisited[i][j]) globalVisited[i][j] = true;
            }
        }
        return result;
    }

    private void dfsTerritory(int x, int y, boolean[][] visited, TerritoryResult result) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19 || visited[x][y]) return;
        visited[x][y] = true;

        int color = matrix[x][y].getColor();
        if (color != 0) {
            if (color == 1) result.isWhiteTerritory = false;
            if (color == 2) result.isBlackTerritory = false;
            return;
        }

        result.area++;

        if (result.area == 1) {
            result.isBlackTerritory = true;
            result.isWhiteTerritory = true;
        }

        dfsTerritory(x+1, y, visited, result);
        dfsTerritory(x-1, y, visited, result);
        dfsTerritory(x, y+1, visited, result);
        dfsTerritory(x, y-1, visited, result);
    }

    //This method should be done each time when a player placed a stone
    public int checkState() {
        if (consecutivePass >= 2 && state == 0) {
            state = calculateWinner();
            System.out.println("Game end");
        }
        return state;
    }

    private boolean hasLiberty(int x, int y, int color, boolean[][] visited) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19) return false;
        if (visited[x][y]) return false;
        visited[x][y] = true;

        if (matrix[x][y].getColor() == 0) return true;
        if (matrix[x][y].getColor() != color) return false;

        return hasLiberty(x+1, y, color, visited) ||
                hasLiberty(x-1, y, color, visited) ||
                hasLiberty(x, y+1, color, visited) ||
                hasLiberty(x, y-1, color, visited);
    }

    public boolean placeStone(int x, int y, int color) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19 || matrix[x][y].getColor() != 0) {
            System.out.println("Invalid move");
            return false;
        }
        matrix[x][y] = (color == 1) ? black : white;
        checkSurround();
        checkState();
        return true;
    }

    public int surrender(int surrenderingTeam) {
        state = 3;
        int winner = (surrenderingTeam == 1) ? 2 : 1;
        System.out.println("player " + surrenderingTeam + " surrendered.");
        return winner;
    }

    public int passTurn() {
        consecutivePass++;
        return checkState();
    }

    public int getState() {
        return state;
    }

    public int getStoneColor(int x, int y) {
        if (x < 0 || x >= 19 || y < 0 || y >= 19) return -1;
        return matrix[x][y].getColor();
    }
} 
