package skyward.pp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
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
import java.util.ArrayList;

import skyward.pp.ecommerce.EcommDashboardActivity;
import skyward.pp.util.Utility;

public class NotificationDetailsActivity extends AppCompatActivity {

    TextView text, title;
    WebView webView;
    String ID, ID1, DetailedDescription, ShortTitle, imagepath;
    String navigatetext="",notiid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        setTitle("Notification Details");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Notification Details");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigatetext.equalsIgnoreCase("fromss")) {
                    Intent i = new Intent(getApplicationContext(), NotificationActivity.class);
                    i.putExtra("fromss", navigatetext);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), NotificationActivity.class);
                    i.putExtra("fromecomm", navigatetext);
                    startActivity(i);
                    finish();
                }
            }
        });


        System.out.println("Inside details");
        webView = (WebView) findViewById(R.id.text);
        title = (TextView) findViewById(R.id.title);


        ShortTitle = getIntent().getStringExtra("title");
        notiid = getIntent().getStringExtra("id");
        title.setText(ShortTitle);
        if (Utility.isInternetConnected(getApplicationContext())) {
            getNotification task = new getNotification();
            task.execute();
        }else{
            Toast.makeText(getApplicationContext(),"Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        navigatetext = getIntent().getStringExtra("navigatetext");

        //webView.loadData(DetailedDescription, "", "");


    }

    class getNotification extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(NotificationDetailsActivity.this);
            progress.setMessage("Please Wait ...");
            progress.setCancelable(false);
            progress.show();
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
                        Toast.makeText(NotificationDetailsActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
                            System.out.println("Count is : " + count);

                            for (int i = 0; i < count; i++) {
                                SoapObject soapResult = null;
                                soapResult = (SoapObject) result4.getProperty(i);


                                String ApproverRemarks = soapResult.getPrimitivePropertySafelyAsString("Remarks").toString();

                                String ID = soapResult.getPrimitivePropertySafelyAsString("ID").toString();

                                if(notiid.equalsIgnoreCase(ID)){
                                    DetailedDescription =  soapResult.getPrimitivePropertySafelyAsString("Remarks").toString();
                                }

                            }

                            webView.loadData(DetailedDescription, "text/html", "utf-8");

                            // at_search.setThreshold(3);



                        } else {
                            Utility.savenoticount(getApplicationContext(), 0);

                           /* Toast.makeText(getApplicationContext(),
                                    soapObject.getProperty("ErrorMessage").toString(),
                                    Toast.LENGTH_SHORT).show();*/
                            Toast.makeText(NotificationDetailsActivity.this,"No Notifications found !", Toast.LENGTH_SHORT).show();

                        }


                    }

                } catch (NullPointerException e) {

                    e.printStackTrace();

                } catch (ArrayIndexOutOfBoundsException e) {

                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {
                progress.dismiss();

                Toast.makeText(NotificationDetailsActivity.this,"Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(navigatetext.equalsIgnoreCase("fromss")){
            Intent i = new Intent(getApplicationContext(),NotificationActivity.class);
            i.putExtra("fromss",navigatetext);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(getApplicationContext(),NotificationActivity.class);
            i.putExtra("fromecomm",navigatetext);
            startActivity(i);
            finish();
        }

    }
}
