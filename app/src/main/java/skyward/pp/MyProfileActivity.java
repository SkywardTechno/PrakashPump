package skyward.pp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import skyward.pp.util.Utility;

public class MyProfileActivity extends AppCompatActivity {

    private ArrayList<String> countrylist = new ArrayList<String>();
    private ArrayList<Integer> countrylistID = new ArrayList<Integer>();
    public int tempcountrylist =0;
    private ArrayList<String> usertypelist = new ArrayList<String>();
    private ArrayList<Integer> usertypelistID = new ArrayList<Integer>();
    public int tempusertype =0;
    String simCode,countryname;
    private ArrayList<String> citylist = new ArrayList<String>();
    private ArrayList<Integer> citylistID = new ArrayList<Integer>();
    private ArrayList<String> arealist = new ArrayList<String>();
    private ArrayList<Integer> arealistID = new ArrayList<Integer>();
    EditText mobileno1,plot,area,place,emailid;
    Button myorders;
    ImageView edit, profileimage ,ok, upload;
    EditText profilename;
    int flag;
    String filepath, picturePath, filename,extension,filenamenew,filepathnew,extenionnew;
    private static final int SELECT_PICTURE = 1;
    int loader = R.drawable.profilebgblank;
    private Bitmap bitmap;
    String imgDecodableString, converted;
    byte[] bytes, encoded;
    String encodedBase64 = null;
    int uploadid ;
    String value;
    String city="",country="";
    String Name,MobileNo,Place, FarmNo,Area,Verificationcode,UserType,ID,imagepath, urlforimage,EmailID;
    int tempcityID = 0;
    int tempareaID = 0;

    final int PIC_CROP = 1;
    String filecontenttype="";
    private Bitmap imageBitmap = null;
    private final int REQ_CAMERA = 1000, REQ_ADD_NEW_JOINEE = 1001;
    private Uri picUri,mImageCaptureUri;
    public final static String APP_PATH_SD_CARD = "/PrakashPump/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "Images";
    Dialog popattachment;
    String glideimagepath="";
    Button btn_galeryattechment,btn_cameraattechmnet,btn_galleryvideo,btn_caemravideoattechment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        setTitle("My Profile");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Profile");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();

