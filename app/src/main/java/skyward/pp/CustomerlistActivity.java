package skyward.pp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import skyward.pp.adapter.Customer;
import skyward.pp.adapter.CustomerAdapter;
import skyward.pp.adapter.CustomerViewHolder;
import skyward.pp.util.Utility;

public class  CustomerlistActivity extends BaseActivity implements ServiceConnection {

    AutoCompleteTextView customer_search;
    ListView listcustomer;
    TextView cl_utype;
    ImageButton addcustomer;
    int pageindex= 1;
    String CustomerName,MobileNumber,Country,Area ,City ,ID,UserTypeID,Password,EmailID;
    ArrayList<Customer> mArrayList;
    private ArrayList<String> customerlist = new ArrayList<String>();
    private ArrayList<Integer>  customerlistid = new ArrayList<Integer>();
    Customer customer;
    String cnm="";
    boolean flag_loading;
    int flag =0;
    String UserType;
    private ArrayList<String> usertypelist = new ArrayList<String>();
    private ArrayList<Integer> usertypelistID = new ArrayList<Integer>();
    public int tempusertype = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerlist);

        setTitle("Customers");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Customers");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CustomerlistActivity.this, AdminDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });


        mArrayList=new ArrayList<>();
        customer_search = (AutoCompleteTextView) findViewById(R.id.customer_search);
        listcustomer = (ListView) findViewById(R.id.listcustomer);
        addcustomer = (ImageButton) findViewById(R.id.new_add_customer);
        cl_utype = (TextView) findViewById(R.id.cl_utype);

        cl_utype.setText("Customer");


        if (!Utility.isInternetConnected(CustomerlistActivity.this)) {

            Toast.makeText(CustomerlistActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;

        }else {

            mArrayList=new ArrayList<>();
            new FetchCustomer().execute();
            new FetchCustomerType().execute();

        }


        customer_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pageindex = 1;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Utility.isInternetConnected(CustomerlistActivity.this)) {

                    if(flag == 1){

                    }else {
                        mArrayList = new ArrayList<>();
                        cnm = s.toString();
                        pageindex =1;
                        mArrayList = new ArrayList<>();
                        FetchCustomer task = new FetchCustomer();
                        task.execute();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection..!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mArrayList=new ArrayList<>();

        listcustomer.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {

                        flag_loading = true;
                        pageindex += 1;

                        FetchCustomer task = new FetchCustomer();
                        task.execute();
                    }
                }
            }
        });


        listcustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                customer = (Customer) parent.getItemAtPosition(position);

                // Toast.makeText(MyVideosActivity.this, "VideoClicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CustomerDetailsActivity.class);
                intent.putExtra("ID", customer.getCustomerID());
                intent.putExtra("UserTypeID", customer.getUserTypeID());
                intent.putExtra("Area", customer.getArea());
                intent.putExtra("City", customer.getFarmno());
                intent.putExtra("MobileNo", customer.getMobileNumber());
                intent.putExtra("Name", customer.getName());
                intent.putExtra("Password", customer.getPassword());
                intent.putExtra("Country", customer.getPlace());
                intent.putExtra("EmailID", customer.getEmailID());
                startActivity(intent);
                finish();
            }
        });


        addcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CustomerlistActivity.this, AddCustomeractivity.class);
                startActivity(intent);
                finish();

            }
        });

    }



    private void alertUserType(final String[] visitType, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                CustomerlistActivity.this);
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
                                flag = 1;
                                pageindex = 1;

                                cnm="";
                                customer_search.setText("");
                                mArrayList = new ArrayList<>();
                                FetchCustomer task = new FetchCustomer();
                                task.execute();

                                cl_utype.setText(visitType[which]);
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
            progress = new ProgressDialog(CustomerlistActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_CUSTOMERTYPE);

           /* if(usertypelist.size() > 0){
                usertypelist.clear();
                usertypelistID.clear();
            }
*/
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

                        //customerID.clear();

                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            usertypelist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());

                            usertypelistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));

                   /* if(UserTypeID.equals(soapResult.getPropertySafelyAsString("ID")
                            .toString())) {
                        UserType = soapResult.getPropertySafelyAsString("Name", "")
                                .toString();

                    }
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

            }else{

                Toast.makeText(CustomerlistActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    class FetchCustomer extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        //private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(CustomerlistActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_CUSTOMER);

           
            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("prefix",cnm);
            request.addProperty("customerTypeID",tempusertype);
            request.addProperty("pageIndex",pageindex);
            request.addProperty("pageSize",10);



            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_CUSTOMER);

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
                            + Utility.GET_CUSTOMER, mySoapEnvelop);


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
            flag_loading = false;
            if(result!=null) {
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

                            CustomerName = soapResult.getPrimitivePropertySafelyAsString("Name")
                                    .toString();
                            MobileNumber = soapResult.getPrimitivePropertySafelyAsString("MobileNo").toString();
                            Password = soapResult.getPrimitivePropertySafelyAsString("Password").toString();
                            Country = soapResult.getPrimitivePropertySafelyAsString("Country").toString();
                            EmailID = soapResult.getPrimitivePropertySafelyAsString("EmailID").toString();
                            Area = soapResult.getPrimitivePropertySafelyAsString("AreaName").toString();
                            City = soapResult.getPrimitivePropertySafelyAsString("CityName").toString();
                            UserTypeID = soapResult.getPrimitivePropertySafelyAsString("UserTypeID").toString();
                            ID = soapResult.getPrimitivePropertySafelyAsString("UserID").toString();


                            mArrayList.add(new Customer(MobileNumber, Password, CustomerName, ID, UserTypeID, Country, City, Area,EmailID));


                        }
                        customer_search.setThreshold(1);

                        listcustomer.setAdapter(new CustomerAdapter1(getApplicationContext(), mArrayList));


                        flag =0;
                    } else {
                   /* Toast.makeText(getApplicationContext(),
                            soapObject.getProperty("ErrorMessage").toString(),
                            Toast.LENGTH_LONG).show();*/
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(CustomerlistActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

/*
    private void gotoalert(){

        String[] countrys = usertypelist.toArray(new String[usertypelist
                .size()]);
        alertUserType(countrys, v, customer_search);
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater menuInflater = getSupportMenuInflater();
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        // return true;
        // MenuItemCompat.getActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_filter:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...

                final View v = getLayoutInflater().inflate(R.layout.activity_customerlist, null);;

                String[] countrys = usertypelist.toArray(new String[usertypelist
                        .size()]);
                alertUserType(countrys, v);


           
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    public class CustomerAdapter1 extends BaseAdapter {

        Context mContext;
        LayoutInflater inflater;

        private ArrayList<Customer> arraylist;


        public CustomerAdapter1(Context mContext, ArrayList<Customer> arraylist) {
            this.mContext = mContext;
            this.arraylist = arraylist;
            inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            TextView name,usertype;
            ImageView icon,videoicon;
            CustomerViewHolder item;
            if (convertView==null)
            {

                convertView=inflater.inflate(R.layout.customer_listitem,null);
                name=(TextView)convertView.findViewById(R.id.txt_uname);
                usertype=(TextView)convertView.findViewById(R.id.usertype);
                icon = (ImageView) convertView.findViewById(R.id.customerimage_view);
                videoicon = (ImageView) convertView.findViewById(R.id.videocallimage_view);
                item=new CustomerViewHolder(icon,videoicon,name,usertype);
                convertView.setTag(item);
            }
            else
            {
                item=(CustomerViewHolder)convertView.getTag();
            }
            item.getName().setText(arraylist.get(position).getName());



            String firstLetter = String.valueOf(arraylist.get(position).getName().toUpperCase().charAt(0));

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getColor(getItem(position));
            //int color = generator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px

            item.getIcon().setImageDrawable(drawable);

            item.getVideoicon().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String username = arraylist.get(position).getName() + "_" + arraylist.get(position).getMobileNumber();
                        Call call = getSinchServiceInterface().callUserVideo(username);
                        String callId = call.getCallId();

                        Intent callScreen = new Intent(getApplicationContext(), CallScreenActivity.class);
                        callScreen.putExtra(SinchService.CALL_ID, callId);
                        Utility.savefromviewproduct(getApplicationContext(), "false");
                        startActivity(callScreen);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            return convertView;



        }






    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CustomerlistActivity.this, AdminDashboardActivity.class);
        startActivity(i);
        finish();
    }
}
