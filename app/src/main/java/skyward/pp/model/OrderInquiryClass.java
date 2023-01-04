package skyward.pp.model;

/**
 * Created by Drashti on 04-10-2016.
 */
public class OrderInquiryClass
{
String name,productname,orderID,inquirydate;
    int replyId;

    public OrderInquiryClass(String name, String productname, String orderID, String inquirydate, int replyId) {
        this.name = name;
        this.productname = productname;
        this.orderID = orderID;
        this.inquirydate = inquirydate;
        this.replyId = replyId;
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

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getInquirydate() {
        return inquirydate;
    }

    public void setInquirydate(String inquirydate) {
        this.inquirydate = inquirydate;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }
}
