package skyward.pp.ecommerce;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.ActionBar;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
import skyward.pp.holder.MyCartHolder;
import skyward.pp.model.MyCartClass;
import skyward.pp.model.OffersPromotioncartClass;
import skyward.pp.model.OffersandPromotionDetailClass;
import skyward.pp.util.Utility;

import static skyward.pp.R.id.display_quantity;

public class Ecomm_MyCart extends NavigationHeader {

    int found=0;
    double totalprice=0;
    double discountedprice=0.0;
    ProgressDialog progress;
    int minqtypass=0,maxqtypass=0,FreeorDiscountpass=0,valuepass=0;
    ArrayList<OffersandPromotionDetailClass> listofferdetials;
    String offername="";
    ArrayList<OffersPromotioncartClass> listofferpromotioncart;
    Integer posdel;
    private TextView ui_hot = null;
    int hot_number=0 ;
    int available_qty;
    double grandtotalpasswishlist=0;
    int qtypasswishlist=1;
    int productidpasswishlist=0;
    LinearLayout ll1;
    ImageView txt_line1,txt_line2;
    double grandtotal;
    Button cart_placeorder;
    TextView  cart_grandtotal,cart_promotions,cart_shippingcharges,cart_total;
    ListView listmycart;
    ArrayList<MyCartClass> listcart;
    int totalcnt = 0;
    int idcnt=0;
    int cid=0,cqty=0;
    int cfree=0;
    double cdiscountmnt=0.0;
    int cdiscount=0;
    double cprice=0;
    int productidtask=0;
    int cproductid=0;
    double singalprice=0;

    int cartcnt=0;
    int totalcartcnt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ecomm_mycart, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("My Cart");
        ab.setHomeButtonEnabled(true);
        listcart=new ArrayList<>();
        cart_placeorder = (Button) findViewById(R.id.cart_btn_placeorder);
        //cart_addservices = (TextView) findViewById(R.id.cart_addservices);
        cart_grandtotal = (TextView) findViewById(R.id.cart_grandtotal);
        ll1= (LinearLayout) findViewById(R.id.ll_total);
        txt_line1= (ImageView) findViewById(R.id.img_placeholderline);
        txt_line2= (ImageView) findViewById(R.id.img_placeholderline2);
        listmycart = (ListView) findViewById(R.id.listmycart);
        if(Utility.isInternetConnected(getApplicationContext()))
        {
            FetchUserCart task=new FetchUserCart();
            task.execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

        }
listofferpromotioncart=new ArrayList<>();
listofferdetials=new ArrayList<>();

