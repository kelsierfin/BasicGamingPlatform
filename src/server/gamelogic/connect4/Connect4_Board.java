package server.gamelogic.connect4;

public class Connect4_Board {
    private final int ROWS = 6;
    private final int COLUMNS = 7;

    // Board holds the main game grid the game is played on.
    private String[][] Board;



    public Connect4_Board(){
        this.Board = new String[ROWS][COLUMNS];


        int x = 0;
        int y;

        // Nested for loop fills Board matrix with empty cells.
        while(x < ROWS){
            y = 0;
            while(y < COLUMNS){
                this.Board[x][y] = "[ ]";

                if(y == COLUMNS -1 ){
                    String cell = this.Board[x][y];
                    cell = cell + "\n";
                    this.Board[x][y] = cell;
                }

                y++;
            }


            x++;
        }


    }

    // Returns game board when called.
    public String[][] getBoard(){
        return this.Board;
    }

    /* placeDisc(int column, Disc disc) takes in a disc object and the column in which it is placed in.
    *  After determining the full cell coordinates of the disc, the  symbol of the
    *  disc is added to the cell. */

    public void placeDisc(int column, Disc disc){

        int row = 0;

        // while loop used to calculate the row the disc will be placed in.
        while(Board[row][column].equals("[ ]") || Board[row][column].equals("[ ]\n") ){

            row++;
            if(row == ROWS ){
                System.out.println("row: " + row);

                break;
            }
        }

        row--;


        // Adding disc to board
        System.out.println("row = " + row + " column = " + column);
        this.Board[row][column] = "["+ disc.getSymbol() +"]";

        if(column == COLUMNS-1){
            this.Board[row][column] = "["+ disc.getSymbol() +"]\n";
        }
    }


    // Used to remove a disc from the game grid
    public void removeDisc(int row, int column){
        this.Board[row][column] = " ";

    }


    /* validMove(int column) checks if the column chosen by the player is with in the bounds
      of the game grid. Also check if a column is currently full. */
    public boolean validMove(int column){

        if( column < 0 || column > 6){
            return false;
        }else if(isColumnFull()){
            return false;
        }
        else{
            return true;
        }

    }

    /* checkWin(String sym) Iterates over the game board and checks if four discs
    *  represented by a particular symbol are placed together horizontally, diagonally
    *  or vertically. */

    public boolean checkWin(String sym){
        int index_row = 0;
        int index_column = 0;
        int match = 1;
        String prevSymbol = getSymbol(Board[index_row][index_column]);

        // Checking for horizontal win.

        while(index_row < ROWS){
            index_column = 0;
            while(index_column < COLUMNS){

                String symbol = getSymbol(Board[index_row][index_column]);

                if(prevSymbol.equals(symbol) && prevSymbol.equals(sym)) {

                    match++;

                    if (match == 3) {
                        return true;
                    }

                }else{
                    match = 0;

                }
                prevSymbol = getSymbol(Board[index_row][index_column]);
                index_column++;
            }
            match = 0;
            index_row++;
        }

        index_column = 0;
        match = 1;

        // Checking for Vertical win.

        while(index_column < COLUMNS){
            index_row = 0;
            while(index_row < ROWS){

                String symbol = getSymbol(Board[index_row][index_column]);

                if(prevSymbol.equals(symbol) && prevSymbol.equals(sym)) {

                    match++;

                    if (match == 3) {
                        return true;
                    }

                }else{
                    match = 0;

                }
                prevSymbol = getSymbol(Board[index_row][index_column]);
                index_row++;
            }
            match = 0;
            index_column++;
        }

        // Checking for diagonal wins starting from the top left corner.
        System.out.println("Executing column diagonal check");

        prevSymbol = getSymbol(Board[0][0]);
        index_column = 0;
        int start_column = 0;


        while(start_column < COLUMNS){
            index_row = 0;
            index_column = start_column;


            while(index_column < COLUMNS && index_row < ROWS){
                String symbol = getSymbol(Board[index_row][index_column]);



                if(prevSymbol.equals(symbol) &&  prevSymbol.equals(sym)){
                    match++;

                    if(match == 3){
                        return true;
                    }

                }else{
                    match = 0;
                }
                index_column++;
                index_row++;


            }

            start_column++;



        }

        prevSymbol = getSymbol(Board[1][0]);
        index_column = 0;


        int start_row = 0;

        System.out.println("Executing row diagonal check");

        // Checking for diagonal win by iterating over each row of the game board.

        while(start_row < ROWS){
            index_row = start_row;

            while(index_column < COLUMNS && index_row < ROWS){
                String symbol = getSymbol(Board[index_row][index_column]);

                if(prevSymbol.equals(symbol) && prevSymbol.equals(sym)){
                    match++;

                    if(match == 3){
                        return true;
                    }

                }else{
                    match = 0;
                }
                index_column++;
                index_row++;


            }

            start_row++;



        }



        return false;
    }

    /*  isColumnFull() Checks if any of the columns in the board are full.*/
    public boolean isColumnFull(){
        int i = 0;


        while(i < COLUMNS){
            if(!Board[0][i].equals("[ ]") && !Board[0][i].equals("[ ]\n")){
                System.out.println("column full");

                return true;
            }else{
                i++;
            }
        }

        return false;
    }






    /* printBoard() prints the current  state of the board. */

    public void printBoard(){
        int x = 0;
        int y;

        while(x < ROWS){
            y = 0;
            while(y < COLUMNS){
                System.out.print(Board[x][y]);

                y++;
            }
            x++;
        }

    }

    /* getSymbol(String x) returns the symbol placed in the cell. */

    public String getSymbol(String x){
        String[] array = x.split("");
        return array[1];
    }













}

