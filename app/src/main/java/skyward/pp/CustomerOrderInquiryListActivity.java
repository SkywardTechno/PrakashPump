package skyward.pp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

import skyward.pp.holder.OrderInquiryByUserIDListHolder;
import skyward.pp.model.OrderInquiryByUserIDClass;
import skyward.pp.model.OrderInquiryClass;
import skyward.pp.model.SupportInquiryByUserIDClass;
import skyward.pp.util.Utility;

public class CustomerOrderInquiryListActivity extends AppCompatActivity {

    ListView listorderinquiry;

    ArrayList<OrderInquiryByUserIDClass> mOrderInquiryArrayList;

    ArrayList<SupportInquiryByUserIDClass> mSupportInquiryArrayList;
    int pageindex = 1;
    String name, productname, inquirydate, ID;
    int rid;
    private ArrayList<String> orderlist = new ArrayList<String>();
    private ArrayList<Integer> orderlistid = new ArrayList<Integer>();
    OrderInquiryClass orderInquiry;
    OrderInquiryByUserIDClass orderInquiryByUserID;
    SupportInquiryByUserIDClass supportInquiryByUserID;
    String cnm = "";
    boolean flag_loading;
    TextView txt_inquirytype;

    TextView textOrderInquiry, textServiceInquiry;
    Dialog dialog;
    String emailid,inquirycode,mobileno,remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orderinquirylist);

        setTitle("My Inquiry");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Inquiry");
        toolbar.setTitleTextColor(Color.WHITE);


        getSupportActionBar().getCustomView();
        listorderinquiry = (ListView) findViewById(R.id.coi_listorderinquiry);


        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CustomerOrderInquiryListActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        txt_inquirytype = (TextView) findViewById(R.id.txtinquirytype);

        txt_inquirytype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EDIT 3.3.2017
                dialog = new Dialog(CustomerOrderInquiryListActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_category);

                dialog.show();

                textOrderInquiry = (TextView) dialog.findViewById(R.id.textOrderInquiry);
                textServiceInquiry = (TextView) dialog.findViewById(R.id.textServiceInquiry);

                textOrderInquiry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Show Order Inquiry By Id
                        dialog.cancel();
                        txt_inquirytype.setText("Order Inquiry");
                        mOrderInquiryArrayList = new ArrayList<>();
                        if(Utility.isInternetConnected(getApplicationContext())) {
                            new FetchOrderInquiryByUserId().execute();
                        }else{
                            Toast.makeText(CustomerOrderInquiryListActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                textServiceInquiry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Show Order Inquiry By Id
                        dialog.cancel();
                        txt_inquirytype.setText("Service Inquiry");
                        mSupportInquiryArrayList = new ArrayList<>();
                        if(Utility.isInternetConnected(getApplicationContext())) {
                            new FetchSupportInquiryByUserId().execute();
                        }else{
                            Toast.makeText(CustomerOrderInquiryListActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


        if(getIntent().hasExtra("fromorder")){
            txt_inquirytype.setText("Order Inquiry");
            mOrderInquiryArrayList = new ArrayList<>();
            if(Utility.isInternetConnected(getApplicationContext())) {
                new FetchOrderInquiryByUserId().execute();
            }else{
                Toast.makeText(CustomerOrderInquiryListActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }else if(getIntent().hasExtra("fromsupport")){
            txt_inquirytype.setText("Service Inquiry");

            mSupportInquiryArrayList = new ArrayList<>();
            if(Utility.isInternetConnected(getApplicationContext())) {
                new FetchSupportInquiryByUserId().execute();
            }else{
                Toast.makeText(CustomerOrderInquiryListActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }


        listorderinquiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(txt_inquirytype.getText().toString().equals("Order Inquiry")) {
                    orderInquiryByUserID = (OrderInquiryByUserIDClass) parent.getItemAtPosition(position);

                    // Toast.makeText(MyVideosActivity.this, "VideoClicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ViewInquiryDetailsActivity.class);
                    intent.putExtra("ID", orderInquiryByUserID.getOrderID());
                    intent.putExtra("name", orderInquiryByUserID.getName());
                    intent.putExtra("inquirycode", orderInquiryByUserID.getInquirycode());
                    intent.putExtra("emailid", orderInquiryByUserID.getEmailid());
                    intent.putExtra("phoneno", orderInquiryByUserID.getPhoneno());
                    intent.putExtra("remarks", orderInquiryByUserID.getRemarks());
                    intent.putExtra("productname", orderInquiryByUserID.getProductname());

                    startActivity(intent);
                    finish();
                }else{
                    supportInquiryByUserID = (SupportInquiryByUserIDClass) parent.getItemAtPosition(position);

                    // Toast.makeText(MyVideosActivity.this, "VideoClicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ViewMySupportInquiryDetails.class);
                    intent.putExtra("ID", supportInquiryByUserID.getServiceID());
                    intent.putExtra("name", supportInquiryByUserID.getName());
                    intent.putExtra("inquirycode", supportInquiryByUserID.getInquirycode());
                    intent.putExtra("emailid", supportInquiryByUserID.getEmailid());
                    intent.putExtra("phoneno", supportInquiryByUserID.getPhoneno());
                    intent.putExtra("servicetype", supportInquiryByUserID.getServicetype());
                    intent.putExtra("remarks", supportInquiryByUserID.getRemarks());
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    class FetchOrderInquiryByUserId extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(CustomerOrderInquiryListActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_ORDERINQUIRYBYUSERID);


            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));

            Log.e("token", "" + Utility.getAuthToken(getApplicationContext()));


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
                            + Utility.GET_ORDERINQUIRYBYUSERID, mySoapEnvelop);


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

            progress.cancel();

            flag_loading = false;

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

                            name = soapResult.getPrimitivePropertySafelyAsString("Name")
                                    .toString();
                            productname = soapResult.getPrimitivePropertySafelyAsString("ProductName").toString();
                            inquirydate = soapResult.getPrimitivePropertySafelyAsString("CreatedDate").toString();
                            ID = soapResult.getPrimitivePropertySafelyAsString("ID")
                                    .toString();
                            inquirycode = soapResult.getPrimitivePropertySafelyAsString("InquiryCode").toString();

                            mobileno = soapResult.getPrimitivePropertySafelyAsString("MobileNo").toString();
                            emailid = soapResult.getPrimitivePropertySafelyAsString("EmailID").toString();
                            remarks = soapResult.getPrimitivePropertySafelyAsString("Remarks").toString();


                            String[] arrdate = inquirydate.split("-");
                            String year = arrdate[0];
                            String month = arrdate[1];
                            String day = arrdate[2];
                            day = day.substring(0, 2);
                            inquirydate = day + "-" + month + "-" + year;

                            Log.e("PNAME", "" + productname);
                            Log.e("PDATE", "" + inquirydate);

                            mOrderInquiryArrayList.add(new OrderInquiryByUserIDClass(name, productname,inquirydate,ID,inquirycode,emailid,mobileno,remarks));


                        }

                        listorderinquiry.setAdapter(new AdapterOrderInquiryByUserId(getApplicationContext(), mOrderInquiryArrayList));


                    } else {
                        listorderinquiry.setAdapter(null);
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

                Toast.makeText(CustomerOrderInquiryListActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FetchSupportInquiryByUserId extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(CustomerOrderInquiryListActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_SERVICE_INQUIRY_BYUSERID);


            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));

            Log.e("token", "" + Utility.getAuthToken(getApplicationContext()));


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
                            + Utility.GET_SERVICE_INQUIRY_BYUSERID, mySoapEnvelop);


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

            progress.cancel();

            flag_loading = false;

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

                            name = soapResult.getPrimitivePropertySafelyAsString("Name")
                                    .toString();
                            productname = soapResult.getPrimitivePropertySafelyAsString("ServiceType").toString();
                            inquirydate = soapResult.getPrimitivePropertySafelyAsString("CreatedDate").toString();
                            ID = soapResult.getPrimitivePropertySafelyAsString("ID")
                                    .toString();
                            mobileno = soapResult.getPrimitivePropertySafelyAsString("MobileNo").toString();
                            emailid = soapResult.getPrimitivePropertySafelyAsString("EmailID").toString();
                            remarks = soapResult.getPrimitivePropertySafelyAsString("Remarks").toString();
                            inquirycode = soapResult.getPrimitivePropertySafelyAsString("InquiryCode").toString();

                            String[] arrdate = inquirydate.split("-");
                            String year = arrdate[0];
                            String month = arrdate[1];
                            String day = arrdate[2];
                            day = day.substring(0, 2);
                            inquirydate = day + "-" + month + "-" + year;

                            Log.e("PNAME", "" + productname);
                            Log.e("PDATE", "" + inquirydate);

                            mSupportInquiryArrayList.add(new SupportInquiryByUserIDClass(name, productname,inquirydate,ID,emailid,mobileno,remarks,inquirycode));


                        }

                        listorderinquiry.setAdapter(new AdapterSupportInquiryByUserId(getApplicationContext(), mSupportInquiryArrayList));


                    } else {
listorderinquiry.setAdapter(null);
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

                Toast.makeText(CustomerOrderInquiryListActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public class AdapterOrderInquiryByUserId extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        int rid;
        private ArrayList<OrderInquiryByUserIDClass> arraylist;

        public AdapterOrderInquiryByUserId(Context mContext, ArrayList<OrderInquiryByUserIDClass> arraylist) {
            this.mContext = mContext;
            this.arraylist = arraylist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return arraylist.size();
        }

        @Override
        public Object getItem(int position) {
            return arraylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView name, productname, inquirydate;

            OrderInquiryByUserIDListHolder item;


            if (convertView == null) {

                convertView = inflater.inflate(R.layout.orderinquirybyuserid_listitem, null);
                productname = (TextView) convertView.findViewById(R.id.oi_listitem_productname);
                inquirydate = (TextView) convertView.findViewById(R.id.oi_listitem_date);
                item = new OrderInquiryByUserIDListHolder(productname, inquirydate);
                convertView.setTag(item);
            } else {
                item = (OrderInquiryByUserIDListHolder) convertView.getTag();
            }

            item.getDate().setText(arraylist.get(position).getInquirydate());
            item.getProductname().setText(arraylist.get(position).getProductname());


            return convertView;


        }
    }

    public class AdapterSupportInquiryByUserId extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        int rid;
        private ArrayList<SupportInquiryByUserIDClass> arraylist;

        public AdapterSupportInquiryByUserId(Context mContext, ArrayList<SupportInquiryByUserIDClass> arraylist) {
            this.mContext = mContext;
            this.arraylist = arraylist;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return arraylist.size();
        }

        @Override
        public Object getItem(int position) {
            return arraylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView name, productname, inquirydate;

            OrderInquiryByUserIDListHolder item;


            if (convertView == null) {

                convertView = inflater.inflate(R.layout.supportinquirybyuserid_listitem, null);
                productname = (TextView) convertView.findViewById(R.id.oi_listitem_servicename);
                inquirydate = (TextView) convertView.findViewById(R.id.oi_listitem_date);
                item = new OrderInquiryByUserIDListHolder(productname, inquirydate);
                convertView.setTag(item);
            } else {
                item = (OrderInquiryByUserIDListHolder) convertView.getTag();
            }

            item.getDate().setText(arraylist.get(position).getInquirydate());
            item.getProductname().setText(arraylist.get(position).getServicetype());


            return convertView;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(CustomerOrderInquiryListActivity.this, DashboardActivity.class);
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
