package skyward.pp.model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by ANDROID 1 on 10-02-2017.
 */
public class OrderSummaryClass {
   
    String productame, imagepath, modelno, productcode,currency;
    int quantity;
    double price,shipping;
    double grandtotal;
    int productid;
    int freeqty;
    int  discount;
    double discountamt;
/*
    public OrderSummaryClass(String productame, String imagepath, String modelno, String productcode, String currency, int quantity, double price, double shipping, double grandtotal, int productid,) {
        this.productame = productame;
        this.imagepath = imagepath;
        this.modelno = modelno;
        this.productcode = productcode;
        this.currency = currency;
        this.quantity = quantity;
        this.price = price;
        this.shipping = shipping;
        this.grandtotal = grandtotal;
        this.productid = productid;

    }*/

    public OrderSummaryClass(String productame, String imagepath, String modelno, String productcode, int quantity, double price, double shipping, double grandtotal, int productid, String currency, int freeqty, int discount, double discountamt) {
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
        this.freeqty = freeqty;
        this.discount = discount;
        this.discountamt = discountamt;
    }

    public int getFreeqty() {
        return freeqty;
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

    public void setDiscountamt(double discountamt) {
        this.discountamt = discountamt;
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

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public OrderSummaryClass(Context applicationContext, ArrayList<OrderSummaryClass> listcart) {

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

    public double getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(double grandtotal) {
        this.grandtotal = grandtotal;
    }
}
