package skyward.pp.holder;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Drashti on 06-10-2016.
 */
public class LiteratureListHolder
{
        TextView txt_ProductName;
ImageView img_ProductImage;
    ImageView img_share;
    ImageButton upin;

    public LiteratureListHolder(TextView txt_ProductName, ImageView img_ProductImage, ImageButton upin) {
        this.txt_ProductName = txt_ProductName;
        this.img_ProductImage = img_ProductImage;
        this.upin = upin;
    }

    public LiteratureListHolder(TextView txt_ProductName, ImageView img_ProductImage) {
        this.txt_ProductName = txt_ProductName;
        this.img_ProductImage = img_ProductImage;
    }

    public LiteratureListHolder(TextView txt_ProductName, ImageView img_ProductImage, ImageView img_share) {
        this.txt_ProductName = txt_ProductName;
        this.img_ProductImage = img_ProductImage;
        this.img_share = img_share;

    }

    public ImageView getImg_share() {
        return img_share;
    }

    public void setImg_share(ImageView img_share) {
        this.img_share = img_share;
    }

    public ImageButton getUpin() {
        return upin;
    }

    public void setUpin(ImageButton upin) {
        this.upin = upin;
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
}
