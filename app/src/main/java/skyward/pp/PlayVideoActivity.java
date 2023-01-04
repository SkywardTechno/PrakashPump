package skyward.pp;

import android.app.ProgressDialog;
import android.content.Intent;


import android.media.MediaPlayer;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import skyward.pp.util.Utility;

public class PlayVideoActivity extends AppCompatActivity {

    VideoView vplay;
    ProgressDialog pDialog;
    String VideoURL,path,name;
    int a = 0;
    int productID,headID,flowrateID,producttypeID,typeID,powerID,inletID,outletID,volt;
    Double headfeet,powerhp;
    String Required,Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        vplay=(VideoView) findViewById(R.id.videoviewplay);

        Intent i = getIntent();
        if(i.hasExtra("File") && i.hasExtra("name"))
        {
            path = i.getStringExtra("File");
            name = i.getStringExtra("name");
            VideoURL = Utility.URLFORIMAGE + path;
            a=1;
        }

        else
        {
            path = i.getStringExtra("videopath");
            name = i.getStringExtra("productname");
            powerID = i.getIntExtra("powerID", 0);
            flowrateID = i.getIntExtra("flowrateID",0);
            headID = i.getIntExtra("headID",0);
            producttypeID = i.getIntExtra("producttypeID", 0);
            typeID =i.getIntExtra("typeID", 0);
            productID = i.getIntExtra("productid",0);
            Text = i.getStringExtra("TcDetails");
            Required = i.getStringExtra("required");
            inletID=i.getIntExtra("Inlet",0);

            outletID=i.getIntExtra("Outlet",0);

            headfeet=i.getDoubleExtra("HeadFeet", 0.0);

            powerhp=i.getDoubleExtra("PowerHP", 0.0);
            volt=i.getIntExtra("Volt",0);
            path = path.replace("\\", "/");
            //imagepath = imagepath.replaceAll(" ", "%20");
            VideoURL = Utility.URLFORIMAGE  + path;
            a=2;

        }

        pDialog = new ProgressDialog(PlayVideoActivity.this);
        // Set progressbar title
        pDialog.setTitle(name);
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(PlayVideoActivity.this);
            mediacontroller.setAnchorView(vplay);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(VideoURL);
            vplay.setMediaController(mediacontroller);
            vplay.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        vplay.requestFocus();
        vplay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                vplay.start();
            }
        });
        vplay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if(a == 1) {
                    Intent i = new Intent(PlayVideoActivity.this, MyVideosActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(PlayVideoActivity.this, ViewProductActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("Power",powerID);
                    b.putInt("FlowRate",flowrateID);
                    b.putInt("Head", headID);
                    b.putInt("ProductType", producttypeID);
                    b.putInt("Type", typeID);
                    b.putInt("productID", productID);
                    b.putInt("Inlet", inletID);
                    b.putInt("Outlet", outletID);
                    b.putDouble("HeadFeet", headfeet);
                    b.putDouble("PowerHP", powerhp);
                    b.putInt("Volt", volt);
                    b.putString("TcDetails",Text);
                    b.putString("required", Required);
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(vplay.isPlaying()){
            vplay.stopPlayback();
            if(a == 1) {
                Intent i = new Intent(PlayVideoActivity.this, MyVideosActivity.class);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(PlayVideoActivity.this, ViewProductActivity.class);
                Bundle b = new Bundle();
                b.putInt("Power",powerID);
                b.putInt("FlowRate",flowrateID);
                b.putInt("Head", headID);
                b.putInt("ProductType", producttypeID);
                b.putInt("Type", typeID);
                b.putInt("productID", productID);
                b.putInt("Inlet", inletID);
                b.putInt("Outlet", outletID);
                b.putDouble("HeadFeet", headfeet);
                b.putDouble("PowerHP", powerhp);
                b.putInt("Volt", volt);
                b.putString("TcDetails",Text);
                b.putString("required", Required);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        }
        if(a == 1) {
            Intent i = new Intent(PlayVideoActivity.this, MyVideosActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(PlayVideoActivity.this, ViewProductActivity.class);
            Bundle b = new Bundle();
            b.putInt("Power",powerID);
            b.putInt("FlowRate",flowrateID);
            b.putInt("Head", headID);
            b.putInt("ProductType", producttypeID);
            b.putInt("Type", typeID);
            b.putInt("productID", productID);
            b.putInt("Inlet", inletID);
            b.putInt("Outlet", outletID);
            b.putDouble("HeadFeet", headfeet);
            b.putDouble("PowerHP", powerhp);
            b.putInt("Volt", volt);
            b.putString("TcDetails",Text);
            b.putString("required", Required);
            i.putExtras(b);
            startActivity(i);
            finish();
        }
    }
}
