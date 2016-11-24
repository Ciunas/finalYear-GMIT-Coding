// Fig. 8.38: AddressBook.java
// An address book database example that allows information to 
// be inserted, updated and deleted. The example uses 
// transactions to ensure that the operations complete 
// successfully.
package Assignment_6;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

public class AddressBook extends JFrame {

    // reference for manipulating multiple document interface
    private JDesktopPane desktop;

    // reference to database access object
    private AddressBookDataAccess database;

    int x = 0, y = 0, z = 0;
    // references to Actions
    Action newAction, saveAction, deleteAction,
            searchAction, exitAction, addEmail, addPhone, addAddress;

    // set up database connection and GUI
    public AddressBook() {
        super("Address Book");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                // create database connection
                try {
                    database = new CloudscapeDataAccess();
                }

                // detect problems with database connection
                catch (Exception exception) {
                    exception.printStackTrace();
                    System.exit(1);
                }

                // database connection successful, create GUI
                JToolBar toolBar = new JToolBar();
                JMenu fileMenu = new JMenu("File");
                fileMenu.setMnemonic('F');

                // Set up actions for common operations. Private inner
                // classes encapsulate the processing of each action.
                newAction = new NewAction();
                saveAction = new SaveAction();
                saveAction.setEnabled(false);    // disabled by default
                deleteAction = new DeleteAction();
                deleteAction.setEnabled(false);  // disabled by default
                searchAction = new SearchAction();
                exitAction = new ExitAction();
                addEmail = new AddEmail();
                addEmail.setEnabled(false);
                addPhone = new AddPhone();
                addPhone.setEnabled(false);
                addAddress = new AddAddress();
                addAddress.setEnabled(false);

                // add actions to tool bar
                toolBar.add(newAction);
                toolBar.add(saveAction);
                toolBar.add(deleteAction);
                toolBar.add(new JToolBar.Separator());
                toolBar.add(searchAction);
                toolBar.add(new JToolBar.Separator());
                toolBar.add(addEmail);
                toolBar.add(addPhone);
                toolBar.add(addAddress);

                // add actions to File menu
                fileMenu.add(newAction);
                fileMenu.add(saveAction);
                fileMenu.add(deleteAction);
                fileMenu.addSeparator();
                fileMenu.add(searchAction);
                fileMenu.addSeparator();
                fileMenu.add(addEmail);
                fileMenu.add(addPhone);
                fileMenu.add(addAddress);
                fileMenu.addSeparator();
                fileMenu.add(exitAction);

                // set up menu bar
                JMenuBar menuBar = new JMenuBar();
                menuBar.add(fileMenu);
                setJMenuBar(menuBar);

                // set up desktop
                desktop = new JDesktopPane();

                // get the content pane to set up GUI
                Container c = getContentPane();
                c.add(toolBar, BorderLayout.NORTH);
                c.add(desktop, BorderLayout.CENTER);

                // register for windowClosing event in case user
                // does not select Exit from File menu to terminate
                // application
                addWindowListener(
                        new WindowAdapter() {
                            public void windowClosing(WindowEvent event) {
                                shutDown();
                            }
                        }
                );

                // set window size and display window
                Toolkit toolkit = getToolkit();
                Dimension dimension = toolkit.getScreenSize();

                // center window on screen
                setBounds(100, 100, dimension.width - 1000,
                        dimension.height - 200);

                setVisible(true);
            }
        });
    }  // end AddressBook constructor

    // close database connection and terminate program
    private void shutDown() {
        database.close();   // close database connection
        System.exit(0);   // terminate program
    }

    // create a new AddressBookEntryFrame and register listener
    private AddressBookEntryFrame createAddressBookEntryFrame(int value) {


        AddressBookEntryFrame frame = new AddressBookEntryFrame(value);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.addInternalFrameListener(
                new InternalFrameAdapter() {

                    // internal frame becomes active frame on desktop
                    public void internalFrameActivated(
                            InternalFrameEvent event) {
                        saveAction.setEnabled(true);
                        deleteAction.setEnabled(true);
                        addEmail.setEnabled(true);
                        addPhone.setEnabled(true);
                        addAddress.setEnabled(true);
                    }

                    // internal frame becomes inactive frame on desktop
                    public void internalFrameDeactivated(
                            InternalFrameEvent event) {
                        saveAction.setEnabled(false);
                        deleteAction.setEnabled(false);
                        addEmail.setEnabled(false);
                        addPhone.setEnabled(false);
                        addAddress.setEnabled(false);
                    }
                }  // end InternalFrameAdapter anonymous inner class
        ); // end call to addInternalFrameListener

        return frame;


    }  // end method createAddressBookEntryFrame

    // method to launch program execution
    public static void main(String args[]) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

