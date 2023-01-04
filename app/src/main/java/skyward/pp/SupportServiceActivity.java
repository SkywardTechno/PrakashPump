package skyward.pp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SupportServiceActivity extends AppCompatActivity {

    LinearLayout ll5star,ll3star,ll1star;
    Button finish;
    ImageView img_5star,img_3star,img_1star;
    int ID = 0;
    TextView termscondition;
    RadioGroup inquire;
    RadioButton yes, no;
    int number;
    EditText servicedetails;
    public  static  String servicetypeid="";
    String a="";
    private ArrayList<String> servicelist = new ArrayList<String>();
    private ArrayList<Integer> servicelistID = new ArrayList<Integer>();
    public int tempservicelist =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supportservice);

        ll5star = (LinearLayout) findViewById(R.id.ll_5star);
        ll3star = (LinearLayout) findViewById(R.id.ll_3star);
        ll1star = (LinearLayout) findViewById(R.id.ll_1star);
        finish = (Button) findViewById(R.id.btn_finishservice);
        termscondition = (TextView) findViewById(R.id.termscondition);
        servicedetails = (EditText) findViewById(R.id.servicedetails);
        inquire = (RadioGroup) findViewById(R.id.inquireyesno);
        yes = (RadioButton) findViewById(R.id.yes);
        no = (RadioButton) findViewById(R.id.no);
        img_5star = (ImageView) findViewById(R.id.img_5star);
        img_3star = (ImageView) findViewById(R.id.img_3star);
        img_1star = (ImageView) findViewById(R.id.img_1star);


        setTitle("Support & Service");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Support & Service");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SupportServiceActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });


        servicetypeid="";
        inquire.setVisibility(View.INVISIBLE);
        finish.setVisibility(View.INVISIBLE);

        if(getIntent().hasExtra("one")){
            a = getIntent().getStringExtra("one");
        }else{
            a= "two";
        }

        System.out.println("***********" +a);

        if(a.equalsIgnoreCase("one")) {
            finish.setVisibility(View.VISIBLE);
            inquire.setVisibility(View.VISIBLE);
            yes.setChecked(true);
            servicetypeid = getIntent().getStringExtra("stype");
            System.out.println(servicetypeid);
            if(servicetypeid.equalsIgnoreCase("5 star")){
                img_5star.setImageResource(R.drawable.star2);
                img_3star.setImageResource(R.drawable.star);
                img_1star.setImageResource(R.drawable.star);
            }else if(servicetypeid.equalsIgnoreCase("3 star")){

                img_3star.setImageResource(R.drawable.star2);
                img_5star.setImageResource(R.drawable.star);
                img_1star.setImageResource(R.drawable.star);


            }else if(servicetypeid.equalsIgnoreCase("1 star")){

                img_1star.setImageResource(R.drawable.star2);
                img_3star.setImageResource(R.drawable.star);
                img_5star.setImageResource(R.drawable.star);
            }

        }else{
            inquire.setVisibility(View.INVISIBLE);
            finish.setVisibility(View.INVISIBLE);
        }

        if(!Utility.isInternetConnected(SupportServiceActivity.this)){
            Toast.makeText(SupportServiceActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }else{
            new FetchService().execute();
        }

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(SupportServiceActivity.this, " successfully!", Toast.LENGTH_SHORT).show();

               if(servicetypeid.isEmpty()){
                   Toast.makeText(SupportServiceActivity.this, "You need to select a service", Toast.LENGTH_SHORT).show();
               }else {
                   Intent i = new Intent(SupportServiceActivity.this, DashboardActivity.class);
                   startActivity(i);
                   finish();
               }
            }
        });

        ll5star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicetypeid="5 Star";

                inquire.setVisibility(View.VISIBLE);
                img_5star.setImageResource(R.drawable.star2);
                img_3star.setImageResource(R.drawable.star);
                img_1star.setImageResource(R.drawable.star);
                finish.setVisibility(View.GONE);
                yes.setChecked(false);
                no.setChecked(false);
               /* ll5star.setBackgroundColor(Color.GRAY);
                ll1star.setBackgroundColor(Color.WHITE);
                ll3star.setBackgroundColor(Color.WHITE);*/
                if (!Utility.isInternetConnected(SupportServiceActivity.this)) {

                    Toast.makeText(SupportServiceActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000) ;
                }else {
                    new FetchService().execute();
                   
                }


                //     number = 5;
            }
        });

        ll3star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicetypeid="3 Star";

                inquire.setVisibility(View.VISIBLE);
                img_3star.setImageResource(R.drawable.star2);
                img_5star.setImageResource(R.drawable.star);
                img_1star.setImageResource(R.drawable.star);
                finish.setVisibility(View.GONE);
                yes.setChecked(false);
                no.setChecked(false);
               /* ll3star.setBackgroundColor(Color.GRAY);
                ll1star.setBackgroundColor(Color.WHITE);
                ll5star.setBackgroundColor(Color.WHITE);
*/
                if (!Utility.isInternetConnected(SupportServiceActivity.this)) {

                    Toast.makeText(SupportServiceActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000) ;
                }else {
                    new FetchService().execute();

                }


                //   number = 3;
            }
        });

        ll1star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                servicetypeid="1 Star";

                inquire.setVisibility(View.VISIBLE);
                img_1star.setImageResource(R.drawable.star2);
                img_3star.setImageResource(R.drawable.star);
                img_5star.setImageResource(R.drawable.star);
                finish.setVisibility(View.GONE);
                yes.setChecked(false);
                no.setChecked(false);
               /* ll1star.setBackgroundColor(Color.GRAY);
                ll5star.setBackgroundColor(Color.WHITE);
                ll3star.setBackgroundColor(Color.WHITE);*/

                if (!Utility.isInternetConnected(SupportServiceActivity.this)) {

                    Toast.makeText(SupportServiceActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000) ;
                }else {
                    new FetchService().execute();

                }


                // number = 1;
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.yes:
                if (checked)
                    //Toast.makeText(SupportServiceActivity.this, yes.getText(), Toast.LENGTH_SHORT).show();

                if(a == "one") {
                    finish.setVisibility(View.VISIBLE);
                }else{
                    finish.setVisibility(View.INVISIBLE);
                }

                Intent i=new Intent(SupportServiceActivity.this,SupportInquiryActivity.class);
                i.putExtra("servicetypeID",servicetypeid);
                startActivity(i);
                finish();


                // Pirates are the best
                break;
            case R.id.no:

                if (checked)
                    finish.setVisibility(View.VISIBLE);
               // Toast.makeText(SupportServiceActivity.this, no.getText(), Toast.LENGTH_SHORT).show();

                // Ninjas rule
                break;
        }
    }


    class FetchServiceDetails extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(SupportServiceActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_TCBYSERVICE);

            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));
            request.addProperty("serviceTypeID", ID);
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
                            + Utility.GET_TCBYSERVICE, mySoapEnvelop);


                } catch (XmlPullParserException e) {
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    // System.out.println("XmlPullParserException 0");
                } catch (SocketTimeoutException e) {
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    // System.out.println("SocketTimeoutException 1");
                } catch (SocketException e) {

                    e.printStackTrace();
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
                            String TC = soapResult.getPrimitivePropertySafelyAsString("TermsandConditions")
                                    .toString();
                            servicedetails.setText(TC);
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

                Toast.makeText(SupportServiceActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

    }

    class FetchService extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(SupportServiceActivity.this);
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

                            if(servicetypeid.equalsIgnoreCase(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString())){
                                ID = Integer.parseInt(soapResult.getPropertySafelyAsString(
                                        "ID").toString());
                            }
                        }

                        if(ID !=0){
                            if (!Utility.isInternetConnected(SupportServiceActivity.this)) {

                                Toast.makeText(SupportServiceActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();


                            }else {
                                new FetchServiceDetails().execute();

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

                Toast.makeText(SupportServiceActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SupportServiceActivity.this, DashboardActivity.class);
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
