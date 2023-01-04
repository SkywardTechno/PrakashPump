package skyward.pp.model;

/**
 * Created by ANDROID 1 on 07-03-2017.
 */
public class MyPumpDetailListClass {
    String serialno, pumpcode, createddate, gstart,gend,serviceTypeID,ID,invoiceno,invoicedate,wstart,wend;

    public MyPumpDetailListClass(String serialno, String pumpcode, String createddate, String gstart, String gend, String serviceTypeID, String ID, String invoiceno, String wstart, String wend) {
        this.serialno = serialno;
        this.pumpcode = pumpcode;
        this.createddate = createddate;
        this.gstart = gstart;
        this.gend = gend;
        this.serviceTypeID = serviceTypeID;
        this.ID = ID;
        this.invoiceno = invoiceno;
        this.wstart = wstart;
        this.wend = wend;
    }

    public MyPumpDetailListClass(String serialno, String pumpcode, String createddate, String gstart, String gend, String serviceTypeID, String ID, String invoiceno, String invoicedate, String wstart, String wend) {
        this.serialno = serialno;
        this.pumpcode = pumpcode;
        this.createddate = createddate;
        this.gstart = gstart;
        this.gend = gend;
        this.serviceTypeID = serviceTypeID;
        this.ID = ID;
        this.invoiceno = invoiceno;
        this.invoicedate = invoicedate;
        this.wstart = wstart;
        this.wend = wend;
    }

    public MyPumpDetailListClass(String serialno, String pumpcode, String createddate, String gstart, String gend, String serviceTypeID, String ID, String invoiceno) {
        this.serialno = serialno;
        this.pumpcode = pumpcode;
        this.createddate = createddate;
        this.gstart = gstart;
        this.gend = gend;
        this.serviceTypeID = serviceTypeID;
        this.ID = ID;
        this.invoiceno = invoiceno;
    }

    public String getInvoicedate() {
        return invoicedate;
    }

    public void setInvoicedate(String invoicedate) {
        this.invoicedate = invoicedate;
    }

    public String getWstart() {
        return wstart;
    }

    public void setWstart(String wstart) {
        this.wstart = wstart;
    }

    public String getWend() {
        return wend;
    }

    public void setWend(String wend) {
        this.wend = wend;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getPumpcode() {
        return pumpcode;
    }

    public void setPumpcode(String pumpcode) {
        this.pumpcode = pumpcode;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public String getGstart() {
        return gstart;
    }

    public void setGstart(String gstart) {
        this.gstart = gstart;
    }

    public String getGend() {
        return gend;
    }

    public void setGend(String gend) {
        this.gend = gend;
    }

    public String getServiceTypeID() {
        return serviceTypeID;
    }

    public void setServiceTypeID(String serviceTypeID) {
        this.serviceTypeID = serviceTypeID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }
}
