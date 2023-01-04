package skyward.pp.ecommerce;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Skyward_Mob1 on 10-03-2017.
 */
public class MyOrderDetailsHolder {

    ImageView imgproduct;
    TextView txtparoductname,txtmodelno,txtquantity,txttotalprice;

    public MyOrderDetailsHolder(ImageView imgproduct, TextView txtparoductname, TextView txtmodelno, TextView txtquantity, TextView txttotalprice) {
        this.imgproduct = imgproduct;
        this.txtparoductname = txtparoductname;
        this.txtmodelno = txtmodelno;
        this.txtquantity = txtquantity;
        this.txttotalprice = txttotalprice;
    }

    public ImageView getImgproduct() {
        return imgproduct;
    }

    public void setImgproduct(ImageView imgproduct) {
        this.imgproduct = imgproduct;
    }

    public TextView getTxtparoductname() {
        return txtparoductname;
    }

    public void setTxtparoductname(TextView txtparoductname) {
        this.txtparoductname = txtparoductname;
    }

    public TextView getTxtmodelno() {
        return txtmodelno;
    }

    public void setTxtmodelno(TextView txtmodelno) {
        this.txtmodelno = txtmodelno;
    }

    public TextView getTxtquantity() {
        return txtquantity;
    }

    public void setTxtquantity(TextView txtquantity) {
        this.txtquantity = txtquantity;
    }



    public TextView getTxttotalprice() {
        return txttotalprice;
    }

    public void setTxttotalprice(TextView txttotalprice) {
        this.txttotalprice = txttotalprice;
    }
}
