package skyward.pp.model;

import android.content.Context;

/**
 * Created by ADNROID 2 on 15-11-2016.
 */
public class EcommSubcategoryClass {

    String product_name;
    String product_image;
    String product_id;
    String price;
    Context context;

    public EcommSubcategoryClass(String product_name, String product_image, String product_id, Context context) {
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_id = product_id;
        this.context = context;
    }

    public EcommSubcategoryClass(String product_name, String product_image, String product_id,String price, Context context) {
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_id = product_id;
        this.price = price;
        this.context = context;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
