package skyward.pp.model;

import android.content.Context;

/**
 * Created by Drashti on 30-09-2016.
 */
public class ProductClass {
    String product_name;
    String product_image;
    String product_id;
    String product_price;
    String currency;
    Context context;

    public ProductClass(String product_name, String product_image, String product_id, String product_price, Context context) {
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_id = product_id;
        this.product_price = product_price;
        this.context = context;
    }
    public ProductClass(String product_name, String product_image, String product_id, String product_price,String currency, Context context) {
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_id = product_id;
        this.product_price = product_price;
        this.currency = currency;
        this.context = context;
    }

    public ProductClass(String product_name, String product_image, String product_id, Context context) {
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_id = product_id;
        this.context = context;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}
