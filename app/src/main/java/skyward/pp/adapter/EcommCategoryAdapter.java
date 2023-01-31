package skyward.pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import skyward.pp.R;
import skyward.pp.model.EcommCategoryClass;
import skyward.pp.holder.EcommCategoryHolder;

/**
 * Created by ADNROID 2 on 15-11-2016.
 */
public class EcommCategoryAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflator;
    String category_name;

    private ArrayList<EcommCategoryClass> categorylist;

    public EcommCategoryAdapter(Context mContext, ArrayList<EcommCategoryClass> categorylist) {
        this.mContext = mContext;
        this.categorylist = categorylist;
        inflator= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categorylist.size();
    }

    @Override
    public Object getItem(int position) {
        return categorylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView txt_categoryname;
        final EcommCategoryHolder item;


        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.ecomm_category_listitem,null);
            txt_categoryname= (TextView) convertView.findViewById(R.id.txt_categoryname);

            item = new EcommCategoryHolder(txt_categoryname);
            convertView.setTag(item);

        }
        else
        {
            item = (EcommCategoryHolder) convertView.getTag();
        }

        item.getCategoryName().setText(categorylist.get(position).getCategoryName());

        return convertView;

    }
}
