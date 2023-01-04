package skyward.pp.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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
import skyward.pp.adapter.EcommSubCategoryAdapter;
import skyward.pp.model.EcommSubcategoryClass;
import skyward.pp.model.OffersPromotionsclass;
import skyward.pp.util.Utility;

public class EcommOffersCategoryActivity extends NavigationHeader {

    private TextView ui_hot = null;
    int hot_number=0;
    GridView gridsubcategory;
    ImageView offersimg;
    ArrayList<EcommSubcategoryClass> listproduct;
    String productname,imagepath,finalpathimg,videopath,finalpathvideo;
    OffersPromotionsclass offerspromotions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ecomm_offers_category);
        getLayoutInflater().inflate(R.layout.activity_ecomm_offers_category, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Products");
        ab.setHomeButtonEnabled(true);

        listproduct=new ArrayList<>();
        offersimg = (ImageView) findViewById(R.id.Offersimage);
        gridsubcategory = (GridView) findViewById(R.id.grid_offersproducts);
        String filepath=Utility.getofferimagepath(getApplicationContext());
       String finalpath= Utility.URLFORIMAGE+filepath;

        gridsubcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int a=listproduct.size();
                try
                {
                    int productid=Integer.valueOf(listproduct.get(i).getProduct_id());
                    Utility.saveProductId(getApplicationContext(),productid);

                    Utility.savefromdashboardorcategory(getApplicationContext(), 3);

                    // Toast.makeText(MyVideosActivity.this, "VideoClicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), EcommProductSpecificationActivity.class);
                    //    intent.putExtra("ID", offerspromotions.getId());

                    startActivity(intent);
                    finish();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_LONG).show();
                }

            }
        });
        //MyVideos videos = arrayList.get(position);
//        ImageView iv = (ImageView ) convertView.findViewById(R.id.imagePreview);
       /* ContentResolver crThumb = mContext.getContentResolver();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
        item.getDisplayvideo().setImageBitmap(curThumb);*/



        Glide.with(getApplicationContext())
                .load(finalpath)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        if(Utility.isInternetConnected(getApplicationContext()))
                        {
                            FetchProductsByProducTypeID task1 = new FetchProductsByProducTypeID();
                            task1.execute();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        if(Utility.isInternetConnected(getApplicationContext()))
                        {
                            FetchProductsByProducTypeID task=new FetchProductsByProducTypeID();
                            task.execute();
                        }
                        return false;
                    }
                })
                .into(offersimg);
      /*  try {
            Glide.with(EcommOffersCategoryActivity.this).load(finalpath).into(offersimg);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
*/

    }

    class FetchProductsByProducTypeID extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommOffersCategoryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_OFFERSPROMOTIONPRODUCTBYOFFERID);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("OfferID",Utility.getOfferId(getApplicationContext()));


            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_OFFERSPROMOTIONPRODUCTBYOFFERID);

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
                            + Utility.GET_OFFERSPROMOTIONPRODUCTBYOFFERID, mySoapEnvelop);


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

                        listproduct=new ArrayList<>();
                        //customer.clear();
                        //customerID.clear();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            String name=soapResult.getPropertySafelyAsString("ProductName", "")
                                    .toString();
                            int id=Integer.valueOf(Integer.parseInt(soapResult.getPropertySafelyAsString(

                                    "ID").toString()));

                            String imagepath=soapResult.getPropertySafelyAsString("ImagePath", "")
                                    .toString();
                            listproduct.add(new EcommSubcategoryClass(name,imagepath,id+"",getApplicationContext()));
                        }

                        if(listproduct.size()>0) {
                            gridsubcategory.setAdapter(new EcommSubCategoryAdapter(getApplicationContext(), listproduct));
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "No Offers Yet!",
                                    Toast.LENGTH_LONG).show();

                        }




                    } else {

                        Toast.makeText(getApplicationContext(),
                                "No Offers Yet!",
                                Toast.LENGTH_LONG).show();

                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }else {

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Utility.getfromdashboardorcategory(getApplicationContext())==1)
        {
            Intent intent=new Intent(getApplicationContext(),EcommDashboardActivity.class
            );
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent=new Intent(getApplicationContext(),EcommOffersActivity.class
            );
            startActivity(intent);
            finish();
        }

    }
}
