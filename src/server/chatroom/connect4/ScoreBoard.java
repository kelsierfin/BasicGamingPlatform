package server.gamelogic.connect4;

public class ScoreBoard {
    private int points;
    private int gamesPlayed;

    public ScoreBoard(){
        this.points = 0;
        this.gamesPlayed =0;
    }

    public void addPoints(int points){
        this.points = this.points +points;
    }

    public void addGamesPlayed(int x){
        this.gamesPlayed = this.gamesPlayed + 1;
    }

}
