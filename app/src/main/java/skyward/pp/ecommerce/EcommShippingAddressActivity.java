package skyward.pp.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import skyward.pp.R;
import skyward.pp.util.Utility;

public class EcommShippingAddressActivity extends NavigationHeader {

    Button btn_confirm;
    int countryid=0;
    int cityid=0;
    int areaid=0;
    EditText edt_addrline1;
    EditText edt_addrline2;
    EditText edt_pincode,edt_mobile,edt_email;
    Spinner spin_country;
    Spinner spin_city;
    Spinner spin_area;
    private ArrayList<String> countrylist = new ArrayList<String>();
    private ArrayList<Integer> countrylistID = new ArrayList<Integer>();
    private ArrayList<String> citylist = new ArrayList<String>();
    private ArrayList<Integer> citylistID = new ArrayList<Integer>();
    private ArrayList<String> arealist = new ArrayList<String>();
    private ArrayList<Integer> arealistID = new ArrayList<Integer>();
    private ArrayList<Integer> countrymobdigitslist = new ArrayList<Integer>();
    public int mobiledigits =0;
    String addrline1="",addrline2="",city1="",state1="",pincode1="",country1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ecomm_shipping_address, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Shipping Address");

        ab.setHomeButtonEnabled(true);
        edt_addrline1= (EditText) findViewById(R.id.addressline1);
        edt_addrline2= (EditText) findViewById(R.id.addressline2);
        edt_pincode= (EditText) findViewById(R.id.address_pincode);
        edt_mobile= (EditText) findViewById(R.id.address_mobile);
        edt_email= (EditText) findViewById(R.id.address_email);
        spin_city= (Spinner) findViewById(R.id.address_city);
        spin_country= (Spinner) findViewById(R.id.address_country);
        spin_area= (Spinner) findViewById(R.id.address_area);
        btn_confirm= (Button) findViewById(R.id.btn_addaddress);

        edt_mobile.setText(Utility.getUserName(getApplicationContext()));
        edt_email.setText(Utility.getemailid(getApplicationContext()));

