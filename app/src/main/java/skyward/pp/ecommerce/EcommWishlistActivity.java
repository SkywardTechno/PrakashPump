package skyward.pp.ecommerce;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.app.AlertDialog;
import android.view.LayoutInflater;
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

import skyward.pp.R;
import skyward.pp.holder.WishlistViewHolder;
import skyward.pp.model.WishlistClass;
import skyward.pp.util.Utility;

public class EcommWishlistActivity extends NavigationHeader {
    public static int posdel=0;
public static int wishlistidtask=0;
    ListView listwishlist;
    ArrayList<WishlistClass> listcart;
    WishlistClass obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ecomm_wishlist, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("My Wishlist");
        ab.setHomeButtonEnabled(true);

       listwishlist = (ListView) findViewById(R.id.listwishlist);
        listcart=new ArrayList<>();
//btn_placeorder= (Button) findViewById(R.id.wishlist_btn_placeorder);
      //  btn_placeorder.setVisibility(View.GONE);

        if(Utility.isInternetConnected(getApplicationContext()))
        {
            FetchUserWishlist task=new FetchUserWishlist();
            task.execute();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Check your internet Connection", Toast.LENGTH_SHORT).show();

        }
        listwishlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                obj = (WishlistClass) parent.getItemAtPosition(position);

                Utility.saveProductId(getApplicationContext(), obj.getProductid());
                Intent i =new Intent(getApplicationContext(),EcommProductSpecificationActivity.class);
                startActivity(i);
                finish();
            }
        });
    }



    class FetchUserWishlist extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(EcommWishlistActivity.this);
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

                       /* if(count>0)
                        {
                            btn_placeorder.setVisibility(View.VISIBLE);

                        }
                        if(count==0)
                        {
                            btn_placeorder.setVisibility(View.GONE);

                        }*/
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

                            /*int cartid=Integer.valueOf(Integer.parseInt(soapResult.getPropertySafelyAsString(

                                    "CartID").toString()));
*/
                           int cartid=Integer.valueOf(soapResult.getPropertySafelyAsString("WishListID","0"));

                            int productid=Integer.valueOf(Integer.parseInt(soapResult.getPropertySafelyAsString(

                                    "ProductID","0").toString()));

                            double price=Double.valueOf(soapResult.getPropertySafelyAsString(

                                    "Price","0").toString());
                            int quantity=Integer.valueOf(Integer.parseInt(soapResult.getPropertySafelyAsString(

                                    "Quantity","0").toString()));


                            String imagepath=soapResult.getPropertySafelyAsString("ImagePath", "")
                                    .toString();
///need to change available qty

                         listcart.add(new WishlistClass(cartid,productid,productname,modelno,quantity,price,imagepath, currency));
                        }

                        if(listcart.size()>0) {
 //                           cart_grandtotal.setText(grandtotal+"");
                            listwishlist.setAdapter(new wishlistAdapter1(getApplicationContext(), listcart));

                        }





                    } else {
                       // btn_placeorder.setVisibility(View.GONE);

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

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public class wishlistAdapter1 extends BaseAdapter {
        String imagepath, productname, finalpath;

        Context mContext;
        LayoutInflater inflator;
        private ArrayList<WishlistClass> arrayList;

        public wishlistAdapter1(Context mContext, ArrayList<WishlistClass> videoList) {
            this.mContext = mContext;
            this.arrayList = videoList;
            inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageButton btndelete;
            ImageView imgproduct;
            TextView txtproductname, txtmodelno, txtqty, txtprice;
            final WishlistViewHolder item;

            if (convertView == null) {
                convertView = inflator.inflate(R.layout.item_wishlist, null);
                btndelete = (ImageButton) convertView.findViewById(R.id.imgbtn_wishlist_delete);
                txtproductname = (TextView) convertView.findViewById(R.id.txt_wishlist_productname);
                txtmodelno = (TextView) convertView.findViewById(R.id.txt_wishlist_modelno);
                txtprice = (TextView) convertView.findViewById(R.id.txt_wishlist_price);
               // txtqty = (TextView) convertView.findViewById(R.id.txt_wishlist_quantity);
                imgproduct = (ImageView) convertView.findViewById(R.id.img_wishlist_productimg);
                item = new WishlistViewHolder(txtproductname, txtmodelno,  txtprice, btndelete, imgproduct);
                convertView.setTag(item);
                btndelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        posdel = (Integer) v.getTag();



                        posdel= (Integer) v.getTag();
                        AlertDialog alertDialog = new AlertDialog.Builder(EcommWishlistActivity.this).create();
                        alertDialog.setTitle("Confirm");
                        alertDialog.setMessage("Are you sure you want to Delete?");
                        alertDialog.setCancelable(false);
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();


                                            if (Utility.isInternetConnected(mContext)) {
                                                wishlistidtask=arrayList.get(posdel).getID();

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
            } else {
                item = (WishlistViewHolder) convertView.getTag();
            }
            imagepath = arrayList.get(position).getImagepath();
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

            finalpath = Utility.URLFORIMAGE + imagepath;
            //MyVideos videos = arrayList.get(position);

            Glide.with(mContext).load(finalpath).into(item.getImgproduct());
            item.getTctmodelno().setText(arrayList.get(position).getModelno());
            item.getTxtproductname().setText(arrayList.get(position).getProdutname());
            item.getTxtprice().setText(arrayList.get(position).getPrice() + " " + arrayList.get(position).getCurrency());
           // item.getTxtqty().setText(arrayList.get(position).getQty() + "");
            item.getBtndelete().setTag(position);


            return convertView;
        }


        class DeleteUserCartByuserId extends AsyncTask<Void, Void, SoapObject> {
            SoapObject result;
            private ProgressDialog progress;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //etClientName.setText("");
                progress = new ProgressDialog(EcommWishlistActivity.this);
                progress.setMessage("Please wait...");
                progress.setCancelable(false);
                progress.show();
            }

            @Override
            protected SoapObject doInBackground(Void... arg0) {
                SoapObject request = new SoapObject(Utility.NAMESPACE,
                        Utility.DELETEUSERWISHLISTBYID);

                request.addProperty("token", Utility.getAuthToken(EcommWishlistActivity.this));
                request.addProperty("WishListID", wishlistidtask);


                System.out.println("URL is ::" + Utility.SOAP_ACTION
                        + Utility.DELETEUSERWISHLISTBYID);

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
                                + Utility.DELETEUSERWISHLISTBYID, mySoapEnvelop);


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

                            Toast.makeText(EcommWishlistActivity.this,
                                    "Item deleted successfully",
                                    Toast.LENGTH_LONG).show();
                            arrayList.remove(arrayList.get(posdel));
                            notifyDataSetChanged();

                        } else {
                            Toast.makeText(EcommWishlistActivity.this,
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
