package Assignment_3;// Fig. 18.5: Client.java
// Client that reads and displays information sent from a Server1.

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class Client extends JFrame {
    private JTextField enterField;
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String chatServer;
    private Socket client;

    public static void main(String args[]) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.print("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
                Client application;

                if (args.length == 0)
                    application = new Client("127.0.0.1");
                else
                    application = new Client(args[0]);
                application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }

    // initialize chatServer and set up GUI
    public Client(String host) {

        super("Client");

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                System.out.print("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
                chatServer = host; // set server to which this client connects
                Container container = getContentPane();
                // create enterField and register listener
                enterField = new JTextField();
                enterField.setEditable(false);
                enterField.addActionListener(
                        new ActionListener() {
                            // send message to server
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
                startClient();
//                runClient th = new runClient();
//                new Thread(th).start();
            }
        });
    } // end Client constructor


    //Start swingworker to run main thread.
    public void startClient() {
        (new startClient()).execute();
    }

    //Method for reversing a line of text
    class startClient extends SwingWorker<Void, String> {
        @Override
        protected void process(List<String> list) {

        }

        @Override
        public Void doInBackground() {
            try {
                displayMessage("Attempting connection\n");
                // create Socket to make connection to server
                client = new Socket(InetAddress.getByName(chatServer), 12345);
                // display connection information
                displayMessage("Connected to: " + client.getInetAddress().getHostName());
                // set up output stream for objects
                output = new ObjectOutputStream(client.getOutputStream());
                output.flush(); // flush output buffer to send header information
                // set up input stream for objects
                input = new ObjectInputStream(client.getInputStream());
                displayMessage("\nGot I/O streams\n");
                // enable enterField so client user can send messages
                setTextFieldEditable(true);
                do { // process messages sent from server
                    // read message and display it
                    try {
                        message = (String) input.readObject();
                        displayMessage("\n" + message);
                    }
                    // catch problems reading from server
                    catch (ClassNotFoundException classNotFoundException) {
                        displayMessage("\nUnknown object type received");
                    }
                } while (!message.equals("SERVER>>> TERMINATE"));
                displayMessage("\nClosing connection");
                setTextFieldEditable(false); // disable enterField

                try {
                    output.close();
                    input.close();
                    client.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            // process problems communicating with server
            catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                displayMessage("\nClosing connection");
                setTextFieldEditable(false); // disable enterField

                try {
                    output.close();
                    input.close();
                    client.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                //closeConnection(); // Step 4: Close connection
            }
            return null;
        }

    }

    // send message to client
    private void sendData(String message) {

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
        // display message from GUI thread of execution
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
        // display message from GUI thread of execution
        SwingUtilities.invokeLater(
                new Runnable() {  // inner class to ensure GUI updates properly

                    public void run()  // sets enterField's editability
                    {
                        enterField.setEditable(editable);
                    }
                }  // end inner class
        ); // end call to SwingUtilities.invokeLater
    }

} // end class Client

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
