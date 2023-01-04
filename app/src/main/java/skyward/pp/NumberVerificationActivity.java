package skyward.pp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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

public class NumberVerificationActivity extends AppCompatActivity {

    EditText countryname,countrycode,phonenumber, name,password, confirmpassword,email;
    Button create;
    String country , simCode = "";
    TextView login, matched, mobilenoguide;
    private ArrayList<String> countrylist = new ArrayList<String>();
    private ArrayList<Integer> countrylistID = new ArrayList<Integer>();
    private ArrayList<String> countrycodelist = new ArrayList<String>();
    private ArrayList<Integer> countrymobdigitslist = new ArrayList<Integer>();
    public int tempcountrylist =0;
    public int mobiledigits =0;
    int mFlipping = 0 ;
    public static final int MY_PERMISSIONS_ALL=1;
    public static final int MY_PERMISSIONS_READ_SMS=4;
    public static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE=2;
    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE=3;
    String countrycodepass="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numberverification);


        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);

        if (mFlipping == 0) {
            /** Start Flipping */
            flipper.startFlipping();
            mFlipping = 1;

        }


        email= (EditText) findViewById(R.id.main_emailid);
        countryname = (EditText) findViewById(R.id.main_country);
        login = (TextView) findViewById(R.id.main_loginlink);
        phonenumber = (EditText) findViewById(R.id.main_mobileno);
        name = (EditText) findViewById(R.id.main_name);
        password = (EditText) findViewById(R.id.main_password);
        confirmpassword = (EditText) findViewById(R.id.main_confirmpassword);
        create = (Button) findViewById(R.id.main_btn_create);
        matched = (TextView) findViewById(R.id.main_matched);
        mobilenoguide = (TextView) findViewById(R.id.mobilenoguide);

        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            simCode = tm.getSimCountryIso();
            System.out.println("Country" + simCode);
        }catch(Exception e){
            e.printStackTrace();
        }

        if (ContextCompat.checkSelfPermission(NumberVerificationActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(NumberVerificationActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(NumberVerificationActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(NumberVerificationActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_ALL);
        }
       /* if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
        {

            String[] perms = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.READ_SMS" ,"android.permission.INTERNET"};

            int permsRequestCode = 200;

            requestPermissions(perms, permsRequestCode);
           *//* TelephonyManager tm2 = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            simCode = tm2.getSimCountryIso();
            System.out.println("Country" + simCode);*//*

        }*/
     /*   if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Toast.makeText(NumberVerificationActivity.this, "Please give necessary permissions as requested", Toast.LENGTH_SHORT).show();
            return;
        }else{
            Locale locale = Locale.getDefault();
            country = locale.getDisplayCountry();
            System.out.println("Country" + locale.getDisplayCountry());
        }*/



        if (!Utility.isInternetConnected(NumberVerificationActivity.this)) {

            Toast.makeText(NumberVerificationActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    finish();
                }
            }, 2000) ;
        }else {
            new FetchCountry().execute();
        }

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(NumberVerificationActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(NumberVerificationActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(NumberVerificationActivity.this,
                        Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(NumberVerificationActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_SMS},
                            MY_PERMISSIONS_ALL);
                }else {
                    init();
                }
            }
        });

        confirmpassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String strPass1 = password.getText().toString();
                String strPass2 = confirmpassword.getText().toString();
                if (strPass1.equals(strPass2)) {
                    matched.setVisibility(View.VISIBLE);
                    matched.setText("Password Matched");
                    matched.setTextColor(Color.GREEN);
                } else {
                    matched.setVisibility(View.VISIBLE);
                    matched.setText("Passwords Do not Match");
                    matched.setTextColor(Color.RED);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strPass1 = password.getText().toString();
                String strPass2 = confirmpassword.getText().toString();
                if (strPass1.equals(strPass2)) {
                    matched.setVisibility(View.VISIBLE);
                    matched.setText("Password Matched");
                    matched.setTextColor(Color.GREEN);
                } else {
                    matched.setVisibility(View.VISIBLE);
                    matched.setText("Passwords Do not Match");
                    matched.setTextColor(Color.RED);
                }
            }
        });

        countryname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] countrys = countrylist.toArray(new String[countrylist
                        .size()]);
                alertCountry(countrys, v, countryname);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NumberVerificationActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
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
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    init();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(NumberVerificationActivity.this, "Please grant all the permissions to use app!", Toast.LENGTH_SHORT).show();

                    if (ContextCompat.checkSelfPermission(NumberVerificationActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(NumberVerificationActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(NumberVerificationActivity.this,
                            Manifest.permission.READ_SMS)
                            != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions(NumberVerificationActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_SMS},
                                MY_PERMISSIONS_ALL);
                    }
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }



        }
        // other 'case' lines to check for other
        // permissions this app might request

    }

    private void alertCountry(final String[] visitType, View v,
                              final TextView tvVisitType2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                NumberVerificationActivity.this);
        // Source of the data in the DIalog

        // Set the dialog title
        builder.setTitle("Select Country")
                // Specify the list array, the items to be selected by
                // default
                // (null for none),
                // and the listener through which to receive callbacks
                // when
                // items are selected
                .setItems(visitType,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                for (int i = 0; i < countrylist.size(); i++) {
                                    if (countrylist.get(i).toString()
                                            .equals(visitType[which])) {
                                        tempcountrylist = countrylistID.get(i);
                                        simCode = countrycodelist.get(i);
                                        mobiledigits = countrymobdigitslist.get(i);
                                    }

                                }
                                phonenumber.setText("");
                                phonenumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mobiledigits)});

                                tvVisitType2.setText(visitType[which]);
                                dialog.dismiss();


                                new FetchCountry().execute();

                            }
                        });

        // Set the action buttons
        builder.show();

    }
    public void init() {

        boolean isError = false;
        String errorMsg = "Invalid Data";
        if (countryname.getText().toString().equals("")) {
            isError = true;
            errorMsg = "Please select your Country";
        }  else if (name.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter name";
        } else if (password.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter password";
        }else if (confirmpassword.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter confirm password";
        }else if (phonenumber.getText().toString().isEmpty()) {
            isError = true;
            errorMsg = "Please enter Mobile number";
        }

        else if (email.getText().toString().isEmpty()) {
            isError = true;
            errorMsg = "Please enter Email Address";
        }
        else if (!email.getText().toString().isEmpty()) {

            boolean chk = isEmailValid(email.getText().toString());
            if (chk == false) {
                isError = true;
                errorMsg = "Enter Valid Email Address";

            }

        }
        else if(matched.getText().toString().equals("Passwords Do not Match")){
            isError = true;
            errorMsg = "Passwords Do Not Match";
        }else if (!phonenumber.getText().toString().isEmpty() ) {

           /* if(Pattern.matches("[+0-9]+", phonenumber.getText().toString()))
            {*/
                if(phonenumber.getText().toString().length() < 6 || phonenumber.getText().toString().length() > 16)
                {
                    isError = true;
                    errorMsg = "Enter Valid Mobile Number";

                }else if(!phonenumber.getText().toString().contains("+")) {

                    isError = true;
                    errorMsg = "Enter Valid Mobile Number with Country code";
                }
           /* }else{

                isError = true;
                errorMsg = "Enter Valid Mobile Number";

            }*/

        }


        if (isError) {
            Toast.makeText(NumberVerificationActivity.this, errorMsg,
                    Toast.LENGTH_LONG).show();
        }else{

            new LoginTask().execute();}

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
        progress = new ProgressDialog(NumberVerificationActivity.this);
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
            countrymobdigitslist.clear();
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
                        countrylist.add(soapResult.getPropertySafelyAsString("Name", "").toString());
                        countrycodelist.add(soapResult.getPropertySafelyAsString(
                                "simcode").toString());

                        countrylistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));

                        if(soapResult.hasProperty("MobDigits")){
                            countrymobdigitslist.add(Integer.parseInt(soapResult.getPropertySafelyAsString("MobDigits").toString()));

                        }else{
                            countrymobdigitslist.add(0);

                        }
                       /* countrymobdigitslist.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "MobDigits").toString()));*/

                        if (simCode.equalsIgnoreCase(soapResult.getPropertySafelyAsString("simcode", "")
                                .toString())) {

                            countryname.setText(soapResult.getPropertySafelyAsString("Name").toString());
                             countrycodepass=soapResult.getPropertySafelyAsString(
                                    "Code").toString();
                           if(countrycodepass.contains("+"))
                           {
                               countrycodepass=countrycodepass.replace("+","");
                           }
                            tempcountrylist = Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString());
                            mobiledigits = Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "MobDigits").toString());
                            phonenumber.setText("");
                            phonenumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mobiledigits)});
                        }

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

            Toast.makeText(NumberVerificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}

