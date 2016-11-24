// Fig. 8.37: AddressBookEntryFrame.java
// A subclass of JInternalFrame customized to display and 
// an AddressBookEntry or set an AddressBookEntry's properties
// based on the current data in the UI.
package Assignment_6;

// Java core packages

import java.util.*;
import java.awt.*;

// Java extension packages
import javax.swing.*;

public class AddressBookEntryFrame extends JInternalFrame {

    //keep track of extra details
    Integer x = 0, y = 0, z = 0;
    // HashMap to store JTextField references for quick access
    private HashMap fields;

    // current AddressBookEntry set by AddressBook application
    private AddressBookEntry person;

    // panels to organize GUI
    private JPanel leftPanel, rightPanel, addPanel;

    // static integers used to determine new window positions
    // for cascading windows
    private static int xOffset = 0, yOffset = 0, oldxOffset = 0, oldyOffset = 0;

    // static Strings that represent name of each text field.
    // These are placed on JLabels and used as keys in
    // HashMap fields.
    private static final String FIRST_NAME = "First Name", LAST_NAME = "Last Name",
            ADDRESS1 = "Address 1",  ADDRESS2 = "Address 2", CITY = "City", STATE = "State", EIRCODE = "Eircode",
            ADDRESS1_2 = "(2)Address 1", ADDRESS2_2 = "(2)Address 2",CITY_2 = "(2)City", STATE_2 = "(2)State", EIRCODE_2 = "(2)Eircode",
            ADDRESS1_3 = "(3)Address 1", ADDRESS2_3 = "(3)Address 2",CITY_3 = "(3)City", STATE_3 = "(3)State", EIRCODE_3 = "(3)Eircode",
             PHONE = "Phone", PHONE1 = "2nd Phone", PHONE2 = "3rd Phone",
            EMAIL = "Email", EMAIL1 = "2nd Email", EMAIL2 = "3rd Email";

    // construct GUI
    public AddressBookEntryFrame(int  value ){
        super("Address Book Entry", true, true);



//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {


        int i = 0;
        int temp = 0b000000 | value;
        //System.out.println("Create value" + value);
        fields = new HashMap();
        leftPanel = new JPanel();
        rightPanel = new JPanel();

        Container container = getContentPane();
        container.add(leftPanel, BorderLayout.WEST);
        container.add(rightPanel, BorderLayout.CENTER);
        createRow(FIRST_NAME);
        createRow(LAST_NAME);
        createRow(ADDRESS1);
        createRow(ADDRESS2);
        createRow(CITY);
        createRow(STATE);
        createRow(EIRCODE);
        //System.out.println("temp: " + temp);
        //System.out.println((((value >> 4)  & 0b01)  & 0b1) == 0b1);

        if ((((value >> 4)  & 0b01) &  0b1) == 0b1) { i+=10;
            createRow(ADDRESS1_2);
            createRow(ADDRESS2_2);
            createRow(CITY_2);
            createRow(STATE_2);
            createRow(EIRCODE_2);

            createRow(ADDRESS1_3);
            createRow(ADDRESS2_3);
            createRow(CITY_3);
            createRow(STATE_3);
            createRow(EIRCODE_3);
        } else if (((value >> 5)  | 0b0) == 0b1) {i+=5;
            createRow(ADDRESS1_2);
            createRow(ADDRESS2_2);
            createRow(CITY_2);
            createRow(STATE_2);
            createRow(EIRCODE_2);
        }
        createRow(PHONE);
        //System.out.println("creating here hahaha");
        if ((((value >> 2)  & 0b0001) &  0b1) == 0b1) { i+=2;
            createRow(PHONE1);
            createRow(PHONE2);
        } else if ((((value >> 3)  & 0b001) &  0b1) == 0b1) { i++;
            createRow(PHONE1);
        }
        createRow(EMAIL);
        if (((value  & 0b000001) &  0b1) == 0b1) { i+=2;
            createRow(EMAIL1);
            createRow(EMAIL2);
        } else if ((((value >> 1)  & 0b01) &  0b1) == 0b1) { i++;
            createRow(EMAIL1);
        }

        //System.out.println("creating herer hahahhahah");
        leftPanel.setLayout(new GridLayout(9 + i, 1, 0, 5));

        rightPanel.setLayout(new GridLayout(9 + i, 1, 0, 5));

        setBounds(xOffset, yOffset, 350, 300 + (30 * i));
        oldxOffset = xOffset;
        oldyOffset = yOffset;
        xOffset = (xOffset + 30) % 300;
        yOffset = (yOffset + 30) % 300;

//                                       }
//        });
    }

