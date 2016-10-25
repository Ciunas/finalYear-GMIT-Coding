package Assignment_4;
// Fig. 18.8: TicTacToeServer.java
// This class maintains a game of Tic-Tac-Toe for two client applets.

import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.swing.*;

public class TicTacToeServer extends JFrame {
    private char[] board;
    private JTextArea outputArea;
    private Player[] players;
    private ServerSocket server;
    private int currentPlayer;
    private boolean result = false;
    private boolean draw = false;
    private final int PLAYER_X = 0, PLAYER_O = 1;
    private final char X_MARK = 'X', O_MARK = 'O';
    StringBuilder sb1 = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    private static Logger serverLogger =
            Logger.getLogger(TicTacToeServer.class.getName());

    //Main call at the start
    public static void main(String args[]) {
        TicTacToeServer application = new TicTacToeServer();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.execute();
    }


    // place code in this method to determine whether game over
    public boolean isGameOver() {
        return false;  // this is left as an exercise
    }


    // set up tic-tac-toe server and GUI that displays messages
    public TicTacToeServer() {

        super("Tic-Tac-Toe Server");
        board = new char[9];
        players = new Player[2];
        currentPlayer = PLAYER_X;

        // set up ServerSocket
        try {
            server = new ServerSocket(12345, 2);
        }catch (IOException ioException) {
            ioException.printStackTrace();
            System.exit(1);
        }

        // set up JTextArea to display messages during execution
        outputArea = new JTextArea();
        getContentPane().add(outputArea, BorderLayout.CENTER);
        outputArea.setText("Server awaiting connections\n");

        setSize(300, 300);
        setVisible(true);

    } // end TicTacToeServer constructor


    // wait for two connections so game can be played
    public void execute() {
        // wait for each client to connect
        for (int i = 0; i < players.length; i++) {
            // wait for connection, create Player, start thread
            try {
                players[i] = new Player(server.accept(), i);
                players[i].start();
            }catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }
        }

