package skyward.pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import skyward.pp.R;
import skyward.pp.model.OrderSummaryClass;
import skyward.pp.holder.OrderSummaryHolder;
import skyward.pp.util.Utility;

/**
 * Created by ANDROID 1 on 10-02-2017.
 */
public class OrderSummaryAdapter extends BaseAdapter {

    String imagepath,productname,finalpath;
    Context mContext;
    LayoutInflater inflator;
    private ArrayList<OrderSummaryClass> productlist;
    public OrderSummaryAdapter(Context mContext, ArrayList<OrderSummaryClass> productlist) {
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

        LinearLayout ll_freeqty,ll_discount;
        TextView txt_freeqty,txt_discount;
        final OrderSummaryHolder item;


        if(convertView == null)
        {

            convertView = inflator.inflate(R.layout.ordersummary_listitem,null);
            img_product = (ImageView) convertView.findViewById(R.id.img_oslist_productimage);
            txt_productnamae= (TextView) convertView.findViewById(R.id.txt_oslist_productname);

            txt_modelno= (TextView) convertView.findViewById(R.id.txt_oslist_modelno);
            txt_qty= (TextView) convertView.findViewById(R.id.txt_oslist_quantity);
            txt_price= (TextView) convertView.findViewById(R.id.txt_oslist_price);
            txt_totalprice= (TextView) convertView.findViewById(R.id.txt_oslist_total);
            txt_shipping= (TextView) convertView.findViewById(R.id.txt_oslist_shippingcharge);
            ll_discount= (LinearLayout) convertView.findViewById(R.id.ll_discount);
            ll_freeqty= (LinearLayout) convertView.findViewById(R.id.ll_freeqty);
            txt_freeqty= (TextView) convertView.findViewById(R.id.txt_oslist_freeqty);
            txt_discount=  (TextView) convertView.findViewById(R.id.txt_oslist_discount);
            item = new OrderSummaryHolder(img_product,txt_productnamae,txt_modelno,txt_qty,txt_price,txt_shipping,txt_totalprice,ll_freeqty,ll_discount,txt_freeqty,txt_discount);
            convertView.setTag(item);


        }
        else
        {
            item = (OrderSummaryHolder) convertView.getTag();
        }
        imagepath=productlist.get(position).getImagepath();
        imagepath = imagepath.replace("\\", "/");


        int freeqty=productlist.get(position).getFreeqty();
        int discount=productlist.get(position).getDiscount();
        double discountamt=productlist.get(position).getDiscountamt();
        finalpath= Utility.URLFORIMAGE+imagepath;
        //MyVideos videos = arrayList.get(position);

        if(freeqty==0 && discount==0)
        {
            item.getLl_freeqty().setVisibility(View.GONE);
            item.getLl_discount().setVisibility(View.GONE);
        }
       else if(freeqty==0 && discount!=0)
        {
            item.getLl_freeqty().setVisibility(View.GONE);
            item.getLl_discount().setVisibility(View.VISIBLE);
        }
       else  if(freeqty!=0 && discount==0)
        {
            item.getLl_freeqty().setVisibility(View.VISIBLE);
            item.getLl_discount().setVisibility(View.GONE);
        }

        item.getTxt_discount().setText(productlist.get(position).getDiscountamt()+" "+productlist.get(position).getCurrency());
        item.getTxt_qty().setText(productlist.get(position).getFreeqty()+"");

        Glide.with(mContext).load(finalpath).into(item.getImgproduct());

        item.getTxtparoductname().setText(productlist.get(position).getProductame());

        item.getTxtmodelno().setText(productlist.get(position).getModelno());
        item.getTxtquantity().setText(productlist.get(position).getQuantity()+"");
       /* if(productlist.get(position).getProductid() == 12){
            item.getTxtprice().setText("130.0 " + productlist.get(position).getCurrency());
        }else{*/
            item.getTxtprice().setText(productlist.get(position).getGrandtotal()+productlist.get(position).getDiscountamt()+" "+productlist.get(position).getCurrency());
        //item.getTxtprice().setText(productlist.get(position).getGrandtotal()/productlist.get(position).getQuantity()+" "+productlist.get(position).getCurrency());

       /* String price = String.format("%.2f",productlist.get(position).getGrandtotal()/productlist.get(position).getQuantity());
        item.getTxtprice().setText(price);*/
       // item.getTxtshipping().setText(productlist.get(position).getShipping()+" "+productlist.get(position).getCurrency());
        item.getTxttotalprice().setText(productlist.get(position).getGrandtotal()+" "+productlist.get(position).getCurrency());

        return convertView;

    }

}
