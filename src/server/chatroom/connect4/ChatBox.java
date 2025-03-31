package main.java.server.gamelogic.connect4;
import server.gamelogic.connect4.Player;

public class ChatBox {
    private server.gamelogic.connect4.Player playerOne;
    private server.gamelogic.connect4.Player playerTwo;

    public ChatBox(Player player1, Player player2){
        this.playerOne = player1;
        this.playerTwo = player2;
    }

    public void sendMessage(String message){


    }

    public String receiveMessage(){
        return null;
    }



}
