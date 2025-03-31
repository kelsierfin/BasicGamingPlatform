package server.gamelogic.connect4;

public class Connect4_Board {
    private final int ROWS = 6;
    private final int COLUMNS = 7;
    private String[][] Board;

    public Connect4_Board(){
        this.Board = new String[ROWS][COLUMNS];


        int x = 0;
        int y;

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

    public String[][] getBoard(){
        return this.Board;
    }

    public void placeDisc(int column, Disc disc){

        int row = 0;

        while(Board[row][column].equals("[ ]")){

            row++;
            if(row == ROWS ){
                System.out.println("row: " + row);

                break;
            }
        }

        row--;



        this.Board[row][column] = "["+ disc.getSymbol() +"]";

        if(column == COLUMNS-1){
            this.Board[row][column] = "["+ disc.getSymbol() +"]\n";
        }
    }

    public void removeDisc(int row, int column){
        this.Board[row][column] = " ";

    }

    public boolean validMove(int column){

        if( column < 0 || column > 6){
            return false;
        }
        else{
            return true;
        }

    }

    public boolean checkWin(String sym){
        int index_row = 0;
        int index_column = 0;
        int match = 1;
        String prevSymbol = getSymbol(Board[index_row][index_column]);


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

        prevSymbol = getSymbol(Board[0][0]);
        index_column = 0;
        int start_column = 0;


        while(start_column < COLUMNS){
            index_row = 0;
            index_column = start_column;


            while(index_column < COLUMNS || index_row < ROWS){
                String symbol = getSymbol(Board[index_row][index_column]);

                if(prevSymbol.equals(symbol) && !symbol.equals("[ ]")){
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



        while(start_row < ROWS){
            index_row = start_row;

            while(index_column < COLUMNS || index_row < ROWS){
                String symbol = getSymbol(Board[index_row][index_column]);

                if(prevSymbol.equals(symbol) && !symbol.equals("[ ]")){
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

    public String getSymbol(String x){
        String[] array = x.split("");
        return array[1];
    }













}

