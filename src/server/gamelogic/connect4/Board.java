package server.gamelogic.connect4;

public class Board {
    private final int ROWS = 5;
    private final int COLUMNS = 6;
    private String[][] Board;

    public Board (){
        this.Board = new String[ROWS][COLUMNS];


        int x = 0;
        int y = 0;

        while(x <= ROWS){
            while(y <= COLUMNS){
                this.Board[x][y] = " ";

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

        while(Board[row][column].equals(" ")){
            row++;
        }

        row  = row - 1;


        this.Board[row][column] = disc.getSymbol();
    }

    public void removeDisc(int row, int column){
        this.Board[row][column] = " ";

    }

    public boolean validMove(int row, int column){

        if( row < 0 || row  > 5 ){
            return false;
        }else if( column < 0 || column > 6){
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
        int y = 0;

        while(x <= ROWS){
            while(y <= COLUMNS){
                System.out.print(Board[x][y]);
                if( y == COLUMNS){
                    System.out.print("\n");
                }
                y++;
            }
            x++;
        }

    }










}
