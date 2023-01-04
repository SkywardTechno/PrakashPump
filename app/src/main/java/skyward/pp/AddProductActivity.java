package skyward.pp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;

import skyward.pp.util.Utility;

public class AddProductActivity extends AppCompatActivity {

    String filepath="", picturePath="", filename="",extension="",filepathv="",filepathvideo="",filenamev="",filenamevideo="",extensionv="";
    String Name,MobileNo,Place, FarmNo,Area,Verificationcode,UserType,ID,imagepath="", urlforimage="";
    String encodedBase64 = null, encodedBase64video;
    private static final int SELECT_PICTURE = 1;
    int loader = R.drawable.loader;
    private Bitmap bitmap;
    Boolean keepChangingText = false;
    String imgDecodableString="", converted="";
    byte[] bytes, encoded,bytesvideo;
    Bitmap bmThumbnail;
    EditText priority,producttype,power,type,txt_description,txt_productname,txt_modelno,txt_price,txt_function,txt_application,txt_specification;
    TextView flowrate,head, txt_upload;
    EditText txt_shipingchgarges,edt_gaurantee,edt_warrantee;
    Button add,btn_uploadphoto,btn_uploadvideo;
    int flowrateID=0,producttypeID=0,typeID=0,inletID=0,outletID=0,voltID=0;
    Double headID=0.0,powerID=0.0,powerhpID=0.0,headfeetID=0.0;
    int productcategoryid=0;
    int productsubcategoryid=0;
    ImageView showimg,showvideo;
    Spinner spin_flowwrate,spin_productype,spin_type,spin_outlet,spin_inlet,spin_volt;
    EditText spin_head,spin_headfeet,spin_power,spin_powerhp;
    private ArrayList<String> powerlist = new ArrayList<String>();
    private ArrayList<Integer>  powerlistid = new ArrayList<Integer>();
    private ArrayList<String> headlist = new ArrayList<String>();
    private ArrayList<Integer>  headlistid = new ArrayList<Integer>();
    private ArrayList<String> flowratelist = new ArrayList<String>();
    private ArrayList<Integer>  flowratelistid = new ArrayList<Integer>();
    private ArrayList<String> producttypelist = new ArrayList<String>();
    private ArrayList<Integer>  producttypelistid = new ArrayList<Integer>();
    private ArrayList<String> typelist = new ArrayList<String>();
    private ArrayList<Integer>  typelistid = new ArrayList<Integer>();
    private ArrayList<String> inletlist = new ArrayList<String>();
    private ArrayList<Integer>  inletlistid = new ArrayList<Integer>();
    private ArrayList<String> outletlist = new ArrayList<String>();
    private ArrayList<Integer>  outletlistid = new ArrayList<Integer>();
    private ArrayList<String> headfeetlist = new ArrayList<String>();
    private ArrayList<Integer>  headfeetlistid = new ArrayList<Integer>();
    private ArrayList<String> powerhplist = new ArrayList<String>();
    private ArrayList<Integer>  powerhplistid = new ArrayList<Integer>();
    private ArrayList<String> voltlist = new ArrayList<String>();
    private ArrayList<Integer>  voltlistid = new ArrayList<Integer>();


    private static final int SELECT_VIDEO = 1;
    public static final int MY_PERMISSIONS_ALL=1;
    public static final int MY_PERMISSIONS_IMAGE=2;
    public static final int MY_PERMISSIONS_VIDEO=3;
    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE=3;
    int uploadid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        setTitle("Add Product");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Product");
        toolbar.setTitleTextColor(Color.WHITE);

        spin_power= (EditText) findViewById(R.id.spin_add_power);
        spin_head= (EditText) findViewById(R.id.spin_add_head);
        spin_flowwrate= (Spinner) findViewById(R.id.spin_add_flowrate);
        spin_productype= (Spinner) findViewById(R.id.spin_add_producttype);
        spin_type= (Spinner) findViewById(R.id.spin_add_type);
        spin_inlet = (Spinner) findViewById(R.id.spin_add_inlet);
        spin_outlet = (Spinner) findViewById(R.id.spin_add_outlet);
        spin_headfeet = (EditText) findViewById(R.id.spin_add_headfeet);
        spin_powerhp = (EditText) findViewById(R.id.spin_add_powerhp);
        spin_volt = (Spinner) findViewById(R.id.spin_add_volt);

