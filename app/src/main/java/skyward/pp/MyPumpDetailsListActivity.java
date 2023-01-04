package skyward.pp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import skyward.pp.holder.MyPumpDetailListHolder;
import skyward.pp.model.MyPumpDetailListClass;
import skyward.pp.util.Utility;

public class MyPumpDetailsListActivity extends AppCompatActivity {

    ListView lst_mydetails;
    String serialno,invoicedate, pumpcode, gstartdate,genddate, ID,createddate,serviceTypeID, invoiceno,wstartdate,wenddate;
    ArrayList<MyPumpDetailListClass> pumpdetailsList;
    MyPumpDetailListClass obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypumpdetails_list);

        setTitle("My Pump Details");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Pump Details");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MyPumpDetailsListActivity.this, PumpDetailActivity.class);
                startActivity(i);
                finish();
            }
        });

        pumpdetailsList = new ArrayList<>();
        lst_mydetails = (ListView) findViewById(R.id.mp_detaillist);

        if(Utility.isInternetConnected(getApplicationContext())){
            new FetchPumpDetail().execute();
        }else{
            Toast.makeText(MyPumpDetailsListActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        lst_mydetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                obj = (MyPumpDetailListClass) parent.getItemAtPosition(position);
                Intent i = new Intent(MyPumpDetailsListActivity.this, ShowPumpDetailactivity.class);
                i.putExtra("pumpcode", obj.getPumpcode());
                i.putExtra("servicetypeid", obj.getServiceTypeID());
                i.putExtra("serialno", obj.getSerialno());
                i.putExtra("invoiceno", obj.getInvoiceno());
                i.putExtra("gstart", obj.getGstart());
                i.putExtra("gend", obj.getGend());
                i.putExtra("id", obj.getID());
                i.putExtra("wstart", obj.getWstart());
                i.putExtra("wend", obj.getWend());
                i.putExtra("invoicedate", obj.getInvoicedate());
                i.putExtra("frommypump", "frommypump");
                startActivity(i);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MyPumpDetailsListActivity.this, PumpDetailActivity.class);
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


    class FetchPumpDetail extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(MyPumpDetailsListActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PUMPDETAILSBYUSERID);


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
                            + Utility.GET_PUMPDETAILSBYUSERID, mySoapEnvelop);


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

                            serialno = soapResult.getPrimitivePropertySafelyAsString("SerialNo")
                                    .toString();
                            pumpcode = soapResult.getPrimitivePropertySafelyAsString("ModelNum").toString();
                            createddate = soapResult.getPrimitivePropertySafelyAsString("CreatedDate").toString();
                            ID = soapResult.getPrimitivePropertySafelyAsString("ID")
                                    .toString();
                            serviceTypeID = soapResult.getPrimitivePropertySafelyAsString("ServiceTypeID").toString();

                            gstartdate = soapResult.getPrimitivePropertySafelyAsString("GuaranteeStartDate").toString();
                            genddate = soapResult.getPrimitivePropertySafelyAsString("GuaranteeEndDate").toString();
                            invoiceno = soapResult.getPrimitivePropertySafelyAsString("InvoiceNo").toString();
                            wstartdate = soapResult.getPrimitivePropertySafelyAsString("WarrantyStartDate").toString();
                            wenddate = soapResult.getPrimitivePropertySafelyAsString("WarrantyEndDate").toString();
                            invoicedate = soapResult.getPrimitivePropertySafelyAsString("InvoiceDate")
                                    .toString();

                           /* String[] arrdate = gstartdate.split("-");
                            String year = arrdate[0];
                            String month = arrdate[1];
                            String day = arrdate[2];
                            day = day.substring(0, 2);
                            gstartdate = day + "-" + month + "-" + year;

                            String[] arrdate2 = genddate.split("-");
                            String year2 = arrdate2[0];
                            String month2 = arrdate2[1];
                            String day2 = arrdate2[2];
                            day2 = day2.substring(0, 2);
                            genddate = day2 + "-" + month2 + "-" + year2;

*/

                            pumpdetailsList.add(new MyPumpDetailListClass(serialno, pumpcode,createddate,gstartdate,genddate,serviceTypeID,ID,invoiceno,invoicedate,wstartdate,wenddate));


                        }

                        lst_mydetails.setAdapter(new AdapterPumpDetailList(getApplicationContext(), pumpdetailsList));


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

                Toast.makeText(MyPumpDetailsListActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class AdapterPumpDetailList extends BaseAdapter {
        Context mContext;
        LayoutInflater inflater;
        int rid;
        private ArrayList<MyPumpDetailListClass> arraylist;

        public AdapterPumpDetailList(Context mContext, ArrayList<MyPumpDetailListClass> arraylist) {
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
            TextView serialno, pumpcode, gdate;

            MyPumpDetailListHolder item;


            if (convertView == null) {

                convertView = inflater.inflate(R.layout.mypumpdetaillist_listitem, null);
                serialno = (TextView) convertView.findViewById(R.id.pdli_serialno);
                pumpcode = (TextView) convertView.findViewById(R.id.pdli_pumpcode);

                item = new MyPumpDetailListHolder(serialno, pumpcode);
                convertView.setTag(item);
            } else {
                item = (MyPumpDetailListHolder) convertView.getTag();
            }

            item.getSerialno().setText(arraylist.get(position).getSerialno());
            item.getPumpcode().setText(arraylist.get(position).getPumpcode());
           // item.getGdates().setText(arraylist.get(position).getGstart() +" to "+ arraylist.get(position).getGend());


            return convertView;


        }
    }


}
