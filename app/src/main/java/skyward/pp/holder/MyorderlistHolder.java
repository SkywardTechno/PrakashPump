package skyward.pp.holder;

import android.widget.TextView;

/**
 * Created by Skyward_Mob1 on 09-03-2017.
 */
public class MyorderlistHolder {
    TextView txt_orderno,txt_orderdate,txt_orderprice,txt_status;

    public MyorderlistHolder(TextView txt_orderno, TextView txt_orderdate, TextView txt_orderprice, TextView txt_status) {
        this.txt_orderno = txt_orderno;
        this.txt_orderdate = txt_orderdate;
        this.txt_orderprice = txt_orderprice;
        this.txt_status = txt_status;
    }

    public TextView getTxt_orderno() {
        return txt_orderno;
    }

    public void setTxt_orderno(TextView txt_orderno) {
        this.txt_orderno = txt_orderno;
    }

    public TextView getTxt_orderdate() {
        return txt_orderdate;
    }

    public void setTxt_orderdate(TextView txt_orderdate) {
        this.txt_orderdate = txt_orderdate;
    }

    public TextView getTxt_orderprice() {
        return txt_orderprice;
    }

    public TextView getTxt_status() {
        return txt_status;
    }

    public void setTxt_status(TextView txt_status) {
        this.txt_status = txt_status;
    }

    public void setTxt_orderprice(TextView txt_orderprice) {
        this.txt_orderprice = txt_orderprice;
    }
}
