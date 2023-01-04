package skyward.pp.holder;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ANDROID 1 on 15-11-2016.
 */
public class WishlistViewHolder {
    TextView txtproductname,tctmodelno,txtqty,txtprice;
    ImageButton btndelete;
    ImageView imgproduct;

    public WishlistViewHolder(TextView txtproductname, TextView tctmodelno, TextView txtprice, ImageButton btndelete, ImageView imgproduct) {
        this.txtproductname = txtproductname;
        this.tctmodelno = tctmodelno;

        this.txtprice = txtprice;
        this.btndelete = btndelete;
        this.imgproduct = imgproduct;
    }


    public ImageView getImgproduct() {
        return imgproduct;
    }

    public void setImgproduct(ImageView imgproduct) {
        this.imgproduct = imgproduct;
    }

    public TextView getTxtproductname() {
        return txtproductname;
    }

    public void setTxtproductname(TextView txtproductname) {
        this.txtproductname = txtproductname;
    }

    public TextView getTctmodelno() {
        return tctmodelno;
    }

    public void setTctmodelno(TextView tctmodelno) {
        this.tctmodelno = tctmodelno;
    }

    public TextView getTxtqty() {
        return txtqty;
    }

    public void setTxtqty(TextView txtqty) {
        this.txtqty = txtqty;
    }

    public TextView getTxtprice() {
        return txtprice;
    }

    public void setTxtprice(TextView txtprice) {
        this.txtprice = txtprice;
    }

    public ImageButton getBtndelete() {
        return btndelete;
    }

    public void setBtndelete(ImageButton btndelete) {
        this.btndelete = btndelete;
    }
}