        flag =1;
        mobileno1 = (EditText) findViewById(R.id.profile_mobileno1);
        emailid = (EditText) findViewById(R.id.emailid);
        place = (EditText) findViewById(R.id.profile_place);
        plot = (EditText) findViewById(R.id.profile_plot);
        area = (EditText) findViewById(R.id.profile_area);
        edit = (ImageView) findViewById(R.id.profile_edit);
        ok = (ImageView) findViewById(R.id.profile_ok);
        upload = (ImageView) findViewById(R.id.profile_upload);
        profilename = (EditText) findViewById(R.id.profile_profileName);
        profileimage = (ImageView) findViewById(R.id.profileImage_iv_user);
        value = getIntent().getStringExtra("value");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              ok.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                upload.setVisibility(View.VISIBLE);
                mobileno1.setEnabled(false);
                place.setEnabled(false);
                plot.setEnabled(true);
                area.setEnabled(true);
                profilename.setEnabled(true);
                emailid.setEnabled(true);

            }
        });
        if (!Utility.isInternetConnected(MyProfileActivity.this)) {

            Toast.makeText(MyProfileActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {

            new FetchCountry().execute();
            //new Fetchprofile().execute();
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobileno1.setEnabled(false);
                place.setEnabled(false);
                plot.setEnabled(false);
                area.setEnabled(false);
                profilename.setEnabled(false);

                if (!Utility.isInternetConnected(MyProfileActivity.this)) {

                    Toast.makeText(MyProfileActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000) ;
                }else {

                    init();


                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                popattachment = new Dialog(MyProfileActivity.this);
                popattachment.requestWindowFeature(Window.FEATURE_NO_TITLE);
                popattachment.setContentView(R.layout.video_attechment);

                btn_galeryattechment = (Button) popattachment.findViewById(R.id.btn_videogallery);
                btn_cameraattechmnet = (Button) popattachment.findViewById(R.id.btn_videocamera_attachment);
                TextView txt_title = (TextView) popattachment.findViewById(R.id.attach_title);
                txt_title.setText("Pick or Capture Image");
                popattachment.show();
                Window window = popattachment.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                btn_cameraattechmnet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        uploadid = 3;
                        // recordvideonotes();
                        popattachment.cancel();
                        try {

                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, REQ_CAMERA);

                        }catch(Exception e){
                            e.printStackTrace();
                        }


                    }
                });
                btn_galeryattechment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popattachment.cancel();
                        uploadid = 1;
                        uploadFile();

                    }
                });
            }
                /* Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, SELECT_PICTURE);*/


        });


        if (!Utility.isInternetConnected(MyProfileActivity.this)) {

            Toast.makeText(MyProfileActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    finish();
                }
            }, 2000) ;
        }else {

//            new FetchCountry().execute();
            new Fetchprofile().execute();
        }


        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] countrys = countrylist.toArray(new String[countrylist
                        .size()]);
                alertCountry(countrys, v, place);


            }
        });

        plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tempcountrylist !=0) {
                    String[] countrys = citylist.toArray(new String[citylist
                            .size()]);
                    alertCity(countrys, v, plot);
                }else{
                    Toast.makeText(MyProfileActivity.this, "Please select Country first!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tempcityID != 0) {
                    String[] countrys = arealist.toArray(new String[arealist
                            .size()]);
                    alertArea(countrys, v, area);

                }else{
                    Toast.makeText(MyProfileActivity.this, "Please select City first!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void alertCountry(final String[] visitType, View v,
                              final TextView tvVisitType2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MyProfileActivity.this);
        // Source of the data in the DIalog

        // Set the dialog title
        builder.setTitle("Select Country")
                // Specify the list array, the items to be selected by
                // default
                // (null for none),
                // and the listener through which to receive callbacks
                // when
                // items are selected
                .setItems(visitType,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                for (int i = 0; i < countrylist.size(); i++) {
                                    if (countrylist.get(i).toString()
                                            .equals(visitType[which])) {
                                        tempcountrylist = countrylistID.get(i);
                                    }
                                }
                                tvVisitType2.setText(visitType[which]);
                                dialog.dismiss();
                                countryname = visitType[which];
                                tempcityID = 0;
                                plot.setText("");
                                tempareaID = 0;
                                area.setText("");
                                citylist = new ArrayList<String>();
                                arealist = new ArrayList<String>();
                                citylistID = new ArrayList<Integer>();
                                arealistID = new ArrayList<Integer>();
                                new FetchCity().execute();

                            }
                        });

        // Set the action buttons
        builder.show();

    }

    private void alertCity(final String[] visitType, View v,
                           final TextView tvVisitType2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MyProfileActivity.this);
        // Source of the data in the DIalog

        // Set the dialog title
        builder.setTitle("Select City")
                // Specify the list array, the items to be selected by
                // default
                // (null for none),
                // and the listener through which to receive callbacks
                // when
                // items are selected
                .setItems(visitType,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                for (int i = 0; i < citylist.size(); i++) {
                                    if (citylist.get(i).toString()
                                            .equals(visitType[which])) {
                                        tempcityID = citylistID.get(i);
                                    }
                                }
                                tvVisitType2.setText(visitType[which]);
                                dialog.dismiss();
                                tempareaID = 0;
                                area.setText("");
                                arealist = new ArrayList<String>();
                                arealistID = new ArrayList<Integer>();
                                new FetchArea().execute();

                            }
                        });

        // Set the action buttons
        builder.show();

    }


    private void alertArea(final String[] visitType, View v,
                           final TextView tvVisitType2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MyProfileActivity.this);
        // Source of the data in the DIalog

        // Set the dialog title
        builder.setTitle("Select Area")
                // Specify the list array, the items to be selected by
                // default
                // (null for none),
                // and the listener through which to receive callbacks
                // when
                // items are selected
                .setItems(visitType,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                for (int i = 0; i < arealist.size(); i++) {
                                    if (arealist.get(i).toString()
                                            .equals(visitType[which])) {
                                        tempareaID = arealistID.get(i);
                                    }
                                }
                                tvVisitType2.setText(visitType[which]);
                                dialog.dismiss();


                            }
                        });

        // Set the action buttons
        builder.show();

    }


    public void init() {

        boolean isError = false;
        String errorMsg = "Invalid Data";
        if (profilename.getText().toString().trim().length() == 0) {
            isError = true;
            errorMsg = "Profile Name cannot be empty";

        }
        if (tempareaID == 0) {
            isError = true;
            errorMsg = "Please Select Area";

        }
        if (tempcityID == 0) {
            isError = true;
            errorMsg = "Please Select City";

        }
        if (mobileno1.getText().toString().equals("") ) {

            isError = true;
            errorMsg = "Please enter Mobile No";

        }else{

                if(mobileno1.getText().toString().length() < 6 || mobileno1.getText().toString().length() > 16) {
                    isError = true;
                    errorMsg = "Enter Valid Mobile Number";


                }

        }

        if(emailid.getText().toString().equals("")) {


        }else{

            boolean chk=isEmailValid(emailid.getText().toString());
            if(chk==false)
            {

                isError = true;
                errorMsg = "Enter Valid Email Address";

            }

        }

        if (isError) {

            place.setEnabled(false);
            plot.setEnabled(true);
            area.setEnabled(true);
            profilename.setEnabled(true);
            emailid.setEnabled(true);
            edit.setVisibility(View.INVISIBLE);
            ok.setVisibility(View.VISIBLE);
            upload.setVisibility(View.VISIBLE);
            Toast.makeText(MyProfileActivity.this, errorMsg,
                    Toast.LENGTH_LONG).show();

        }else{
            place.setEnabled(false);
            plot.setEnabled(false);
            area.setEnabled(false);
            profilename.setEnabled(false);
            emailid.setEnabled(false);
            edit.setVisibility(View.VISIBLE);
            ok.setVisibility(View.GONE);
            upload.setVisibility(View.GONE);
            new EditProfile().execute();
        }

    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\D\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    private void uploadFile() {

        uploadid = 1;

        Intent in = new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_PICK);
        in.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(Intent.createChooser(in, "Select Image"), SELECT_PICTURE);

    }


    private void performCrop(Uri picUri) {
        try {
            uploadid = 2;
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
           /* cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 4);*/
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && uploadid == 1) {
            System.out.println("*********Image");


            try {

                Uri fileUri = data.getData();
                String path = fileUri.getPath().toString();
                glideimagepath = path;
                File f = new File(path);
                if (f.exists()) {

                    filenamenew = f.getName().toString();
                    if (filenamenew.contains(" ")) {
                        filenamenew = filenamenew.replace(" ", "");
                    }
                    extenionnew = filenamenew.substring(filenamenew.lastIndexOf("."));
                    System.out.println("*****" + extenionnew);
                    System.out.println("\n**** Uri :> " + fileUri.toString());
                    System.out.println("\n**** Path :> " + path.toString());



                } else {

                    path = getPath(data.getData());
                    glideimagepath = path;
                    if (path == null) {
                        finish();
                        System.out.println("+++++++++++++++++++++filenot exists");
                    } else {
                        File fl = new File(path);
                        if (fl.exists()) {
                            filenamenew = fl.getName().toString();
                            extenionnew = filenamenew.substring(filenamenew.lastIndexOf("."));
                            System.out.println("*****" + extenionnew);

                        }


                        System.out.println("\n**** File Not Exist :> " + path);
                    }
                }

                performCrop(fileUri);

            } catch (Exception e) {
                //  ShowDialog_Ok("Error", "Cannot Open File");
            }


        }else if(resultCode == RESULT_OK && uploadid ==2){
            System.out.println("*********Crop");

            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();


                Bitmap selectedBitmap = extras.getParcelable("data");

                profileimage.setImageBitmap(selectedBitmap);

                ///////////////////////////////////////

             /*   int bytes = selectedBitmap.getByteCount();
//or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
//int bytes = b.getWidth()*b.getHeight()*4;

                ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
                selectedBitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

                byte[] array = buffer.array();*/
                ///////////////////////////

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                // get the cropped bitmap

                filename = filenamenew;
                extension = extenionnew;
                new UploadPhoto().execute();

            }else{
                System.out.println("IIIIIIIIINSSSSS");
            }
        }else if(requestCode == REQ_CAMERA && resultCode == RESULT_OK && uploadid == 3){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            String finalfilepath=saveImageToExternalStorage(photo, getApplicationContext());
            glideimagepath = finalfilepath;
            File mediaFile = new
                    File(finalfilepath);
            Uri camuri = Uri.fromFile(mediaFile);
            String []arr=finalfilepath.split("/");
            filenamenew=arr[arr.length-1];
            extenionnew = filenamenew.substring(filenamenew.lastIndexOf("."));
            performCrop(camuri);

        }
    }

    public String saveImageToExternalStorage(Bitmap image,Context context) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;

        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            OutputStream fOut = null;
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            String currentdate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String currenttime = new SimpleDateFormat("HHmmss").format(new Date());
            String finalfilename=currentdate+"_"+currenttime+".png";
            File file = new File(fullPath,finalfilename);
            file.createNewFile();
            fOut = new FileOutputStream(file);

