package skyward.pp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skyward.pp.util.Utility;

public class ForgotPassword extends AppCompatActivity {

    EditText emailid, forgot_number;
    Button submit;
    TextView goback;
    int mFlipping =0;
    String countrycode,country,simCode="";
    private ArrayList<String> countrylist = new ArrayList<String>();
    private ArrayList<Integer> countrylistID = new ArrayList<Integer>();
    public int tempcountrylist =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);

        if (mFlipping == 0) {
            /** Start Flipping */
            flipper.startFlipping();
            mFlipping = 1;

        }

        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            simCode = tm.getSimCountryIso();
            System.out.println("Country" + simCode);
        }catch(Exception e){
            e.printStackTrace();
        }

        emailid = (EditText) findViewById(R.id.forgot_emailid);
        forgot_number = (EditText) findViewById(R.id.forgot_number);
        //forgot_numbercode = (EditText) findViewById(R.id.forgot_numbercode);
        submit = (Button) findViewById(R.id.forgot_btn_submit);
       // goback = (TextView) findViewById(R.id.forgot_back);

       // goback.setText("<< Back");


       /* if (!Utility.isInternetConnected(ForgotPassword.this)) {

            Toast.makeText(ForgotPassword.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            new FetchCountry().execute();
        }*/


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkInformer.isNetworkConnected(getBaseContext())) {
                    init();

                } else {
                    Toast.makeText(getBaseContext(), "Check your Internet Connection",
                            Toast.LENGTH_LONG).show();

                }




            }
        });

       /* goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(ForgotPassword.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ForgotPassword.this,LoginActivity.class);
        startActivity(i);
        finish();
    }


    public void init() {

//        String MobileNUmber=forgot_numbercode.getText().toString()+ forgot_number.getText().toString();
              String MobileNUmber=forgot_number.getText().toString();

        boolean isError = false;
        String errorMsg = "Invalid Data";
       /*if (forgot_numbercode.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter country Code";
        }*/
        if (forgot_number.getText().toString().isEmpty()) {
            isError = true;
            errorMsg = "Please enter Mobile No";
        }

        if (!MobileNUmber.isEmpty()) {

           /* if(Pattern.matches("[+0-9]+", MobileNUmber))
            {*/
                if(MobileNUmber.length() < 6 || MobileNUmber.length() > 16)
                {
                    isError = true;
                    errorMsg = "Enter Valid Mobile Number";

                }/*else if(!MobileNUmber.contains("+")) {
                    isError = true;
                    errorMsg = "Enter Valid Mobile Number with Country code";
                }*/
            /*}else{

                isError = true;
                errorMsg = "Enter Valid Mobile Number";

            }*/

        }

        if(emailid.getText().toString().equals("")) {


        }else{

            boolean chk=isEmailValid(emailid.getText().toString());
            if(chk==false)
            {
                isError = true;
                errorMsg = "Enter Valid Email Address";

            }

        }
        if (isError) {
            Toast.makeText(ForgotPassword.this, errorMsg,
                    Toast.LENGTH_LONG).show();
        }else {

            new ForgotVerificationCodeTask().execute();
        }

    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\D\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    class FetchCountry extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(ForgotPassword.this);
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

                           /*if (simCode.equalsIgnoreCase(soapResult.getPropertySafelyAsString("SimCode", "")
                                    .toString())) {

                                forgot_numbercode.setText(soapResult.getPropertySafelyAsString(
                                         "Code").toString());
                            }*/

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
                Toast.makeText(ForgotPassword.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ForgotVerificationCodeTask extends AsyncTask<Void, Void, SoapObject> {

        SoapObject result;
        private ProgressDialog progress;
        String email= emailid.getText().toString();
        String mobileno= forgot_number.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(ForgotPassword.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.FORGOT_PASSVERIFICATIONCODE);

            request.addProperty("userName",mobileno);
            request.addProperty("EmailID", email);


            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            myAndroidHttpTransport = null;


            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.FORGOT_PASSVERIFICATIONCODE, mySoapEnvelop);

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

                    if (!Utility.isInternetConnected(ForgotPassword.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

                            //Utility.savePassword(ForgotPassword.this, password.getText().toString());
                            Utility.saveUserName(ForgotPassword.this, mobileno);
                            Utility.saveUserNamefordisplay(ForgotPassword.this, soapObject.getPrimitivePropertySafelyAsString("Name"));

                            Utility.saveemailid(ForgotPassword.this, emailid.getText().toString());
                            Utility.saveVerificationCode(ForgotPassword.this, soapObject.getPrimitivePropertySafelyAsString("VerificationCode"));

                            final ProgressDialog p = new ProgressDialog(ForgotPassword.this);
                            p.setMessage("Please wait while we are reading your verification code. You'll redirected to verification page...");
                            p.setCancelable(false);
                            p.show();
                            final Handler handler = new Handler();
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(new Runnable() {
                                        public void run()

                                        {

                                            p.dismiss();
                                            Intent intent = new Intent(ForgotPassword.this, ForgotPasswordverificationActivity.class);
                                            startActivity(intent);
                                            finish();

                                            // YOUR Code
                                        }
                                    });
                                }
                            }, 20000);


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

                Toast.makeText(ForgotPassword.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }



}
