package skyward.pp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

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

public class AddMiscellaneousActivity extends AppCompatActivity {

    EditText misc_name;
    TextView misc_cat;
    Button misc_add;
    private ArrayList<String> catagoryList = new ArrayList<String>();
    private ArrayList<Integer> catagorylistID = new ArrayList<Integer>();
    String tempcatagorylist ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmiscellaneous);

        setTitle("Add Miscellaneous");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Miscellaneous");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(AddMiscellaneousActivity.this, CategorylistActivity.class);
                startActivity(i);
                finish();
            }
        });
        
        misc_add = (Button) findViewById(R.id.misc_btn_add);
        misc_cat = (TextView) findViewById(R.id.misc_selcat);
        misc_name = (EditText) findViewById(R.id.misc_addvalue);

        if (!Utility.isInternetConnected(AddMiscellaneousActivity.this)) {

            Toast.makeText(AddMiscellaneousActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            new FetchCatagory().execute();
        }

        misc_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] countrys = catagoryList.toArray(new String[catagoryList
                        .size()]);
                alertCatagory(countrys, v, misc_cat);
            }
        });
        
        misc_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(misc_cat.getText().toString().equals("")){

                    Toast.makeText(AddMiscellaneousActivity.this, "Please Select category", Toast.LENGTH_SHORT).show();
                    
                }else if(misc_name.getText().toString().equals("")){

                    Toast.makeText(AddMiscellaneousActivity.this, "Please enter value", Toast.LENGTH_SHORT).show();
                    
                }else if(misc_name.getText().toString().startsWith(" ")){

                    Toast.makeText(AddMiscellaneousActivity.this, "Value Cannot start with space", Toast.LENGTH_SHORT).show();
                } else{
                    if (!Utility.isInternetConnected(AddMiscellaneousActivity.this)) {

                        Toast.makeText(AddMiscellaneousActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000) ;
                    }else {
                        new AddCatagory().execute();
                    }
                }
            }
        });

    }

    private void alertCatagory(final String[] visitType, View v,
                              final TextView tvVisitType2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                AddMiscellaneousActivity.this);
        // Source of the data in the DIalog

        // Set the dialog title
        builder.setTitle("Select Category")
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

                                for (int i = 0; i < catagoryList.size(); i++) {
                                    if (catagoryList.get(i).toString()
                                            .equals(visitType[which])) {
                                       // tempcatagorylist = String.valueOf(catagoryList.get(i));

                                    }

                                }
                                tvVisitType2.setText(visitType[which]);
                                dialog.dismiss();

                               // country = visitType[which];
                               // new FetchCountry().execute();

                            }
                        });

        // Set the action buttons
        builder.show();

    }

    class FetchCatagory extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddMiscellaneousActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_CATAGORY);

            if(catagoryList.size() > 0){
                catagoryList.clear();
                //catagorylistID.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_CATAGORY);

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
                            + Utility.GET_CATAGORY, mySoapEnvelop);


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
            if (result != null) {
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
                            catagoryList.add(soapResult.getPropertySafelyAsString("Category", "")
                                    .toString());

/*
                        catagorylistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));
*/


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

            } else {

                Toast.makeText(AddMiscellaneousActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class AddCatagory extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddMiscellaneousActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.INSERT_MISCDATA);


            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            myAndroidHttpTransport = null;

            request.addProperty("token", Utility.getAuthToken(AddMiscellaneousActivity.this));
            request.addProperty("Name", misc_name.getText().toString());
            request.addProperty("category", misc_cat.getText().toString());

            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.INSERT_MISCDATA, mySoapEnvelop);

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
            if (result != null) {
                try {

                    if (!Utility.isInternetConnected(AddMiscellaneousActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                            boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                    "IsSucceed").toString());

                            misc_name.setText("");
                            Toast.makeText(AddMiscellaneousActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();


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


            } else {

                Toast.makeText(AddMiscellaneousActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddMiscellaneousActivity.this, CategorylistActivity.class);
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
