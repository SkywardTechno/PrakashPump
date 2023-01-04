package skyward.pp.holder;

import android.widget.TextView;

/**
 * Created by Drashti on 04-10-2016.
 */
public class OrderInquiryByUserIDListHolder {
    TextView productname, date;


    public OrderInquiryByUserIDListHolder(TextView productname, TextView date) {
        this.productname = productname;
        this.date = date;
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
}
