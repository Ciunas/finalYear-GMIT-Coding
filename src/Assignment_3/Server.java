package Assignment_3;// Fig. 18.4: Server.java
// Set up a Server that will receive a connection from a client, send
// a string to the client, and close the connection.

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {

    private JTextField enterField;
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private int counter = 1;

    public static void main(String args[]) throws IOException {
        EventQueue.invokeLater(new Runnable() {
                                       public void run() {
        Server application = new Server();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                       }
        });
    }


    // set up GUI
    public Server() {

        super("Server");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Container container = getContentPane();
                // create enterField and register listener
                enterField = new JTextField();
                enterField.setEditable(false);
                enterField.addActionListener(
                        new ActionListener() {
                            // send message to client
                            public void actionPerformed(ActionEvent event) {
                                sendData(event.getActionCommand());
                                enterField.setText("");
                            }
                        }
                );
                container.add(enterField, BorderLayout.NORTH);
                // create displayArea
                displayArea = new JTextArea();
                container.add(new JScrollPane(displayArea),
                        BorderLayout.CENTER);
                setSize(300, 150);
                setVisible(true);

                runThread th = new runThread();
                new Thread(th).start();
            }
        });
    } // end Server constructor

    public class runThread implements Runnable {
        // set up server to receive connections; process connections
        public void run() {
            try {
                // Step 1: Create a ServerSocket.
                server = new ServerSocket(12345, 100);
                System.out.println("Server created on port: 12345");
                server.setSoTimeout(1000);                        //set timeout for  12 second.

                while (true) {
                    try {
                        displayMessage("Waiting for connection\n");

                        while (true) {
                            try {
                                connection = server.accept(); // allow server to accept connection
                                break;
                            } catch (SocketTimeoutException s) {
                                System.out.println("Timeout retry for connection.");
                            }
                        }
                        displayMessage("Connection " + counter + " received from: " + connection.getInetAddress().getHostName());
                        try {
                            // set up output stream for objects
                            output = new ObjectOutputStream(connection.getOutputStream());
                            output.flush(); // flush output buffer to send header information
                            // set up input stream for objects
                            input = new ObjectInputStream(connection.getInputStream());

                            displayMessage("\nGot I/O streams\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String message = "Connection successful";
                        sendData(message);
                        // enable enterField so server user can send messages
                        setTextFieldEditable(true);
                        do { // process messages sent from client
                            // read message and display it
                            try {
                                message = (String) input.readObject();
                                displayMessage("\n" + message);
                            }
                            // catch problems reading from client
                            catch (ClassNotFoundException classNotFoundException) {
                                displayMessage("\nUnknown object type received");
                            }
                        } while (!message.equals("CLIENT>>> TERMINATE"));
                    }
                    // process EOFException when client closes connection
                    catch (EOFException eofException) {
                        System.err.println("Server terminated connection");
                    } finally {
                        displayMessage("\nTerminating connection\n");
                        setTextFieldEditable(false); // disable enterField
                        try {
                            output.close();
                            input.close();
                            connection.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        //closeConnection();   // Step 5: Close connection.
                        ++counter;
                    }
                } // end while
            } // end try
            // process problems with I/O
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } // end method runServer
    }


    // send message to client
    //synchronized
    private  void sendData  (String message) {

        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() // updates displayArea
                    {
                        // send object to client
                        try {
                            output.writeObject("SERVER>>> " + message);
                            output.flush();
                            displayMessage("\nSERVER>>> " + message);
                        }
                        // process problems sending object
                        catch (IOException ioException) {
                            displayArea.append("\nError writing object");
                        }
                    }
                }  // end inner class
        ); // end call to SwingUtilities.invokeLater
    }


    // utility method called from other threads to manipulate
    // displayArea in the event-dispatch thread
    private void displayMessage(final String messageToDisplay) {
        // display message from event-dispatch thread of execution
        SwingUtilities.invokeLater(
                new Runnable() {  // inner class to ensure GUI updates properly

                    public void run() // updates displayArea
                    {
                        displayArea.append(messageToDisplay);
                        displayArea.setCaretPosition(
                                displayArea.getText().length());
                    }
                }  // end inner class
        ); // end call to SwingUtilities.invokeLater
    }


    // utility method called from other threads to manipulate
    // enterField in the event-dispatch thread
    private void setTextFieldEditable(final boolean editable) {
        // display message from event-dispatch  thread of execution
        SwingUtilities.invokeLater(new Runnable() {  // inner class to ensure GUI updates properly

                                       public void run()  // sets enterField's editability
                                       {
                                           enterField.setEditable(editable);
                                       }
                                   }  // end inner class
        ); // end call to SwingUtilities.invokeLater
    }

}  // end class Server

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