    public void email(int X, int Y, int Z, int extras) {
        this.z = Z;
        this.x = X;
        this.y = Y;
        //System.out.println("value of Y:" + y);
        createRC(extras);
    }

    public void phoneNumber(int X, int Y, int Z , int extras) {
        this.z = Z;
        this.x = X;
        this.y = Y;
        createRC(extras);
    }

    public void address(int X, int Y, int Z,  int extras ) {
        this.z = Z;
        this.x = X;
        this.y = Y;
        createRC(extras);
    }

    public void createRC( int extras) {


//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {

        System.out.println("Creating new fields");
        int i = 0;
        leftPanel.removeAll();
        rightPanel.removeAll();
        Container container = getContentPane();
        container.add(leftPanel, BorderLayout.WEST);
        container.add(rightPanel, BorderLayout.CENTER);

        createRow(FIRST_NAME);
        createRow(LAST_NAME);
        createRow(ADDRESS1);
        createRow(ADDRESS2);
        createRow(CITY);
        createRow(STATE);
        createRow(EIRCODE);


        if (z.equals(1) || z.equals(2)) {
            System.out.println("value is one or two");
            if (z.equals(1)) {
                i += 5;
                createRow(ADDRESS1_2);
                createRow(ADDRESS2_2);
                createRow(CITY_2);
                createRow(STATE_2);
                createRow(EIRCODE_2);
                System.out.println("Just first one ");
            }
            if (z.equals(2)) {
                i += 10;
                createRow(ADDRESS1_2);
                createRow(ADDRESS2_2);
                createRow(CITY_2);
                createRow(STATE_2);
                createRow(EIRCODE_2);

                createRow(ADDRESS1_3);
                createRow(ADDRESS2_3);
                createRow(CITY_3);
                createRow(STATE_3);
                createRow(EIRCODE_3);
                //System.out.println("Create second one");
            }
        } else if ((((extras >> 5) | 0b0) == 0b1) || ((((extras >> 4) & 0b01) & 0b1) == 0b1)) {
            if ((((extras >> 5) | 0b0) == 0b1)) {
                i += 5;
                createRow(ADDRESS1_2);
                createRow(ADDRESS2_2);
                createRow(CITY_2);
                createRow(STATE_2);
                createRow(EIRCODE_2);
                // System.out.println("Just first one ");
            }
            if (((((extras >> 4) & 0b01) & 0b1) == 0b1)) {
                i += 5;
                createRow(ADDRESS1_2);
                createRow(ADDRESS2_2);
                createRow(CITY_2);
                createRow(STATE_2);
                createRow(EIRCODE_2);
                System.out.println("Create second one");
            }
        }

        // System.out.println("create phone rows");
        createRow(PHONE);

        if (x.equals(1) || x.equals(2)) {
            if(x.equals(1)){i++;
                createRow(PHONE1);
            }
            else if (x.equals(2)) {i += 2;
                createRow(PHONE1);
                createRow(PHONE2);
            }
        } else if (((((extras >> 3)  & 0b001) &  0b1) == 0b1) || ((((extras >> 2)  & 0b0001) &  0b1) == 0b1)) {

            if (((((extras >> 2)  & 0b0001) &  0b1) == 0b1)) { i++;
                createRow(PHONE2);
            }
            if (((((extras >> 3)  & 0b001) &  0b1) == 0b1)) { i++;
                createRow(PHONE1);
            }
        }


        createRow(EMAIL);
        if (y.equals(1) || y.equals(2)) {
            if (  y.equals(1)) {i++;
                createRow(EMAIL1);
            }
            if (y.equals(2)) {i+=2;
                createRow(EMAIL1);
                createRow(EMAIL2);
                System.out.println("Create second one");
            }
        } else if( ((((extras >> 1)  & 0b01) &  0b1) == 0b1) || (((extras  & 0b000001) &  0b1) == 0b1) ){
            if (  ((((extras >> 1)  & 0b01) &  0b1) == 0b1)) {i++;
                createRow(EMAIL1);
            }
            if ((((extras  & 0b000001) &  0b1) == 0b1)) {i++;
                createRow(EMAIL2);
            }
        }

        leftPanel.setLayout(new GridLayout(9 + i, 1, 0, 5));
        rightPanel.setLayout(new GridLayout(9 + i, 1, 0, 5));
        setBounds(oldxOffset, oldyOffset, 350, 300 + (30 * i));
        System.out.println("Created rows and columns");

//                                       }
//        });
    }


