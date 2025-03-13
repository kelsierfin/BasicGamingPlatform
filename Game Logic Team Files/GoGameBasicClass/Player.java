public class Player {
    //0 = unset; 1 = black; 2 = white;
    int team = 0;
    //Initialize UCID as 0, each player use UCID to login.
    //Remember to let user register by their UCID.
    int UCID;
    //Initialize empty password
    String password = "";
    public Player(int team, int UCID, String password){
        this.team = team;
        this.UCID = UCID;
        this.password = password;
    }
    //Setters and Getters
    private void setTeam(int team){
        this.team = team;
    }
    private void setUCID(int UCID){
        this.UCID = UCID;
    }
    private void setPassword(String password){
        this.password = password;
    }
    private int getTeam(){
        return this.team;
    }
    private int getUCID(){
        return this.UCID;
    }
    private String getPassword(){
        return this.password;
    }
    public void placeStone(int x, int y){
        //Place the stone to the x, y position
    }

}
