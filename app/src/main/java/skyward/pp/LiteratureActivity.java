package skyward.pp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

import skyward.pp.adapter.Literature;
import skyward.pp.holder.LiteratureListHolder;
import skyward.pp.model.ProductClass;
import skyward.pp.util.Utility;

public class LiteratureActivity extends AppCompatActivity {

    ListView listliterature;
    ProductClass product;
    String LiteratureName,LiteratureImage,LiteratureID,Filepath,FileName, ProductID;
    boolean flag_loading;
    int pageindex= 1;
    ArrayList<Literature> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_literature);

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

                Intent i = new Intent(LiteratureActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        listliterature = (ListView) findViewById(R.id.listliterature);

        if (!Utility.isInternetConnected(LiteratureActivity.this)) {

            Toast.makeText(LiteratureActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();


        }else {
            mArrayList=new ArrayList<>();
            new FetchProduct().execute();

        }

        mArrayList=new ArrayList<>();
        listliterature.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {

                        flag_loading = true;
                        pageindex += 1;

                        FetchProduct task = new FetchProduct();
                        task.execute();
                    }
                }
            }
        });

        listliterature.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Literature obj=mArrayList.get(position);
                Intent i=new Intent(LiteratureActivity.this,ViewLiteratureActivity.class);
                i.putExtra("Filepath", obj.getFilePath());
                i.putExtra("fromcustomer", "fromcustomer");

                startActivity(i);
                finish();


            }
        });
    }


    class FetchProduct extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(LiteratureActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_ALLLITERATURE);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("pageIndex",pageindex);
            request.addProperty("pageSize",10);
            request.addProperty("ProductType",Utility.getProductCategoryId(getApplicationContext()));


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
                            + Utility.GET_ALLLITERATURE, mySoapEnvelop);


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

                flag_loading = false;
            if(result != null) {
                try {
                    if(progress.isShowing()) {
                        progress.dismiss();
                    }
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
                            LiteratureName = soapResult.getPrimitivePropertySafelyAsString("LiteratureName").toString();
                            LiteratureImage = soapResult.getPrimitivePropertySafelyAsString("ImagePath").toString();
                            LiteratureID = soapResult.getPrimitivePropertySafelyAsString("ID").toString();
                            Filepath = soapResult.getPrimitivePropertySafelyAsString("FilePath").toString();
                            FileName = soapResult.getPrimitivePropertySafelyAsString("FileName").toString();
                            ProductID = soapResult.getPrimitivePropertySafelyAsString("ProductID").toString();


                            mArrayList.add(new Literature(LiteratureName, LiteratureID,LiteratureImage, ProductID,  Filepath, FileName));

                        }
                        listliterature.setAdapter(new CustomerLiteratureAdapter(getApplicationContext(), mArrayList));

                    } else {
/*
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
*/
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    if(progress.isShowing()) {
                        progress.dismiss();
                    }
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    if(progress.isShowing()) {
                        progress.dismiss();
                    }
                    e.printStackTrace();
                }

            }else{
                if(progress.isShowing()) {

                    progress.dismiss();
                }
                Toast.makeText(LiteratureActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class CustomerLiteratureAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater inflator;
        String imagepath,productname,finalpath;

        private ArrayList<Literature> productlist;

        public CustomerLiteratureAdapter(Context mContext, ArrayList<Literature> productlist) {
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
            ImageView img_product,img_share;
            TextView txt_productnamae;
            String urlforimage;
            int loader = R.drawable.loader;
            final LiteratureListHolder item;


            if(convertView == null)
            {
                convertView = inflator.inflate(R.layout.customerliterature_listitem,null);
                img_product = (ImageView) convertView.findViewById(R.id.clist_productimg);
                img_share = (ImageView) convertView.findViewById(R.id.img_shareliterature);
                txt_productnamae= (TextView) convertView.findViewById(R.id.clist_productname);
                item = new LiteratureListHolder(txt_productnamae,img_product,img_share);
                convertView.setTag(item);


            }
            else
            {
                item = (LiteratureListHolder) convertView.getTag();
            }
            productname = productlist.get(position).getLiteratureName();
            imagepath=productlist.get(position).getLiteraturePath();
            imagepath = imagepath.replace("\\", "/");


            finalpath= Utility.URLFORIMAGE+imagepath;
            //MyVideos videos = arrayList.get(position);

            Glide.with(mContext).load(finalpath).into(item.getImg_ProductImage());
            item.getTxt_ProductName().setText(productname);
            //item.getDisplayvideo().setImageBitmap(retriveVideoFrameFromVideo("http://192.168.1.25:5090/" + filepath));
            item.getImg_share().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
/*
                    Toast.makeText(mContext, "button is pressed", Toast.LENGTH_SHORT).show();
*/
                    imagepath = productlist.get(position).getFilePath();
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

            return convertView;

        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LiteratureActivity.this, LiteratureCategory.class);
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
}
