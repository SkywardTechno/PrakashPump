package skyward.pp.ecommerce;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.ActionBar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import skyward.pp.R;
import skyward.pp.adapter.OrderSummaryAdapter;
import skyward.pp.model.OrderSummaryClass;
import skyward.pp.util.Utility;

public class EcommOrderSummaryActivity extends NavigationHeader {
RadioButton radiobutton;
   RadioGroup radiogrp;
    TextView termstext;
    Dialog dialog;
    Button os_btn_confirm;
    int totalitemcount=0;
    int finalorderid=0;
    int totalordercnt=0;
    int ordercnt=0;
    int taskorderid,taskproductid,taskqty;
    int taskfreeqty=0,taskdiscount=0;
    double taskdiscountamt=0.0;
    double taskprice;
    TextView os_shippingname, os_total,os_shippingcharges;
    ListView listordersummary;
    ArrayList<OrderSummaryClass> listcart;
    Double grandtotal=0.0;
    String addrline1="",addrline2="",city1="",state1="",pincode1="",country1="";
    int discount1=0,freeqty1=0;
    double discountamt1=0.0;
    Button btn_oredr;
    double shippingcharges=0.0;
    TextView txtaddress;
    private ArrayList<String> countrylist = new ArrayList<String>();
    private ArrayList<Integer> countrylistID = new ArrayList<Integer>();
    private ArrayList<String> citylist = new ArrayList<String>();
    private ArrayList<Integer> citylistID = new ArrayList<Integer>();
    private ArrayList<String> arealist = new ArrayList<String>();
    private ArrayList<Integer> arealistID = new ArrayList<Integer>();
    int countryid=0;
    int cityid=0;
    int areaid=0;
    String currency = "";
    //  private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;//sandbox

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;//live
    // note that these credentials will differ between live & sandbox
// environments.
    //  private static final String CONFIG_CLIENT_ID = "AV_fE3PCWmvaQPIY32MhdOxWEOWrA0-tSQVd4Xz8JzjeGOAzEQHHaAKXewFNTCVQxA4k74dPZhr42a9K";//sandbox
    private static final String CONFIG_CLIENT_ID="Ac97owzkS-LzJDNfgs2nP_F548agaL2Rj8XGVMWoPzbMkEMKvLe7_rrOg0jD5Xf5ApCWfQWpwv0_UIlX";//live
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
// the following are only used in PayPalFuturePaymentActivity.
            .merchantName("Hipster Store")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.example.com/legal"));
                 PayPalPayment thingToBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_ecomm_ordersummary, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Order Summary");
        ab.setHomeButtonEnabled(true);
        os_btn_confirm = (Button) findViewById(R.id.btn_os_confirm);
        /*os_shippingaddress = (TextView) findViewById(R.id.os_shippingaddress);
        os_estimatedtime = (TextView) findViewById(R.id.os_estimateddelivery);
        os_shippingcharges = (TextView) findViewById(R.id.os_shippingcharges);
        os_grandtotal = (TextView) findViewById(R.id.os_grandtotal);*/
        os_total = (TextView) findViewById(R.id.txtx_os_total);
        os_shippingname = (TextView) findViewById(R.id.txt_os_shippingname);
        os_shippingcharges = (TextView) findViewById(R.id.os_shippingcharges);
/*        address1 = (EditText) findViewById(R.id.edt_os_addressline1);
        address2 = (EditText) findViewById(R.id.edt_os_addressline2);
        state = (EditText) findViewById(R.id.edt_os_address_state);*/
        txtaddress = (TextView) findViewById(R.id.txt_eos_address);

       /* pincode = (EditText) findViewById(R.id.edt_os_address_pincode);
        city = (EditText) findViewById(R.id.edt_os_address_city);
        country = (EditText) findViewById(R.id.edt_os_address_country);*/
        listordersummary = (ListView) findViewById(R.id.listordersummary);
       radiogrp= (RadioGroup) findViewById(R.id.payment_rg);


        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        if(Utility.isInternetConnected(getApplicationContext()))
        {
            FetchSummary task=new FetchSummary();
            task.execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

        }
        os_shippingname.setText(Utility.getName(getApplicationContext()));
        os_btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog = new Dialog(EcommOrderSummaryActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.tc_popup);
                dialog.setCancelable(false);

                // set the custom dialog components - text, image and button
                termstext = (TextView) dialog.findViewById(R.id.tcdetails);


