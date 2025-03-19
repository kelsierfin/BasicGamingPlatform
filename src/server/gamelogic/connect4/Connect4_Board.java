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
                //System.out.println("y = " + y);
            }


            x++;
            //System.out.println("x = " + x);
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

    public boolean checkWin(){

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










}

