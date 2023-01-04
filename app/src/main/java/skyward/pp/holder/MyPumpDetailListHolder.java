package skyward.pp.holder;

import android.widget.TextView;

/**
 * Created by ANDROID 1 on 07-03-2017.
 */
public class MyPumpDetailListHolder {

    TextView serialno,pumpcode,gdates;

    public MyPumpDetailListHolder(TextView serialno, TextView pumpcode) {
        this.serialno = serialno;
        this.pumpcode = pumpcode;

    }

    public TextView getSerialno() {
        return serialno;
    }

    public void setSerialno(TextView serialno) {
        this.serialno = serialno;
    }

    public TextView getPumpcode() {
        return pumpcode;
    }

    public void setPumpcode(TextView pumpcode) {
        this.pumpcode = pumpcode;
    }

    public TextView getGdates() {
        return gdates;
    }

    public void setGdates(TextView gdates) {
        this.gdates = gdates;
    }
}
