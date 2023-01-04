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
import android.widget.AdapterView;
import android.widget.ListView;
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

import skyward.pp.R;
import skyward.pp.adapter.EcommCategoryAdapter;
import skyward.pp.model.EcommCategoryClass;
import skyward.pp.util.Utility;

public class EcommCategoryActivity extends NavigationHeader {

    private TextView ui_hot = null;
    int hot_number=0 ;
    ListView listcategories;
    ArrayList<EcommCategoryClass> listproducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ecommcategory, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Categories");
        ab.setHomeButtonEnabled(true);
        listproducts=new ArrayList<>();

        listcategories = (ListView) findViewById(R.id.listcategories);
        if(Utility.isInternetConnected(getApplicationContext()))
        {/*
            FetchProductCategory task=new FetchProductCategory();
            task.execute();*/
            FetchProductType task=new FetchProductType();
            task.execute();
        }
        else
        {
            Toast.makeText(EcommCategoryActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();
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
        listcategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                EcommCategoryClass cls=listproducts.get(position);
                int productid=cls.getId();
                String productname=cls.getCategoryName();
/*
                Utility.saveProductCategoryId(getApplicationContext(), productid);
                Intent intent=new Intent(getApplicationContext(),Ecomm_productsubcayegory.class);
                startActivity(intent);
                */
                Utility.saveProductCategoryId(getApplicationContext(), productid);
                Utility.saveCategoryName(getApplicationContext(),productname);
                Intent intent=new Intent(getApplicationContext(),EcommSubCategoryActivity.class);
                startActivity(intent);
                finish();


            }
        });

    }
    private class Fetchmycartcount extends AsyncTask<Void, Void, SoapObject> {
        private ProgressDialog progress;
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(EcommCategoryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETMYCARTCOUNT);

            request.addProperty("token", Utility.getAuthToken(EcommCategoryActivity.this));

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
                if (Utility.getUserType(EcommCategoryActivity.this).equals("E")) {


                } else {
                    Intent intent = new Intent(EcommCategoryActivity.this,Ecomm_MyCart.class);
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

            case R.id.action_wishlist:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent i = new Intent(EcommCategoryActivity.this, EcommWishlistActivity.class);
                startActivity(i);
                finish();
                return true;

            case R.id.action_mycart:
                Intent intent = new Intent(EcommCategoryActivity.this,Ecomm_MyCart.class);
                startActivity(intent);
                finish();
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


/*
    class FetchProductType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommCategoryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","ProductType");


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
                        listproducts=new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);

                            String name=soapResult.getPropertySafelyAsString("Name", "")
                                    .toString();
                            int id=Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString());

                            listproducts.add(new EcommCategoryClass(name,id));

                        }

                        if(listproducts.size()>0) {
                            listcategories.setAdapter(new EcommCategoryAdapter(getApplicationContext(), listproducts));
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

                Toast.makeText(EcommCategoryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }
*/


    class FetchProductType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommCategoryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTTYPE);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));


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
                        listproducts=new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);

                            String name=soapResult.getPropertySafelyAsString("ProductTypeName", "")
                                    .toString();
                            int id=Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString());

                            listproducts.add(new EcommCategoryClass(name,id));

                        }

                        if(listproducts.size()>0) {
                            listcategories.setAdapter(new EcommCategoryAdapter(getApplicationContext(), listproducts));
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

                Toast.makeText(EcommCategoryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class FetchProductCategory extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommCategoryActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODSUCTCATEGORY);

            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_PRODSUCTCATEGORY);

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
                            + Utility.GET_PRODSUCTCATEGORY, mySoapEnvelop);


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


                            listproducts.add(new EcommCategoryClass(name,id));
                        }

                        if(listproducts.size()>0) {
                            listcategories.setAdapter(new EcommCategoryAdapter(getApplicationContext(), listproducts));
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

                Toast.makeText(EcommCategoryActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),EcommDashboardActivity.class);
        startActivity(i);
        finish();
    }

}
