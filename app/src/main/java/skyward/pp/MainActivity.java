package skyward.pp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import skyward.pp.util.Utility;


public class  MainActivity extends AppCompatActivity {


    int mFlipping = 0 ;
    SharedPreferences prefs = null;
    VideoView v1;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);

        if (mFlipping == 0) {
            /** Start Flipping */
            flipper.startFlipping();
            mFlipping = 1;

        }

       // v1 = (VideoView) findViewById(R.id.splash_video);
        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

        if (!Utility.isInternetConnected(MainActivity.this)) {

            Toast.makeText(getApplicationContext(), "Please Check your Internet Connection and come again!", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 4000);

        }else{
            Thread timer = new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        Thread.sleep(4000);
                        if (!isPaused) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (TextUtils.isEmpty(Utility.getAuthToken(MainActivity.this))) {
                                        if (prefs.getBoolean("firstrun", true)) {

                                            prefs.edit().putBoolean("firstrun", false).commit();
                                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();


                                        }else

                                        {
                                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else {
                                        if(!TextUtils.isEmpty(Utility.getIsAdministrator(MainActivity.this))){
                                            if(Utility.getIsAdministrator(MainActivity.this).equals("true")) {
                                                Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Intent intent = new Intent(MainActivity.this, LaunchActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }else{

                                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                }
                            });



                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            timer.start();

        }

       /* if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
        {

            String[] perms = {"android.permission.READ_SMS" ,"android.permission.INTERNET" , "android.permission.WRITE_EXTERNAL_STORAGE" ,"android.permission.READ_EXTERNAL_STORAGE"};

            int permsRequestCode = 200;

            requestPermissions(perms, permsRequestCode);

        }*/


      /*  MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(v1);
//        v1.setMediaController(mediaController);
        v1.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.movelogo));
        v1.start();
        v1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
*/
    }



   /* protected void onResume() {
        super.onResume();
        isPaused = false;
        Thread timer = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    Thread.sleep(4000);
                    if (!isPaused) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (TextUtils.isEmpty(Utility.getAuthToken(MainActivity.this))) {
                                    if (prefs.getBoolean("firstrun", true)) {

                                        prefs.edit().putBoolean("firstrun", false).commit();
                                        Intent intent = new Intent(MainActivity.this, NumberVerificationActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }else

                                    {
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    if(!TextUtils.isEmpty(Utility.getIsAdministrator(MainActivity.this))){
                                        if(Utility.getIsAdministrator(MainActivity.this).equals("true")) {
                                            Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Intent intent = new Intent(MainActivity.this, LaunchActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }else{

                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            }
                        });



                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timer.start();
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
