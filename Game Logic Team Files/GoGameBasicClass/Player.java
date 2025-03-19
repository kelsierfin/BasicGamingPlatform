public class Player {
    // 0 = unset; 1 = black; 2 = white;
    int team = 0;
    // Initialize UCID as 0, each player use UCID to login.
    // Remember to let user register by their UCID.
    int UCID;
    // Initialize empty password
    String password = "";

    public Player(int team, int UCID, String password) {
        this.team = team;
        this.UCID = UCID;
        this.password = password;
    }

    // Setters and Getters
    public void setTeam(int team) {
        this.team = team;
    }

    public void setUCID(int UCID) {
        this.UCID = UCID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTeam() {
        return this.team;
    }

    public int getUCID() {
        return this.UCID;
    }

    public String getPassword() {
        return this.password;
    }

    public void placeStone(int x, int y, Board board) {
        board.placeStone(x, y, this.team);
    }
    public void surrender(Board board) {
        board.surrender(this.team);
    }
}