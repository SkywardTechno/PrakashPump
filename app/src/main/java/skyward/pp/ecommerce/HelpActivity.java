package skyward.pp.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.calling.Call;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import skyward.pp.BaseActivity;
import skyward.pp.CallScreenActivity;
import skyward.pp.DashboardActivity;
import skyward.pp.PumpDetailActivity;
import skyward.pp.R;
import skyward.pp.SinchService;
import skyward.pp.util.Utility;

public class HelpActivity extends NavigationHeader implements ServiceConnection {

    EditText help_txt_feedback;
    Button help_btn_submit,help_btn_call;
    private SinchService.SinchServiceInterface mSinchServiceInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_help);

        getLayoutInflater().inflate(R.layout.activity_help, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Help");
        ab.setHomeButtonEnabled(true);

        help_btn_submit = (Button) findViewById(R.id.help_btn_submit);
        help_btn_call = (Button) findViewById(R.id.help_btn_call);
        help_txt_feedback = (EditText) findViewById(R.id.help_txt_feedback);


        help_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.isInternetConnected(getApplicationContext())){
                    if(help_txt_feedback.getText().toString().isEmpty()){
                        Toast.makeText(HelpActivity.this, "Please enter some text", Toast.LENGTH_SHORT).show();
                    }else if(help_txt_feedback.getText().toString().startsWith(" ")){
                        Toast.makeText(HelpActivity.this, "Text cannot start with space !", Toast.LENGTH_SHORT).show();

                    }else{
                        new Submit().execute();
                    }
                }
            }
        });

        help_btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    String username = "Admin";
                    Call call = getSinchServiceInterface().callUserVideo(username);
                    String callId = call.getCallId();
                    Intent callScreen = new Intent(getApplicationContext(), CallScreenActivity.class);
                    callScreen.putExtra(SinchService.CALL_ID, callId);
                    Utility.savefromviewproduct(getApplicationContext(), "false");
                    startActivity(callScreen);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }



    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }

   /* private void stopButtonClicked() {

        finish();
    }
*/

    class Submit extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(HelpActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.SEND_HELP_QUERIES);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("Name",Utility.getName(getApplicationContext()));
            request.addProperty("MobileNo",Utility.getUserName(getApplicationContext()));
            request.addProperty("Description",help_txt_feedback.getText().toString());
            request.addProperty("emailID",Utility.getemailid(getApplicationContext()));

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
                            + Utility.SEND_HELP_QUERIES, mySoapEnvelop);

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

                    if (!Utility.isInternetConnected(HelpActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

                            Toast.makeText(getBaseContext(),
                                    "Your Feedback submitted successfully !",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(HelpActivity.this, EcommDashboardActivity.class);
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

                Toast.makeText(HelpActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

   /* public SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), EcommDashboardActivity.class);
        startActivity(i);
        finish();
    }
}
