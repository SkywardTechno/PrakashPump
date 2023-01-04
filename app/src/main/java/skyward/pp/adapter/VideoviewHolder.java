package skyward.pp.adapter;

import android.widget.ImageView;

/**
 * Created by ANDROID 1 on 27-09-2016.
 */
public class VideoviewHolder {

    ImageView playtbtn,displayvideo;

    public VideoviewHolder(ImageView playtbtn, ImageView displayvideo) {
        this.playtbtn = playtbtn;
        this.displayvideo = displayvideo;
    }

    public ImageView getPlaytbtn() {
        return playtbtn;
    }

    public void setPlaytbtn(ImageView playtbtn) {
        this.playtbtn = playtbtn;
    }

    public ImageView getDisplayvideo() {
        return displayvideo;
    }

    public void setDisplayvideo(ImageView displayvideo) {
        this.displayvideo = displayvideo;
    }
}
