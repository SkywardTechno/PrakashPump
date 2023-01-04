package skyward.pp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skyward.pp.util.Utility;

public class CustomerDetailsActivity extends AppCompatActivity {
    Dialog pop_confimation;
    TextView txtpop_msg;
    Button btnpop_cancel;
    Button getBtnpop_submit;
    EditText name, country, mobileno,city,area,usertype,email;
    Button delete,edit;
    TextView goback;
    String UserTypeID, CustomerID;
    private ArrayList<String> citylist = new ArrayList<String>();
    private ArrayList<Integer> citylistID = new ArrayList<Integer>();
    int tempcityID = 0;
    private ArrayList<String> arealist = new ArrayList<String>();
    private ArrayList<Integer> arealistID = new ArrayList<Integer>();
    int tempareaID = 0;
    private ArrayList<String> countrylist = new ArrayList<String>();
    private ArrayList<Integer> countrylistID = new ArrayList<Integer>();
    private ArrayList<Integer> countrymobdigitslist = new ArrayList<Integer>();
    public int mobiledigits =0;
    public int tempcountrylist =0;
    private ArrayList<String> usertypelist = new ArrayList<String>();
    private ArrayList<Integer> usertypelistID = new ArrayList<Integer>();
    public int tempusertypelist =0;
    int countryid=0;
    int cityid=0;
    int areaid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerdetails);

        setTitle("Customer Details");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Customer Details");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CustomerDetailsActivity.this, CustomerlistActivity.class);
                startActivity(i);
                finish();
            }
        });

        name = (EditText) findViewById(R.id.cd_name);
        country = (EditText) findViewById(R.id.cd_place);
        email = (EditText) findViewById(R.id.cd_email);
        mobileno = (EditText) findViewById(R.id.cd_mobileno);
        city = (EditText) findViewById(R.id.cd_plot);
        area = (EditText) findViewById(R.id.cd_area);
        usertype = (EditText) findViewById(R.id.cd_usertype);
        delete = (Button) findViewById(R.id.cd_btn_delete);
        edit = (Button) findViewById(R.id.cd_btn_edit);


        usertype.setEnabled(false);
        Intent i = getIntent();
        name.setText(i.getStringExtra("Name"));
        country.setText(i.getStringExtra("Country"));
        country.setEnabled(false);
        mobileno.setText(i.getStringExtra("MobileNo"));
        city.setText(i.getStringExtra("City"));
        area.setText(i.getStringExtra("Area"));
        email.setText(i.getStringExtra("EmailID"));

        UserTypeID = i.getStringExtra("UserTypeID");
        CustomerID = i.getStringExtra("ID");

        if (!Utility.isInternetConnected(CustomerDetailsActivity.this)) {

            Toast.makeText(CustomerDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            new FetchCustomerType().execute();
            new FetchCountry().execute();

        }

       /* mobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               *//* Intent skypeVideo = new Intent("android.intent.action.VIEW");
                skypeVideo.setData(Uri.parse("skype:" + mobileno.getText().toString() + "?call&video=true"));
                startActivity(skypeVideo);
*//*
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setData(Uri.parse("tel:" + mobileno.getText().toString()));
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);

            }
        });*/
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String[] countrys = countrylist.toArray(new String[countrylist
                        .size()]);
                alertCountry(countrys, v, country);


            }
        });

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tempcountrylist !=0) {

                    String[] countrys = citylist.toArray(new String[citylist
                            .size()]);
                    alertCity(countrys, v, city);
                }else{
                    Toast.makeText(CustomerDetailsActivity.this, "Please select Country first!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tempcityID != 0) {

                    String[] countrys = arealist.toArray(new String[arealist
                            .size()]);
                    alertArea(countrys, v, area);

                }else{
                    Toast.makeText(CustomerDetailsActivity.this, "Please select City first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Utility.isInternetConnected(CustomerDetailsActivity.this)) {

                    Toast.makeText(CustomerDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000) ;
                }else {
                    init();
                    

                }


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(CustomerDetailsActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure you want to Inactive?");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                if (!Utility.isInternetConnected(CustomerDetailsActivity.this)) {

                                    Toast.makeText(CustomerDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                                    new Handler().postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000) ;
                                }else {
                                    new DeleteCustomer().execute();

                                }

                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();



            }
        });

