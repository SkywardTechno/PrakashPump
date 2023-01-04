package skyward.pp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skyward.pp.util.Utility;

public class PumpSelectionInquiryActivity extends AppCompatActivity {

    EditText pinquiry_name, pinquiry_mobileno,pinquiry_emailid, pinquiry_inquirydetails;
    Button submit;
    int ProductID;
    String countrycode,country="" +
            "", Text="", Required="",simCode="",proname="",modelno="";
    private ArrayList<String> countrylist = new ArrayList<String>();
    private ArrayList<Integer> countrylistID = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pumpselection_inquiry);

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

                Intent i = new Intent(PumpSelectionInquiryActivity.this, PumpselectionActivity.class);
                startActivity(i);
                finish();
            }
        });



        if (!Utility.isInternetConnected(PumpSelectionInquiryActivity.this)) {

            Toast.makeText(PumpSelectionInquiryActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            new FetchCountry().execute();
        }

        Intent i = getIntent();
        ProductID=getIntent().getIntExtra("productID", 0);
        Text = i.getStringExtra("TcDetails");
        Required = i.getStringExtra("Required");
        proname = i.getStringExtra("productname");
        modelno = i.getStringExtra("modelno");

        submit = (Button) findViewById(R.id.pinquiry_btn_submit);
        pinquiry_name = (EditText) findViewById(R.id.pinquiry_name);
        pinquiry_mobileno = (EditText) findViewById(R.id.pinquiry_mobileno);
        pinquiry_emailid = (EditText) findViewById(R.id.pinquiry_emailid);
        pinquiry_inquirydetails = (EditText) findViewById(R.id.pinquiry_inquirydetails);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                init();


            }
        });

        if(TextUtils.isEmpty(Utility.getName(PumpSelectionInquiryActivity.this))){

            pinquiry_name.setEnabled(true);

        }else{

            pinquiry_name.setText(Utility.getName(PumpSelectionInquiryActivity.this));


        }


        String tmp=Utility.getemailid(getApplicationContext());
        pinquiry_emailid.setText(Utility.getemailid(PumpSelectionInquiryActivity.this));
        pinquiry_mobileno.setText(Utility.getUserName(PumpSelectionInquiryActivity.this));

      /*  if(TextUtils.isEmpty(Utility.getUserName(PumpSelectionInquiryActivity.this))){

            pinquiry_mobileno.setEnabled(true);
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
        if (pinquiry_name.getText().toString().trim().length() == 0 || pinquiry_inquirydetails.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter Mandatory fields";
        }

        if (pinquiry_mobileno.getText().toString().isEmpty() ) {

            isError = true;
            errorMsg = "Please enter Mobile No";

        }else{


                if(pinquiry_mobileno.getText().toString().length() < 6 || pinquiry_mobileno.getText().toString().length() > 16)
                {
                    isError = true;
                    errorMsg = "Enter Valid Mobile Number";

                }/*else if(!pinquiry_mobileno.getText().toString().contains("+")) {
                    isError = true;
                    errorMsg = "Enter Valid Mobile Number with Country code";
                }*/


        }


        if(pinquiry_emailid.getText().toString().equals("")) {

            isError = true;
            errorMsg = "Enter Email Address";
        }else{

            boolean chk=isEmailValid(pinquiry_emailid.getText().toString());
            if(chk==false)
            {
                isError = true;
                errorMsg = "Enter Valid Email Address";

            }


        }
        if (isError) {
            Toast.makeText(PumpSelectionInquiryActivity.this, errorMsg,
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
            progress = new ProgressDialog(PumpSelectionInquiryActivity.this);
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

                           /* if (simCode.equalsIgnoreCase(soapResult.getPropertySafelyAsString("SimCode", "")
                                    .toString())) {

                                pinquiry_mobileno.setText(soapResult.getPropertySafelyAsString(
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

                Toast.makeText(PumpSelectionInquiryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class insertInquiryTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;
        String name=pinquiry_name.getText().toString();
        String mobileno=pinquiry_mobileno.getText().toString();
        String email_ID=pinquiry_emailid.getText().toString();
        String remarks=pinquiry_inquirydetails.getText().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(PumpSelectionInquiryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.INSERT_ORDERINQUIRY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("UserName",Utility.getName(getApplicationContext()));
            request.addProperty("Productname",proname);
            request.addProperty("ModelNum",modelno);
            request.addProperty("productID",ProductID);
            request.addProperty("mobileNo",mobileno);
            request.addProperty("emailID",email_ID);
            request.addProperty("technicalServiceRequired",Required);
            request.addProperty("serviceDetail",Text);
            request.addProperty("inquiryDetail",remarks);

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
                            + Utility.INSERT_ORDERINQUIRY, mySoapEnvelop);

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

                    if (!Utility.isInternetConnected(PumpSelectionInquiryActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

                            Toast.makeText(getBaseContext(),
                                    "Inquiry Sent Successfully",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(PumpSelectionInquiryActivity.this, PumpselectionActivity.class);
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

                Toast.makeText(PumpSelectionInquiryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PumpSelectionInquiryActivity.this, ViewProductActivity.class);
       i.putExtra("productID",ProductID);
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
