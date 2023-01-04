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
import skyward.pp.model.WishlistClass;
import skyward.pp.holder.WishlistViewHolder;
import skyward.pp.util.Utility;

/**
 * Created by ANDROID 1 on 15-11-2016.
 */
public class wishlistAdapter extends BaseAdapter {
    String imagepath,productname,finalpath;


    Context mContext;
    LayoutInflater inflator;
    private ArrayList<WishlistClass> arrayList;

    public wishlistAdapter(Context mContext, ArrayList<WishlistClass> videoList) {
        this.mContext = mContext;
        this.arrayList = videoList;
        inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageButton btndelete;
        ImageView imgproduct;
        TextView txtproductname,txtmodelno,txtqty,txtprice;
        final WishlistViewHolder item;

        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.item_wishlist,null);
            btndelete= (ImageButton) convertView.findViewById(R.id.imgbtn_wishlist_delete);
            txtproductname= (TextView) convertView.findViewById(R.id.txt_wishlist_productname);
            txtmodelno= (TextView) convertView.findViewById(R.id.txt_wishlist_modelno);
            txtprice= (TextView) convertView.findViewById(R.id.txt_wishlist_price);
            //txtqty= (TextView) convertView.findViewById(R.id.txt_wishlist_quantity);
            imgproduct= (ImageView) convertView.findViewById(R.id.img_wishlist_productimg);
            item = new WishlistViewHolder(txtproductname,txtmodelno,txtprice,btndelete,imgproduct);
            convertView.setTag(item);
        }
        else
        {
            item = (WishlistViewHolder) convertView.getTag();
        }
        imagepath=arrayList.get(position).getImagepath();
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
      item.getTctmodelno().setText(arrayList.get(position).getModelno());
        item.getTxtproductname().setText(arrayList.get(position).getProdutname());
        item.getTxtprice().setText(arrayList.get(position).getPrice()+"");
       // item.getTxtqty().setText(arrayList.get(position).getQty()+"");


        return convertView;
    }
}
