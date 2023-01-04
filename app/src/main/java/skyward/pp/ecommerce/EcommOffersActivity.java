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
import skyward.pp.adapter.OffersPromotionsAdapter;
import skyward.pp.model.OffersPromotionsclass;
import skyward.pp.util.Utility;

public class EcommOffersActivity extends NavigationHeader {

    ListView listoffers;
    ArrayList<OffersPromotionsclass> offerslist;
    OffersPromotionsclass offerspromotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ecomm_offers, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Offers & Promotions");
        ab.setHomeButtonEnabled(true);


        listoffers = (ListView) findViewById(R.id.listoffers);


        if(Utility.isInternetConnected(getApplicationContext()))
        {
            FetchProductDetailsTask task=new FetchProductDetailsTask();
            task.execute();
        }
        listoffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                offerspromotions = (OffersPromotionsclass) parent.getItemAtPosition(position);


                Utility.savefromdashboardorcategory(getApplicationContext(), 2);
                // Toast.makeText(MyVideosActivity.this, "VideoClicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EcommOffersCategoryActivity.class);
                intent.putExtra("ID", offerspromotions.getId());

                String imagepath=offerspromotions.getOfferimage();
                Utility.saveofferimagepath(getApplicationContext(),imagepath);
                Utility.saveOfferId(getApplicationContext(),Integer.valueOf(offerspromotions.getId()));
                startActivity(intent);
                finish();
            }
        });


    }



    class FetchProductDetailsTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommOffersActivity .this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_OFFERSBYUSERID);


            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));


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
                            + Utility.GET_OFFERSBYUSERID, mySoapEnvelop);


                } catch (XmlPullParserException e) {
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    // System.out.println("XmlPullParserException 0");
                } catch (SocketTimeoutException e) {
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    // System.out.println("SocketTimeoutException 1");
                } catch (SocketException e) {

                    e.printStackTrace();
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
                        SoapObject result5 = (SoapObject) result4.getProperty(0);
                        System.out.println("Result4 is : " + result4.toString());
                        int count = result4.getPropertyCount();
                        System.out.println("Count is : " + count);
                        offerslist = new ArrayList<>();

                        for(int i=0;i<count;i++)
                        {
                            SoapObject obj= (SoapObject) result4.getProperty(i);
                            String imagepath=obj.getPropertySafelyAsString("ImagePath","");
                            int id=Integer.valueOf(obj.getPropertySafelyAsString("ID","0"));
                            String decs=obj.getPropertySafelyAsString("OfferName","");
                            offerslist.add(new OffersPromotionsclass(id+"",decs,imagepath));
                        }



                if(offerslist.size()>0)
                {
                  listoffers.setAdapter(new OffersPromotionsAdapter(getApplicationContext(), offerslist));

                }else{
                    Toast.makeText(getApplicationContext(),
                            "No Offers Yet !",
                            Toast.LENGTH_LONG).show();

                }

                    } else {

                        Toast.makeText(getApplicationContext(),
                                "No Offers Yet !",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }else{

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),EcommDashboardActivity.class
        );
        startActivity(intent);
        finish();
    }
}
