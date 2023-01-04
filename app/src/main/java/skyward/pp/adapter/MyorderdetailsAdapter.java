package skyward.pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import skyward.pp.R;
import skyward.pp.ecommerce.MyOrderDetailsHolder;
import skyward.pp.model.MyordersDetailsClass;
import skyward.pp.util.Utility;

/**
 * Created by Skyward_Mob1 on 10-03-2017.
 */
public class MyorderdetailsAdapter extends BaseAdapter{
    String imagepath,productname,finalpath;
    Context mContext;
    LayoutInflater inflator;
    private ArrayList<MyordersDetailsClass> productlist;
    public MyorderdetailsAdapter(Context mContext, ArrayList<MyordersDetailsClass> productlist) {
        this.mContext = mContext;
        this.productlist = productlist;
        inflator= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return productlist.size();
    }

    @Override
    public Object getItem(int position) {
        return productlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img_product;
        TextView txt_productnamae;

        TextView txt_modelno,txt_price,txt_qty,txt_totalprice,txt_shipping;
        ImageButton btnwishlist,btndelete,btnaddqty,btnsubtractqty;

        final MyOrderDetailsHolder item;


        if(convertView == null)
        {

            convertView = inflator.inflate(R.layout.myorders_details_listitem,null);
            img_product = (ImageView) convertView.findViewById(R.id.img_modli_productimage);
            txt_productnamae= (TextView) convertView.findViewById(R.id.txt_modli_productname);

            txt_modelno= (TextView) convertView.findViewById(R.id.txt_modli_modelno);
            txt_qty= (TextView) convertView.findViewById(R.id.txt_modli_quantity);
            txt_totalprice= (TextView) convertView.findViewById(R.id.txt_modli_total);


            item = new MyOrderDetailsHolder(img_product,txt_productnamae,txt_modelno,txt_qty,txt_totalprice);
            convertView.setTag(item);


        }
        else
        {
            item = (MyOrderDetailsHolder) convertView.getTag();
        }
        imagepath=productlist.get(position).getImagepath();
        imagepath = imagepath.replace("\\", "/");
/*
        ImageLoader imgLoader = new ImageLoader(productlist.get(position).getContext());
        imgLoader.DisplayImage("http://192.168.1.25:5090/ProfileImage/images(1)_d6e28626-0b47-45ce-9651-a14b8dede9db.jpg", loader, item.getImg_ProductImage());

//imagepath = "ProfileImage\images(1)_d6e28626-0b47-45ce-9651-a14b8dede9db.jpg";
        String newimg;
        if (imagepath != null) {
            try {

                newimg = imagepath.replace("\\", "/");
                //imagepath = imagepath.replaceAll(" ", "%20");
                urlforimage = Utility.URLFORIMAGE  + newimg;

                System.out.println("Image path is : " + urlforimage);


            } catch (NullPointerException e) {

            }

        }*/

        finalpath= Utility.URLFORIMAGE+imagepath;
        //MyVideos videos = arrayList.get(position);

        Glide.with(mContext).load(finalpath).into(item.getImgproduct());

        item.getTxtparoductname().setText(productlist.get(position).getProductame());

        item.getTxtmodelno().setText(productlist.get(position).getModelno());
        item.getTxtquantity().setText(productlist.get(position).getQuantity()+"");
        item.getTxttotalprice().setText(productlist.get(position).getPrice()+" " + productlist.get(position).getCurrency());

        return convertView;

    }


}
