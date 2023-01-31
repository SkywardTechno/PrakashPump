package skyward.pp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
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

import skyward.pp.util.Utility;

public class AdminDashboardActivity extends BaseActivity implements SinchService.StartFailedListener{

    Dialog pop_confimation;
    TextView txtpop_msg;
    Button btnpop_cancel;
    Button getBtnpop_submit;
    public static final int MY_PERMISSIONS_ALL=1;
    LinearLayout customer,productmaster,orderinquiry,supportserviceinquiry,termscondition,categorydata,literature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_new);

        setTitle("Admin Dashboard");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Admin Dashboard");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();
        customer = (LinearLayout) findViewById(R.id.dash_ll_customer);
       // productmaster = (LinearLayout) findViewById(R.id.dash_ll_productmaster);
        orderinquiry = (LinearLayout) findViewById(R.id.dash_ll_orderinquiry);
        supportserviceinquiry = (LinearLayout) findViewById(R.id.dash_ll_supportserviceinquiry);
        termscondition = (LinearLayout) findViewById(R.id.dash_ll_termscondition);
        categorydata = (LinearLayout) findViewById(R.id.dash_ll_categorydata);
        literature = (LinearLayout) findViewById(R.id.dash_ll_addliterature);


        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(AdminDashboardActivity.this)){
                Intent i = new Intent(AdminDashboardActivity.this, CustomerlistActivity.class);
                startActivity(i);
                    finish();
            }else{
                Toast.makeText(AdminDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
            }
        });

       /* productmaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.isInternetConnected(AdminDashboardActivity.this)){
                Intent i = new Intent(AdminDashboardActivity.this, ProductsmasterActivity.class);
                startActivity(i);
                    finish();
            }else{
                Toast.makeText(AdminDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
            }
        });*/

        orderinquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(AdminDashboardActivity.this)){
                Intent i = new Intent(AdminDashboardActivity.this, OrderInquiryList.class);
                startActivity(i);
                    finish();
                }else{
                    Toast.makeText(AdminDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        supportserviceinquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.isInternetConnected(AdminDashboardActivity.this)){
                Intent i = new Intent(AdminDashboardActivity.this, SupportInquiryListActivity.class);
                startActivity(i);
                    finish();
            }else{
                Toast.makeText(AdminDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
            }
        });

        termscondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(AdminDashboardActivity.this)){
                Intent i = new Intent(AdminDashboardActivity.this, AddTermsConditionActivity.class);
                startActivity(i);
                    finish();
                }else{
                    Toast.makeText(AdminDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        categorydata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(AdminDashboardActivity.this)){
               Intent i = new Intent(AdminDashboardActivity.this, CategorylistActivity.class);
                startActivity(i);
                    finish();
            }else{
                Toast.makeText(AdminDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
            }
        });

        literature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(AdminDashboardActivity.this)){
                Intent i = new Intent(AdminDashboardActivity.this, AdminLiteratureList.class);
                startActivity(i);
                    finish();
                }else{
                    Toast.makeText(AdminDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(AdminDashboardActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(AdminDashboardActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AdminDashboardActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AdminDashboardActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AdminDashboardActivity.this,
                Manifest.permission.MODIFY_AUDIO_SETTINGS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AdminDashboardActivity.this,
                Manifest.permission.RECEIVE_BOOT_COMPLETED)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(AdminDashboardActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.RECEIVE_BOOT_COMPLETED},
                    MY_PERMISSIONS_ALL);
        }


//        getSinchServiceInterface().startClient(Utility.getCallUser(getApplicationContext()));

/*
        String unameadmin = "565790645";
        if (!getSinchServiceInterface().isStarted()) {
            System.out.println("/////////////////userregistered");
            getSinchServiceInterface().startClient(unameadmin);

        }
*/


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

                    Toast.makeText(AdminDashboardActivity.this, "Since you have not granted all permissions, you will not able to use some features of the app.", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater menuInflater = getSupportMenuInflater();
        getMenuInflater().inflate(R.menu.menu_new, menu);
        // return true;
        // MenuItemCompat.getActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_myprofile:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent i = new Intent(AdminDashboardActivity.this, MyProfileActivity.class);
                i.putExtra("value","admindash");
                startActivity(i);
                finish();
                return true;

            case R.id.action_logout:
                AlertDialog alertDialog = new AlertDialog.Builder(AdminDashboardActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure you want to Logout?");
                alertDialog.setCancelable(false);
                alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                new LogoutTask().execute();

                            }
                        });
                alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
        getSinchServiceInterface().startClient(Utility.getCallUser(getApplicationContext()));

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
        getSinchServiceInterface().startClient(Utility.getCallUser(getApplicationContext()));

    }


    private class LogoutTask extends AsyncTask<Void, Void, SoapObject> {
        private ProgressDialog progress;
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(AdminDashboardActivity.this);
            progress.setMessage("Logging out...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.LOGOUT);

            request.addProperty("token", Utility.getAuthToken(AdminDashboardActivity.this));

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

                    Utility.saveAuthToken(AdminDashboardActivity.this, "");
                    Utility.saveUserType(AdminDashboardActivity.this, "");
                    Utility.saveUserName(AdminDashboardActivity.this, "");
                    Utility.saveUserNamefordisplay(AdminDashboardActivity.this, "");

                    Toast.makeText(AdminDashboardActivity.this, "You have logged out successfully", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AdminDashboardActivity.this, LoginActivity.class);
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
       /* Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        finish();

    }
}
