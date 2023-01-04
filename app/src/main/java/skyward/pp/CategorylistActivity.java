package skyward.pp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
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

import skyward.pp.adapter.CategoryListAdapter;
import skyward.pp.model.CategoryListClass;
import skyward.pp.util.Utility;

public class CategorylistActivity extends AppCompatActivity {

    AutoCompleteTextView cateogry_search;
    ListView listcategory;
    TextView txtcattype;
    ImageButton addcategory;
    String strValue="",strInputValue="";
    int id,catId;
    ArrayList<CategoryListClass> mArrayList;
    private ArrayList<String> categorylist = new ArrayList<String>();
    private ArrayList<Integer>  categorylistid = new ArrayList<Integer>();
    CategoryListClass obj;
    String value="";
    String UserType;
    private ArrayList<String> categorytypelist = new ArrayList<String>();
    private ArrayList<Integer> categorytypelistID = new ArrayList<Integer>();
    public int tempusertype = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);

        setTitle("Category");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Category");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CategorylistActivity.this, AdminDashboardActivity.class);
                startActivity(i);
                finish();
            }
        });


        mArrayList=new ArrayList<>();
        //cateogry_search = (AutoCompleteTextView) findViewById(R.id.category_search);
        listcategory = (ListView) findViewById(R.id.listCategory);
        addcategory = (ImageButton) findViewById(R.id.add_category);
        txtcattype = (TextView) findViewById(R.id.txtcategorytype);

      //  txtcattype.setText("FlowRate");

        txtcattype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] countrys = categorytypelist.toArray(new String[categorytypelist
                        .size()]);
                alertUserType(countrys, txtcattype);
            }
        });

        if (!Utility.isInternetConnected(CategorylistActivity.this)) {

            Toast.makeText(CategorylistActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {
            mArrayList=new ArrayList<>();
            new FetchCategoryList().execute();
            new FetchCustomerType().execute();

        }


        listcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                obj = (CategoryListClass) parent.getItemAtPosition(position);
                catId = obj.getId();
                value = obj.getCategoryValue();
                //Toast.makeText(getApplicationContext(),"id = "+catId,Toast.LENGTH_LONG).show();
                showAlert();

            }
        });


        addcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CategorylistActivity.this, AddMiscellaneousActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void showAlert(){
        final Dialog dialog = new Dialog(CategorylistActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_updatecategory);
        dialog.setCancelable(false);

        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.inputcatvalue);

        TextView positive = (TextView) dialog.findViewById(R.id.btnUpdate);
        TextView negative = (TextView) dialog.findViewById(R.id.btnCancel);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtctype);
        txtTitle.setText(txtcattype.getText().toString());
        text.setText(value);
        // if button is clicked, close the custom dialog
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (text.getText().toString().isEmpty()) {

                    Toast.makeText(CategorylistActivity.this, "Please enter value", Toast.LENGTH_SHORT).show();

                }else if(text.getText().toString().startsWith(" ")){

                    Toast.makeText(CategorylistActivity.this, "Value cannot start with space", Toast.LENGTH_SHORT).show();
                }
                else {
                    strInputValue = text.getText().toString();
                    dialog.dismiss();
                    UpdateCategory task=new UpdateCategory();
                    task.execute();
                }

            }
        });


        dialog.show();
    }

    private void alertUserType(final String[] visitType, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                CategorylistActivity.this);
        // Source of the data in the DIalog

        // Set the dialog title
        builder.setTitle("Select Category")
                .setItems(visitType,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                for (int i = 0; i < categorytypelist.size(); i++) {
                                    if (categorytypelist.get(i).toString()
                                            .equals(visitType[which])) {
                                        //tempusertype = categorytypelistID.get(i);
                                        //serTypeID = String.valueOf(countrylistID.get(i));
                                    }

                                }
                                mArrayList = new ArrayList<>();
                              //  pageindex = 1;

                                FetchCategoryList task = new FetchCategoryList();
                                task.execute();
                                txtcattype.setText(visitType[which]);
                                dialog.dismiss();

                               /* country = visitType[which];
                                new FetchCountry().execute();*/

                            }
                        });

        // Set the action buttons
        builder.show();

    }


    class FetchCustomerType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(CategorylistActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_CATAGORY);

           /* if(categorytypelist.size() > 0){
                categorytypelist.clear();
                categorytypelistID.clear();
            }
*/
            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_CATAGORY);

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
                            + Utility.GET_CATAGORY, mySoapEnvelop);


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
                            categorytypelist.add(soapResult.getPropertySafelyAsString("Category", "")
                                    .toString());

//                            categorytypelistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
//                                    "ID").toString()));

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

                Toast.makeText(CategorylistActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    class FetchCategoryList extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        //private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(CustomerlistActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.CATEGORYLIST);

            mArrayList=new ArrayList<>();
            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category",txtcattype.getText().toString());

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.CATEGORYLIST);

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
                            + Utility.CATEGORYLIST, mySoapEnvelop);


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

            if(result!=null) {
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

                            strValue = soapResult.getPrimitivePropertySafelyAsString("Name")
                                    .toString();
                            id =Integer.parseInt(soapResult.getPrimitivePropertySafelyAsString("ID")
                                    .toString());


                            mArrayList.add(new CategoryListClass(strValue,txtcattype.getText().toString(),id));

                        }
                       // customer_search.setThreshold(1);

                        listcategory.setAdapter(new CategoryListAdapter(getApplicationContext(), mArrayList));


                    } else {
                   /* Toast.makeText(getApplicationContext(),
                            soapObject.getProperty("ErrorMessage").toString(),
                            Toast.LENGTH_LONG).show();*/
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(CategorylistActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class UpdateCategory extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(CategorylistActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.UPDATE_CATEGORY);

            request.addProperty("token", Utility.getAuthToken(CategorylistActivity.this));
            request.addProperty("ID", catId);
            request.addProperty("Name",strInputValue);
            request.addProperty("category",txtcattype.getText().toString());

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.UPDATE_CATEGORY);

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
                            + Utility.UPDATE_CATEGORY, mySoapEnvelop);


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


                        Toast.makeText(CategorylistActivity.this, "Category Updated Successfully", Toast.LENGTH_SHORT).show();
                        FetchCategoryList task=new FetchCategoryList();
                        task.execute();

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

                Toast.makeText(CategorylistActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater menuInflater = getSupportMenuInflater();
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        // return true;
        // MenuItemCompat.getActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_filter:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...

                final View v = getLayoutInflater().inflate(R.layout.activity_categorylist, null);;

                String[] countrys = categorytypelist.toArray(new String[categorytypelist
                        .size()]);
                alertUserType(countrys, v);



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CategorylistActivity.this, AdminDashboardActivity.class);
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
}
