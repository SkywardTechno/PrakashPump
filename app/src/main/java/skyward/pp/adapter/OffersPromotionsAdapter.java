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
import skyward.pp.model.OffersPromotionsclass;
import skyward.pp.holder.OfferspromotionsHolder;
import skyward.pp.util.Utility;

/**
 * Created by ANDROID 1 on 15-11-2016.
 */
public class OffersPromotionsAdapter extends BaseAdapter{
    Context mContext;
    LayoutInflater inflater;
String filepath,finalpath;
    private ArrayList<OffersPromotionsclass> arraylist;

    public OffersPromotionsAdapter(Context mContext, ArrayList<OffersPromotionsclass> arraylist) {
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
        TextView txtoffrename;
        final OfferspromotionsHolder item;

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.offerspromotions_listitem,null);
            imgoffer = (ImageView) convertView.findViewById(R.id.offer_image_view);
            txtoffrename = (TextView) convertView.findViewById(R.id.txt_offername);


            item = new OfferspromotionsHolder(txtoffrename,imgoffer);
            convertView.setTag(item);


        }
        else
        {
            item = (OfferspromotionsHolder) convertView.getTag();
        }
        OffersPromotionsclass offer = arraylist.get(position);
        filepath = offer.getOfferimage();

       finalpath= Utility.URLFORIMAGE+filepath;

        item.getTxtpromotionname().setText(arraylist.get(position).getOffername());
        //MyVideos videos = arrayList.get(position);
//        ImageView iv = (ImageView ) convertView.findViewById(R.id.imagePreview);
       /* ContentResolver crThumb = mContext.getContentResolver();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
        item.getDisplayvideo().setImageBitmap(curThumb);*/

        try {
            Glide.with(mContext).load(finalpath).into(item.getImgpromotion());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return convertView;

    }
}
