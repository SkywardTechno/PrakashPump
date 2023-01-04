package skyward.pp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import skyward.pp.util.Utility;

public class SupportInquiryDetailsActivity extends AppCompatActivity {

    String name,mobileno,emailid,remarks,service,inquirycode;
    int serviceID;
    TextView txt_name,txt_mobileno,txt_emailid, txt_service;
    TextView txt_inquirydetails,txt_inquirycode;
    Button reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supportinquiry_details);

        setTitle("Service Inquiry Details");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Service Inquiry Details");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SupportInquiryDetailsActivity.this, SupportInquiryListActivity.class);
                startActivity(i);
                finish();
            }
        });

        txt_name= (TextView) findViewById(R.id.sidetails_name);
        txt_mobileno= (TextView) findViewById(R.id.sidetails_mobileno);
        txt_emailid= (TextView) findViewById(R.id.sidetails_emailid);
        txt_service= (TextView) findViewById(R.id.sidetails_service);
        txt_inquirydetails= (TextView) findViewById(R.id.sidetails_supportinquirydetails);
        txt_inquirycode= (TextView) findViewById(R.id.sidetails_inquirycode);
        reply= (Button) findViewById(R.id.sidetails_btn_reply);
        Intent i = getIntent();
        serviceID=Integer.valueOf(i.getStringExtra("ID"));

        if (!Utility.isInternetConnected(SupportInquiryDetailsActivity.this)) {

            Toast.makeText(SupportInquiryDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            new FetchSupportInquiryDetailstask().execute();

        }


        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SupportInquiryDetailsActivity.this, ComposeMailSupportInquiry.class);
                i.putExtra("ID",String.valueOf(serviceID));
                i.putExtra("emailid",emailid);
                startActivity(i);
                finish();
            }
        });
    }
    class FetchSupportInquiryDetailstask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        //private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(CustomerlistActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_SERVICEINQUIRYBYID);


            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ID",serviceID);


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
                            + Utility.GET_SERVICEINQUIRYBYID, mySoapEnvelop);


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
if(result!= null) {

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
            int count = result4.getPropertyCount();
            System.out.println("Count is : " + count);
            //customer.clear();
            //customerID.clear();


            name = result5.getPrimitivePropertySafelyAsString("Name")
                    .toString();
            mobileno = result5.getPrimitivePropertySafelyAsString("MobileNo").toString();
            emailid = result5.getPrimitivePropertySafelyAsString("EmailID").toString();

            service = result5.getPrimitivePropertySafelyAsString("ServiceType").toString();
            inquirycode = result5.getPrimitivePropertySafelyAsString("InquiryCode").toString();
            remarks = result5.getPrimitivePropertySafelyAsString("Remarks").toString();

            txt_emailid.setText(emailid);
            txt_mobileno.setText(mobileno);
            txt_name.setText(name);
            txt_inquirydetails.setText(remarks);
            txt_inquirydetails.setMovementMethod(new ScrollingMovementMethod());
            txt_service.setText(service);
            txt_inquirycode.setText(inquirycode);


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

    Toast.makeText(SupportInquiryDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
}
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SupportInquiryDetailsActivity.this, SupportInquiryListActivity.class);
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
            Intent i = new Intent(getApplicationContext(),AdminDashboardActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
