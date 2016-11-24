// Fig. 8.36: CloudscapeDataAccess.java
// An implementation of interface AddressBookDataAccess that 
// performs database operations with PreparedStatements.
package Assignment_6;

// Java core packages

import java.sql.*;
import java.util.ArrayList;

public class CloudscapeDataAccess
        implements AddressBookDataAccess {

    // reference to database connection
    private Connection connection;

    // reference to prepared statement for locating entry
    private PreparedStatement sqlFind;

    // reference to prepared statement for determining personID
    private PreparedStatement sqlPersonID;

    // references to prepared statements for inserting entry
    private PreparedStatement sqlInsertName;
    private PreparedStatement sqlInsertAddress;
    private PreparedStatement sqlInsertPhone;
    private PreparedStatement sqlInsertEmail;

    // references to prepared statements for updating entry
    private PreparedStatement sqlUpdateName;
    private PreparedStatement sqlUpdateAddress;
    private PreparedStatement sqlUpdatePhone;
    private PreparedStatement sqlUpdateEmail;

    // references to prepared statements for updating entry
    private PreparedStatement sqlDeleteName;
    private PreparedStatement sqlDeleteAddress;
    private PreparedStatement sqlDeletePhone;
    private PreparedStatement sqlDeleteEmail;


    //used to count how many entries are in the database
    private PreparedStatement sqlCountEntries;
    private PreparedStatement sqlGetNameDatA;
    private PreparedStatement sqlGetEmailDatA;
    private PreparedStatement sqlGetPhoneDatA;
    private PreparedStatement sqlGetAddressDatA;

    private PreparedStatement sqlGetEmailID;
    private PreparedStatement sqlGetPhoneID;
    private PreparedStatement sqlGetAddressID;
    private PreparedStatement sqlDeleteAddressID;
    private PreparedStatement sqlDeletePhoneUsingID;
    private PreparedStatement sqlDeleteEmailUsingID;

    // set up PreparedStatements to access database
    public CloudscapeDataAccess() throws Exception {
        // connect to addressbook database
        connect();

        // locate person
        sqlFind = connection.prepareStatement("SELECT personID FROM names WHERE lastName LIKE   ?");
//                "SELECT names.personID, firstName, lastName, " +
//                        "addressID, address1, address2, city, state, " +
//                        "zipcode, phoneID, phoneNumber, emailID, " +
//                        "emailAddress " +
//                        "FROM names, addresses, phoneNumbers, emailAddresses " +
//                        "WHERE lastName = ? AND " +
//                        "names.personID = addresses.personID AND " +
//                        "names.personID = phoneNumbers.personID AND " +
//                        "names.personID = emailAddresses.personID");

        // Obtain personID for last person inserted in database.
        // [This is a Cloudscape-specific database operation.]
        sqlPersonID = connection.prepareStatement("SELECT MAX(personID) FROM names");

        sqlGetNameDatA = connection.prepareStatement("SELECT firstname , lastName FROM names WHERE personID = ?");


        sqlGetEmailDatA = connection.prepareStatement(" SELECT emailAddress FROM emailAddresses WHERE personID = ?");


        sqlGetPhoneDatA = connection.prepareStatement("SELECT phoneNumber FROM phoneNumbers WHERE personID = ?");


        sqlGetAddressDatA = connection.prepareStatement("SELECT address1, address2 ,city, state, zipcode FROM addresses" +
                " WHERE personID = ?");

        //Obtain amount of entries in the database
        sqlCountEntries = connection.prepareStatement("SELECT COUNT(personID) FROM names");


        sqlGetEmailID = connection.prepareStatement("SELECT emailID FROM emailAddresses WHERE personID = ?");

        sqlGetPhoneID = connection.prepareStatement("SELECT phoneID FROM phoneNumbers WHERE personID = ? ");

        sqlGetAddressID = connection.prepareStatement("SELECT addressID FROM addresses WHERE personID = ? ");

        // Insert first and last names in table names.
        // For referential integrity, this must be performed
        // before sqlInsertAddress, sqlInsertPhone and
        // sqlInsertEmail.
        sqlInsertName = connection.prepareStatement(
                "INSERT INTO names ( firstName, lastName ) " +
                        "VALUES ( ? , ? )");

        // insert address in table addresses
        sqlInsertAddress = connection.prepareStatement(
                "INSERT INTO addresses ( personID, address1, " +
                        "address2, city, state, zipcode ) " +
                        "VALUES ( ? , ? , ? , ? , ? , ? )");

        // insert phone number in table phoneNumbers
        sqlInsertPhone = connection.prepareStatement(
                "INSERT INTO phoneNumbers " +
                        "( personID, phoneNumber) " +
                        "VALUES ( ? , ? )");

        // insert email in table emailAddresses
        sqlInsertEmail = connection.prepareStatement(
                "INSERT INTO emailAddresses " +
                        "( personID, emailAddress ) " +
                        "VALUES ( ? , ? )");

        // update first and last names in table names
        sqlUpdateName = connection.prepareStatement(
                "UPDATE names SET firstName = ?, lastName = ? " +
                        "WHERE personID = ?");

        // update address in table addresses
        sqlUpdateAddress = connection.prepareStatement(
                "UPDATE addresses SET address1 = ?, address2 = ?, " +
                        "city = ?, state = ?, zipcode = ? " +
                        "WHERE personID = ? AND addressID = ?");

        // update phone number in table phoneNumbers
        sqlUpdatePhone = connection.prepareStatement(
                "UPDATE phoneNumbers SET phoneNumber = ? WHERE personID = ? AND phoneID = ? ");

        // update email in table emailAddresses
        sqlUpdateEmail = connection.prepareStatement(
                "UPDATE emailAddresses SET emailAddress = ?  WHERE personID = ? AND emailID = ? ");

        // Delete row from table names. This must be executed
        // after sqlDeleteAddress, sqlDeletePhone and
        // sqlDeleteEmail, because of referential integrity.
        sqlDeleteName = connection.prepareStatement(
                "DELETE FROM names WHERE personID = ?");

        // delete address from table addresses
        sqlDeleteAddress = connection.prepareStatement(
                "DELETE FROM addresses WHERE personID = ?");

        // delete address from table addresses
        sqlDeleteAddressID = connection.prepareStatement(
                "DELETE FROM addresses WHERE personID = ? AND addressID = ?");

        // delete phone number from table phoneNumbers
        sqlDeletePhone = connection.prepareStatement(
                "DELETE FROM phoneNumbers WHERE personID = ?");

        sqlDeleteEmailUsingID = connection.prepareStatement(
                "DELETE FROM emailAddresses WHERE personID = ? AND emailID = ?");

        // delete phone number from table phoneNumbers
        sqlDeletePhoneUsingID = connection.prepareStatement(
                "DELETE FROM phoneNumbers WHERE personID = ? AND phoneID = ?");

        // delete email address from table emailAddresses
        sqlDeleteEmail = connection.prepareStatement(
                "DELETE FROM emailAddresses WHERE personID = ?");
    }  // end CloudscapeDataAccess constructor

    // Obtain a connection to addressbook database. Method may
    // may throw ClassNotFoundException or SQLException. If so,
    // exception is passed via this class's constructor back to
    // the AddressBook application so the application can display
    // an error message and terminate.
    private void connect() throws Exception {
        // Cloudscape database driver class name
        String driver = "com.mysql.jdbc.Driver";

        // URL to connect to addressbook database
        String url = "jdbc:mysql://localhost:3306/addressbook?autoReconnect=true&useSSL=false";

        // load database driver class
        Class.forName(driver);

        // connect to database
        connection = DriverManager.getConnection(url, "root", "root");

        // Require manual commit for transactions. This enables
        // the program to rollback transactions that do not
        // complete and commit transactions that complete properly.
        connection.setAutoCommit(false);
    }

    // Locate specified person. Method returns AddressBookEntry
    // containing information.
    public ArrayList<AddressBookEntry> findPerson(String lastName) {

        ArrayList<AddressBookEntry> returnedPeople = new ArrayList<AddressBookEntry>();
        StringBuilder sb = new StringBuilder();
        int x = 0;
        //int extrasFields = 0b0;
        try {
            // set query parameter and execute query
            sqlFind.setString(1, lastName);
            ResultSet resultSet = sqlFind.executeQuery();

            // if no records found, return immediately
            if (!resultSet.next())
                return null;
            else {
                do {
                    sb.append(resultSet.getInt(1) + "#");
                    // System.out.println("result set: " + sb.toString());
                } while (resultSet.next());
            }
            String[] tokens = sb.toString().split("#");

            for (int i = 0; i < tokens.length; i++) {
                int extrasFields = 0b0;
                //System.out.println(tokens.length  +  "token value: " + tokens[i]);
                AddressBookEntry person = new AddressBookEntry(
                        Integer.parseInt(tokens[i]));                   //set Person ID using the value in token

                System.out.println("creating person object");
                // set query parameter and execute query
                sqlGetNameDatA.setInt(1, Integer.parseInt(tokens[i]));
                //System.out.println("setting paramater");
                ResultSet resultSet1 = sqlGetNameDatA.executeQuery();
                //System.out.println("returned query");
                if (!resultSet1.next())
                    System.out.println("no data in first name");

                // System.out.println("getting firstname:"  + resultSet1.getString(1));
                person.setFirstName(resultSet1.getString(1));
                person.setLastName(lastName);

                //System.out.println("starting getting addresss data");
                sqlGetAddressDatA.setInt(1, Integer.parseInt(tokens[i]));
                ResultSet resultSet2 = sqlGetAddressDatA.executeQuery();
                //System.out.println("here2");
                if (!resultSet2.next()) {
                    System.out.println("no adreess ");
                } else {

                    //System.out.println("Getting address data: Readin address1: " +resultSet2.getString(1));
                    person.setAddressID(Integer.parseInt(tokens[i]));
                    person.setAddress1(resultSet2.getString(1));
                    person.setAddress2(resultSet2.getString(2));
                    person.setCity(resultSet2.getString(3));
                    person.setState(resultSet2.getString(4));
                    person.setEircode(resultSet2.getString(5));
                }

                System.out.println("Getting 2nd address data");
                if (resultSet2.next()) {
                    System.out.println("Getting 2nd address data1");
                    extrasFields = (extrasFields | 0b100000);
                    person.setAddress1_2(resultSet2.getString(1));
                    person.setAddress2_2(resultSet2.getString(2));
                    person.setCity_2(resultSet2.getString(3));
                    person.setState_2(resultSet2.getString(4));
                    person.setEircode_2(resultSet2.getString(5));
                }
                System.out.println("Getting third address data");
                if (resultSet2.next()) {
                    System.out.println("Getting third address data3");
                    extrasFields = (extrasFields | 0b010000);
                    person.setAddress1_3(resultSet2.getString(1));
                    person.setAddress2_3(resultSet2.getString(2));
                    person.setCity_3(resultSet2.getString(3));
                    person.setState_3(resultSet2.getString(4));
                    person.setEircode_3(resultSet2.getString(5));
                }

                System.out.println("getting Phone data");

                sqlGetPhoneDatA.setInt(1, Integer.parseInt(tokens[i]));
                //System.out.println("getting Phone data2");
                ResultSet resultSet3 = sqlGetPhoneDatA.executeQuery();
                //System.out.println("query executed value of phone: " );

                if (!resultSet3.next()) {
                    System.out.println("no Phone Number ");

                } else {

                    person.setPhoneID(Integer.parseInt(tokens[i]));
                    person.setPhoneNumber(resultSet3.getString(1));
                    //System.out.println("query executed value of phone: " + resultSet3.getString(1));
                }

                if (resultSet3.next()) {
                    extrasFields = (extrasFields | 0b001000);
                    person.setPhoneNumber1(resultSet3.getString(1));
                } else System.out.println("No second phone number ");

                if (resultSet3.next()) {
                    extrasFields = (extrasFields | 0b000100);
                    person.setPhoneNumber2(resultSet3.getString(1));
                    System.out.println("Third phone number value: " + resultSet3.getString(1));
                } else System.out.println("No third phone number ");


                sqlGetEmailDatA.setInt(1, Integer.parseInt(tokens[i]));
                ResultSet resultSet4 = sqlGetEmailDatA.executeQuery();

                if (!resultSet4.next()) {
                    System.out.println("No Data");
                } else {
                    person.setEmailID(Integer.parseInt(tokens[i]));
                    person.setEmailAddress(resultSet4.getString(1));
                    // System.out.println("Email Address: " + resultSet4.getString(1));
                }

                if (resultSet4.next()) {
                    extrasFields = (extrasFields | 0b000010);
                    person.setEmailAddress1(resultSet4.getString(1));
                    System.out.println(person.getEmailAddress1());
                } else System.out.println("No second Email number ");
                if (resultSet4.next()) {
                    extrasFields = (extrasFields | 0b000001);
                    person.setEmailAddress2(resultSet4.getString(1));
                } else System.out.println("No third Eail number ");

                //System.out.println("add person to array");
                returnedPeople.add(person);
                System.out.println("binery sum: " + extrasFields);
                person.setExtraDetails(extrasFields);

            }

            return returnedPeople;
        }

        // catch SQLException
        catch (SQLException sqlException) {
            return null;
        }
    }  // end method findPerson

    // Update an entry. Method returns boolean indicating
    // success or failure.
    public boolean savePerson(AddressBookEntry person)
            throws DataAccessException {
        // update person in database
        try {
            int result;
            Integer count = 0;
            sqlUpdateName.setString(1, person.getFirstName());
            sqlUpdateName.setString(2, person.getLastName());
            sqlUpdateName.setInt(3, person.getPersonID());
            result = sqlUpdateName.executeUpdate();
            if (result == 0) {
                connection.rollback(); // rollback update
                System.out.println("connection failed");
                return false;          // update unsuccessful
            }


            // set query parameter and execute query
            sqlGetAddressID.setInt(1, person.getPersonID());
            ResultSet resultSet2 = sqlGetAddressID.executeQuery();
            int[] temp2 = new int[3];



            Integer temp4 = person.getAddDetails();
            Integer temp5 = person.getAddDetailsEmail();
            Integer temp6 = person.getAddDetailsPhone();
            System.out.println("AddDetails: " + temp4);
//
            if(temp4.equals(1) ){
                System.out.println("Here:" + person.getAddress1_2());
                System.out.println("Creating new entry with temp4 adress2");
                sqlInsertAddress.setInt(1, person.getPersonID());
                sqlInsertAddress.setString(2,
                        person.getAddress1_2());
                sqlInsertAddress.setString(3,
                        person.getAddress2_2());
                sqlInsertAddress.setString(4,
                        person.getCity_2());
                sqlInsertAddress.setString(5,
                        person.getState_2());
                sqlInsertAddress.setString(6,
                        person.getEircode_2());
                result = sqlInsertAddress.executeUpdate();

                // if insert fails, rollback and discontinue
                if (result == 0) {
                    System.out.println("Fallback Address if insert fails: " + result);
                    connection.rollback(); // rollback insert
                    return false;          // insert unsuccessful
                }
            }

            if(temp4.equals(2) ){

                // if insert fails, rollback and discontinue
                if (result == 0) {
                    System.out.println("Fallback Address if insert fails: " + result);
                    connection.rollback(); // rollback insert
                    return false;          // insert unsuccessful
                }
                System.out.println("Creating new entry with temp4 adress3");
                sqlInsertAddress.setInt(1, person.getPersonID());
                sqlInsertAddress.setString(2,
                        person.getAddress1_3());
                sqlInsertAddress.setString(3,
                        person.getAddress2_3());
                sqlInsertAddress.setString(4,
                        person.getCity_3());
                sqlInsertAddress.setString(5,
                        person.getState_3());
                sqlInsertAddress.setString(6,
                        person.getEircode_3());
                result = sqlInsertAddress.executeUpdate();

                // if insert fails, rollback and discontinue
                if (result == 0) {
                    System.out.println("Fallbcak Address if insert fails: " + result);
                    connection.rollback();
                    return false;
                }
            }
            connection.commit();   // commit update

            if(temp5.equals(1) ){
                //System.out.println("Here:" + person.getEmailAddress1());

                // insert email address in emailAddresses table
                sqlInsertEmail.setInt(1, person.getPersonID());
                sqlInsertEmail.setString(2,
                        person.getEmailAddress1());
                result = sqlInsertEmail.executeUpdate();
                System.out.println("Fallbcak if fail email number" + result);
                // if insert fails, rollback and discontinue
                if (result == 0) {
                    connection.rollback(); // rollback insert
                    return false;          // insert unsuccessful
                }

            }
            if(temp5.equals(2) ){
                System.out.println("Here:" + person.getEmailAddress1());



                // insert email address in emailAddresses table
                sqlInsertEmail.setInt(1, person.getPersonID());
                sqlInsertEmail.setString(2,
                        person.getEmailAddress2());
                result = sqlInsertEmail.executeUpdate();
                System.out.println("Fallbcak if fail email number" + result);
                // if insert fails, rollback and discontinue
                if (result == 0) {
                    connection.rollback(); // rollback insert
                    return false;          // insert unsuccessful
                }

            }
            connection.commit();   // commit update

            if(temp6.equals(1) ){
                sqlInsertPhone.setInt(1, person.getPersonID());
                sqlInsertPhone.setString(2,
                        person.getEmailAddress1());
                result = sqlInsertPhone.executeUpdate();
                System.out.println("Fallbcak if fail Phone number 2" + result);
                // if insert fails, rollback and discontinue
                if (result == 0) {
                    connection.rollback(); // rollback insert
                    return false;          // insert unsuccessful
                }

            }
            if(temp6.equals(2) ){
                System.out.println("Here:" + person.getEmailAddress1());
                sqlInsertPhone.setInt(1, person.getPersonID());
                sqlInsertPhone.setString(2,
                        person.getEmailAddress2());
                result = sqlInsertPhone.executeUpdate();
                System.out.println("Fallbcak if fail Phone number 3" + result);
                // if insert fails, rollback and discontinue
                if (result == 0) {
                    connection.rollback(); // rollback insert
                    return false;          // insert unsuccessful
                }
            }
            connection.commit();   // commit update



            System.out.println("Updating user addresses");
            // if no records found, return immediately
            if (!resultSet2.next()) {
                System.out.println("no data addresses");
            } else {
                temp2[0] = resultSet2.getInt(1);
                //System.out.println("First value returned" + temp2[0]);


                if (person.getAddress1().equals("") && person.getAddress2().equals("")
                        && person.getCity().equals("") && person.getEircode().equals("")) {

                    //delete phone number
                    System.out.println("blank Address deletaing full address");

                    sqlDeleteAddressID.setInt(1, person.getPersonID());
                    sqlDeleteAddressID.setInt(2, temp2[0]);
                    System.out.println(person.getPersonID());
                    //System.out.println("deleting address one");
                    result = sqlDeleteAddressID.executeUpdate();
                    System.out.println(result);
                    person.setAddress1("");
                    person.setAddress2("");
                    person.setCity("");
                    person.setState("");
                    person.setEircode("");

                    if (result == 0) {
                        System.out.println("failure in deleting Address one");
                        connection.rollback(); // rollback update
                        return false;          // update unsuccessful
                    }
                    count++;
                } else {

                    sqlUpdateAddress.setString(1, person.getAddress1());
                    sqlUpdateAddress.setString(2, person.getAddress2());
                    sqlUpdateAddress.setString(3, person.getCity());
                    sqlUpdateAddress.setString(4, person.getState());
                    sqlUpdateAddress.setString(5, person.getEircode());
                    sqlUpdateAddress.setInt(6, person.getPersonID());
                    sqlUpdateAddress.setInt(7, temp2[0]);
                    result = sqlUpdateAddress.executeUpdate();
                    System.out.println("Updating Email addresses one");
                    // if update fails, rollback and discontinue
                    if (result == 0) {
                        System.out.println("Failure in updating Email one");
                        connection.rollback(); // rollback update
                        return false;          // update unsuccessful
                    }
                }


                if (resultSet2.next()) {

                    temp2[1] = resultSet2.getInt(1);


                    if (person.getAddress1_2().equals("") && person.getAddress2_2().equals("")
                            && person.getCity_2().equals("") && person.getEircode_2().equals("")) {

                        //delete phone number
                        System.out.println("blank Address deleting address two");

                        sqlDeleteAddressID.setInt(1, person.getPersonID());
                        sqlDeleteAddressID.setInt(2, temp2[1]);
                        System.out.println(person.getPersonID());
                        System.out.println(temp2[1]);
                        result = sqlDeleteAddressID.executeUpdate();
                        System.out.println(result);

                        person.setAddress1_2("");
                        person.setAddress2_2("");
                        person.setCity_2("");
                        person.setState_2("");
                        person.setEircode_2("");


                        if (result == 0) {
                            System.out.println("failure in deleting Address two");
                            connection.rollback(); // rollback update
                            return false;          // update unsuccessful
                        }
                        count += 2;
                    } else {
                        sqlUpdateAddress.setString(1, person.getAddress1_2());
                        sqlUpdateAddress.setString(2, person.getAddress2_2());
                        sqlUpdateAddress.setString(3, person.getCity_2());
                        sqlUpdateAddress.setString(4, person.getState_2());
                        sqlUpdateAddress.setString(5, person.getEircode_2());
                        sqlUpdateAddress.setInt(6, person.getPersonID());
                        sqlUpdateAddress.setInt(7, temp2[1]);
                        result = sqlUpdateAddress.executeUpdate();
                        System.out.println("updating Email addresses two");
                        // if update fails, rollback and discontinue
                        if (result == 0) {
                            System.out.println("failure in updating Address two");
                            connection.rollback(); // rollback update
                            return false;          // update unsuccessful
                        }
                    }


                }


                if (resultSet2.next()) {
                    temp2[2] = resultSet2.getInt(1);

                    if (person.getAddress1_3().equals("") && person.getAddress2_3().equals("")
                            && person.getCity_3().equals("") && person.getEircode_3().equals("")) {

                        //delete phone number
                        System.out.println("blank Address ");

                        sqlDeleteAddressID.setInt(1, person.getPersonID());
                        sqlDeleteAddressID.setInt(2, temp2[2]);
                        System.out.println(person.getPersonID());
                        System.out.println(temp2[2]);
                        result = sqlDeleteAddressID.executeUpdate();
                        System.out.println(result);

                        person.setAddress1_3("");
                        person.setAddress2_3("");
                        person.setCity_3("");
                        person.setState_3("");
                        person.setEircode_3("");


                        if (result == 0) {
                            System.out.println("failure in deleting Address two");
                            connection.rollback(); // rollback update
                            return false;          // update unsuccessful
                        }

                    } else {
                        sqlUpdateAddress.setString(1, person.getAddress1_3());
                        sqlUpdateAddress.setString(2, person.getAddress2_3());
                        sqlUpdateAddress.setString(3, person.getCity_3());
                        sqlUpdateAddress.setString(4, person.getState_3());
                        sqlUpdateAddress.setString(5, person.getEircode_3());
                        sqlUpdateAddress.setInt(6, person.getPersonID());
                        sqlUpdateAddress.setInt(7, temp2[2]);
                        result = sqlUpdateAddress.executeUpdate();
                        System.out.println("updating Email addresses three");
                        // if update fails, rollback and discontinue
                        if (result == 0) {
                            System.out.println("failure in updating address three");
                            connection.rollback(); // rollback update
                            return false;          // update unsuccessful
                        }
                    }

                    if(count.equals(1)){

                        person.setAddress1(person.getAddress1_2());
                        person.setAddress2(person.getAddress2_2());
                        person.setCity(person.getCity_2());
                        person.setState(person.getState_2());
                        person.setEircode(person.getEircode_2());

                        person.setAddress1_2(person.getAddress1_3());
                        person.setAddress2_2(person.getAddress2_3());
                        person.setCity_2(person.getCity_3());
                        person.setState_2(person.getState_3());
                        person.setEircode_2(person.getEircode_3());

                        person.setAddress1_3("");
                        person.setAddress2_3("");
                        person.setCity_3("");
                        person.setState_3("");
                        person.setEircode_3("");



                    }else if(count.equals(2)){

                        person.setAddress1_2(person.getAddress1_3());
                        person.setAddress2_2(person.getAddress2_3());
                        person.setCity_2(person.getCity_3());
                        person.setState_2(person.getState_3());
                        person.setEircode_2(person.getEircode_3());

                        person.setAddress1_3("");
                        person.setAddress2_3("");
                        person.setCity_3("");
                        person.setState_3("");
                        person.setEircode_3("");

                    }
                    else if(count.equals(3)){

                        person.setAddress1(person.getAddress1_3());
                        person.setAddress2(person.getAddress2_3());
                        person.setCity(person.getCity_3());
                        person.setState(person.getState_3());
                        person.setEircode(person.getEircode_3());

                        person.setAddress1_2("");
                        person.setAddress2_2("");
                        person.setCity_2("");
                        person.setState_2("");
                        person.setEircode_2("");

                        person.setAddress1_3("");
                        person.setAddress2_3("");
                        person.setCity_3("");
                        person.setState_3("");
                        person.setEircode_3("");
                    }



                }

            }







            // set query parameter and execute query
            sqlGetPhoneID.setInt(1, person.getPersonID());
            ResultSet resultSet = sqlGetPhoneID.executeQuery();
            // if no records found, return immediately
            if (!resultSet.next()) {
                System.out.println("no data");
            } else {

                int[] temp = new int[3];
                temp[0] = resultSet.getInt(1);


                if (person.getPhoneNumber().equals("")) {
                    System.out.println("blank Phone ");
                    sqlDeletePhoneUsingID.setInt(1, person.getPersonID());
                    sqlDeletePhoneUsingID.setInt(2, temp[0]);
                    result = sqlDeletePhoneUsingID.executeUpdate();
                    System.out.println(result);

                    person.setPhoneNumber("");

                    if (result == 0) {
                        System.out.println("failure in deleting phone number");
                        connection.rollback(); // rollback update
                        return false;          // update unsuccessful
                    }

                } else {

                    sqlUpdatePhone.setString(1, person.getPhoneNumber());
                    sqlUpdatePhone.setInt(2, person.getPersonID());
                    sqlUpdatePhone.setInt(3, temp[0]);
                    result = sqlUpdatePhone.executeUpdate();
                    System.out.println("updating phone numbers");
                    // if update fails, rollback and discontinue

                    if (result == 0) {
                        System.out.println("failure in updating Phone");
                        connection.rollback(); // rollback update
                        return false;          // update unsuccessful
                    }
                }


                if (resultSet.next()) {
                    temp[1] = resultSet.getInt(1);

                    if (person.getPhoneNumber1().equals("")) {
                        //delete phone number


                        //   System.out.println("blank Phone number 2 ");

                        sqlDeletePhoneUsingID.setInt(1, person.getPersonID());
                        sqlDeletePhoneUsingID.setInt(2, temp[1]);
                        System.out.println(person.getPersonID());
                        System.out.println(temp[1]);
                        result = sqlDeletePhoneUsingID.executeUpdate();
                        System.out.println(result);
                        person.setPhoneNumber1("");
                        if (result == 0) {
                            System.out.println("failure in deleting phone number 2");
                            connection.rollback(); // rollback update
                            return false;          // update unsuccessful
                        }

                    } else {

                        System.out.println(temp[1]);
                        sqlUpdatePhone.setString(1, person.getPhoneNumber1());
                        sqlUpdatePhone.setInt(2, person.getPersonID());
                        sqlUpdatePhone.setInt(3, temp[1]);
                        result = sqlUpdatePhone.executeUpdate();
                        //  System.out.println("upadte second phone number");
                        // if update fails, rollback and discontinue
                        if (result == 0) {
                            System.out.println("failure in updating Phone");
                            connection.rollback(); // rollback update
                            return false;          // update unsuccessful
                        }
                    }

                }


                if (resultSet.next()) {
                    temp[2] = resultSet.getInt(1);


                    if (person.getPhoneNumber2().equals("")) {
                        //delete phone number


                        // System.out.println("blank Phone number 3 ");

                        sqlDeletePhoneUsingID.setInt(1, person.getPersonID());
                        sqlDeletePhoneUsingID.setInt(2, temp[2]);
                        System.out.println(person.getPersonID());
                        System.out.println(temp[2]);
                        result = sqlDeletePhoneUsingID.executeUpdate();
                        System.out.println(result);
                        person.setPhoneNumber2("");
                        if (result == 0) {
                            System.out.println("failure in deleting phone number 3");
                            connection.rollback(); // rollback update
                            return false;          // update unsuccessful
                        }

                    } else {

                        sqlUpdatePhone.setString(1, person.getPhoneNumber2());
                        sqlUpdatePhone.setInt(2, person.getPersonID());
                        sqlUpdatePhone.setInt(3, temp[2]);
                        result = sqlUpdatePhone.executeUpdate();
                    }
                    if (result == 0) {
                        System.out.println("failure in updating Phone");
                        connection.rollback(); // rollback update
                        return false;          // update unsuccessful
                    }
                }

                if (person.getPhoneNumber().equals("")) {
                    if (person.getPhoneNumber1().equals("")) {

                    } else {
                        person.setPhoneNumber(person.getPhoneNumber1());
                        person.setPhoneNumber1("");

                        if (person.getPhoneNumber2().equals("")) {
                        } else {
                            person.setPhoneNumber1(person.getPhoneNumber2());
                            person.setPhoneNumber2("");
                        }
                    }
                    if (person.getPhoneNumber2().equals("")) {

                    } else {
                        person.setPhoneNumber(person.getPhoneNumber2());
                        person.setPhoneNumber2("");
                    }
                }

                if (person.getPhoneNumber1().equals("")) {
                    if (person.getPhoneNumber2().equals("")) {

                    } else {
                        person.setPhoneNumber1(person.getPhoneNumber2());
                        person.setPhoneNumber2("");
                    }
                }
            }

            // set query parameter and execute query
            sqlGetEmailID.setInt(1, person.getPersonID());
            ResultSet resultSet1 = sqlGetEmailID.executeQuery();

            //System.out.println("Updating the email addresses");

            // if no records found, return immediately
            if (!resultSet1.next()) {
                System.out.println("no data email");
            } else {
                int[] temp1 = new int[3];
                temp1[0] = resultSet1.getInt(1);


                if (person.getEmailAddress().equals("")) {
                    //delete phone number
                    // System.out.println("blank email ");

                    sqlDeleteEmailUsingID.setInt(1, person.getPersonID());
                    sqlDeleteEmailUsingID.setInt(2, temp1[0]);
                    //System.out.println("Person ID" + person.getPersonID());
                    result = sqlDeleteEmailUsingID.executeUpdate();
                    System.out.println(result);

                    person.setEmailAddress("");

                    if (result == 0) {
                        System.out.println("failure in deleting phone number");
                        connection.rollback(); // rollback update
                        return false;          // update unsuccessful
                    }

                } else {

                    sqlUpdateEmail.setString(1, person.getEmailAddress());
                    sqlUpdateEmail.setInt(2, person.getPersonID());
                    sqlUpdateEmail.setInt(3, temp1[0]);
                    result = sqlUpdateEmail.executeUpdate();
                    //System.out.println("updating phone number 1");
                    // if update fails, rollback and discontinue

                    if (result == 0) {
                        System.out.println("failure in updating Phone");
                        connection.rollback(); // rollback update
                        return false;          // update unsuccessful
                    }
                }
                if (resultSet1.next()) {
                    temp1[1] = resultSet1.getInt(1);

                    if (person.getEmailAddress1().equals("")) {
                        //delete phone number
                        //  System.out.println("blank Email number 2 ");

                        sqlDeleteEmailUsingID.setInt(1, person.getPersonID());
                        sqlDeleteEmailUsingID.setInt(2, temp1[1]);
                        //    System.out.println(person.getPersonID());
                        //    System.out.println(temp1[1]);
                        result = sqlDeleteEmailUsingID.executeUpdate();
                        //    System.out.println(result);

                        person.setEmailAddress1("");

                        if (result == 0) {
                            System.out.println("failure in deleting email number 2");
                            connection.rollback(); // rollback update
                            return false;          // update unsuccessful
                        }

                    } else {

                        //   System.out.println(temp1[1]);
                        sqlUpdateEmail.setString(1, person.getEmailAddress1());
                        sqlUpdateEmail.setInt(2, person.getPersonID());
                        sqlUpdateEmail.setInt(3, temp1[1]);
                        result = sqlUpdateEmail.executeUpdate();
                        //    System.out.println("upadte second phone number");
                        // if update fails, rollback and discontinue
                        if (result == 0) {
                            System.out.println("failure in updating Phone");
                            connection.rollback(); // rollback update
                            return false;          // update unsuccessful
                        }
                    }
                }

                if (resultSet1.next()) {
                    temp1[2] = resultSet1.getInt(1);


                    if (person.getEmailAddress2().equals("")) {

                        //   System.out.println("blank Phone number 3 ");

                        sqlDeleteEmailUsingID.setInt(1, person.getPersonID());
                        sqlDeleteEmailUsingID.setInt(2, temp1[2]);
                        System.out.println(person.getPersonID());
                        result = sqlDeleteEmailUsingID.executeUpdate();
                        //     System.out.println(result);
                        person.setEmailAddress2("");
                        if (result == 0) {
                            System.out.println("failure in deleting email number 3");
                            connection.rollback(); // rollback update
                            return false;          // update unsuccessful
                        }

                    } else {
                        ///   System.out.println("Email Number two ");
                        sqlUpdateEmail.setString(1, person.getEmailAddress2());
                        sqlUpdateEmail.setInt(2, person.getPersonID());
                        sqlUpdateEmail.setInt(3, temp1[2]);
                        result = sqlUpdateEmail.executeUpdate();
                    }
                    if (result == 0) {
                        System.out.println("failure in updating Phone");
                        connection.rollback(); // rollback update
                        return false;          // update unsuccessful
                    }
                }

                if (person.getEmailAddress().equals("")) {
                    if (person.getEmailAddress1().equals("")) {
                    } else {
                        person.setEmailAddress(person.getEmailAddress1());
                        person.setEmailAddress1("");
                        if (person.getEmailAddress2().equals("")) {
                        } else {
                            person.setEmailAddress1(person.getEmailAddress2());
                            person.setEmailAddress2("");
                        }
                    }
                    if (person.getEmailAddress2().equals("")) {
                    } else {
                        person.setEmailAddress(person.getEmailAddress2());
                        person.setEmailAddress2("");
                    }
                }

                if (person.getEmailAddress1().equals("")) {
                    if (person.getEmailAddress2().equals("")) {

                    } else {
                        person.setEmailAddress1(person.getEmailAddress2());
                        person.setEmailAddress2("");
                    }
                }

//                System.out.println("email1: " + person.getEmailAddress());
//                System.out.println("email2: " + person.getEmailAddress1());
//                System.out.println("email3: " + person.getEmailAddress2());
            }


            System.out.println("connection cmmmited");
            connection.commit();   // commit update
            return true;           // update successful
        }  // end try

        // detect problems updating database
        catch (SQLException sqlException) {

            // rollback transaction
            try {
                System.out.println("connection rolback");
                connection.rollback(); // rollback update
                return false;          // update unsuccessful
            }

            // handle exception rolling back transaction
            catch (SQLException exception) {
                throw new DataAccessException(exception);
            }
        }
    }  // end method savePerson


    //Method to return the amount of name entries in the database
    public int calculateEntries() throws SQLException {
        //System.out.println("Start calculate Entries? ");
        ResultSet resultEntries = sqlCountEntries.executeQuery();
        int temp = 0;
        if (resultEntries.next()) {
            temp = resultEntries.getInt(1);
        }
        //System.out.println("calculate Entries: " + temp);
        return temp;
    }

    // Insert new entry. Method returns boolean indicating
    // success or failure.
    public boolean newPerson(AddressBookEntry person)
            throws DataAccessException {
        System.out.println("newPerson Method");
        // insert person in database
        try {
            int result;

            // insert first and last name in names table
            sqlInsertName.setString(1, person.getFirstName());
            sqlInsertName.setString(2, person.getLastName());
            result = sqlInsertName.executeUpdate();

            System.out.println("here: resul; " + result);
            // if insert fails, rollback and discontinue
            if (result == 0) {
                connection.rollback(); // rollback insert
                System.out.println("failure");
                return false;          // insert unsuccessful

            }

            // determine new personID
            System.out.println("here2");
            ResultSet resultPersonID = sqlPersonID.executeQuery();
            System.out.println("here3");
            System.out.println("returned value: " + resultPersonID);
            //System.out.println(resultPersonID.next() );

            if (resultPersonID.next()) {

                int personID = resultPersonID.getInt(1);
                System.out.println(personID);
                // insert address in addresses table
                sqlInsertAddress.setInt(1, personID);
                sqlInsertAddress.setString(2,
                        person.getAddress1());
                sqlInsertAddress.setString(3,
                        person.getAddress2());
                sqlInsertAddress.setString(4,
                        person.getCity());
                sqlInsertAddress.setString(5,
                        person.getState());
                sqlInsertAddress.setString(6,
                        person.getEircode());
                result = sqlInsertAddress.executeUpdate();
                System.out.println("Fallbcak Address if fail" + result);
                // if insert fails, rollback and discontinue
                if (result == 0) {
                    connection.rollback(); // rollback insert
                    return false;          // insert unsuccessful
                }

                // insert phone number in phoneNumbers table
                sqlInsertPhone.setInt(1, personID);
                sqlInsertPhone.setString(2,
                        person.getPhoneNumber());
                result = sqlInsertPhone.executeUpdate();
                System.out.println("Fallbcak if fail phone number" + result);
                // if insert fails, rollback and discontinue
                if (result == 0) {
                    connection.rollback(); // rollback insert
                    return false;          // insert unsuccessful
                }


                // insert email address in emailAddresses table
                sqlInsertEmail.setInt(1, personID);
                sqlInsertEmail.setString(2,
                        person.getEmailAddress());
                result = sqlInsertEmail.executeUpdate();
                System.out.println("Fallbcak if fail email number" + result);
                // if insert fails, rollback and discontinue
                if (result == 0) {
                    connection.rollback(); // rollback insert
                    return false;          // insert unsuccessful
                }

                connection.commit();   // commit insert


                //update any other address a user has. ///////////////////////////////////////
                if (person.getAddress1_2().equals("")) {
                    System.out.println("no second Address");
                } else {
                    sqlInsertAddress.setInt(1, personID);
                    sqlInsertAddress.setString(2,
                            person.getAddress1_2());
                    sqlInsertAddress.setString(3,
                            person.getAddress2_2());
                    sqlInsertAddress.setString(4,
                            person.getCity_2());
                    sqlInsertAddress.setString(5,
                            person.getState_2());
                    sqlInsertAddress.setString(6,
                            person.getEircode_2());
                    result = sqlInsertAddress.executeUpdate();
                    System.out.println("Fallback Address if fail" + result);
                    // if insert fails, rollback and discontinue
                    if (result == 0) {
                        connection.rollback(); // rollback insert
                        return false;          // insert unsuccessful
                    }
                }
                connection.commit();   // commit insert

                //update any other address a user has. ///////////////////////////////////////
                if (person.getAddress1_3().equals("")) {
                    System.out.println("no Third Address");
                } else {
                    sqlInsertAddress.setInt(1, personID);
                    sqlInsertAddress.setString(2,
                            person.getAddress1_3());
                    sqlInsertAddress.setString(3,
                            person.getAddress2_3());
                    sqlInsertAddress.setString(4,
                            person.getCity_3());
                    sqlInsertAddress.setString(5,
                            person.getState_3());
                    sqlInsertAddress.setString(6,
                            person.getEircode_3());
                    result = sqlInsertAddress.executeUpdate();
                    System.out.println("Fallback Address if fail" + result);
                    // if insert fails, rollback and discontinue
                    if (result == 0) {
                        connection.rollback(); // rollback insert
                        return false;          // insert unsuccessful
                    }
                }
                connection.commit();   // commit insert

                //update any other number a user has. ///////////////////////////////////////
                if (person.getPhoneNumber1().equals("")) {
                    System.out.println("no second PoneNumber");
                } else {
                    sqlInsertPhone.setInt(1, personID);
                    sqlInsertPhone.setString(2,
                            person.getPhoneNumber1());
                    result = sqlInsertPhone.executeUpdate();
                    if (result == 0) {
                        connection.rollback();
                        return false;
                    }
                }
                connection.commit();   // commit insert

                if (person.getPhoneNumber2().equals("")) {
                    System.out.println("no third Phone Number");
                } else {
                    sqlInsertPhone.setInt(1, personID);
                    sqlInsertPhone.setString(2,
                            person.getPhoneNumber2());
                    result = sqlInsertPhone.executeUpdate();
                    if (result == 0) {
                        connection.rollback(); // rollback update
                        return false;          // update unsuccessful
                    }
                }
                connection.commit();   // commit insert


                //Update any othter emails addresses.  /////////////////////////////////////////
                if (person.getEmailAddress1().equals("")) {
                    System.out.println("no second email address");
                } else {
                    //Updating second email1
                    sqlInsertEmail.setInt(1, personID);
                    sqlInsertEmail.setString(2,
                            person.getEmailAddress1());
                    result = sqlInsertEmail.executeUpdate();
                    if (result == 0) {
                        connection.rollback();
                        return false;
                    }
                }
                connection.commit();   // commit insert

                if (person.getEmailAddress2().equals("")) {
                    System.out.println("no third email address");
                } else {
                    sqlInsertEmail.setInt(1, personID);
                    sqlInsertEmail.setString(2,
                            person.getEmailAddress2());
                    result = sqlInsertEmail.executeUpdate();
                    if (result == 0) {
                        connection.rollback(); // rollback update
                        return false;          // update unsuccessful
                    }
                }
                connection.commit();   // commit insert


                System.out.println("commited to database");
                return true;           // insert successful
            } else
                return false;
        }  // end try

        // detect problems updating database
        catch (SQLException sqlException) {
            // rollback transaction
            try {
                connection.rollback(); // rollback update
                return false;          // update unsuccessful
            }

            // handle exception rolling back transaction
            catch (SQLException exception) {
                throw new DataAccessException(exception);
            }
        }
    }  // end method newPerson

    // Delete an entry. Method returns boolean indicating
    // success or failure.
    public boolean deletePerson(AddressBookEntry person)
            throws DataAccessException {
        // delete a person from database
        try {
            int result;

            // delete address from addresses table
            sqlDeleteAddress.setInt(1, person.getPersonID());
            result = sqlDeleteAddress.executeUpdate();

            // if delete fails, rollback and discontinue
            if (result == 0) {
                connection.rollback(); // rollback delete
                return false;          // delete unsuccessful
            }

            // delete phone number from phoneNumbers table
            sqlDeletePhone.setInt(1, person.getPersonID());
            result = sqlDeletePhone.executeUpdate();

            // if delete fails, rollback and discontinue
            if (result == 0) {
                connection.rollback(); // rollback delete
                return false;          // delete unsuccessful
            }

            // delete email address from emailAddresses table
            sqlDeleteEmail.setInt(1, person.getPersonID());
            result = sqlDeleteEmail.executeUpdate();

            // if delete fails, rollback and discontinue
            if (result == 0) {
                connection.rollback(); // rollback delete
                return false;          // delete unsuccessful
            }

            // delete name from names table
            sqlDeleteName.setInt(1, person.getPersonID());
            result = sqlDeleteName.executeUpdate();

            // if delete fails, rollback and discontinue
            if (result == 0) {
                connection.rollback(); // rollback delete
                return false;          // delete unsuccessful
            }

            connection.commit();   // commit delete
            System.out.println("commited to database");
            return true;           // delete successful
        }  // end try

        // detect problems updating database
        catch (SQLException sqlException) {
            // rollback transaction
            try {
                System.out.println("connection roolbacked");
                connection.rollback(); // rollback update
                return false;          // update unsuccessful
            }

            // handle exception rolling back transaction
            catch (SQLException exception) {
                throw new DataAccessException(exception);
            }
        }
    }  // end method deletePerson

    // method to close statements and database connection
    public void close() {
        // close database connection
        try {
            sqlFind.close();
            sqlPersonID.close();
            sqlInsertName.close();
            sqlInsertAddress.close();
            sqlInsertPhone.close();
            sqlInsertEmail.close();
            sqlUpdateName.close();
            sqlUpdateAddress.close();
            sqlUpdatePhone.close();
            sqlUpdateEmail.close();
            sqlDeleteName.close();
            sqlDeleteAddress.close();
            sqlDeletePhone.close();
            sqlDeleteEmail.close();
            connection.close();
        }  // end try

        // detect problems closing statements and connection
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }  // end method close

    // Method to clean up database connection. Provided in case
    // CloudscapeDataAccess object is garbage collected.
    protected void finalize() {
        close();
    }
}  // end class CloudscapeDataAccess


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
