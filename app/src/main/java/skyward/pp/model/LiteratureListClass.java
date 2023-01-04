package skyward.pp.model;

import android.content.Context;

/**
 * Created by Drashti on 06-10-2016.
 */
public class LiteratureListClass
{
    String product_name;
    String product_image;
    String product_id;
    String lit_id;
    Context context;

    public LiteratureListClass(String product_name, String product_image, String product_id, Context context) {
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_id = product_id;
        this.context = context;
    }


    public LiteratureListClass(String product_name, String product_image, String product_id, String lit_id, Context context) {
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_id = product_id;
        this.lit_id = lit_id;
        this.context = context;
    }

    public String getLit_id() {
        return lit_id;
    }

    public void setLit_id(String lit_id) {
        this.lit_id = lit_id;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