/*
        usertype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] countrys = countrylist.toArray(new String[countrylist
                        .size()]);
                alertUserType(countrys, v, usertype);
            }
        });
*/

    }


    public void init() {

        boolean isError = false;
        String errorMsg = "Invalid Data";
        if (name.getText().toString().equals("")) {
            isError = true;
            errorMsg = "Please Enter Name";
        }else if (mobileno.getText().toString().isEmpty()) {
            isError = true;
            errorMsg = "Please enter Mobile number";
        }else if (!mobileno.getText().toString().isEmpty() ) {

           /* if(Pattern.matches("[+0-9]+", mobileno.getText().toString()))
            {*/
                if(mobileno.getText().toString().length() < 6 || mobileno.getText().toString().length() > 16)
                {
                    isError = true;
                    errorMsg = "Enter Valid Mobile Number";

                }/*else if(!mobileno.getText().toString().contains("+")) {
                    isError = true;
                    errorMsg = "Enter Valid Mobile Number with Country code";
                }*/
           /* }else{

                isError = true;
                errorMsg = "Enter Valid Mobile Number";

            }
*/
        }


        if(email.getText().toString().equals("")) {


        }else{

            boolean chk=isEmailValid(email.getText().toString());
            if(chk==false)
            {
                isError = true;
                errorMsg = "Enter Valid Email Address";

            }

        }

        /*else if(!customer_email.getText().toString().isEmpty()) {

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (customer_email.getText().toString().matches(emailPattern))
            {

            } else{
              isError = true;
              errorMsg = "Please enter valid email address";
            }

        }*/
        if (isError) {
            Toast.makeText(CustomerDetailsActivity.this, errorMsg,
                    Toast.LENGTH_LONG).show();
        }else {

            new UpdateCustomer().execute();
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

    private void alertCountry(final String[] visitType, View v,
                              final TextView tvVisitType2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                CustomerDetailsActivity.this);
        // Source of the data in the DIalog

        // Set the dialog title
        builder.setTitle("Select Country")
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

                                for (int i = 0; i < countrylist.size(); i++) {
                                    if (countrylist.get(i).toString()
                                            .equals(visitType[which])) {
                                        tempcountrylist = countrylistID.get(i);
                                        mobiledigits = countrymobdigitslist.get(i);
                                    }
                                }
                                tvVisitType2.setText(visitType[which]);
                                dialog.dismiss();
                                mobileno.setText("");
                                mobileno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mobiledigits)});

                                tempcityID = 0;
                                city.setText("");
                                tempareaID = 0;
                                area.setText("");
                                citylist = new ArrayList<String>();
                                arealist = new ArrayList<String>();
                                citylistID = new ArrayList<Integer>();
                                arealistID = new ArrayList<Integer>();
                                new FetchCity().execute();

                            }
                        });

        // Set the action buttons
        builder.show();

    }

    private void alertCity(final String[] visitType, View v,
                           final TextView tvVisitType2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                CustomerDetailsActivity.this);
        // Source of the data in the DIalog

        // Set the dialog title
        builder.setTitle("Select City")
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

                                for (int i = 0; i < citylist.size(); i++) {
                                    if (citylist.get(i).toString()
                                            .equals(visitType[which])) {
                                        tempcityID = citylistID.get(i);
                                    }
                                }
                                tvVisitType2.setText(visitType[which]);
                                dialog.dismiss();
                                tempareaID = 0;
                                area.setText("");
                                arealist = new ArrayList<String>();
                                arealistID = new ArrayList<Integer>();
                                new FetchArea().execute();

                            }
                        });

        // Set the action buttons
        builder.show();

    }


    private void alertArea(final String[] visitType, View v,
                           final TextView tvVisitType2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                CustomerDetailsActivity.this);
        // Source of the data in the DIalog

        // Set the dialog title
        builder.setTitle("Select Area")
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

                                for (int i = 0; i < arealist.size(); i++) {
                                    if (arealist.get(i).toString()
                                            .equals(visitType[which])) {
                                        tempareaID = arealistID.get(i);
                                    }
                                }
                                tvVisitType2.setText(visitType[which]);
                                dialog.dismiss();


                            }
                        });

        // Set the action buttons
        builder.show();

    }




    class FetchCustomerType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(CustomerDetailsActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_CUSTOMERTYPE);

            if(usertypelist.size() > 0){
                usertypelist.clear();
                usertypelistID.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_CUSTOMERTYPE);

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
                            + Utility.GET_CUSTOMERTYPE, mySoapEnvelop);


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
                            usertypelist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());

                            usertypelistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));

                            if (UserTypeID.equals(soapResult.getPropertySafelyAsString("ID")
                                    .toString())) {
                                usertype.setText(soapResult.getPropertySafelyAsString("Name", "")
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
                }

            }else{

                Toast.makeText(CustomerDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class UpdateCustomer extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(CustomerDetailsActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.UPDATE_CUSTOMER);

            request.addProperty("token", Utility.getAuthToken(CustomerDetailsActivity.this));
            request.addProperty("customerID", CustomerID);
            request.addProperty("name",name.getText().toString() );
            request.addProperty("country",tempcountrylist);
            request.addProperty("city",tempcityID );
            request.addProperty("area",tempareaID );
            request.addProperty("emailID",email.getText().toString());
            request.addProperty("phoneNo",mobileno.getText().toString() );
            request.addProperty("fileName","" );
            request.addProperty("filePath","" );

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.UPDATE_CUSTOMER);

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
                            + Utility.UPDATE_CUSTOMER, mySoapEnvelop);


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


                        Toast.makeText(CustomerDetailsActivity.this, "Customer Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(CustomerDetailsActivity.this, CustomerlistActivity.class);
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
                }

            }else{

                Toast.makeText(CustomerDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }



    class DeleteCustomer extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(CustomerDetailsActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.DELETE_CUSTOMER);

            request.addProperty("token", Utility.getAuthToken(CustomerDetailsActivity.this));
            request.addProperty("ID",CustomerID );

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.DELETE_CUSTOMER);

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
                            + Utility.DELETE_CUSTOMER, mySoapEnvelop);


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


                        Toast.makeText(CustomerDetailsActivity.this, usertype.getText().toString()+" inactivated successfully. Still you can activate users from web portal again.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(CustomerDetailsActivity.this, CustomerlistActivity.class);
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
                }

            }else{

                Toast.makeText(CustomerDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FetchCountry extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(CustomerDetailsActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_COUNTRY);

            if (countrylist.size() > 0) {
                countrylist.clear();
                countrylistID.clear();
                countrymobdigitslist.clear();
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
                            countrylist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());
                            countrylistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));
                            if(soapResult.hasProperty("MobDigits")){
                                countrymobdigitslist.add(Integer.parseInt(soapResult.getPropertySafelyAsString("MobDigits").toString()));

                            }else{
                                countrymobdigitslist.add(0);

                            }

                        }


                        if(country.getText().toString().equalsIgnoreCase("")){

                        }else {

                            for (int i = 0; i < countrylist.size(); i++) {

                                if (country.getText().toString().equalsIgnoreCase(countrylist.get(i))) {

                                    tempcountrylist = countrylistID.get(i);
                                    mobiledigits = countrymobdigitslist.get(i);
                                    mobileno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mobiledigits)});

                                    break;
                                }else{

                                }
                            }

                        }
                        if(tempcountrylist!=0) {
                            if (Utility.isInternetConnected(getApplicationContext())) {
                                FetchCity task = new FetchCity();
                                task.execute();
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
                }

            } else {
                Toast.makeText(CustomerDetailsActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FetchCity extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(CustomerDetailsActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETCITY);

            request.addProperty("CountryID", tempcountrylist);

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
                            + Utility.GETCITY, mySoapEnvelop);


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

                        citylist = new ArrayList<>();
                        citylistID = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            citylist.add(soapResult.getPropertySafelyAsString("CityName", "")
                                    .toString());

                            citylistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }
                        if(city.getText().toString().equalsIgnoreCase("")){

                        }else {

                            for (int i = 0; i < citylist.size(); i++) {

                                if (city.getText().toString().equalsIgnoreCase(citylist.get(i))) {

                                    tempcityID = citylistID.get(i);

                                    break;
                                }else{

                                }
                            }


                        }
                        if(tempcityID !=0){
                            if(Utility.isInternetConnected(getApplicationContext()))
                            {
                                FetchArea task=new FetchArea();
                                task.execute();
                            }
                        }

                    } else {

                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FetchArea extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(CustomerDetailsActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETAREA);



            request.addProperty("CityID", tempcityID);



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
                            + Utility.GETAREA, mySoapEnvelop);


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

                        arealist = new ArrayList<>();
                        arealistID = new ArrayList<>();

                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            arealist.add(soapResult.getPropertySafelyAsString("AreaName", "")
                                    .toString());

                            arealistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }

                        if(area.getText().toString().equalsIgnoreCase("")){

                        }else{

                            if(arealist.size() >0) {
                                for (int i = 0; i < arealist.size(); i++) {

                                    if (area.getText().toString().equalsIgnoreCase(arealist.get(i))) {
                                        tempareaID = arealistID.get(i);

                                        break;
                                    }
                                }
                            }

                        }


                    } else {

                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(CustomerDetailsActivity.this, CustomerlistActivity.class);
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
