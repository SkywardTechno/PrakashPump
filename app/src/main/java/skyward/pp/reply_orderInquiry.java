package skyward.pp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
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

public class reply_orderInquiry extends AppCompatActivity {
    TextView mail_to,mail_from,mail_cc,mail_bcc,mail_subject,mail_compose,mail_attach;
    String strTo,strCc,strBcc,strSub,strAttach,strCompose;
    int orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_order_inquiry);

        mail_to= (TextView) findViewById(R.id.rOIMail_to);
       mail_cc= (TextView) findViewById(R.id.rOIMail_cc);
        mail_bcc= (TextView) findViewById(R.id.rOIMail_bcc);
        mail_subject= (TextView) findViewById(R.id.rOIMail_subject);
        mail_compose= (TextView) findViewById(R.id.rOIMail_compose);
        mail_attach= (TextView) findViewById(R.id.rOIMail_attachments);

        setTitle("Order Inquiry");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Order Inquiry");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(reply_orderInquiry.this, OrderInquiryList.class);
                startActivity(i);
                finish();
            }
        });

        Intent i = getIntent();
        orderId=i.getIntExtra("RID",0);

        if (!Utility.isInternetConnected(reply_orderInquiry.this)) {

            Toast.makeText(reply_orderInquiry.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            new FetchOrderDetailstask().execute();

        }

    }

    class FetchOrderDetailstask extends AsyncTask<Void, Void, SoapObject> {
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
                    Utility.GET_ORDERREPLY);


            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("id",orderId);




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
                            + Utility.GET_ORDERREPLY, mySoapEnvelop);


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
                        int count = result4.getPropertyCount();
                        System.out.println("Count is : " + count);
                        //customer.clear();
                        //customerID.clear();


                        strTo = result5.getPrimitivePropertySafelyAsString("ToMailID")
                                .toString();
                        strCc = result5.getPrimitivePropertySafelyAsString("CC").toString();
                        strBcc = result5.getPrimitivePropertySafelyAsString("BCC").toString();

                        strAttach = result5.getPrimitivePropertySafelyAsString("FileName").toString();
                        strSub = result5.getPrimitivePropertySafelyAsString("Subject").toString();
                        strCompose = result5.getPrimitivePropertySafelyAsString("Reply").toString();

                        mail_to.setText(strTo);
                        mail_cc.setText(strCc);
                        mail_bcc.setText(strBcc);
                        mail_attach.setText(strAttach);
                        mail_subject.setText(strSub);
                        mail_compose.setText(strCompose);


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

                Toast.makeText(reply_orderInquiry.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(reply_orderInquiry.this, OrderInquiryList.class);
        startActivity(i);
        finish();
    }

}
