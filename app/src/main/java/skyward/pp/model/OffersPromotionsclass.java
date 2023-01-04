package skyward.pp.model;

/**
 * Created by ANDROID 1 on 15-11-2016.
 */
public class OffersPromotionsclass {
    String id,offername,offerimage;

    public OffersPromotionsclass(String id, String offername, String offerimage) {
        this.id = id;
        this.offername = offername;
        this.offerimage = offerimage;
    }

    public OffersPromotionsclass(String id, String offername) {

        this.id = id;
        this.offername = offername;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOffername() {
        return offername;
    }

    public void setOffername(String offername) {
        this.offername = offername;
    }

    public String getOfferimage() {
        return offerimage;
    }

    public void setOfferimage(String offerimage) {
        this.offerimage = offerimage;
    }
}
