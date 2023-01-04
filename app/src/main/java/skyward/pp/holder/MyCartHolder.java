package skyward.pp.holder;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Skyward_Mob1 on 09-02-2017.
 */
public class MyCartHolder {
    ImageView imgproduct;
    TextView txtparoductname,txtmodelno,txtquantity,txtprice;
    ImageButton btnwishlist,btndelete,btnqtyadd,btnqtysubtract;
TextView txt_offername,txt_disconut;


    public MyCartHolder(ImageView imgproduct, TextView txtparoductname, TextView txtmodelno, TextView txtquantity, TextView txtprice, ImageButton btnwishlist, ImageButton btndelete, ImageButton btnqtyadd, ImageButton btnqtysubtract, TextView txt_offername, TextView txt_disconut) {
        this.imgproduct = imgproduct;
        this.txtparoductname = txtparoductname;
        this.txtmodelno = txtmodelno;
        this.txtquantity = txtquantity;
        this.txtprice = txtprice;
        this.btnwishlist = btnwishlist;
        this.btndelete = btndelete;
        this.btnqtyadd = btnqtyadd;
        this.btnqtysubtract = btnqtysubtract;
        this.txt_offername = txt_offername;
        this.txt_disconut = txt_disconut;
    }

    public TextView getTxt_offername() {
        return txt_offername;
    }

    public void setTxt_offername(TextView txt_offername) {
        this.txt_offername = txt_offername;
    }

    public TextView getTxt_disconut() {
        return txt_disconut;
    }

    public void setTxt_disconut(TextView txt_disconut) {
        this.txt_disconut = txt_disconut;
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

    public ImageButton getBtnwishlist() {
        return btnwishlist;
    }

    public void setBtnwishlist(ImageButton btnwishlist) {
        this.btnwishlist = btnwishlist;
    }

    public ImageButton getBtndelete() {
        return btndelete;
    }

    public void setBtndelete(ImageButton btndelete) {
        this.btndelete = btndelete;
    }

    public ImageButton getBtnqtyadd() {
        return btnqtyadd;
    }

    public void setBtnqtyadd(ImageButton btnqtyadd) {
        this.btnqtyadd = btnqtyadd;
    }

    public ImageButton getBtnqtysubtract() {
        return btnqtysubtract;
    }

    public void setBtnqtysubtract(ImageButton btnqtysubtract) {
        this.btnqtysubtract = btnqtysubtract;
    }
}
