package skyward.pp.holder;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ADNROID 2 on 15-11-2016.
 */
public class EcommSubCategoryHolder {

    TextView txt_Productname;
    ImageView img_ProductImage;

    public EcommSubCategoryHolder(TextView txt_Productname, ImageView img_ProductImage) {
        this.txt_Productname = txt_Productname;
        this.img_ProductImage = img_ProductImage;
    }

    public TextView getTxt_Productname() {
        return txt_Productname;
    }

    public void setTxt_Productname(TextView txt_Productname) {
        this.txt_Productname = txt_Productname;
    }

    public ImageView getImg_ProductImage() {
        return img_ProductImage;
    }

    public void setImg_ProductImage(ImageView img_ProductImage) {
        this.img_ProductImage = img_ProductImage;
    }
}

