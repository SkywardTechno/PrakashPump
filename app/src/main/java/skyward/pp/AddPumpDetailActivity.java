package skyward.pp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import skyward.pp.util.Utility;

public class AddPumpDetailActivity extends AppCompatActivity {

    Button add , btnscanqr;
    EditText  serialno,invoiceno;
    EditText gstart, gend,service, pd_warranty_end,pd_warranty_start,scanQR,invoicedate;
    AutoCompleteTextView modelno;
    ImageView img_modelscan,img_serialscan;
    LinearLayout ll_warranty,ll_guarantee;
    private ArrayList<String> servicelist = new ArrayList<String>();
    private ArrayList<Integer> servicelistID = new ArrayList<Integer>();
    public int tempservicelist =0;
    Calendar c, cg, cw;
    String st="",en="";
    Date onedate;
    Date twodate;
    String serialNo;
    String Guarantee="",Warranty="";
    int scanid=0;
    String prefix="";
    ArrayAdapter<String> adaptersearch;
    public static ArrayList<String> comtemp = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pumpdetail);

        setTitle("Add Pump Detail");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Pump Detail");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AddPumpDetailActivity.this, PumpDetailActivity.class);
                startActivity(i);
                finish();

            }
        });

        add = (Button) findViewById(R.id.btn_addpumpdetail);
        modelno = (AutoCompleteTextView) findViewById(R.id.pd_modelno);
        serialno = (EditText) findViewById(R.id.pd_serialno);
        invoiceno = (EditText) findViewById(R.id.pd_invoiceno);
        gstart = (EditText) findViewById(R.id.pd_guarantee_start);
        gend = (EditText) findViewById(R.id.pd_guarantee_end);
        pd_warranty_end = (EditText) findViewById(R.id.pd_warranty_end);
        pd_warranty_start = (EditText) findViewById(R.id.pd_warranty_start);
        invoicedate = (EditText) findViewById(R.id.pd_invoicedate);

        service = (EditText) findViewById(R.id.pd_service);
        img_modelscan= (ImageView) findViewById(R.id.img_modelscan);
        img_serialscan= (ImageView) findViewById(R.id.img_serialscan);

        ll_warranty = (LinearLayout) findViewById(R.id.ll_warranty);
        ll_guarantee = (LinearLayout) findViewById(R.id.ll_guarantee);

        if(!Utility.isInternetConnected(AddPumpDetailActivity.this)){
            Toast.makeText(AddPumpDetailActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }else{
            new FetchService().execute();
        }
        final Activity activity=this;

/*
        btnscanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator i=new IntentIntegrator(activity);
                i.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                i.setPrompt("Scan");
                i.setCameraId(0);
                i.setBeepEnabled(true);
                i.setBarcodeImageEnabled(true);
                i.initiateScan();

            }
        });
*/

        img_modelscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanid = 1;
                IntentIntegrator i=new IntentIntegrator(activity);
                i.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                i.setPrompt("Scan");
                i.setCameraId(0);
                i.setBeepEnabled(true);
                i.setBarcodeImageEnabled(true);
                i.initiateScan();
            }
        });

        img_serialscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanid=2;
                IntentIntegrator i=new IntentIntegrator(activity);
                i.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                i.setPrompt("Scan");
                i.setCameraId(0);
                i.setBeepEnabled(true);
                i.setBarcodeImageEnabled(true);
                i.initiateScan();
            }
        });

        cg = Calendar.getInstance();
        cw = Calendar.getInstance();
        c = Calendar.getInstance();
        // c.set(Calendar.DAY_OF_MONTH, 1);
        //setCurrentTime(etFromtime);
        int Date = cg.get(Calendar.DATE);
        int Date1 = cw.get(Calendar.DATE);
        invoicedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddPumpDetailActivity.this, invoicedatedt, c
                        .get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
                //displayDatePicker(date);
            }
        });

/*
        pd_warranty_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddPumpDetailActivity.this, wrstartdate, c
                        .get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
                //displayDatePicker(date);
            }
        });
*/

       /* gend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddPumpDetailActivity.this, genddate, c
                        .get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
                //displayDatePicker(date);
            }
        });*/



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                //Toast.makeText(AddPumpDetailActivity.this, "Pump details added successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] countrys = servicelist.toArray(new String[servicelist
                        .size()]);
                alertservice(countrys, v, service);
            }
        });
