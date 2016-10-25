# finalYear-GMIT-Programming

The goal in developing this game was to demonstrate a multithreaded server that could process multiple connections from clients at the same time. The server in the example is really a mediator between the two client applets—it makes sure that each move is valid and that each client moves in the proper order. The server does not determine who won or lost or if there was a draw. Also, there is no capability to allow a new game to be played or to terminate an existing game.

The following is a list of modifications that is required to the multithreaded server and client:

a) Modify the TicTacToeServer class to test for a win, loss or draw on each move in the game. Send a message to each client applet that indicates the result of the game when the game is over.

b) Modify the TicTacToeClient class to display a button that when clicked allows the client to play another game.

The button should be enabled only when a game completes.

Note that both classes TicTacToeClient and class TicTacToeServer must be modified to reset the board array and program state varibales.

Also, the other TicTacToeClient should be notified that a new game is about to begin so its board and state can be reset.

The first client to activate the "new game" button should be allocated 'X'.
