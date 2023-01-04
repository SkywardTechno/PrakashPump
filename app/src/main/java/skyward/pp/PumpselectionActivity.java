package skyward.pp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import skyward.pp.util.Utility;

public class PumpselectionActivity extends BaseActivity {

    int powerID,flowrateID,headID,producttypeID,typeID,powerhpID,inletID,outletID,headfeetID,voltID;
    ImageView showimg;
    Spinner spin_inlet,spin_outlet,spin_flowwrate,spin_productype,spin_type,spin_volt;
    EditText spin_head,spin_headfeet,spin_power,spin_powerhp;
    RadioGroup yesno;
    Button go;
    RadioButton yes, no;
    String Text;
    int val =0;
    Context context;
    private ArrayList<String> powerlist = new ArrayList<String>();
    private ArrayList<Integer>  powerlistid = new ArrayList<Integer>();
    private ArrayList<String> powerhplist = new ArrayList<String>();
    private ArrayList<Integer>  powerhplistid = new ArrayList<Integer>();
    private ArrayList<String> inletlist = new ArrayList<String>();
    private ArrayList<Integer>  inletlistid = new ArrayList<Integer>();
    private ArrayList<String> outletlist = new ArrayList<String>();
    private ArrayList<Integer>  outletlistid = new ArrayList<Integer>();
    private ArrayList<String> headlist = new ArrayList<String>();
    private ArrayList<Integer>  headlistid = new ArrayList<Integer>();
    private ArrayList<String> headfeetlist = new ArrayList<String>();
    private ArrayList<Integer>  headfeetlistid = new ArrayList<Integer>();
    private ArrayList<String> flowratelist = new ArrayList<String>();
    private ArrayList<Integer>  flowratelistid = new ArrayList<Integer>();
    private ArrayList<String> producttypelist = new ArrayList<String>();
    private ArrayList<Integer>  producttypelistid = new ArrayList<Integer>();
    private ArrayList<String> typelist = new ArrayList<String>();
    private ArrayList<Integer>  typelistid = new ArrayList<Integer>();
    private ArrayList<String> voltlist = new ArrayList<String>();
    private ArrayList<Integer>  voltlistid = new ArrayList<Integer>();
    Boolean keepChangingText = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pumpselection);
        setTitle("Pump Selection");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Pump Selection");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PumpselectionActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        spin_power = (EditText) findViewById(R.id.spin_pumpselection_power);
        spin_head = (EditText) findViewById(R.id.spin_pumpselection_head);
        spin_flowwrate = (Spinner) findViewById(R.id.spin_pumpselection_flowrate);
        spin_productype = (Spinner) findViewById(R.id.spin_pumpselection_producttype);
        spin_inlet = (Spinner) findViewById(R.id.spin_pumpselection_inlet);
        spin_outlet = (Spinner) findViewById(R.id.spin_pumpselection_outlet);
        spin_headfeet = (EditText) findViewById(R.id.spin_pumpselection_headfeet);
        spin_powerhp = (EditText) findViewById(R.id.spin_pumpselection_powerhp);
        spin_volt = (Spinner) findViewById(R.id.spin_pumpselection_volt);

        spin_type = (Spinner) findViewById(R.id.spin_pumpselection_type);
        go = (Button) findViewById(R.id.btn_pumpselection_go);
        yesno = (RadioGroup) findViewById(R.id.pumpselection_yesno);
        yes = (RadioButton) findViewById(R.id.pumpselection_yes);
        no = (RadioButton) findViewById(R.id.pumpselection_no);
        no.setChecked(true);

        /*spin_power.setText("0.00");
        spin_powerhp.setText("0.00");
        spin_head.setText("0.00");
        spin_headfeet.setText("0.00");*/

        yesno.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {

                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.pumpselection_yes:
                                val = 1;
                                showAlert();
                                break;
                            case R.id.pumpselection_no:
                                val = 0;
                                //Toast.makeText(PumpselectionActivity.this, "Noo", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });


        spin_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                typeID = typelistid.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_productype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                producttypeID=producttypelistid.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spin_flowwrate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                flowrateID=flowratelistid.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_inlet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                inletID=inletlistid.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_outlet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                outletID=outletlistid.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_volt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                voltID=voltlistid.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    go.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if(spin_powerhp.getText().toString().isEmpty()){
                spin_powerhp.setText("0.00");
            }

            if(spin_power.getText().toString().isEmpty()){
                spin_power.setText("0.00");
            }

            if(spin_head.getText().toString().isEmpty()){
                spin_head.setText("0.00");
            }

            if(spin_headfeet.getText().toString().isEmpty()){
                spin_headfeet.setText("0.00");
            }

            Bundle b = new Bundle();
            b.putDouble("Power", Double.valueOf(spin_power.getText().toString()));
            b.putInt("FlowRate", flowrateID);
            b.putDouble("Head", Double.valueOf(spin_head.getText().toString()));
            b.putInt("ProductType", producttypeID);
            b.putInt("Type", typeID);
            b.putInt("Inlet", inletID);
            b.putInt("Outlet", outletID);
            b.putDouble("HeadFeet", Double.valueOf(spin_headfeet.getText().toString()));
            b.putDouble("PowerHP", Double.valueOf(spin_powerhp.getText().toString()));
            b.putInt("Volt", voltID);


            if (val == 0) {
                b.putString("TcDetails", "");
                b.putString("required", "false");
                Intent t = new Intent(PumpselectionActivity.this, ProductsListActivity.class);
                t.putExtras(b);

                startActivity(t);
                finish();
            } else {
                b.putString("TcDetails", Text);
                b.putString("required", "true");
                if (Text.isEmpty()) {
                    createAndShowAlertDialog();
                } else {
                    Intent t = new Intent(PumpselectionActivity.this, ProductsListActivity.class);
                    t.putExtras(b);

                    startActivity(t);
                    finish();
                }
            }

        }
    });



        spin_power.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(spin_power.getText().toString().isEmpty() || spin_power.getText().toString().equalsIgnoreCase("0.0")){
                    if (keepChangingText) {
                        keepChangingText = false;

                        spin_powerhp.setText("0.00");
                    } else {
                        keepChangingText = true;
                    }
                }else{
                    if (keepChangingText) {
                        keepChangingText = false;
                        Double powerhp = Double.valueOf(spin_power.getText().toString()) * 1.34;
                        spin_powerhp.setText(String.format("%.2f", powerhp));
                    } else {
                        keepChangingText = true;
                    }

                }

            }
        });



        spin_powerhp.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(spin_powerhp.getText().toString().isEmpty() || spin_powerhp.getText().toString().equalsIgnoreCase("0.0")){
                    if (keepChangingText) {
                        keepChangingText = false;

                        spin_power.setText("0.00");
                    } else {
                        keepChangingText = true;
                    }
                }else{
                    if (keepChangingText) {
                        keepChangingText = false;
                        Double power = Double.valueOf(spin_powerhp.getText().toString()) * 0.75;
                        spin_power.setText(String.format( "%.2f", power ));
                    } else {
                        keepChangingText = true;
                    }

                }            }
        });


        spin_head.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(spin_head.getText().toString().isEmpty() || spin_head.getText().toString().equalsIgnoreCase("0.0")){
                    if (keepChangingText) {
                        keepChangingText = false;

                        spin_headfeet.setText("0.00");
                    } else {
                        keepChangingText = true;
                    }
                }else{
                    if (keepChangingText) {
                        keepChangingText = false;
                        Double headfeet = Double.valueOf(spin_head.getText().toString()) * 3.28;
                        spin_headfeet.setText(String.format( "%.2f", headfeet ));
                    } else {
                        keepChangingText = true;
                    }

                }

            }
        });

        spin_headfeet.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(spin_headfeet.getText().toString().isEmpty() || spin_headfeet.getText().toString().equalsIgnoreCase("0.0")){
                    if (keepChangingText) {
                        keepChangingText = false;

                        spin_head.setText("0.00");
                    } else {
                        keepChangingText = true;
                    }
                }else{
                    if (keepChangingText) {
                        keepChangingText = false;
                        Double head = Double.valueOf(spin_headfeet.getText().toString()) * 0.30;
                        spin_head.setText(String.format( "%.2f", head ));
                    } else {
                        keepChangingText = true;
                    }

                }

            }
        });



        if (!Utility.isInternetConnected(PumpselectionActivity.this)) {

            Toast.makeText(PumpselectionActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            new FetchFlowRate().execute();
            new FetchProductType().execute();
            new FetchType().execute();
            new FetchInlet().execute();
            new FetchOutlet().execute();
            new FetchVolt().execute();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(PumpselectionActivity.this, DashboardActivity.class);
        startActivity(i);
        finish();
    }


/*
    public void onPSRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
           /* case R.id.pumpselection_yes:
                if (checked) {
                    val = 1;*/
                  /*  showAlert();

//                Intent i=new Intent(PumpselectionActivity.this,SupportInquiryActivity.class);
//                i.putExtra("servicetypeID",servicetypeid);
//                startActivity(i);


                    break;
                }
            case R.id.pumpselection_no:

                if (checked)
                {

                    val = 0;
                }
//                    finish.setVisibility(View.VISIBLE);
//                Toast.makeText(PumpselectionActivity.this, no.getText(), Toast.LENGTH_SHORT).show();
//
//                // Ninjas rule
      /*          break;
        }
    }*//*
*//*
*/


    private void createAndShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PumpselectionActivity.this);
        builder.setTitle("Add Details ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
                showAlert();


            }
        });
        builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
                val = 0;
                no.setChecked(true);
                Bundle b = new Bundle();
                b.putInt("Power",powerID);
                b.putInt("FlowRate", flowrateID);
                b.putInt("Head", headID);
                b.putInt("ProductType", producttypeID);
                b.putInt("Type", typeID);
                b.putInt("Inlet", inletID);
                b.putInt("Outlet", outletID);
                b.putInt("HeadFeet", headfeetID);
                b.putInt("PowerHP", powerhpID);
                b.putString("TcDetails","");
                b.putString("required" ,"false");
                Intent t = new Intent(PumpselectionActivity.this, ProductsListActivity.class);
                t.putExtras(b);

                startActivity(t);
                finish();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlert(){
        final Dialog dialog = new Dialog(PumpselectionActivity.this);
        dialog.setContentView(R.layout.alert_layout);
        dialog.setTitle("Enter Details");
        dialog.setCancelable(false);

        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.input);

        TextView positive = (TextView) dialog.findViewById(R.id.btn_positive);
        TextView negative = (TextView) dialog.findViewById(R.id.btn_negative);
        // if button is clicked, close the custom dialog
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                val =0;
                no.setChecked(true);
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(text.getText().toString().isEmpty()){
                    Toast.makeText(PumpselectionActivity.this, "Please enter Details", Toast.LENGTH_SHORT).show();
                }else {
                    Text = text.getText().toString();
                    dialog.dismiss();
                }

            }
        });


        dialog.show();
    }





    ///////////////////////get Flowrate task////////////////////////////////

    class FetchFlowRate extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
           /* progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","FlowRate");
            if(flowratelist.size() > 0){
                flowratelist.clear();
                flowratelistid.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_MISCDATAFROMCATEGORY);

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
                            + Utility.GET_MISCDATAFROMCATEGORY, mySoapEnvelop);


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
           // progress.dismiss();
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
                    flowratelist.add("Select FlowRate");
                    flowratelistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        flowratelist.add(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());

                        flowratelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.spinner_leadsrc, flowratelist);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                    spin_flowwrate.setAdapter(spinnerAdapter);
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

        }
    }

    /////////////////////////////////////////////////////////////////////////



    ////////////////////////////////get Head Taask/////////////////////////


    /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////get productype task/////////////////////////////////////////

    class FetchProductType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
           /* progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTTYPE);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
//            request.addProperty("category","ProductType");
            if(producttypelist.size() > 0){
                producttypelist.clear();
                producttypelistid.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_PRODUCTTYPE);

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
                            + Utility.GET_PRODUCTTYPE, mySoapEnvelop);


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
           // progress.dismiss();
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
                    producttypelist.add("Select ProductType");
                    producttypelistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        producttypelist.add(soapResult.getPropertySafelyAsString("ProductTypeName", "")
                                .toString());

                        producttypelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.spinner_leadsrc, producttypelist);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                    spin_productype.setAdapter(spinnerAdapter);


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

        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////fetch Type TAsk///////////////////////


    class FetchType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Type");
            if(typelist.size() > 0){
                typelist.clear();
                typelistid.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_MISCDATAFROMCATEGORY);

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
                            + Utility.GET_MISCDATAFROMCATEGORY, mySoapEnvelop);


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
                    typelist.add("Select Type");
                    typelistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        typelist.add(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());

                        typelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.spinner_leadsrc, typelist);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                    spin_type.setAdapter(spinnerAdapter);


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

        }
    }

    class FetchInlet extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Inlet");
            if(inletlistid.size() > 0){
                inletlistid.clear();
                inletlist.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_MISCDATAFROMCATEGORY);

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
                            + Utility.GET_MISCDATAFROMCATEGORY, mySoapEnvelop);


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
                    inletlist.add("Select Inlet");
                    inletlistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        inletlist.add(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());

                        inletlistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.spinner_leadsrc, inletlist);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                    spin_inlet.setAdapter(spinnerAdapter);


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

        }
    }

    class FetchOutlet extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Outlet");
            if(outletlist.size() > 0){
                outletlist.clear();
                outletlistid.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_MISCDATAFROMCATEGORY);

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
                            + Utility.GET_MISCDATAFROMCATEGORY, mySoapEnvelop);


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
                    outletlist.add("Select Outlet");
                    outletlistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        outletlist.add(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());

                        outletlistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.spinner_leadsrc, outletlist);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                    spin_outlet.setAdapter(spinnerAdapter);


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

        }
    }



    class FetchVolt extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Volt");
            if(voltlistid.size() > 0){
                voltlistid.clear();
                voltlist.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_MISCDATAFROMCATEGORY);

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
                            + Utility.GET_MISCDATAFROMCATEGORY, mySoapEnvelop);


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
                    voltlist.add("Select Volt");
                    voltlistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        voltlist.add(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());

                        voltlistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.spinner_leadsrc, voltlist);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                    spin_volt.setAdapter(spinnerAdapter);


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

        }
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
