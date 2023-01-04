package skyward.pp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import skyward.pp.model.ProductClass;
import skyward.pp.holder.ProductListHolder;
import skyward.pp.R;
import skyward.pp.util.Utility;

/**
 * Created by Drashti on 30-09-2016.
 */
public class ProductListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflator;

    private ArrayList<ProductClass> productlist;

    public ProductListAdapter(Context mContext, ArrayList<ProductClass> productlist) {
        this.mContext = mContext;
        this.productlist = productlist;
        inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    String imagepath,productname,finalpath;
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
        ImageView img_product;
        ImageView img_share;
        ImageView img_mycart;
        TextView txt_productnamae;
        String urlforimage;
        int loader = R.drawable.loader;
        final ProductListHolder item;


        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.productsmaster_listitem,null);
         img_mycart= (ImageView) convertView.findViewById(R.id.img_list_productmycart);
            img_product = (ImageView) convertView.findViewById(R.id.list_productimg);
            txt_productnamae= (TextView) convertView.findViewById(R.id.list_productname);
           img_share= (ImageView) convertView.findViewById(R.id.img_list_productshare);
            item = new ProductListHolder(txt_productnamae,img_product,img_share,img_mycart);
            convertView.setTag(item);




            img_mycart.setVisibility(View.GONE);

        }
        else
        {
            item = (ProductListHolder) convertView.getTag();
        }
        productname = productlist.get(position).getProduct_name();
        imagepath=productlist.get(position).getProduct_image();
        imagepath = imagepath.replace("\\", "/");

        item.getImg_share().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagepath = productlist.get(position).getProduct_image();
                imagepath = imagepath.replace("\\", "/");
                if(imagepath.contains(" ")) {
                    imagepath = imagepath.replace(" ", "");
                }
                System.out.println("**********" + imagepath);
                finalpath = Utility.URLFORIMAGE + imagepath;

                Intent sendIntent = new Intent();
                sendIntent.setType("text/plain");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, finalpath);

                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Launches the activity; Open 'Text editor' if you set it as default app to handle Text
                mContext.startActivity(sendIntent);


            }
        });

        finalpath= Utility.URLFORIMAGE+imagepath;
        //MyVideos videos = arrayList.get(position);

        Glide.with(mContext).load(finalpath).into(item.getImg_ProductImage());
        item.getTxt_ProductName().setText(productname);
            //item.getDisplayvideo().setImageBitmap(retriveVideoFrameFromVideo("http://192.168.1.25:5090/" + filepath));


        return convertView;
    }
    public void shareText(View v) {

    }
}
