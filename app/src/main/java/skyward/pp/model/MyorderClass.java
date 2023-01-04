package skyward.pp.model;

/**
 * Created by Skyward_Mob1 on 09-03-2017.
 */
public class MyorderClass

{
    
    String orderno,orderdate,status,currency;
    double price;
    int orderid;

    public MyorderClass(String orderno, String orderdate, double price, int orderid,String status, String currency) {
        this.orderno = orderno;
        this.orderdate = orderdate;
        this.price = price;
        this.orderid = orderid;
        this.status = status;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
