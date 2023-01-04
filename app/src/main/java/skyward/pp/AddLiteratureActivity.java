package skyward.pp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FilenameUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import skyward.pp.util.Utility;

public class AddLiteratureActivity extends AppCompatActivity {

    byte[] bytes, encoded, bytesvideo;
    String filepath, picturePath, filename, extension, filepathv, filenamev, extensionv;
    EditText Literaturename;
    String Productname;
    int ProductID;
    TextView addlit_selectprod, addlit_docname;
    Button submit;
    ImageButton uploadfile;
    String encodedBase64 = null, encodedBase64video;

    private static final int SELECT_PICTURE = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addliterature);
        setTitle("Add Literature");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Literature");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AddLiteratureActivity.this, AdminLiteratureList.class);
                startActivity(i);
                finish();
            }
        });
        Bundle b = getIntent().getExtras();
        ProductID = b.getInt("ID");
        Productname = b.getString("ProductName");
       // Toast.makeText(getApplicationContext(), ProductID + "  " + Productname, Toast.LENGTH_LONG).show();
        addlit_selectprod = (TextView) findViewById(R.id.addlit_selproduct);
        addlit_docname = (TextView) findViewById(R.id.addlit_docname);
        Literaturename = (EditText) findViewById(R.id.addlit_Literaturename);
        submit = (Button) findViewById(R.id.addlit_btn_upload);
        uploadfile = (ImageButton) findViewById(R.id.addlit_btnfile);
        addlit_selectprod.setText(Productname);
        uploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Literaturename.getText().toString().equals("")){
                    Toast.makeText(AddLiteratureActivity.this, "Please enter Literature Name", Toast.LENGTH_SHORT).show();
                }
                else if(Literaturename.getText().toString().startsWith(" ")){
                    Toast.makeText(AddLiteratureActivity.this, "Literature Name cannot start with blank space", Toast.LENGTH_SHORT).show();
                }
                else if(filename == null){
                    Toast.makeText(AddLiteratureActivity.this, "Please upload document", Toast.LENGTH_SHORT).show();
                }else if(extension.equalsIgnoreCase("pdf") || extension.equalsIgnoreCase("doc") || extension.equalsIgnoreCase("docx"))
                {
                    new UploadPhoto().execute();

                } else {
                    Toast.makeText(AddLiteratureActivity.this, "Invalid Document Format", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            System.out.println("*********pdf");
            try {
                Uri fileUri = data.getData();
                String path = fileUri.getPath().toString();
                File f = new File(path);
                if (f.exists()) {
                    // uploadfilenm.setText(f.getName().toString());
                    //txt_file.setVisibility(View.VISIBLE);
                    //txt_file.setText(f.getName().toString());
                    filename = f.getName().toString();
                    addlit_docname.setText(filename);
                    if (filename.contains(" ")) {
                        filename = filename.replace(" ", "");
                    }
                    //extension = filename.substring(filename.lastIndexOf("."));
                    extension = FilenameUtils.getExtension(filename);
                    System.out.println("*****" + extension);
                    System.out.println("\n**** Uri :> " + fileUri.toString());
                    System.out.println("\n**** Path :> " + path.toString());
                    try {
                        FileInputStream fileInputStreamReader = new FileInputStream(f);
                        try {
                            bytes = loadFile(f);
                            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
//                            Toast.makeText(getApplicationContext(),encodedBase64,Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(AddLiteratureActivity.this, "Too much large file to upload!", Toast.LENGTH_SHORT).show();
                        }
                        //  byte[] bytes = new byte[(int)f.length()];
                        // fileInputStreamReader.read(bytes);
                        //encodedBase64 = new String(encoded);
/*
                        new UploadPhoto().execute();
*/

                        // Log.e(encodedBase64,"Encoded string is");
                        //oast.makeText(getApplicationContext(),encodedBase64,Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    path = getPath(data.getData());
                    if (path == null) {
                        //finish();
                        System.out.println("+++++++++++++++++++++filenot exists");
                    } else {
                        File fl = new File(path);
                        if (fl.exists()) {
                            filename = fl.getName().toString();
                            addlit_docname.setText(filename);

                            //extension = filename.substring(filename.lastIndexOf("."));
                            extension = FilenameUtils.getExtension(filename);
                            System.out.println("*****" + extension);

                            try {
                                FileInputStream fileInputStreamReader = new FileInputStream(fl);
                                // byte[] bytes = new byte[(int) originalFile2.length()];
                                //fileInputStreamReader.read(bytes);
                                bytes = loadFile(fl);
                                encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
//                                Toast.makeText(getApplicationContext(),encodedBase64,Toast.LENGTH_LONG).show();
/*
                                new UploadPhoto().execute();
*/
//
                                //  Toast.makeText(getApplicationContext(), encodedBase64, Toast.LENGTH_SHORT).show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        System.out.println("\n**** File Not Exist :> " + path);
                    }
                }

            } catch (Exception e) {
                //  ShowDialog_Ok("Error", "Cannot Open File");
            }


        }


    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void uploadFile() {


        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(intent, "Select File To Upload"), SELECT_PICTURE);

    }

    private static byte[] loadFile(File file) throws IOException {

        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;

    }

    private class UploadPhoto extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddLiteratureActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SharedPreferences settings = getSharedPreferences("info",
                    MODE_PRIVATE);

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.UPLOADLITERATURE);
            request.addProperty("token", Utility.getAuthToken(AddLiteratureActivity.this));
            request.addProperty("literatureName",Literaturename.getText().toString());;
            request.addProperty("f", encodedBase64);
            request.addProperty("fileName", filename);
            request.addProperty("contentType", extension);
            request.addProperty("productID",ProductID);
            request.addProperty("Extension",extension);
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
                            + Utility.UPLOADLITERATURE, mySoapEnvelop);

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





/*
               SoapFault error = (SoapFault)mySoapEnvelop.bodyIn;
                    System.out.println("Error message : "+error.toString());
*/
                result = (SoapObject) mySoapEnvelop.bodyIn;
                System.out.println("exec in try");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }




        @Override
        protected void onPostExecute(SoapObject result) {
            // adTODO Auto-generated method stub
            super.onPostExecute(result);
    if(result!=null) {
    progress.dismiss();
    try {

        SoapObject soapObject = (SoapObject) result.getProperty(0);
        System.out.println(soapObject.getProperty("IsSucceed"));
        if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
        Intent i = new Intent(AddLiteratureActivity.this, AdminLiteratureList.class);
            startActivity(i);
            finish();
            Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
            Literaturename.setText("");
            filename = "";
            encodedBase64 = "";
            addlit_docname.setText("");


        } else {
            Toast.makeText(getBaseContext(),
                    soapObject.getProperty("ErrorMessage").toString(),
                    Toast.LENGTH_LONG).show();
        }

    } catch (NullPointerException e) {
        progress.dismiss();
        Toast.makeText(getApplicationContext(), "Please fill proper Data", Toast.LENGTH_LONG).show();
    } catch (Exception e) {
        progress.dismiss();
        e.printStackTrace();
    }
}else{
    progress.dismiss();
}
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddLiteratureActivity.this, AdminLiteratureList.class);
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
