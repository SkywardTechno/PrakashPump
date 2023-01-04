package skyward.pp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
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
import java.util.ArrayList;

import skyward.pp.util.Utility;

public class ShowPumpDetailactivity extends AppCompatActivity {

    Button ok;
    TextView feedback,modelno,serialno,invoiceno,invoicedate,guarantee_start,guarantee_end,service,warranty_start,warranty_end;
    String servicetypeid;
    private ArrayList<String> servicelist = new ArrayList<String>();
    private ArrayList<Integer> servicelistID = new ArrayList<Integer>();
    public int tempservicelist =0;
    String navigatevalue="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pumpdetailactivity);

        setTitle("Pump Detail");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Pump Detail");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ShowPumpDetailactivity.this, PumpDetailActivity.class);
                startActivity(i);
                finish();
            }
        });

        ok = (Button) findViewById(R.id.pdshow_btn_ok);
        feedback = (TextView) findViewById(R.id.pdshow_sharefeedback);
        modelno = (TextView) findViewById(R.id.pdshow_modelno);
        serialno = (TextView) findViewById(R.id.pdshow_serialno);
        invoiceno = (TextView) findViewById(R.id.pdshow_invoiceno);
        guarantee_start = (TextView) findViewById(R.id.pdshow_guarantee_start);
        guarantee_end = (TextView) findViewById(R.id.pdshow_guarantee_end);
        invoicedate = (TextView) findViewById(R.id.pdshow_invoicedate);

        warranty_start = (TextView) findViewById(R.id.pdshow_warranty_start);
        warranty_end = (TextView) findViewById(R.id.pdshow_warranty_end);
        service = (TextView) findViewById(R.id.pdshow_service);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowPumpDetailactivity.this, PumpDetailActivity.class);
                startActivity(i);
                finish();
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShowPumpDetailactivity.this, FeedbackActivity.class);
                startActivity(i);
                finish();
            }
        });

        Intent i = getIntent();
        modelno.setText(i.getStringExtra("pumpcode"));
        Utility.savepumpmodelno(getApplicationContext(), modelno.getText().toString());
        serialno.setText(i.getStringExtra("serialno"));
        Utility.savepumpserialno(getApplicationContext(), serialno.getText().toString());

        invoiceno.setText(i.getStringExtra("invoiceno"));
        guarantee_start.setText(i.getStringExtra("gstart"));
        guarantee_end.setText(i.getStringExtra("gend"));
        warranty_end.setText(i.getStringExtra("wend"));
        warranty_start.setText(i.getStringExtra("wstart"));
        invoicedate.setText(i.getStringExtra("invoicedate"));
        servicetypeid = i.getStringExtra("servicetypeid");

        if(i.hasExtra("frommypump")){
            navigatevalue = "frommypump";
        }else{
            navigatevalue = "frompumpdetail";
        }

        System.out.println("**************"+servicetypeid);

        if(!Utility.isInternetConnected(ShowPumpDetailactivity.this)){
            Toast.makeText(ShowPumpDetailactivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }else{
            new FetchService().execute();
        }

    }

    class FetchService extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(ShowPumpDetailactivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_SERVICE);



            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_SERVICE);

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
                            + Utility.GET_SERVICE, mySoapEnvelop);


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

                            servicelist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());

                            servicelistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));

                            if (servicetypeid.equals(soapResult.getPropertySafelyAsString("ID").toString())) {

                                service.setText(soapResult.getPropertySafelyAsString("Name")
                                        .toString());
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(ShowPumpDetailactivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(navigatevalue.equalsIgnoreCase("frompumpdetail")) {
            Intent i = new Intent(ShowPumpDetailactivity.this, PumpDetailActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(ShowPumpDetailactivity.this, MyPumpDetailsListActivity.class);
            startActivity(i);
            finish();
        }
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
