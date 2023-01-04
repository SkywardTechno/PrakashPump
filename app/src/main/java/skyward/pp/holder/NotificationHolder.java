package skyward.pp.holder;

import android.widget.TextView;

/**
 * Created by ANDROID 1 on 09-05-2017.
 */
public class NotificationHolder {

    TextView title;

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public NotificationHolder(TextView title) {
        this.title = title;
    }
}
