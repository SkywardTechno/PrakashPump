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
 * Created by ANDROID 1 on 09-03-2017.
 */
public class PumpSelectionProductListAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflator;

    private ArrayList<ProductClass> productlist;

    public PumpSelectionProductListAdapter(Context mContext, ArrayList<ProductClass> productlist) {
        this.mContext = mContext;
        this.productlist = productlist;
        inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    String imagepath, productname, finalpath;

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


        if (convertView == null) {
            convertView = inflator.inflate(R.layout.productsmaster_listitem, null);
            img_mycart = (ImageView) convertView.findViewById(R.id.img_list_productmycart);
            img_product = (ImageView) convertView.findViewById(R.id.list_productimg);
            txt_productnamae = (TextView) convertView.findViewById(R.id.list_productname);
            img_share = (ImageView) convertView.findViewById(R.id.img_list_productshare);
            item = new ProductListHolder(txt_productnamae, img_product, img_share, img_mycart);
            convertView.setTag(item);


            img_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
/*
                    Toast.makeText(mContext, "button is pressed", Toast.LENGTH_SHORT).show();
*/
                    Intent sendIntent = new Intent();
                    // Set the action to be performed i.e 'Send Data'
                    sendIntent.setAction(Intent.ACTION_SEND);
                    imagepath = productlist.get(position).getProduct_image();
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

                    finalpath = Utility.URLFORIMAGE + imagepath;

                    // Add the text to the intent
                    sendIntent.putExtra(Intent.EXTRA_TEXT, finalpath);
                    // Set the type of data i.e 'text/plain'
                    sendIntent.setType("text/plain");
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // Launches the activity; Open 'Text editor' if you set it as default app to handle Text
                    mContext.startActivity(sendIntent);


                }
            });

           // img_mycart.setVisibility(View.GONE);
            img_mycart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        } else {
            item = (ProductListHolder) convertView.getTag();
        }
        productname = productlist.get(position).getProduct_name();
        imagepath = productlist.get(position).getProduct_image();
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

        finalpath = Utility.URLFORIMAGE + imagepath;
        //MyVideos videos = arrayList.get(position);

        Glide.with(mContext).load(finalpath).into(item.getImg_ProductImage());
        item.getTxt_ProductName().setText(productname);
        //item.getDisplayvideo().setImageBitmap(retriveVideoFrameFromVideo("http://192.168.1.25:5090/" + filepath));


        return convertView;
    }

    public void shareText(View v) {

    }
}
