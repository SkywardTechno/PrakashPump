package skyward.pp.model;

/**
 * Created by IBM on 31-05-2017.
 */

public class OffersandPromotionDetailClass {

    int minqty,maxqty,freeordiscount,value;

    public OffersandPromotionDetailClass(int minqty, int maxqty, int freeordiscount, int value) {
        this.minqty = minqty;
        this.maxqty = maxqty;
        this.freeordiscount = freeordiscount;
        this.value = value;
    }

    public int getMinqty() {
        return minqty;
    }

    public void setMinqty(int minqty) {
        this.minqty = minqty;
    }

    public int getMaxqty() {
        return maxqty;
    }

    public void setMaxqty(int maxqty) {
        this.maxqty = maxqty;
    }

    public int getFreeordiscount() {
        return freeordiscount;
    }

    public void setFreeordiscount(int freeordiscount) {
        this.freeordiscount = freeordiscount;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
