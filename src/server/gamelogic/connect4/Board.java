package server.gamelogic.connect4;

public class Board {
    private final int ROWS = 5;
    private final int COLUMNS = 6;
    private String[][] Board;

    public Board (){
        this.Board = new String[ROWS][COLUMNS];
    }

    public String[][] getBoard(){
        return this.Board;
    }

    public void placeDisc(int row , int column, Disc disc){
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







}