class LoginTask extends AsyncTask<Void, Void, SoapObject> {
    SoapObject result;
    private ProgressDialog progress;
    String Username = name.getText().toString();
    String Password = password.getText().toString();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //etClientName.setText("");
        progress = new ProgressDialog(NumberVerificationActivity.this);
        progress.setMessage("Registering.\n" +
                "Please wait...");
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected SoapObject doInBackground(Void... arg0) {

        SoapSerializationEnvelope mySoapEnvelop = null;
        HttpTransportSE myAndroidHttpTransport;
        SoapObject request = new SoapObject(Utility.NAMESPACE,
                Utility.USER_REGISTERATION);
        mySoapEnvelop = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        mySoapEnvelop.dotNet = true;
        myAndroidHttpTransport = null;
          request.addProperty("mobileNo", phonenumber.getText().toString());
        request.addProperty("password", Password);
        request.addProperty("name", Username);
        request.addProperty("country", tempcountrylist);
        request.addProperty("City", "");
        request.addProperty("area", "");
        request.addProperty("emailID",email.getText().toString());
        request.addProperty("userTypeID", "1");
        request.addProperty("CountryCode",countrycodepass);

        System.out.println(Utility.URL);
        System.out.println(request);
        mySoapEnvelop.setOutputSoapObject(request);

        try {
            try {
                myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                myAndroidHttpTransport.call(Utility.SOAP_ACTION
                        + Utility.USER_REGISTERATION, mySoapEnvelop);

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

                if (!Utility.isInternetConnected(NumberVerificationActivity.this)) {
                    Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                } else {

                    SoapObject soapObject = (SoapObject) result.getProperty(0);
                    System.out.println(soapObject.getProperty("IsSucceed"));
                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                        boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                "IsSucceed").toString());

                        Utility.savePassword(NumberVerificationActivity.this, password.getText().toString());
                        Utility.saveUserName(NumberVerificationActivity.this, phonenumber.getText().toString());
                       // Utility.saveName(NumberVerificationActivity.this, soapObject.getPrimitivePropertySafelyAsString("Name"));
                        Utility.saveVerificationCode(NumberVerificationActivity.this, soapObject.getPrimitivePropertySafelyAsString("VerificationCode"));
                        //Utility.saveVerificationCode(NumberVerificationActivity.this, soapObject.getPrimitivePropertySafelyAsString("IsAdministrator"));
                        Utility.saveemailid(getApplicationContext(), email.getText().toString());

                        Utility.saveemailid(getApplicationContext(), soapObject.getPrimitivePropertySafelyAsString("EmailID"));

                        if(Utility.getVerificationCode(getApplicationContext()).equalsIgnoreCase("1001")){

                            Verify task = new Verify();
                            task.execute();

                        }else{

                            try {

                                final Handler handler = new Handler();
                                final ProgressDialog p = new ProgressDialog(NumberVerificationActivity.this);
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
                                                Intent intent = new Intent(NumberVerificationActivity.this, VerificationcodeActivity.class);
                                                startActivity(intent);
                                                finish();

                                                // YOUR Code
                                            }
                                        });
                                    }
                                }, 20000);

                            }catch(Exception e){
                                e.printStackTrace();
                            }

                        }
                        // System.out.println("hvksduhgkfdjbnk"+Utility.getUserName(NumberVerificationActivity.this));

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

            Toast.makeText(NumberVerificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

}

    class Verify extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(NumberVerificationActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.USER_VERIFICATION);

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            myAndroidHttpTransport = null;

            request.addProperty("userName", Utility.getUserName(NumberVerificationActivity.this));
            request.addProperty("verificationCode", Utility.getVerificationCode(getApplicationContext()));

            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.USER_VERIFICATION, mySoapEnvelop);

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

                    if (!Utility.isInternetConnected(NumberVerificationActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                            boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                    "IsSucceed").toString());

                            Utility.saveAuthToken(NumberVerificationActivity.this, soapObject.getPrimitivePropertySafelyAsString("AuthenticationToken"));
                            Utility.saveUserName(NumberVerificationActivity.this, soapObject.getPrimitivePropertySafelyAsString("UserName"));
                            Utility.saveName(NumberVerificationActivity.this, soapObject.getPrimitivePropertySafelyAsString("Name"));
                            Utility.saveIsAdministrator(getApplicationContext(), "false");

                            Utility.savenoticount(getApplicationContext(), 0);

                            Intent i = new Intent(NumberVerificationActivity.this, LaunchActivity.class);
                            i.putExtra("fromlogin","fromlogin");
                            startActivity(i);
                            finish();
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

                Toast.makeText(NumberVerificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
