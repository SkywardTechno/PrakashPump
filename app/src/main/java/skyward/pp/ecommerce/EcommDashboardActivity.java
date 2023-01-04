package skyward.pp.ecommerce;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import skyward.pp.LaunchActivity;
import skyward.pp.NotificationActivity;
import skyward.pp.R;
import skyward.pp.adapter.ItemClickListener;
import skyward.pp.adapter.OffersPromotionsDashboardAdapter;
import skyward.pp.model.EcommCategoryClass;
import skyward.pp.model.EcommSubcategoryClass;
import skyward.pp.model.OffersPromotionsclass;
import skyward.pp.util.Utility;


public class EcommDashboardActivity extends NavigationHeader {

    ArrayList<String> arrsearch = new ArrayList<>();
    ArrayList<Integer> arrsearchid = new ArrayList<>();
    ArrayList<Integer> arrsearchtypeid = new ArrayList<>();
    ArrayAdapter<String> adaptersearch;
    private TextView ui_hot = null;
    private TextView ui_hot1 = null;
    int hot_number = 0;
    LinearLayout ll_offers, ll_category1,ll_category2,ll_category3,ll_category4,ll_category5,ll_category6,ll_category7;
    TextView txt_title, txt_offerstitle, txt_category1, txt_category2, txt_category3,txt_category4,txt_category5,txt_category6,txt_category7;
    TextView txt_seealloffers;
    WebView details;
    ImageView img_shopbycategory;
    GridView grid_offers;
    RecyclerView mRecyclerView,recycler_view_category1, recycler_view_category2,recycler_view_category3,recycler_view_category4,recycler_view_category5,recycler_view_category6,recycler_view_category7;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    String prefix = "";
    SliderLayout sliderLayout;
    AutoCompleteTextView act_search;
    Dialog dialog;
    ArrayList<EcommSubcategoryClass> listproduct;
    ArrayList<OffersPromotionsclass> offerslist;
    OffersPromotionsclass offerspromotions;
    ArrayList<String> arrimagesurl;
    ArrayList<Integer> arrimagesurlID;
    int notificationtotalcount = 0;

    ArrayList<EcommCategoryClass> listproductcategory;
    int productcategoryid = 0;
    int totalcnt = 0;
    int catcnt = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_ecomm_dashboard, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Hi, " + Utility.getName(getApplicationContext()));
        ab.setHomeButtonEnabled(true);

        //img_shopbycategory = (ImageView) findViewById(R.id.img_shopbycategory);

        ll_offers = (LinearLayout) findViewById(R.id.ll_offers);
        ll_category1 = (LinearLayout) findViewById(R.id.ll_category1);
        ll_category2 = (LinearLayout) findViewById(R.id.ll_category2);
        ll_category3 = (LinearLayout) findViewById(R.id.ll_category3);
        ll_category4 = (LinearLayout) findViewById(R.id.ll_category4);
        ll_category5 = (LinearLayout) findViewById(R.id.ll_category5);
        ll_category6 = (LinearLayout) findViewById(R.id.ll_category6);
        ll_category7 = (LinearLayout) findViewById(R.id.ll_category7);

        txt_category1 = (TextView) findViewById(R.id.txt_category1);
        txt_category2 = (TextView) findViewById(R.id.txt_category2);
        txt_category3 = (TextView) findViewById(R.id.txt_category3);
        txt_category4 = (TextView) findViewById(R.id.txt_category4);
        txt_category5 = (TextView) findViewById(R.id.txt_category5);
        txt_category6 = (TextView) findViewById(R.id.txt_category6);
        txt_category7 = (TextView) findViewById(R.id.txt_category7);

        txt_offerstitle = (TextView) findViewById(R.id.txt_offersforyou);
        txt_seealloffers = (TextView) findViewById(R.id.txt_seealloffers);

        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        act_search = (AutoCompleteTextView) findViewById(R.id.act_search);

        grid_offers = (GridView) findViewById(R.id.grid_offers);

