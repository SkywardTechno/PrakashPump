package skyward.pp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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

import skyward.pp.holder.LiteratureListHolder;
import skyward.pp.model.LiteratureListClass;
import skyward.pp.util.Utility;

public class AdminLiteratureList extends AppCompatActivity {

    ListView literaturename;
    String ProductImage,ProductName, ProductID, LiteratureID,productCode;
    ListView listproducts;
    ArrayList<LiteratureListClass> ProductList;
    LiteratureListClass product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_literaturelist);

        setTitle("Literature");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Literature");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminLiteratureList.this, AdminDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        ProductList=new ArrayList<>();

        literaturename = (ListView) findViewById(R.id.listadminliterature);
        if (!Utility.isInternetConnected(AdminLiteratureList.this)) {

            Toast.makeText(AdminLiteratureList.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            new FetchProductsTask().execute();

        }

        listproducts= (ListView) findViewById(R.id.listadminliterature);

   listproducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       @Override
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           LiteratureListClass obj=ProductList.get(position);
           Intent i=new Intent(AdminLiteratureList.this,AddLiteratureActivity.class);

           Bundle b = new Bundle();
           b.putInt("ID", Integer.parseInt(obj.getProduct_id()));
           b.putString("ProductName", obj.getProduct_name());
           i.putExtras(b);
           startActivity(i);
           finish();
       }
   });
    }

    /////////////////////getproduct list task///////////////////////////////


    class FetchProductsTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AdminLiteratureList.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTSBYCATEGORIES);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("productTypeID",0);
            request.addProperty("powerID",0);
            request.addProperty("typeID",0);
            request.addProperty("headID",0);
            request.addProperty("flowRateID",0);
            request.addProperty("InletID",0);
            request.addProperty("OutletID",0);
            request.addProperty("PowerHPID",0);
            request.addProperty("HeadFeetID",0);
            request.addProperty("voltID",0);

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
                    ProductList=new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        ProductName=soapResult.getPrimitivePropertySafelyAsString("ProductName").toString();
                        ProductImage=soapResult.getPrimitivePropertySafelyAsString("ImagePath").toString();
                        ProductID=soapResult.getPrimitivePropertySafelyAsString("ID").toString();
                        LiteratureID=soapResult.getPrimitivePropertySafelyAsString("literatureID").toString();
                        productCode=soapResult.getPrimitivePropertySafelyAsString("productCode").toString();


                        //                        producttypelist.add(soapResult.getPropertySafelyAsString("Name", "")
//                                .toString());
//
//                        producttypelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
//                                "ID").toString()));
                        product= new LiteratureListClass(ProductName+" - "+productCode,ProductImage,ProductID,LiteratureID,getApplicationContext());
                        ProductList.add(product);
                    }



//
//                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
//                            getApplicationContext(), R.layout.spinner_leadsrc, producttypelist);
//                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
//                    spin_productype.setAdapter(spinnerAdapter);
                    listproducts.setAdapter(new Adapter(getApplicationContext(),ProductList));

                } else {
                    Toast.makeText(getApplicationContext(),
                            soapObject.getProperty("ErrorMessage").toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }



    public class Adapter extends BaseAdapter {
        Context mContext;
        LayoutInflater inflator;
        int lid;
        String imagepath,productname,finalpath;

        private ArrayList<LiteratureListClass> productlist;

        public Adapter(Context mContext, ArrayList<LiteratureListClass> productlist) {
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
            ImageButton upin;
            TextView txt_productnamae;
            String urlforimage;
            int loader = R.drawable.loader;
            final LiteratureListHolder item;


            if(convertView == null)
            {
                convertView = inflator.inflate(R.layout.adminliterature_listitem,null);
                img_product = (ImageView) convertView.findViewById(R.id.list_productimg);
                txt_productnamae= (TextView) convertView.findViewById(R.id.list_productname);
                upin= (ImageButton) convertView.findViewById(R.id.list_upin);
                item = new LiteratureListHolder(txt_productnamae,img_product,upin);
                convertView.setTag(item);


            }
            else
            {
                item = (LiteratureListHolder) convertView.getTag();
            }
            productname = productlist.get(position).getProduct_name();
            lid= Integer.parseInt(productlist.get(position).getLit_id());

            if(lid==0)
            {
                //item.getUpin().setEnabled(false);
                item.getUpin().setVisibility(View.GONE);
            }
            else
            {
                item.getUpin().setVisibility(View.VISIBLE);
            }

            item.getUpin().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent();
                    i.setClass(mContext,AdminMultipleLiteratureList.class);
                    i.putExtra("LID",productlist.get(position).getProduct_id());
                    startActivity(i);
                    finish();

                }
            });
            imagepath = productlist.get(position).getProduct_image();
            imagepath = imagepath.replace("\\", "/");



            finalpath= Utility.URLFORIMAGE+imagepath;
            //MyVideos videos = arrayList.get(position);

            Glide.with(mContext).load(finalpath).into(item.getImg_ProductImage());
            item.getTxt_ProductName().setText(productname);
            //item.getDisplayvideo().setImageBitmap(retriveVideoFrameFromVideo("http://192.168.1.25:5090/" + filepath));


            return convertView;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminLiteratureList.this, AdminDashboardActivity.class);
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
    //////////////////////////////////////////////////////////////////////////


}
