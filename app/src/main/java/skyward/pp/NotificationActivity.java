package skyward.pp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import skyward.pp.adapter.NotificationAdapter;
import skyward.pp.ecommerce.EcommDashboardActivity;
import skyward.pp.model.NotificationsClass;
import skyward.pp.util.Utility;

public class NotificationActivity extends AppCompatActivity {

    ListView list_notifications;
    ArrayList<NotificationsClass> arrayList;
    Dialog dialog;
    TextView noti_details, bidcode;
    SwipeRefreshLayout swipe_refresh_layout;

    String navigatetext="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setTitle("Notifications");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Notifications");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navigatetext.equalsIgnoreCase("fromss")) {
                    Intent i = new Intent(NotificationActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(NotificationActivity.this, EcommDashboardActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        if(getIntent().hasExtra("fromss")){
            navigatetext = "fromss";
        }else{
            navigatetext = "fromecomm";
        }

        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        list_notifications = (ListView) findViewById(R.id.list_notifications);

        if (Utility.isInternetConnected(getApplicationContext())) {
            getNotification task = new getNotification();
            task.execute();
        }else{
            Toast.makeText(getApplicationContext(),"Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        list_notifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    NotificationsClass obj = (NotificationsClass) parent.getItemAtPosition(position);

                    System.out.println("inside noti intent");
                    Intent i = new Intent(NotificationActivity.this, NotificationDetailsActivity.class);

                    i.putExtra("title", obj.getTitle().toString());
                    i.putExtra("id", obj.getId());
                    i.putExtra("navigatetext", navigatetext);
                    startActivity(i);
                    finish();
                }catch(Exception e){
                    e.printStackTrace();
                }


            }
        });

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_refresh_layout.setRefreshing(false);

                if (Utility.isInternetConnected(getApplicationContext())) {

                    getNotification task = new getNotification();
                    task.execute();


                } else {
                    Toast.makeText(getApplicationContext(),"Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipe_refresh_layout.setNestedScrollingEnabled(true);
    }



    class getNotification extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(NotificationActivity.this);
            progress.setMessage("Please Wait ...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_NOTIFICATION);

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;

            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));

            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.GET_NOTIFICATION, mySoapEnvelop);

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
                progress.dismiss();
                try {

                    if (!Utility.isInternetConnected(getApplicationContext())) {
                        Toast.makeText(NotificationActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    } else {

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
                            Utility.savenoticount(getApplicationContext(), count);
                            System.out.println("Count is : " + count);
                            arrayList = new ArrayList<>();
                            for (int i = 0; i < count; i++) {
                                SoapObject soapResult = null;
                                soapResult = (SoapObject) result4.getProperty(i);


                                String BidCode = soapResult.getPrimitivePropertySafelyAsString("ShortTitle").toString();
                                String ApproverRemarks = soapResult.getPrimitivePropertySafelyAsString("Remarks").toString();

                                String ID = soapResult.getPrimitivePropertySafelyAsString("ID").toString();


                                arrayList.add(new NotificationsClass(ID,BidCode, ApproverRemarks));


                            }


                            // at_search.setThreshold(3);

                            if (arrayList.size() > 0) {
                                list_notifications.setAdapter(new NotificationAdapter(getApplicationContext(), arrayList));

                            } else {
                                Utility.savenoticount(getApplicationContext(), 0);
                                Toast.makeText(NotificationActivity.this,"No Notifications found !", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Utility.savenoticount(getApplicationContext(), 0);

                           /* Toast.makeText(getApplicationContext(),
                                    soapObject.getProperty("ErrorMessage").toString(),
                                    Toast.LENGTH_SHORT).show();*/
                            Toast.makeText(NotificationActivity.this,"No Notifications found !", Toast.LENGTH_SHORT).show();

                        }


                    }

                } catch (NullPointerException e) {

                    e.printStackTrace();

                } catch (ArrayIndexOutOfBoundsException e) {

                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {
                progress.dismiss();

                Toast.makeText(NotificationActivity.this,"Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(navigatetext.equalsIgnoreCase("fromss")) {
            Intent i = new Intent(NotificationActivity.this, DashboardActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(NotificationActivity.this, EcommDashboardActivity.class);
            startActivity(i);
            finish();
        }

    }
}