// 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

            }
            catch (Exception e)
            {
                String msg=e.getMessage();
                // Toast.makeText(takeNotes.this,msg, Toast.LENGTH_SHORT).show();
            }
            return file.getAbsolutePath();

        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return "";
        }
    }




    //////////////////////
    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    class Fetchprofile extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(MyProfileActivity.this);
            progress.setMessage("Loading your Profile...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_PROFILE);


            request.addProperty("token", Utility.getAuthToken(MyProfileActivity.this));

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_PROFILE);

            //System.out.println("enpl id:" + abc);
            //request.addProperty("EmployeeID", abc);
            SoapSerializationEnvelope mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            mySoapEnvelop.setOutputSoapObject(request);
            HttpTransportSE myAndroidHttpTransport = null;
            System.out.println(Utility.URL);
            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);

                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.GET_PROFILE, mySoapEnvelop);


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
                            Name = soapResult.getPrimitivePropertySafelyAsString("Name")
                                    .toString();
                            Area = soapResult.getPrimitivePropertySafelyAsString("Area")
                                    .toString();
                            if(soapResult.hasProperty("ShippingCity")) {
                                if(soapResult.getPrimitivePropertySafelyAsString("ShippingCity")
                                        .toString().equalsIgnoreCase("anyType{}") || soapResult.getPrimitivePropertySafelyAsString("ShippingCity")
                                        .toString().equalsIgnoreCase("")){

                                }else {
                                    tempcityID = Integer.valueOf(soapResult.getPrimitivePropertySafelyAsString("ShippingCity")
                                            .toString());
                                }
                            }
                            if(soapResult.hasProperty("AreaID")) {
                                if(soapResult.getPrimitivePropertySafelyAsString("AreaID")
                                        .toString().equalsIgnoreCase("anyType{}") || soapResult.getPrimitivePropertySafelyAsString("AreaID")
                                        .toString().equalsIgnoreCase("")){

                                }else {
                                    tempareaID = Integer.valueOf(soapResult.getPrimitivePropertySafelyAsString("AreaID")
                                            .toString());
                                }
                            }
                            if(soapResult.hasProperty("ShippingCountry")) {
                                if(soapResult.getPrimitivePropertySafelyAsString("ShippingCountry")
                                        .toString().equalsIgnoreCase("anyType{}") || soapResult.getPrimitivePropertySafelyAsString("ShippingCountry")
                                        .toString().equalsIgnoreCase("")){

                                }else {
                                    tempcountrylist = Integer.valueOf(soapResult.getPrimitivePropertySafelyAsString("ShippingCountry")
                                            .toString());
                                }
                            }
                            city=soapResult.getPrimitivePropertySafelyAsString("CityName")
                                    .toString();
                            country=soapResult.getPrimitivePropertySafelyAsString("Country")
                                    .toString();
                            FarmNo = soapResult.getPrimitivePropertySafelyAsString("FarmNo")
                                    .toString();
                            MobileNo = soapResult.getPrimitivePropertySafelyAsString("UserName")
                                    .toString();
                            EmailID = soapResult.getPrimitivePropertySafelyAsString("EmailID")
                                    .toString();
                            Place = soapResult.getPrimitivePropertySafelyAsString("Place")
                                    .toString();
                            Verificationcode = soapResult.getPrimitivePropertySafelyAsString("VerificationCode")
                                    .toString();
                            UserType = soapResult.getPrimitivePropertySafelyAsString("UserTypeID")
                                    .toString();
                            filename = soapResult.getPrimitivePropertySafelyAsString("FileName").toString();

                            ID = soapResult.getPrimitivePropertySafelyAsString(
                                    "UserID").toString();
                            filepath = soapResult.getPrimitivePropertySafelyAsString("FilePath").toString();

                            if(filename.isEmpty()){

                            }else{
                                extension = filename.substring(filename.lastIndexOf("."));

                            }


                            System.out.println(filename);

                            profilename.setText(Name);
                            mobileno1.setText(MobileNo);
                            place.setText(country);
                            plot.setText(city);
                            area.setText(Area);
                            emailid.setText(EmailID);
                            /*System.out
							.println("Sss:"
									+ soapResult.getProperty("CustomerName")
											.toString());
*/
                        }

                            new FetchCountry().execute();
                        if(tempcountrylist!=0)
                        {

                            new FetchCity().execute();
                        }

                        if(tempcityID!=0)
                        {
                            new FetchArea().execute();
                        }

                        if (filepath.isEmpty()) {

                        }else{
                            try {

                                filepath = filepath.replace("\\", "/");
                                //imagepath = imagepath.replaceAll(" ", "%20");

                                urlforimage = Utility.URL + filepath;
                                String finalpath= Utility.URLFORIMAGE+filepath;
                                Glide.with(getApplicationContext()).load(finalpath).into(profileimage);


                               /* ImageLoader imgLoader = new ImageLoader(getApplicationContext());
                                imgLoader.DisplayImage(Utility.URLFORIMAGE + filepath, loader, profileimage);*/
                                // LoadImageFromWebOperations(urlforimage);
                                // new LoadImage().execute(imagepath);
                                System.out.println("Image path is : " + finalpath);


                            } catch (NullPointerException e) {

                            }

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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(MyProfileActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class EditProfile extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(MyProfileActivity.this);
            progress.setMessage("Please Wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.UPDATE_PROFILE);


            request.addProperty("token", Utility.getAuthToken(MyProfileActivity.this));
            request.addProperty("name", profilename.getText().toString());
            request.addProperty("country",tempcountrylist);
            request.addProperty("city",tempcityID);
            request.addProperty("area", tempareaID);
            request.addProperty("emailID", emailid.getText().toString());
            request.addProperty("phoneNo", mobileno1.getText().toString());
            request.addProperty("fileName", filename);
            request.addProperty("filePath", filepath);
            request.addProperty("filePath", extension);

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.UPDATE_PROFILE);

            //System.out.println("enpl id:" + abc);
            //request.addProperty("EmployeeID", abc);
            SoapSerializationEnvelope mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            mySoapEnvelop.setOutputSoapObject(request);
            HttpTransportSE myAndroidHttpTransport = null;
            System.out.println(Utility.URL);
            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);

                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.UPDATE_PROFILE, mySoapEnvelop);


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
            if(result != null) {
                try {
                    SoapObject soapObject = (SoapObject) result.getProperty(0);
                    System.out.println(soapObject.getProperty("IsSucceed"));

                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
                        Utility.saveemailid(getApplicationContext(),emailid.getText().toString());

                        Toast.makeText(MyProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        if(value.equals("dash")){
                            Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            Intent i = new Intent(getApplicationContext(),AdminDashboardActivity.class);
                            startActivity(i);
                            finish();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(MyProfileActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }



    class FetchCountry extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(MyProfileActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GET_COUNTRY);

            if (countrylist.size() > 0) {
                countrylist.clear();
                countrylistID.clear();
            }

            System.out.println("URL is ::" + Utility.SOAP_ACTION
                    + Utility.GET_COUNTRY);
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
                            + Utility.GET_COUNTRY, mySoapEnvelop);
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
                        System.out.println("Count is : " + count);
                        //customer.clear();
                        //customerID.clear();
                        countrylist=new ArrayList<>();
                        countrylistID=new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            countrylist.add(soapResult.getPropertySafelyAsString("Name", "")
                                    .toString());
                            countrylistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));

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

            } else {
                Toast.makeText(MyProfileActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FetchCity extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(MyProfileActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETCITY);

            request.addProperty("CountryID", tempcountrylist);

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
                            + Utility.GETCITY, mySoapEnvelop);


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
                        citylist=new ArrayList<>();
                        citylistID=new ArrayList<>();

                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            citylist.add(soapResult.getPropertySafelyAsString("CityName", "")
                                    .toString());

                            citylistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }


                    } else {

                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FetchArea extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(MyProfileActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETAREA);



            request.addProperty("CityID", tempcityID);



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
                            + Utility.GETAREA, mySoapEnvelop);


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
                      arealist = new ArrayList<>();
                        arealistID = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            SoapObject soapResult = null;
                            soapResult = (SoapObject) result4.getProperty(i);
                            arealist.add(soapResult.getPropertySafelyAsString("AreaName", "")
                                    .toString());

                            arealistID.add(Integer.parseInt(soapResult.getPropertySafelyAsString(
                                    "ID").toString()));


                        }



                    } else {

                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }
    }


    private class UploadPhoto extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(MyProfileActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SharedPreferences settings = getSharedPreferences("info",
                    MODE_PRIVATE);

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.UPLOAD_PROFILEIMAGE);

            request.addProperty("token", Utility.getAuthToken(MyProfileActivity.this));
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
                            + Utility.UPLOAD_PROFILEIMAGE, mySoapEnvelop);

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
                                String finalpath= Utility.URLFORIMAGE+filepath;
                                Glide.with(getApplicationContext()).load(finalpath).into(profileimage);

                              /*  ImageLoader imgLoader = new ImageLoader(getApplicationContext());
                                imgLoader.DisplayImage(Utility.URLFORIMAGE + newimg, loader, profileimage);*/
                                // LoadImageFromWebOperations(urlforimage);
                                // new LoadImage().execute(imagepath);
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

            }else{

                Toast.makeText(MyProfileActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(value.equals("dash")){
            Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(getApplicationContext(),AdminDashboardActivity.class);
            startActivity(i);
            finish();
        }

    }
}
