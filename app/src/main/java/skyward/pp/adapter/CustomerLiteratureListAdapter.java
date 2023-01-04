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

import skyward.pp.holder.LiteratureListHolder;
import skyward.pp.R;
import skyward.pp.util.Utility;

/**
 * Created by ANDROID 1 on 06-10-2016.
 */
public class CustomerLiteratureListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflator;
    String imagepath,productname,finalpath;

    private ArrayList<Literature> productlist;

    public CustomerLiteratureListAdapter(Context mContext, ArrayList<Literature> productlist) {
        this.mContext = mContext;
        this.productlist = productlist;
        inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView img_product,img_share;
        TextView txt_productnamae;
        String urlforimage;
        int loader = R.drawable.loader;
        final LiteratureListHolder item;


        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.customerliterature_listitem,null);
            img_product = (ImageView) convertView.findViewById(R.id.clist_productimg);
            img_share = (ImageView) convertView.findViewById(R.id.img_shareliterature);
            txt_productnamae= (TextView) convertView.findViewById(R.id.clist_productname);
            item = new LiteratureListHolder(txt_productnamae,img_product,img_share);
            convertView.setTag(item);


        }
        else
        {
            item = (LiteratureListHolder) convertView.getTag();
        }
        productname = productlist.get(position).getLiteratureName();
        imagepath=productlist.get(position).getLiteraturePath();
        imagepath = imagepath.replace("\\", "/");


        finalpath= Utility.URLFORIMAGE+imagepath;
        //MyVideos videos = arrayList.get(position);

        Glide.with(mContext).load(finalpath).into(item.getImg_ProductImage());
        item.getTxt_ProductName().setText(productname);
        //item.getDisplayvideo().setImageBitmap(retriveVideoFrameFromVideo("http://192.168.1.25:5090/" + filepath));

        return convertView;

    }


}
