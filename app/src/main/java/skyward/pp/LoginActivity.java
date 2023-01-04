package skyward.pp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sinch.android.rtc.SinchError;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import skyward.pp.util.Utility;

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener {

    int mFlipping = 0 ;
    Button login;
    TextView registerhere, forgot;
    EditText username, password;
    String countrycode,country,simCode="";
    private ArrayList<String> countrylist = new ArrayList<String>();
    private ArrayList<Integer> countrylistID = new ArrayList<Integer>();
    public int tempcountrylist =0;
    private ProgressDialog progress;
    ArrayList<String> orderstatusnoti;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);
        if (mFlipping == 0) {
            /** Start Flipping */
            flipper.startFlipping();
            mFlipping = 1;

        }

       /* Locale locale = Locale.getDefault();
        country = locale.getDisplayCountry();
        System.out.println("Country" + locale.getDisplayCountry());
*/

        /*try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            simCode = tm.getSimCountryIso();
            System.out.println("Country" + simCode);
        }catch(Exception e){
            e.printStackTrace();
        }*/
        /*    if (!Utility.isInternetConnected(LoginActivity.this)) {

            Toast.makeText(LoginActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            new FetchCountry().execute();
        }*/

        init();
        login = (Button) findViewById(R.id.btn_login);
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        registerhere = (TextView) findViewById(R.id.login_registerlink);
        forgot = (TextView) findViewById(R.id.login_forgotpassword);

        registerhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, NumberVerificationActivity.class);
                startActivity(i);
                finish();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void init() {
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isError = false;
                String errorMsg = "Invalid Username and Password";
                if (username.getText().toString().isEmpty()) {
                    isError = true;
                    errorMsg = "Please enter Username";
                }else if (!username.getText().toString().isEmpty() ) {

//                    if(Pattern.matches("[+0-9]+", username.getText().toString()))
//                    {
//                    }else{
//
//                        isError = true;
//                        errorMsg = "Enter Valid User Name";
//
//                    }
//                    }+
                        if(username.getText().toString().length() < 6 || username.getText().toString().length() > 16)
                        {
                            isError = true;
                            errorMsg = "Enter Valid Username";

                        }/*else if(!username.getText().toString().contains("+")) {
                            isError = true;
                            errorMsg = "Enter Valid UserName with Country code";
                        }*/


                } else if (password.getText().toString().trim().length() == 0) {
                    isError = true;
                    errorMsg = "Please enter Password";

                } else if (username.getText().toString().trim().length() == 0 && password.getText().toString().trim().length() == 0) {
                    isError = true;
                    errorMsg = "Please enter Username and Password";
                }
                if (isError) {
                    Toast.makeText(LoginActivity.this, errorMsg,
                            Toast.LENGTH_LONG).show();
                } else {
                    login();

                }
            }
        });
    }

    protected void login() {
        if (NetworkInformer.isNetworkConnected(getBaseContext())) {
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
            new LoginTask().execute();
        } else {
            Toast.makeText(getBaseContext(), "Check your Internet Connection",
                    Toast.LENGTH_LONG).show();

        }
    }

    private void goToWelcomeActivity() {


            Intent intent = new Intent(LoginActivity.this, LaunchActivity.class);
            intent.putExtra("fromlogin","fromlogin");
            startActivity(intent);
            finish();


    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
       // Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStarted() {

    }

    class FetchCountry extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_COUNTRY);

            if(countrylist.size() > 0){
                countrylist.clear();
                countrylistID.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_COUNTRY);

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
                            + Utility.GET_COUNTRY, mySoapEnvelop);


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
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            countrylist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());

                            countrylistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));



                        }


                    } else {
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(LoginActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LoginTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;

        String Username = username.getText().toString();
        String Password = password.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");

        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.USER_AUTHENTICATION);

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            myAndroidHttpTransport = null;

            request.addProperty("Username", Username);
            request.addProperty("Password", Password);


            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.USER_AUTHENTICATION, mySoapEnvelop);

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
                progress.dismiss();
                try {

                    if (!Utility.isInternetConnected(LoginActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));


                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                            boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                    "IsSucceed").toString());


                            Utility.saveAuthToken(LoginActivity.this, soapObject.getPrimitivePropertySafelyAsString("AuthenticationToken"));
                            Utility.saveUserName(LoginActivity.this, soapObject.getPrimitivePropertySafelyAsString("UserName"));
                            Utility.saveUserNamefordisplay(LoginActivity.this, soapObject.getPrimitivePropertySafelyAsString("Name"));

                            Utility.saveemailid(getApplicationContext(), soapObject.getPrimitivePropertySafelyAsString("EmailID"));
                            Utility.saveName(LoginActivity.this, soapObject.getPrimitivePropertySafelyAsString("Name"));
                            Utility.savePassword(LoginActivity.this, password.getText().toString());
                            Utility.saveUserType(LoginActivity.this, soapObject.getPrimitivePropertySafelyAsString("UserType"));


                            Utility.savenoticount(getApplicationContext(), 0);

                            String uname = soapObject.getPrimitivePropertySafelyAsString("Name").toString() +"_"+soapObject.getPrimitivePropertySafelyAsString("UserName").toString();

                            //new GetProfile().execute();
                            if (soapObject.getPrimitivePropertySafelyAsString("IsAdministrator").toString().equals("true")) {

                                Utility.saveIsAdministrator(LoginActivity.this, "true");
                                String unameadmin = "Admin";
                                if (!getSinchServiceInterface().isStarted()) {
                                    System.out.println("/////////////////userregistered");
                                    try {
                                        getSinchServiceInterface().startClient(unameadmin);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }

                                }
                                Utility.saveCallUser(getApplicationContext(),unameadmin);

                                Intent i = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                    startActivity(i);
                                    finish();

                            } else {

                                Utility.saveCallUser(getApplicationContext(),uname);

                                if (!getSinchServiceInterface().isStarted()) {
                                    System.out.println("///////////////////userregistered");
                                    try {
                                        getSinchServiceInterface().startClient(uname);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }

                                }
                                Intent alarm = new Intent(getApplicationContext(), OrderUpdateStatusReceiver.class);
                                boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
                                if (alarmRunning == false) {
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, 0);
                                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
                                }




                                Utility.saveIsAdministrator(LoginActivity.this, "false");
                                goToWelcomeActivity();
                            }

                            Intent alarm2 = new Intent(getApplicationContext(), SinchReceiver.class);
                            boolean alarmRunning2 = (PendingIntent.getBroadcast(getApplicationContext(), 0, alarm2, PendingIntent.FLAG_NO_CREATE) != null);
                            if (alarmRunning2 == false) {
                                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm2, 0);
                                AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager2.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 30000, pendingIntent2);
                            }


                        } else {


                            if (soapObject.getProperty("IsVerified").toString().equals("true")) {
                                Toast.makeText(getBaseContext(),
                                        soapObject.getProperty("ErrorMessage").toString(),
                                        Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(getBaseContext(),
                                        soapObject.getProperty("ErrorMessage").toString(),
                                        Toast.LENGTH_LONG).show();
                              //  if (TextUtils.isEmpty(Utility.getUserName(LoginActivity.this))) {
                                    Utility.saveUserName(LoginActivity.this, username.getText().toString());
                                //}
                                new ResendCode().execute();



                            }

                        }
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }else{
                progress.dismiss();
                Toast.makeText(LoginActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }



/*
    class getNotification extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_NOTIFICATION);

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;

            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));

            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.GET_NOTIFICATION, mySoapEnvelop);

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

            if (result != null) {
                progress.dismiss();
                try {

                    if (!Utility.isInternetConnected(getApplicationContext())) {
                        Toast.makeText(LoginActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    } else {

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
                            Utility.savenoticount(getApplicationContext(), count);

                            if (Utility.getIsAdministrator(getApplicationContext()).equalsIgnoreCase("true")) {

                                Intent i = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                startActivity(i);
                                finish();

                            } else {

                                goToWelcomeActivity();

                            }
                        } else {
                            Utility.savenoticount(getApplicationContext(), 0);
                            if (Utility.getIsAdministrator(getApplicationContext()).equalsIgnoreCase("true")) {


                                Intent i = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                startActivity(i);
                                finish();
                            } else {


                                goToWelcomeActivity();
                            }

                           */
/* Toast.makeText(getApplicationContext(),
                                    soapObject.getProperty("ErrorMessage").toString(),
                                    Toast.LENGTH_SHORT).show();*//*


                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.savenoticount(getApplicationContext(), 0);
                    if (Utility.getIsAdministrator(getApplicationContext()).equalsIgnoreCase("true")) {


                        Intent i = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else {


                        goToWelcomeActivity();
                    }
                }
            } else {
                progress.dismiss();
                Utility.savenoticount(getApplicationContext(), 0);
                if (Utility.getIsAdministrator(getApplicationContext()).equalsIgnoreCase("true")) {


                    Intent i = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                    startActivity(i);
                    finish();
                } else {


                    goToWelcomeActivity();
                }
            }

        }
    }
*/
    class ResendCode extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog prog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            prog = new ProgressDialog(LoginActivity.this);
            prog.setMessage("Please wait...");
            prog.setCancelable(false);
            prog.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.RESEND_VERIFICATIONCODE);

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            myAndroidHttpTransport = null;

            request.addProperty("userName", Utility.getUserName(LoginActivity.this));

            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.RESEND_VERIFICATIONCODE, mySoapEnvelop);

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
            prog.dismiss();
            if( result != null) {
                try {

                    if (!Utility.isInternetConnected(LoginActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                            boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                    "IsSucceed").toString());

                            Utility.saveVerificationCode(LoginActivity.this, soapObject.getPrimitivePropertySafelyAsString("VerificationCode"));

                            final Handler handler = new Handler();
                            final ProgressDialog p = new ProgressDialog(LoginActivity.this);
                            p.setMessage("Wait...");
                            p.setCancelable(false);
                            p.show();
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(new Runnable() {
                                        public void run()

                                        {

                                            p.dismiss();
                                            Intent intent = new Intent(LoginActivity.this, VerificationcodeActivity.class);
                                            startActivity(intent);
                                            finish();
                                            // YOUR Code
                                        }
                                    });
                                }
                            }, 10000);

                        } else {

                            Toast.makeText(getBaseContext(),
                                    soapObject.getProperty("ErrorMessage").toString(),
                                    Toast.LENGTH_LONG).show();

                        }
                    }

                } catch (NullPointerException e) {

                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e) {

                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(LoginActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
