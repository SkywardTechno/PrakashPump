package skyward.pp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

public class ForgotPasswordverificationActivity extends AppCompatActivity {

    EditText vcode, newpassword,confirmpassword;
    Button btn_verify, btn_resend;
    TextView txt_match,forgot_login;
    int mFlipping =0;
    ImageView tick;
    Button btnpop_cancel;
    Button getBtnpop_submit;
    Dialog pop_confimation;
    TextView txtpop_msg;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passwordverification);

        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);

        if (mFlipping == 0) {
            /** Start Flipping */
            flipper.startFlipping();
            mFlipping = 1;

        }


        vcode = (EditText) findViewById(R.id.forgot_code);
        newpassword = (EditText) findViewById(R.id.forgot_newpassword);
        confirmpassword = (EditText) findViewById(R.id.forgot_confirmpassword);
        btn_verify = (Button) findViewById(R.id.forgot_btn_verify);
        btn_resend = (Button) findViewById(R.id.forgot_btn_resend);
        txt_match = (TextView) findViewById(R.id.forgot_txt_matched);
        forgot_login = (TextView) findViewById(R.id.forgot_login);
        tick = (ImageView) findViewById(R.id.forgot_tick);

        forgot_login.setText("<< Login");

        forgot_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ForgotPasswordverificationActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code= vcode.getText().toString();
                if(code.contains(" "))
                {
                    code=code.replace(" ","");

                }
                if (code.equals("")) {
                    Toast.makeText(ForgotPasswordverificationActivity.this, "Please enter verificationcode", Toast.LENGTH_SHORT).show();
                } else if (code.equals(Utility.getVerificationCode(ForgotPasswordverificationActivity.this))) {

                    if(newpassword.getText().toString().isEmpty()){
                        Toast.makeText(ForgotPasswordverificationActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    }else if(confirmpassword.getText().toString().isEmpty()){
                        Toast.makeText(ForgotPasswordverificationActivity.this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    }else if(txt_match.getText().toString().equals("Passwords Do not Match")){
                        Toast.makeText(ForgotPasswordverificationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new Verify().execute();
                    }

                } else {
                    Toast.makeText(ForgotPasswordverificationActivity.this, "Invalid Verification Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordverificationActivity.this).create();

                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure,you want to Resend the code?");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                if(Utility.isInternetConnected(ForgotPasswordverificationActivity.this)) {
                                    new ResendCode().execute();
                                }else{
                                    Toast.makeText(ForgotPasswordverificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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

        handler.postDelayed(run, 3000);

        vcode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                String strPass1 = Utility.getVerificationCode(ForgotPasswordverificationActivity.this);
                String strPass2 = vcode.getText().toString();
                if (strPass1.equals(strPass2)) {
                    tick.setVisibility(View.VISIBLE);

                } else {
                    tick.setVisibility(View.INVISIBLE);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strPass1 = Utility.getVerificationCode(ForgotPasswordverificationActivity.this);
                String strPass2 = vcode.getText().toString();
                if (strPass1.equals(strPass2)) {
                    tick.setVisibility(View.VISIBLE);

                } else {
                    tick.setVisibility(View.INVISIBLE);

                }
            }
        });


        confirmpassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String strPass1 = newpassword.getText().toString();
                String strPass2 = confirmpassword.getText().toString();
                if (strPass1.equals(strPass2)) {
                    txt_match.setVisibility(View.VISIBLE);
                    txt_match.setText("Password Matched");
                    txt_match.setTextColor(Color.GREEN);
                } else {
                    txt_match.setVisibility(View.VISIBLE);
                    txt_match.setText("Passwords Do not Match");
                    txt_match.setTextColor(Color.RED);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strPass1 = newpassword.getText().toString();
                String strPass2 = confirmpassword.getText().toString();
                if (strPass1.equals(strPass2)) {
                    txt_match.setVisibility(View.VISIBLE);
                    txt_match.setText("Password Matched");
                    txt_match.setTextColor(Color.GREEN);
                } else {
                    txt_match.setVisibility(View.VISIBLE);
                    txt_match.setText("Passwords Do not Match");
                    txt_match.setTextColor(Color.RED);
                }
            }
        });

        t.start();
    }


    Thread t = new Thread() {
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
            }
        }
    };


    Runnable run = new Runnable() {
        @Override
        public void run() {
            getCode();
        }
    };

    public void getCode() {
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
                            handler.postDelayed(run, 7000);
                        } else {
                            vcode.setText(matcher.group());
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
    }


    class Verify extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(ForgotPasswordverificationActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.CHANGE_PASSWORD);

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            myAndroidHttpTransport = null;

            request.addProperty("userName", Utility.getUserName(ForgotPasswordverificationActivity.this));
            request.addProperty("verificationCode", vcode.getText().toString());
            request.addProperty("password", newpassword.getText().toString());



            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.CHANGE_PASSWORD, mySoapEnvelop);

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

                    if (!Utility.isInternetConnected(ForgotPasswordverificationActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                            boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                    "IsSucceed").toString());

                            Utility.saveAuthToken(ForgotPasswordverificationActivity.this, soapObject.getPrimitivePropertySafelyAsString("AuthenticationToken"));
                            Utility.saveUserType(ForgotPasswordverificationActivity.this, soapObject.getPrimitivePropertySafelyAsString("UserType"));
                            Utility.savePassword(ForgotPasswordverificationActivity.this, newpassword.getText().toString());
                            Intent i = new Intent(ForgotPasswordverificationActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
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

                Toast.makeText(ForgotPasswordverificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
            progress = new ProgressDialog(ForgotPasswordverificationActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;


            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.FORGOTPASS_RESENDVERIFICATION);

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            myAndroidHttpTransport = null;

            request.addProperty("userName", Utility.getUserName(ForgotPasswordverificationActivity.this));
            request.addProperty("EmailID", Utility.getemailid(ForgotPasswordverificationActivity.this));



            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.FORGOTPASS_RESENDVERIFICATION, mySoapEnvelop);

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

                    if (!Utility.isInternetConnected(ForgotPasswordverificationActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                            boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                    "IsSucceed").toString());

                            Utility.saveVerificationCode(ForgotPasswordverificationActivity.this, soapObject.getPrimitivePropertySafelyAsString("VerificationCode"));

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

                Toast.makeText(ForgotPasswordverificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }
}
