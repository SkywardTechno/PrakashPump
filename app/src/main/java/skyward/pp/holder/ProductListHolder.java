package skyward.pp.holder;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Drashti on 30-09-2016.
 */
public class ProductListHolder {
    TextView txt_ProductName;
    ImageView img_ProductImage;
    ImageView img_share;
    ImageView img_mycart;

    public ProductListHolder(TextView txt_ProductName, ImageView img_ProductImage, ImageView img_share, ImageView img_mycart) {
        this.txt_ProductName = txt_ProductName;
        this.img_ProductImage = img_ProductImage;
        this.img_share = img_share;
        this.img_mycart = img_mycart;
    }

    public TextView getTxt_ProductName() {
        return txt_ProductName;
    }

    public void setTxt_ProductName(TextView txt_ProductName) {
        this.txt_ProductName = txt_ProductName;
    }

    public ImageView getImg_ProductImage() {
        return img_ProductImage;
    }

    public void setImg_ProductImage(ImageView img_ProductImage) {
        this.img_ProductImage = img_ProductImage;
    }

    public ImageView getImg_share() {
        return img_share;
    }

    public void setImg_share(ImageView img_share) {
        this.img_share = img_share;
    }

    public ImageView getImg_mycart() {
        return img_mycart;
    }

    public void setImg_mycart(ImageView img_mycart) {
        this.img_mycart = img_mycart;
    }
}
