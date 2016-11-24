// Fig. 8.33: AddressBookEntry.java
// JavaBean to represent one address book entry.
package Assignment_6;

public class AddressBookEntry {
    private String firstName = "";
    private String lastName = "";
    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String state = "";
    private String eircode = "";

    private String address1_2 = "";
    private String address2_2 = "";
    private String city_2 = "";
    private String state_2 = "";
    private String eircode_2 = "";

    private String address1_3 = "";
    private String address2_3 = "";
    private String city_3 = "";
    private String state_3 = "";
    private String eircode_3 = "";

    private String phoneNumber = "";
    private String phoneNumber1 = "";
    private String phoneNumber2 = "";
    private String emailAddress = "";
    private String emailAddress1 = "";
    private String emailAddress2 = "";

    private int personID;
    private int addressID;
    private int phoneID;
    private int emailID;
    private int addDetails = 0;
    private int addDetailsEmail = 0;
    private int addDetailsPhone = 0;

    public int getAddDetailsEmail() {
        return addDetailsEmail;
    }

    public void setAddDetailsEmail(int addDetailsEmail) {
        this.addDetailsEmail = addDetailsEmail;
    }

    public int getAddDetailsPhone() {
        return addDetailsPhone;
    }

    public void setAddDetailsPhone(int addDetailsPhone) {
        this.addDetailsPhone = addDetailsPhone;
    }


    public int getAddDetails() {
        return addDetails;
    }

    public void setAddDetails(int addDetails) {
        this.addDetails = addDetails;
    }

    private int addressID_1;
    private int addressID_2;
    private int phoneID_1;
    private int phoneID_2;
    private int emailID_1;

    private int emailID_2;
    private int extraDetails = 0b0;

    public int getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(int extraDetails) {
        this.extraDetails = extraDetails;
    }

    public String getAddress1_2() {
        return address1_2;
    }

    public void setAddress1_2(String address1_2) {
        this.address1_2 = address1_2;
    }

    public String getAddress2_2() {
        return address2_2;
    }

    public void setAddress2_2(String address2_2) {
        this.address2_2 = address2_2;
    }

    public String getCity_2() {
        return city_2;
    }

    public void setCity_2(String city_2) {
        this.city_2 = city_2;
    }

    public String getState_2() {
        return state_2;
    }

    public void setState_2(String state_2) {
        this.state_2 = state_2;
    }

    public String getEircode_2() {
        return eircode_2;
    }

    public void setEircode_2(String eircode_2) {
        this.eircode_2 = eircode_2;
    }

    public String getAddress1_3() {
        return address1_3;
    }

    public void setAddress1_3(String address1_3) {
        this.address1_3 = address1_3;
    }

    public String getAddress2_3() {
        return address2_3;
    }

    public void setAddress2_3(String address2_3) {
        this.address2_3 = address2_3;
    }

    public String getCity_3() {
        return city_3;
    }

    public void setCity_3(String city_3) {
        this.city_3 = city_3;
    }

    public String getState_3() {
        return state_3;
    }

    public void setState_3(String state_3) {
        this.state_3 = state_3;
    }

    public String getEircode_3() {
        return eircode_3;
    }

    public void setEircode_3(String eircode_3) {
        this.eircode_3 = eircode_3;
    }


    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }




    public String getEmailAddress1() {
        return emailAddress1;
    }

    public void setEmailAddress1(String emailAddress1) {
        this.emailAddress1 = emailAddress1;
    }

    public String getEmailAddress2() {
        return emailAddress2;
    }

    public void setEmailAddress2(String emailAddress2) {
        this.emailAddress2 = emailAddress2;
    }


    // empty constructor
    public AddressBookEntry() {
    }

    // set person's id
    public AddressBookEntry(int id) {
        personID = id;
    }

    // set person's first name
    public void setFirstName(String first) {
        firstName = first;
    }

    // get person's first name
    public String getFirstName() {
        return firstName;
    }

    // set person's last name
    public void setLastName(String last) {
        lastName = last;
    }

    // get person's last name
    public String getLastName() {
        return lastName;
    }

    // set first line of person's address
    public void setAddress1(String firstLine) {
        address1 = firstLine;
    }

    // get first line of person's address
    public String getAddress1() {
        return address1;
    }

    // set second line of person's address
    public void setAddress2(String secondLine) {
        address2 = secondLine;
    }

    // get second line of person's address
    public String getAddress2() {
        return address2;
    }

    // set city in which person lives
    public void setCity(String personCity) {
        city = personCity;
    }

    // get city in which person lives
    public String getCity() {
        return city;
    }

    // set state in which person lives
    public void setState(String personState) {
        state = personState;
    }

    // get state in which person lives
    public String getState() {
        return state;
    }

    // set person's zip code
    public void setEircode(String zip) {
        eircode = zip;
    }

    // get person's zip code
    public String getEircode() {
        return eircode;
    }

    // set person's phone number
    public void setPhoneNumber(String number) {
        phoneNumber = number;
    }

    // get person's phone number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // set person's email address
    public void setEmailAddress(String email) {
        emailAddress = email;
    }

    // get person's email address
    public String getEmailAddress() {
        return emailAddress;
    }

    // get person's ID
    public int getPersonID()

    {
        return personID;
    }

    // set person's addressID
    public void setAddressID(int id) {
        addressID = id;
    }

    // get person's addressID
    public int getAddressID() {
        return addressID;
    }

    // set person's phoneID
    public void setPhoneID(int id) {
        phoneID = id;
    }

    // get person's phoneID
    public int getPhoneID() {
        return phoneID;
    }

    // set person's emailID
    public void setEmailID(int id) {
        emailID = id;
    }

    // get person's emailID
    public int getEmailID() {
        return emailID;
    }


}  // end class AddressBookEntry


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
