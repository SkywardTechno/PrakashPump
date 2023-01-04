package skyward.pp.holder;

import android.widget.TextView;

/**
 * Created by Skyward_Mob1 on 08-02-2017.
 */
public class Ecomm_productsubcategoryHolder {

TextView txtcategoryname;

    public TextView getTxtcategoryname() {
        return txtcategoryname;
    }

    public void setTxtcategoryname(TextView txtcategoryname) {
        this.txtcategoryname = txtcategoryname;
    }

    public Ecomm_productsubcategoryHolder(TextView txtcategoryname) {

        this.txtcategoryname = txtcategoryname;
    }
}