                TextView btnagree = (TextView) dialog.findViewById(R.id.btn_agree);
                TextView btndisagree = (TextView) dialog.findViewById(R.id.btn_disagree);
                if (!Utility.isInternetConnected(EcommOrderSummaryActivity.this)) {

                    Toast.makeText(EcommOrderSummaryActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                } else {
                    new FetchServiceDetails().execute();

                }

                btnagree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();


                        // get selected radio button from radioGroup
                        int selectedId = radiogrp.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        radiobutton = (RadioButton) findViewById(selectedId);


                        if(radiobutton.getId()==R.id.payment_cc)
                        {

                            thingToBuy = new PayPalPayment(new BigDecimal("1"), "USD",
                                    "Payment", PayPalPayment.PAYMENT_INTENT_SALE);
                            Intent intent = new Intent(EcommOrderSummaryActivity.this,
                                    PaymentActivity.class);
                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                            startActivityForResult(intent, REQUEST_CODE_PAYMENT);


//                            Toast.makeText(getApplicationContext(),"cash o deleviey selecetd",Toast.LENGTH_LONG).show();
                        }
                        else if(radiobutton.getId()==R.id.payment_dc)
                        {

                            if (Utility.isInternetConnected(getApplicationContext())) {
                                InsertMyOrderTask task = new InsertMyOrderTask();
                                task.execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

                            }

//                            Toast.makeText(getApplicationContext(),"pay pal",Toast.LENGTH_LONG).show();

                        }



                    }
                });

                btndisagree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();


            }
        });
    }




    class FetchServiceDetails extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommOrderSummaryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_TCBYSERVICE);

            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));
            request.addProperty("serviceTypeID", 0);


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
            if(progress.isShowing()) {
                progress.dismiss();
            }
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
                            termstext.setText(TC);
                            termstext.setMovementMethod(new ScrollingMovementMethod());
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

                Toast.makeText(EcommOrderSummaryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
            progress = new ProgressDialog(EcommOrderSummaryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETORDERSUMMARY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("AreaID", Utility.getareaid(getApplicationContext()));

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GETORDERSUMMARY);

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

                        listcart=new ArrayList<>();
                        //customer.clear();
                        //customerID.clear();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);

                            String productname1 =soapResult.getPrimitivePropertySafelyAsString("productName")
                                    .toString();
                            String productcode=soapResult.getPrimitivePropertySafelyAsString("productCode")
                                    .toString();
                            currency=soapResult.getPrimitivePropertySafelyAsString("Currency")
                                    .toString();


                            String modelno=soapResult.getPrimitivePropertySafelyAsString("ModelNum")
                                    .toString();
                            int quantity= Integer.parseInt(soapResult.getPrimitivePropertySafelyAsString("Quantity")
                                    .toString());
                            totalitemcount+=quantity;

                             shippingcharges=Double.valueOf(soapResult.getPropertySafelyAsString("ShippingCharge","0")
                                    .toString());
                            double totalprice=Double.valueOf(soapResult.getPrimitivePropertySafelyAsString("TotalPrice")
                                    .toString());
                            String price=soapResult.getPrimitivePropertySafelyAsString("Price")
                                    .toString();
                            String imagepath=soapResult.getPrimitivePropertySafelyAsString("imagePath")
                                    .toString();
                          /*  addrline1=soapResult.getPrimitivePropertySafelyAsString("ShippingAddressLine1")
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
                                    .toString();*/

                            freeqty1=Integer.valueOf(soapResult.getPropertySafelyAsString("FreeQuantity","0")
                                    .toString());
                            discount1=Integer.valueOf(soapResult.getPropertySafelyAsString("Discount","0")
                                    .toString());
                            discountamt1=Double.valueOf(soapResult.getPropertySafelyAsString("DiscountAmount","0")
                                    .toString());
                            if(soapResult.hasProperty("GrandTotal")) {
                                grandtotal = Double.valueOf(soapResult.getPrimitivePropertySafelyAsString("GrandTotal")
                                        .toString());
                            }else{
                                grandtotal = 0.0;
                            }

                            int productid=Integer.parseInt(soapResult.getPropertySafelyAsString("ProductID","0"));
                            ///need to change available qty
                            listcart.add(new OrderSummaryClass(productname1,imagepath,modelno,productcode,quantity,Double.valueOf(price),shippingcharges,totalprice,productid, currency,freeqty1,discount1,discountamt1));

                        }


                        addrline1= Utility.getaddr1(getApplicationContext());
                        addrline2= Utility.getaddr2(getApplicationContext());
                        city1= Utility.getcity(getApplicationContext());
                        state1 = Utility.getarea(getApplicationContext());
                        country1= Utility.getcountry(getApplicationContext());
                        pincode1=Utility.getpincode(getApplicationContext());
                        if(addrline1.isEmpty()){

                        }else{
                            addrline1 = addrline1 +",";
                        }
                        if(addrline2.isEmpty()){

                        }else{
                            addrline2 = "\n"+addrline2 +",";
                        }
                        if(pincode1.isEmpty()){

                        }else{
                            pincode1 = "\n"+pincode1 +",";
                        }
                        String finaladress=addrline1+addrline2+"\n"+state1+","+"\n"+city1+","+pincode1+"\n"+country1;


                        Utility.saveshippingaddress(getApplicationContext(), finaladress);
                        Utility.saveordercurrency(getApplicationContext(), currency);
                        Utility.savetotal(getApplicationContext(),grandtotal+"");

                        //shippingcharge.settext(shppingcharges);
                       txtaddress.setText(finaladress);
                        os_total.setText(grandtotal+" " +currency);
                        os_shippingcharges.setText(String.valueOf(shippingcharges)+" "+currency);

                        if(listcart.size()>0) {

                            listordersummary.setAdapter(new OrderSummaryAdapter(getApplicationContext(), listcart));
                            setListViewHeightBasedOnChildrenproducts(listordersummary);
                        }

                        new FetchCountry().execute();

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

                Toast.makeText(EcommOrderSummaryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }





    class InsertMyOrderTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommOrderSummaryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.INSERTMYORDER);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("TotalQuantity",totalitemcount);
            request.addProperty("TotalPrice",grandtotal+"");
            request.addProperty("OrderStatus",0);
            if(addrline1.trim().endsWith(",")){
                addrline1 = addrline1.substring(0,addrline1.length()-1);
            }
            if(addrline2.trim().endsWith(",")){
                addrline2 = addrline2.substring(0,addrline2.length()-1);
            }
            if(pincode1.trim().endsWith(",")){
                pincode1 = pincode1.substring(0,pincode1.length()-1);
            }
            request.addProperty("ShippingAddressLine1", Utility.getaddr1(getApplicationContext()));
            request.addProperty("ShippingAddressLine2",Utility.getaddr2(getApplicationContext()));
            request.addProperty("ShippingState",Utility.getareaid(getApplicationContext())+"");
            request.addProperty("shippingCity",Utility.getcityid(getApplicationContext())+"");
            request.addProperty("ShippingPincode",Utility.getpincode(getApplicationContext()));
            request.addProperty("ShippingCountry", Utility.getcountryid(getApplicationContext())+"");
            request.addProperty("PhoneNum", Utility.getmobileno(getApplicationContext()));

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.INSERTMYORDER);

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
                            + Utility.INSERTMYORDER, mySoapEnvelop);


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

                        finalorderid=Integer.parseInt(soapObject.getPropertySafelyAsString("ID", "0"));
                        String finalordercode=soapObject.getPropertySafelyAsString("OrderCode", "");
                        Utility.saveorderidorder(getApplicationContext(), finalordercode);
                        totalordercnt=listcart.size();
                        if(totalordercnt>0)
                        {
                                OrderSummaryClass cls=listcart.get(ordercnt);
                                taskorderid=finalorderid;
                                taskprice=cls.getGrandtotal();
                                taskproductid=cls.getProductid();
                                taskqty=cls.getQuantity();
                                taskfreeqty=cls.getFreeqty();
                                taskdiscount=cls.getDiscount();
                                 taskdiscountamt=cls.getDiscountamt();
                                InsertMyOrderTaskDetails task=new InsertMyOrderTaskDetails();
                                task.execute();
                                //enter code here

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
            }else {

                Toast.makeText(EcommOrderSummaryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class InsertMyOrderTaskDetails extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommOrderSummaryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.INSERTMYORDERDETAILS);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("Quantity",taskqty);
            request.addProperty("Price",taskprice+"");
            request.addProperty("ProductID",taskproductid);
            request.addProperty("ServiceID",0);
            request.addProperty("OrderID", taskorderid);
            request.addProperty("FreeQuantity", taskfreeqty);
            request.addProperty("Discount", taskdiscount);
            request.addProperty("DiscountAmount", taskdiscountamt+"");

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.INSERTMYORDERDETAILS);

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
                            + Utility.INSERTMYORDERDETAILS, mySoapEnvelop);


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
            ordercnt++;
            progress.dismiss();
            if(result != null) {
                try {
                    SoapObject soapObject = (SoapObject) result.getProperty(0);
                    System.out.println(soapObject.getProperty("IsSucceed"));
                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {


                        totalordercnt=listcart.size();

                            if(ordercnt<totalordercnt)
                            {
                                OrderSummaryClass cls=listcart.get(ordercnt);
                                taskorderid=finalorderid;
                                taskprice=cls.getGrandtotal();
                                taskproductid=cls.getProductid();
                                taskqty=cls.getQuantity();
                                taskfreeqty=cls.getFreeqty();
                                taskdiscount=cls.getDiscount();
                                taskdiscountamt=cls.getDiscountamt();
                                InsertMyOrderTaskDetails task=new InsertMyOrderTaskDetails();
                                task.execute();
                                //enter code here
                            }

                        if(ordercnt==totalordercnt)
                        {
                            ordercnt=0;
                         /*   Toast.makeText(getApplicationContext(),
                                    "order placed sucessfully",
                                    Toast.LENGTH_LONG).show();*/
                            Intent intent=new Intent(getApplicationContext(),Ecomm_ConfirmationActivity.class);
                            startActivity(intent);
                            finish();
                        }



                    } else {     Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }else {

                Toast.makeText(EcommOrderSummaryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public  void setListViewHeightBasedOnChildrenproducts(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + 100;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(Utility.isInternetConnected(getApplicationContext()))
        {
            DeleteOrderSummery task=new DeleteOrderSummery();
            task.execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

        }
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

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject()
                                .toString(4));
                      /*  Toast.makeText(getApplicationContext(), "Order placed",
                                Toast.LENGTH_LONG).show();
*/


                        if (Utility.isInternetConnected(getApplicationContext())) {
                            InsertMyOrderTask task = new InsertMyOrderTask();
                            task.execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            System.out.println("The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            System.out
                    .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
        else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));
                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);
                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(),
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {
    }
    public void onFuturePaymentPurchasePressed(View pressed) {
// Get the Application Correlation ID from the SDK
        String correlationId = PayPalConfiguration
                .getApplicationCorrelationId(this);
        Log.i("FuturePaymentExample", "Application Correlation ID: "
                + correlationId);
// TODO: Send correlationId and transaction details to your server for
// processing with
// PayPal...
        Toast.makeText(getApplicationContext(),
                "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }
    @Override
    public void onDestroy() {
// Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    class DeleteOrderSummery extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");

        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.DELETEORDERSUMMAARY);
            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));


            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.DELETEORDERSUMMAARY);

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
                            + Utility.DELETEORDERSUMMAARY, mySoapEnvelop);


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
                try {
                    SoapObject soapObject = (SoapObject) result.getProperty(0);
                    System.out.println(soapObject.getProperty("IsSucceed"));
                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {




                    } else {
                        Toast.makeText(EcommOrderSummaryActivity.this,
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
            progress = new ProgressDialog(EcommOrderSummaryActivity.this);
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
                        countrylist.add("Select Country *");
                        countrylistID.add(0);
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            countrylist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());

                            countrylistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }



                        if(country1.equalsIgnoreCase("")){

                        }else {
                            System.out.println(country1);
                            for (int i = 0; i < countrylist.size(); i++) {

                                if (country1.equalsIgnoreCase(countrylist.get(i))) {

                                    countryid = countrylistID.get(i);

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

    class FetchCity extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommOrderSummaryActivity.this);
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


                        if(city1.equalsIgnoreCase("")){

                        }else {
                            System.out.println(city1);
                            for (int i = 0; i < citylist.size(); i++) {

                                if (city1.equalsIgnoreCase(citylist.get(i))) {

                                    cityid = citylistID.get(i);

                                    break;
                                }else{

                                }
                            }


                        }
                        if(cityid !=0){
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
            progress = new ProgressDialog(EcommOrderSummaryActivity.this);
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



                        if(state1.equalsIgnoreCase("")){

                        }else{

                            if(arealist.size() >0) {
                                for (int i = 0; i < arealist.size(); i++) {

                                    if (state1.equalsIgnoreCase(arealist.get(i))) {
                                        areaid = arealistID.get(i);

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

}
