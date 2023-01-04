package skyward.pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;

import skyward.pp.holder.NotificationHolder;
import skyward.pp.model.NotificationsClass;
import skyward.pp.R;

/**
 * Created by ANDROID 1 on 24-04-2017.
 */
public class NotificationAdapter extends BaseAdapter {
    private Context context;
    TextView txtbidId, txtproduct, txtreceivedAt, txtsatus;
    LayoutInflater inflater;
    private ArrayList<NotificationsClass> arraylist;

    public NotificationAdapter(Context context) {
        this.context = context;

    }

    public NotificationAdapter(Context mContext, ArrayList<NotificationsClass> arraylist) {
        this.context = mContext;
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
        TextView title;

        NotificationHolder item;


        if (convertView==null)
        {

            convertView=inflater.inflate(R.layout.announcement_list_item,null);
            title=(TextView)convertView.findViewById(R.id.txt_noti);


            item=new NotificationHolder(title);
            convertView.setTag(item);
        }
        else
        {
            item=(NotificationHolder)convertView.getTag();
        }
        item.getTitle().setText(arraylist.get(position).getTitle());


        return convertView;

    }
}
