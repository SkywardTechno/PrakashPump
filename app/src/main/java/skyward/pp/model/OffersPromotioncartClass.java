package skyward.pp.model;

import java.util.ArrayList;

/**
 * Created by IBM on 01-06-2017.
 */

public class OffersPromotioncartClass {
    int cartid;
    ArrayList<OffersandPromotionDetailClass> listofferbycart;
    String offername;

    public OffersPromotioncartClass(int cartid, ArrayList<OffersandPromotionDetailClass> listofferbycart, String offername) {
        this.cartid = cartid;
        this.listofferbycart = listofferbycart;
        this.offername = offername;
    }

    public String getOffername() {
        return offername;
    }

    public void setOffername(String offername) {
        this.offername = offername;
    }



    public int getCartid() {
        return cartid;
    }

    public void setCartid(int cartid) {
        this.cartid = cartid;
    }

    public ArrayList<OffersandPromotionDetailClass> getListofferbycart() {
        return listofferbycart;
    }

    public void setListofferbycart(ArrayList<OffersandPromotionDetailClass> listofferbycart) {
        this.listofferbycart = listofferbycart;
    }
}
