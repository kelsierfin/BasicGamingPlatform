package server.gamelogic.connect4;

public class Player {
    private String name;
    private String selectedSymbol;
    private int numberOfDiscPlaced;
    private int numberOfWins;

    public Player(String name, String selectedSymbol){
        this.name = name;
        this.selectedSymbol = selectedSymbol;
        this.numberOfDiscPlaced = 0;
        this.numberOfWins = 0;
    }

    public void addWin(){
        this.numberOfWins++;
    }


    public String getSelectedSymbol(){
        return this.selectedSymbol;
    }

    public int getNumberOfWins(){
        return this.numberOfWins;
    }

    public int getNumberOfDiscPlaced(){
        return this.numberOfDiscPlaced;
    }

    public String getName(){
        return this.name;
    }



}
