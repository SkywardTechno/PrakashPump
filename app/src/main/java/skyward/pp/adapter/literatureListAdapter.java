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

import skyward.pp.model.LiteratureListClass;
import skyward.pp.holder.LiteratureListHolder;
import skyward.pp.R;
import skyward.pp.util.Utility;

/**
 * Created by Drashti on 06-10-2016.
 */
public class literatureListAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflator;
    String imagepath,productname,finalpath;

    private ArrayList<LiteratureListClass> productlist;

    public literatureListAdapter(Context mContext, ArrayList<LiteratureListClass> productlist) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img_product;
        ImageButton upin;
        TextView txt_productnamae;
        String urlforimage;
        int loader = R.drawable.loader;
        final LiteratureListHolder item;


        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.adminliterature_listitem,null);
            img_product = (ImageView) convertView.findViewById(R.id.list_productimg);
            txt_productnamae= (TextView) convertView.findViewById(R.id.list_productname);
            upin= (ImageButton) convertView.findViewById(R.id.list_upin);
            item = new LiteratureListHolder(txt_productnamae,img_product,upin);
            convertView.setTag(item);


        }
        else
        {
            item = (LiteratureListHolder) convertView.getTag();
        }
        productname = productlist.get(position).getProduct_name();
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
        item.getTxt_ProductName().setText(productname);
        //item.getDisplayvideo().setImageBitmap(retriveVideoFrameFromVideo("http://192.168.1.25:5090/" + filepath));


        return convertView;

    }
}