        cart_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isInternetConnected(getApplicationContext())) {
                    cart_placeorder.setEnabled(false);
                    totalcnt = listcart.size();


                    if(idcnt < totalcnt){
                        cid = listcart.get(idcnt).getCartid();
                        cqty=listcart.get(idcnt).getQuantity();
                        cprice=listcart.get(idcnt).getPrice();
                        cproductid =listcart.get(idcnt).getProductid();

                        cfree=listcart.get(idcnt).getFreeqty();
                        cdiscount=listcart.get(idcnt).getDiscount();
                        cdiscountmnt=listcart.get(idcnt).getDiscountamt();

                        DeleteOrderSummerytask task = new DeleteOrderSummerytask();
                        task.execute();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

                }
            }
        });


        cart_grandtotal.setVisibility(View.GONE);
       // cart_addservices.setVisibility(View.GONE);
        cart_placeorder.setVisibility(View.GONE);
        txt_line1.setVisibility(View.GONE);
        txt_line2.setVisibility(View.GONE);
        ll1.setVisibility(View.GONE);

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
                            cart_placeorder.setEnabled(true);
                            Toast.makeText(Ecomm_MyCart.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

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
                            cart_placeorder.setEnabled(true);
                            Toast.makeText(Ecomm_MyCart.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

                        }
                       /* Toast.makeText(Ecomm_MyCart.this,
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();*/
                    }
                } catch (Exception e) {
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
                        cart_placeorder.setEnabled(true);
                        Toast.makeText(Ecomm_MyCart.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

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
                    cart_placeorder.setEnabled(true);
                    Toast.makeText(Ecomm_MyCart.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

                }
                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class Fetchmycartcount extends AsyncTask<Void, Void, SoapObject> {

        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETMYCARTCOUNT);

            request.addProperty("token", Utility.getAuthToken(Ecomm_MyCart.this));

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
        getMenuInflater().inflate(R.menu.menu_dash_onlycart, menu);
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
                if (Utility.getUserType(Ecomm_MyCart.this).equals("E")) {


                } else {
                    Intent intent = new Intent(Ecomm_MyCart.this,Ecomm_MyCart.class);
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
                Intent i = new Intent(Ecomm_MyCart
                        .this, Ecomm_MyCart.class);
                startActivity(i);
                finish();
                return true;


              /*  Intent intent = new Intent(EcommEcommDashboardActivity.this,LoginActivity.class);
                startActivity(intent);*/
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    class FetchUserCart extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(Ecomm_MyCart.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETCARTBYUSER);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GETCARTBYUSER);

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
                            + Utility.GETCARTBYUSER, mySoapEnvelop);


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
                        if(count==0)
                        {
                            cart_grandtotal.setVisibility(View.GONE);
                           // cart_addservices.setVisibility(View.GONE);
                            cart_placeorder.setVisibility(View.GONE);
                            txt_line1.setVisibility(View.GONE);
                            txt_line2.setVisibility(View.GONE);
                            ll1.setVisibility(View.GONE);

                        }
                        System.out.println("Count is : " + count);

                        if(count>0)
                        {
                            cart_grandtotal.setVisibility(View.VISIBLE);
                          //  cart_addservices.setVisibility(View.VISIBLE);
                            cart_placeorder.setVisibility(View.VISIBLE);
                            txt_line1.setVisibility(View.VISIBLE);
                            txt_line2.setVisibility(View.VISIBLE);
                            ll1.setVisibility(View.VISIBLE);

                        }
                        listcart=new ArrayList<>();
                        //customer.clear();
                        //customerID.clear();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            String productname=soapResult.getPropertySafelyAsString("ProductName", "")
                                    .toString();
                            String modelno=soapResult.getPropertySafelyAsString("ModelNum", "")
                                    .toString();
                            String currency=soapResult.getPropertySafelyAsString("Currency", "")
                                    .toString();
                            int cartid=Integer.parseInt(soapResult.getPropertySafelyAsString(

                                    "CartID", "0").toString());

                            int productid=Integer.parseInt(soapResult.getPropertySafelyAsString(

                                    "ProductID", "0").toString());

                            int productypeid=Integer.parseInt(soapResult.getPropertySafelyAsString(

                                    "ProductType", "0").toString());
                            double price=Double.valueOf(soapResult.getPropertySafelyAsString(

                                    "Price","0").toString());
                                                      int quantity=Integer.valueOf(Integer.parseInt(soapResult.getPropertySafelyAsString(

                                    "Quantity","1").toString()));

                           grandtotal=Double.valueOf(soapResult.getPropertySafelyAsString(

                                    "TotalPrice","0").toString());

                            available_qty=Integer.parseInt(soapResult.getPropertySafelyAsString("AvailableQuantity", "1"));



                            String imagepath=soapResult.getPropertySafelyAsString("ImagePath", "")
                                    .toString();
///need to change available qty
                            listcart.add(new MyCartClass(cartid,productid,productname,imagepath,modelno,quantity,price,available_qty,grandtotal,currency,productypeid,new ArrayList<OffersandPromotionDetailClass>(),0,0,0));
                        }

                        if(listcart.size()>0) {
                            cart_grandtotal.setText(grandtotal+"");
                            listmycart.setAdapter(new MycartAdapter1(getApplicationContext(), listcart));

                        }

                        totalcnt = listcart.size();
                        cid = listcart.get(idcnt).getCartid();

                        cqty=listcart.get(idcnt).getQuantity();
                        cprice=listcart.get(idcnt).getPrice();
                        cproductid=listcart.get(idcnt).getProductid();


                        totalcartcnt=listcart.size();
                        if(listcart.size()>0)
                        {
                            if(cartcnt<totalcartcnt)
                            {
                                int productid=listcart.get(cartcnt).getProductid();
                                int producttypeid=listcart.get(cartcnt).getProducttypeid();
                                Utility.saveProductId(getApplicationContext(),productid);
                                Utility.saveProductCategoryId(getApplicationContext(),producttypeid);

                                if(Utility.isInternetConnected(getApplicationContext()))
                                {
                                    progress = new ProgressDialog(Ecomm_MyCart .this);
                                    progress.setMessage("Please wait...");
                                    progress.setCancelable(false);
                                    progress.show();
                                    Fetchofferdetails task=new Fetchofferdetails();
                                    task.execute();
                                }
                            }
                        }


                    } else {

                        cart_grandtotal.setVisibility(View.GONE);
                        //cart_addservices.setVisibility(View.GONE);
                        cart_placeorder.setVisibility(View.GONE);
                        txt_line1.setVisibility(View.GONE);
                        txt_line2.setVisibility(View.GONE);
                        ll1.setVisibility(View.GONE);

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

                Toast.makeText(Ecomm_MyCart.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
            progress = new ProgressDialog(Ecomm_MyCart.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.INSERT_ORDERREQUEST);
            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ProductID",cproductid);
            request.addProperty("ServiceID",0);
            request.addProperty("Quantity",cqty);
            request.addProperty("Price",(cprice-cdiscountmnt)+"");
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
                idcnt++;
                try {
                    SoapObject soapObject = (SoapObject) result.getProperty(0);
                    System.out.println(soapObject.getProperty("IsSucceed"));
                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {


                       if(idcnt < totalcnt){
                           cid = listcart.get(idcnt).getCartid();
                           cqty=listcart.get(idcnt).getQuantity();
                           cprice=listcart.get(idcnt).getPrice();
                           cproductid=listcart.get(idcnt).getProductid();
                           cfree=listcart.get(idcnt).getFreeqty();
                           cdiscount=listcart.get(idcnt).getDiscount();
                           cdiscountmnt=listcart.get(idcnt).getDiscountamt();
                           PlaceOrderTask task = new PlaceOrderTask();
                           task.execute();
                       }


                        if(idcnt == totalcnt){
                           /* Intent i = new Intent(getApplicationContext(),EcommOrderSummaryActivity.class);
                            startActivity(i);
                            finish();*/
                            Utility.savefromwhichordersummary(getApplicationContext(),1);
                            Intent i = new Intent(getApplicationContext(),EcommShippingAddressActivity.class);
                            startActivity(i);
                            finish();
                        }

                    } else {
                        cart_placeorder.setEnabled(true);
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    cart_placeorder.setEnabled(true);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    cart_placeorder.setEnabled(true);
                }
            }else {
                cart_placeorder.setEnabled(true);
                Toast.makeText(Ecomm_MyCart.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public class MycartAdapter1 extends BaseAdapter {
        String imagepath, productname, finalpath;


        public int cartidtask = 0, qtytask = 0,productidtask=0;
        double pricetask = 0;
        Context mContext;
        LayoutInflater inflator;
        private ArrayList<MyCartClass> productlist;

        public MycartAdapter1(Context mContext, ArrayList<MyCartClass> productlist) {
            this.mContext = mContext;
            this.productlist = productlist;
            inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        @Override
        public int getCount() {
            return productlist.size();
        }

        @Override
        public Object getItem(int position) {
            return productlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView img_product;
            TextView txt_productnamae;

            final TextView txt_modelno, txt_price, txt_qty;
            final ImageButton btnwishlist, btndelete, btnaddqty, btnsubtractqty;
            final TextView txt_offername,txt_discount;
            final MyCartHolder item;


               if (convertView == null) {
                convertView = inflator.inflate(R.layout.mycart_listitem, null);
                img_product = (ImageView) convertView.findViewById(R.id.offer_image_view);
                txt_productnamae = (TextView) convertView.findViewById(R.id.txt_cart_productname);

                txt_modelno = (TextView) convertView.findViewById(R.id.txt_cart_modelno);
                txt_price = (TextView) convertView.findViewById(R.id.txt_cart_price);
                txt_qty = (TextView) convertView.findViewById(display_quantity);
                btnwishlist = (ImageButton) convertView.findViewById(R.id.imgbtn_cart_addtowishlist);
                btndelete = (ImageButton) convertView.findViewById(R.id.imgbtn_cart_delete);
                btnaddqty = (ImageButton) convertView.findViewById(R.id.quantity_add);
                btnsubtractqty = (ImageButton) convertView.findViewById(R.id.quantity_subtract);
                   txt_offername= (TextView)convertView. findViewById(R.id.txt_cart_offer);
                   txt_discount= (TextView) convertView.findViewById(R.id.txt_cart_discount);

                   item = new MyCartHolder(img_product, txt_productnamae, txt_modelno, txt_qty, txt_price, btnwishlist, btndelete, btnaddqty, btnsubtractqty,txt_offername,txt_discount);
                convertView.setTag(item);

                btnwishlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                btndelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        posdel= (Integer) v.getTag();
                        AlertDialog alertDialog = new AlertDialog.Builder(Ecomm_MyCart.this).create();
                        alertDialog.setTitle("Confirm");
                        alertDialog.setMessage("Are you sure you want to Delete?");
                        alertDialog.setCancelable(false);
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        if (Utility.isInternetConnected(mContext)) {

                                            cartidtask=productlist.get(posdel).getCartid();
                                            productidtask=productlist.get(posdel).getProductid();
                                            double grandtotal=productlist.get(posdel).getGrandtotal();
                                            double price=productlist.get(posdel).getPrice();
                                            double discountamt=productlist.get(posdel).getDiscountamt();
                                            double finaltotal=grandtotal-(price-discountamt);

                                            double tmptotal=0;
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                tmptotal+=productlist.get(i).getPrice()-productlist.get(i).getDiscountamt();
                                            }
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                productlist.get(i).setGrandtotal(tmptotal);
                                            }

                                            cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());
                                            new DeleteUserCartByuserId().execute();

                                        } else {


                                            Toast.makeText(mContext, "Check your internet Connection", Toast.LENGTH_SHORT).show();

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
                   btnwishlist.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           if (Utility.isInternetConnected(mContext)) {
                               posdel= (Integer) v.getTag();

                               productidpasswishlist=productlist.get(posdel).getProductid();
                               qtypasswishlist=Integer.valueOf(txt_qty.getText().toString());
                               int totalqty = productlist.get(posdel).getQuantity();
                               double totalprice = productlist.get(posdel).getPrice();
                               singalprice = totalprice / totalqty;
                               int availqty = productlist.get(posdel).getAvvailableqty();
                                   totalqty =qtypasswishlist;
                                   final double newprice = totalqty * singalprice;

                                   grandtotalpasswishlist=newprice;

                               new AddtowishlistTask().execute();

                           } else {
                               Toast.makeText(mContext, "Check your internet Connection", Toast.LENGTH_SHORT).show();

                           }

                       }
                   });

                btnaddqty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        double newprice = 0;
                        Integer pos= (Integer) v.getTag();

                        double tempdiscountgrand=productlist.get(pos).getDiscountamt();
                        int totalqty = productlist.get(pos).getQuantity();
                            double totalprice = productlist.get(pos).getPrice();
                            double singalprice = totalprice / totalqty;
                            int availqty = productlist.get(pos).getAvvailableqty();

                            totalqty += 1;
                            newprice = totalqty * singalprice;

                            productlist.get(pos).setQuantity(totalqty);
                            productlist.get(pos).setPrice(newprice);

                            item.getTxtquantity().setText(totalqty + "");
                            item.getTxtprice().setText(newprice + " "+productlist.get(pos).getCurrency());

                            pricetask = newprice;
                            qtytask = totalqty;
                            cartidtask = productlist.get(pos).getCartid();
                                productidtask=productlist.get(pos).getProductid();

                            double grandtotal=productlist.get(pos).getGrandtotal();
                            double price=productlist.get(pos).getPrice();

                            double finaltotal=grandtotal+singalprice-productlist.get(pos).getDiscountamt();

                        found=0;
                            /*for(int i=0;i<productlist.size();i++)
                            {
                                productlist.get(i).setGrandtotal(finaltotal);
                            }
                            cart_grandtotal.setText(finaltotal+" "+productlist.get(pos).getCurrency());

*/


                        double tmptotal=0;
                        for(int i=0;i<productlist.size();i++)
                        {
                            tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                        }
                        for(int i=0;i<productlist.size();i++)
                        {
                            productlist.get(i).setGrandtotal(tmptotal);
                        }

                        cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());



                            int id = productlist.get(pos).getCartid();

                            for (int j = 0; j < listofferpromotioncart.size(); j++) {
                                int istmp = listofferpromotioncart.get(j).getCartid();
                                ArrayList<OffersandPromotionDetailClass> cls;
                                cls = new ArrayList<>();
                                cls = listofferpromotioncart.get(j).getListofferbycart();
                                if (id == istmp) {

                                    int qtyaddsubtract = productlist.get(pos).getQuantity();

                                    double originalprice = listcart.get(pos).getPrice();
                                    ///////////////////////
                                    found = 0;
                                    for (int k = 0; k < cls.size(); k++) {
                                        int value = cls.get(k).getValue();
                                        int minqt = cls.get(k).getMinqty();
                                        int maxqt = cls.get(k).getMaxqty();
                                        int freeordiscount = cls.get(k).getFreeordiscount();

                                        if (maxqt == 0) {
                                            if (qtyaddsubtract >= minqt) {

                                                valuepass = value;
                                                FreeorDiscountpass = freeordiscount;

                                                found = 1;

                                                tmptotal=0;
                                                for(int i=0;i<productlist.size();i++)
                                                {
                                                    tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                                }
                                                for(int i=0;i<productlist.size();i++)
                                                {
                                                    productlist.get(i).setGrandtotal(tmptotal);
                                                }

                                                cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());

                                            }
                                        } else {
                                            if (qtyaddsubtract >= minqt && qtyaddsubtract <= maxqt) {

                                                valuepass = value;
                                                FreeorDiscountpass = freeordiscount;
                                                found = 1;
                                                tmptotal=0;
                                                for(int i=0;i<productlist.size();i++)
                                                {
                                                    tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                                }
                                                for(int i=0;i<productlist.size();i++)
                                                {
                                                    productlist.get(i).setGrandtotal(tmptotal);
                                                }

                                                cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());

                                            }
                                        }


                                    }
                                    if (FreeorDiscountpass == 1) {

                                        if (found == 1) {
                                            productlist.get(pos).setDiscount(0);
                                            productlist.get(pos).setDiscountamt(0);
                                            productlist.get(pos).setFreeqty(valuepass);

                                            tmptotal=0;
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                            }
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                productlist.get(i).setGrandtotal(tmptotal);
                                            }

                                            cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());


                                            /*    txt_discount.setText("");

                                                txt_discount.setVisibility(View.GONE);*/

                                            //   txt_offer.setText(offername+"\nYour free quantity will be "+valuepass);

                                        } else {
                                            productlist.get(pos).setFreeqty(0);
                                            productlist.get(pos).setDiscount(0);
                                            productlist.get(pos).setDiscountamt(0);
                                            double finaltotal1=grandtotal+singalprice+tempdiscountgrand;

                                            tmptotal=0;
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                            }
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                productlist.get(i).setGrandtotal(tmptotal);
                                            }

                                            cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());

                                               /* txt_discount.setText("");

                                                txt_discount.setVisibility(View.GONE);
                                                txt_offer.setText(offername);
                                                txt_discount.setVisibility(View.GONE);*/

                                        }

                                    } else if (FreeorDiscountpass == 2) {

                                        if (found == 1) {
/*

                                                txt_offer.setVisibility(View.VISIBLE);
                                                txt_offer.setText(offername+"\nYour discount will be "+valuepass+"%");*/
                                            originalprice = listcart.get(pos).getPrice() / listcart.get(pos).getQuantity();
                                            double temp1 = originalprice * qtyaddsubtract;
                                            totalprice = temp1;
                                            double discountrs = (totalprice * valuepass) / 100;
                                            double finalprice = totalprice - discountrs;
                                            discountedprice = finalprice;
                                            String combine="Original Price : "+temp1+" "+productlist.get(pos).getCurrency()+ " "+"\n"+"Discount Amount : "+discountrs+" "+productlist.get(pos).getCurrency()+ " "+"\n"+"Final Price : "+finalprice+" "+productlist.get(pos).getCurrency()+ " ";
                                            //String combine = temp1 + " " + "\n" + discountrs + "\n" + finalprice;
                                            //       spec_price.setText(combine);
                                            productlist.get(pos).setDiscount(valuepass);

                                            productlist.get(pos).setDiscountamt(discountrs);

                                            productlist.get(pos).setFreeqty(0);
                                    tmptotal=0;
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                            }
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                productlist.get(i).setGrandtotal(tmptotal);
                                            }

                                            cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());

                                            /////////////////////////////////try1


                                         /*   double grandtotal1 = productlist.get(pos).getGrandtotal();
                                            double discountamt1 = productlist.get(pos).getDiscountamt();
                                            double t1 = temp1;

                                            grandtotal = (grandtotal1 - t1) + (t1 - discountamt1);


                                            for (int i1 = 0; i1 < productlist.size(); i1++) {
                                                productlist.get(i1).setGrandtotal(grandtotal);
                                            }
                                            cart_grandtotal.setText(grandtotal + " " + productlist.get(pos).getCurrency());

*/
                                            //////////////////////////////////////////////////try1
         /*
                                                double temptotal=listcart.get(i).getGrandtotal();
                                                double tempdis=discountrs;
                                                double finaltotal1=temptotal-tempdis;

                                                for(int i1=0;i1<listcart.size();i1++)
                                                {
                                                    listcart.get(i1).setGrandtotal(finaltotal1);
                                                }
                                                cart_grandtotal.setText(finaltotal1+" "+listcart.get(i).getCurrency());
//

                                       /*         double temptotal=listcart.get(i).getGrandtotal();
                                                double tempdis=discountrs;
                                                double finaltotal1=temptotal-tempdis;

                                                for(int i1=0;i1<listcart.size();i1++)
                                                {
                                                    listcart.get(i1).setGrandtotal(finaltotal1);

                                                }
                                                cart_grandtotal.setText(finaltotal1+" "+listcart.get(i).getCurrency());*/

                                              /*  txt_discount.setVisibility(View.VISIBLE);
                                                txt_discount.setText(combine);

                                                display_quantity.setText(qtaddsubtract+"");*/

                                        } else {


                                            productlist.get(pos).setDiscount(0);

                                            productlist.get(pos).setDiscountamt(0);
                                            productlist.get(pos).setFreeqty(0);
                                            tmptotal=0;
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                            }
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                productlist.get(i).setGrandtotal(tmptotal);
                                            }

                                            cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());

                                          /*      txt_discount.setText("");
                                                txt_discount.setVisibility(View.GONE);

                                                //  spec_price.setText(price + " "+currency);
                                                txt_offer.setText("");
                                                txt_offer.setVisibility(View.GONE);*/
                                        }


                                    }

                                    /////////////////////////////////

                                }

                            }









                        ////////////////////////////////////////////////////////////////////
                   /*     if(productlist.get(pos).getDiscount()!=0  || productlist.get(pos).getFreeqty()!=0 )
                        {
                            if(productlist.get(pos).getFreeqty()!=0)
                            {
                                productlist.get(pos).setDiscount(0);
                                productlist.get(pos).setDiscountamt(0);
                                productlist.get(pos).setFreeqty(valuepass);
                                item.getTxt_offername().setVisibility(View.VISIBLE);
                                item.getTxt_offername().setText(offername+"\nYour free quantity will be "+valuepass);

                            }
                            else if(productlist.get(pos).getDiscount()!=0 )
                            {
                                int qtaddsubtract=productlist.get(pos).getQuantity();

                                double totalprice1 = productlist.get(pos).getPrice();
                                double originalprice = totalprice1 / qtaddsubtract;

                                item.getTxt_offername().setVisibility(View.VISIBLE);

                                double temp1=originalprice*qtaddsubtract;
                                totalprice=temp1;
                                double discountrs=(totalprice*valuepass)/100;
                                double finalprice=totalprice-discountrs;
                                discountedprice=finalprice;
                                String combine=temp1+ " "+"\n"+discountrs+"\n"+finalprice;
                                productlist.get(pos).setDiscount(valuepass);
                                productlist.get(pos).setDiscountamt(discountrs);
                                productlist.get(pos).setFreeqty(0);

                                //       spec_price.setText(combine);

                                double temptotal=productlist.get(pos).getGrandtotal();
                                double tempdis=discountrs;
                                double finaltotal1=temptotal-tempdis;

                                for(int i=0;i<productlist.size();i++)
                                {
                                    productlist.get(i).setGrandtotal(finaltotal1);
                                }
                                cart_grandtotal.setText(finaltotal1+" "+productlist.get(pos).getCurrency());

                                item.getTxt_disconut().setText(combine);
                                item.getTxt_offername().setText(offername+"\nYour discount will be "+valuepass+"%");


                            }

                        }
                        else
                        {
                            productlist.get(pos).setDiscount(0);
                            productlist.get(pos).setDiscountamt(0);
                            productlist.get(pos).setFreeqty(0);
                            item.getTxt_offername().setVisibility(View.GONE);

                        }
*/
//////////////////////////////////////////////////////////////////////////////////////////


                        notifyDataSetChanged();
                            if (Utility.isInternetConnected(mContext)) {
                                new UpdatecartTask().execute();

                            } else {
                                Toast.makeText(mContext, "Check your internet Connection", Toast.LENGTH_SHORT).show();

                            }






                     /*   else
                            {
                                Toast.makeText(getApplicationContext(),"You can not add more than available quantity",Toast.LENGTH_LONG).show();

                            }
*/
                    }
                });
                btnsubtractqty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double newprice = 0;
                        Integer pos= (Integer) v.getTag();
                        double tempdiscountgrand=productlist.get(pos).getDiscountamt();

                        int totalqty = productlist.get(pos).getQuantity();
                        double totalprice = productlist.get(pos).getPrice();
                        double singalprice = totalprice / totalqty;
                        int availqty = productlist.get(pos).getAvvailableqty();
                        if (totalqty == 1) {

                        } else {
                            totalqty -= 1;
                            newprice = totalqty * singalprice;
                            productlist.get(pos).setQuantity(totalqty);
                            productlist.get(pos).setPrice(newprice);
                            item.getTxtquantity().setText(totalqty + "");
                            item.getTxtprice().setText(newprice + " "+productlist.get(pos).getCurrency());
                            pricetask = newprice;
                            qtytask = totalqty;
                            cartidtask = productlist.get(pos).getCartid();
                            productidtask=productlist.get(pos).getProductid();
                            double grandtotal=productlist.get(pos).getGrandtotal();
                            double price=productlist.get(pos).getPrice();
                            double finaltotal=grandtotal-singalprice-productlist.get(pos).getDiscountamt();
                            double tmptotal=0;
                            for(int i=0;i<productlist.size();i++)
                            {
                                tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                            }
                            for(int i=0;i<productlist.size();i++)
                            {
                                productlist.get(i).setGrandtotal(tmptotal);
                            }

                            cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());
