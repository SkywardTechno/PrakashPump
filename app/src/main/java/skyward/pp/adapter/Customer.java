package skyward.pp.adapter;

/**
 * Created by ANDROID 1 on 30-09-2016.
 */
public class Customer {

    String MobileNumber = null;
    String Password = null;
    String Name = null;
    String CustomerID = null;
    String UserTypeID = null;
    String Place = null;
    String Farmno = null;
    String Area = null;
    String EmailID = null;


    public Customer(String mobileNumber, String password, String name, String customerID, String userTypeID, String place, String farmno, String area, String emailID) {
        MobileNumber = mobileNumber;
        Password = password;
        Name = name;
        CustomerID = customerID;
        UserTypeID = userTypeID;
        Place = place;
        Farmno = farmno;
        Area = area;
        EmailID = emailID;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getFarmno() {
        return Farmno;
    }

    public void setFarmno(String farmno) {
        Farmno = farmno;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getUserTypeID() {
        return UserTypeID;
    }

    public void setUserTypeID(String userTypeID) {
        UserTypeID = userTypeID;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }
}
