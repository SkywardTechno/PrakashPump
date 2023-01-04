package skyward.pp.adapter;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ANDROID 1 on 30-09-2016.
 */
public class CustomerViewHolder {

    ImageView icon,videoicon;
    TextView name, usertype;

    public CustomerViewHolder(ImageView icon, TextView name, TextView usertype) {
        this.icon = icon;
        this.name = name;
        this.usertype = usertype;
    }

    public CustomerViewHolder(ImageView icon, ImageView videoicon, TextView name, TextView usertype) {
        this.icon = icon;
        this.videoicon = videoicon;
        this.name = name;
        this.usertype = usertype;
    }

    public ImageView getVideoicon() {
        return videoicon;
    }

    public void setVideoicon(ImageView videoicon) {
        this.videoicon = videoicon;
    }

    public TextView getUsertype() {
        return usertype;
    }

    public void setUsertype(TextView usertype) {
        this.usertype = usertype;
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }
}
