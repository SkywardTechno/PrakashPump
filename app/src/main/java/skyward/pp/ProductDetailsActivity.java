package skyward.pp;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;

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
import java.util.HashMap;

import skyward.pp.util.Utility;

import static skyward.pp.R.id.edt_modelNo;

public class ProductDetailsActivity extends AppCompatActivity {

    Dialog pop_confimation;
    TextView txtpop_msg;
    Button btnpop_cancel;
    Button getBtnpop_submit;
    int ProductID;
    String filepath, picturePath, filename,extension,filepathv,filenamev,extensionv;
    Double price,shippingcharge;
    int availableqty=0;
    String applications,functionandfeature,specification;
    private static final int SELECT_VIDEO = 1;
    byte[] bytes, encoded,bytesvideo;
    Spinner spin_flowrate,spin_producttype,spin_type,spin_outlet,spin_inlet,spin_volt;
    EditText spin_head,spin_headfeet,spin_power,spin_powerhp;
    TextView  txt_upload;
    EditText priority,txt_description,txt_productname;
    Button edit,delete,add,uploadphoto,uploadvideo;
    ImageView product_img,product_video;
    String namevideo,nameimg;
    String encodedBase64 = null, encodedBase64video;
    private static final int SELECT_PICTURE = 1;
    private Bitmap bitmap;
    String productcode,modelno;
    String imgDecodableString, converted;
    int flowrateID,producttypeID,typeID,inletID,outletID,voltID=0;
    Double headID=0.0,powerID=0.0,powerhpID=0.0,headfeetID=0.0;
    int Guarantee=0;
    int Warranty=0;
    Bitmap bmThumbnail;
    String productname,model_no,productype,power,productdescriptiion,type,head,flowrate,volt,imagepath="",finalpathimg,videopath="",finalpathvideo,inlet,outlet,powerhp,headfeet;
    int uploadid;
    int send_producttype,send_type,send_flowrate,send_inlet,send_outlet,send_volt;
    Double send_head,send_headfeet,send_power,send_powerhp;
    EditText edt_gaurantee,edt_warranty,txt_price,txt_specification,txt_function,txt_application,txt_productcode,txt_modelno;
    int subcategoryid=0,categoryid=0;
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
    Boolean keepChangingText = false;

