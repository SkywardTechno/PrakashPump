package skyward.pp.model;

/**
 * Created by ANDROID 1 on 06-03-2017.
 */
public class SupportInquiryByUserIDClass {

    String name, servicetype, inquirydate, serviceID,emailid,phoneno,remarks,inquirycode;;

    public SupportInquiryByUserIDClass(String name, String servicetype, String inquirydate, String serviceID) {
        this.name = name;
        this.servicetype = servicetype;
        this.inquirydate = inquirydate;
        this.serviceID = serviceID;
    }

    public SupportInquiryByUserIDClass(String name, String servicetype, String inquirydate, String serviceID, String emailid, String phoneno, String remarks, String inquirycode) {
        this.name = name;
        this.servicetype = servicetype;
        this.inquirydate = inquirydate;
        this.serviceID = serviceID;
        this.emailid = emailid;
        this.phoneno = phoneno;
        this.remarks = remarks;
        this.inquirycode = inquirycode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public String getInquirydate() {
        return inquirydate;
    }

    public void setInquirydate(String inquirydate) {
        this.inquirydate = inquirydate;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getInquirycode() {
        return inquirycode;
    }

    public void setInquirycode(String inquirycode) {
        this.inquirycode = inquirycode;
    }
}
