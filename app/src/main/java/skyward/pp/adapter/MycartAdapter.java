package skyward.pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import skyward.pp.R;
import skyward.pp.model.MyCartClass;
import skyward.pp.holder.MyCartHolder;
import skyward.pp.util.Utility;

/**
 * Created by Skyward_Mob1 on 09-02-2017.
 */
public class MycartAdapter extends BaseAdapter {
    String imagepath,productname,finalpath;
    Context mContext;
    LayoutInflater inflator;
    private ArrayList<MyCartClass> productlist;
    public MycartAdapter(Context mContext, ArrayList<MyCartClass> productlist) {
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

        TextView txt_modelno,txt_price,txt_qty;
        ImageButton btnwishlist,btndelete,btnaddqty,btnsubtractqty;

        final MyCartHolder item;


        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.mycart_listitem,null);
            img_product = (ImageView) convertView.findViewById(R.id.offer_image_view);
            txt_productnamae= (TextView) convertView.findViewById(R.id.txt_cart_productname);

            txt_modelno= (TextView) convertView.findViewById(R.id.txt_cart_modelno);
            txt_price= (TextView) convertView.findViewById(R.id.txt_cart_price);
            txt_qty= (TextView) convertView.findViewById(R.id.display_quantity);
            btnwishlist= (ImageButton) convertView.findViewById(R.id.imgbtn_cart_addtowishlist);
            btndelete= (ImageButton) convertView.findViewById(R.id.imgbtn_cart_delete);
            btnaddqty= (ImageButton) convertView.findViewById(R.id.quantity_add);
            btnsubtractqty= (ImageButton) convertView.findViewById(R.id.quantity_subtract);

//            item = new MyCartHolder(img_product,txt_productnamae,txt_modelno,txt_qty,txt_price,btnwishlist,btndelete,btnaddqty,btnsubtractqty);
           // convertView.setTag(item);


        }
        else
        {
            item = (MyCartHolder) convertView.getTag();
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
  ImageView img_product;
        TextView txt_productnamae;

        TextView txt_modelno,txt_price,txt_qty;
        ImageButton btnwishlist,btndelete,btnaddqty,btnsubtractqty;

        final MyCartHolder item;


        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.mycart_listitem,null);
            img_product = (ImageView) convertView.findViewById(R.id.offer_image_view);
            txt_productnamae= (TextView) convertView.findViewById(R.id.txt_cart_productname);

            txt_modelno= (TextView) convertView.findViewById(R.id.txt_cart_modelno);
            txt_price= (TextView) convertView.findViewById(R.id.txt_cart_price);
            txt_qty= (TextView) convertView.findViewById(R.id.display_quantity);
            btnwishlist= (ImageButton) convertView.findViewById(R.id.imgbtn_cart_addtowishlist);
            btndelete= (ImageButton) convertView.findViewById(R.id.imgbtn_cart_delete);
            btnaddqty= (ImageButton) convertView.findViewById(R.id.quantity_add);
            btnsubtractqty= (ImageButton) convertView.findViewById(R.id.quantity_subtract);

            item = new MyCartHolder(img_product,txt_productnamae,txt_modelno,txt_qty,txt_price,btnwishlist,btndelete,btnaddqty,btnsubtractqty);
            convertView.setTag(item);


        }
        else
        {
            item = (MyCartHolder) convertView.getTag();
        }
        imagepath=productlist.get(position).getImagepath();
        imagepath = imagepath.replace("\\", "/");
/*
        ImageLoader imgLoader = new
        }*/

        finalpath= Utility.URLFORIMAGE+imagepath;
        //MyVideos videos = arrayList.get(position);

   //     Glide.with(mContext).load(finalpath).into(item.getImgproduct());
        //item.getTxtparoductname().setText(productlist.get(position).getProductame());

       // item.getTxtmodelno().setText(productlist.get(position).getModelno());
        //item.getTxtquantity().setText(productlist.get(position).getQuantity()+"");
       // item.getTxtprice().setText(productlist.get(position).getPrice()+"");

        return convertView;

    }

}
