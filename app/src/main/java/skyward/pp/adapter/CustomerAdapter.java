package skyward.pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import skyward.pp.ColorGenerator;
import skyward.pp.R;
import skyward.pp.TextDrawable;

/**
 * Created by ANDROID 1 on 30-09-2016.
 */
public class CustomerAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;

    private ArrayList<Customer> arraylist;


    public CustomerAdapter(Context mContext, ArrayList<Customer> arraylist) {
        this.mContext = mContext;
        this.arraylist = arraylist;
        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView name,usertype;
        ImageView icon;
        CustomerViewHolder item;
        if (convertView==null)
        {

            convertView=inflater.inflate(R.layout.customer_listitem,null);
            name=(TextView)convertView.findViewById(R.id.txt_uname);
            usertype=(TextView)convertView.findViewById(R.id.usertype);
            icon = (ImageView) convertView.findViewById(R.id.customerimage_view);
            item=new CustomerViewHolder(icon,name,usertype);
            convertView.setTag(item);
        }
        else
        {
            item=(CustomerViewHolder)convertView.getTag();
        }
        item.getName().setText(arraylist.get(position).getName());



        String firstLetter = String.valueOf(arraylist.get(position).getName().toUpperCase().charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(getItem(position));
        //int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px

        item.getIcon().setImageDrawable(drawable);
        return convertView;

    }






}
