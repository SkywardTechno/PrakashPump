package skyward.pp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import skyward.pp.R;
import skyward.pp.model.MyorderClass;
import skyward.pp.holder.MyorderlistHolder;

/**
 * Created by Skyward_Mob1 on 09-03-2017.
 */
public class MyorderlistAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater inflator;
    private ArrayList<MyorderClass> productlist;
    public MyorderlistAdapter(Context mContext, ArrayList<MyorderClass> productlist) {
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


        TextView txt_orderno,txt_orderdate,txt_orderprice,txt_status;

        final MyorderlistHolder item;


        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.myorders_listitem,null);
            txt_orderno= (TextView) convertView.findViewById(R.id.moli_orderid);

            txt_orderdate= (TextView) convertView.findViewById(R.id.moli_date);
            txt_orderprice= (TextView) convertView.findViewById(R.id.moli_amount);
            txt_status= (TextView) convertView.findViewById(R.id.moli_status);

            item = new MyorderlistHolder(txt_orderno,txt_orderdate,txt_orderprice,txt_status);
            convertView.setTag(item);


        }
        else
        {
            item = (MyorderlistHolder) convertView.getTag();
        }

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

        String status=productlist.get(position).getStatus();
        if(status.equalsIgnoreCase("Order Placed"))
        {
            item.getTxt_status().setTextColor(Color.BLUE);
        }
       else if(status.equalsIgnoreCase("In Process"))
        {
            item.getTxt_status().setTextColor(mContext.getResources().getColor(R.color.yellow));
        }
        else if(status.equalsIgnoreCase("Dispatched"))
        {
            item.getTxt_status().setTextColor(Color.RED);
        }
        else if(status.equalsIgnoreCase("Completed"))
        {
            item.getTxt_status().setTextColor(Color.GREEN);
        }
        else if(status.equalsIgnoreCase("Cancelled"))
        {
            item.getTxt_status().setTextColor(mContext.getResources().getColor(R.color.orange));
        }
        else if(status.equalsIgnoreCase("Client Not Availed"))
        {
            item.getTxt_status().setTextColor(Color.GRAY);
        }

        item.getTxt_orderno().setText(productlist.get(position).getOrderno());
        item.getTxt_orderdate().setText(productlist.get(position).getOrderdate()+"");
        item.getTxt_orderprice().setText(productlist.get(position).getPrice()+" "+productlist.get(position).getCurrency());
        item.getTxt_status().setText(productlist.get(position).getStatus()+"");

        return convertView;

    }


}
