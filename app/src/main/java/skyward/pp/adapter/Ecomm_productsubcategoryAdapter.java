package skyward.pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import skyward.pp.R;
import skyward.pp.holder.Ecomm_productsubcategoryHolder;
import skyward.pp.model.ProductSubCategoryClass;

/**
 * Created by Skyward_Mob1 on 08-02-2017.
 */
public class Ecomm_productsubcategoryAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflator;
    String imagepath,productname,finalpath;

    private ArrayList<ProductSubCategoryClass> productlist;

    public Ecomm_productsubcategoryAdapter(Context mContext, ArrayList<ProductSubCategoryClass> productlist) {
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
        TextView txt_productcategory;
        final Ecomm_productsubcategoryHolder item;


        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.ecomm_category_listitem,null);

            txt_productcategory= (TextView) convertView.findViewById(R.id.txt_categoryname);
            item = new Ecomm_productsubcategoryHolder(txt_productcategory);
            convertView.setTag(item);


        }
        else
        {
            item = (Ecomm_productsubcategoryHolder) convertView.getTag();
        }
        item.getTxtcategoryname().setText(productlist.get(position).getCategoryname());
        //item.getDisplayvideo().setImageBitmap(retriveVideoFrameFromVideo("http://192.168.1.25:5090/" + filepath));


        return convertView;

    }

}
