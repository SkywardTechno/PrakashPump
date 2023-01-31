package skyward.pp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skyward.pp.util.Utility;

public class SupportInquiryActivity extends AppCompatActivity {

    Button submit;
    String number, simCode;
    TextView choseservice;
    EditText sinquiry_name, sinquiry_mobileno,sinquiry_emailid, sinquiry_inquirydetails,sinquiry_location;
    public int serviceTypeID=0;
    String stype="";
    ArrayList<String> servicenames;
    ArrayList<String> serviceids;
    String countrycode,country;
    private ArrayList<String> countrylist = new ArrayList<String>();
    private ArrayList<Integer> countrylistID = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supportinquiry);

        setTitle("Inquiry");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Inquiry");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SupportInquiryActivity.this, SupportServiceActivity.class);
                i.putExtra("one", "one");
                i.putExtra("stype", stype);
                startActivity(i);
                finish();
            }
        });


        if (!Utility.isInternetConnected(SupportInquiryActivity.this)) {

            Toast.makeText(SupportInquiryActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            new FetchCountry().execute();
        }


        submit = (Button) findViewById(R.id.sinquiry_btn_submit);
        choseservice = (TextView) findViewById(R.id.sinquiry_choseservice);
        sinquiry_name = (EditText) findViewById(R.id.sinquiry_name);
        sinquiry_mobileno = (EditText) findViewById(R.id.sinquiry_mobileno);
        sinquiry_emailid = (EditText) findViewById(R.id.sinquiry_emailid);
        sinquiry_inquirydetails = (EditText) findViewById(R.id.sinquiry_inquirydetails);
       // sinquiry_location= (EditText) findViewById(R.id.sinquiry_location);
        stype=getIntent().getStringExtra("servicetypeID");
        choseservice.setText("You have chosen "+stype +" service");

        servicenames=new ArrayList<>();
        serviceids=new ArrayList<>();

        if (NetworkInformer.isNetworkConnected(getBaseContext())) {

            new getServiceTypeTask().execute();

        } else {

            Toast.makeText(getBaseContext(), "Check your Internet Connection",
                    Toast.LENGTH_LONG).show();

        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                init();


            }
        });

        if(TextUtils.isEmpty(Utility.getName(SupportInquiryActivity.this))){
            sinquiry_name.setEnabled(true);
        }else{

            sinquiry_name.setText(Utility.getName(SupportInquiryActivity.this));


        }
        sinquiry_emailid.setText(Utility.getemailid(SupportInquiryActivity.this));

        sinquiry_mobileno.setText(Utility.getUserName(SupportInquiryActivity.this));
       /* if(TextUtils.isEmpty(Utility.getUserName(SupportInquiryActivity.this))){

            sinquiry_mobileno.setEnabled(true);
            try {
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                simCode = tm.getSimCountryIso();
                System.out.println("Country" + simCode);
            }catch(Exception e){
                e.printStackTrace();
            }

        }else{*/







    }


    public void init() {

        boolean isError = false;
        String errorMsg = "Invalid Data";
        if (sinquiry_name.getText().toString().trim().length() == 0 || sinquiry_inquirydetails.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter Mandatory fields";
        }

        if (sinquiry_mobileno.getText().toString().equals("")) {
            isError = true;
            errorMsg = "Please enter Mobile Number";
        }else{

                if(sinquiry_mobileno.getText().toString().length() < 6 || sinquiry_mobileno.getText().toString().length() > 16)
                {
                    isError = true;
                    errorMsg = "Enter Valid Mobile Number";

                }/*else if(!sinquiry_mobileno.getText().toString().contains("+")) {
                    isError = true;
                    errorMsg = "Enter Valid Mobile Number with Country code";
                }*/


        }
            if (sinquiry_emailid.getText().toString().equals("")) {
                isError = true;
                errorMsg = "Please enter emailid";
            }else {

                boolean chk = isEmailValid(sinquiry_emailid.getText().toString());
                if(chk==false)
                {
                    isError = true;
                    errorMsg = "Enter Valid Email Address";

                }


            }

      /*  if (sinquiry_location.getText().toString().equals("")) {
            isError = true;
            errorMsg = "Please enter Location";
        }*/



        if (isError) {
            Toast.makeText(SupportInquiryActivity.this, errorMsg,
                    Toast.LENGTH_LONG).show();
        }else{

            if (NetworkInformer.isNetworkConnected(getBaseContext())) {
                new insertInquiryTask().execute();
            } else {
                Toast.makeText(getBaseContext(), "Check your Internet Connection",
                        Toast.LENGTH_LONG).show();

            }

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
            progress = new ProgressDialog(SupportInquiryActivity.this);
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

                            countrylistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString("ID").toString()));



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

                Toast.makeText(SupportInquiryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class getServiceTypeTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(SupportInquiryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.SERVICE_TYPE);

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
                            + Utility.SERVICE_TYPE, mySoapEnvelop);

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

                    if (!Utility.isInternetConnected(SupportInquiryActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                            boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                    "IsSucceed").toString());

                            SoapObject soapObject1 = (SoapObject) soapObject.getProperty("Data");
                            SoapObject soapObject2 = (SoapObject) soapObject1.getProperty(1);
                            SoapObject soapObject3 = (SoapObject) soapObject2.getProperty(0);

                            servicenames = new ArrayList<>();
                            serviceids = new ArrayList<>();
                            int count = soapObject3.getPropertyCount();
                            for (int i = 0; i < count; i++) {
                                SoapObject soapObject4 = (SoapObject) soapObject3.getProperty(i);
                                String name = soapObject4.getPropertySafelyAsString("Name", "");
                                String id = soapObject4.getPropertySafelyAsString("ID", "0");
                                servicenames.add(name);
                                serviceids.add(id);
                            }

                            int flag = 0;
                            for (int i = 0; i < servicenames.size(); i++) {
                                if (stype.equalsIgnoreCase(servicenames.get(i))) {
                                    flag = 1;
                                    String id = serviceids.get(i);
                                    serviceTypeID = Integer.valueOf(id);
                                    break;
                                }
                            }
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

                Toast.makeText(SupportInquiryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class insertInquiryTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;
        String name=sinquiry_name.getText().toString();
        String mobileno=sinquiry_mobileno.getText().toString();
        String email_ID=sinquiry_emailid.getText().toString();
        String remarks=sinquiry_inquirydetails.getText().toString();
       // String location=sinquiry_location.getText().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(SupportInquiryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.INSERT_INQUIRY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("name",name);
            request.addProperty("mobileNo",mobileno);
            request.addProperty("emailID",email_ID);
            request.addProperty("remarks",remarks);
            request.addProperty("serviceTypeID",serviceTypeID);
            request.addProperty("Location","");
            request.addProperty("ServiceName",stype);


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
                            + Utility.INSERT_INQUIRY, mySoapEnvelop);

                } catch (XmlPullParserException | SocketTimeoutException | SocketException e) {
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    // System.out.println("XmlPullParserException 0");
                } // System.out.println("SocketTimeoutException 1");
                // System.out.println("SocketException  2");
                catch (IOException e) {
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

                    if (!Utility.isInternetConnected(SupportInquiryActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

                            Toast.makeText(getBaseContext(),
                                    "Inquiry Sent Successfully",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(SupportInquiryActivity.this, SupportServiceActivity.class);
                            i.putExtra("one", "one");
                            i.putExtra("stype", stype);
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

                Toast.makeText(SupportInquiryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       Intent i = new Intent(SupportInquiryActivity.this, SupportServiceActivity.class);
        i.putExtra("one", "one");
        i.putExtra("stype", stype);
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
