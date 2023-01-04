package skyward.pp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import skyward.pp.adapter.ProductListAdapter;
import skyward.pp.model.ProductClass;
import skyward.pp.util.Utility;

public class MasterProductListActivity extends AppCompatActivity {

    int flowrateID,producttypeID,typeID,inletID,outletID,voltID;
    Double headID=0.0,powerID=0.0,powerhpID=0.0,headfeetID=0.0;
    String ProductImage,ProductName, ProductID;
    ArrayList<ProductClass> ProductList;
    ProductClass product;
    ListView listproducts;
    int send_producttype,send_type,send_flowrate,send_inlet,send_outlet,send_volt;
    Double send_head,send_headfeet,send_power,send_powerhp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_productlist);

        setTitle("Products");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Products");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MasterProductListActivity.this, ProductsmasterActivity.class);
                startActivity(i);
                finish();
            }
        });

        listproducts = (ListView) findViewById(R.id.listproductsmaster);

        Bundle b = getIntent().getExtras();
        powerID=b.getDouble("Power");
        send_power=powerID;
        flowrateID=b.getInt("FlowRate");
        send_flowrate=flowrateID;
        headID=b.getDouble("Head");
        send_head=headID;
        producttypeID=b.getInt("ProductType");
        send_producttype=producttypeID;
        typeID=b.getInt("Type");
        send_type=typeID;
        inletID=b.getInt("Inlet");
        send_inlet=inletID;
        outletID=b.getInt("Outlet");
        send_outlet=outletID;
        headfeetID=b.getDouble("HeadFeet");
        send_headfeet=headfeetID;
        powerhpID=b.getDouble("PowerHP");
        send_powerhp=powerhpID;
        voltID=b.getInt("Volt");
        send_volt=voltID;

        if (!Utility.isInternetConnected(MasterProductListActivity.this)) {

            Toast.makeText(MasterProductListActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        } else {
            new FetchProductsTask().execute();

        }

        listproducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductClass obj=ProductList.get(position);
                Intent i=new Intent(MasterProductListActivity.this,ProductDetailsActivity.class);
                Bundle b = new Bundle();
                b.putDouble("Power", send_power);
                b.putInt("FlowRate",send_flowrate);
                b.putDouble("Head", send_head);
                b.putInt("ProductType", send_producttype);
                b.putInt("Type", send_type);
                b.putInt("Inlet", send_inlet);
                b.putInt("Outlet", send_outlet);
                b.putDouble("HeadFeet", send_headfeet);
                b.putDouble("PowerHP", send_powerhp);
                b.putInt("Volt", send_volt);
                b.putInt("productID", Integer.valueOf(obj.getProduct_id()));
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });

    }


/////////////////////////get Product List Task//////////////////////////////


    class FetchProductsTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(MasterProductListActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTSBYCATEGORIES);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("productTypeID",producttypeID);
            request.addProperty("powerID",powerID+"");
            request.addProperty("typeID",typeID);
            request.addProperty("headID",headID+"");
            request.addProperty("flowRateID",flowrateID);
            request.addProperty("InletID",inletID);
            request.addProperty("OutletID",outletID);
            request.addProperty("PowerHPID",powerhpID+"");
            request.addProperty("HeadFeetID",headfeetID+"");
            request.addProperty("voltID",voltID);

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
                            + Utility.GET_PRODUCTSBYCATEGORIES, mySoapEnvelop);


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
                        ProductList = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            ProductName = soapResult.getPrimitivePropertySafelyAsString("ProductName").toString();
                            ProductImage = soapResult.getPrimitivePropertySafelyAsString("ImagePath").toString();
                            ProductID = soapResult.getPrimitivePropertySafelyAsString("ID").toString();

                            product = new ProductClass(ProductName, ProductImage, ProductID, getApplicationContext());
                            ProductList.add(product);
                        }

                        listproducts.setAdapter(new ProductListAdapter(getApplicationContext(), ProductList));

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

                Toast.makeText(MasterProductListActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //////////////////////////////////////////////////////////////////////////


    @Override
    public void onBackPressed() {

        Intent i = new Intent(MasterProductListActivity.this, ProductsmasterActivity.class);
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
