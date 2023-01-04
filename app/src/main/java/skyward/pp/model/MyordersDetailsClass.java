package skyward.pp.model;

/**
 * Created by Skyward_Mob1 on 10-03-2017.
 */
public class MyordersDetailsClass {


    String productame, imagepath, modelno, productcode,currency;
    int quantity;
    double price,shipping;
    double grandtotal;
    int productid;


    public MyordersDetailsClass(String productame, String imagepath, String modelno, String productcode, int quantity, double price, double shipping, double grandtotal, int productid, String currency) {
        this.productame = productame;
        this.imagepath = imagepath;
        this.modelno = modelno;
        this.productcode = productcode;
        this.quantity = quantity;
        this.price = price;
        this.shipping = shipping;
        this.grandtotal = grandtotal;
        this.productid = productid;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProductame() {
        return productame;
    }

    public void setProductame(String productame) {
        this.productame = productame;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getModelno() {
        return modelno;
    }

    public void setModelno(String modelno) {
        this.modelno = modelno;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public double getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(double grandtotal) {
        this.grandtotal = grandtotal;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }
}
