package skyward.pp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

import skyward.pp.holder.OrderInquiryListHolder;
import skyward.pp.model.OrderInquiryClass;
import skyward.pp.util.Utility;

public class OrderInquiryList extends AppCompatActivity {

    ListView listorderinquiry;
    ArrayList<OrderInquiryClass> mArrayList;
    int pageindex= 1;
    String name,productname,inquirydate,ID;
    int rid;
    private ArrayList<String> orderlist = new ArrayList<String>();
    private ArrayList<Integer>  orderlistid = new ArrayList<Integer>();
    OrderInquiryClass orderInquiry;
    String cnm="";
    boolean flag_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_inquirylist);

        setTitle("Order Inquiry");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Order Inquiry");
        toolbar.setTitleTextColor(Color.WHITE);


        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(OrderInquiryList.this, AdminDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
        if (!Utility.isInternetConnected(OrderInquiryList.this)) {

            Toast.makeText(OrderInquiryList.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
            }
            }, 2000) ;
        }else {
            mArrayList=new ArrayList<>();
            new FetchOrderInquiry().execute();

        }

        listorderinquiry = (ListView) findViewById(R.id.oi_listorderinquiry);
mArrayList=new ArrayList<>();

     listorderinquiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        orderInquiry = (OrderInquiryClass) parent.getItemAtPosition(position);

        // Toast.makeText(MyVideosActivity.this, "VideoClicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), OrderInquiryDetailsActivity.class);
        intent.putExtra("ID", orderInquiry.getOrderID());
        startActivity(intent);
        finish();
    }
});
        listorderinquiry.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {

                        flag_loading = true;
                        pageindex += 1;

                        FetchOrderInquiry task = new FetchOrderInquiry();
                        task.execute();
                    }
                }
            }
        });
    }
    class FetchOrderInquiry extends AsyncTask<Void, Void, SoapObject> {
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
                    Utility.GETALLORDER_INQUIRY);


            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("pageIndex",pageindex);
            request.addProperty("pageSize", 10);




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
                            + Utility.GETALLORDER_INQUIRY, mySoapEnvelop);


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

                            name = soapResult.getPrimitivePropertySafelyAsString("Name")
                                    .toString();
                            productname = soapResult.getPrimitivePropertySafelyAsString("ProductName").toString();
                            inquirydate = soapResult.getPrimitivePropertySafelyAsString("InquiryDate").toString();
                            rid =Integer.parseInt(soapResult.getPrimitivePropertySafelyAsString("ReplyID").toString());
                            ID = soapResult.getPrimitivePropertySafelyAsString("ID")
                                    .toString();

                            String[] arrdate = inquirydate.split("-");
                            String year = arrdate[0];
                            String month = arrdate[1];
                            String day = arrdate[2];
                            day = day.substring(0, 2);
                            inquirydate = day + "-" + month + "-" + year;
                            mArrayList.add(new OrderInquiryClass(name, productname, ID, inquirydate,rid));


                        }

                        listorderinquiry.setAdapter(new Adapter(getApplicationContext(), mArrayList));


                    } else {
                 /*   Toast.makeText(getApplicationContext(),
                            soapObject.getProperty("ErrorMessage").toString(),
                            Toast.LENGTH_LONG).show();*/
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }else{

                Toast.makeText(OrderInquiryList.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class Adapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        int rid;
        private ArrayList<OrderInquiryClass> arraylist;

        public Adapter(Context mContext, ArrayList<OrderInquiryClass> arraylist) {
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
            TextView name,productname,inquirydate;
            ImageView icon;
            Button btn_OIStatus;
            OrderInquiryListHolder item;


            if (convertView==null)
            {

                convertView=inflater.inflate(R.layout.orderinquiry_listitem,null);
                name=(TextView)convertView.findViewById(R.id.oi_listitem_cname);
                productname=(TextView)convertView.findViewById(R.id.oi_listitem_productname);
                inquirydate= (TextView) convertView.findViewById(R.id.oi_listitem_date);
                icon = (ImageView) convertView.findViewById(R.id.oi_listitem_image_view);
                btn_OIStatus= (Button) convertView.findViewById(R.id.btn_orderInquiryStatus);
                item=new OrderInquiryListHolder(icon,name,productname,inquirydate,btn_OIStatus);
                convertView.setTag(item);
            }
            else
            {
                item=(OrderInquiryListHolder)convertView.getTag();
            }
            item.getName().setText(arraylist.get(position).getName());

            item.getDate().setText(arraylist.get(position).getInquirydate());
            item.getProductname().setText(arraylist.get(position).getProductname());
            rid=arraylist.get(position).getReplyId();
            if(rid==0)
            {
                item.getBtn_status().setEnabled(false);
                item.getBtn_status().setText("Pending");
                item.getBtn_status().setBackgroundColor(getResources().getColor(R.color.orange));

            }
            else
            {
                item.getBtn_status().setEnabled(true);
                item.getBtn_status().setText("Replied");
            }
            item.getBtn_status().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(getApplicationContext(),reply_orderInquiry.class);
                    i.putExtra("RID",arraylist.get(position).getReplyId());
                    startActivity(i);
                }
            });

            String firstLetter = String.valueOf(arraylist.get(position).getName().toUpperCase().charAt(0));

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getColor(getItem(position));
            //int color = generator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px

            item.getIcon().setImageDrawable(drawable);

            return convertView;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(OrderInquiryList.this, AdminDashboardActivity.class);
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
