package skyward.pp;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sinch.android.rtc.calling.Call;

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
import skyward.pp.BaseActivity;
import skyward.pp.CallScreenActivity;
import skyward.pp.DashboardActivity;
import skyward.pp.PumpDetailActivity;
import skyward.pp.R;
import skyward.pp.SinchService;

public class ViewProductActivity extends BaseActivity {

    int ProductID;
    int powerID,flowrateID,headID,producttypeID,typeID;
    int send_producttype =0,send_type=0,send_flowrate=0,send_inlet=0,send_outlet=0,send_volt=0;
    Double send_head=0.0,send_headfeet=0.0,send_power=0.0,send_powerhp=0.0;
    TextView view_productname,view_modelno,view_producttype,view_power,view_inletoutlet,view_productdescription,view_type,view_head,view_flowrate,view_volt;
    ImageView view_showimg;
    Button view_btn_showvideo,view_btn_sendinquiry,view_btn_call;
    String namevideo,nameimg, Text, Required,send_Text,send_Required;
    String productname,model_no,productype,power,productdescriptiion,type,head,flowrate,imagepath,finalpathimg,videopath,finalpathvideo,inlet,outlet,powerhp,headfeet,volt;
    private ArrayList<String> powerlist = new ArrayList<String>();
    private ArrayList<Integer>  powerlistid = new ArrayList<Integer>();
    private ArrayList<String> headlist = new ArrayList<String>();
    private ArrayList<Integer>  headlistid = new ArrayList<Integer>();
    private ArrayList<String> flowratelist = new ArrayList<String>();
    private ArrayList<Integer>  flowratelistid = new ArrayList<Integer>();
    private ArrayList<String> producttypelist = new ArrayList<String>();
    private ArrayList<Integer>  producttypelistid = new ArrayList<Integer>();
    private ArrayList<String> typelist = new ArrayList<String>();
    private ArrayList<Integer>  typelistid = new ArrayList<Integer>();
    private ArrayList<String> powerhplist = new ArrayList<String>();
    private ArrayList<Integer>  powerhplistid = new ArrayList<Integer>();
    private ArrayList<String> inletlist = new ArrayList<String>();
    private ArrayList<Integer>  inletlistid = new ArrayList<Integer>();
    private ArrayList<String> outletlist = new ArrayList<String>();
    private ArrayList<Integer>  outletlistid = new ArrayList<Integer>();
    private ArrayList<String> headfeetlist = new ArrayList<String>();
    private ArrayList<Integer>  headfeetlistid = new ArrayList<Integer>();
    private ArrayList<String> voltlist = new ArrayList<String>();
    private ArrayList<Integer>  voltlistid = new ArrayList<Integer>();
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewproduct);

        setTitle("View Product");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("View Product");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ViewProductActivity.this, ProductsListActivity.class);
                Bundle b = new Bundle();
                b.putDouble("Power", send_power);
                b.putInt("FlowRate", send_flowrate);
                b.putDouble("Head", send_head);
                b.putInt("ProductType", send_producttype);
                b.putInt("Type", send_type);
                b.putInt("Inlet", send_inlet);
                b.putInt("Outlet", send_outlet);
                b.putDouble("HeadFeet", send_headfeet);
                b.putDouble("PowerHP", send_powerhp);
                b.putInt("Volt", send_volt);
                b.putInt("productID", Integer.valueOf(producttypeID));
                b.putString("TcDetails", send_Text);
                b.putString("required", send_Required);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });

        view_productname = (TextView) findViewById(R.id.view_productname);
        view_modelno = (TextView) findViewById(R.id.view_modelno);
        view_producttype = (TextView) findViewById(R.id.view_producttype);
        view_power = (TextView) findViewById(R.id.view_power);
        view_type = (TextView) findViewById(R.id.view_type);
        view_head = (TextView) findViewById(R.id.view_head);
        view_flowrate = (TextView) findViewById(R.id.view_flowrate);
        view_productdescription = (TextView) findViewById(R.id.viewdetails_product_description);
        view_inletoutlet = (TextView) findViewById(R.id.view_inletoutlet);
        view_volt = (TextView) findViewById(R.id.view_volt);
        view_showimg = (ImageView) findViewById(R.id.view_productimg);
        view_btn_call = (Button) findViewById(R.id.vp_btn_call);

        Bundle b = getIntent().getExtras();
        send_power=b.getDouble("Power");
        send_flowrate=b.getInt("FlowRate");
        send_head=b.getDouble("Head");
        send_producttype=b.getInt("ProductType");
        send_type=b.getInt("Type");

        send_inlet=b.getInt("Inlet");

        send_outlet=b.getInt("Outlet");

        send_headfeet=b.getDouble("HeadFeet");

        send_powerhp=b.getDouble("PowerHP");
        send_volt=b.getInt("Volt");

        producttypeID = getIntent().getIntExtra("productID", 0);
        send_Required = getIntent().getStringExtra("required");
        send_Text = getIntent().getStringExtra("TcDetails");


        view_btn_sendinquiry = (Button) findViewById(R.id.view_btn_sendinquiry);
        view_btn_showvideo = (Button) findViewById(R.id.view_btn_video);
        view_btn_sendinquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewProductActivity.this, PumpSelectionInquiryActivity.class);
                i.putExtra("productID", Integer.valueOf(producttypeID));
                i.putExtra("Required", send_Required);
                i.putExtra("TcDetails", send_Text);
                i.putExtra("productname", productname);
                i.putExtra("modelno", model_no);
                startActivity(i);
                finish();
            }
        });

        view_btn_showvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PlayVideoActivity.class);
                Bundle b = new Bundle();
                b.putDouble("powerID", send_power);
                b.putInt("flowrateID", send_flowrate);
                b.putDouble("headID", send_head);
                b.putInt("producttypeID", send_producttype);
                b.putInt("typeID", send_type);
                b.putInt("Inlet", send_inlet);
                b.putInt("Outlet", send_outlet);
                b.putDouble("HeadFeet", send_headfeet);
                b.putDouble("PowerHP", send_powerhp);
                b.putInt("Volt", send_volt);
                b.putInt("productid", Integer.valueOf(producttypeID));
                b.putString("TcDetails", send_Text);
                b.putString("required", send_Required);
                b.putString("productname", productname);
                b.putString("videopath", videopath);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });
        new FetchFlowRate().execute();

        new FetchInlet().execute();
        new FetchOutlet().execute();

        new FetchVolt().execute();

        view_btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String callusername= "Admin";
                    Call call = getSinchServiceInterface().callUserVideo(callusername);
                    String callId = call.getCallId();
                    Utility.savefromviewproduct(getApplicationContext(),"true");
                    Intent callScreen = new Intent(getApplicationContext(), CallScreenActivity.class);
                    callScreen.putExtra(SinchService.CALL_ID, callId);
                    Bundle b = new Bundle();
                    b.putDouble("powerID", send_power);
                    b.putInt("flowrateID", send_flowrate);
                    b.putDouble("headID", send_head);
                    b.putInt("producttypeID", send_producttype);
                    b.putInt("typeID", send_type);
                    b.putInt("Inlet", send_inlet);
                    b.putInt("Outlet", send_outlet);
                    b.putDouble("HeadFeet", send_headfeet);
                    b.putDouble("PowerHP", send_powerhp);
                    b.putInt("Volt", send_volt);
                    b.putInt("productid", Integer.valueOf(producttypeID));
                    b.putString("TcDetails", send_Text);
                    b.putString("required", send_Required);
                    b.putString("productname", productname);
                    b.putString("videopath", videopath);
                    callScreen.putExtras(b);
                    startActivity(callScreen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    class FetchProductDetailsTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
           /* progress = new ProgressDialog(ViewProductActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTBYPRODUCTID);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("productID", producttypeID);



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
                            + Utility.GET_PRODUCTBYPRODUCTID, mySoapEnvelop);


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

            if(result != null) {
                try {
                    progress.dismiss();
                }catch(Exception e){
                    e.printStackTrace();
                }
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
                        System.out.println("Result4 is : " + result4.toString());
                        int count = result4.getPropertyCount();
                        System.out.println("Count is : " + count);
                        productname = result5.getPrimitivePropertySafelyAsString("ProductName");
                        model_no = result5.getPrimitivePropertySafelyAsString("ModelNum");
                        productype = result5.getPrimitivePropertySafelyAsString("ProductType");
                        power = result5.getPrimitivePropertySafelyAsString("Power");
                        productdescriptiion = result5.getPrimitivePropertySafelyAsString("ProductDesc");
                        type = result5.getPrimitivePropertySafelyAsString("Type");
                        head = result5.getPrimitivePropertySafelyAsString("Head");
                        flowrate = result5.getPrimitivePropertySafelyAsString("FlowRate");
                        imagepath = result5.getPrimitivePropertySafelyAsString("ImagePath");
                        videopath = result5.getPrimitivePropertySafelyAsString("VideoPath");
                        nameimg = result5.getPrimitivePropertySafelyAsString("ImageName");
                        namevideo = result5.getPrimitivePropertySafelyAsString("VideoName");
                        inlet = result5.getPrimitivePropertySafelyAsString("Inlet");
                        outlet = result5.getPrimitivePropertySafelyAsString("Outlet");
                        powerhp = result5.getPrimitivePropertySafelyAsString("PowerHP");
                        headfeet = result5.getPrimitivePropertySafelyAsString("HeadFeet");
                        volt = result5.getPrimitivePropertySafelyAsString("Volt");

                        try {


                            if (flowrate.isEmpty() || flowrate.equalsIgnoreCase("anyType{}")) {

                            } else {
                                for (int i = 0; i < flowratelistid.size(); i++) {
                                    int t = Integer.valueOf(flowrate);
                                    if (t == flowratelistid.get(i)) {
                                        flowrate = flowratelist.get(i);
                                        break;

                                    }
                                }
                            }
                            if (productype.isEmpty() || productype.equalsIgnoreCase("anyType{}")) {

                            } else {
                                for (int i = 0; i < producttypelistid.size(); i++) {
                                    int t = Integer.valueOf(productype);
                                    if (t == producttypelistid.get(i)) {
                                        productype = producttypelist.get(i);
                                        break;

                                    }
                                }
                            }
                            if (type.equalsIgnoreCase("anyType{}") || type.isEmpty()) {

                            } else {
                                for (int i = 0; i < typelistid.size(); i++) {
                                    int t = Integer.valueOf(type);
                                    if (t == typelistid.get(i)) {
                                        type = typelist.get(i);
                                        break;

                                    }
                                }
                            }

                            if (inlet.isEmpty() || inlet.equalsIgnoreCase("anyType{}")) {

                            } else {
                                for (int i = 0; i < inletlistid.size(); i++) {
                                    int t = Integer.valueOf(inlet);
                                    if (t == inletlistid.get(i)) {
                                        inlet = inletlist.get(i);
                                        break;

                                    }
                                }
                            }
                            if (outlet.isEmpty() || outlet.equalsIgnoreCase("anyType{}")) {

                            } else {
                                for (int i = 0; i < outletlistid.size(); i++) {
                                    int t = Integer.valueOf(outlet);
                                    if (t == outletlistid.get(i)) {
                                        outlet = outletlist.get(i);
                                        break;

                                    }
                                }
                            }

                            if (volt.isEmpty() || volt.equalsIgnoreCase("anyType{}")) {

                            } else {
                                for (int i = 0; i < voltlistid.size(); i++) {
                                    int t = Integer.valueOf(volt);
                                    if (t == voltlistid.get(i)) {
                                        volt = voltlist.get(i);
                                        break;

                                    }
                                }
                            }

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        view_productdescription.setText(productdescriptiion);
                        view_flowrate.setText(flowrate);
                        view_head.setText(headfeet+" Feet"+" , "+head+" Meter");
                        view_modelno.setText(model_no);
                        view_power.setText(powerhp+" HP  , "+power+" KW");
                        view_inletoutlet.setText("Inlet :"+inlet+" , Outlet :"+outlet);
                        view_producttype.setText(productype);
                        view_type.setText(type);
                        if(volt.isEmpty()){
                            view_volt.setText("");
                        }else{
                            view_volt.setText(volt);
                        }

                        view_productname.setText(productname);
                        finalpathimg = Utility.URLFORIMAGE + imagepath;
                        Glide.with(getApplicationContext()).load(finalpathimg).into(view_showimg);

                        if(videopath.isEmpty() || videopath.equalsIgnoreCase("anyType{}")){
                            view_btn_showvideo.setVisibility(View.GONE);
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
                progress.dismiss();
                Toast.makeText(ViewProductActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }










    ///////////////////////get Flowrate task////////////////////////////////

    class FetchFlowRate extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(ViewProductActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);
            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","FlowRate");
            if(flowratelist.size() > 0){
                flowratelist.clear();
                flowratelistid.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_MISCDATAFROMCATEGORY);

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
                            + Utility.GET_MISCDATAFROMCATEGORY, mySoapEnvelop);


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
                        int count = result4.getPropertyCount();
                        System.out.println("Count is : " + count);

                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            flowratelist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());
                            flowratelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }
                       new FetchProductType().execute();


                    } else {

                        new FetchProductType().execute();
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    new FetchProductType().execute();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    new FetchProductType().execute();
                }
            }else{
                progress.dismiss();
                Toast.makeText(ViewProductActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////



    ////////////////////////////////get Head Taask/////////////////////////


    /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////get productype task/////////////////////////////////////////

    class FetchProductType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
           /* progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTTYPE);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
//            request.addProperty("category","ProductType");
            if(producttypelist.size() > 0){
                producttypelist.clear();
                producttypelistid.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_PRODUCTTYPE);

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
                            + Utility.GET_PRODUCTTYPE, mySoapEnvelop);


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
            // progress.dismiss();
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
                    producttypelist.add("Select ProductType");
                    producttypelistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        producttypelist.add(soapResult.getPropertySafelyAsString("ProductTypeName", "")
                                .toString());

                        producttypelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



                    }

                    FetchType task = new FetchType();
                    task.execute();

                } else {
                    FetchType task = new FetchType();
                    task.execute();

                    Toast.makeText(getApplicationContext(),
                            soapObject.getProperty("ErrorMessage").toString(),
                            Toast.LENGTH_LONG).show();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                FetchType task = new FetchType();
                task.execute();

            } catch (NullPointerException e) {
                e.printStackTrace();
                FetchType task = new FetchType();
                task.execute();

            }

        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////fetch Type TAsk///////////////////////


    class FetchType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Type");
            if(typelist.size() > 0){
                typelist.clear();
                typelistid.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_MISCDATAFROMCATEGORY);

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
                            + Utility.GET_MISCDATAFROMCATEGORY, mySoapEnvelop);


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

                        int count = result4.getPropertyCount();
                        System.out.println("Count is : " + count);
                        //customer.clear();
                        //customerID.clear();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            typelist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());

                            typelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }


                        if (!Utility.isInternetConnected(ViewProductActivity.this)) {

                            Toast.makeText(ViewProductActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 2000);
                        } else {
                            new FetchProductDetailsTask().execute();

                        }

                    } else {
                        if (!Utility.isInternetConnected(ViewProductActivity.this)) {

                            Toast.makeText(ViewProductActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 2000);
                        } else {
                            new FetchProductDetailsTask().execute();

                        }

                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    new FetchProductDetailsTask().execute();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    new FetchProductDetailsTask().execute();
                }

            }else{
progress.dismiss();
                Toast.makeText(ViewProductActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {

        Intent i=new Intent(ViewProductActivity.this,ProductsListActivity.class);

        Bundle b = new Bundle();
        b.putDouble("Power", send_power);
        b.putInt("FlowRate",send_flowrate);
        b.putDouble("Head", send_head);
        b.putInt("ProductType", send_producttype);
        b.putInt("Type", send_type);
        b.putInt("Inlet", send_inlet);
        b.putInt("Outlet", send_outlet);
        b.putDouble("HeadFeet", send_headfeet);
        b.putDouble("PowerHP", send_powerhp);
        b.putInt("Volt", send_volt);
        b.putInt("productID",Integer.valueOf(producttypeID));
        b.putString("TcDetails", send_Text);
        b.putString("required", send_Required);
        i.putExtras(b);
        startActivity(i);
        finish();

    }



    class FetchInlet extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Inlet");
            if(inletlistid.size() > 0){
                inletlistid.clear();
                inletlist.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_MISCDATAFROMCATEGORY);

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
                            + Utility.GET_MISCDATAFROMCATEGORY, mySoapEnvelop);


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
                    inletlist.add("Select Inlet");
                    inletlistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        inletlist.add(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());

                        inletlistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



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

        }
    }

    class FetchOutlet extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Outlet");
            if(outletlist.size() > 0){
                outletlist.clear();
                outletlistid.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_MISCDATAFROMCATEGORY);

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
                            + Utility.GET_MISCDATAFROMCATEGORY, mySoapEnvelop);


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
                    outletlist.add("Select Outlet");
                    outletlistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        outletlist.add(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());

                        outletlistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



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

        }
    }

    class FetchVolt extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Volt");
            if(voltlistid.size() > 0){
                voltlistid.clear();
                voltlist.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_MISCDATAFROMCATEGORY);

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
                            + Utility.GET_MISCDATAFROMCATEGORY, mySoapEnvelop);


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
                    voltlist.add("Select Volt");
                    voltlistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        voltlist.add(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());

                        voltlistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



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
    ///////////////////////////////////////////////
}
