package skyward.pp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skyward.pp.util.Utility;

public class AddCustomeractivity extends AppCompatActivity {

    EditText customer_name,customer_phn,country,city,area,usertype,customer_email,customer_password;
    Button btn_addcustomer;
    private ArrayList<String> countrylist = new ArrayList<String>();
    private ArrayList<Integer> countrylistID = new ArrayList<Integer>();
    private ArrayList<Integer> countrymobdigitslist = new ArrayList<Integer>();
    public int mobiledigits =0;
    public int tempcountrylist =0;
    private ArrayList<String> usertypelist = new ArrayList<String>();
    private ArrayList<Integer> usertypelistID = new ArrayList<Integer>();
    public int tempusertype =0;
    String simCode="",countryname;
    private ArrayList<String> citylist = new ArrayList<String>();
    private ArrayList<Integer> citylistID = new ArrayList<Integer>();
    int tempcityID = 0;
    private ArrayList<String> arealist = new ArrayList<String>();
    private ArrayList<Integer> arealistID = new ArrayList<Integer>();
    int tempareaID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customeractivity);

        setTitle("Add Customer");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Customer");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddCustomeractivity.this, CustomerlistActivity.class);
                startActivity(i);
                finish();
            }
        });
        customer_name = (EditText) findViewById(R.id.add_customer_name);
        customer_phn = (EditText) findViewById(R.id.add_customer_phn);
        customer_password = (EditText) findViewById(R.id.add_customer_password);
        country = (EditText) findViewById(R.id.add_customer_place);
        city = (EditText) findViewById(R.id.add_customer_farmno);
        area = (EditText) findViewById(R.id.add_customer_area);
        usertype = (EditText) findViewById(R.id.add_customer_usertype);
        customer_email = (EditText) findViewById(R.id.add_customer_email);
        btn_addcustomer = (Button) findViewById(R.id.btn_addcustomer);
        btn_addcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                init();
            }
        });

       /* try {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            simCode = tm.getSimCountryIso();
            System.out.println("Country" + simCode);
        }catch(Exception e){
            e.printStackTrace();
        }
*/
        if (!Utility.isInternetConnected(AddCustomeractivity.this)) {
            Toast.makeText(AddCustomeractivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            new FetchCountry().execute();
            new FetchCustomerType().execute();
        }

        usertype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] countrys = usertypelist.toArray(new String[usertypelist
                        .size()]);
                alertUserType(countrys, v, usertype);
            }
        });

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
                    Toast.makeText(AddCustomeractivity.this, "Please select Country first!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AddCustomeractivity.this, "Please select City first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void init() {
        boolean isError = false;
        String errorMsg = "Invalid Data";
        if (customer_name.getText().toString().equals("") || customer_password.getText().toString().equals("") || usertype.getText().toString().equals("") || customer_name.getText().toString().startsWith(" ")) {
            isError = true;
            errorMsg = "Please Enter Mandatory fields";
        }
        if (customer_phn.getText().toString().equals("") ) {
            isError = true;
            errorMsg = "Please Enter Mobile Number";
        }else if(customer_phn.getText().toString().length() < 6 || customer_phn.getText().toString().length() > 16)
                {
                    isError = true;
                    errorMsg = "Enter Valid Mobile Number";
                }




        if(customer_email.getText().toString().equals("")) {


        }else{

            boolean chk=isEmailValid(customer_email.getText().toString());
            if(chk==false)
            {
                isError = true;
                errorMsg = "Enter Valid Email Address";

            }

        }

        if (isError) {
            Toast.makeText(AddCustomeractivity.this, errorMsg,
                    Toast.LENGTH_LONG).show();
        }else{

            new CreateCustomer().execute();}

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



    private void alertUserType(final String[] visitType, View v,
                               final TextView tvVisitType2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                AddCustomeractivity.this);
        // Source of the data in the DIalog

        // Set the dialog title
        builder.setTitle("Select UserType")
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

                                for (int i = 0; i < usertypelist.size(); i++) {
                                    if (usertypelist.get(i).toString()
                                            .equals(visitType[which])) {
                                        tempusertype = usertypelistID.get(i);
                                       //serTypeID = String.valueOf(countrylistID.get(i));
                                    }

                                }
                                tvVisitType2.setText(visitType[which]);
                                dialog.dismiss();

                               /* country = visitType[which];
                                new FetchCountry().execute();*/

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
        progress = new ProgressDialog(AddCustomeractivity.this);
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

                   /* if(UserTypeID.equals(soapResult.getPropertySafelyAsString("ID")
                            .toString())){
                        usertype.setText(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());*/
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
            Toast.makeText(AddCustomeractivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}

    private void alertCountry(final String[] visitType, View v,
                              final TextView tvVisitType2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                AddCustomeractivity.this);
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
                                customer_phn.setText("");
                                customer_phn.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mobiledigits)});

                                countryname = visitType[which];
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
                AddCustomeractivity.this);
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
                AddCustomeractivity.this);
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

    class FetchCountry extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddCustomeractivity.this);
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
                Toast.makeText(AddCustomeractivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
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
            progress = new ProgressDialog(AddCustomeractivity.this);
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
            progress = new ProgressDialog(AddCustomeractivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETAREA);



            request.addProperty("CityID",tempcityID);



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


    class CreateCustomer extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
        progress = new ProgressDialog(AddCustomeractivity.this);
        progress.setMessage("Creating User...");
        progress.setCancelable(false);
        progress.show();
    }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.ADD_CUSTOMER);
            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            myAndroidHttpTransport = null;
            request.addProperty("token", Utility.getAuthToken(AddCustomeractivity.this));
            request.addProperty("mobileNo", customer_phn.getText().toString());
            request.addProperty("password", customer_password.getText().toString());
            request.addProperty("name", customer_name.getText().toString());
            request.addProperty("country",tempcountrylist);
            request.addProperty("City",tempcityID );
            request.addProperty("area",tempareaID );
            request.addProperty("emailID", customer_email.getText().toString());
            request.addProperty("userTypeID", tempusertype);
            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);
            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.ADD_CUSTOMER, mySoapEnvelop);
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
                    if (!Utility.isInternetConnected(AddCustomeractivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {
                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                            boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                    "IsSucceed").toString());
                            Toast.makeText(AddCustomeractivity.this, "Customer Created Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AddCustomeractivity.this, CustomerlistActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {

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

            }else {
                Toast.makeText(AddCustomeractivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddCustomeractivity.this, CustomerlistActivity.class);
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
