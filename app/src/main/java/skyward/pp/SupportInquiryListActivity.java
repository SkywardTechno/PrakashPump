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

import skyward.pp.holder.SupportInquiryHolder;
import skyward.pp.model.SupportInquiryClass;
import skyward.pp.util.Utility;

public class SupportInquiryListActivity extends AppCompatActivity {
    boolean flag_loading;
    ArrayList<SupportInquiryClass> mArrayList;
    int pageindex = 1;
    private ArrayList<String> inquirylist = new ArrayList<String>();
    private ArrayList<Integer> inquirylistid = new ArrayList<Integer>();
    SupportInquiryClass supportInquiry;
    ListView listsupportinquiry;
    int rid;
    String name, servicetype, date, serviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_inquirylist);

        setTitle("Service Inquiry");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Service Inquiry");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SupportInquiryListActivity.this, AdminDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        listsupportinquiry= (ListView) findViewById(R.id.listsupportinquiry);


        if (!Utility.isInternetConnected(SupportInquiryListActivity.this)) {

            Toast.makeText(SupportInquiryListActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        } else {
            mArrayList = new ArrayList<>();
            new FetchSupportInquiry().execute();

        }

        listsupportinquiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                supportInquiry = (SupportInquiryClass) parent.getItemAtPosition(position);

                // Toast.makeText(MyVideosActivity.this, "VideoClicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SupportInquiryDetailsActivity.class);
                intent.putExtra("ID", String.valueOf(supportInquiry.getSupportID()));
                startActivity(intent);
                finish();
            }
        });
        listsupportinquiry.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {

                        flag_loading = true;
                        pageindex += 1;

                        FetchSupportInquiry task = new FetchSupportInquiry();
                        task.execute();
                    }
                }
            }
        });
    }



    class FetchSupportInquiry extends AsyncTask<Void, Void, SoapObject> {
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
                    Utility.GET_ALLSERVICEINQUIRY);


            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));
            request.addProperty("pageIndex", pageindex);
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
                            + Utility.GET_ALLSERVICEINQUIRY, mySoapEnvelop);


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
                            servicetype = soapResult.getPrimitivePropertySafelyAsString("ServiceType").toString();
                            rid =Integer.parseInt(soapResult.getPrimitivePropertySafelyAsString("ReplyID").toString());
                            date = soapResult.getPrimitivePropertySafelyAsString("InquiryDate").toString();
                            String[] arrdate = date.split("-");
                            String year = arrdate[0];
                            String month = arrdate[1];
                            String day = arrdate[2];
                            day = day.substring(0, 2);
                            date = day + "-" + month + "-" + year;
                            serviceID = soapResult.getPrimitivePropertySafelyAsString("ID")
                                    .toString();


                            mArrayList.add(new SupportInquiryClass(name, servicetype, serviceID, date,rid));


                        }

                        listsupportinquiry.setAdapter(new Adapter(getApplicationContext(), mArrayList));


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

                Toast.makeText(SupportInquiryListActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class Adapter extends BaseAdapter {


        Context mContext;
        LayoutInflater inflater;
        int rid;
        private ArrayList<SupportInquiryClass> arraylist;

        public Adapter(Context mContext, ArrayList<SupportInquiryClass> arraylist) {
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

            TextView name,ServiceType,date;
            ImageView icon;
            SupportInquiryHolder item;
            Button btn_SIStatus;
            if (convertView==null)
            {

                convertView=inflater.inflate(R.layout.supportinquiry_listitem,null);
                name=(TextView)convertView.findViewById(R.id.sui_listitem_cname);
                ServiceType=(TextView)convertView.findViewById(R.id.sui_listitem_servicetype);
                date= (TextView) convertView.findViewById(R.id.sui_date);
                icon = (ImageView) convertView.findViewById(R.id.sui_listitem_image_view);
                btn_SIStatus= (Button) convertView.findViewById(R.id.btn_supportInquiryStatus);
                item=new SupportInquiryHolder(icon,name,ServiceType,date,btn_SIStatus);
                convertView.setTag(item);
            }
            else
            {
                item=(SupportInquiryHolder)convertView.getTag();
            }
            item.getName().setText(arraylist.get(position).getName());

            item.getDate().setText(arraylist.get(position).getDate());
            item.getServicetype().setText(arraylist.get(position).getServiceType());
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
                    Intent i=new Intent();
                    i.setClass(mContext,reply_supportInquiry.class);
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

        Intent i = new Intent(SupportInquiryListActivity.this, AdminDashboardActivity.class);
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