        if(Utility.isInternetConnected(getApplicationContext()))
        {
            FetchSummary task=new FetchSummary();
            task.execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

        }



        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_addrline1.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Addressline1", Toast.LENGTH_SHORT).show();

                }

                else if (countryid==0) {
                    Toast.makeText(getApplicationContext(), "Please Select country", Toast.LENGTH_SHORT).show();

                }
                else if (cityid==0) {
                    Toast.makeText(getApplicationContext(), "Please Select City", Toast.LENGTH_SHORT).show();

                }
                else if (areaid==0) {
                    Toast.makeText(getApplicationContext(), "Please Select area", Toast.LENGTH_SHORT).show();

                }else if (edt_mobile.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();

                }else if (!edt_mobile.getText().toString().isEmpty() ) {

                    if(edt_mobile.getText().toString().length() < 6 || edt_mobile.getText().toString().length() > 16)
                    {
                        Toast.makeText(getApplicationContext(), "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();

                    }else{
                        if(Utility.isInternetConnected(getApplicationContext()))
                        {

                            Utility.saveaddr1(getApplicationContext(), edt_addrline1.getText().toString());
                            Utility.saveaddr2(getApplicationContext(), edt_addrline2.getText().toString());
                            Utility.savecountry(getApplicationContext(), spin_country.getSelectedItem().toString());
                            Utility.savecity(getApplicationContext(), city1);
                            Utility.savearea(getApplicationContext(), spin_area.getSelectedItem().toString());
                            Utility.saveemail(getApplicationContext(), edt_email.getText().toString());
                            Utility.savemobileno(getApplicationContext(), edt_mobile.getText().toString());

                            Utility.savecountryid(getApplicationContext(), countryid);
                            Utility.savecityid(getApplicationContext(), cityid);
                            Utility.saveareaid(getApplicationContext(), areaid);

                            Utility.savepincode(getApplicationContext(),edt_pincode.getText().toString());
                         /*   UpdateShippingAddress task=new UpdateShippingAddress();
                            task.execute();*/
                            Intent intent=new Intent(getApplicationContext(),EcommOrderSummaryActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }

                }

                else
                {
                    if(Utility.isInternetConnected(getApplicationContext()))
                    {
/*
                        UpdateShippingAddress task=new UpdateShippingAddress();
                        task.execute();*/
                        Utility.saveaddr1(getApplicationContext(), edt_addrline1.getText().toString());
                        Utility.saveaddr2(getApplicationContext(), edt_addrline2.getText().toString());
                        Utility.savecountry(getApplicationContext(), spin_country.getSelectedItem().toString());
                        Utility.savecity(getApplicationContext(), city1);
                        Utility.savearea(getApplicationContext(), spin_area.getSelectedItem().toString());
                        Utility.saveemail(getApplicationContext(), edt_email.getText().toString());
                        Utility.savemobileno(getApplicationContext(), edt_mobile.getText().toString());

                        Utility.savecountryid(getApplicationContext(), countryid);
                        Utility.savecityid(getApplicationContext(), cityid);
                        Utility.saveareaid(getApplicationContext(),areaid);

                        Utility.savepincode(getApplicationContext(),edt_pincode.getText().toString());
                         /*   UpdateShippingAddress task=new UpdateShippingAddress();
                            task.execute();*/
                        Intent intent=new Intent(getApplicationContext(),EcommOrderSummaryActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }
        });

       /* spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spin_city.setAdapter(null);
                spin_area.setAdapter(null);
                countryid=countrylistID.get(position);
                if(Utility.isInternetConnected(getApplicationContext()))
                {
                    FetchCity task=new FetchCity();
                    task.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        spin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // spin_area.setAdapter(null);
                cityid=citylistID.get(position);
                city1 = citylist.get(position);

                if(Utility.isInternetConnected(getApplicationContext()))
                {
                    spin_area.setAdapter(null);
                    areaid = 0;
                    FetchArea task=new FetchArea();
                    task.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                areaid=arealistID.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    class FetchCountry extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommShippingAddressActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_COUNTRY);


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
                        countrylist=new ArrayList<>();
                        countrylistID=new ArrayList<>();
                        countrymobdigitslist=new ArrayList<>();
                        countrylist.add("Select Country *");
                        countrylistID.add(0);
                        countrymobdigitslist.add(0);
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

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_leadsrc, countrylist);
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                        spin_country.setAdapter(spinnerAdapter);

                        if(country1.equalsIgnoreCase("")){

                        }else {
                            System.out.println(country1);
                            spin_country.setEnabled(false);
                            for (int i = 0; i < countrylist.size(); i++) {

                                if (country1.equalsIgnoreCase(countrylist.get(i))) {
                                    spin_country.setSelection(i);
                                    countryid = countrylistID.get(i);
                                    mobiledigits = countrymobdigitslist.get(i);
                                    edt_mobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mobiledigits)});

                                    break;
                                }else{

                                }
                            }

                        }
                        if(countryid!=0) {
                            if (Utility.isInternetConnected(getApplicationContext())) {
                                FetchCity task = new FetchCity();
                                task.execute();
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

    class FetchSummary extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommShippingAddressActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETORDERSUMMARY);

            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));
            request.addProperty("AreaID", 0);


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
                            + Utility.GETORDERSUMMARY, mySoapEnvelop);


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


                        //customerID.clear();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);


                            addrline1=soapResult.getPrimitivePropertySafelyAsString("ShippingAddressLine1")
                                    .toString();
                            addrline2=soapResult.getPrimitivePropertySafelyAsString("ShippingAddressLine2")
                                    .toString();
                            city1=soapResult.getPrimitivePropertySafelyAsString("ShippingCity")
                                    .toString();
                            state1=soapResult.getPrimitivePropertySafelyAsString("ShippingState")
                                    .toString();
                            pincode1=soapResult.getPrimitivePropertySafelyAsString("ShippingPincode")
                                    .toString();
                            country1 = soapResult.getPrimitivePropertySafelyAsString("ShippingCountry")
                                    .toString();


                        }





                        edt_addrline1.setText(addrline1);
                        edt_addrline2.setText(addrline2);
                        edt_pincode.setText(pincode1);
                        spin_country.setEnabled(false);
                        if(Utility.isInternetConnected(getApplicationContext()))
                        {
                            FetchCountry task=new FetchCountry();
                            task.execute();
                        }

                    } else {
                        spin_country.setEnabled(false);
                        if(Utility.isInternetConnected(getApplicationContext()))
                        {
                            FetchCountry task=new FetchCountry();
                            task.execute();
                        }
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                    spin_country.setEnabled(false);
                    if(Utility.isInternetConnected(getApplicationContext()))
                    {
                        FetchCountry task=new FetchCountry();
                        task.execute();
                    }
                }
            }else {
                spin_country.setEnabled(false);
                if(Utility.isInternetConnected(getApplicationContext()))
                {
                    FetchCountry task=new FetchCountry();
                    task.execute();
                }
                Toast.makeText(EcommShippingAddressActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    class UpdateShippingAddress extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommShippingAddressActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.UPDATE_SHIPINGADDRESS);
            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));
            request.addProperty("AddressLine1", edt_addrline1.getText().toString());
            request.addProperty("AddressLine2",edt_addrline2.getText().toString());
            request.addProperty("Area",areaid+"");
            request.addProperty("City", cityid + "");
            request.addProperty("PinCode",edt_pincode.getText().toString());
            request.addProperty("Country",countryid+"");
            request.addProperty("emailID",edt_email.getText().toString());
            request.addProperty("PhoneNum",edt_mobile.getText().toString());

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.UPDATE_SHIPINGADDRESS);

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
                            + Utility.UPDATE_SHIPINGADDRESS, mySoapEnvelop);


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

                      /*  Toast.makeText(getApplicationContext(),
                                "Address Updated Successfully",
                                Toast.LENGTH_LONG).show();*/
                        ////////////////////////

                        Intent intent=new Intent(getApplicationContext(),EcommOrderSummaryActivity.class);
                        startActivity(intent);
                        finish();


                        /////////////////////////

                    /*    thingToBuy = new PayPalPayment(new BigDecimal("1"), "USD",
                                "Payment", PayPalPayment.PAYMENT_INTENT_SALE);
                        Intent intent = new Intent(EcommOrderSummaryActivity.this,
                                PaymentActivity.class);
                        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                        startActivityForResult(intent, REQUEST_CODE_PAYMENT);*/

                      /*  Intent intent=new Intent(getApplicationContext(), Main2Activity.class);
                        startActivity(intent);*/

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
            }else {

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
            progress = new ProgressDialog(EcommShippingAddressActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETCITY);


            request.addProperty("CountryID", countryid);



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
                        citylist=new ArrayList<>();
                        citylistID=new ArrayList<>();
                        citylist.add("Select City *");
                        citylistID.add(0);
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            citylist.add(soapResult.getPropertySafelyAsString("CityName", "")
                                    .toString());

                            citylistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_leadsrc, citylist);
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                        spin_city.setAdapter(spinnerAdapter);

                        if(city1.equalsIgnoreCase("")){

                        }else {
                            System.out.println(city1);
                            for (int i = 0; i < citylist.size(); i++) {

                                if (city1.equalsIgnoreCase(citylist.get(i))) {
                                    spin_city.setSelection(i);
                                    cityid = citylistID.get(i);

                                    break;
                                }else{

                                }
                            }


                        }
                        if(cityid !=0){
                            if(Utility.isInternetConnected(getApplicationContext()))
                            {
                                areaid = 0;
                                FetchArea task=new FetchArea();
                                task.execute();
                            }
                        }


                    } else {
                        spin_city.setAdapter(null);
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
            progress = new ProgressDialog(EcommShippingAddressActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETAREA);



            request.addProperty("CityID",cityid);



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
                        arealist=new ArrayList<>();
                        arealistID=new ArrayList<>();
                        arealist.add("Select Area *");
                        arealistID.add(0);
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            arealist.add(soapResult.getPropertySafelyAsString("AreaName", "")
                                    .toString());

                            arealistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_leadsrc, arealist);
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                        spin_area.setAdapter(spinnerAdapter);

                        if(state1.equalsIgnoreCase("")){

                        }else{

                            if(arealist.size() >0) {
                                for (int i = 0; i < arealist.size(); i++) {

                                    if (state1.equalsIgnoreCase(arealist.get(i))) {
                                        areaid = arealistID.get(i);
                                        spin_area.setSelection(i);
                                        break;
                                    }
                                }
                            }

                        }



                    } else {
                        spin_area.setAdapter(null);
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

        if(Utility.getfromwhichordersummary(getApplicationContext())==1)
        {
            Intent intent=new Intent(getApplicationContext(),Ecomm_MyCart.class);
            startActivity(intent);
            finish();        }
        else if(Utility.getfromwhichordersummary(getApplicationContext())==2)
        {
            Intent intent=new Intent(getApplicationContext(),EcommProductSpecificationActivity.class);
            startActivity(intent);
            finish();
        }else if(Utility.getfromwhichordersummary(getApplicationContext())==3)
        {
            Intent intent=new Intent(getApplicationContext(),EcommOffersProductSpecificationActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(getApplicationContext(),EcommDashboardActivity.class);
            startActivity(intent);
            finish();
        }
      /*  Intent intent=new Intent(getApplicationContext(),Ecomm_MyCart.class);
        startActivity(intent);
        finish();*/
    }
}
