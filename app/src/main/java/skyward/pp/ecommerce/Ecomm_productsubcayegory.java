package skyward.pp.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
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

import skyward.pp.R;
import skyward.pp.adapter.Ecomm_productsubcategoryAdapter;
import skyward.pp.model.ProductSubCategoryClass;
import skyward.pp.util.Utility;

public class Ecomm_productsubcayegory extends NavigationHeader {

    ListView listcategories;
    ArrayList<ProductSubCategoryClass> listproducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ecomm_productsubcayegory, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Sub Categories");
        ab.setHomeButtonEnabled(true);
        listproducts=new ArrayList<>();
        listcategories = (ListView) findViewById(R.id.listcategories1);
        if(Utility.isInternetConnected(getApplicationContext()))
        {
            FetchProductSubCategory task=new FetchProductSubCategory();
            task.execute();
        }
        else
        {
            Toast.makeText(Ecomm_productsubcayegory.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

        }
        listcategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductSubCategoryClass cls=listproducts.get(position);
                int subid=cls.getId();
                Utility.saveProductSubCategoryId(getApplicationContext(),subid);
                Intent intent=new Intent(getApplicationContext(),EcommSubCategoryActivity.class);
                startActivity(intent);

            }
        });

    }



    class FetchProductSubCategory extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(Ecomm_productsubcayegory.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTSUBCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ProductcategoryID",Utility.getProductCategoryId(getApplicationContext()));


            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_PRODUCTSUBCATEGORY);

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
                            + Utility.GET_PRODUCTSUBCATEGORY, mySoapEnvelop);


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

                        listproducts=new ArrayList<>();
                        //customer.clear();
                        //customerID.clear();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            String name=soapResult.getPropertySafelyAsString("Name", "")
                                    .toString();
                            int id=Integer.valueOf(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                            listproducts.add(new ProductSubCategoryClass(id,name));
                        }

                        if(listproducts.size()>0) {
                            listcategories.setAdapter(new Ecomm_productsubcategoryAdapter(getApplicationContext(), listproducts));
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
                }
            }else {

                Toast.makeText(Ecomm_productsubcayegory.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),EcommCategoryActivity.class
        );
        startActivity(intent);
        finish();
    }
}
