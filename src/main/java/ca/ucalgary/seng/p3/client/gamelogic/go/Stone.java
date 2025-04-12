package ca.ucalgary.seng.p3.client.gamelogic.go;

public class Stone {
    //0 = Blank; 1 = black; 2 = white; 3 = eaten by black; 4 = eaten by white
    int color = 0;
    //constructor
    public Stone(int color){
        this.color = color;
    }
    //getters and setters
    public int getColor(){
        return color;
    }
    public void setColor(int color){
        this.color = color;
    }
    

}