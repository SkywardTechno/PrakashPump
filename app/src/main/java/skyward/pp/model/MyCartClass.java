package skyward.pp.model;

import java.util.ArrayList;

import skyward.pp.model.OffersandPromotionDetailClass;

/**
 * Created by Skyward_Mob1 on 09-02-2017.
 */
public class MyCartClass {

    int cartid;
    int productid;
    String productame, imagepath, modelno, productcode,currency;
    int quantity;
    double price;
    int avvailableqty;
    double grandtotal;
int producttypeid;
    ArrayList<OffersandPromotionDetailClass> listoffer;
    int freeqty;
    int discount;
    double  discountamt;
    public MyCartClass() {
    }

    public int getFreeqty() {
        return freeqty;
    }

    public void setDiscountamt(double discountamt) {
        this.discountamt = discountamt;
    }
/* public MyCartClass(int cartid, int productid, String productame, String imagepath, String modelno, String currency, int quantity, double price, int avvailableqty, double grandtotal, int producttypeid, ArrayList<OffersandPromotionDetailClass> listoffer, int freeqty, int discount, double discountamt) {
        this.cartid = cartid;
        this.productid = productid;
        this.productame = productame;
        this.imagepath = imagepath;
        this.modelno = modelno;
        this.currency = currency;
        this.quantity = quantity;
        this.price = price;
        this.avvailableqty = avvailableqty;
        this.grandtotal = grandtotal;
        this.producttypeid = producttypeid;
        this.listoffer = listoffer;
        this.freeqty = freeqty;
        this.discount = discount;
        this.discountamt = discountamt;
    }*/

    public MyCartClass(int cartid, int productid, String productame, String imagepath, String modelno, int quantity, double price, int avvailableqty, double grandtotal, String currency, int productypeid, ArrayList<OffersandPromotionDetailClass> listoffer,int freeqty,int discount,int discountamt) {
        this.cartid = cartid;
        this.productid = productid;
        this.productame = productame;
        this.imagepath = imagepath;
        this.modelno = modelno;
        this.quantity = quantity;
        this.price = price;
        this.avvailableqty = avvailableqty;
        this.grandtotal = grandtotal;
        this.currency = currency;
        this.producttypeid=productypeid;
        this.listoffer=listoffer;
         this.freeqty = freeqty;
        this.discount = discount;
        this.discountamt = discountamt;
    }

    public void setFreeqty(int freeqty) {
        this.freeqty = freeqty;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public double getDiscountamt() {
        return discountamt;
    }

    public ArrayList<OffersandPromotionDetailClass> getListoffer() {
        return listoffer;
    }

    public void setListoffer(ArrayList<OffersandPromotionDetailClass> listoffer) {
        this.listoffer = listoffer;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public int getProducttypeid() {
        return producttypeid;
    }

    public void setProducttypeid(int producttypeid) {
        this.producttypeid = producttypeid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getCartid() {
        return cartid;
    }

    public void setCartid(int cartid) {
        this.cartid = cartid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
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

    public int getAvvailableqty() {
        return avvailableqty;
    }

    public void setAvvailableqty(int avvailableqty) {
        this.avvailableqty = avvailableqty;
    }

    public double getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(double grandtotal) {
        this.grandtotal = grandtotal;
    }
}