//                    UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
                    UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                new AddressBook();
            }
        });

    }


    private class AddAddress extends AbstractAction {

        // set up action's name, icon, descriptions and mnemonic
        public AddAddress() {
            putValue(NAME, "AddAddress");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/addressbookadd.png")));
            putValue(SHORT_DESCRIPTION, "Add Address");
            putValue(LONG_DESCRIPTION,
                    "Add another Address to entry");
            putValue(MNEMONIC_KEY, new Integer('A'));
        }

        public void actionPerformed(ActionEvent e) {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {

                    AddressBookEntryFrame currentFrame =
                            (AddressBookEntryFrame) desktop.getSelectedFrame();
                    AddressBookEntry person =
                            currentFrame.getAddressBookEntry();
                    Integer personID = person.getPersonID();
                    System.out.println(personID);

                    if (personID.equals(0)) {
                        System.out.println("get empty details nothing there");
                        desktop.removeAll();
                        currentFrame.dispose();
                        currentFrame.address(y, x, ++z, 0);
                        System.out.println("desktop .add ");
                        desktop.add(currentFrame);
                        currentFrame.setVisible(true);
                    } else {
                        int extrasFields = 0;
                        extrasFields = person.getExtraDetails();
                        System.out.println("ExtraFields: " + extrasFields);
                        if (((extrasFields >> 5) | 0b0) == 0b0) {
                            z = 1;
                            //System.out.println("temp is one");
                            System.out.println("creating extra panel first one");

                            extrasFields = extrasFields | 0b100000;
                            System.out.println("Extras details:" + person.getExtraDetails());
                        } else if ((((extrasFields >> 4) & 0b01) & 0b1) == 0b0) {

                            System.out.println("creating extra panel nubumber two");
                            extrasFields = extrasFields | 0b010000;
                            // System.out.println("Extras details:" + person.getExtraDetails());
                            z = 2;
                        }

                        person.setExtraDetails(extrasFields);
                        desktop.removeAll();
                        desktop.repaint();
                        currentFrame.dispose();
                        currentFrame.address(y, x, z, extrasFields);
                        currentFrame.setAddressBookEntry(person, extrasFields);
                        desktop.add(currentFrame);
                        currentFrame.setVisible(true);
                        person.setAddDetails(z);
                    }
                }
            });

        }           ////////////////////////

    }  // end inner class AddEmail

    // Private inner class defines action that enables
    // user to input new entry. User must "Save" entry
    // after inputting data.
    private class AddEmail extends AbstractAction {

        // set up action's name, icon, descriptions and mnemonic
        public AddEmail() {
            putValue(NAME, "AddEmail");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/email_add.png")));
            putValue(SHORT_DESCRIPTION, "Add Email");
            putValue(LONG_DESCRIPTION,
                    "Add another Email address to entry");
            putValue(MNEMONIC_KEY, new Integer('E'));
        }

        public void actionPerformed(ActionEvent e) {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    AddressBookEntryFrame currentFrame =
                            (AddressBookEntryFrame) desktop.getSelectedFrame();
                    AddressBookEntry person =
                            currentFrame.getAddressBookEntry();
                    Integer personID = person.getPersonID();
                    System.out.println(personID);

                    if (personID.equals(0)) {
                        System.out.println("get empty details nothing there");
                        desktop.removeAll();
                        currentFrame.dispose();
                        currentFrame.email(y, ++x, z, 0);
                        System.out.println("desktop .add ");
                        desktop.add(currentFrame);
                        currentFrame.setVisible(true);
                    } else {
                        Integer temp1 = 0;
                        int extrasFields = 0b0;
                        extrasFields = person.getExtraDetails();
                        System.out.println("ExtraFields: " + extrasFields);

                        if (((extrasFields >> 1) & 0b00001) == 0b0) {
                            x = 1;
                            System.out.println("temp is one email");
                            System.out.println("creating extra panel");

                            extrasFields = (extrasFields | 0b000010);
                            //System.out.println("Extras details:" + person.getExtraDetails());
                        } else if ((extrasFields & 0b000001) == 0b0) {

                            System.out.println("temp is two email");
                            extrasFields = (extrasFields | 0b000001);
                            System.out.println("Extras details:" + person.getExtraDetails());
                            x = 2;
                        }

                        person.setExtraDetails(extrasFields);

                        System.out.println("ExtraFields2: " + extrasFields);
                        desktop.removeAll();
                        desktop.repaint();
                        currentFrame.dispose();
                        currentFrame.email(y, x, z, extrasFields);
                        currentFrame.setAddressBookEntry(person, extrasFields);
                        person.setAddDetailsEmail(x);
                        desktop.add(currentFrame);
                        currentFrame.setVisible(true);
                    }
                }
            });
        }

    }  // end inner class AddEmail


    private class AddPhone extends AbstractAction {

        // set up action's name, icon, descriptions and mnemonic
        public AddPhone() {
            putValue(NAME, "AddPhone");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/phone-add-icon.png")));
            putValue(SHORT_DESCRIPTION, "Add Phone");
            putValue(LONG_DESCRIPTION,
                    "Add another Phone Number to entry");
            putValue(MNEMONIC_KEY, new Integer('P'));
        }

        public void actionPerformed(ActionEvent e) {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    AddressBookEntryFrame currentFrame =
                            (AddressBookEntryFrame) desktop.getSelectedFrame();
                    AddressBookEntry person =
                            currentFrame.getAddressBookEntry();
                    Integer personID = person.getPersonID();
                    System.out.println(personID);

                    if (personID.equals(0)) {
                        System.out.println("get empty details nothing there Phone");
                        desktop.removeAll();
                        currentFrame.dispose();
                        currentFrame.email(++y, x, z, 0);
                        desktop.add(currentFrame);
                        currentFrame.setVisible(true);
                    } else {
                        Integer temp1 = 0;
                        int extrasFields = 0b0;
                        extrasFields = person.getExtraDetails();
                        if (((extrasFields >> 3) & 0b00001) == 0b0) {
                            y = 1;
                            System.out.println("temp is one, Phone");
                            System.out.println("creating extra panel");

                            extrasFields = (extrasFields | 0b001000);
                            //System.out.println("Extras details:" + person.getExtraDetails());
                        } else if ((extrasFields >> 2 & 0b0001) == 0b0) {

                            System.out.println("temp is two, Phone");
                            extrasFields = (extrasFields | 0b000100);
                            //System.out.println("Extras details:" + person.getExtraDetails());
                            y = 2;
                        }

                        person.setExtraDetails(extrasFields);

                        //System.out.println("ExtraFields2: " + extrasFields);
                        desktop.removeAll();
                        desktop.repaint();
                        currentFrame.dispose();
                        currentFrame.email(y, x, z, extrasFields);
                        currentFrame.setAddressBookEntry(person, extrasFields);
                        person.setAddDetailsPhone(y);
                        desktop.add(currentFrame);
                        currentFrame.setVisible(true);
                    }
                }
            });
        }


    }  // end inner class AddEmail


    // Private inner class defines action that enables
    // user to input new entry. User must "Save" entry
    // after inputting data.
    private class NewAction extends AbstractAction {

        // set up action's name, icon, descriptions and mnemonic
        public NewAction() {
            putValue(NAME, "New");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/New24.png")));
            putValue(SHORT_DESCRIPTION, "New");
            putValue(LONG_DESCRIPTION,
                    "Add a new address book entry");
            putValue(MNEMONIC_KEY, new Integer('N'));
        }

        // display window in which user can input entry
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    // create new internal window
                    // int [] temp = new int[6];
                    AddressBookEntryFrame entryFrame =
                            createAddressBookEntryFrame(0);

                    // set new AddressBookEntry in window
                    entryFrame.setAddressBookEntry(
                            new AddressBookEntry(), 0b0);

                    // display window
                    desktop.add(entryFrame);
                    entryFrame.setVisible(true);
                    x = 0;
                    y = 0;
                    z = 0;
                }
            });
        }


    }  // end inner class NewAction


    // inner class defines an action that can save new or
    // updated entry
    private class SaveAction extends AbstractAction {

        // set up action's name, icon, descriptions and mnemonic
        public SaveAction() {
            putValue(NAME, "Save");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/Save24.png")));
            putValue(SHORT_DESCRIPTION, "Save");
            putValue(LONG_DESCRIPTION,
                    "Save an address book entry");
            putValue(MNEMONIC_KEY, new Integer('S'));
        }

        // save new entry or update existing entry
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    // get currently active window
                    AddressBookEntryFrame currentFrame =
                            (AddressBookEntryFrame) desktop.getSelectedFrame();

                    // obtain AddressBookEntry from window
                    AddressBookEntry person =
                            currentFrame.getAddressBookEntry();

                    // insert person in address book
                    try {

                        // Get personID. If 0, this is a new entry;
                        // otherwise an update must be performed.
                        int personID = person.getPersonID();

                        // determine string for message dialogs
                        String operation =
                                (personID == 0) ? "Insertion" : "Update";

                        // insert or update entry
                        if (personID == 0)
                            database.newPerson(person);
                        else
                            database.savePerson(person);

                        // display success message
                        JOptionPane.showMessageDialog(desktop,
                                operation + " successful");
                    }  // end try

                    // detect database errors
                    catch (DataAccessException exception) {
                        JOptionPane.showMessageDialog(desktop, exception,
                                "DataAccessException",
                                JOptionPane.ERROR_MESSAGE);
                        exception.printStackTrace();
                    }
                    x = 0;
                    y = 0;
                    z = 0;
                    // close current window and dispose of resources
                    currentFrame.dispose();
                }
            });

        }  // end method actionPerformed

    }  // end inner class SaveAction

    // inner class defines action that deletes entry
    private class DeleteAction extends AbstractAction {

        // set up action's name, icon, descriptions and mnemonic
        public DeleteAction() {
            putValue(NAME, "Delete");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/Delete24.png")));
            putValue(SHORT_DESCRIPTION, "Delete");
            putValue(LONG_DESCRIPTION,
                    "Delete an address book entry");
            putValue(MNEMONIC_KEY, new Integer('D'));
        }

        // delete entry
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    x = 0;
                    y = 0;
                    z = 0;
                    // get currently active window
                    AddressBookEntryFrame currentFrame =
                            (AddressBookEntryFrame) desktop.getSelectedFrame();

                    // get AddressBookEntry from window
                    AddressBookEntry person =
                            currentFrame.getAddressBookEntry();

                    // If personID is 0, this is new entry that has not
                    // been inserted. Therefore, delete is not necessary.
                    // Display message and return.
                    if (person.getPersonID() == 0) {
                        JOptionPane.showMessageDialog(desktop,
                                "New entries must be saved before they can be " +
                                        "deleted. \nTo cancel a new entry, simply " +
                                        "close the window containing the entry");
                        return;
                    }

                    // delete person
                    try {
                        database.deletePerson(person);

                        // display message indicating success
                        JOptionPane.showMessageDialog(desktop,
                                "Deletion successful");
                    }

                    // detect problems deleting person
                    catch (DataAccessException exception) {
                        JOptionPane.showMessageDialog(desktop, exception,
                                "Deletion failed", JOptionPane.ERROR_MESSAGE);
                        exception.printStackTrace();
                    }

                    // close current window and dispose of resources
                    currentFrame.dispose();
                }
            });

        }  // end method actionPerformed

    }  // end inner class DeleteAction

    // inner class defines action that locates entry
    private class SearchAction extends AbstractAction {

        // set up action's name, icon, descriptions and mnemonic
        public SearchAction() {
            putValue(NAME, "Search");
            putValue(SMALL_ICON, new ImageIcon(
                    getClass().getResource("images/Find24.png")));
            putValue(SHORT_DESCRIPTION, "Search");
            putValue(LONG_DESCRIPTION,
                    "Search for an address book entry");
            putValue(MNEMONIC_KEY, new Integer('r'));
        }

        // locate existing entry
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    x = 0;
                    y = 0;
                    z = 0;
                    String lastName =
                            JOptionPane.showInputDialog(desktop,
                                    "Enter last name");

                    // if last name was input, search for it; otherwise,
                    // do nothing
                    System.out.println("start search");
                    if (lastName != null) {
                        int extrasFields = 0b0;
                        //Calculate the amount of person entries/
                        try {
                            int temp = database.calculateEntries();
                            System.out.println("Amount of entries in the database: " + temp);
                        } catch (SQLException e1) {
                            //System.out.println("SQL exception");
                            e1.printStackTrace();
                        }


                        ArrayList<AddressBookEntry> person = new ArrayList<AddressBookEntry>();

                        person = database.findPerson(
                                lastName);

                        if (person != null) {

                            for (int i = 0; i < person.size(); i++) {

                                person.get(i).getExtraDetails();
                                x = 0;
                                y = 0;
                                z = 0;
                                extrasFields = person.get(i).getExtraDetails();

                                System.out.println("result of extrfield person: " + extrasFields + " " + i);
                                // create window to display AddressBookEntry
                                AddressBookEntryFrame entryFrame =
                                        createAddressBookEntryFrame(extrasFields);

                                // set AddressBookEntry to display
                                entryFrame.setAddressBookEntry(person.get(i), extrasFields);

                                // display window
                                desktop.add(entryFrame);
                                entryFrame.setVisible(true);
                            }
                        } else
                            JOptionPane.showMessageDialog(desktop,
                                    "Entry with last name \"" + lastName +
                                            "\" not found in address book");
                        //}

                    }  // end "if ( lastName == null )"
                }
            });

        }  // end method actionPerformed

    }  // end inner class SearchAction

    // inner class defines action that closes connection to
    // database and terminates program
    private class ExitAction extends AbstractAction {

        // set up action's name, descriptions and mnemonic
        public ExitAction() {
            putValue(NAME, "Exit");
            putValue(SHORT_DESCRIPTION, "Exit");
            putValue(LONG_DESCRIPTION, "Terminate the program");
            putValue(MNEMONIC_KEY, new Integer('x'));
        }

        // terminate program
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    shutDown();  // close database connection and terminate
                }
            });

        }

    }  // end inner class ExitAction
}


/**************************************************************************
 * (C) Copyright 2001 by Deitel & Associates, Inc. and Prentice Hall.     *
 * All Rights Reserved.                                                   *
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


//line 59 in CloudscapeDtaAccess need to come up with own version.