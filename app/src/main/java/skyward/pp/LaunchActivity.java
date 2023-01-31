package skyward.pp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sinch.android.rtc.SinchError;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import skyward.pp.ecommerce.EcommDashboardActivity;
import skyward.pp.util.Utility;

public class LaunchActivity extends BaseActivity implements SinchService.StartFailedListener{


    int mFlipping = 0 ;
    Button support, ecommerce;
    ImageView logout;
    Dialog pop_confimation;
    TextView txtpop_msg;
    Button btnpop_cancel;
    Button getBtnpop_submit;
    String navigatetext="";
    public static final int MY_PERMISSIONS_ALL=1;
    public static final int MY_PERMISSIONS_RECORD_AUDIO=3;
    public static final int MY_PERMISSIONS_MODIFY_AUDIO_SETTINGS=4;
    public static final int MY_PERMISSIONS_CAMERA=5;
    public static final int MY_PERMISSIONS_RECEIVE_BOOT_COMPLETED=6;
    public static final int MY_PERMISSIONS_READ_PHONE_STATE=7;
    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE=8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);

        if (mFlipping == 0) {
            /** Start Flipping */
            flipper.startFlipping();
            mFlipping = 1;

        }

        support = (Button) findViewById(R.id.support);
        support.setText("Support & Service");
        ecommerce = (Button) findViewById(R.id.ecommerce);
        logout = (ImageView) findViewById(R.id.launchlogout);

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LaunchActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        ecommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LaunchActivity.this, "This functionality is under development. Please contact your admin.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LaunchActivity.this, EcommDashboardActivity.class);
                startActivity(i);
                finish();

            }
        });


        if(getIntent().hasExtra("fromlogin")){
            navigatetext = "fromlogin";
        }else{
            navigatetext = "fromother";
        }

        if(navigatetext.equalsIgnoreCase("fromlogin")) {
            if (Utility.isInternetConnected(getApplicationContext())) {
                FetchProductType task = new FetchProductType();
                task.execute();
            } else {
                Toast.makeText(LaunchActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();
            }
        }else{

        }

        if (ContextCompat.checkSelfPermission(LaunchActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(LaunchActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(LaunchActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(LaunchActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(LaunchActivity.this,
                Manifest.permission.MODIFY_AUDIO_SETTINGS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(LaunchActivity.this,
                Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(LaunchActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.RECEIVE_BOOT_COMPLETED},
                    MY_PERMISSIONS_ALL);
        }



        Intent alarm = new Intent(getApplicationContext(), OrderUpdateStatusReceiver.class);
        PendingIntent pendingIntent = null;
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_MUTABLE) != null);
        if (alarmRunning == false) {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, PendingIntent.FLAG_MUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
        }


        Intent alarm2 = new Intent(getApplicationContext(), SinchReceiver.class);
        PendingIntent pendingIntent2 = null;
        boolean alarmRunning2 = (PendingIntent.getBroadcast(getApplicationContext(), 0, alarm2, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_MUTABLE) != null);
        if (alarmRunning2 == false) {
            pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm2, PendingIntent.FLAG_MUTABLE);
            AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager2.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 30000, pendingIntent2);
        }


        logout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog alertDialog = new AlertDialog.Builder(LaunchActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are you sure you want to Logout?");
        alertDialog.setCancelable(false);
        alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (!Utility.isInternetConnected(LaunchActivity.this)) {

                            Toast.makeText(LaunchActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                        } else {
                            new LogoutTask().execute();
                        }

                    }
                });
        alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();

    }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            // If request is cancelled, the result arrays are empty.
            case MY_PERMISSIONS_ALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(LaunchActivity.this, "Since you have not granted all permissions, you will not able to use some features of the app. \n You can grant the permissions from application settings for this app", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
        // other 'case' lines to check for other
        // permissions this app might request

    }


    @Override
    protected void onServiceConnected() {
        try{
            getSinchServiceInterface().setStartListener(this);
            getSinchServiceInterface().startClient(Utility.getCallUser(getApplicationContext()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStarted() {
        try{
            getSinchServiceInterface().startClient(Utility.getCallUser(getApplicationContext()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    class FetchProductType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");

        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTTYPE);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));


            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_PRODUCTTYPE);

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
                            + Utility.GET_PRODUCTTYPE, mySoapEnvelop);


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
                        SoapObject result5 = (SoapObject) result4.getProperty(0);
                        System.out.println("Result5 is : " + result5.toString());

                        int count = result4.getPropertyCount();
                        System.out.println("Count is : " + count);
                        //customer.clear();
                        //customerID.clear();

                       Utility.saveProductCategoryId(getApplicationContext(), Integer.parseInt(result5.getPrimitivePropertySafelyAsString("ID").toString()));

                    } else {
                        Utility.saveProductCategoryId(getApplicationContext(), 1);

                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    Utility.saveProductCategoryId(getApplicationContext(), 1);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Utility.saveProductCategoryId(getApplicationContext(), 1);

                }

            }else {
                Utility.saveProductCategoryId(getApplicationContext(), 1);

                Toast.makeText(LaunchActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class LogoutTask extends AsyncTask<Void, Void, SoapObject> {
        private ProgressDialog progress;
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(LaunchActivity.this);
            progress.setMessage("Logging out...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.LOGOUT);

            request.addProperty("token", Utility.getAuthToken(LaunchActivity.this));

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
                            + Utility.LOGOUT, mySoapEnvelop);
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


            }

            catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(SoapObject result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progress.dismiss();
            try {
                SoapObject soapObject = (SoapObject) result.getProperty(0);
                System.out.println(soapObject.getProperty("IsSucceed"));
                if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                    boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                            "IsSucceed").toString());

                    Utility.saveAuthToken(LaunchActivity.this, "");
                    Utility.saveUserType(LaunchActivity.this, "");
                    Utility.saveUserName(LaunchActivity.this, "");
                    Utility.saveUserNamefordisplay(LaunchActivity.this, "");


                    Intent intent = new Intent(getApplicationContext(), OrderUpdateStatusReceiver.class);
                    PendingIntent pendingIntent = null;
                    boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_MUTABLE) != null);
                    if(alarmRunning == true) {
                        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
                        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);
                    }


                    Toast.makeText(LaunchActivity.this, "You have logged out successfully", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(LaunchActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    if (getSinchServiceInterface() != null) {
                        getSinchServiceInterface().stopClient();
                    }


                } else {
                    Toast.makeText(getApplicationContext(),
                            soapObject.getProperty("ErrorMessage").toString(),
                            Toast.LENGTH_LONG).show();
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
