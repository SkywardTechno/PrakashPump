package skyward.pp.ecommerce;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import skyward.pp.R;
import skyward.pp.adapter.MyorderdetailsAdapter;
import skyward.pp.model.MyordersDetailsClass;
import skyward.pp.util.Utility;

public class Ecomm_MyOrders_Details extends NavigationHeader {

    TextView statusname;
    Dialog dialog;
    Button btn_cancelorder;
    int totalitemcount=0;
    int finalorderid=0;
    int totalordercnt=0;
    int ordercnt=0;
    int taskorderid,taskproductid,taskqty;
    double taskprice;
    TextView os_shippingname, txt_grandtotal;
    ListView listmyorders;
    TextView txt_address;
    ArrayList<MyordersDetailsClass> listcart;
    Double grandtotal=0.0;
    String addrline1="",addrline2="",city1="",state1="",pincode1="",country1="", currency = "";
    Button btn_oredr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ecomm_myorders_details, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("My Order Details");
        ab.setHomeButtonEnabled(true);

        txt_grandtotal= (TextView) findViewById(R.id.txt_mo_total);
        txt_address= (TextView) findViewById(R.id.edt_mo_address);
        statusname= (TextView) findViewById(R.id.txt_mo_status);
        btn_cancelorder = (Button) findViewById(R.id.btn_cancelorder);
        listmyorders= (ListView) findViewById(R.id.listmyorders);

        if(Utility.getorderstatus(getApplicationContext()).equalsIgnoreCase("Cancelled") || Utility.getorderstatus(getApplicationContext()).equalsIgnoreCase("Completed")||Utility.getorderstatus(getApplicationContext()).equalsIgnoreCase("Dispatched")){

            btn_cancelorder.setVisibility(View.GONE);

        }else{

            btn_cancelorder.setVisibility(View.VISIBLE);

        }

        if(Utility.isInternetConnected(getApplicationContext()))
        {
            FetchMyorder task=new FetchMyorder();
            task.execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

        }


        btn_cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(Ecomm_MyOrders_Details.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure you want to Cancel this Order?");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (!Utility.isInternetConnected(Ecomm_MyOrders_Details.this)) {

                                    Toast.makeText(Ecomm_MyOrders_Details.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                                } else {

                                    new DeleteMyOrder().execute();
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

    }



    class FetchMyorder extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(Ecomm_MyOrders_Details.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETORDERHISTORYBYORDERID);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("OrderID",Utility.getOrderId(getApplicationContext()));

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GETORDERHISTORYBYORDERID);

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
                            + Utility.GETORDERHISTORYBYORDERID, mySoapEnvelop);


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
                        totalitemcount=count;
                        //customer.clear();
                        //customerID.clear();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);

                            String productname1 =soapResult.getPrimitivePropertySafelyAsString("ProductName")
                                    .toString();
                            String productcode=soapResult.getPrimitivePropertySafelyAsString("ProductCode")
                                    .toString();

                            String modelno=soapResult.getPropertySafelyAsString("modelnum", "")
                                    .toString();
                            String status=soapResult.getPropertySafelyAsString("OrderStatusName", "")
                                    .toString();
                            int quantity= Integer.parseInt(soapResult.getPropertySafelyAsString("Quantity", "0")
                                    .toString());

                            double totalprice=Double.valueOf(soapResult.getPrimitivePropertySafelyAsString("Price")
                                    .toString());

                            int orderid=Integer.parseInt(soapResult.getPropertySafelyAsString("OrderID","0"));
                            String imagepath=soapResult.getPrimitivePropertySafelyAsString("ImagePath")
                                    .toString();
                            addrline1=soapResult.getPrimitivePropertySafelyAsString("ShippingAddressLine1")
                                    .toString();
                            addrline2=soapResult.getPrimitivePropertySafelyAsString("ShippingAddressLine2")
                                    .toString();
                            city1=soapResult.getPrimitivePropertySafelyAsString("CityName")
                                    .toString();
                            state1=soapResult.getPrimitivePropertySafelyAsString("AreaName")
                                    .toString();
                            pincode1=soapResult.getPrimitivePropertySafelyAsString("ShippingPincode")
                                    .toString();
                            country1 = soapResult.getPrimitivePropertySafelyAsString("CountryName")
                                    .toString();
                            currency = soapResult.getPrimitivePropertySafelyAsString("Currency")
                                    .toString();
//                            grandtotal=Double.valueOf(soapResult.getPropertySafelyAsString("GrandTotal", "0")
//                                    .toString());

                            grandtotal=Double.valueOf(Utility.getmyordergrandtotal(getApplicationContext()));
                            ///need to change available qty
                            listcart.add(new MyordersDetailsClass(productname1,imagepath,modelno,productcode,quantity,totalprice,0,grandtotal,orderid,currency));
                        }

                        if(addrline2.isEmpty() || addrline2.equalsIgnoreCase("anyType{}")){

                        }else{
                            addrline2 = "\n"+addrline2.trim() +",";
                        }
                        if(pincode1.isEmpty() || pincode1.equalsIgnoreCase("anyType{}")){

                        }else{
                            pincode1 = "\n"+pincode1.trim() ;
                        }
                        String finaladress=addrline1+addrline2+"\n"+state1+","+"\n"+city1+","+pincode1+"\n"+country1;

                        txt_address.setText(finaladress);
                        txt_grandtotal.setText(grandtotal+" " +Utility.getordercurrency(getApplicationContext()));
                        statusname.setText(Utility.getorderstatus(getApplicationContext()));

                        if(Utility.getorderstatus(getApplicationContext()).equalsIgnoreCase("Order Placed"))
                        {
                            statusname.setTextColor(Color.BLUE);
                        }
                        else if(Utility.getorderstatus(getApplicationContext()).equalsIgnoreCase("In Process"))
                        {
                            statusname.setTextColor(getResources().getColor(R.color.yellow));

                        }
                        else if(Utility.getorderstatus(getApplicationContext()).equalsIgnoreCase("Dispatched"))
                        {
                            statusname.setTextColor(Color.RED);
                        }
                        else if(Utility.getorderstatus(getApplicationContext()).equalsIgnoreCase("Completed"))
                        {
                            statusname.setTextColor(Color.GREEN);
                        }
                        else if(Utility.getorderstatus(getApplicationContext()).equalsIgnoreCase("Cancelled"))
                        {
                            statusname.setTextColor(getResources().getColor(R.color.orange));
                        }
                        else if(Utility.getorderstatus(getApplicationContext()).equalsIgnoreCase("Client Not Availed"))
                        {
                            statusname.setTextColor(Color.GRAY);
                        }

                        if(listcart.size()>0) {

                            listmyorders.setAdapter(new MyorderdetailsAdapter(getApplicationContext(), listcart));
                            setListViewHeightBasedOnChildrenproducts(listmyorders);

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

                Toast.makeText(Ecomm_MyOrders_Details.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class DeleteMyOrder extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(Ecomm_MyOrders_Details.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.DELETE_MYORDER);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("OrderID",Utility.getOrderId(getApplicationContext()));

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.DELETE_MYORDER);

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
                            + Utility.DELETE_MYORDER, mySoapEnvelop);


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


                        Toast.makeText(Ecomm_MyOrders_Details.this, "Your Order is Cancelled !", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),MyOrders.class);
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
            }else {

                Toast.makeText(Ecomm_MyOrders_Details.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
        Intent intent=new Intent(getApplicationContext(),MyOrders.class);
        startActivity(intent);
        finish();
    }

}
