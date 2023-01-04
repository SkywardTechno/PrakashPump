package skyward.pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import skyward.pp.R;
import skyward.pp.holder.OffersPromotionsDashboardHolder;
import skyward.pp.model.OffersPromotionsclass;
import skyward.pp.util.Utility;

/**
 * Created by IBM on 05-06-2017.
 */

public class OffersPromotionsDashboardAdapter extends BaseAdapter{
    Context mContext;
    LayoutInflater inflater;
    String filepath,finalpath;
    private ArrayList<OffersPromotionsclass> arraylist;

    public OffersPromotionsDashboardAdapter(Context mContext, ArrayList<OffersPromotionsclass> arraylist) {
        this.mContext = mContext;
        this.arraylist = arraylist;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        ImageView imgoffer;
        TextView title;
        final OffersPromotionsDashboardHolder item;

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.offerspromotiondashbaordlistitem,null);
            imgoffer = (ImageView) convertView.findViewById(R.id.offer_image_view);



            item = new OffersPromotionsDashboardHolder(imgoffer);
            convertView.setTag(item);


        }
        else
        {
            item = (OffersPromotionsDashboardHolder) convertView.getTag();
        }
        OffersPromotionsclass offer = arraylist.get(position);
        filepath = offer.getOfferimage();

        finalpath= Utility.URLFORIMAGE+filepath;


        try {
            Glide.with(mContext).load(finalpath).into(item.getImgpromotion());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

       // item.getTitle().setText(offer.getOffername());

        return convertView;

    }
}
