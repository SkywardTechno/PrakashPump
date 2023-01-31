package skyward.pp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sinch.android.rtc.SinchError;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skyward.pp.util.Utility;

public class VerificationcodeActivity extends BaseActivity implements SinchService.StartFailedListener {

    Dialog pop_confimation;
    TextView txtpop_msg;
    Button btnpop_cancel;
    Button getBtnpop_submit;
    Button verify, resend;
    EditText verificationcode;
    int mFlipping = 0 ;
    ImageView tick;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificationcode);

        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);

        if (mFlipping == 0) {
            /** Start Flipping */
            flipper.startFlipping();
            mFlipping = 1;

        }
        verificationcode = (EditText) findViewById(R.id.verificationcode);
        verify = (Button) findViewById(R.id.btn_verify);
        resend = (Button) findViewById(R.id.btn_resend);
        tick = (ImageView) findViewById(R.id.tick);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code=verificationcode.getText().toString();
                if(code.contains(" "))
                {
                    code=code.replace(" ","");

                }
                if (code.equals("")) {
                    Toast.makeText(VerificationcodeActivity.this, "Please enter verificationcode", Toast.LENGTH_SHORT).show();
                } else if (code.equals(Utility.getVerificationCode(VerificationcodeActivity.this))) {

                    new Verify().execute();
                } else {
                    Toast.makeText(VerificationcodeActivity.this, "Invalid Verification Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog alertDialog = new AlertDialog.Builder(VerificationcodeActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure,you want to Resend the code?");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (!Utility.isInternetConnected(VerificationcodeActivity.this)) {

                                    Toast.makeText(VerificationcodeActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                                } else {

                                    new ResendCode().execute();

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


//        handler.postDelayed(run, 3000);

        verificationcode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                String strPass1 = Utility.getVerificationCode(VerificationcodeActivity.this);
                String strPass2 = verificationcode.getText().toString();
                if (strPass1.equals(strPass2)) {
                    tick.setVisibility(View.VISIBLE);

                } else {
                    tick.setVisibility(View.INVISIBLE);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strPass1 = Utility.getVerificationCode(VerificationcodeActivity.this);
                String strPass2 = verificationcode.getText().toString();
                if (strPass1.equals(strPass2)) {
                    tick.setVisibility(View.VISIBLE);

                } else {
                    tick.setVisibility(View.INVISIBLE);
                }
            }
        });

     //   t.start();
    }


        /*Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(3000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
// Toast.makeText(chatPage.this,"hello from chat page",Toast.LENGTH_LONG).show();
// update TextView here!
                                getCode();

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }catch (Exception e){

                }
            }
        };

    Runnable run = new Runnable() {
        @Override
        public void run() {
            getCode();
        }
    };*/


    public void getCode() {
        try {
            StringBuilder smsBuilder = new StringBuilder();
            final String SMS_URI_INBOX = "content://sms/inbox";
            final String SMS_URI_ALL = "content://sms/";
            try {
                Uri uri = Uri.parse(SMS_URI_INBOX);
                String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
                Cursor cur = getContentResolver().query(uri, projection, "address LIKE '%PrakashPump'", null, "date desc limit 1");
                if (cur.moveToFirst()) {
                    int index_Address = cur.getColumnIndex("address");
                    int index_Person = cur.getColumnIndex("person");
                    int index_Body = cur.getColumnIndex("body");
                    int index_Date = cur.getColumnIndex("date");
                    int index_Type = cur.getColumnIndex("type");
                    do {
                        String strAddress = cur.getString(index_Address);
                        int intPerson = cur.getInt(index_Person);
                        String strbody = cur.getString(index_Body);
                        long longDate = cur.getLong(index_Date);
                        int int_Type = cur.getInt(index_Type);

                        smsBuilder.append("[ ");
                        smsBuilder.append(strAddress + ", ");
                        smsBuilder.append(intPerson + ", ");
                        smsBuilder.append(strbody + ", ");
                        smsBuilder.append(longDate + ", ");
                        smsBuilder.append(int_Type);
                        smsBuilder.append(" ]\n\n");

                        String str = strbody;
                        Pattern pattern = Pattern.compile("\\s+([0-9]+)");
//                    Pattern pattern = Pattern.compile("\\s+\\d+\\s");

                        Matcher matcher = pattern.matcher(str);
                        for (int i = 0; i < matcher.groupCount(); i++) {
                            matcher.find();
                            Log.e("No....--", matcher.group());
                            if (matcher.group().equals("")) {
                              //  handler.postDelayed(run, 7000);
                            } else {
                                verificationcode.setText(matcher.group());
                            }
                        }
                    } while (cur.moveToNext());

                    if (!cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }
                } else {
                    smsBuilder.append("no result!");
                } // end if

                Log.e("sms", smsBuilder.toString());
                // verificationcode.setText(smsBuilder.toString());
            } catch (SQLiteException ex) {
                Log.d("SQLiteException", ex.getMessage());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
      // Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStarted() {

    }

    class Verify extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(VerificationcodeActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.USER_VERIFICATION);

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            myAndroidHttpTransport = null;

            request.addProperty("userName", Utility.getUserName(VerificationcodeActivity.this));
            request.addProperty("verificationCode", verificationcode.getText().toString());

            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.USER_VERIFICATION, mySoapEnvelop);

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

                    if (!Utility.isInternetConnected(VerificationcodeActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                            boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                    "IsSucceed").toString());

                            Utility.saveAuthToken(VerificationcodeActivity.this, soapObject.getPrimitivePropertySafelyAsString("AuthenticationToken"));
                            Utility.saveUserName(VerificationcodeActivity.this, soapObject.getPrimitivePropertySafelyAsString("UserName"));
                            Utility.saveName(VerificationcodeActivity.this, soapObject.getPrimitivePropertySafelyAsString("Name"));
                            Utility.saveemailid(getApplicationContext(), soapObject.getPrimitivePropertySafelyAsString("EmailID"));
                            Utility.saveIsAdministrator(getApplicationContext(), "false");
                            String uname = soapObject.getPrimitivePropertySafelyAsString("Name").toString() +"_"+soapObject.getPrimitivePropertySafelyAsString("UserName").toString();

                            Utility.savenoticount(getApplicationContext(), 0);

                            Utility.saveCallUser(getApplicationContext(),uname);

                            if (!getSinchServiceInterface().isStarted()) {
                                System.out.println("///////////////////userregistered");
                                try {
                                    getSinchServiceInterface().startClient(uname);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }

                            }
                            Intent alarm = new Intent(getApplicationContext(), OrderUpdateStatusReceiver.class);
                            PendingIntent pendingIntent = null;
                            boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_MUTABLE) != null);
                            if (alarmRunning == false) {
                                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, PendingIntent.FLAG_MUTABLE);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
                            }

                            Intent i = new Intent(VerificationcodeActivity.this, LaunchActivity.class);
                            i.putExtra("fromlogin","fromlogin");
                            startActivity(i);
                            finish();

                        } else {

                            Toast.makeText(getBaseContext(),
                                    soapObject.getProperty("ErrorMessage").toString(),
                                    Toast.LENGTH_LONG).show();

                        }
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }else{

                Toast.makeText(VerificationcodeActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class ResendCode extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(VerificationcodeActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.RESEND_VERIFICATIONCODE);

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            myAndroidHttpTransport = null;

            request.addProperty("userName",Utility.getUserName(getApplicationContext()));
            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.RESEND_VERIFICATIONCODE, mySoapEnvelop);

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

                    if (!Utility.isInternetConnected(VerificationcodeActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                            boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                    "IsSucceed").toString());

                            System.out.println(soapObject.getPrimitivePropertySafelyAsString("VerificationCode"));
                            Utility.saveVerificationCode(VerificationcodeActivity.this, soapObject.getPrimitivePropertySafelyAsString("VerificationCode"));

                        } else {

                            Toast.makeText(getBaseContext(),
                                    soapObject.getProperty("ErrorMessage").toString(),
                                    Toast.LENGTH_LONG).show();

                        }
                    }

                } catch (NullPointerException e) {

                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e) {

                    e.printStackTrace();
                } catch (Exception e) {
                   e.printStackTrace();
                }

            }else{

                Toast.makeText(VerificationcodeActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
