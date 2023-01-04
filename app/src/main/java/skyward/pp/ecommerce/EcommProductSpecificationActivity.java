package skyward.pp.ecommerce;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.ActionBar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

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
import skyward.pp.model.OffersandPromotionDetailClass;
import skyward.pp.util.Utility;

public class EcommProductSpecificationActivity extends NavigationHeader {

    int minqtypass=0,maxqtypass=0,FreeorDiscountpass=0,valuepass=0;
    ArrayList<OffersandPromotionDetailClass> listofferdetials;
    private TextView ui_hot = null;
    int hot_number=0 ;
    int qtaddsubtract=1;
    double cdiscountmnt=0.0;
    int cdiscount=0;
    double cprice=0;
    double totalprice=0;
    double originalprice=0;
    int totalqty=1;
    int cfree=0;

    int productid=0;
    int found=0;
    TextView txt_offer,txt_discount;
    String navigatetext="";
    double discountedprice=0.0;
    TextView spec_productname, spec_modelno,spec_price,spec_applications,spec_specifications,spec_functions, display_quantity, spec_openclose;
    ImageView  wishlisticon;
    ImageButton quantity_add,quantity_subtract;
    LinearLayout addtowishlist, addtocart;
    Button btn_buyproduct;
    TextView headapplication,headspecification,headff;
    ImageView lineapplication,linespecification,lineff;
    String productname,model_no,productype,power,productdescriptiion,type,head,flowrate,imagepath,finalpathimg,videopath,finalpathvideo;
    String applications,functionandfeature,specifications,price,modelno,productcode,currency;
    ArrayList<String> arrimagesurl ;
    SliderLayout sliderLayout;
    String offername="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_ecomm_productspecification, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Product Specification");
        ab.setHomeButtonEnabled(true);


        spec_productname = (TextView) findViewById(R.id.txt_spec_productname);
        spec_modelno = (TextView) findViewById(R.id.txt_spec_modelno);
        spec_price = (TextView) findViewById(R.id.txt_spec_price);
        spec_openclose = (TextView) findViewById(R.id.txt_spec_openclose);
        spec_applications = (TextView) findViewById(R.id.txt_spec_applications);
        spec_specifications = (TextView) findViewById(R.id.txt_spec_specifications);
        spec_functions = (TextView) findViewById(R.id.txt_spec_functions);
        display_quantity = (TextView) findViewById(R.id.txt_spec_display_quantity);
        sliderLayout = (SliderLayout)findViewById(R.id.ps_slider);
        quantity_add = (ImageButton) findViewById(R.id.imgbtn_spec_quantity_add);
        quantity_subtract = (ImageButton) findViewById(R.id.imgbtn_spec_quantity_subtract);
        addtocart = (LinearLayout) findViewById(R.id.ll_add_tocart);
        addtowishlist = (LinearLayout) findViewById(R.id.ll_add_towishlist);
        btn_buyproduct = (Button) findViewById(R.id.btn_spec_buyproduct);
        wishlisticon = (ImageView) findViewById(R.id.wishlisticon);

        txt_offer= (TextView) findViewById(R.id.offerstext);
        txt_discount= (TextView) findViewById(R.id.discounttext);

        headapplication = (TextView) findViewById(R.id.txt_headapp);
        headspecification = (TextView) findViewById(R.id.txt_headspec);
        headff = (TextView) findViewById(R.id.txt_headff);

        lineapplication = (ImageView) findViewById(R.id.lineapplication);
        linespecification = (ImageView) findViewById(R.id.linespecification);
        lineff = (ImageView) findViewById(R.id.lineff);

        txt_discount.setText("");
        txt_offer.setText("");
        listofferdetials=new ArrayList<>();

        if(Utility.isInternetConnected(getApplicationContext()))
        {
            FetchProductimages task = new FetchProductimages();
            task.execute();

        }
        else
        {
            Toast.makeText(EcommProductSpecificationActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

        }
        quantity_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                found=0;
                   qtaddsubtract += 1;
                   double temp=originalprice*qtaddsubtract;
                   totalprice=temp;
                   display_quantity.setText(qtaddsubtract+"");


                for(int i=0;i<listofferdetials.size();i++)
                {
                    int value=listofferdetials.get(i).getValue();
                    int minqt=listofferdetials.get(i).getMinqty();
                    int maxqt=listofferdetials.get(i).getMaxqty();
                    int freeordiscount=listofferdetials.get(i).getFreeordiscount();

                    if(maxqt==0)
                    {
                        if(qtaddsubtract>=minqt)
                        {

                            valuepass=value;
                            FreeorDiscountpass=freeordiscount;

                            found=1;
                        }
                    }
                    else
                    {
                        if(qtaddsubtract>=minqt && qtaddsubtract<=maxqt)
                        {

                            valuepass=value;
                            FreeorDiscountpass=freeordiscount;
                            found=1;

                        }
                    }


                }
                if(FreeorDiscountpass==1)
                {

                    if(found==1)
                    {
                        txt_discount.setText("");

                        txt_discount.setVisibility(View.GONE);
                        // spec_price.setText(price + " "+currency);

                        cdiscount=0;
                        cdiscountmnt=0;
                        cfree=valuepass;
                        txt_offer.setVisibility(View.VISIBLE);
                        txt_offer.setText(offername+"\nYour free quantity will be "+valuepass);

                    }
                    else
                    {
                        txt_discount.setText("");
                        cdiscount=0;
                        cdiscountmnt=0;
                        cfree=0;
                        txt_offer.setVisibility(View.GONE);
                        txt_discount.setVisibility(View.GONE);
                       // txt_offer.setText(offername);           temp
                        txt_discount.setVisibility(View.GONE);

                    }

                }
                else if(FreeorDiscountpass==2)
                {

                    if(found==1)
                    {
                        txt_offer.setVisibility(View.VISIBLE);
                        txt_offer.setText(offername+"\nYour discount will be "+valuepass+"%");
                        double temp1=originalprice*qtaddsubtract;
                        totalprice=temp1;
                        double discountrs=(totalprice*valuepass)/100;
                        double finalprice=totalprice-discountrs;
                        discountedprice=finalprice;
                        String combine="Original Price : "+temp1+" "+currency+ " "+"\n"+"Discount Amount : "+discountrs+" "+currency+ " "+"\n"+"Final Price : "+finalprice+" "+currency+ " ";
                        //       spec_price.setText(combine);

                        txt_discount.setVisibility(View.VISIBLE);
                        txt_discount.setText(combine);

                        display_quantity.setText(qtaddsubtract+"");
                        cdiscount=valuepass;
                        cdiscountmnt=discountrs;
                        cfree=0;
                    }
                    else
                    {
                        cdiscount=0;
                        cdiscountmnt=0;
                        cfree=0;
                        txt_discount.setText("");
                        txt_discount.setVisibility(View.GONE);

                        //  spec_price.setText(price + " "+currency);
                        txt_offer.setText("");
                        txt_offer.setVisibility(View.GONE);
                    }


                }



                           }
        });
        quantity_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qtaddsubtract==1)
                {

                }
                else {
                    qtaddsubtract -= 1;
                    double temp=originalprice*qtaddsubtract;
                    totalprice=temp;
                    display_quantity.setText(qtaddsubtract + "");

                }


                found=0;

                for(int i=0;i<listofferdetials.size();i++)
                {
                    int value=listofferdetials.get(i).getValue();
                    int minqt=listofferdetials.get(i).getMinqty();
                    int maxqt=listofferdetials.get(i).getMaxqty();
                    int freeordiscount=listofferdetials.get(i).getFreeordiscount();

                    if(maxqt==0)
                    {
                        if(qtaddsubtract>=minqt)
                        {

                            valuepass=value;
                            FreeorDiscountpass=freeordiscount;

                            found=1;
                        }
                    }
                    else
                    {
                        if(qtaddsubtract>=minqt && qtaddsubtract<=maxqt)
                        {

                            valuepass=value;
                            FreeorDiscountpass=freeordiscount;
                            found=1;

                        }
                    }


                }

                if(FreeorDiscountpass==1)
                {

                    if(found==1)
                    {
                        txt_discount.setText("");

                        txt_discount.setVisibility(View.GONE);
                        // spec_price.setText(price + " "+currency);

                        cdiscount=0;
                        cdiscountmnt=0;
                        cfree=valuepass;
                        txt_offer.setVisibility(View.VISIBLE);
                        txt_offer.setText(offername+"\nYour free quantity will be "+valuepass);

                    }
                    else
                    {
                        txt_discount.setText("");
                        cdiscount=0;
                        cdiscountmnt=0;
                        cfree=0;
                        txt_offer.setVisibility(View.GONE);
                        txt_discount.setVisibility(View.GONE);
                        // txt_offer.setText(offername);           temp
                        txt_discount.setVisibility(View.GONE);

                    }

                }
                else if(FreeorDiscountpass==2)
                {

                    if(found==1)
                    {
                        txt_offer.setVisibility(View.VISIBLE);
                        txt_offer.setText(offername+"\nYour discount will be "+valuepass+"%");
                        double temp1=originalprice*qtaddsubtract;
                        totalprice=temp1;
                        double discountrs=(totalprice*valuepass)/100;
                        double finalprice=totalprice-discountrs;
                        discountedprice=finalprice;
                        String combine="Original Price : "+temp1+" "+currency+ " "+"\n"+"Discount Amount : "+discountrs+" "+currency+ " "+"\n"+"Final Price : "+finalprice+" "+currency+ " ";

                       // String combine="Original Price : "+temp1+ " "+"\n"+"Discount Amount : "+discountrs+"\n"+"Final Price : "+finalprice;

                       // String combine=temp1+ " "+"\n"+discountrs+"\n"+finalprice;
                        //       spec_price.setText(combine);

                        txt_discount.setVisibility(View.VISIBLE);
                        txt_discount.setText(combine);

                        display_quantity.setText(qtaddsubtract+"");
                        cdiscount=valuepass;
                        cdiscountmnt=discountrs;
                        cfree=0;
                    }
                    else
                    {
                        cdiscount=0;
                        cdiscountmnt=0;
                        cfree=0;
                        txt_discount.setText("");
                        txt_discount.setVisibility(View.GONE);

                        //  spec_price.setText(price + " "+currency);
                        txt_offer.setText("");
                        txt_offer.setVisibility(View.GONE);
                    }


                }




            }
        });
        addtowishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.isInternetConnected(getApplicationContext()))
                {
                    AddtowishlistTask task=new AddtowishlistTask();
                    task.execute();
                }
                else
                {
                    Toast.makeText(EcommProductSpecificationActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

                }

            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Utility.isInternetConnected(getApplicationContext()))
                {
                    AddtocartTask task=new AddtocartTask();
                    task.execute();
                }
                else
                {
                    Toast.makeText(EcommProductSpecificationActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

                }

            }
        });
        btn_buyproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isInternetConnected(getApplicationContext()))
                {
                    btn_buyproduct.setEnabled(false);
                    DeleteOrderSummerytask task=new DeleteOrderSummerytask();
                    task.execute();
                   /* AddtocartTaskBuy task=new AddtocartTaskBuy();
                    task.execute();*/
                }
                else
                {
                    btn_buyproduct.setEnabled(true);
                    Toast.makeText(EcommProductSpecificationActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if(Utility.isInternetConnected(getApplicationContext()))
        {
            Fetchmycartcount task=new Fetchmycartcount();
            task.execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

        }
    }
    class Fetchofferdetails extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommProductSpecificationActivity .this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_OFFERBYPRODUCTANDPRODUCTYPEID);


            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ProductID",Utility.getProductId(getApplicationContext()));
            request.addProperty("ProductTypeID",Utility.getProductCategoryId(getApplicationContext()));

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
                            + Utility.GET_OFFERBYPRODUCTANDPRODUCTYPEID, mySoapEnvelop);


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

                        listofferdetials=new ArrayList<>();
                        SoapObject obj1= (SoapObject) result4.getProperty(0);
                        for(int i=1;i<count;i++)
                        {
                            SoapObject obj2= (SoapObject) result4.getProperty(i);

                            minqtypass=Integer.valueOf(obj2.getPropertySafelyAsString("MinQty","0"));

                            maxqtypass=Integer.valueOf(obj2.getPropertySafelyAsString("MaxQty","0"));

                            FreeorDiscountpass=Integer.valueOf(obj2.getPropertySafelyAsString("FreeDiscount","0"));

                            valuepass=Integer.valueOf(obj2.getPropertySafelyAsString("Value","0"));

                            listofferdetials.add(new OffersandPromotionDetailClass(minqtypass,maxqtypass,FreeorDiscountpass,valuepass));

                        }

                        offername=obj1.getPropertySafelyAsString("OfferName","");


                        txt_offer.setText(offername);

                        for(int i=0;i<listofferdetials.size();i++)
                        {
                            int value=listofferdetials.get(i).getValue();
                            int minqt=listofferdetials.get(i).getMinqty();
                            int maxqt=listofferdetials.get(i).getMaxqty();
                            int freeordiscount=listofferdetials.get(i).getFreeordiscount();

                            if(maxqt==0)
                            {
                                if(qtaddsubtract>=minqt)
                                {

                                    valuepass=value;
                                    FreeorDiscountpass=freeordiscount;

                                    found=1;
                                }
                            }
                            else
                            {
                                if(qtaddsubtract>=minqt && qtaddsubtract<=maxqt)
                                {

                                    valuepass=value;
                                    FreeorDiscountpass=freeordiscount;
                                    found=1;

                                }
                            }


                        }
                        if(FreeorDiscountpass==1)
                        {

                            if(found==1)
                            {
                                txt_discount.setText("");

                                txt_discount.setVisibility(View.GONE);
                                // spec_price.setText(price + " "+currency);

                                cdiscount=0;
                                cdiscountmnt=0;
                                cfree=valuepass;
                                txt_offer.setVisibility(View.VISIBLE);
                                txt_offer.setText(offername+"\nYour free quantity will be "+valuepass);

                            }
                            else
                            {
                                txt_discount.setText("");
                                cdiscount=0;
                                cdiscountmnt=0;
                                cfree=0;
                                txt_offer.setVisibility(View.GONE);
                                txt_discount.setVisibility(View.GONE);
                                // txt_offer.setText(offername);           temp
                                txt_discount.setVisibility(View.GONE);

                            }

                        }
                        else if(FreeorDiscountpass==2)
                        {

                            if(found==1)
                            {
                                txt_offer.setVisibility(View.VISIBLE);
                                txt_offer.setText(offername+"\nYour discount will be "+valuepass+"%");
                                double temp1=originalprice*qtaddsubtract;
                                totalprice=temp1;
                                double discountrs=(totalprice*valuepass)/100;
                                double finalprice=totalprice-discountrs;
                                discountedprice=finalprice;
                                String combine="Original Price : "+temp1+" "+currency+ " "+"\n"+"Discount Amount : "+discountrs+" "+currency+ " "+"\n"+"Final Price : "+finalprice+" "+currency+ " ";

                                //String combine=temp1+ " "+"\n"+discountrs+"\n"+finalprice;
                                //       spec_price.setText(combine);

                                txt_discount.setVisibility(View.VISIBLE);
                                txt_discount.setText(combine);

                                display_quantity.setText(qtaddsubtract+"");
                                cdiscount=valuepass;
                                cdiscountmnt=discountrs;
                                cfree=0;
                            }
                            else
                            {
                                cdiscount=0;
                                cdiscountmnt=0;
                                cfree=0;
                                txt_discount.setText("");
                                txt_discount.setVisibility(View.GONE);

                                //  spec_price.setText(price + " "+currency);
                                txt_offer.setText("");
                                txt_offer.setVisibility(View.GONE);
                            }


                        }


                    } else {
/*
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
*/
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

    class DeleteOrderSummerytask extends AsyncTask<Void, Void, SoapObject> {
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
                    Utility.DELETEORDERSUMMAARY);
            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));


            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.DELETEORDERSUMMAARY);

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
                            + Utility.DELETEORDERSUMMAARY, mySoapEnvelop);


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
                    SoapObject soapObject = (SoapObject) result.getProperty(0);
                    System.out.println(soapObject.getProperty("IsSucceed"));
                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

                        if(Utility.isInternetConnected(getApplicationContext()))
                        {
                            PlaceOrderTask task=new PlaceOrderTask();
                            task.execute();
                   /* AddtocartTaskBuy task=new AddtocartTaskBuy();
                    task.execute();*/
                        }
                        else
                        {
                            btn_buyproduct.setEnabled(true);
                            Toast.makeText(EcommProductSpecificationActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

                        }


                    } else {
                        if(Utility.isInternetConnected(getApplicationContext()))
                        {
                            PlaceOrderTask task=new PlaceOrderTask();
                            task.execute();
                   /* AddtocartTaskBuy task=new AddtocartTaskBuy();
                    task.execute();*/
                        }
                        else
                        {
                            btn_buyproduct.setEnabled(true);
                            Toast.makeText(EcommProductSpecificationActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

                        }


                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                    if(Utility.isInternetConnected(getApplicationContext()))
                    {
                        PlaceOrderTask task=new PlaceOrderTask();
                        task.execute();
                   /* AddtocartTaskBuy task=new AddtocartTaskBuy();
                    task.execute();*/
                    }
                    else
                    {
                        btn_buyproduct.setEnabled(true);
                        Toast.makeText(EcommProductSpecificationActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

                    }

                }
            } else {
                if(Utility.isInternetConnected(getApplicationContext()))
                {
                    PlaceOrderTask task=new PlaceOrderTask();
                    task.execute();
                   /* AddtocartTaskBuy task=new AddtocartTaskBuy();
                    task.execute();*/
                }
                else
                {
                    btn_buyproduct.setEnabled(true);
                    Toast.makeText(EcommProductSpecificationActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

                }


                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class Fetchmycartcount extends AsyncTask<Void, Void, SoapObject> {
        private ProgressDialog progress;
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(EcommProductSpecificationActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETMYCARTCOUNT);

            request.addProperty("token", Utility.getAuthToken(EcommProductSpecificationActivity.this));

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
                            + Utility.GETMYCARTCOUNT, mySoapEnvelop);
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

                    SoapObject result1 = (SoapObject) soapObject.getProperty("Data");
                    System.out.println("Result1 is : " + result1.toString());
                    SoapObject result3 = (SoapObject) result1.getProperty(1);
                    System.out.println("Result3 is : " + result3.toString());
                    SoapObject result4 = (SoapObject) result3.getProperty(0);
                    System.out.println("Result4 is : " + result4.toString());
                    int count = result4.getPropertyCount();

                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        int cartcount = Integer.parseInt(soapResult.getPropertySafelyAsString("TotalProductCount", "")
                                .toString());
                        hot_number=cartcount;
                    }
                    updateHotCount(hot_number);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater menuInflater = getSupportMenuInflater();
        getMenuInflater().inflate(R.menu.menu_ecomm, menu);

        MenuItem item = menu.findItem(R.id.action_mycart);
        MenuItemCompat.setActionView(item, R.layout.action_bar_notifitcation_icon);
        View view = MenuItemCompat.getActionView(item);
        ui_hot = (TextView)view.findViewById(R.id.hotlist_hot);
       /* ui_hot.setText(String.valueOf(mNotifCount));
        View action_count = menu.findItem(R.id.action_count).getActionView();
        ui_hot = (TextView) action_count.findViewById(R.id.hotlist_hot);*/
        updateHotCount(hot_number);
        new MyMenuItemStuffListener(view, "My Cart") {
            @Override
            public void onClick(View v) {
                if (Utility.getUserType(EcommProductSpecificationActivity.this).equals("E")) {


                } else {
                    Intent intent = new Intent(EcommProductSpecificationActivity.this,Ecomm_MyCart.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        // return true;
        // MenuItemCompat.getActionView();

        return super.onCreateOptionsMenu(menu);
    }
    public void updateHotCount(final int new_hot_number) {
        hot_number = new_hot_number;
        if (ui_hot == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_hot_number == 0)
                    ui_hot.setVisibility(View.INVISIBLE);
                else {
                    ui_hot.setVisibility(View.VISIBLE);
                    ui_hot.setText(Integer.toString(new_hot_number));
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

            case R.id.action_mycart:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent i = new Intent(EcommProductSpecificationActivity.this, Ecomm_MyCart.class);
                startActivity(i);
                finish();

                return true;

            case R.id.action_wishlist:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent i1 = new Intent(EcommProductSpecificationActivity.this, EcommWishlistActivity.class);
                startActivity(i1);
                finish();

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    class FetchUserWishlist extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommProductSpecificationActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETWISHLISTBYUSER);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GETWISHLISTBYUSER);

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
                            + Utility.GETWISHLISTBYUSER, mySoapEnvelop);


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


                            if(productcode.equalsIgnoreCase(soapResult.getPropertySafelyAsString("ProductCode", "")
                                    .toString())){
                                wishlisticon.setImageDrawable(getResources().getDrawable(R.drawable.wishlist_bluefill));

                            }else{
                                wishlisticon.setImageDrawable(getResources().getDrawable(R.drawable.wishlist_bluenotfill));

                            }

                        }
if(Utility.isInternetConnected(getApplicationContext()))
{
    Fetchofferdetails task=new Fetchofferdetails();
    task.execute();
}

                    } else {

                        if(Utility.isInternetConnected(getApplicationContext()))
                        {
                            Fetchofferdetails task=new Fetchofferdetails();
                            task.execute();
                        }
/*
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
*/
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    if(Utility.isInternetConnected(getApplicationContext()))
                    {
                        Fetchofferdetails task=new Fetchofferdetails();
                        task.execute();
                    }
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    if(Utility.isInternetConnected(getApplicationContext()))
                    {
                        Fetchofferdetails task=new Fetchofferdetails();
                        task.execute();
                    }
                    e.printStackTrace();
                }
            }else {

                if(Utility.isInternetConnected(getApplicationContext()))
                {
                    Fetchofferdetails task=new Fetchofferdetails();
                    task.execute();
                }
                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    class AddtocartTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommProductSpecificationActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.ADDTOCART);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ProductID",productid);
            request.addProperty("ServiceID",0);
            request.addProperty("Quantity",qtaddsubtract);
            request.addProperty("Price",totalprice+"");

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.ADDTOCART);

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
                            + Utility.ADDTOCART, mySoapEnvelop);


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

                        Toast.makeText(getApplicationContext(), "Product Added to cart Successfully",
                                Toast.LENGTH_LONG).show();

                        if(Utility.isInternetConnected(getApplicationContext()))
                        {
                            Fetchmycartcount task=new Fetchmycartcount();
                            task.execute();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

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
            }else{
                Toast.makeText(EcommProductSpecificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }



    class AddtocartTaskBuy extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommProductSpecificationActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.ADDTOCART);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ProductID",productid);
            request.addProperty("ServiceID",0);
            request.addProperty("Quantity",qtaddsubtract);
            request.addProperty("Price",totalprice+"");

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.ADDTOCART);

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
                            + Utility.ADDTOCART, mySoapEnvelop);


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

                      Intent intent=new Intent(getApplicationContext(),Ecomm_MyCart.class);
                        startActivity(intent);
                        finish();

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
                Toast.makeText(EcommProductSpecificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    class AddtowishlistTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommProductSpecificationActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.ADDTOWISHLIST);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ProductID",productid);
            request.addProperty("ServiceID",0);
            request.addProperty("Quantity",qtaddsubtract);
            request.addProperty("Price",originalprice+"");

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.ADDTOWISHLIST);

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
                            + Utility.ADDTOWISHLIST, mySoapEnvelop);


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

                        wishlisticon.setImageDrawable(getResources().getDrawable(R.drawable.wishlist_bluefill));
                        Toast.makeText(getApplicationContext(), "Product Added to Wishlist Successfully",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        wishlisticon.setImageDrawable(getResources().getDrawable(R.drawable.wishlist_bluefill));

                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(EcommProductSpecificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }



    class PlaceOrderTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommProductSpecificationActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.INSERT_ORDERREQUEST);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ProductID",productid);
            request.addProperty("ServiceID",0);
            request.addProperty("Quantity",qtaddsubtract);
