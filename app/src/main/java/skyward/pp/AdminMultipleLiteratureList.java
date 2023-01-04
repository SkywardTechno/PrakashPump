package skyward.pp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import skyward.pp.adapter.AdminMultipleListAdapter;
import skyward.pp.model.AdminMultipleListClass;
import skyward.pp.util.Utility;

public class AdminMultipleLiteratureList extends AppCompatActivity {

    
    ListView listmultiple;
    int lit_id;
    ArrayList<AdminMultipleListClass> ProductList;
    String LiteratureName,FileName,FilePath;
    AdminMultipleListClass product;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_multiple_literaturelist);


        setTitle("Literature List");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Literature List");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminMultipleLiteratureList.this, AdminLiteratureList.class);
                startActivity(i);
                finish();
            }
        });
        
        listmultiple = (ListView) findViewById(R.id.listliteraturebyproduct);
        lit_id = Integer.parseInt(getIntent().getStringExtra("LID"));
        
        new FetchLiteratureName().execute();

        listmultiple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AdminMultipleListClass obj=ProductList.get(position);
                Intent i =new Intent(AdminMultipleLiteratureList.this, ViewLiteratureActivity.class);
                i.putExtra("FilePath",obj.getLiterature_path());
                i.putExtra("FileName",obj.getLiterature_name());
                i.putExtra("LiteratureName",obj.getLiterature_name());
                i.putExtra("fromadmin","fromadmin");
                i.putExtra("Value", String.valueOf(lit_id));
                startActivity(i);
                finish();

            }
        });
        
        
    }

    class FetchLiteratureName extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AdminMultipleLiteratureList.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_LITERATUREBYPRODUCTID);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("productID",lit_id);



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
                            + Utility.GET_LITERATUREBYPRODUCTID, mySoapEnvelop);


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
                        LiteratureName=soapResult.getPrimitivePropertySafelyAsString("LiteratureName").toString();
                        FileName=soapResult.getPrimitivePropertySafelyAsString("FileName").toString();
                        FilePath=soapResult.getPrimitivePropertySafelyAsString("FilePath").toString();
                       // LiteratureID=soapResult.getPrimitivePropertySafelyAsString("literatureID").toString();


                        //                        producttypelist.add(soapResult.getPropertySafelyAsString("Name", "")
//                                .toString());
//
//                        producttypelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
//                                "ID").toString()));
                        product= new AdminMultipleListClass(LiteratureName,FileName,FilePath);
                        ProductList.add(product);
                    }




                    listmultiple.setAdapter(new AdminMultipleListAdapter(getApplicationContext(),ProductList));

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminMultipleLiteratureList.this, AdminLiteratureList.class);
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
