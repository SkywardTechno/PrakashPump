package skyward.pp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import skyward.pp.ColorGenerator;
import skyward.pp.R;
import skyward.pp.model.SupportInquiryClass;
import skyward.pp.holder.SupportInquiryHolder;
import skyward.pp.TextDrawable;
import skyward.pp.reply_supportInquiry;

/**
 * Created by Drashti on 04-10-2016.
 */
public class SupportInquiryAdapter extends BaseAdapter{


    Context mContext;
    LayoutInflater inflater;
    int rid;
    private ArrayList<SupportInquiryClass> arraylist;

    public SupportInquiryAdapter(Context mContext, ArrayList<SupportInquiryClass> arraylist) {
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

        TextView name,ServiceType,date;
        ImageView icon;
        SupportInquiryHolder item;
        Button btn_SIStatus;
        if (convertView==null)
        {

            convertView=inflater.inflate(R.layout.supportinquiry_listitem,null);
            name=(TextView)convertView.findViewById(R.id.sui_listitem_cname);
            ServiceType=(TextView)convertView.findViewById(R.id.sui_listitem_servicetype);
            date= (TextView) convertView.findViewById(R.id.sui_date);
            icon = (ImageView) convertView.findViewById(R.id.sui_listitem_image_view);
            btn_SIStatus= (Button) convertView.findViewById(R.id.btn_supportInquiryStatus);
            item=new SupportInquiryHolder(icon,name,ServiceType,date,btn_SIStatus);
            convertView.setTag(item);
        }
        else
        {
            item=(SupportInquiryHolder)convertView.getTag();
        }
        item.getName().setText(arraylist.get(position).getName());

        item.getDate().setText(arraylist.get(position).getDate());
        item.getServicetype().setText(arraylist.get(position).getServiceType());
        rid=arraylist.get(position).getReplyId();
        if(rid==0)
        {
            item.getBtn_status().setEnabled(false);
            item.getBtn_status().setText("Pending");
        }
        else
        {
            item.getBtn_status().setEnabled(true);
            item.getBtn_status().setText("Replied");
        }

        item.getBtn_status().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClass(mContext,reply_supportInquiry.class);
                i.putExtra("RID",rid);
                mContext.startActivity(i);

            }
        });
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