/*
            request.addProperty("Price",totalprice+"");
*/
            request.addProperty("Price",totalprice-cdiscountmnt+"");
            request.addProperty("FreeQuantity",cfree);
            request.addProperty("Discount",cdiscount);
            request.addProperty("DiscountAmount",cdiscountmnt+"");

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.INSERT_ORDERREQUEST);

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
                            + Utility.INSERT_ORDERREQUEST, mySoapEnvelop);


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


                      /*  Intent i1 = new Intent(getApplicationContext(),EcommShippingAddressActivity.class);
                        startActivity(i1);
                        finish();*/

                        Utility.savefromwhichordersummary(getApplicationContext(),2);
                        Intent i = new Intent(getApplicationContext(), EcommShippingAddressActivity.class);
                        startActivity(i);
                        finish();

                    }


                     else {
                        btn_buyproduct.setEnabled(true);
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    btn_buyproduct.setEnabled(true);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    btn_buyproduct.setEnabled(true);
                }
            }else {
                btn_buyproduct.setEnabled(true);
                Toast.makeText(EcommProductSpecificationActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class FetchProductimages extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommProductSpecificationActivity .this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTIMAGE);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ProductID", Utility.getProductId(getApplicationContext()));


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
                            + Utility.GET_PRODUCTIMAGE, mySoapEnvelop);


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

                        int count = result4.getPropertyCount();
                        System.out.println("Count is : " + count);

                        arrimagesurl = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            imagepath = soapResult.getPrimitivePropertySafelyAsString("ImagePath");

                            imagepath = imagepath.replace("\\", "/");

                            finalpathimg = Utility.URLFORIMAGE + imagepath;

                            arrimagesurl.add(finalpathimg);
                        }
                        for(int i=0;i<arrimagesurl.size();i++)
                        {

                            TextSliderView textSliderView = new TextSliderView(EcommProductSpecificationActivity.this);
                            textSliderView
                                    // .description(name)
                                    .image(arrimagesurl.get(i))
                                            //.image(Hash_file_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);
                            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra",name);
                            sliderLayout.addSlider(textSliderView);

                        }
                        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        sliderLayout.setCustomAnimation(new DescriptionAnimation());
                        sliderLayout.setDuration(10000);
                        FetchProductDetailsTask task=new FetchProductDetailsTask();
                        task.execute();

                    } else {
                        FetchProductDetailsTask task=new FetchProductDetailsTask();
                        task.execute();

/*
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
*/
                    }
                } catch (Exception e) {
                   e.printStackTrace();
                    FetchProductDetailsTask task=new FetchProductDetailsTask();
                    task.execute();

                }

            }else{
                FetchProductDetailsTask task=new FetchProductDetailsTask();
                task.execute();

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FetchProductDetailsTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommProductSpecificationActivity .this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTBYPRODUCTID);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("productID",Utility.getProductId(getApplicationContext()));


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
                            + Utility.GET_PRODUCTBYPRODUCTID, mySoapEnvelop);


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
                        productid = Integer.valueOf(result5.getPrimitivePropertySafelyAsString("ID"));
                        productname = result5.getPrimitivePropertySafelyAsString("ProductName");
                        productcode = result5.getPrimitivePropertySafelyAsString("ProductCode");
                        productype = result5.getPrimitivePropertySafelyAsString("ProductType");
                        power = result5.getPrimitivePropertySafelyAsString("Power");
                        productdescriptiion = result5.getPrimitivePropertySafelyAsString("ProductDesc");
                        type = result5.getPrimitivePropertySafelyAsString("Type");
                        head = result5.getPrimitivePropertySafelyAsString("Head");
                        flowrate = result5.getPrimitivePropertySafelyAsString("FlowRate");
                        imagepath = result5.getPrimitivePropertySafelyAsString("ImagePath");
                        videopath = result5.getPrimitivePropertySafelyAsString("VideoPath");
                        applications = result5.getPrimitivePropertySafelyAsString("Applications");
                        functionandfeature = result5.getPrimitivePropertySafelyAsString("FunctAndFeatures");
                        specifications = result5.getPrimitivePropertySafelyAsString("Specifications");
                        model_no = result5.getPrimitivePropertySafelyAsString("ModelNum");
                        price = result5.getPrimitivePropertySafelyAsString("NewPrice");
                        currency = result5.getPrimitivePropertySafelyAsString("Currency");
                        int productypeid=Integer.valueOf(result5.getPropertySafelyAsString("ProductType","0"));

                        Utility.saveProductCategoryId(getApplicationContext(),productypeid);
                        try {
                        totalqty = Integer.valueOf(result5.getPrimitivePropertySafelyAsString("Quantity"));
                    }
                    catch (Exception e)
                    {
                        totalqty=1;
                    }


                       spec_productname.setText(productname);
                        if(applications.isEmpty() || applications.equalsIgnoreCase("anyType{}")){
                            headapplication.setVisibility(View.GONE);
                            lineapplication.setVisibility(View.GONE);
                            spec_applications.setVisibility(View.GONE);
                        }else{
                            headapplication.setVisibility(View.VISIBLE);
                            lineapplication.setVisibility(View.VISIBLE);
                            spec_applications.setVisibility(View.VISIBLE);
                            spec_applications.setText(applications);
                        }

                        if(specifications.isEmpty() || specifications.equalsIgnoreCase("anyType{}")){
                            headspecification.setVisibility(View.GONE);
                            linespecification.setVisibility(View.GONE);
                            spec_specifications.setVisibility(View.GONE);
                        }else{
                            headspecification.setVisibility(View.VISIBLE);
                            linespecification.setVisibility(View.VISIBLE);
                            spec_specifications.setVisibility(View.VISIBLE);
                            spec_specifications.setText(specifications);
                        }

                        if(functionandfeature.isEmpty() || functionandfeature.equalsIgnoreCase("anyType{}")){
                            headff.setVisibility(View.GONE);
                            lineff.setVisibility(View.GONE);
                            spec_functions.setVisibility(View.GONE);
                        }else{
                            headff.setVisibility(View.VISIBLE);
                            lineff.setVisibility(View.VISIBLE);
                            spec_functions.setVisibility(View.VISIBLE);
                            spec_functions.setText(functionandfeature);
                        }



                        if(price.isEmpty()){
                            spec_price.setText("0.0"+ " "+currency);
                            originalprice = 0.0;
                            totalprice=originalprice;
                        }else{
                            spec_price.setText(price + " "+currency);
                            originalprice=Double.valueOf(price);
                            totalprice=originalprice;
                        }
                        spec_modelno.setText(model_no);

                        imagepath = imagepath.replace("\\", "/");
                        videopath = videopath.replace("\\", "/");
                        videopath = videopath.replace(" ","");
                        finalpathimg = Utility.URLFORIMAGE + imagepath;
                        finalpathvideo = Utility.URLFORIMAGE + videopath;
                        // product_video.setImageBitmap(retriveVideoFrameFromVideo(finalpathvideo));
                       // Glide.with(getApplicationContext()).load(finalpathimg).into(spec_productimg);


                        FetchUserWishlist task = new FetchUserWishlist();
                        task.execute();

                    } else {

                        FetchUserWishlist task = new FetchUserWishlist();
                        task.execute();

                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {

                    FetchUserWishlist task = new FetchUserWishlist();
                    task.execute();

                    e.printStackTrace();
                } catch (NullPointerException e) {

                    FetchUserWishlist task = new FetchUserWishlist();
                    task.execute();

                    e.printStackTrace();
                } catch (Throwable throwable) {

                    FetchUserWishlist task = new FetchUserWishlist();
                    task.execute();

                    throwable.printStackTrace();
                }

            }else{

                FetchUserWishlist task = new FetchUserWishlist();
                task.execute();

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Utility.getfromdashboardorcategory(getApplicationContext())==1) {
            Intent intent = new Intent(getApplicationContext(), EcommDashboardActivity.class);
            startActivity(intent);
            finish();
        }
        else if(Utility.getfromdashboardorcategory(getApplicationContext())==3) {
            Intent intent = new Intent(getApplicationContext(), EcommOffersCategoryActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), EcommSubCategoryActivity.class);
            startActivity(intent);
            finish();
        }
        }
}
