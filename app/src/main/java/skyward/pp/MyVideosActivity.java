package skyward.pp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import skyward.pp.adapter.DisplayVideoAdapter;
import skyward.pp.adapter.MyVideos;
import skyward.pp.util.Utility;

public class MyVideosActivity extends AppCompatActivity {

    GridView grid;
    ImageButton addvideos;
    String FileName, Filepath,ID;
String ImageName,ImagePath;
    ArrayList<MyVideos> videoList;
    MyVideos video;
    Dialog pop,popattachment;
    LinearLayout ll_share,ll_play;
    String videopath="",finalpath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myvideos);

        setTitle("My Videos");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Videos");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyVideosActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        videoList = new ArrayList<MyVideos>();
        grid = (GridView) findViewById(R.id.grid);
        
        addvideos = (ImageButton) findViewById(R.id.btn_addvideos);

        addvideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyVideosActivity.this, AddVideosActivity.class);
                startActivity(i);
                finish();
            }
        });

        if(!Utility.isInternetConnected(MyVideosActivity.this)){
            Toast.makeText(MyVideosActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }else{
            new FetchVideo().execute();
        }

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                video = (MyVideos) parent.getItemAtPosition(position);

                popattachment = new Dialog(MyVideosActivity.this);
                popattachment.requestWindowFeature(Window.FEATURE_NO_TITLE);

                popattachment.setContentView(R.layout.popup_myvideos);
//                popattachment.setTitle("Attachment");



                ll_share = (LinearLayout) popattachment.findViewById(R.id.ll_share);
                ll_play = (LinearLayout) popattachment.findViewById(R.id.ll_play);

                popattachment.show();
                Window window = popattachment.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND ;
                window.setAttributes(wlp);


                ll_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PlayVideoActivity.class);
                        intent.putExtra("ID", video.getID());
                        intent.putExtra("File", video.getFilePath());
                        intent.putExtra("name", video.getFileName());
                        startActivity(intent);
                        finish();
                    }
                });


                ll_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        videopath = video.getFilePath();
                        videopath = videopath.replace("\\", "/");

                        if(videopath.contains(" ")) {
                            videopath = videopath.replace(" ", "");
                        }
                        System.out.println("**********" + videopath);
                        finalpath = Utility.URLFORIMAGE + videopath;

                        Intent sendIntent = new Intent();
                        sendIntent.setType("text/plain");
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, finalpath);

                        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // Launches the activity; Open 'Text editor' if you set it as default app to handle Text
                        startActivity(sendIntent);

                    }
                });
                // When clicked, show a toast with the TextView text

               // Toast.makeText(MyVideosActivity.this, "VideoClicked", Toast.LENGTH_SHORT).show();

               // playVideo();
            }
        });

    }


    class FetchVideo extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(MyVideosActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_VIDEOS);



            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_VIDEOS);

            //System.out.println("enpl id:" + abc);
            //request.addProperty("EmployeeID", abc);
            SoapSerializationEnvelope mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            mySoapEnvelop.setOutputSoapObject(request);
            HttpTransportSE myAndroidHttpTransport = null;
            System.out.println(Utility.URL);
            System.out.println(request);
            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);

                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.GET_VIDEOS, mySoapEnvelop);


                } catch (XmlPullParserException e) {
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    // System.out.println("XmlPullParserException 0");
                } catch (SocketTimeoutException e) {
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    // System.out.println("SocketTimeoutException 1");
                } catch (SocketException e) {
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    // System.out.println("SocketException  2");
                } catch (IOException e) {
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    System.out.println("IO Exception 3");
                    // return objLoginBean;
                }

                result = (SoapObject) mySoapEnvelop.bodyIn;
                System.out.println("exec in try");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(SoapObject result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progress.dismiss();
            if(result != null) {
                try {
                    SoapObject soapObject = (SoapObject) result.getProperty(0);
                    System.out.println(soapObject.getProperty("IsSucceed"));
                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {


                        SoapObject result1 = (SoapObject) soapObject.getProperty("Data");
                        System.out.println("Result1 is : " + result1.toString());

                        SoapObject result3 = (SoapObject) result1.getProperty(1);
                        System.out.println("Result3 is : " + result3.toString());
                        SoapObject result4 = (SoapObject) result3.getProperty(0);
                        System.out.println("Result4 is : " + result4.toString());

                        int count = result4.getPropertyCount();
                        System.out.println("Count is : " + count);
                        //customer.clear();
                        //customerID.clear();
                        videoList = new ArrayList<MyVideos>();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);

                            FileName = soapResult.getPropertySafelyAsString("FileName", "")
                                    .toString();
                            Filepath = soapResult.getPropertySafelyAsString("FilePath").toString();

                            ImageName = soapResult.getPropertySafelyAsString("ImageName", "")
                                    .toString();
                            ImagePath = soapResult.getPropertySafelyAsString("ImagePath").toString();

                            ID = soapResult.getPropertySafelyAsString("ID")
                                    .toString();
                            video = new MyVideos(ID,FileName,Filepath,ImageName,ImagePath);
                            videoList.add(video);


                            System.out.println(Filepath);
                            System.out.println(FileName);
                        }
                        grid.setAdapter(new DisplayVideoAdapter(getApplicationContext(), videoList));

                        //dataAdapter = new MyVideos(MyVideosActivity.this,R.layout.grid_listitem,videoList);
//                    displayListView();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(MyVideosActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


/*
    private void displayListView() {

        //Array list of countries
        //videoList = new ArrayList<MyVideos>();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(MyVideosActivity.this,
                R.layout.grid_listitem, videoList);
        grid = (GridView) findViewById(R.id.grid);

        grid.setAdapter(dataAdapter);
       // dataAdapter.notifyDataSetChanged();


    }
*/


/*
    private class MyCustomAdapter extends ArrayAdapter<MyVideos> {

        ArrayList<MyVideos> videoList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<MyVideos> videoList) {
            super(context, textViewResourceId, videoList);
            this.videoList = new ArrayList<MyVideos>();
            this.videoList.addAll(videoList);
        }

        private class ViewHolder {

            ImageView myvideos, btnplay;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.grid_listitem, null);

                holder = new ViewHolder();
                holder.myvideos = (ImageView) convertView.findViewById(R.id.myvideosimg);
                holder.btnplay = (ImageView) convertView.findViewById(R.id.btnplay);
                convertView.setTag(holder);

            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            MyVideos videos = videoList.get(position);

            try {
                holder.myvideos.setImageBitmap(retriveVideoFrameFromVideo("http://192.168.1.25:5090/" + videos.getFilePath()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            //holder.myvideo.setVideoPath(videos.getFilePath());
            //holder.myvideo.setText(videos.getFilePath());
            // holder.description.setText(country.getDescription());


            return convertView;


        }

    }
*/


/*
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
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
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
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }

        return bitmap;
    }
*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MyVideosActivity.this, DashboardActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater menuInflater = getSupportMenuInflater();
        getMenuInflater().inflate(R.menu.menu_home, menu);
        // return true;
        // MenuItemCompat.getActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_home){
            Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
