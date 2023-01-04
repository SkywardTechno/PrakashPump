package skyward.pp.holder;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ADNROID 2 on 24-10-2016.
 */
public class AdminMultipleListHolder {

    TextView LiteratureName,FileName;
    ImageView icon;


    public AdminMultipleListHolder(TextView literatureName, TextView fileName, ImageView icon) {
        LiteratureName = literatureName;
        FileName = fileName;
        this.icon = icon;
    }

    public TextView getLiteratureName() {
        return LiteratureName;
    }

    public void setLiteratureName(TextView literatureName) {
        LiteratureName = literatureName;
    }

    public TextView getFileName() {
        return FileName;
    }

    public void setFileName(TextView fileName) {
        FileName = fileName;
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }
}
