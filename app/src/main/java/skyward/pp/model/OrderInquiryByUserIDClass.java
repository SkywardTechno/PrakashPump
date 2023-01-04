package skyward.pp.model;

/**
 * Created by Drashti on 04-10-2016.
 */
public class OrderInquiryByUserIDClass {
    String name, productname, inquirydate, orderID,inquirycode,emailid,phoneno,remarks;



    public OrderInquiryByUserIDClass(String name, String productname, String orderID, String inquirydate) {
        this.name = name;
        this.productname = productname;
        this.inquirydate = inquirydate;
        this.orderID = orderID;
    }

    public OrderInquiryByUserIDClass(String name, String productname, String inquirydate, String orderID, String inquirycode, String emailid, String phoneno, String remarks) {
        this.name = name;
        this.productname = productname;
        this.inquirydate = inquirydate;
        this.orderID = orderID;
        this.inquirycode = inquirycode;
        this.emailid = emailid;
        this.phoneno = phoneno;
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }


    public String getInquirydate() {
        return inquirydate;
    }

    public void setInquirydate(String inquirydate) {
        this.inquirydate = inquirydate;
    }


    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getInquirycode() {
        return inquirycode;
    }

    public void setInquirycode(String inquirycode) {
        this.inquirycode = inquirycode;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
}
