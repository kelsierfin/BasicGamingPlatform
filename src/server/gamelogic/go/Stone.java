package server.gamelogic.go;
public class Stone {
    //0 = Blank; 1 = black; 2 = white;
    int color = 0;
    //position of the stones;
    int x = -1;
    int y = -1;
    //constructor
    public Stone(int x, int y, int color){
        this.x = x;
        this.y = y;
        this.color = color;
    }
    //getters and setters
    public int getColor(){
        return color;
    }
    public void setColor(int color){
        this.color = color;
    }
    public int getX(){
        return x;
    }
    public void setX(int x){
        this.x = x;
    }
    public int getY(){
        return y;
    }
    public void setY(int y){
        this.y = y;
    }
    

}