    public static final int MY_PERMISSIONS_ALL=1;
    public static final int MY_PERMISSIONS_IMAGE=2;
    public static final int MY_PERMISSIONS_VIDEO=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);

        setTitle("Product Details");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Product Details");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ProductDetailsActivity.this, MasterProductListActivity.class);
                Bundle b = new Bundle();
                b.putDouble("Power", send_power);
                b.putInt("FlowRate", send_flowrate);
                b.putDouble("Head", send_head);
                b.putInt("ProductType", send_producttype);
                b.putInt("Type", send_type);
                b.putInt("Inlet", send_inlet);
                b.putInt("Outlet", send_outlet);
                b.putDouble("HeadFeet", send_headfeet);
                b.putDouble("PowerHP", send_powerhp);
                b.putInt("Volt", send_volt);
                b.putInt("productID", Integer.valueOf(producttypeID));
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });

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

        ProductID=b.getInt("productID");


       spin_producttype = (Spinner) findViewById(R.id.spin_pdt_producttype);
        spin_power = (EditText) findViewById(R.id.spin_pdt_power);
        spin_flowrate = (Spinner) findViewById(R.id.spin_pdt_flowrate);
        spin_type = (Spinner) findViewById(R.id.spin_pdt_type);
        spin_head = (EditText) findViewById(R.id.spin_pdt_head);
        txt_description = (EditText) findViewById(R.id.pdt_description);
        txt_productname= (EditText) findViewById(R.id.pdt_name);
        spin_inlet = (Spinner) findViewById(R.id.spin_pdt_inlet);
        spin_outlet = (Spinner) findViewById(R.id.spin_pdt_outlet);
        spin_headfeet = (EditText) findViewById(R.id.spin_pdt_headfeet);
        spin_powerhp = (EditText) findViewById(R.id.spin_pdt_powerhp);
        edt_gaurantee = (EditText) findViewById(R.id.edt_pdt_gaurantee);
        edt_warranty = (EditText) findViewById(R.id.edt_pdt_warranty);
        priority = (EditText) findViewById(R.id.edt_pdt_priority);
        spin_volt = (Spinner) findViewById(R.id.spin_pdt_volt);

        product_video = (ImageView) findViewById(R.id.add_showvideo);
        edit = (Button) findViewById(R.id.details_edit);
        delete = (Button) findViewById(R.id.details_delete);
        product_img = (ImageView) findViewById(R.id.add_showimg);

        uploadphoto= (Button) findViewById(R.id.btn_pdt_txt_upload);
        uploadvideo= (Button) findViewById(R.id.btn_pdt_videoupload);
        txt_price= (EditText) findViewById(R.id.edt_price);
        txt_function= (EditText) findViewById(R.id.pdt_funNFeature);
        txt_application= (EditText) findViewById(R.id.pdt_application);
        txt_specification= (EditText) findViewById(R.id.pdt_specification);
       // txt_qauntity= (EditText) findViewById(R.id.edt_pdt_quantity);


        txt_productcode= (EditText) findViewById(R.id.edt_productCode);
        txt_modelno=(EditText)findViewById(edt_modelNo);
        /*spin_power.setText("0.00");
        spin_powerhp.setText("0.00");
        spin_head.setText("0.00");
        spin_headfeet.setText("0.00");*/
        uploadphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ProductDetailsActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ProductDetailsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProductDetailsActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS},
                            MY_PERMISSIONS_IMAGE);
                } else {
                    uploadid = 2;
                    uploadFile();
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(ProductDetailsActivity.this).create();
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Are you sure you want to Inactive this product?\n" +
                        " Literature(s) associated with this product will get Inactivated.");
                alertDialog.setCancelable(false);
                alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (!Utility.isInternetConnected(ProductDetailsActivity.this)) {

                                    Toast.makeText(ProductDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                                    new Handler().postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000);
                                } else {
                                    new DeleteProductTask().execute();
                                }

                            }
                        });
                alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();


            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spin_powerhp.getText().toString().isEmpty()){
                    spin_powerhp.setText("0.00");
                }

                if(spin_power.getText().toString().isEmpty()){
                    spin_power.setText("0.00");
                }

                if(spin_head.getText().toString().isEmpty()){
                    spin_head.setText("0.00");
                }

                if(spin_headfeet.getText().toString().isEmpty()){
                    spin_headfeet.setText("0.00");
                }

                if (!Utility.isInternetConnected(ProductDetailsActivity.this)) {

                    Toast.makeText(ProductDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                } else {

                    if (txt_productname.getText().toString().isEmpty()) {

                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();

                    } else if (txt_productname.getText().toString().startsWith(" ")) {

                        Toast.makeText(ProductDetailsActivity.this, "Product Name cannot start with blank space", Toast.LENGTH_SHORT).show();

                    }  else if (producttypeID == 0) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Select Product Type", Toast.LENGTH_SHORT).show();
                    } else if (spin_powerhp.getText().toString().isEmpty() ||  spin_powerhp.getText().toString().equalsIgnoreCase("0.00")) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter PowerHP", Toast.LENGTH_SHORT).show();
                    } else if (spin_power.getText().toString().isEmpty() ||  spin_power.getText().toString().equalsIgnoreCase("0.00")) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Power", Toast.LENGTH_SHORT).show();
                    } else if (spin_head.getText().toString().isEmpty() || spin_head.getText().toString().equals("0.00")) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Head", Toast.LENGTH_SHORT).show();
                    } else if (spin_headfeet.getText().toString().isEmpty() || spin_headfeet.getText().toString().equals("0.00")) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Head Feet", Toast.LENGTH_SHORT).show();
                    } else if (flowrateID == 0) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Select Flowrate", Toast.LENGTH_SHORT).show();
                    }
                  /*  else if (txt_productcode.getText().toString().isEmpty()) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Product Code", Toast.LENGTH_SHORT).show();
                    } */
                    else if (txt_modelno.getText().toString().isEmpty()) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Model No", Toast.LENGTH_SHORT).show();
                    } else if (txt_price.getText().toString().isEmpty()) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Price", Toast.LENGTH_SHORT).show();
                    } else if (validateprice(txt_price.getText().toString()) == false) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Valid Price", Toast.LENGTH_SHORT).show();
                    } /*else if (txt_qauntity.getText().toString().isEmpty()) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Quantity", Toast.LENGTH_SHORT).show();
                    } else if (validatequantity(txt_qauntity.getText().toString()) == false) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Valid Quantity", Toast.LENGTH_SHORT).show();
                    }*/ else if (txt_description.getText().toString().isEmpty()) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Product Description", Toast.LENGTH_SHORT).show();

                    } else if (imagepath == null) {

                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Product Image", Toast.LENGTH_SHORT).show();

                    }else if (priority.getText().toString().isEmpty()) {

                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Priority", Toast.LENGTH_SHORT).show();

                    }/*else if(filepathv == null){

                Toast.makeText(AddProductActivity.this, "Please Enter Product Video", Toast.LENGTH_SHORT).show();

            }*/
                  /*  else if (txt_function.getText().toString().isEmpty()) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Functions and Features", Toast.LENGTH_SHORT).show();
                    } else if (txt_application.getText().toString().isEmpty()) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter product Applications", Toast.LENGTH_SHORT).show();
                    } else if (txt_specification.getText().toString().isEmpty()) {
                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Product Specifications", Toast.LENGTH_SHORT).show();
                    }*/
                    else {
                        new UpdateProductTask().execute();
                    }

                }