        recycler_view_category1 = (RecyclerView) findViewById(R.id.recycler_view_category1);
        recycler_view_category2 = (RecyclerView) findViewById(R.id.recycler_view_category2);
        recycler_view_category3 = (RecyclerView) findViewById(R.id.recycler_view_category3);
        recycler_view_category4 = (RecyclerView) findViewById(R.id.recycler_view_category4);
        recycler_view_category5 = (RecyclerView) findViewById(R.id.recycler_view_category5);
        recycler_view_category6 = (RecyclerView) findViewById(R.id.recycler_view_category6);
        recycler_view_category7 = (RecyclerView) findViewById(R.id.recycler_view_category7);

        recycler_view_category1.setHasFixedSize(true);
        recycler_view_category2.setHasFixedSize(true);
        recycler_view_category3.setHasFixedSize(true);
        recycler_view_category4.setHasFixedSize(true);
        recycler_view_category5.setHasFixedSize(true);
        recycler_view_category6.setHasFixedSize(true);
        recycler_view_category7.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_category1.setLayoutManager(mLayoutManager);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_category2.setLayoutManager(mLayoutManager);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_category3.setLayoutManager(mLayoutManager);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_category4.setLayoutManager(mLayoutManager);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_category5.setLayoutManager(mLayoutManager);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_category6.setLayoutManager(mLayoutManager);

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_category7.setLayoutManager(mLayoutManager);

        arrimagesurl = new ArrayList<>();
        arrimagesurlID = new ArrayList<>();

        String banner1 = Utility.URLFORIMAGE + "Product/ProductImage/pcp-05_c05461ec-151a-4f76-87bd-1dec590ac209.png";
        String banner2 = Utility.URLFORIMAGE + "Product/ProductImage/pcp-20a.png";
        String banner3 = Utility.URLFORIMAGE + "Product/ProductImage/pcp-30a.png";


        arrimagesurl.add(banner1);
        arrimagesurl.add(banner2);
        arrimagesurl.add(banner3);

        arrimagesurlID.add(1);
        arrimagesurlID.add(5);
        arrimagesurlID.add(6);


