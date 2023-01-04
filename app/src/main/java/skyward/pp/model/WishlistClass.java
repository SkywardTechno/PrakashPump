package skyward.pp.model;

/**
 * Created by ANDROID 1 on 15-11-2016.
 */
public class WishlistClass {
    int ID;
    int productid;
    String produtname,modelno;
    int qty;
    double price;
    String imagepath;
    String currency;

    public WishlistClass(int ID, int productid, String produtname, String modelno, int qty, double price, String imagepath, String currency) {
        this.ID = ID;
        this.productid = productid;
        this.produtname = produtname;
        this.modelno = modelno;
        this.qty = qty;
        this.price = price;
        this.imagepath = imagepath;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProdutname() {
        return produtname;
    }

    public void setProdutname(String produtname) {
        this.produtname = produtname;
    }

    public String getModelno() {
        return modelno;
    }

    public void setModelno(String modelno) {
        this.modelno = modelno;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