/*
                    if (!Utility.isInternetConnected(ProductDetailsActivity.this)) {

                    Toast.makeText(ProductDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000) ;
                }else {

                    if(txt_productname.getText().toString().trim().length() == 0){

                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Product Name", Toast.LENGTH_SHORT).show();

                    }else if(imagepath == null){

                        Toast.makeText(ProductDetailsActivity.this, "Please Enter Product Image", Toast.LENGTH_SHORT).show();

                    }else{

                        new UpdateProductTask().execute();
                    }
                    }*/
            }


        });


        uploadvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ProductDetailsActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ProductDetailsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProductDetailsActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS},
                            MY_PERMISSIONS_VIDEO);
                } else {
                    uploadid = 1;
                    uploadVideoFile();
                }


            }
        });

        if (!Utility.isInternetConnected(ProductDetailsActivity.this)) {

        Toast.makeText(ProductDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

        }else {

            new FetchProductType().execute();

            new FetchFlowRate().execute();

            new FetchInlet().execute();
            new FetchOutlet().execute();

            new FetchVolt().execute();
            new FetchType().execute();

        }

        spin_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                typeID = typelistid.get(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_producttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                producttypeID = producttypelistid.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_flowrate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                    Toast.makeText(ProductDetailsActivity.this, "Please grant the permissions!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProductDetailsActivity.this, "Please grant the permissions!", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(ProductDetailsActivity.this, "Please grant the permissions!", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
        // other 'case' lines to check for other
        // permissions this app might request

    }

    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void uploadFile() {

        uploadid =2;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PICTURE);

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


    private void uploadVideoFile() {

        uploadid =1;
        Intent intentv = new Intent();
        intentv.setType("video/*");
        intentv.setAction(Intent.ACTION_PICK);
        intentv.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(intentv, "Select Video"), SELECT_VIDEO);

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
                            Toast.makeText(ProductDetailsActivity.this, "Too much large file to upload!", Toast.LENGTH_SHORT).show();
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
    private class UploadPhoto extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(ProductDetailsActivity.this);
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

            request.addProperty("token", Utility.getAuthToken(ProductDetailsActivity.this));
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

                        imagepath = filepath;
                        //filename = soapObject.getFileName();
//                        filepath = resp.getFilePath();
//                        filecontenttype = resp.getFileContentType();
                        Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
                        String newimg;
                        if (filepath != null) {
                            try {

                                filepath = filepath.replace("\\", "/");
                                //imagepath = imagepath.replaceAll(" ", "%20");

                                finalpathimg = Utility.URLFORIMAGE + filepath;
                                Glide.with(getApplicationContext()).load(finalpathimg).into(product_img);


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

            }else{

                Toast.makeText(ProductDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        //bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(2000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }

        return bitmap;
    }

    class FetchProductDetailsTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(ProductDetailsActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PRODUCTBYPRODUCTID);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("productID", ProductID);



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
                            + Utility.GET_PRODUCTBYPRODUCTID, mySoapEnvelop);


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
                        SoapObject result5 = (SoapObject) result4.getProperty(0);
                        System.out.println("Result4 is : " + result4.toString());
                        int count = result4.getPropertyCount();
                        System.out.println("Count is : " + count);
                        productname = result5.getPrimitivePropertySafelyAsString("ProductName");
                        model_no = result5.getPrimitivePropertySafelyAsString("ProductCode");
                        productype = result5.getPrimitivePropertySafelyAsString("ProductType");
                        power = result5.getPrimitivePropertySafelyAsString("Power");
                        productdescriptiion = result5.getPrimitivePropertySafelyAsString("ProductDesc");
                        type = result5.getPrimitivePropertySafelyAsString("Type");
                        head = result5.getPrimitivePropertySafelyAsString("Head");
                        flowrate = result5.getPrimitivePropertySafelyAsString("FlowRate");
                        inlet = result5.getPrimitivePropertySafelyAsString("Inlet");
                        outlet = result5.getPrimitivePropertySafelyAsString("Outlet");
                        powerhp = result5.getPrimitivePropertySafelyAsString("PowerHP");
                        headfeet = result5.getPrimitivePropertySafelyAsString("HeadFeet");
                        volt = result5.getPrimitivePropertySafelyAsString("Volt");
                        imagepath = result5.getPrimitivePropertySafelyAsString("ImagePath");
                        videopath = result5.getPrimitivePropertySafelyAsString("VideoPath");
                      applications=result5.getPrimitivePropertySafelyAsString("Applications");
                        specification=result5.getPrimitivePropertySafelyAsString("Specifications");;
                        functionandfeature=result5.getPrimitivePropertySafelyAsString("FunctAndFeatures");;
                        price=Double.valueOf(result5.getPropertySafelyAsString("Price", "0"));
                        availableqty=Integer.valueOf(result5.getPropertySafelyAsString("Quantity", "0"));

                       categoryid=Integer.valueOf(result5.getPropertySafelyAsString("ProductCategoryID", "0"));
                        subcategoryid=Integer.valueOf(result5.getPropertySafelyAsString("ProductSubCategoryID", "0"));
                        productcode=result5.getPrimitivePropertySafelyAsString("ProductCode");
                        modelno=result5.getPrimitivePropertySafelyAsString("ModelNum");
                        nameimg = result5.getPrimitivePropertySafelyAsString("ImageName");
                        namevideo = result5.getPrimitivePropertySafelyAsString("VideoName");
                        if(result5.hasProperty("Guarantee")) {
                            Guarantee = Integer.parseInt(result5.getPrimitivePropertySafelyAsString("Guarantee"));
                        }
                        if(result5.hasProperty("Warranty")) {
                            Warranty = Integer.parseInt(result5.getPrimitivePropertySafelyAsString("Warranty"));
                        }
                        String Prio="";
                        if(result5.hasProperty("Priority")) {
                            Prio = result5.getPrimitivePropertySafelyAsString("Priority");
                        }

                        txt_application.setText(applications);
                        txt_specification.setText(specification);
                        txt_function.setText(functionandfeature);
                        txt_price.setText(price+"");
                        spin_powerhp.setText(powerhp);
                        spin_power.setText(power);
                        spin_head.setText(head);
                        spin_headfeet.setText(headfeet);
                        edt_warranty.setText(Warranty+"");
                        edt_gaurantee.setText(Guarantee+"");
                        //txt_qauntity.setText(availableqty+"");

                        txt_productcode.setText(productcode);
                        txt_modelno.setText(modelno);
                        priority.setText(Prio);

                        try {




                            if (inlet.isEmpty() || inlet.equalsIgnoreCase("anyType{}")) {

                            } else {
                                for (int i = 0; i < inletlistid.size(); i++) {
                                    int t = Integer.valueOf(inlet);
                                    if (t == inletlistid.get(i)) {
                                        spin_inlet.setSelection(i);
                                        break;
                                    }
                                }
                            }

                            if (outlet.isEmpty() || outlet.equalsIgnoreCase("anyType{}")) {

                            } else {
                                for (int i = 0; i < outletlistid.size(); i++) {
                                    int t = Integer.valueOf(outlet);
                                    if (t == outletlistid.get(i)) {
                                        spin_outlet.setSelection(i);
                                        break;
                                    }
                                }
                            }

                            if (volt.isEmpty() || volt.equalsIgnoreCase("anyType{}")) {

                            } else {
                                for (int i = 0; i < voltlistid.size(); i++) {
                                    int t = Integer.valueOf(volt);
                                    if (t == voltlistid.get(i)) {
                                        spin_volt.setSelection(i);
                                        break;
                                    }
                                }
                            }

                            if (flowrate.isEmpty() || flowrate.equalsIgnoreCase("anyType{}")) {

                            } else {
                                for (int i = 0; i < flowratelistid.size(); i++) {
                                    int t = Integer.valueOf(flowrate);
                                    if (t == flowratelistid.get(i)) {
                                        spin_flowrate.setSelection(i);
                                        break;

                                    }
                                }
                            }
                            if (productype.isEmpty() || productype.equalsIgnoreCase("anyType{}")) {

                            } else {
                                for (int i = 0; i < producttypelistid.size(); i++) {
                                    int t = Integer.valueOf(productype);
                                    if (t == producttypelistid.get(i)) {
                                        spin_producttype.setSelection(i);
                                        break;

                                    }
                                }
                            }
                            if (type.isEmpty() || type.equalsIgnoreCase("anyType{}")) {

                            } else {
                                for (int i = 0; i < typelistid.size(); i++) {
                                    int t = Integer.valueOf(type);
                                    if (t == typelistid.get(i)) {
                                        spin_type.setSelection(i);
                                        break;

                                    }
                                }
                            }

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        txt_description.setText(productdescriptiion);

                        txt_productname.setText(productname);
                        imagepath = imagepath.replace("\\", "/");
                        videopath = videopath.replace("\\", "/");
                        videopath = videopath.replace(" ","");
                        finalpathimg = Utility.URLFORIMAGE + imagepath;
                        finalpathvideo = Utility.URLFORIMAGE + videopath;
                       // product_video.setImageBitmap(retriveVideoFrameFromVideo(finalpathvideo));
                        Glide.with(getApplicationContext()).load(finalpathimg).into(product_img);
                        if(videopath.isEmpty() || videopath.equalsIgnoreCase("anyType{}")){

                        }else{
                            Glide.with(getApplicationContext()).load(finalpathimg).into(product_video);
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
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }else{

                Toast.makeText(ProductDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }







        ////



        ///////////////////////get Flowrate task////////////////////////////////

        class FetchFlowRate extends AsyncTask<Void, Void, SoapObject> {
            SoapObject result;
            private ProgressDialog progress;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //etClientName.setText("");
                progress = new ProgressDialog(ProductDetailsActivity.this);
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
                            flowratelist.add("Select Flowrate");
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
                            spin_flowrate.setAdapter(spinnerAdapter);


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

                    Toast.makeText(ProductDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
                progress = new ProgressDialog(ProductDetailsActivity.this);
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
                if(result!= null) {
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
                            spin_producttype.setAdapter(spinnerAdapter);


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

                    Toast.makeText(ProductDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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
                progress = new ProgressDialog(ProductDetailsActivity.this);
                progress.setMessage("Please wait...");
                progress.setCancelable(false);
                progress.show();
            }

            @Override
            protected SoapObject doInBackground(Void... arg0) {
                SoapObject request = new SoapObject(Utility.NAMESPACE,
                        Utility.GET_MISCDATAFROMCATEGORY);
                if(typelist.size() > 0){
                    typelist.clear();
                    typelistid.clear();
                }

                request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
                request.addProperty("category","Type");

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
                            if (!Utility.isInternetConnected(ProductDetailsActivity.this)) {

                                Toast.makeText(ProductDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                            } else {
                                new FetchProductDetailsTask().execute();

                            }

                        } else {
                            if (!Utility.isInternetConnected(ProductDetailsActivity.this)) {

                                Toast.makeText(ProductDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                            } else {
                                new FetchProductDetailsTask().execute();

                            }

                            Toast.makeText(getApplicationContext(),
                                    soapObject.getProperty("ErrorMessage").toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        if (!Utility.isInternetConnected(ProductDetailsActivity.this)) {

                            Toast.makeText(ProductDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                        } else {
                            new FetchProductDetailsTask().execute();

                        }

                    }

                }else{
                    if (!Utility.isInternetConnected(ProductDetailsActivity.this)) {

                        Toast.makeText(ProductDetailsActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                    } else {
                        new FetchProductDetailsTask().execute();

                    }

                    Toast.makeText(ProductDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
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


///////////////////upload video task//////////////////



    private class UploadVideo extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog prog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            prog = new ProgressDialog(ProductDetailsActivity.this);
            prog.setMessage("Please wait...");
            prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog.setIndeterminate(true);
            prog.setCancelable(false);
            prog.show();
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
            System.out.println(Utility.URL);
            System.out.println(request);
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

            if(result != null) {
                if(prog.isShowing())
                {
                    prog.dismiss();
                }
                try {

                    SoapObject soapObject = (SoapObject) result.getProperty(0);

                    System.out.println(soapObject.getProperty("IsSucceed"));
                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                        product_video.setImageBitmap(bmThumbnail);
                        filenamev = soapObject.getPropertyAsString("FileName");
                        filepathv = soapObject.getPropertyAsString("FilePath");
                        videopath = filepathv;
                        filepathv = filepathv.replace("\\", "/");
                        finalpathvideo = Utility.URLFORIMAGE + filepathv;

                        product_video.setImageBitmap(retriveVideoFrameFromVideo(finalpathvideo));

                        //imagepath = imagepath.replaceAll(" ", "%20");

                        // Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
                        //   uploadfilenm.setText("");

                        //new insertVideoTask().execute();

                    } else {
                        Toast.makeText(getBaseContext(),
                                soapObject.getProperty("ErrorMessage").toString(),
                                Toast.LENGTH_LONG).show();
                    }

                } catch (NullPointerException e) {

                    Toast.makeText(getApplicationContext(), "Please fill proper Data", Toast.LENGTH_LONG).show();
                } catch (Exception e) {

                    e.printStackTrace();
                } catch (Throwable throwable) {

                    throwable.printStackTrace();
                }

            }else{
                if(prog.isShowing())
                {
                    prog.dismiss();
                }
                Toast.makeText(ProductDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


//////////////////////update product task/////////////////////////////



    /////////////////////////////////////////////////////////////////////

    class UpdateProductTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(ProductDetailsActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.UPDATEPRODUCT);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("productID",ProductID);
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
            request.addProperty("imagePath",imagepath);
            request.addProperty("videoName",filenamev);
            request.addProperty("videoPath",videopath);
            request.addProperty("FunctionAndFeatures",txt_function.getText().toString());
            request.addProperty("Applications",txt_application.getText().toString());
            request.addProperty("Specifications",txt_specification.getText().toString());
            request.addProperty("ShippingCharges", Double.valueOf("0.0")+"");
            request.addProperty("ImageContentType",extension);
            request.addProperty("VideoContentType",extensionv);
            request.addProperty("voltID",voltID);
            if(edt_gaurantee.getText().toString().isEmpty()){
                request.addProperty("Guarantee",0);

            }else{
                request.addProperty("Guarantee",Integer.valueOf(edt_gaurantee.getText().toString()));

            }

            if(edt_warranty.getText().toString().isEmpty()){
                request.addProperty("Warranty",0);

            }else{
                request.addProperty("Warranty",Integer.valueOf(edt_warranty.getText().toString()));

            }
            request.addProperty("Priority",priority.getText().toString());
            request.addProperty("ModelNum",txt_modelno.getText().toString());
            request.addProperty("Price",txt_price.getText().toString());
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
                            + Utility.UPDATEPRODUCT, mySoapEnvelop);


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

                        Toast.makeText(getApplicationContext(), "Product Updated Successfully",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ProductDetailsActivity.this, ProductsmasterActivity.class);
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

                Toast.makeText(ProductDetailsActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class DeleteProductTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(ProductDetailsActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.DELETEPRODUCT);

            request.addProperty("token",Utility.getAuthToken(getApplicationContext()));
            request.addProperty("ID",ProductID);



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
                            + Utility.DELETEPRODUCT, mySoapEnvelop);


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

                        Toast.makeText(getApplicationContext(), "Product Inactivated Successfully",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ProductDetailsActivity.this, ProductsmasterActivity.class);
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

             }
        }
    }

    /////////////////////////


    @Override
    public void onBackPressed() {

        Intent i=new Intent(ProductDetailsActivity.this,MasterProductListActivity.class);

        Bundle b = new Bundle();
        b.putDouble("Power", send_power);
        b.putInt("FlowRate",send_flowrate);
        b.putDouble("Head", send_head);
        b.putInt("ProductType", send_producttype);
        b.putInt("Type", send_type);
        b.putInt("Inlet", send_inlet);
        b.putInt("Outlet", send_outlet);
        b.putDouble("HeadFeet", send_headfeet);
        b.putDouble("PowerHP", send_powerhp);
        b.putInt("Volt", send_volt);
        b.putInt("productID",Integer.valueOf(producttypeID));
        i.putExtras(b);
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
