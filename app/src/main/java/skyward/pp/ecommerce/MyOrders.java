package skyward.pp.ecommerce;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import skyward.pp.adapter.MyorderlistAdapter;
import skyward.pp.model.MyorderClass;
import skyward.pp.util.Utility;

public class MyOrders extends NavigationHeader {

    ArrayList<MyorderClass> arrlistorders;
    ListView listmyorders;
    ArrayList<String> arrmain;
    String orderID="";
    int totalcnt = 0;
    int oid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_myorders, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("My Orders");
        ab.setHomeButtonEnabled(true);

        listmyorders  = (ListView) findViewById(R.id.listmyorders);
        arrlistorders=new ArrayList<>();

        if(Utility.isInternetConnected(getApplicationContext()))
        {
            FetchOrderHistory task=new FetchOrderHistory();
            task.execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

        }

        arrmain = new ArrayList<>();
        if(Utility.getorderstatusnoti(getApplicationContext()).isEmpty()){

        }else{
            arrmain = Utility.getorderstatusnoti(getApplicationContext());
            int cnt = arrmain.size();
            totalcnt = arrmain.size();

            if(cnt > 0){

                    orderID = arrmain.get(oid);
                    updatenotistatus task = new updatenotistatus();
                    task.execute();

            }
        }

        listmyorders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyorderClass cls=arrlistorders.get(position);
                int orderid=cls.getOrderid();
                double price=cls.getPrice();
                String statusname = cls.getStatus();
                String currency = cls.getCurrency();
                Utility.saveOrderId(getApplicationContext(),orderid);
                Utility.savemyordergrandtotal(getApplicationContext(),price+"");
                Utility.saveorderstatus(getApplicationContext(),statusname+"");
                Utility.saveordercurrency(getApplicationContext(),currency+"");
                Intent intent=new Intent(getApplicationContext(),Ecomm_MyOrders_Details.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),EcommDashboardActivity.class
        );
        startActivity(intent);
        finish();
    }



    class FetchOrderHistory extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(MyOrders.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETORDERHISTORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GETORDERHISTORY);

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
                            + Utility.GETORDERHISTORY, mySoapEnvelop);


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

                        arrlistorders=new ArrayList<>();
                        //customer.clear();
                        //customerID.clear();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);

                            int orderid=Integer.parseInt(soapResult.getPropertySafelyAsString(

                                    "OrderID", "0").toString());



                            double orderprice=Double.valueOf(soapResult.getPropertySafelyAsString(

                                    "TotalPrice", "0").toString());


                            String ordercode=soapResult.getPropertySafelyAsString(

                                    "OrderCode","").toString();

                            String orderdate=soapResult.getPropertySafelyAsString(

                                    "OrderDate","").toString();
                            String status=soapResult.getPropertySafelyAsString(

                                    "OrderStatusName","").toString();

                            String currency=soapResult.getPropertySafelyAsString(

                                    "Currency","").toString();
                            String []arrtempdate=orderdate.split("-");
                            String year=arrtempdate[0];
                            String month=arrtempdate[1];
                            String daytmp=arrtempdate[2];
                            String finalday=daytmp.substring(0,2);
                            String finaldate=finalday+"-"+month+"-"+year;
                            orderdate=finaldate;
///need to change available qty
                            arrlistorders.add(new MyorderClass(ordercode,orderdate,orderprice,orderid,status,currency));
                        }

                        if(arrlistorders.size()>0) {
                            listmyorders.setAdapter(new MyorderlistAdapter(getApplicationContext(), arrlistorders));
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

                Toast.makeText(MyOrders.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class updatenotistatus extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(MyOrders.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.UPDATE_ORDERSTATUS);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("OrderID",orderID);

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.UPDATE_ORDERSTATUS);

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
                            + Utility.UPDATE_ORDERSTATUS, mySoapEnvelop);


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
                oid++;
                try {
                    SoapObject soapObject = (SoapObject) result.getProperty(0);
                    System.out.println(soapObject.getProperty("IsSucceed"));
                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

                        if(oid < totalcnt) {
                            orderID = arrmain.get(oid);
                            updatenotistatus task = new updatenotistatus();
                            task.execute();
                        }
                        if(oid==totalcnt){

                            arrmain = new ArrayList<>();
                            Utility.saveorderstatusnoti(getApplicationContext(),arrmain);
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

                Toast.makeText(MyOrders.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
