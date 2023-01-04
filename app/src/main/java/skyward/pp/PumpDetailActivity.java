package skyward.pp;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import skyward.pp.util.Utility;

public class PumpDetailActivity extends AppCompatActivity {

    Button btn_go, pd_scanqr;
    TextView viewdetail;
    EditText pumpserialno;
    ImageButton addpumpdetail;
    String Serialno,Guaranteestartdate,Guaranteeenddate,invoiceno,servicetypeid,pumpcode,ID,gstartnew,gendnew,invoicedate,wstart,wend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pumpdetail);

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

                Intent i = new Intent(PumpDetailActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_go = (Button) findViewById(R.id.pumpdetail_btn_go);
        viewdetail = (TextView) findViewById(R.id.pumpdetail_view);
        pd_scanqr = (Button) findViewById(R.id.pumpdetail_scanqr);
        pumpserialno = (EditText) findViewById(R.id.pumpdetail_pumpserialno);
        addpumpdetail = (ImageButton) findViewById(R.id.add_pumpdetail);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pumpserialno.getText().toString().equals("")){

                    Toast.makeText(PumpDetailActivity.this, "Enter Pump Serial No", Toast.LENGTH_SHORT).show();
                }else{
                    new ShowPumpDetails().execute();
                }

            }
        });
        final Activity activity=this;
        pd_scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator i=new IntentIntegrator(activity);
                i.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                i.setPrompt("Scan");
                i.setCameraId(0);
                i.setBeepEnabled(true);
                i.setBarcodeImageEnabled(true);
                i.initiateScan();
            }
        });

        addpumpdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PumpDetailActivity.this, AddPumpDetailActivity.class);
                startActivity(i);
                finish();
            }
        });

        viewdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PumpDetailActivity.this, MyPumpDetailsListActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents()==null)
            {
                //Toast.makeText(this,"Cancelled..,",Toast.LENGTH_LONG).show();
            }
            else
            {
                pumpserialno.setText(result.getContents().toString());
                if(pumpserialno.getText().toString().equals("")){

                    Toast.makeText(PumpDetailActivity.this, "Enter Pump Serial No or Model No", Toast.LENGTH_SHORT).show();
                }else{
                    new ShowPumpDetails().execute();
                }
                // Toast.makeText(this,"Scanned..."+result.getContents(),Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class ShowPumpDetails extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(PumpDetailActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PUMPDETAILS);


            request.addProperty("token", Utility.getAuthToken(PumpDetailActivity.this));
            request.addProperty("serialNo", pumpserialno.getText().toString());

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_PUMPDETAILS);

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
                            + Utility.GET_PUMPDETAILS, mySoapEnvelop);


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


                            pumpcode = soapResult.getPrimitivePropertySafelyAsString("ModelNum").toString();
                            servicetypeid = soapResult.getPrimitivePropertySafelyAsString("ServiceTypeID")
                                    .toString();

                            Serialno = soapResult.getPrimitivePropertySafelyAsString("SerialNo")
                                    .toString();
                            invoiceno = soapResult.getPrimitivePropertySafelyAsString("InvoiceNo")
                                    .toString();
                            Guaranteestartdate = soapResult.getPrimitivePropertySafelyAsString("GuaranteeStartDate")
                                    .toString();

                            Guaranteeenddate = soapResult.getPrimitivePropertySafelyAsString("GuaranteeEndDate")
                                    .toString();
                            wstart = soapResult.getPrimitivePropertySafelyAsString("WarrantyStartDate")
                                    .toString();
                            wend = soapResult.getPrimitivePropertySafelyAsString("WarrantyEndDate")
                                    .toString();

                            invoicedate = soapResult.getPrimitivePropertySafelyAsString("InvoiceDate")
                                    .toString();

                            ID = soapResult.getProperty(
                                    "ID").toString();
/*
                            String[] arrdate = Guaranteestartdate.split(" ");
                            String year = arrdate[2];
                            String month = arrdate[0];
                            String day = arrdate[1];

                            Guaranteestartdate = day + "-" + month + "-" + year;

                            String[] arrdate1 = Guaranteeenddate.split(" ");
                            String year1 = arrdate1[2];
                            String month1 = arrdate1[0];
                            String day1 = arrdate1[1];

                            Guaranteeenddate = day1 + "-" + month1 + "-" + year1;
*/

                        }

                        Intent i = new Intent(PumpDetailActivity.this, ShowPumpDetailactivity.class);
                        i.putExtra("pumpcode", pumpcode);
                        i.putExtra("servicetypeid", servicetypeid);
                        i.putExtra("serialno", Serialno);
                        i.putExtra("invoiceno", invoiceno);
                        i.putExtra("gstart", Guaranteestartdate);
                        i.putExtra("gend", Guaranteeenddate);
                        i.putExtra("wend", wend);
                        i.putExtra("wstart", wstart);
                        i.putExtra("invoicedate", invoicedate);
                        i.putExtra("id", ID);
                        i.putExtra("frompumpdetail", "frompumpdetail");
                        startActivity(i);
                        finish();

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

                Toast.makeText(PumpDetailActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(PumpDetailActivity.this, DashboardActivity.class);
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