/////////////////////

found=0;

                            int id = productlist.get(pos).getCartid();

                            for (int j = 0; j < listofferpromotioncart.size(); j++) {
                                int istmp = listofferpromotioncart.get(j).getCartid();
                                ArrayList<OffersandPromotionDetailClass> cls;
                                cls = new ArrayList<>();
                                cls = listofferpromotioncart.get(j).getListofferbycart();
                                if (id == istmp) {

                                    int qtyaddsubtract = productlist.get(pos).getQuantity();

                                    double originalprice = listcart.get(pos).getPrice();
                                    ///////////////////////
                                    found = 0;
                                    for (int k = 0; k < cls.size(); k++) {
                                        int value = cls.get(k).getValue();
                                        int minqt = cls.get(k).getMinqty();
                                        int maxqt = cls.get(k).getMaxqty();
                                        int freeordiscount = cls.get(k).getFreeordiscount();

                                        if (maxqt == 0) {
                                            if (qtyaddsubtract >= minqt) {

                                                valuepass = value;
                                                FreeorDiscountpass = freeordiscount;

                                                found = 1;
                                                tmptotal=0;
                                                for(int i=0;i<productlist.size();i++)
                                                {
                                                    tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                                }
                                                for(int i=0;i<productlist.size();i++)
                                                {
                                                    productlist.get(i).setGrandtotal(tmptotal);
                                                }

                                                cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());

                                            }
                                        } else {
                                            if (qtyaddsubtract >= minqt && qtyaddsubtract <= maxqt) {

                                                valuepass = value;
                                                FreeorDiscountpass = freeordiscount;
                                                found = 1;
                                                tmptotal=0;
                                                for(int i=0;i<productlist.size();i++)
                                                {
                                                    tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                                }
                                                for(int i=0;i<productlist.size();i++)
                                                {
                                                    productlist.get(i).setGrandtotal(tmptotal);
                                                }

                                                cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());


                                            }
                                        }


                                    }
                                    if (FreeorDiscountpass == 1) {

                                        if (found == 1) {
                                            productlist.get(pos).setDiscount(0);
                                            productlist.get(pos).setDiscountamt(0);
                                            productlist.get(pos).setFreeqty(valuepass);

                                            tmptotal=0;
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                            }
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                productlist.get(i).setGrandtotal(tmptotal);
                                            }

                                            cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());

                                            /*    txt_discount.setText("");

                                                txt_discount.setVisibility(View.GONE);*/

                                            //   txt_offer.setText(offername+"\nYour free quantity will be "+valuepass);

                                        } else {
                                            productlist.get(pos).setFreeqty(0);
                                            productlist.get(pos).setDiscount(0);
                                            productlist.get(pos).setDiscountamt(0);


                                            tmptotal=0;
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                            }
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                productlist.get(i).setGrandtotal(tmptotal);
                                            }

                                            cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());

                                               /* txt_discount.setText("");

                                                txt_discount.setVisibility(View.GONE);
                                                txt_offer.setText(offername);
                                                txt_discount.setVisibility(View.GONE);*/

                                        }

                                    } else if (FreeorDiscountpass == 2) {

                                        if (found == 1) {
/*

                                                txt_offer.setVisibility(View.VISIBLE);
                                                txt_offer.setText(offername+"\nYour discount will be "+valuepass+"%");*/
                                            originalprice = listcart.get(pos).getPrice() / listcart.get(pos).getQuantity();
                                            double temp1 = originalprice * qtyaddsubtract;
                                            totalprice = temp1;
                                            double discountrs = (totalprice * valuepass) / 100;
                                            double finalprice = totalprice - discountrs;
                                            discountedprice = finalprice;
                                            String combine="Original Price : "+temp1+" "+productlist.get(pos).getCurrency()+ " "+"\n"+"Discount Amount : "+discountrs+" "+productlist.get(pos).getCurrency()+ " "+"\n"+"Final Price : "+finalprice+" "+productlist.get(pos).getCurrency()+ " ";
                                            //String combine = temp1 + " " + "\n" + discountrs + "\n" + finalprice;
                                            //       spec_price.setText(combine);
                                            productlist.get(pos).setDiscount(valuepass);

                                            productlist.get(pos).setDiscountamt(discountrs);

                                            productlist.get(pos).setFreeqty(0);
////////////try2...................


                                            tmptotal=0;
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                            }
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                productlist.get(i).setGrandtotal(tmptotal);
                                            }

                                            cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());

                                            /////////////////////////////////try1


                                         /*   double grandtotal1 = productlist.get(pos).getGrandtotal();
                                            double discountamt1 = productlist.get(pos).getDiscountamt();
                                            double t1 = temp1;

                                            grandtotal = (grandtotal1 - t1) + (t1 - discountamt1);


                                            for (int i1 = 0; i1 < productlist.size(); i1++) {
                                                productlist.get(i1).setGrandtotal(grandtotal);
                                            }
                                            cart_grandtotal.setText(grandtotal + " " + productlist.get(pos).getCurrency());

*/
                                            //////////////////////////////////////////////////try1
         /*
                                                double temptotal=listcart.get(i).getGrandtotal();
                                                double tempdis=discountrs;
                                                double finaltotal1=temptotal-tempdis;

                                                for(int i1=0;i1<listcart.size();i1++)
                                                {
                                                    listcart.get(i1).setGrandtotal(finaltotal1);
                                                }
                                                cart_grandtotal.setText(finaltotal1+" "+listcart.get(i).getCurrency());
//

                                       /*         double temptotal=listcart.get(i).getGrandtotal();
                                                double tempdis=discountrs;
                                                double finaltotal1=temptotal-tempdis;

                                                for(int i1=0;i1<listcart.size();i1++)
                                                {
                                                    listcart.get(i1).setGrandtotal(finaltotal1);

                                                }
                                                cart_grandtotal.setText(finaltotal1+" "+listcart.get(i).getCurrency());*/

                                              /*  txt_discount.setVisibility(View.VISIBLE);
                                                txt_discount.setText(combine);

                                                display_quantity.setText(qtaddsubtract+"");*/

                                        } else {
                                            productlist.get(pos).setDiscount(0);

                                            productlist.get(pos).setDiscountamt(0);
                                            productlist.get(pos).setFreeqty(0);
                                            tmptotal=0;
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                tmptotal+=(productlist.get(i).getPrice()-productlist.get(i).getDiscountamt());
                                            }
                                            for(int i=0;i<productlist.size();i++)
                                            {
                                                productlist.get(i).setGrandtotal(tmptotal);
                                            }

                                            cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());

                                          /*      txt_discount.setText("");
                                                txt_discount.setVisibility(View.GONE);

                                                //  spec_price.setText(price + " "+currency);
                                                txt_offer.setText("");
                                                txt_offer.setVisibility(View.GONE);*/
                                        }


                                    }

                                    /////////////////////////////////

                                }

                            }









                            ////////////////////////////////////////////////////////////////////
                   /*     if(productlist.get(pos).getDiscount()!=0  || productlist.get(pos).getFreeqty()!=0 )
                        {
                            if(productlist.get(pos).getFreeqty()!=0)
                            {
                                productlist.get(pos).setDiscount(0);
                                productlist.get(pos).setDiscountamt(0);
                                productlist.get(pos).setFreeqty(valuepass);
                                item.getTxt_offername().setVisibility(View.VISIBLE);
                                item.getTxt_offername().setText(offername+"\nYour free quantity will be "+valuepass);

                            }
                            else if(productlist.get(pos).getDiscount()!=0 )
                            {
                                int qtaddsubtract=productlist.get(pos).getQuantity();

                                double totalprice1 = productlist.get(pos).getPrice();
                                double originalprice = totalprice1 / qtaddsubtract;

                                item.getTxt_offername().setVisibility(View.VISIBLE);

                                double temp1=originalprice*qtaddsubtract;
                                totalprice=temp1;
                                double discountrs=(totalprice*valuepass)/100;
                                double finalprice=totalprice-discountrs;
                                discountedprice=finalprice;
                                String combine=temp1+ " "+"\n"+discountrs+"\n"+finalprice;
                                productlist.get(pos).setDiscount(valuepass);
                                productlist.get(pos).setDiscountamt(discountrs);
                                productlist.get(pos).setFreeqty(0);

                                //       spec_price.setText(combine);

                                double temptotal=productlist.get(pos).getGrandtotal();
                                double tempdis=discountrs;
                                double finaltotal1=temptotal-tempdis;

                                for(int i=0;i<productlist.size();i++)
                                {
                                    productlist.get(i).setGrandtotal(finaltotal1);
                                }
                                cart_grandtotal.setText(finaltotal1+" "+productlist.get(pos).getCurrency());

                                item.getTxt_disconut().setText(combine);
                                item.getTxt_offername().setText(offername+"\nYour discount will be "+valuepass+"%");


                            }

                        }
                        else
                        {
                            productlist.get(pos).setDiscount(0);
                            productlist.get(pos).setDiscountamt(0);
                            productlist.get(pos).setFreeqty(0);
                            item.getTxt_offername().setVisibility(View.GONE);

                        }
*/
//////////////////////////////////////////////////////////////////////////////////////////


                            notifyDataSetChanged();


                            ///////////
                           /* if(productlist.get(pos).getDiscount()!=0  || productlist.get(pos).getFreeqty()!=0 )
                            {
                                if(productlist.get(pos).getFreeqty()!=0)
                                {
                                    productlist.get(pos).setDiscount(0);
                                    productlist.get(pos).setDiscountamt(0);
                                    productlist.get(pos).setFreeqty(valuepass);
                                    item.getTxt_offername().setVisibility(View.VISIBLE);
                                    item.getTxt_offername().setText(offername+"\nYour free quantity will be "+valuepass);

                                }
                                else if(productlist.get(pos).getDiscount()!=0 )
                                {
                                    int qtaddsubtract=productlist.get(pos).getQuantity();

                                    double totalprice1 = productlist.get(pos).getPrice();
                                    double originalprice = totalprice1 / qtaddsubtract;

                                    item.getTxt_offername().setVisibility(View.VISIBLE);

                                    double temp1=originalprice*qtaddsubtract;
                                    totalprice=temp1;
                                    double discountrs=(totalprice*valuepass)/100;
                                    double finalprice=totalprice-discountrs;
                                    discountedprice=finalprice;
                                    String combine=temp1+ " "+"\n"+discountrs+"\n"+finalprice;
                                    productlist.get(pos).setDiscount(valuepass);
                                    productlist.get(pos).setDiscountamt(discountrs);
                                    productlist.get(pos).setFreeqty(0);
                                    //       spec_price.setText(combine);

                                    double temptotal=productlist.get(pos).getGrandtotal();
                                    double tempdis=discountrs;
                                    double finaltotal1=temptotal-tempdis;

                                    for(int i=0;i<productlist.size();i++)
                                    {
                                        productlist.get(i).setGrandtotal(finaltotal1);
                                    }
                                    cart_grandtotal.setText(finaltotal1+" "+productlist.get(pos).getCurrency());

                                    item.getTxt_disconut().setText(combine);
                                    item.getTxt_offername().setText(offername+"\nYour discount will be "+valuepass+"%");


                                }

                            }
                            else
                            {
                                productlist.get(pos).setDiscount(0);
                                productlist.get(pos).setDiscountamt(0);
                                productlist.get(pos).setFreeqty(0);
                                item.getTxt_offername().setVisibility(View.GONE);

                            }*/
                            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            if (Utility.isInternetConnected(mContext)) {
                                new UpdatecartTask().execute();
                            } else {
                                Toast.makeText(mContext, "Check your internet Connection", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                });

            } else {
                item = (MyCartHolder) convertView.getTag();
            }
            imagepath = productlist.get(position).getImagepath();
            imagepath = imagepath.replace("\\", "/");