        btn_uploadphoto= (Button) findViewById(R.id.btn_add_txt_upload);
        btn_uploadvideo= (Button) findViewById(R.id.btn_add_videoupload);

        //txt_qauntity= (EditText) findViewById(R.id.edt_quantity);


        getSupportActionBar().getCustomView();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AddProductActivity.this, ProductsmasterActivity.class);
                startActivity(i);
                finish();
            }
        });

        txt_productname= (EditText) findViewById(R.id.ap_name);
       // txt_productcode= (EditText) findViewById(R.id.edt_productCode);
        txt_modelno= (EditText) findViewById(R.id.edt_modelNo);
        txt_price= (EditText) findViewById(R.id.edt_price);
        txt_function= (EditText) findViewById(R.id.add_funNFeature);
        txt_application= (EditText) findViewById(R.id.add_application);
        txt_specification= (EditText) findViewById(R.id.add_specification);
        edt_gaurantee= (EditText) findViewById(R.id.edt_gaurantee);
        edt_warrantee= (EditText) findViewById(R.id.edt_warranty);
        priority= (EditText) findViewById(R.id.edt_priotity);

        add = (Button) findViewById(R.id.add_product);
        showimg = (ImageView) findViewById(R.id.add_showimg);
        showvideo = (ImageView) findViewById(R.id.add_showvideo);
       /* spin_power.setText("0.00");
        spin_powerhp.setText("0.00");
        spin_head.setText("0.00");
        spin_headfeet.setText("0.00");*/

        txt_description = (EditText) findViewById(R.id.add_description);

        if (ContextCompat.checkSelfPermission(AddProductActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(AddProductActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  ) {
            ActivityCompat.requestPermissions(AddProductActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_ALL);
        }

        if (!Utility.isInternetConnected(AddProductActivity.this)) {

            Toast.makeText(AddProductActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {

            new FetchFlowRate().execute();

            new FetchProductType().execute();
            new FetchType().execute();
            new FetchInlet().execute();
            new FetchOutlet().execute();

            new FetchVolt().execute();
/*
            new FetchProductCategory().execute();
*/
        }

        add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if (spin_powerhp.getText().toString().isEmpty()) {
            spin_powerhp.setText("0.00");
        }

        if (spin_power.getText().toString().isEmpty()) {
            spin_power.setText("0.00");
        }

        if (spin_head.getText().toString().isEmpty()) {
            spin_head.setText("0.00");
        }

        if (spin_headfeet.getText().toString().isEmpty()) {
            spin_headfeet.setText("0.00");
        }
        if (!Utility.isInternetConnected(AddProductActivity.this)) {

            Toast.makeText(AddProductActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {

            if(txt_productname.getText().toString().isEmpty()){

                Toast.makeText(AddProductActivity.this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();

            }else if(txt_productname.getText().toString().startsWith(" ")){

                Toast.makeText(AddProductActivity.this, "Product Name cannot start with blank space", Toast.LENGTH_SHORT).show();

            }
            else if(producttypeID==0)
            {
                Toast.makeText(AddProductActivity.this, "Please Select Product Type", Toast.LENGTH_SHORT).show();
            }
            else if (spin_powerhp.getText().toString().isEmpty() ||  spin_powerhp.getText().toString().equalsIgnoreCase("0.00")) {
                Toast.makeText(AddProductActivity.this, "Please Enter PowerHP", Toast.LENGTH_SHORT).show();
            } else if (spin_power.getText().toString().isEmpty() ||  spin_power.getText().toString().equalsIgnoreCase("0.00")) {
                Toast.makeText(AddProductActivity.this, "Please Enter Power", Toast.LENGTH_SHORT).show();
            } else if (spin_head.getText().toString().isEmpty() || spin_head.getText().toString().equals("0.00")) {
                Toast.makeText(AddProductActivity.this, "Please Enter Head", Toast.LENGTH_SHORT).show();
            } else if (spin_headfeet.getText().toString().isEmpty() || spin_headfeet.getText().toString().equals("0.00")) {
                Toast.makeText(AddProductActivity.this, "Please Enter Head Feet", Toast.LENGTH_SHORT).show();
            } else if(flowrateID==0)
            {
                Toast.makeText(AddProductActivity.this, "Please Select Flowrate", Toast.LENGTH_SHORT).show();
            }
            /*else if(txt_productcode.getText().toString().isEmpty())
            {
                Toast.makeText(AddProductActivity.this, "Please Enter Product Code", Toast.LENGTH_SHORT).show();
            }*/
            else if(txt_modelno.getText().toString().isEmpty())
            {
                Toast.makeText(AddProductActivity.this, "Please Enter Model No", Toast.LENGTH_SHORT).show();
            }
            else if(txt_price.getText().toString().isEmpty())
            {
                Toast.makeText(AddProductActivity.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
            }
            else if(validateprice(txt_price.getText().toString())==false)
            {
                Toast.makeText(AddProductActivity.this, "Please Enter Valid Price", Toast.LENGTH_SHORT).show();
            }else if (priority.getText().toString().isEmpty()) {

                Toast.makeText(AddProductActivity.this, "Please Enter Priority", Toast.LENGTH_SHORT).show();

            }
           /* else if(txt_qauntity.getText().toString().isEmpty())
            {
                Toast.makeText(AddProductActivity.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
            }
            else if(validatequantity(txt_qauntity.getText().toString())==false)
            {
                Toast.makeText(AddProductActivity.this, "Please Enter Valid Quantity", Toast.LENGTH_SHORT).show();
            }*/
            else if(filepath.isEmpty()){

                Toast.makeText(AddProductActivity.this, "Please Upload Product Image", Toast.LENGTH_SHORT).show();

            }else if(txt_description.getText().toString().isEmpty()){
                Toast.makeText(AddProductActivity.this, "Please Enter Product Description", Toast.LENGTH_SHORT).show();

            }

          else {
                new InsertProductTask().execute();
            }

        }
    }
});
    btn_uploadvideo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (ContextCompat.checkSelfPermission(AddProductActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(AddProductActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED  ) {
                ActivityCompat.requestPermissions(AddProductActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS},
                        MY_PERMISSIONS_VIDEO);
            }else{
                uploadid = 1;
                uploadVideoFile();
            }


        }
    });


        btn_uploadphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(AddProductActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(AddProductActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED  ) {
                    ActivityCompat.requestPermissions(AddProductActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS},
                            MY_PERMISSIONS_IMAGE);
                }else{
                    uploadid =2;
                    uploadFile();
                }


            }
        });


        spin_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                typeID = typelistid.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_productype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                producttypeID = producttypelistid.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    spin_flowwrate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            flowrateID = flowratelistid.get(position);
        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });

        spin_inlet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                inletID = inletlistid.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_outlet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                outletID=outletlistid.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_volt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                voltID=voltlistid.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_power.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (spin_power.getText().toString().isEmpty() || spin_power.getText().toString().equalsIgnoreCase("0.0")) {
                    if (keepChangingText) {
                        keepChangingText = false;

                        spin_powerhp.setText("0.00");
                    } else {
                        keepChangingText = true;
                    }
                } else {
                    if (keepChangingText) {
                        keepChangingText = false;
                        Double powerhp = Double.valueOf(spin_power.getText().toString()) * 1.34;
                        spin_powerhp.setText(String.format("%.2f", powerhp));
                    } else {
                        keepChangingText = true;
                    }

                }

            }
        });



        spin_powerhp.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (spin_powerhp.getText().toString().isEmpty() || spin_powerhp.getText().toString().equalsIgnoreCase("0.0")) {
                    if (keepChangingText) {
                        keepChangingText = false;

                        spin_power.setText("0.00");
                    } else {
                        keepChangingText = true;
                    }
                } else {
                    if (keepChangingText) {
                        keepChangingText = false;
                        Double power = Double.valueOf(spin_powerhp.getText().toString()) * 0.75;
                        spin_power.setText(String.format("%.2f", power));
                    } else {
                        keepChangingText = true;
                    }

                }
            }
        });


        spin_head.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (spin_head.getText().toString().isEmpty() || spin_head.getText().toString().equalsIgnoreCase("0.0")) {
                    if (keepChangingText) {
                        keepChangingText = false;

                        spin_headfeet.setText("0.00");
                    } else {
                        keepChangingText = true;
                    }
                } else {
                    if (keepChangingText) {
                        keepChangingText = false;
                        Double headfeet = Double.valueOf(spin_head.getText().toString()) * 3.28;
                        spin_headfeet.setText(String.format("%.2f", headfeet));
                    } else {
                        keepChangingText = true;
                    }

                }

            }
        });

        spin_headfeet.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (spin_headfeet.getText().toString().isEmpty() || spin_headfeet.getText().toString().equalsIgnoreCase("0.0")) {
                    if (keepChangingText) {
                        keepChangingText = false;

                        spin_head.setText("0.00");
                    } else {
                        keepChangingText = true;
                    }
                } else {
                    if (keepChangingText) {
                        keepChangingText = false;
                        Double head = Double.valueOf(spin_headfeet.getText().toString()) * 0.30;
                        spin_head.setText(String.format("%.2f", head));
                    } else {
                        keepChangingText = true;
                    }

                }

            }
        });




    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            // If request is cancelled, the result arrays are empty.
            case MY_PERMISSIONS_ALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(AddProductActivity.this, "Please grant the permissions!", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_IMAGE:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    uploadid = 2;
                    uploadFile();
                }else{
                    Toast.makeText(AddProductActivity.this, "Please grant the permissions!", Toast.LENGTH_SHORT).show();

                }
                return;
            }
            case MY_PERMISSIONS_VIDEO:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    uploadid = 1;
                    uploadVideoFile();
                }else{
                    Toast.makeText(AddProductActivity.this, "Please grant the permissions!", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
        // other 'case' lines to check for other
        // permissions this app might request

    }


    /////
    private void uploadFile() {

        uploadid =2;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PICTURE);

    }


    public boolean validateprice(String input)
    {
        boolean ismatches=false;
     /*   String pattern ="([0-9]{4})(\.)([0-2]{2})"; // 4 digits followe by . followed by 2 digits
     */
        /*String pattern="^\\d{0,8}(\\.\\d{1,4})?$\n";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);
        if(m.matches())
        {
            ismatches=true;
            System.out.println("Validated");
        }
        else
        {
            ismatches=false;
            System.out.println("Not Validated");
        }

        return ismatches;*/

        String text =input;
        try {
            double num = Double.parseDouble(text);
            ismatches=true;
        } catch (NumberFormatException e) {
            ismatches=false;
        }
        return ismatches;

    }


    public boolean validatequantity(String input)
    {
        boolean ismatches=false;

        String text =input;
        try {
            int num = Integer.parseInt(text);
            ismatches=true;
        } catch (NumberFormatException e) {
            ismatches=false;
        }

        return ismatches;
    }

    private void uploadVideoFile() {

        uploadid =1;
        Intent intentv = new Intent();
        intentv.setType("video/*");
        intentv.setAction(Intent.ACTION_PICK);
        intentv.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(intentv, "Select Video"), SELECT_VIDEO);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && uploadid == 2) {
            System.out.println("*********Image");
            try {

                Uri fileUri = data.getData();
                String path = fileUri.getPath().toString();

                File f = new File(path);
                if (f.exists()) {

                    // uploadfilenm.setText(f.getName().toString());
                    //txt_file.setVisibility(View.VISIBLE);
                    //txt_file.setText(f.getName().toString());
                    filename = f.getName().toString();
                    if (filename.contains(" ")) {
                        filename = filename.replace(" ", "");
                    }
                    extension = filename.substring(filename.lastIndexOf("."));
                    System.out.println("*****" + extension);
                    System.out.println("\n**** Uri :> " + fileUri.toString());
                    System.out.println("\n**** Path :> " + path.toString());


                    try {
                        FileInputStream fileInputStreamReader = new FileInputStream(f);
                        try {
                            bytes = loadFile(f);
                            encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(AddProductActivity.this, "Too much large file to upload!", Toast.LENGTH_SHORT).show();
                        }
                        //  byte[] bytes = new byte[(int)f.length()];
                        // fileInputStreamReader.read(bytes);
                        //encodedBase64 = new String(encoded);
                        new UploadPhoto().execute();

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
                        finish();
                        System.out.println("+++++++++++++++++++++filenot exists");
                    } else {
                        File fl = new File(path);
                        if (fl.exists()) {
                            filename = fl.getName().toString();
                            extension = filename.substring(filename.lastIndexOf("."));
                            System.out.println("*****" + extension);

                            try {
                                FileInputStream fileInputStreamReader = new FileInputStream(fl);
                                // byte[] bytes = new byte[(int) originalFile2.length()];
                                //fileInputStreamReader.read(bytes);
                                bytes = loadFile(fl);
                                encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
                                String a = "";
                                new UploadPhoto().execute();
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
        }else if(requestCode == SELECT_VIDEO && resultCode == RESULT_OK && uploadid == 1) {

            System.out.println("*********VIDEO");
            Uri fileUri = data.getData();
            String pathvideo = fileUri.getPath().toString();

               /* if(path == null) {
                    System.out.println("********************Path not found");
                    Toast.makeText(AddProductActivity.this, "FilePath Not Found", Toast.LENGTH_SHORT).show();
                    finish();

                } else {*/

            //Toast.makeText(getApplicationContext(),"paths is: "+filepath,Toast.LENGTH_LONG).show();
            File fv = new File(pathvideo);
            if(fv.exists())
            {
                filenamev = fv.getName().toString();
                extensionv = filenamev.substring(filenamev.lastIndexOf("."));
                System.out.println("*****" + extensionv);

                bmThumbnail = ThumbnailUtils.createVideoThumbnail(pathvideo, MediaStore.Video.Thumbnails.MICRO_KIND);


                /////////////////latest code////////////////////
                File originalFile = new File(pathvideo);
                long fileSizeInBytes = originalFile.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;
                if(fileSizeInMB>10)
                {
                    Toast.makeText(getApplicationContext(),"You cannot upload more than 10 MB video", Toast.LENGTH_LONG).show();

                }
                else
                {

                    try {
                        FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
                        byte[] bytes = new byte[(int)originalFile.length()];
                        //  byte[] bytes = new byte[30000];
                        fileInputStreamReader.read(bytes);
                        //     byte []encode= Base64.encodeToString(bytes,Base64.DEFAULT);
                        encodedBase64video = Base64.encodeToString(bytes,Base64.DEFAULT);
                        String a="";
                        new UploadVideo().execute();

                        //btn_upload.setText(encodedBase64);
                        // Log.e(encodedBase64,"Encoded string is   ");
//
                        //  Toast.makeText(getApplicationContext(), encodedBase64, Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }else{

                filepathv= getPath(data.getData());
                if(filepathv == null) {
                    finish();
                    System.out.println("+++++++++++++++++++++filenot exists");
                } else {

                    File fl = new File(filepathv);
                    if (fl.exists()) {
                        filenamev = fl.getName().toString();
                        extensionv = filenamev.substring(filenamev.lastIndexOf("."));
                        System.out.println("*****" + extensionv);

                        bmThumbnail = ThumbnailUtils.createVideoThumbnail(filepathv, MediaStore.Video.Thumbnails.MICRO_KIND);

                        /////////////////latest code////////////////////
                        File originalFile2 = new File(filepathv);
                        long fileSizeInBytes2 = originalFile2.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                        long fileSizeInKB2 = fileSizeInBytes2 / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                        long fileSizeInMB2 = fileSizeInKB2 / 1024;
                        if (fileSizeInMB2 > 10) {
                            Toast.makeText(getApplicationContext(), "You cannot upload more than 10 MB video", Toast.LENGTH_LONG).show();

                        } else {

                            try {
                                FileInputStream fileInputStreamReader = new FileInputStream(originalFile2);
                                byte[] bytes = new byte[(int) originalFile2.length()];
                                //  byte[] bytes = new byte[30000];
                                fileInputStreamReader.read(bytes);
                                //     byte []encode= Base64.encodeToString(bytes,Base64.DEFAULT);
                                encodedBase64video = Base64.encodeToString(bytes, Base64.DEFAULT);
                                String a = "";
                                new UploadVideo().execute();
//
                                //  Toast.makeText(getApplicationContext(), encodedBase64, Toast.LENGTH_SHORT).show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }


            }

        }else{
            System.out.println("Nothing");
        }

    }

    ////////

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddProductActivity.this, ProductsmasterActivity.class);
        startActivity(i);
        finish();
    }

    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }




    ///////////////////////get Flowrate task////////////////////////////////

    class FetchFlowRate extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddProductActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","FlowRate");
            if(flowratelist.size() > 0){
                flowratelist.clear();
                flowratelistid.clear();
            }

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
                        flowratelist.add("Select Flow Rate");
                        flowratelistid.add(0);
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            flowratelist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());

                            flowratelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_leadsrc, flowratelist);
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                        spin_flowwrate.setAdapter(spinnerAdapter);
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

                Toast.makeText(AddProductActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////



    ////////////////////////////////get Head Taask/////////////////////////


    /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////get productype task/////////////////////////////////////////

    class FetchProductType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddProductActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTTYPE);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
           // request.addProperty("category","ProductType");
            if(producttypelist.size() > 0){
                producttypelist.clear();
                producttypelistid.clear();
            }

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
                        producttypelist.add("Select Product Type");
                        producttypelistid.add(0);
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            producttypelist.add(soapResult.getPropertySafelyAsString("ProductTypeName", "")
                                    .toString());

                            producttypelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_leadsrc, producttypelist);
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                        spin_productype.setAdapter(spinnerAdapter);


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

                Toast.makeText(AddProductActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////fetch Type TAsk///////////////////////


    class FetchType extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddProductActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Type");
            if(producttypelist.size() > 0){
                producttypelist.clear();
                producttypelistid.clear();
            }

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
                        typelist.add("Select Type");
                        typelistid.add(0);
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            typelist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());

                            typelistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_leadsrc, typelist);
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                        spin_type.setAdapter(spinnerAdapter);


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

                Toast.makeText(AddProductActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class FetchVolt extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Volt");
            if(voltlistid.size() > 0){
                voltlistid.clear();
                voltlist.clear();
            }

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
                    voltlist.add("Select Volt");
                    voltlistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        voltlist.add(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());

                        voltlistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.spinner_leadsrc, voltlist);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                    spin_volt.setAdapter(spinnerAdapter);


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

        }
    }

    class FetchInlet extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Inlet");
            if(inletlistid.size() > 0){
                inletlistid.clear();
                inletlist.clear();
            }

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
                    inletlist.add("Select Inlet");
                    inletlistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        inletlist.add(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());

                        inletlistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.spinner_leadsrc, inletlist);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                    spin_inlet.setAdapter(spinnerAdapter);


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

        }
    }

    class FetchOutlet extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
          /*  progress = new ProgressDialog(PumpselectionActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();*/
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_MISCDATAFROMCATEGORY);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("category","Outlet");
            if(outletlist.size() > 0){
                outletlist.clear();
                outletlistid.clear();
            }

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
                    outletlist.add("Select Outlet");
                    outletlistid.add(0);
                    for (int i = 0; i < count; i++) {
                        SoapObject soapResult = null;
                        soapResult = (SoapObject) result4.getProperty(i);
                        outletlist.add(soapResult.getPropertySafelyAsString("Name", "")
                                .toString());

                        outletlistid.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                "ID").toString()));



                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.spinner_leadsrc, outletlist);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_leadsrc_item);
                    spin_outlet.setAdapter(spinnerAdapter);


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

        }
    }






    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class UploadPhoto extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddProductActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SharedPreferences settings = getSharedPreferences("info",
                    MODE_PRIVATE);

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.UPLOAD_PRODUCTIMAGE);

            request.addProperty("token", Utility.getAuthToken(AddProductActivity.this));
            request.addProperty("f", encodedBase64);
            request.addProperty("fileName", filename);
            request.addProperty("contentType", extension);

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
                            + Utility.UPLOAD_PRODUCTIMAGE, mySoapEnvelop);

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
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            progress.dismiss();
            if(result != null) {
                try {

                    SoapObject soapObject = (SoapObject) result.getProperty(0);

                    System.out.println(soapObject.getProperty("IsSucceed"));
                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

                        filename = soapObject.getPrimitivePropertySafelyAsString("FileName");
                        filepath = soapObject.getPrimitivePropertySafelyAsString("FilePath");

                        //filename = soapObject.getFileName();
//                        filepath = resp.getFilePath();
//                        filecontenttype = resp.getFileContentType();
                        Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
                        String newimg;
                        if (filepath != null) {
                            try {

                                newimg = filepath.replace("\\", "/");
                                //imagepath = imagepath.replaceAll(" ", "%20");

                                urlforimage = Utility.URL + newimg;

                                ImageLoader imgLoader = new ImageLoader(getApplicationContext());
                                imgLoader.DisplayImage(Utility.URLFORIMAGE + newimg, loader, showimg);
                                System.out.println("Image path is : " + urlforimage);


                            } catch (NullPointerException e) {

                            }

                        }


                    } else {
                        Toast.makeText(getBaseContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }

                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Please fill proper Data", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {

                Toast.makeText(AddProductActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /////////////////////////////////////////////////////////Insert product task//////////////////////


    class InsertProductTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

      @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddProductActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.INSERT_PRODUCT);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("productName",txt_productname.getText().toString());
            request.addProperty("productTypeID",producttypeID);
            request.addProperty("inletID",inletID);
            request.addProperty("outletID",outletID);
            request.addProperty("powerHPID",Double.valueOf(spin_powerhp.getText().toString())+"");
            request.addProperty("powerID",Double.valueOf(spin_power.getText().toString())+"");
            request.addProperty("typeID",typeID);
            request.addProperty("headFeetID",Double.valueOf(spin_headfeet.getText().toString())+"");
            request.addProperty("headID",Double.valueOf(spin_head.getText().toString())+"");
            request.addProperty("flowRateID",flowrateID);
            request.addProperty("productDesc",txt_description.getText().toString());
            request.addProperty("imageName",filename);
            request.addProperty("imagePath",filepath);
            request.addProperty("videoName",filenamevideo);
            request.addProperty("videoPath",filepathvideo);
            request.addProperty("Quantity",0);
            request.addProperty("FunctionAndFeatures",txt_function.getText().toString());
            request.addProperty("Applications",txt_application.getText().toString());
            request.addProperty("Specifications",txt_specification.getText().toString());
            request.addProperty("ProductCategoryID",productcategoryid);
            request.addProperty("ProductSubCategoryID",productsubcategoryid);
            request.addProperty("ModelNum",txt_modelno.getText().toString());
            request.addProperty("Price",Double.valueOf(txt_price.getText().toString())+"");
            request.addProperty("ShippingCharges",Double.valueOf("0.0")+"");
            request.addProperty("ImageContentType",extension);
            request.addProperty("VideoContentType",extensionv);
            request.addProperty("voltID",voltID);
            if(edt_gaurantee.getText().toString().isEmpty()){
                request.addProperty("Guarantee",0);

            }else{
                request.addProperty("Guarantee",Integer.valueOf(edt_gaurantee.getText().toString()));

            }

            if(edt_warrantee.getText().toString().isEmpty()){
                request.addProperty("Warranty",0);

            }else{
                request.addProperty("Warranty",Integer.valueOf(edt_warrantee.getText().toString()));

            }
            request.addProperty("Priority",priority.getText().toString());

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.INSERT_PRODUCT);

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
                            + Utility.INSERT_PRODUCT, mySoapEnvelop);


                } catch (XmlPullParserException e) {
                     System.out.println("-------------------------------------------------" +
                             "-------------------------------------------------------" +
                             "--------------------------------------------------------" +
                             "-----------------------------------------------------------------------------"+
                     "----------------------------------------------------------------");
                    e.printStackTrace();
                    // System.out.println("XmlPullParserException 0");
                } catch (SocketTimeoutException e) {
                    System.out.println("-------------------------------------------------" +
                            "-------------------------------------------------------" +
                            "--------------------------------------------------------" +
                            "-----------------------------------------------------------------------------"+
                            "----------------------------------------------------------------");
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    // System.out.println("SocketTimeoutException 1");
                } catch (SocketException e) {
                    System.out.println("-------------------------------------------------" +
                            "-------------------------------------------------------" +
                            "--------------------------------------------------------" +
                            "-----------------------------------------------------------------------------"+
                            "----------------------------------------------------------------");
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    // System.out.println("SocketException  2");
                } catch (IOException e) {
                    System.out.println("-------------------------------------------------" +
                            "-------------------------------------------------------" +
                            "--------------------------------------------------------" +
                            "-----------------------------------------------------------------------------"+
                            "----------------------------------------------------------------");
                    // System.out.println(e.getClass());
                    e.printStackTrace();
                    System.out.println("IO Exception 3");
                    // return objLoginBean;
                }

                result = (SoapObject) mySoapEnvelop.bodyIn;
                System.out.println("exec in try");
            } catch (Exception e) {
                System.out.println("-------------------------------------------------" +
                        "-------------------------------------------------------" +
                        "--------------------------------------------------------" +
                        "-----------------------------------------------------------------------------"+
                        "----------------------------------------------------------------");
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

                        Toast.makeText(getApplicationContext(), "Product Added Successfully",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(AddProductActivity.this, ProductsmasterActivity.class);
                        startActivity(intent);
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
                Toast.makeText(AddProductActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private class UploadVideo extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddProductActivity.this);
            progress.setMessage("Uploading Video \nPlease wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SharedPreferences settings = getSharedPreferences("info",
                    MODE_PRIVATE);

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.UPLOAD_PRODUCTVIDEO);

            request.addProperty("Token", Utility.getAuthToken(getApplicationContext()));
            request.addProperty("f", encodedBase64video);
            request.addProperty("fileName", filenamev);

            SoapSerializationEnvelope mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            mySoapEnvelop.setOutputSoapObject(request);
            HttpTransportSE myAndroidHttpTransport = null;

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);

                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.UPLOAD_PRODUCTVIDEO, mySoapEnvelop);

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
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);

        }



        @Override
        protected void onPostExecute(SoapObject result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if(progress.isShowing())
            {
                progress.dismiss();

            }
            if(result != null) {
                try {

                    SoapObject soapObject = (SoapObject) result.getProperty(0);

                    System.out.println(soapObject.getProperty("IsSucceed"));
                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

                        showvideo.setImageBitmap(bmThumbnail);
                        filenamevideo = soapObject.getPropertyAsString("FileName");
                        filepathvideo = soapObject.getPropertyAsString("FilePath");
                        // Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
                        //   uploadfilenm.setText("");

                        //new insertVideoTask().execute();

                    } else {

                        Toast.makeText(getBaseContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }

                }
                catch (NullPointerException e) {
                    progress.dismiss();

                    Toast.makeText(getApplicationContext(), "Please fill proper Data", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    progress.dismiss();

                    e.printStackTrace();
                }
            }else {

                Toast.makeText(AddProductActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
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








    /////////////////////////////////////////////////

}
