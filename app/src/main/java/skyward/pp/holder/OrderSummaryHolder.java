package skyward.pp.holder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ANDROID 1 on 10-02-2017.
 */
public class OrderSummaryHolder {
    ImageView imgproduct;
    TextView txtparoductname,txtmodelno,txtquantity,txtprice,txtshipping,txttotalprice;
    LinearLayout ll_freeqty,ll_discount;
    TextView txt_qty,txt_discount;

    public OrderSummaryHolder(ImageView imgproduct, TextView txtparoductname, TextView txtmodelno, TextView txtquantity, TextView txtprice, TextView txtshipping, TextView txttotalprice, LinearLayout ll_freeqty, LinearLayout ll_discount, TextView txt_qty, TextView txt_discount) {
        this.imgproduct = imgproduct;
        this.txtparoductname = txtparoductname;
        this.txtmodelno = txtmodelno;
        this.txtquantity = txtquantity;
        this.txtprice = txtprice;
        this.txtshipping = txtshipping;
        this.txttotalprice = txttotalprice;
        this.ll_freeqty = ll_freeqty;
        this.ll_discount = ll_discount;
        this.txt_qty = txt_qty;
        this.txt_discount = txt_discount;
    }

    public LinearLayout getLl_freeqty() {
        return ll_freeqty;
    }

    public void setLl_freeqty(LinearLayout ll_freeqty) {
        this.ll_freeqty = ll_freeqty;
    }

    public LinearLayout getLl_discount() {
        return ll_discount;
    }

    public void setLl_discount(LinearLayout ll_discount) {
        this.ll_discount = ll_discount;
    }

    public TextView getTxt_qty() {
        return txt_qty;
    }

    public void setTxt_qty(TextView txt_qty) {
        this.txt_qty = txt_qty;
    }

    public TextView getTxt_discount() {
        return txt_discount;
    }

    public void setTxt_discount(TextView txt_discount) {
        this.txt_discount = txt_discount;
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

    public TextView getTxtprice() {
        return txtprice;
    }

    public void setTxtprice(TextView txtprice) {
        this.txtprice = txtprice;
    }

    public TextView getTxtshipping() {
        return txtshipping;
    }

    public void setTxtshipping(TextView txtshipping) {
        this.txtshipping = txtshipping;
    }

    public TextView getTxttotalprice() {
        return txttotalprice;
    }

    public void setTxttotalprice(TextView txttotalprice) {
        this.txttotalprice = txttotalprice;
    }
}