/*
        ImageLoader imgLoader = new ImageLoader(productlist.get(position).getContext());
        imgLoader.DisplayImage("http://192.168.1.25:5090/ProfileImage/images(1)_d6e28626-0b47-45ce-9651-a14b8dede9db.jpg", loader, item.getImg_ProductImage());

//imagepath = "ProfileImage\images(1)_d6e28626-0b47-45ce-9651-a14b8dede9db.jpg";
        String newimg;
        if (imagepath != null) {
            try {

                newimg = imagepath.replace("\\", "/");
                //imagepath = imagepath.replaceAll(" ", "%20");
                urlforimage = Utility.URLFORIMAGE  + newimg;

                System.out.println("Image path is : " + urlforimage);


            } catch (NullPointerException e) {

            }

        }*/

            int qty=productlist.get(position).getQuantity();






            ArrayList<OffersandPromotionDetailClass> lstoffer;


            finalpath = Utility.URLFORIMAGE + imagepath;
            //MyVideos videos = arrayList.get(position);

            double disamt=productlist.get(position).getDiscountamt();
            int freeqty=productlist.get(position).getFreeqty();
            int dis=productlist.get(position).getDiscount();
            Glide.with(mContext).load(finalpath).into(item.getImgproduct());
            item.getTxtparoductname().setText(productlist.get(position).getProductame());
           if(productlist.get(position).getDiscount()==0)
                {
                   item.getTxt_disconut().setVisibility(View.GONE);

                }
                else {
                         item.getTxt_disconut().setVisibility(View.VISIBLE);
                         item.getTxt_disconut().setText(productlist.get(position).getDiscountamt()+"");

                     }
             if(productlist.get(position).getDiscount()!=0  || productlist.get(position).getFreeqty()!=0 )
                {
    if(productlist.get(position).getFreeqty()!=0)
    {
       /* productlist.get(position).setDiscount(0);
        productlist.get(position).setDiscountamt(0);
        productlist.get(position).setFreeqty(freeqty);*/
        item.getTxt_offername().setVisibility(View.VISIBLE);
        item.getTxt_offername().setText(offername+"\nYour free quantity will be "+freeqty);

    }
    else if(productlist.get(position).getDiscount()!=0 )
    {
        /*int qtaddsubtract=productlist.get(position).getQuantity();

        double totalprice = productlist.get(position).getPrice();
        double originalprice = totalprice / qtaddsubtract;
*/
        item.getTxt_offername().setVisibility(View.VISIBLE);

       /* double temp1=originalprice*qtaddsubtract;
        totalprice=temp1;
        double discountrs=(totalprice*valuepass)/100;
        double finalprice=totalprice-discountrs;
        discountedprice=finalprice;*/
        double price=productlist.get(position).getPrice();
        double discount=productlist.get(position).getDiscountamt();
        double totalamt=price-discount;
        String combine="Original Price : "+price+" "+productlist.get(position).getCurrency()+ " "+"\n"+"Discount Amount : "+discount+" "+productlist.get(position).getCurrency()+ " "+"\n"+"Final Price : "+totalamt+" "+productlist.get(position).getCurrency()+ " ";
       // String combine=price+ " "+"\n"+discount+"\n"+totalamt;
   /*     productlist.get(position).setDiscount(valuepass);
        productlist.get(position).setDiscountamt(discountrs);

        productlist.get(position).setFreeqty(0);*/
        //       spec_price.setText(combine);


        item.getTxt_disconut().setText(combine);
        item.getTxt_offername().setText(offername+"\nYour discount will be "+dis+"%");


    }

}
else
{
 /*   productlist.get(position).setDiscount(0);
    productlist.get(position).setDiscountamt(0);
    productlist.get(position).setFreeqty(0);*/
    item.getTxt_offername().setVisibility(View.GONE);

}

            item.getTxtmodelno().setText(productlist.get(position).getModelno());
            item.getTxtquantity().setText(productlist.get(position).getQuantity() + "");
            item.getTxtprice().setText(productlist.get(position).getPrice() + " " +productlist.get(position).getCurrency());
            item.getBtnqtysubtract().setTag(position);
            item.getBtnqtyadd().setTag(position);
            item.getBtndelete().setTag(position);
            item.getBtnwishlist().setTag(position);
            return convertView;




        }




        class UpdatecartTask extends AsyncTask<Void, Void, SoapObject> {
            SoapObject result;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //etClientName.setText("");

            }

            @Override
            protected SoapObject doInBackground(Void... arg0) {
                SoapObject request = new SoapObject(Utility.NAMESPACE,
                        Utility.UPDATEUSERCART);

                request.addProperty("token", Utility.getAuthToken(mContext));
                request.addProperty("CartID", cartidtask);
                request.addProperty("Quantity", qtytask);
                request.addProperty("Price", pricetask + "");

                System.out.println("URL is ::" + Utility.SOAP_ACTION
                        + Utility.UPDATEUSERCART);

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
                                + Utility.UPDATEUSERCART, mySoapEnvelop);


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
                                Fetchmycartcount task=new Fetchmycartcount();
                                task.execute();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(mContext,
                                    soapObject.getProperty("ErrorMessage").toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        }


        class DeleteUserCartByuserId extends AsyncTask<Void, Void, SoapObject> {
            SoapObject result;
            private ProgressDialog progress;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //etClientName.setText("");
                progress = new ProgressDialog(Ecomm_MyCart.this);
                progress.setMessage("Please wait...");
                progress.setCancelable(false);
                progress.show();
            }

            @Override
            protected SoapObject doInBackground(Void... arg0) {
                SoapObject request = new SoapObject(Utility.NAMESPACE,
                        Utility.DELETEUSERCARTBYUSERID);
                request.addProperty("token", Utility.getAuthToken(mContext));
                request.addProperty("CartID", cartidtask);
                request.addProperty("ProductID", productidtask);

                System.out.println("URL is ::" + Utility.SOAP_ACTION
                        + Utility.DELETEUSERCARTBYUSERID);

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
                                + Utility.DELETEUSERCARTBYUSERID, mySoapEnvelop);


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
                if (result != null) {
                    try {
                        SoapObject soapObject = (SoapObject) result.getProperty(0);
                        System.out.println(soapObject.getProperty("IsSucceed"));
                        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

                            Toast.makeText(Ecomm_MyCart.this,
                                    "Item deleted successfully",
                                    Toast.LENGTH_SHORT).show();
                            productlist.remove(productlist.get(posdel));
                            notifyDataSetChanged();

                            if(productlist.size()==0)
                            {
                                Toast.makeText(Ecomm_MyCart.this,
                                        "No Items in Cart!",
                                        Toast.LENGTH_SHORT).show();
                                cart_grandtotal.setVisibility(View.GONE);
                                // cart_addservices.setVisibility(View.GONE);
                                cart_placeorder.setVisibility(View.GONE);
                                txt_line1.setVisibility(View.GONE);
                                txt_line2.setVisibility(View.GONE);
                                ll1.setVisibility(View.GONE);

                            }
                            else
                            {
                                double tmptotal=0;
                                for(int i=0;i<productlist.size();i++)
                                {
                                    tmptotal+=productlist.get(i).getPrice()-productlist.get(i).getDiscountamt();
                                }
                                for(int i=0;i<productlist.size();i++)
                                {
                                    productlist.get(i).setGrandtotal(tmptotal);
                                }

                                cart_grandtotal.setText(tmptotal +" "+productlist.get(0).getCurrency());
                            }

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
                            Toast.makeText(Ecomm_MyCart.this,
                                    soapObject.getProperty("ErrorMessage").toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
                progress = new ProgressDialog(Ecomm_MyCart.this);
                progress.setMessage("Please wait...");
                progress.setCancelable(false);
                progress.show();
            }

            @Override
            protected SoapObject doInBackground(Void... arg0) {
                SoapObject request = new SoapObject(Utility.NAMESPACE,
                        Utility.ADDTOWISHLIST);

                request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
                request.addProperty("ProductID",productidpasswishlist);
                request.addProperty("ServiceID",0);
                request.addProperty("Quantity",qtypasswishlist);
                request.addProperty("Price",singalprice+"");

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

                            Toast.makeText(getApplicationContext(), "Product Added to wishlist Successfully",
                                    Toast.LENGTH_LONG).show();



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
                    Toast.makeText(Ecomm_MyCart.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        }





    }



    class Fetchofferdetails extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");

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
            cartcnt++;
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



                        totalcartcnt=listcart.size();
                        int tempcarttid=listcart.get(cartcnt-1).getCartid();

                        listofferpromotioncart.add(new OffersPromotioncartClass(tempcarttid,listofferdetials,offername));

                            if(cartcnt<totalcartcnt)
                            {
                                int productid=listcart.get(cartcnt).getProductid();
                                int producttypeid=listcart.get(cartcnt).getProducttypeid();
                                Utility.saveProductId(getApplicationContext(),productid);
                                Utility.saveProductCategoryId(getApplicationContext(),producttypeid);

                                if(Utility.isInternetConnected(getApplicationContext()))
                                {

                                    Fetchofferdetails task=new Fetchofferdetails();
                                    task.execute();
                                }
                            }

                        if(cartcnt==totalcartcnt) {

                            int cnt = listofferpromotioncart.size();


                            for (int i = 0; i < listcart.size(); i++) {
                                int id = listcart.get(i).getCartid();

                                for (int j = 0; j < listofferpromotioncart.size(); j++) {
                                    int istmp = listofferpromotioncart.get(j).getCartid();
                                    ArrayList<OffersandPromotionDetailClass> cls ;
                                    cls=new ArrayList<>();
                                    cls=listofferpromotioncart.get(j).getListofferbycart();
                                    if (id == istmp) {

                                        int qtyaddsubtract = listcart.get(i).getQuantity();

                                        double originalprice = listcart.get(i).getPrice();
                                        ///////////////////////
                                        found=0;
                                        for (int k = 0; k < cls.size(); k++) {
                                            int value = cls.get(k).getValue();
                                            int minqt = cls.get(k).getMinqty();
                                            int maxqt = cls.get(k).getMaxqty();
                                            int freeordiscount = cls.get(k).getFreeordiscount();

                                            if (maxqt == 0) {
                                                if (qtyaddsubtract >= minqt) {

                                                    valuepass = value;
                                                    FreeorDiscountpass = freeordiscount;

                                                    found = 1;
                                                }
                                            } else {
                                                if (qtyaddsubtract >= minqt && qtyaddsubtract <= maxqt) {

                                                    valuepass = value;
                                                    FreeorDiscountpass = freeordiscount;
                                                    found = 1;

                                                }
                                            }


                                        }
                                        if (FreeorDiscountpass == 1) {

                                            if (found == 1) {
                                                listcart.get(i).setDiscount(0);
                                                listcart.get(i).setFreeqty(valuepass);
                                                listcart.get(i).setDiscountamt(0);
                                            /*    txt_discount.setText("");

                                                txt_discount.setVisibility(View.GONE);*/

                                                //   txt_offer.setText(offername+"\nYour free quantity will be "+valuepass);

                                            } else {
                                                listcart.get(i).setDiscount(0);
                                                listcart.get(i).setDiscountamt(0);
                                                listcart.get(i).setFreeqty(0);

                                               /* txt_discount.setText("");

                                                txt_discount.setVisibility(View.GONE);
                                                txt_offer.setText(offername);
                                                txt_discount.setVisibility(View.GONE);*/

                                            }

                                        } else if (FreeorDiscountpass == 2) {

                                            if (found == 1) {
/*

                                                txt_offer.setVisibility(View.VISIBLE);
                                                txt_offer.setText(offername+"\nYour discount will be "+valuepass+"%");*/
originalprice=listcart.get(i).getPrice()/listcart.get(i).getQuantity();
                                                double temp1 = originalprice * qtyaddsubtract;
                                                totalprice = temp1;
                                                double discountrs = (totalprice * valuepass) / 100;
                                                double finalprice = totalprice - discountrs;
                                                discountedprice = finalprice;
                                                String combine="Original Price : "+temp1+" "+listcart.get(i).getCurrency()+ " "+"\n"+"Discount Amount : "+discountrs+" "+listcart.get(i).getCurrency()+ " "+"\n"+"Final Price : "+finalprice+" "+listcart.get(i).getCurrency()+ " ";
                                               // String combine = temp1 + " " + "\n" + discountrs + "\n" + finalprice;
                                                //       spec_price.setText(combine);
                                                listcart.get(i).setDiscount(valuepass);

                                                listcart.get(i).setDiscountamt(discountrs);


                                                listcart.get(i).setFreeqty(0);
                                                /////////////////////////////////try1


                                               double grandtotal1=listcart.get(i).getGrandtotal();
                                                double discountamt1=listcart.get(i).getDiscountamt();
                                                double t1=temp1;

                                                grandtotal=(grandtotal1-t1)+(t1-discountamt1);





                                                for(int i1=0;i1<listcart.size();i1++)
                                                {
                                                    listcart.get(i1).setGrandtotal(grandtotal);
                                                }
                                                cart_grandtotal.setText(grandtotal+" "+listcart.get(i).getCurrency());





                                                //////////////////////////////////////////////////try1
         /*
                                                double temptotal=listcart.get(i).getGrandtotal();
                                                double tempdis=discountrs;
                                                double finaltotal1=temptotal-tempdis;

                                                for(int i1=0;i1<listcart.size();i1++)
                                                {
                                                    listcart.get(i1).setGrandtotal(finaltotal1);
                                                }
                                                cart_grandtotal.setText(finaltotal1+" "+listcart.get(i).getCurrency());
//

                                       /*         double temptotal=listcart.get(i).getGrandtotal();
                                                double tempdis=discountrs;
                                                double finaltotal1=temptotal-tempdis;

                                                for(int i1=0;i1<listcart.size();i1++)
                                                {
                                                    listcart.get(i1).setGrandtotal(finaltotal1);

                                                }
                                                cart_grandtotal.setText(finaltotal1+" "+listcart.get(i).getCurrency());*/

                                              /*  txt_discount.setVisibility(View.VISIBLE);
                                                txt_discount.setText(combine);

                                                display_quantity.setText(qtaddsubtract+"");*/

                                            } else {
                                                listcart.get(i).setDiscount(0);

                                                listcart.get(i).setFreeqty(0);
                                                listcart.get(i).setDiscountamt(0);
                                          /*      txt_discount.setText("");
                                                txt_discount.setVisibility(View.GONE);

                                                //  spec_price.setText(price + " "+currency);
                                                txt_offer.setText("");
                                                txt_offer.setVisibility(View.GONE);*/
                                            }


                                        }

                                        /////////////////////////////////

                                    }

                                }
                            }


                            double discount=0;
                            double grandtoaal=0;


                            for(int i=0;i<listcart.size();i++)
                            {
                               grandtoaal=listcart.get(i).getGrandtotal();
                                discount+=listcart.get(i).getDiscountamt();
                            }
                            double newtoatl=grandtoaal-discount;


                            listmycart.setAdapter(new MycartAdapter1(getApplicationContext(), listcart));

//////////////change temp
/*
                            double temp=grandtotal;
                            double distemp=0.0;
                            for(int j=0;j<listcart.size();j++)
                            {
                                distemp+=listcart.get(j).getDiscountamt();
                            }

                            grandtotal=grandtotal-distemp;
                            cart_grandtotal.setText(grandtotal+" "+listcart.get(0).getCurrency());

*/
//////////////


                       /*     double temptotal=0;
                            temptotal=grandtotal;
                            for(int i=0;i<listcart.size();i++)
                            {

                                double tmpprice=listcart.get(i).getDiscountamt();
                                temptotal-=tmpprice;

                            }
                            grandtotal=temptotal;
                            cart_grandtotal.setText(grandtotal+"");*/
                        }
                            try
                            {
                                progress.dismiss();
                            }catch (Exception e)
                            {

                            }
                        }
                     else {
                        try
                        {
                            progress.dismiss();
                        }catch (Exception e)
                        {

                        }
/*
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
*/
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    try
                    {
                        progress.dismiss();
                    }catch (Exception e1)
                    {

                    }
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    try
                    {
                        progress.dismiss();
                    }catch (Exception e1)
                    {

                    }
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    try
                    {
                        progress.dismiss();
                    }catch (Exception e)
                    {

                    }
                    throwable.printStackTrace();
                }

            }else{
                try
                {
                    progress.dismiss();
                }catch (Exception e)
                {

                }
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
