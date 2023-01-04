package skyward.pp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.view.MenuItemCompat;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
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

import skyward.pp.util.Utility;

public class DashboardActivity extends BaseActivity {

    Dialog pop_confimation;
    TextView txt_title;
    TextView txtpop_msg;
    Button btnpop_cancel;
    Button getBtnpop_submit;
    LinearLayout pumpdetail,web,termscondition,order,literature,supportservice,myvideos,myinquiry;
    WebView details;
    String website = "";
    Dialog dialog;
    int notificationtotalcount=0;
    private TextView ui_hot1 = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_new);

        setTitle("Dashboard");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Dashboard");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();



        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DashboardActivity.this, LaunchActivity.class);
                startActivity(i);
                finish();
            }
        });
        pumpdetail = (LinearLayout) findViewById(R.id.dash_ll_pumpdetail);
        literature = (LinearLayout) findViewById(R.id.dash_ll_literature);
        web = (LinearLayout) findViewById(R.id.dash_ll_web);
        supportservice = (LinearLayout) findViewById(R.id.dash_ll_supportservice);
        order = (LinearLayout) findViewById(R.id.dash_ll_pumpselection);
        myvideos = (LinearLayout) findViewById(R.id.dash_ll_myvideos);
        termscondition = (LinearLayout) findViewById(R.id.dash_ll_termscondition);
        myinquiry = (LinearLayout) findViewById(R.id.dash_ll_myinquiry);


        if(Utility.isInternetConnected(getApplicationContext())){
            getNotification task = new getNotification();
            task.execute();
        }
        pumpdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(DashboardActivity.this)) {
                    Intent i = new Intent(DashboardActivity.this, PumpDetailActivity.class);
                    startActivity(i);
                   finish();
                }else{
                    Toast.makeText(DashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        literature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(DashboardActivity.this)) {
                Intent i = new Intent(DashboardActivity.this,LiteratureCategory.class);
                startActivity(i);
                    finish();
                }else{
                    Toast.makeText(DashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                website = "www.prakashpump.com";
                if (Utility.isInternetConnected(getApplicationContext())) {
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + website));
                    startActivity(launchBrowser);
                } else {
                    Toast.makeText(DashboardActivity.this, "Plaese Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
               // Intent i = new Intent(DashboardActivity.this,PumpDetailActivity.class);
                //startActivity(i);
            }
        });

        supportservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(DashboardActivity.this)) {
                Intent i = new Intent(DashboardActivity.this,SupportServiceActivity.class);
                i.putExtra("two" ,"two");
                startActivity(i);
                    finish();
            }else{
                Toast.makeText(DashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(DashboardActivity.this)) {
                Intent i = new Intent(DashboardActivity.this,PumpselectionActivity.class);
                startActivity(i);
                    finish();
                }else{
                    Toast.makeText(DashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myvideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.isInternetConnected(DashboardActivity.this)) {
                Intent i = new Intent(DashboardActivity.this,MyVideosActivity.class);
                startActivity(i);
                    finish();
            }else{
                Toast.makeText(DashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
            }
        });

        termscondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(DashboardActivity.this)) {
                Intent i = new Intent(DashboardActivity.this,ShowTermsConditionActivity.class);
                startActivity(i);
                    finish();
                }else{
                    Toast.makeText(DashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myinquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(DashboardActivity.this)) {
                    Intent i = new Intent(DashboardActivity.this,CustomerOrderInquiryListActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(DashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    class getNotification extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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

                try {

                    if (!Utility.isInternetConnected(getApplicationContext())) {
                        Toast.makeText(DashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
                            SoapObject result5 = (SoapObject) result4.getProperty(0);
                            System.out.println("Result5 is : " + result5.toString());

                            final int count = result4.getPropertyCount();
                            notificationtotalcount = count;
                            updateNotiCount(notificationtotalcount);

                            if(count == Utility.getnoticount(getApplicationContext())){

                            }else{

                                dialog = new Dialog(DashboardActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.popup_notification);
                                dialog.setCancelable(false);
                                dialog.show();
                                // set the custom dialog components - text, image and button
                                txt_title = (TextView) dialog.findViewById(R.id.txt_title);

                                details = (WebView) dialog.findViewById(R.id.noti_details);
                                TextView ok = (TextView) dialog.findViewById(R.id.btn_ok);
                                TextView go = (TextView) dialog.findViewById(R.id.btn_goto);

                                String DetailedDescription = result5.getPrimitivePropertySafelyAsString("Remarks").toString();

                               if(count - Integer.valueOf(Utility.getnoticount(getApplicationContext())) > 1){
                                   txt_title.setText("Message");

                                   String msg = "You have more than one Notification !";
                                   details.loadData(msg, "text/html", "utf-8");
                                }else{
                                   txt_title.setText(result5.getPrimitivePropertySafelyAsString("ShortTitle").toString());

                                   details.loadData(DetailedDescription, "text/html", "utf-8");

                               }

                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();

                                        Utility.savenoticount(getApplicationContext(),count);
                                    }
                                });

                                go.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        Utility.savenoticount(getApplicationContext(), count);

                                        Intent i = new Intent(getApplicationContext(), NotificationActivity.class);
                                        i.putExtra("fromss","fromss");
                                        startActivity(i);
                                        finish();
                                    }
                                });




                            }

                        } else {


                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {


            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater menuInflater = getSupportMenuInflater();
        getMenuInflater().inflate(R.menu.menu_ss, menu);
        MenuItem item1 = menu.findItem(R.id.action_noti);
        MenuItemCompat.setActionView(item1, R.layout.action_bar_annnouncement_icon);
        View view1 = MenuItemCompat.getActionView(item1);
        ui_hot1 = (TextView)view1.findViewById(R.id.hotlist_hot);
        updateNotiCount(notificationtotalcount);
        new MyMenuItemStuffListener(view1, "Notifications") {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DashboardActivity.this,NotificationActivity.class);
                intent.putExtra("fromss","fromss");
                startActivity(intent);
                finish();


            }
        };
        // return true;
        // MenuItemCompat.getActionView();

        return super.onCreateOptionsMenu(menu);
    }

    public void updateNotiCount(final int new_hot_number) {
        notificationtotalcount = new_hot_number;
        if (ui_hot1 == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (notificationtotalcount == 0)
                    ui_hot1.setVisibility(View.INVISIBLE);
                else {
                    ui_hot1.setVisibility(View.VISIBLE);
                    ui_hot1.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }
    static abstract class MyMenuItemStuffListener implements View.OnClickListener, View.OnLongClickListener {
        private String hint;
        private View view;

        MyMenuItemStuffListener(View view, String hint) {
            this.view = view;
            this.hint = hint;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override abstract public void onClick(View v);

        @Override public boolean onLongClick(View v) {
            final int[] screenPos = new int[2];
            final Rect displayFrame = new Rect();
            view.getLocationOnScreen(screenPos);
            view.getWindowVisibleDisplayFrame(displayFrame);
            final Context context = view.getContext();
            final int width = view.getWidth();
            final int height = view.getHeight();
            final int midy = screenPos[1] + height / 2;
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            Toast cheatSheet = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
            if (midy < displayFrame.height()) {
                cheatSheet.setGravity(Gravity.TOP | Gravity.RIGHT,
                        screenWidth - screenPos[0] - width / 2, height);
            } else {
                cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
            }
            cheatSheet.show();
            return true;
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_noti:
                Intent i1 = new Intent(DashboardActivity.this, NotificationActivity.class);
                i1.putExtra("fromss","fromss");
                startActivity(i1);
                finish();
                return true;

            case R.id.action_myprofile:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent i = new Intent(DashboardActivity.this, MyProfileActivity.class);
                i.putExtra("value","dash");
                startActivity(i);
                finish();
                return true;

            case R.id.action_logout:

                AlertDialog alertDialog = new AlertDialog.Builder(DashboardActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure you want to Logout?");
                alertDialog.setCancelable(false);
                alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                if (!Utility.isInternetConnected(DashboardActivity.this)) {

                                    Toast.makeText(DashboardActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                                }else {
                                    new LogoutTask().execute();

                                }

                            }
                        });
                alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(DashboardActivity.this , LaunchActivity.class);
        startActivity(i);
        finish();

    }


    private class LogoutTask extends AsyncTask<Void, Void, SoapObject> {
        private ProgressDialog progress;
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(DashboardActivity.this);
            progress.setMessage("Logging out...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.LOGOUT);

            request.addProperty("token", Utility.getAuthToken(DashboardActivity.this));

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
                            + Utility.LOGOUT, mySoapEnvelop);
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


            }

            catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(SoapObject result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progress.dismiss();
            try {
                SoapObject soapObject = (SoapObject) result.getProperty(0);
                System.out.println(soapObject.getProperty("IsSucceed"));
                if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                    boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                            "IsSucceed").toString());

                    Utility.saveAuthToken(DashboardActivity.this, "");
                    Utility.saveUserType(DashboardActivity.this, "");
                    Utility.saveUserName(DashboardActivity.this, "");
                    Utility.saveUserNamefordisplay(DashboardActivity.this, "");

                    if (getSinchServiceInterface() != null) {
                        getSinchServiceInterface().stopClient();
                    }

                    Intent intent = new Intent(DashboardActivity.this, OrderUpdateStatusReceiver.class);
                    boolean alarmRunning = (PendingIntent.getBroadcast(DashboardActivity.this, 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
                    if(alarmRunning == true) {
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(DashboardActivity.this, 0, intent, PendingIntent.FLAG_NO_CREATE);
                        AlarmManager alarmManager = (AlarmManager) DashboardActivity.this.getSystemService(DashboardActivity.this.ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);
                    }

                    Toast.makeText(DashboardActivity.this, "You have logged out successfully", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();


                } else {
                    Toast.makeText(getApplicationContext(),
                            soapObject.getProperty("ErrorMessage").toString(),
                            Toast.LENGTH_LONG).show();
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

}
