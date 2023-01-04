package skyward.pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import skyward.pp.model.AdminMultipleListClass;
import skyward.pp.holder.AdminMultipleListHolder;
import skyward.pp.R;

/**
 * Created by ADNROID 2 on 24-10-2016.
 */
public class AdminMultipleListAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater inflator;
    String filename,literaturename,filepath;

    private ArrayList<AdminMultipleListClass> productlist;


    public AdminMultipleListAdapter(Context mContext, ArrayList<AdminMultipleListClass> productlist) {
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
        TextView txt_literaturename,fname;
        String urlforimage;

        final AdminMultipleListHolder item;


        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.multipleliterature_listitem,null);
            img_product = (ImageView) convertView.findViewById(R.id.listitem_image_view);
            txt_literaturename= (TextView) convertView.findViewById(R.id.listitem_lname);
            fname= (TextView) convertView.findViewById(R.id.listitem_fname);
            item = new AdminMultipleListHolder(txt_literaturename,fname,img_product);
            convertView.setTag(item);


        }
        else
        {
            item = (AdminMultipleListHolder) convertView.getTag();
        }
        literaturename = productlist.get(position).getLiterature_list();
        filename=productlist.get(position).getLiterature_name();



        item.getLiteratureName().setText(literaturename);
        item.getFileName().setText(filename);
        //item.getDisplayvideo().setImageBitmap(retriveVideoFrameFromVideo("http://192.168.1.25:5090/" + filepath));


        return convertView;

    }
}
