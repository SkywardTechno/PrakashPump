package skyward.pp.holder;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Drashti on 04-10-2016.
 */
public class OrderInquiryListHolder {
    ImageView icon;
    TextView name, productname,date;
    Button btn_status;

    public OrderInquiryListHolder(ImageView icon, TextView name, TextView productname, TextView date, Button btn_status) {
        this.icon = icon;
        this.name = name;
        this.productname = productname;
        this.date = date;
        this.btn_status = btn_status;
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

    public TextView getProductname() {
        return productname;
    }

    public void setProductname(TextView productname) {
        this.productname = productname;
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    public Button getBtn_status() {
        return btn_status;
    }

    public void setBtn_status(Button btn_status) {
        this.btn_status = btn_status;
    }
}
