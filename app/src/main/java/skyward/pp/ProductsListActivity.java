package skyward.pp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

import skyward.pp.ecommerce.Ecomm_MyCart;
import skyward.pp.holder.ProductListHolder;
import skyward.pp.model.ProductClass;
import skyward.pp.util.Utility;

public class ProductsListActivity extends BaseActivity {

    int flowrateID=0,producttypeID=0,typeID=0,inletID=0,outletID=0,voltID=0;
    Double headID=0.0,powerID=0.0,powerhpID=0.0,headfeetID=0.0;
    String ProductImage,ProductName, ProductID,Text, Required,send_Text,send_Required,Productprice,Currency;
    ListView listproducts;
    ArrayList<ProductClass> ProductList;
    ProductClass product;
    int send_producttype,send_type,send_flowrate,send_inlet,send_outlet,send_volt;
    Double send_head,send_headfeet,send_power,send_powerhp;
    String ProductIDCart,totalpricecart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        ProductList=new ArrayList<>();
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

                Intent i = new Intent(ProductsListActivity.this, PumpselectionActivity.class);
                startActivity(i);
                finish();
            }
        });

        listproducts = (ListView) findViewById(R.id.listproducts);


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
        Text = b.getString("TcDetails");
        send_Text = Text;
        Required = b.getString("required");
        send_Required = Required;
        //Toast.makeText(ProductsListActivity.this, "Power="+powerID+"flowrate= "+flowrateID+"head="+headID+"protype= "+producttypeID+"typeid= "+typeID, Toast.LENGTH_SHORT).show();
        if (!Utility.isInternetConnected(ProductsListActivity.this)) {

            Toast.makeText(ProductsListActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
         new FetchProductsTask().execute();

        }


        Intent i = getIntent();
        Text = i.getStringExtra("TcDetails");
        Required = i.getStringExtra("required");
        listproducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductClass obj=ProductList.get(position);
                Intent i=new Intent(ProductsListActivity.this,ViewProductActivity.class);

                Bundle b = new Bundle();
                b.putDouble("Power", send_power);
                b.putInt("FlowRate",send_flowrate);
                b.putDouble("Head", send_head);
                b.putInt("ProductType", send_producttype);
                b.putInt("Type", send_type);
                b.putInt("Inlet", send_inlet);
                b.putInt("Outlet", send_outlet);
                b.putDouble("HeadFeet", send_headfeet);
                b.putInt("Volt", send_volt);
                b.putDouble("PowerHP", send_powerhp);
                b.putInt("productID",Integer.valueOf(obj.getProduct_id()));
                b.putString("TcDetails",send_Text);
                b.putString("required",send_Required);
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
            progress = new ProgressDialog(ProductsListActivity.this);
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
                            Productprice = soapResult.getPrimitivePropertySafelyAsString("NewPrice").toString();
                            Currency = soapResult.getPrimitivePropertySafelyAsString("Currency").toString();


                            //                        producttypelist.add(soapResult.getPropertySafelyAsString("Name", "")
//                                .toString());
//
//                        producttypelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
//                                "ID").toString()));
                            product = new ProductClass(ProductName, ProductImage, ProductID,Productprice, Currency,getApplicationContext());
                            ProductList.add(product);
                        }


//
//                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
//                            getApplicationContext(), R.layout.spinner_leadsrc, producttypelist);
//                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
//                    spin_productype.setAdapter(spinnerAdapter);
                        listproducts.setAdapter(new PumpSelectionProductAdapter(getApplicationContext(), ProductList));

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

                Toast.makeText(ProductsListActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ProductsListActivity.this, PumpselectionActivity.class);
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

    public class PumpSelectionProductAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflator;
        String imagepath, productname, finalpath;
        private ArrayList<ProductClass> productlist;

        public PumpSelectionProductAdapter(Context mContext, ArrayList<ProductClass> productlist) {
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
            ImageView img_share;
            ImageView img_mycart;
            TextView txt_productnamae;
            String urlforimage;
            int loader = R.drawable.loader;
            final ProductListHolder item;


            if (convertView == null) {
                convertView = inflator.inflate(R.layout.productsmaster_listitem, null);
                img_mycart = (ImageView) convertView.findViewById(R.id.img_list_productmycart);
                img_product = (ImageView) convertView.findViewById(R.id.list_productimg);
                txt_productnamae = (TextView) convertView.findViewById(R.id.list_productname);
                img_share = (ImageView) convertView.findViewById(R.id.img_list_productshare);
                item = new ProductListHolder(txt_productnamae, img_product, img_share, img_mycart);
                convertView.setTag(item);



                // img_mycart.setVisibility(View.GONE);

            } else {
                item = (ProductListHolder) convertView.getTag();
            }
            productname = productlist.get(position).getProduct_name();
            imagepath = productlist.get(position).getProduct_image();
            imagepath = imagepath.replace("\\", "/");

            item.getImg_mycart().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Utility.isInternetConnected(getApplicationContext()))
                    {
                        ProductIDCart = productlist.get(position).getProduct_id();
                        totalpricecart = productlist.get(position).getProduct_price();
                        AddtocartTask task=new AddtocartTask();
                        task.execute();
                    }
                    else
                    {
                        Toast.makeText(ProductsListActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            item.getImg_share().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
/*
                    Toast.makeText(mContext, "button is pressed", Toast.LENGTH_SHORT).show();
*/
                    imagepath = productlist.get(position).getProduct_image();
                    imagepath = imagepath.replace("\\", "/");

                    if(imagepath.contains(" ")) {
                        imagepath = imagepath.replace(" ", "");
                    }
                    System.out.println("**********" + imagepath);
                    finalpath = Utility.URLFORIMAGE + imagepath;

                    Intent sendIntent = new Intent();
                    sendIntent.setType("text/plain");
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, finalpath);

                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // Launches the activity; Open 'Text editor' if you set it as default app to handle Text
                    startActivity(sendIntent);


                }
            });

            finalpath = Utility.URLFORIMAGE + imagepath;
            System.out.println("*************************************"+finalpath);
            //MyVideos videos = arrayList.get(position);

            Glide.with(mContext).load(finalpath).into(item.getImg_ProductImage());
            item.getTxt_ProductName().setText(productname);
            //item.getDisplayvideo().setImageBitmap(retriveVideoFrameFromVideo("http://192.168.1.25:5090/" + filepath));


            return convertView;
        }

        public void shareText(View v) {

        }
    }

    class AddtocartTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(ProductsListActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.ADDTOCART);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ProductID",ProductIDCart);
            request.addProperty("ServiceID",0);
            request.addProperty("Quantity",1);
            request.addProperty("Price",totalpricecart);

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

Intent i = new Intent(getApplicationContext(), Ecomm_MyCart.class);
                        startActivity(i);
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
                Toast.makeText(ProductsListActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