        for (int i = 0; i < arrimagesurl.size(); i++) {
            final int finalI = i;
            TextSliderView textSliderView = new TextSliderView(EcommDashboardActivity.this);
            textSliderView
                    // .description(name)
                    .image(arrimagesurl.get(i))
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {

                            Utility.savefromdashboardorcategory(getApplicationContext(), 1);
                            Utility.saveProductId(getApplicationContext(), arrimagesurlID.get(finalI));
                            Intent i = new Intent(getApplicationContext(), EcommProductSpecificationActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

            textSliderView.setScaleType(BaseSliderView.ScaleType.FitCenterCrop);
            textSliderView.bundle(new Bundle());
            sliderLayout.addSlider(textSliderView);

        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(6000);


        if (Utility.isInternetConnected(getApplicationContext())) {

            FetchProductType task2 = new FetchProductType();
            task2.execute();
            getNotification task = new getNotification();
            task.execute();
            Fetchmycartcount task1 = new Fetchmycartcount();
            task1.execute();
            FecthOffers taskoffer = new FecthOffers();
            taskoffer.execute();
        }

        act_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prefix = s.toString();
                if (Utility.isInternetConnected(getApplicationContext())) {
                    FetchsearchresultTask task = new FetchsearchresultTask();
                    task.execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        act_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int searchtypeid = arrsearchtypeid.get(position);
                int searchid = arrsearchid.get(position);
                String searchname = arrsearch.get(position);
                if (searchtypeid == 1) {
                    Intent intent = new Intent(getApplicationContext(), EcommCategoryActivity.class);
                    startActivity(intent);
                    finish();
                } else if (searchtypeid == 3) {

                    Utility.saveProductId(getApplicationContext(), searchid);
                    Utility.savefromdashboardorcategory(getApplicationContext(), 1);
                    Intent intent = new Intent(getApplicationContext(), EcommProductSpecificationActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        txt_seealloffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  =  new Intent(getApplicationContext(), EcommOffersActivity.class);
                startActivity(i);
                finish();
            }
        });

        grid_offers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                offerspromotions = (OffersPromotionsclass) parent.getItemAtPosition(position);


                Utility.savefromdashboardorcategory(getApplicationContext(), 1);
                // Toast.makeText(MyVideosActivity.this, "VideoClicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EcommOffersCategoryActivity.class);
                intent.putExtra("ID", offerspromotions.getId());

                String imagepath = offerspromotions.getOfferimage();
                Utility.saveofferimagepath(getApplicationContext(), imagepath);
                Utility.saveOfferId(getApplicationContext(), Integer.valueOf(offerspromotions.getId()));
                startActivity(intent);
                finish();

            }
        });
    }


    public class FecthOffers extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommDashboardActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_OFFERSBYUSERID);


            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));


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
            if (result != null) {
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
                        if (count >= 2) {
                            count = 2;
                        } else {

                        }
                        System.out.println("Count offer is : " + count);
                        offerslist = new ArrayList<>();

                        for (int i = 0; i < count; i++) {
                            SoapObject obj = (SoapObject) result4.getProperty(i);
                            String imagepath = obj.getPropertySafelyAsString("ImagePath", "");
                            int id = Integer.valueOf(obj.getPropertySafelyAsString("ID", "0"));
                            String decs = obj.getPropertySafelyAsString("OfferName", "");
                            offerslist.add(new OffersPromotionsclass(id + "", decs, imagepath));
                        }


                        if(offerslist.size()>0)
                        {
                            ll_offers.setVisibility(View.VISIBLE);
                            grid_offers.setAdapter(new OffersPromotionsDashboardAdapter(getApplicationContext(), offerslist));
                            //setListViewHeightBasedOnChildrenproducts(grid_offers);

                        }else{
                            ll_offers.setVisibility(View.GONE);

                        }


                    } else {

                        ll_offers.setVisibility(View.GONE);

                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            } else {

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public  void setListViewHeightBasedOnChildrenproducts(GridView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            if(listAdapter.getCount() %2 == 0){
                totalHeight += listItem.getMeasuredHeight()/2;
            }else {
                totalHeight += listItem.getMeasuredHeight() - 90;
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

            params.height = totalHeight + ((listAdapter.getCount() - 1)) ;

        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    class FetchProductType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommDashboardActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTTYPE);

            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));


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
            if (result != null) {
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

                        if (count > 7) {
                            count = 7;
                        } else {

                        }

                        System.out.println("Count is : " + count);

                        listproductcategory = new ArrayList<>();

                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);

                            String name = soapResult.getPropertySafelyAsString("ProductTypeName", "")
                                    .toString();
                            int id = Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString());

                            listproductcategory.add(new EcommCategoryClass(name, id));

                        }

                        if (listproductcategory.size() == 1) {

                            txt_category1.setText(listproductcategory.get(0).getCategoryName());
                            ll_category2.setVisibility(View.GONE);
                            ll_category3.setVisibility(View.GONE);
                            ll_category4.setVisibility(View.GONE);
                            ll_category5.setVisibility(View.GONE);
                            ll_category6.setVisibility(View.GONE);
                            ll_category7.setVisibility(View.GONE);

                        }else  if (listproductcategory.size() == 2) {

                            txt_category1.setText(listproductcategory.get(0).getCategoryName());
                            txt_category2.setText(listproductcategory.get(1).getCategoryName());
                            ll_category3.setVisibility(View.GONE);
                            ll_category4.setVisibility(View.GONE);
                            ll_category5.setVisibility(View.GONE);
                            ll_category6.setVisibility(View.GONE);
                            ll_category7.setVisibility(View.GONE);

                        }else  if (listproductcategory.size() == 3) {

                            txt_category1.setText(listproductcategory.get(0).getCategoryName());
                            txt_category2.setText(listproductcategory.get(1).getCategoryName());
                            txt_category3.setText(listproductcategory.get(2).getCategoryName());
                            ll_category4.setVisibility(View.GONE);
                            ll_category5.setVisibility(View.GONE);
                            ll_category6.setVisibility(View.GONE);
                            ll_category7.setVisibility(View.GONE);

                        }else  if (listproductcategory.size() == 4) {

                            txt_category1.setText(listproductcategory.get(0).getCategoryName());
                            txt_category2.setText(listproductcategory.get(1).getCategoryName());
                            txt_category3.setText(listproductcategory.get(2).getCategoryName());
                            txt_category4.setText(listproductcategory.get(3).getCategoryName());
                            ll_category5.setVisibility(View.GONE);
                            ll_category6.setVisibility(View.GONE);
                            ll_category7.setVisibility(View.GONE);

                        }else  if (listproductcategory.size() == 5) {

                            txt_category1.setText(listproductcategory.get(0).getCategoryName());
                            txt_category2.setText(listproductcategory.get(1).getCategoryName());
                            txt_category3.setText(listproductcategory.get(2).getCategoryName());
                            txt_category4.setText(listproductcategory.get(3).getCategoryName());
                            txt_category5.setText(listproductcategory.get(4).getCategoryName());
                            ll_category6.setVisibility(View.GONE);
                            ll_category7.setVisibility(View.GONE);

                        }else  if (listproductcategory.size() == 6) {

                            txt_category1.setText(listproductcategory.get(0).getCategoryName());
                            txt_category2.setText(listproductcategory.get(1).getCategoryName());
                            txt_category3.setText(listproductcategory.get(2).getCategoryName());
                            txt_category4.setText(listproductcategory.get(3).getCategoryName());
                            txt_category5.setText(listproductcategory.get(4).getCategoryName());
                            txt_category6.setText(listproductcategory.get(5).getCategoryName());
                            ll_category7.setVisibility(View.GONE);

                        }else  if (listproductcategory.size() == 7) {

                            txt_category1.setText(listproductcategory.get(0).getCategoryName());
                            txt_category2.setText(listproductcategory.get(1).getCategoryName());
                            txt_category3.setText(listproductcategory.get(2).getCategoryName());
                            txt_category4.setText(listproductcategory.get(3).getCategoryName());
                            txt_category5.setText(listproductcategory.get(4).getCategoryName());
                            txt_category6.setText(listproductcategory.get(5).getCategoryName());
                            txt_category7.setText(listproductcategory.get(6).getCategoryName());

                        }else{
                            ll_category1.setVisibility(View.GONE);
                            ll_category2.setVisibility(View.GONE);
                            ll_category3.setVisibility(View.GONE);
                            ll_category4.setVisibility(View.GONE);
                            ll_category5.setVisibility(View.GONE);
                            ll_category6.setVisibility(View.GONE);
                            ll_category7.setVisibility(View.GONE);
                        }

                        totalcnt = listproductcategory.size();
                        if (totalcnt > 0) {
                            catcnt = 0;
                            productcategoryid = listproductcategory.get(catcnt).getId();
                            FetchProductsByCategoryID task = new FetchProductsByCategoryID();
                            task.execute();
                        }


                    } else {
                        ll_category1.setVisibility(View.GONE);
                        ll_category2.setVisibility(View.GONE);
                        ll_category3.setVisibility(View.GONE);
                        ll_category4.setVisibility(View.GONE);
                        ll_category5.setVisibility(View.GONE);
                        ll_category6.setVisibility(View.GONE);
                        ll_category7.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } else {

                Toast.makeText(EcommDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    class FetchProductsByCategoryID extends AsyncTask<Void, Void, SoapObject> {
            SoapObject result;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //etClientName.setText("");

            }

            @Override
            protected SoapObject doInBackground(Void... arg0) {
                SoapObject request = new SoapObject(Utility.NAMESPACE,
                        Utility.GET_PRODUCTBYPRODUCTYPEID);

                request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
                request.addProperty("ProductTypeID",productcategoryid);

                System.out.println("URL is ::" + Utility.SOAP_ACTION
                        + Utility.GET_PRODUCTBYPRODUCTYPEID);

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
                                + Utility.GET_PRODUCTBYPRODUCTYPEID, mySoapEnvelop);


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

                                String price=soapResult.getPropertySafelyAsString("NewPrice", "")
                                        .toString();

                                String currency=soapResult.getPropertySafelyAsString("Currency", "")
                                        .toString();
                                String wholeprice = price +" "+currency;
                                listproduct.add(new EcommSubcategoryClass(name,imagepath,id+"",wholeprice,getApplicationContext()));
                            }

                            if(catcnt == 0){
                                mAdapter = new DashboardProductsAdapter(EcommDashboardActivity.this, listproduct);
                                recycler_view_category1.setAdapter(mAdapter);
                            }else if(catcnt == 1){
                                mAdapter = new DashboardProductsAdapter(EcommDashboardActivity.this, listproduct);
                                recycler_view_category2.setAdapter(mAdapter);
                            }else if(catcnt == 2){
                                mAdapter = new DashboardProductsAdapter(EcommDashboardActivity.this, listproduct);
                                recycler_view_category3.setAdapter(mAdapter);
                            }else if(catcnt == 3){
                                mAdapter = new DashboardProductsAdapter(EcommDashboardActivity.this, listproduct);
                                recycler_view_category4.setAdapter(mAdapter);
                            }else if(catcnt == 4){
                                mAdapter = new DashboardProductsAdapter(EcommDashboardActivity.this, listproduct);
                                recycler_view_category5.setAdapter(mAdapter);
                            }else if(catcnt == 5){
                                mAdapter = new DashboardProductsAdapter(EcommDashboardActivity.this, listproduct);
                                recycler_view_category6.setAdapter(mAdapter);
                            }else if(catcnt == 6){
                                mAdapter = new DashboardProductsAdapter(EcommDashboardActivity.this, listproduct);
                                recycler_view_category7.setAdapter(mAdapter);
                            }

                            catcnt++;
                            if(totalcnt > catcnt){
                                productcategoryid = listproductcategory.get(catcnt).getId();
                                FetchProductsByCategoryID task = new FetchProductsByCategoryID();
                                task.execute();
                            }


                        } else {

                            if(catcnt == 0){
                                ll_category1.setVisibility(View.GONE);
                            }else if(catcnt == 1){
                                ll_category2.setVisibility(View.GONE);
                            }else if(catcnt == 2){
                                ll_category3.setVisibility(View.GONE);
                            }else if(catcnt == 3){
                                ll_category4.setVisibility(View.GONE);
                            }else if(catcnt == 4){
                                ll_category5.setVisibility(View.GONE);
                            }else if(catcnt == 5){
                                ll_category6.setVisibility(View.GONE);
                            }else if(catcnt == 6){
                                ll_category7.setVisibility(View.GONE);
                            }

                            System.out.println("catcnt : "+catcnt);
                            catcnt++;
                            if(totalcnt > catcnt){
                                productcategoryid = listproductcategory.get(catcnt).getId();
                                FetchProductsByCategoryID task = new FetchProductsByCategoryID();
                                task.execute();
                            }
                           /* Toast.makeText(getApplicationContext(),
                                    soapObject.getProperty("ErrorMessage").toString(),
                                    Toast.LENGTH_LONG).show();*/

                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }else {

                    Toast.makeText(EcommDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        }



/*
        class FetchProductsByProducTypeID extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommDashboardActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTBYPRODUCTYPEID);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ProductTypeID",Utility.getProductCategoryId(getApplicationContext()));

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_PRODUCTBYPRODUCTYPEID);

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
                            + Utility.GET_PRODUCTBYPRODUCTYPEID, mySoapEnvelop);


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

                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            String name=soapResult.getPropertySafelyAsString("ProductName", "")
                                    .toString();
                            int id=Integer.valueOf(Integer.parseInt(soapResult.getPropertySafelyAsString(

                                    "ID").toString()));

                            String imagepath=soapResult.getPropertySafelyAsString("ImagePath", "")
                                    .toString();
                            String finalimagepath = Utility.URLFORIMAGE +imagepath;


                            listproduct.add(new EcommSubcategoryClass(name,imagepath,id+"",getApplicationContext()));
                        }

                        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        mRecyclerView.setLayoutManager(mLayoutManager);

                        mAdapter = new RecyclerViewAdapter(EcommDashboardActivity.this, listproduct);
                        mRecyclerView.setAdapter(mAdapter);

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

                Toast.makeText(EcommDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }
*/



    private class Fetchmycartcount extends AsyncTask<Void, Void, SoapObject> {
        private ProgressDialog progress;
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(EcommDashboardActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETMYCARTCOUNT);

            request.addProperty("token", Utility.getAuthToken(EcommDashboardActivity.this));

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


    class FetchsearchresultTask extends AsyncTask<Void, Void, SoapObject> {
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
                    Utility.GLOBALSEARCH);
            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("SearchValue", prefix);
            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GLOBALSEARCH);

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
                            + Utility.GLOBALSEARCH, mySoapEnvelop);


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
                        arrsearch = new ArrayList<>();
                        arrsearchid = new ArrayList<>();
                        arrsearchtypeid = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            String name = soapResult.getPropertySafelyAsString("Name", "")
                                    .toString();
                            int searchid = Integer.parseInt(soapResult.getPropertySafelyAsString("ID", "0")
                                    .toString());
                            int searchtypeid = Integer.parseInt(soapResult.getPropertySafelyAsString("Type", "0")
                                    .toString());

                            arrsearch.add(name);
                            arrsearchid.add(searchid);
                            arrsearchtypeid.add(searchtypeid);
                        }

                        if (arrsearch.size() > 0) {

                            adaptersearch = new ArrayAdapter<String>(EcommDashboardActivity.this, R.layout.companydropdown, arrsearch);
                            act_search.setAdapter(adaptersearch);
                            act_search.setThreshold(1);
                            adaptersearch.notifyDataSetChanged();


                        } else {


                        }
                    }
                }
                catch (Exception e)
                {

                }
            }else {

                Toast.makeText(EcommDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class getNotification extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_NOTIFICATION);

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;

            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));

            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.GET_NOTIFICATION, mySoapEnvelop);

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

                    if (!Utility.isInternetConnected(getApplicationContext())) {
                        Toast.makeText(EcommDashboardActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    } else {

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
                            System.out.println("Result5 is : " + result5.toString());

                            final int count = result4.getPropertyCount();
                            notificationtotalcount = count;
                            updateNotiCount(notificationtotalcount);
                            if(count == Utility.getnoticount(getApplicationContext())){

                            }else{

                                dialog = new Dialog(EcommDashboardActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.popup_notification);
                                dialog.setCancelable(false);
                                dialog.show();
                                // set the custom dialog components - text, image and button
                                txt_title = (TextView) dialog.findViewById(R.id.txt_title);

                                details = (WebView) dialog.findViewById(R.id.noti_details);
                                TextView ok = (TextView) dialog.findViewById(R.id.btn_ok);
                                TextView go = (TextView) dialog.findViewById(R.id.btn_goto);

                                String DetailedDescription = result5.getPrimitivePropertySafelyAsString("Remarks").toString();

                                if(count - Integer.valueOf(Utility.getnoticount(getApplicationContext())) > 1){
                                    txt_title.setText("Message");

                                    String msg = "You have more than one Notification !";
                                    details.loadData(msg, "text/html", "utf-8");
                                }else{
                                    txt_title.setText(result5.getPrimitivePropertySafelyAsString("ShortTitle").toString());

                                    details.loadData(DetailedDescription, "text/html", "utf-8");

                                }

                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();

                                        Utility.savenoticount(getApplicationContext(),count);
                                    }
                                });

                                go.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        Utility.savenoticount(getApplicationContext(), count);

                                        Intent i = new Intent(getApplicationContext(), NotificationActivity.class);
                                        i.putExtra("fromss","fromss");
                                        startActivity(i);
                                        finish();
                                    }
                                });




                            }

                        } else {


                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {


            }

        }
    }


    ///Product Adapter////
    public class DashboardProductsAdapter extends RecyclerView.Adapter<DashboardProductsAdapter.ViewHolder> {

        ArrayList<EcommSubcategoryClass> alName;

        Context context;

        public DashboardProductsAdapter(Context context, ArrayList<EcommSubcategoryClass> alName) {
            super();
            this.context = context;
            this.alName = alName;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.dashboard_product_listitem, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            viewHolder.tvSpecies.setText(alName.get(i).getProduct_name());
            viewHolder.price.setText(alName.get(i).getPrice());
            String imagepath = Utility.URLFORIMAGE+alName.get(i).getProduct_image();
            Glide.with(context).load(imagepath).into(viewHolder.imgThumbnail);

            final int finali = i;
            viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (isLongClick) {

                    } else {
                        Utility.savefromdashboardorcategory(getApplicationContext(), 1);
                        Utility.saveProductId(context, Integer.parseInt(alName.get(finali).getProduct_id()));
                        Intent i  = new Intent(context, EcommProductSpecificationActivity.class);
                        startActivity(i);
                        finish();

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return alName.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            public ImageView imgThumbnail;
            public TextView tvSpecies;
            public TextView price;
            private ItemClickListener clickListener;

            public ViewHolder(View itemView) {
                super(itemView);
                imgThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
                tvSpecies = (TextView) itemView.findViewById(R.id.title);
                price = (TextView) itemView.findViewById(R.id.count);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            public void setClickListener(ItemClickListener itemClickListener) {
                this.clickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getPosition(), false);
            }

            @Override
            public boolean onLongClick(View view) {
                clickListener.onClick(view, getPosition(), true);
                return true;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_dash_onlycart, menu);

        MenuItem item1 = menu.findItem(R.id.action_noti);
        MenuItemCompat.setActionView(item1, R.layout.action_bar_annnouncement_icon);
        View view1 = MenuItemCompat.getActionView(item1);
        ui_hot1 = (TextView)view1.findViewById(R.id.hotlist_hot);
        updateNotiCount(notificationtotalcount);
        new MyMenuItemStuffListener(view1, "Notifications") {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EcommDashboardActivity.this,NotificationActivity.class);
                startActivity(intent);
                finish();

            }
        };

        MenuItem item = menu.findItem(R.id.action_mycart);
        MenuItemCompat.setActionView(item, R.layout.action_bar_notifitcation_icon);
        View view = MenuItemCompat.getActionView(item);
        ui_hot = (TextView)view.findViewById(R.id.hotlist_hot);
        updateHotCount(hot_number);
        new MyMenuItemStuffListener(view, "My Cart") {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EcommDashboardActivity.this,Ecomm_MyCart.class);
                startActivity(intent);
                finish();

            }
        };

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

    public void updateNotiCount(final int new_hot_number) {
        notificationtotalcount = new_hot_number;
        if (ui_hot1 == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (notificationtotalcount == 0)
                    ui_hot1.setVisibility(View.INVISIBLE);
                else {
                    ui_hot1.setVisibility(View.VISIBLE);
                    ui_hot1.setText(Integer.toString(new_hot_number));
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

            case R.id.action_noti:
                Intent i1 = new Intent(EcommDashboardActivity.this, NotificationActivity.class);
                i1.putExtra("fromecomm","fromecomm");
                startActivity(i1);
                finish();
                return true;

            case R.id.action_mycart:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent i = new Intent(EcommDashboardActivity.this, Ecomm_MyCart.class);
                startActivity(i);
                finish();
                return true;


              /*  Intent intent = new Intent(EcommEcommEcommDashboardActivity.this,LoginActivity.class);
                startActivity(intent);*/
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),LaunchActivity.class
        );
        startActivity(intent);
        finish();
    }


}