        // Player X is suspended until Player O connects.
        // Resume player X now.
        synchronized (players[PLAYER_X]) {
            players[PLAYER_X].setSuspended(false);
            players[PLAYER_X].notify();
        }
    }  // end method execute


    // utility method called from other threads to manipulate
    // outputArea in the event-dispatch thread
    private void displayMessage(final String messageToDisplay) {
        // display message from event-dispatch thread of execution
        SwingUtilities.invokeLater(
                new Runnable() {  // inner class to ensure GUI updates properly

                    public void run() // updates outputArea
                    {
                        outputArea.append(messageToDisplay);
                        outputArea.setCaretPosition(
                                outputArea.getText().length());
                    }
                }  // end inner class
        ); // end call to SwingUtilities.invokeLater
    }


    // Determine if a move is valid. This method is synchronized because
    // only one move can be made at a time.
    public synchronized String validateAndMove(int location, int player) {

        boolean moveDone = false;

        // while not current player, must wait for turn
        while (player != currentPlayer) {

            // wait for turn
            try {
                System.out.println("stuck in waiting" + player);
                wait();
            }catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

        // if location not occupied, make move
        if (!isOccupied(location)) {

            // set move in board array
            board[location] = currentPlayer == PLAYER_X ? X_MARK : O_MARK;
            currentPlayer = (currentPlayer + 1) % 2;                                     // change current player
            String str = "";
            result = calculateWinner(location, player);                                  //Caculate if the game was won.

            //create string and set it to draw or won, this is then sent ot other playermoved
            if (result == true) {
                str = "won";
                serverLogger.warning("returning winner: " + str);
            }else if (draw ==  true){
                str = "draw";
            }
            // let new current player know that move occurred
            players[currentPlayer].otherPlayerMoved(location, str);
            notify(); // tell waiting player to continue

            if (result) {
                return "true YouWin";
            }else if (draw){
                return "true YouDraw";
            }
            // tell player that made move that the move was valid
            return "true KeepPLaying";
        }// tell player that made move that the move was not valid
        else
            return "false";
    } // end method validateAndMove


    //Method to calculate if current move wins game
    public boolean calculateWinner(int location, int player) {
        serverLogger.warning("Calculating Winner");
        boolean winner = false;
        String st = "0,1,2'0,3,6'0,4,8'1,4,7'5,8,2'3,4,5'2,4,6'6,7,8";
        String moves;
        int i = 0, win = 0;

        if (player == 0) {
            sb1.append(location + "'");
            moves = sb1.toString();
        } else {
            sb2.append(location + "'");
            moves = sb2.toString();
        }

        String[] movesInt = moves.split("'");                   //split up player moves into tokens
        String[] result = st.split("'");                        //split up into string containing three digits

        while (i < result.length && winner == false) {                             // run  8 times
            String[] indInt = result[i].split(",");             //split up into individual number characters 3
            for (int j = 0; j < movesInt.length; j++) {        //loop to check if move conataines the pattern of a win.
                for (int k = 0; k < indInt.length; k++) {
                    if ((indInt[k].equals(movesInt[j])) == true) {
                        if (++win == 3) {
                            winner = true;
                            break;
                        }
                    }
                }
            }
            win = 0;
            i++;
        }
        if(sb1.length() + sb2.length() >= 18){
            draw = true;
        }
        serverLogger.warning("Returning value of calculate winner: " + winner);
        return winner;
    }


    // determine whether location is occupied
    public boolean isOccupied(int location) {
        if (board[location] == X_MARK || board[location] == O_MARK)
            return true;
        else
            return false;
    }


    // private inner class Player manages each Player as a thread
    private class Player extends Thread {
        private Socket connection;
        private DataInputStream input;
        private DataOutputStream output;
        private int playerNumber;
        private char mark;
        protected boolean suspended = true;

        // set up Player thread
        public Player(Socket socket, int number) {
            playerNumber = number;
            // specify player's mark
            mark = (playerNumber == PLAYER_X ? X_MARK : O_MARK);
            connection = socket;
            // obtain streams from Socket
            try {
                input = new DataInputStream(connection.getInputStream());
                output = new DataOutputStream(connection.getOutputStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }
        } // end Player constructor


        // send message that other player moved
        public void otherPlayerMoved(int location, String update) {

            if (update.equals("won")) {
                try {
                    output.writeUTF("Opponent moved And Wins");
                    output.writeInt(location);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (update.equals("reset")) {
                try {
                    output.writeUTF("Reset");
                    Arrays.fill(board, ' ');
                    result = false;
                    draw = false;
                    sb1.setLength(0);
                    sb2.setLength(0);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (update.equals("draw")) {
                try {
                    output.writeUTF("Opponent moved Draw Game");
                    output.writeInt(location);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                // send message indicating move
                try {
                    output.writeUTF("Opponent moved");
                    output.writeInt(location);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }


        // control thread's execution
        public void run() {
            // send client message indicating its mark (X or O),
            // process messages from client
            try {
                displayMessage("Player " + (playerNumber ==
                        PLAYER_X ? X_MARK : O_MARK) + " connected\n");

                output.writeChar(mark); // send player's mark

                // send message indicating connection
                output.writeUTF("Player " + (playerNumber == PLAYER_X ?
                        "X connected\n" : "O connected, please wait\n"));

                // if player X, wait for another player to arrive
                if (mark == X_MARK) {
                    output.writeUTF("Waiting for another player");

                    // wait for player O
                    try {
                        synchronized (this) {
                            while (suspended)
                                wait();
                        }
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                    // send message that other player connected and player X can make a move
                    output.writeUTF("Other player connected. Your move.");
                }

                // while game not over
                while (!isGameOver()) {

                    // get move location from client
                    int location = input.readInt();
                    if (location == 33) {               //33 numeric value of character 'X'
                        System.out.println("Character 'X'");
                        // while not current player, must wait for turn
                        if (playerNumber != currentPlayer) {
                            players[currentPlayer].otherPlayerMoved(location, "reset");
                        } else {
                            currentPlayer = 1;
                            players[currentPlayer].otherPlayerMoved(location, "reset");
                        }
                        currentPlayer = 0;
                    } else if (location == 24) {          //24 numeric value of character 'O'
                        System.out.println("Character 'O'");
                        // while not current player, must wait for turn
                        if (playerNumber != currentPlayer) {
                            players[currentPlayer].otherPlayerMoved(location, "reset");
                        } else {
                            currentPlayer = 0;
                            players[currentPlayer].otherPlayerMoved(location, "reset");
                        }
                        currentPlayer = 1;
                    } else {
                        String result = validateAndMove(location, playerNumber);
                        // check for valid move
                        if (result.equals("true KeepPLaying")) {
                            displayMessage("\nlocation: " + location);
                            output.writeUTF("Valid move.");
                        } else if (result.equals("true YouWin")) {
                            displayMessage("\nlocation: " + location);
                            output.writeUTF("Valid move. YouWin");
                        }  else if (result.equals("true YouDraw")) {
                        displayMessage("\nlocation: " + location);
                        output.writeUTF("Valid move. YouDraw");
                    } else
                            output.writeUTF("Invalid move, try again");
                    }
                }
                connection.close(); // close connection to client
            } // end try

            // process problems communicating with client
            catch (IOException ioException) {
                ioException.printStackTrace();
                System.exit(1);
            }

        } // end method run

        // set whether or not thread is suspended
        public void setSuspended(boolean status) {
            suspended = status;
        }

    } // end class Player
} // end class TicTacToeServer

/**************************************************************************
 * (C) Copyright 1992-2003 by Deitel & Associates, Inc. and               *
 * Prentice Hall. All Rights Reserved.                                    *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