    // set AddressBookEntry then use its properties to
    // place data in each JTextField
    public void setAddressBookEntry(AddressBookEntry entry, int extras) {
        person = entry;
        System.out.println("Setting address book");

        setField(FIRST_NAME, person.getFirstName());
        setField(LAST_NAME, person.getLastName());
        setField(ADDRESS1, person.getAddress1());
        setField(ADDRESS2, person.getAddress2());
        setField(CITY, person.getCity());
        setField(STATE, person.getState());
        setField(EIRCODE, person.getEircode());



        if ((((extras >> 5) | 0b0) == 0b1) || ((((extras >> 4) & 0b01) & 0b1) == 0b1)) {

            if ((((extras >> 5) | 0b0) == 0b1)) {
                setField(ADDRESS1_2, person.getAddress1_2());
                setField(ADDRESS2_2, person.getAddress2_2());
                setField(CITY_2, person.getCity_2());
                setField(STATE_2, person.getState_2());
                setField(EIRCODE_2, person.getEircode_2());
            }
            if (((((extras >> 4) & 0b01) & 0b1) == 0b1)) {

                setField(ADDRESS1_3, person.getAddress1_3());
                setField(ADDRESS2_3, person.getAddress2_3());
                setField(CITY_3, person.getCity_3());
                setField(STATE_3, person.getState_3());
                setField(EIRCODE_3, person.getEircode_3());
            }

        } else if (z.equals(1) || z.equals(2)) {

            if (z.equals(1)) {
                setField(ADDRESS1_2, person.getAddress1_2());
                setField(ADDRESS2_2, person.getAddress2_2());
                setField(CITY_2, person.getCity_2());
                setField(STATE_2, person.getState_2());
                setField(EIRCODE_2, person.getEircode_2());
            }

            if (z.equals(2)) {
                setField(ADDRESS1_2, person.getAddress1_2());
                setField(ADDRESS2_2, person.getAddress2_2());
                setField(CITY_2, person.getCity_2());
                setField(STATE_2, person.getState_2());
                setField(EIRCODE_2, person.getEircode_2());

                setField(ADDRESS1_3, person.getAddress1_3());
                setField(ADDRESS2_3, person.getAddress2_3());
                setField(CITY_3, person.getCity_3());
                setField(STATE_3, person.getState_3());
                setField(EIRCODE_3, person.getEircode_3());
            }
        }


        setField(PHONE, person.getPhoneNumber());
        if (x.equals(1) || ((((extras >> 3) & 0b001) & 0b1) == 0b1) || x.equals(2) || ((((extras >> 2) & 0b0001) & 0b1) == 0b1)) {

            if (((((extras >> 3)  & 0b001) &  0b1) == 0b1)) {
                setField(PHONE1, person.getPhoneNumber1());
            }
            if ( ((((extras >> 2)  & 0b0001) &  0b1) == 0b1)) {
                setField(PHONE2, person.getPhoneNumber2());
            }

        } else if (x.equals(2) || x.equals(1)) {
            if(x.equals(1)){
                setField(PHONE1, person.getPhoneNumber1());
            }else if(x.equals(2)){
                setField(PHONE1, person.getPhoneNumber1());
                setField(PHONE2, person.getPhoneNumber2());
            }

        }

//        if (x.equals(2) || ((((extras >> 2)  & 0b0001) &  0b1) == 0b1) ) {
//
//            setField(PHONE1, person.getPhoneNumber1());
//            setField(PHONE2, person.getPhoneNumber2());
//        }else  if (x.equals(1) || ((((extras >> 3)  & 0b001) &  0b1) == 0b1)) {
//            //System.out.println("setting phone number");
//            setField(PHONE1, person.getPhoneNumber1());
//        }





        setField(EMAIL, person.getEmailAddress());

        if( ((((extras >> 1)  & 0b01) &  0b1) == 0b1) || (((extras  & 0b000001) &  0b1) == 0b1) ){
            if (  ((((extras >> 1)  & 0b01) &  0b1) == 0b1)) {
                setField(EMAIL1, person.getEmailAddress1());
                System.out.println("Just first one ");
            }
            if ((((extras  & 0b000001) &  0b1) == 0b1)) {
                setField(EMAIL2, person.getEmailAddress2());
                System.out.println("Create second one");
            }
        }else if (y.equals(1) || y.equals(2)) {
            //System.out.println("value is one or two");
            if (  y.equals(1)) {
                setField(EMAIL1, person.getEmailAddress1());
                //System.out.println("Just first one ");
            }
            if (y.equals(2)) {
                setField(EMAIL1, person.getEmailAddress1());
                setField(EMAIL2, person.getEmailAddress2());
                //System.out.println("Create second one");
            }
        }


    }