/*
        final Activity activity=this;
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator i=new IntentIntegrator(activity);
                i.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                i.setPrompt("Scan");
                i.setCameraId(0);
                i.setBeepEnabled(false);
                i.setBarcodeImageEnabled(false);
                i.initiateScan();
            }
        });
*/


        modelno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                prefix = s.toString();
                if (prefix.contains("'")) {
                    prefix.replace("'", "\'");
                }
                invoicedate.setText("");
                gstart.setText("");
                gend.setText("");
                pd_warranty_end.setText("");
                pd_warranty_start.setText("");
                if (Utility.isInternetConnected(getApplicationContext())) {
                    FetchsearchresultTask task = new FetchsearchresultTask();
                    task.execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        modelno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result!=null)
        {
            if(result.getContents()==null)
            {
               // Toast.makeText(this,"Cancelled",Toast.LENGTH_LONG).show();
            }
            else
            {
                if(scanid == 1){
                    modelno.setText(result.getContents().toString());

                }else if(scanid == 2){
                    serialno.setText(result.getContents().toString());

                }

                // Toast.makeText(this,"Scanned..."+result.getContents(),Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



/*
    private void displayDatePicker(final TextView gstart) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        try {
                            gstart.setText(Utility.getFormattedDate(year, (monthOfYear + 1), dayOfMonth));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, year, month, day);

        dpd.show();
    }
*/

    final DatePickerDialog.OnDateSetListener invoicedatedt = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            cg.set(Calendar.YEAR, year);
            cg.set(Calendar.MONTH, monthOfYear);
            cg.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            cw.set(Calendar.YEAR, year);
            cw.set(Calendar.MONTH, monthOfYear);
            cw.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

            SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.getDefault());


            invoicedate.setText(sdf.format(c.getTime()));
            if(Utility.isInternetConnected(getApplicationContext())){
               new  getguarantee().execute();
            }



            st = sdf.format(c.getTime());


        }

    };

