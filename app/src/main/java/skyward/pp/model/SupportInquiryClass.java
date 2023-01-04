package skyward.pp.model;

/**
 * Created by Drashti on 04-10-2016.
 */
public class SupportInquiryClass {
    String name,ServiceType,supportID,date;
    int replyId;

    public SupportInquiryClass(String name, String serviceType, String supportID, String date, int replyId) {
        this.name = name;
        ServiceType = serviceType;
        this.supportID = supportID;
        this.date = date;
        this.replyId = replyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getSupportID() {
        return supportID;
    }

    public void setSupportID(String supportID) {
        this.supportID = supportID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }
}