    // store AddressBookEntry data from GUI and return
    // AddressBookEntry
    public AddressBookEntry getAddressBookEntry() {
        System.out.println("getting address book");

        Integer temp4 = person.getAddDetails();
        Integer temp5 = person.getAddDetailsEmail();
        Integer temp6 = person.getAddDetailsPhone();
        person.setFirstName(getField(FIRST_NAME));
        person.setLastName(getField(LAST_NAME));
        person.setAddress1(getField(ADDRESS1));
        person.setAddress2(getField(ADDRESS2));
        person.setCity(getField(CITY));
        person.setState(getField(STATE));
        person.setEircode(getField(EIRCODE));


        if (temp4.equals(1) || temp4.equals(2)) {
            if (temp4.equals(1)) {
                System.out.println("setting second address using temp, Perosn value: " + person.getAddDetails());
                person.setAddress1_2(getField(ADDRESS1_2));
                person.setAddress2_2(getField(ADDRESS2_2));
                person.setCity_2(getField(CITY_2));
                person.setState_2(getField(STATE_2));
                person.setEircode_2(getField(EIRCODE_2));

            } else if (temp4.equals(2)) {

                System.out.println("setting third address using temp");
                person.setAddress1_2(getField(ADDRESS1_2));
                person.setAddress2_2(getField(ADDRESS2_2));
                person.setCity_2(getField(CITY_2));
                person.setState_2(getField(STATE_2));
                person.setEircode_2(getField(EIRCODE_2));

                person.setAddress1_3(getField(ADDRESS1_3));
                person.setAddress2_3(getField(ADDRESS2_3));
                person.setCity_3(getField(CITY_3));
                person.setState_3(getField(STATE_3));
                person.setEircode_3(getField(EIRCODE_3));
            }

        } else if (z.equals(1) || z.equals(2)) {
            if (z.equals(1)) {
                System.out.println("setting second address using Z");

                person.setAddress1_2(getField(ADDRESS1_2));
                person.setAddress2_2(getField(ADDRESS2_2));
                person.setCity_2(getField(CITY_2));
                person.setState_2(getField(STATE_2));
                person.setEircode_2(getField(EIRCODE_2));

            } else if (z.equals(2)) {
                System.out.println("setting third address using Z");
                person.setAddress1_2(getField(ADDRESS1_2));
                person.setAddress2_2(getField(ADDRESS2_2));
                person.setCity_2(getField(CITY_2));
                person.setState_2(getField(STATE_2));
                person.setEircode_2(getField(EIRCODE_2));

                person.setAddress1_3(getField(ADDRESS1_3));
                person.setAddress2_3(getField(ADDRESS2_3));
                person.setCity_3(getField(CITY_3));
                person.setState_3(getField(STATE_3));
                person.setEircode_3(getField(EIRCODE_3));
            }
        } else if (person.getAddress1_3().equals("") == false || person.getAddress1_2().equals("") == false) {

            if (person.getAddress1_3().equals("") == false) {

                System.out.println("setting third address using false");
                person.setAddress1_3(getField(ADDRESS1_3));
                person.setAddress2_3(getField(ADDRESS2_3));
                person.setCity_3(getField(CITY_3));
                person.setState_3(getField(STATE_3));
                person.setEircode_3(getField(EIRCODE_3));

            }
            if (person.getAddress1_2().equals("") == false) {

                System.out.println("setting second address  using false");
                person.setAddress1_2(getField(ADDRESS1_2));
                person.setAddress2_2(getField(ADDRESS2_2));
                person.setCity_2(getField(CITY_2));
                person.setState_2(getField(STATE_2));
                person.setEircode_2(getField(EIRCODE_2));

            }

        }


        person.setPhoneNumber(getField(PHONE));

        if (temp6.equals(1) || temp6.equals(2)) {
            if (temp6.equals(1)) {
                System.out.println("setting second Oine using temp6");
                person.setPhoneNumber1(getField(PHONE1));
            } else if (temp6.equals(2)) {
                person.setPhoneNumber1(getField(PHONE1));
                person.setPhoneNumber2(getField(PHONE2));
            }
        } else if (x.equals(1) || x.equals(2)) {
            if (x.equals(12)) {
                // System.out.println("third phone number");
                person.setPhoneNumber1(getField(PHONE1));

            } else if (x.equals(2)) {
                // System.out.println("third phone number");
                person.setPhoneNumber1(getField(PHONE1));
                person.setPhoneNumber2(getField(PHONE2));

            }
        }else if (person.getPhoneNumber2().equals("") == false || person.getPhoneNumber1().equals("") == false) {
            if (person.getPhoneNumber2().equals("") == false) {
                person.setPhoneNumber1(getField(PHONE1));
                person.setPhoneNumber2(getField(PHONE2));
            }
            if (person.getPhoneNumber1().equals("") == false) {
                person.setPhoneNumber1(getField(PHONE1));
            }
        }


        person.setEmailAddress(getField(EMAIL));

        if(temp5.equals(1) || temp5.equals(2)){
            if(temp5.equals(1)){
                System.out.println("setting second email using temp5");
                person.setEmailAddress1(getField(EMAIL1));
            }else if (temp5.equals(2)){
                System.out.println("setting third address using temp5");
                person.setEmailAddress1(getField(EMAIL1));
                person.setEmailAddress2(getField(EMAIL2));
            }
        }else if(y.equals(1) || y.equals(2)){
            if (y.equals(1)) {
                person.setEmailAddress1(getField(EMAIL1));
            } else if (y.equals(2)) {
                person.setEmailAddress1(getField(EMAIL1));
                person.setEmailAddress2(getField(EMAIL2));
            }
        }else if(person.getEmailAddress2().equals("") == false || person.getEmailAddress1().equals("") == false) {

            if (person.getEmailAddress2().equals("") == false) {
                //System.out.println("third phone number");
                person.setEmailAddress2(getField(EMAIL2));
            }
            if (person.getEmailAddress1().equals("") == false) {
                System.out.println("secind phone");
                person.setEmailAddress1(getField(EMAIL1));
            }
        }



        return person;
    }

    // set text in JTextField by specifying field's
    // name and value
    private void setField(String fieldName, String value) {
        JTextField field =
                (JTextField) fields.get(fieldName);

        field.setText(value);
    }

    // get text in JTextField by specifying field's name
    private String getField(String fieldName) {
        JTextField field =
                (JTextField) fields.get(fieldName);

        return field.getText();
    }

    // utility method used by constructor to create one row in
    // GUI containing JLabel and JTextField
    private void createRow(String name) {
        JLabel label = new JLabel(name, SwingConstants.RIGHT);
        label.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        leftPanel.add(label);

        JTextField field = new JTextField(30);
        rightPanel.add(field);


        fields.put(name, field);
    }
}  // end class AddressBookEntryFrame


/**************************************************************************
 * (C) Copyright 2001 by Deitel & Associates, Inc. and Prentice Hall.     *
 * All Rights Reserved.                                                   *
 *                                         ch                               *
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