/*
    final DatePickerDialog.OnDateSetListener wrstartdate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

            SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.getDefault());


            pd_warranty_start.setText(sdf.format(c.getTime()));
            if(Utility.isInternetConnected(getApplicationContext())){
                new  getwarranty().execute();
            }




            st = sdf.format(c.getTime());


        }

    };
*/

    final DatePickerDialog.OnDateSetListener genddate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, monthOfYear);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

            SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat, Locale.getDefault());


            gend.setText(sdf.format(c.getTime()));
            en = sdf.format(c.getTime());


        }

    };

    private void alertservice(final String[] visitType, View v,
                              final TextView tvVisitType2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                AddPumpDetailActivity.this);
        // Source of the data in the DIalog

        // Set the dialog title
        builder.setTitle("Select Service")
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

                                for (int i = 0; i < servicelist.size(); i++) {
                                    if (servicelist.get(i).toString()
                                            .equals(visitType[which])) {
                                        tempservicelist = servicelistID.get(i);

                                    }

                                }
                                tvVisitType2.setText(visitType[which]);
                                dialog.dismiss();



                            }
                        });

        // Set the action buttons
        builder.show();

    }


    public void init() {

        boolean isError = false;
        String errorMsg = "Invalid Data";
       /* if (modelno.getText().toString().equals("")) {
            isError = true;
            errorMsg = "Please enter Modelno";
        }  else*/
        if (serialno.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter serial no Or Scan QR";
        } else if (invoiceno.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter Invoice No";
        }else if (invoicedate.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter Select Invoice Date";
        }else if (modelno.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter Model No";
        }/*else if (gstart.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter guarantee start date";
        }else if (gend.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please enter guarantee end date";
        }*//*else if (service.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Please select service";
        }*/

        if (isError) {
            Toast.makeText(AddPumpDetailActivity.this, errorMsg,
                    Toast.LENGTH_SHORT).show();
        } else {
            new AddDetail().execute();
        }

       /* if(gend.getText().toString().isEmpty() || gstart.getText().toString().isEmpty()){
            isError = true;
            errorMsg = "Guarantee dates cannot be empty";

        }else {
            String pat = "dd-MMM-yyyy";
            SimpleDateFormat formatting = new SimpleDateFormat(pat);

            try {
                onedate = formatting.parse(st);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                twodate = formatting.parse(en);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (twodate.before(onedate)) {

                isError = true;
                errorMsg = "Guarantee end date cannot be before start date";
            }

        }*/
    }

    class FetchsearchresultTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MODELNO);
            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ModelNum", prefix);
            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_MODELNO);

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
                            + Utility.GET_MODELNO, mySoapEnvelop);


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

                        comtemp = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            comtemp.add(soapResult.getPropertySafelyAsString("ModelNum", "")
                                    .toString());

                        }

                        if (comtemp.size() > 0) {

                            adaptersearch = new ArrayAdapter<String>(AddPumpDetailActivity.this, R.layout.companydropdown, comtemp);

                            modelno.setAdapter(adaptersearch);
                            modelno.setThreshold(1);


                        } else {


                        }
                    }
                }
                catch (Exception e)
                {

                }
            }else {

                Toast.makeText(AddPumpDetailActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class FetchService extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddPumpDetailActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_SERVICE);



            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_SERVICE);

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
                            + Utility.GET_SERVICE, mySoapEnvelop);


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
                        //customer.clear();
                        //customerID.clear();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);

                            servicelist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());

                            servicelistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(AddPumpDetailActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class getguarantee extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddPumpDetailActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GEt_GUARANTEE);


            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ModelNum",modelno.getText().toString());


            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GEt_GUARANTEE);

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
                            + Utility.GEt_GUARANTEE, mySoapEnvelop);


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

            if(result != null) {
                progress.dismiss();
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

                            Guarantee = soapResult.getPrimitivePropertySafelyAsString("Guarantee");
                            Warranty = soapResult.getPrimitivePropertySafelyAsString("Warranty");
                        }

                        if(Guarantee.isEmpty()){
                           /* Toast.makeText(getApplicationContext(),
                                    "No Guarantee for this product".toString(),
                                    Toast.LENGTH_LONG).show();*/
                        }else {
                            gstart.setText(invoicedate.getText().toString());
                            String dt = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
                            cg.add(Calendar.MONTH, Integer.valueOf(Guarantee));  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                            cg.add(Calendar.DAY_OF_MONTH,-1);
                            String output = new SimpleDateFormat("dd-MMM-yyyy").format(cg.getTime());

                            gend.setText(output);
                        }

                        if(Warranty.isEmpty()){
/*
                            Toast.makeText(getApplicationContext(),
                                    "No Warranty for this product".toString(),
                                    Toast.LENGTH_LONG).show();
*/
                        }else {
                            pd_warranty_start.setText(invoicedate.getText().toString());
                            String dt = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
                            cw.add(Calendar.MONTH, Integer.valueOf(Warranty));  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                            cw.add(Calendar.DAY_OF_MONTH,-1);

                            String output = new SimpleDateFormat("dd-MMM-yyyy").format(cw.getTime());

                            pd_warranty_end.setText(output);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{
                progress.dismiss();
                Toast.makeText(AddPumpDetailActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class getwarranty extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddPumpDetailActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GEt_GUARANTEE);


            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ModelNum",modelno.getText().toString());


            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GEt_GUARANTEE);

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
                            + Utility.GEt_GUARANTEE, mySoapEnvelop);


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

            if(result != null) {
                progress.dismiss();
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

                            Guarantee = soapResult.getPrimitivePropertySafelyAsString("Guarantee");
                            Warranty = soapResult.getPrimitivePropertySafelyAsString("Warranty");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{
                progress.dismiss();
                Toast.makeText(AddPumpDetailActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class AddDetail extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddPumpDetailActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.ADD_PUMPDETAILS);

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            myAndroidHttpTransport = null;

            request.addProperty("token", Utility.getAuthToken(AddPumpDetailActivity.this));
            request.addProperty("serialNo", serialno.getText().toString());
            request.addProperty("invoiceNo",invoiceno.getText().toString() );
            request.addProperty("guaranteeStartDate", gstart.getText().toString());
            request.addProperty("guaranteeEndDate", gend.getText().toString());
            request.addProperty("serviceTypeID", tempservicelist);
            request.addProperty("remarks", "");
            request.addProperty("ModelNum", modelno.getText().toString());
            request.addProperty("warrantyStartDate", pd_warranty_start.getText().toString());
            request.addProperty("warrantyEndDate", pd_warranty_end.getText().toString());
            request.addProperty("InvoiceDate", invoicedate.getText().toString());


            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.ADD_PUMPDETAILS, mySoapEnvelop);

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
            if(result !=null) {
                try {

                    if (!Utility.isInternetConnected(AddPumpDetailActivity.this)) {
                        Toast.makeText(getApplicationContext(), "Please Check your Internet Connection", Toast.LENGTH_LONG).show();
                    } else {

                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                            boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
                                    "IsSucceed").toString());

                            Toast.makeText(AddPumpDetailActivity.this, "PumpDetails added successfully", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(AddPumpDetailActivity.this, PumpDetailActivity.class);
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
                Toast.makeText(AddPumpDetailActivity.this, "Check your Intenet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddPumpDetailActivity.this, PumpDetailActivity.class);
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
