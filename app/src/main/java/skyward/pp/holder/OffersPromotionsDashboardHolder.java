package skyward.pp.holder;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by IBM on 05-06-2017.
 */

public class OffersPromotionsDashboardHolder {
    ImageView imgpromotion;
    TextView title;

    public OffersPromotionsDashboardHolder(ImageView imgpromotion) {
        this.imgpromotion = imgpromotion;
    }

    public OffersPromotionsDashboardHolder(ImageView imgpromotion, TextView title) {
        this.imgpromotion = imgpromotion;
        this.title = title;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public ImageView getImgpromotion() {
        return imgpromotion;
    }

    public void setImgpromotion(ImageView imgpromotion) {
        this.imgpromotion = imgpromotion;
    }
}
