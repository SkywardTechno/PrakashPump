package skyward.pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import skyward.pp.R;
import skyward.pp.holder.EcommSubCategoryHolder;
import skyward.pp.model.EcommSubcategoryClass;
import skyward.pp.util.Utility;

/**
 * Created by ADNROID 2 on 15-11-2016.
 */
public class EcommSubCategoryAdapter extends BaseAdapter {
    String imagepath,productname,finalpath;

    Context mContext;
    LayoutInflater inflator;

    private ArrayList<EcommSubcategoryClass> productlist;

    public EcommSubCategoryAdapter(Context mContext, ArrayList<EcommSubcategoryClass> productlist) {
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

        final EcommSubCategoryHolder item;


        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.ecomm_subcategory_item,null);
            img_product = (ImageView) convertView.findViewById(R.id.img_subcategory_productimg);
            txt_productnamae= (TextView) convertView.findViewById(R.id.txt_subcategory_productname);

            item = new EcommSubCategoryHolder(txt_productnamae,img_product);
            convertView.setTag(item);


        }
        else
        {
            item = (EcommSubCategoryHolder) convertView.getTag();
        }
        imagepath=productlist.get(position).getProduct_image();
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

        Glide.with(mContext).load(finalpath).into(item.getImg_ProductImage());
        item.getTxt_Productname().setText(productlist.get(position).getProduct_name());


        return convertView;

    }
}
