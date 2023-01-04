package skyward.pp.holder;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ANDROID 1 on 15-11-2016.
 */
public class OfferspromotionsHolder {
TextView txtpromotionname;
    ImageView imgpromotion;

    public OfferspromotionsHolder(TextView txtpromotionname, ImageView imgpromotion) {
        this.txtpromotionname = txtpromotionname;
        this.imgpromotion = imgpromotion;
    }

    public TextView getTxtpromotionname() {
        return txtpromotionname;
    }

    public void setTxtpromotionname(TextView txtpromotionname) {
        this.txtpromotionname = txtpromotionname;
    }

    public ImageView getImgpromotion() {
        return imgpromotion;
    }

    public void setImgpromotion(ImageView imgpromotion) {
        this.imgpromotion = imgpromotion;
    }
}
