package skyward.pp.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import skyward.pp.R;
import skyward.pp.util.Utility;

/**
 * Created by ANDROID 1 on 27-09-2016.
 */
public class DisplayVideoAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater inflator;
    private ArrayList<MyVideos> videoList;
    String filepath,filename,imagename,imagepath,finalpath;



    public DisplayVideoAdapter(Context mContext, ArrayList<MyVideos> arrayList) {
        this.mContext = mContext;
        this.videoList = arrayList;
        inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView btnplay, videoplay;
        final VideoviewHolder item;

        if(convertView == null)
        {
            convertView = inflator.inflate(R.layout.grid_listitem,null);
            btnplay = (ImageView) convertView.findViewById(R.id.btnplay);
            videoplay = (ImageView) convertView.findViewById(R.id.myvideosimg);

            item = new VideoviewHolder(btnplay,videoplay);
            convertView.setTag(item);


        }
        else
        {
            item = (VideoviewHolder) convertView.getTag();
        }
        MyVideos videos = videoList.get(position);
        filepath = videos.getFilePath();
        filename =  videos.getFileName();
imagename=videos.getImageName();
        imagepath=videos.getImagePath();
        finalpath= Utility.URLFORIMAGE+imagepath;

        //MyVideos videos = arrayList.get(position);
//        ImageView iv = (ImageView ) convertView.findViewById(R.id.imagePreview);
       /* ContentResolver crThumb = mContext.getContentResolver();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
        item.getDisplayvideo().setImageBitmap(curThumb);*/
        try {
            Glide.with(mContext).load(finalpath).into(item.getDisplayvideo());
            item.getDisplayvideo().setMaxHeight(200);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return convertView;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        //bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
            //mediaMetadataRetriever.setDataSource(videoPath,1000,1000);
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {

                mediaMetadataRetriever.release();

        }

        return bitmap;
    }

}
