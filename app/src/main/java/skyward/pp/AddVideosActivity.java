package skyward.pp;

import android.Manifest;
import android.app.Dialog;
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
import android.os.Handler;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
//import org.apache.commons.codec.binary.*;

import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import skyward.pp.util.Utility;

public class AddVideosActivity extends AppCompatActivity {

    String encodedBase64 = null;
    String enocdeBase64img=null;
    private static final int SELECT_VIDEO = 1;
    private static final String CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    VideoView videoview;
    ImageView imgview;
    int uploadid=1;
    private static final int SELECT_DOC = 1;
    String filename="",filepath="" , extension,imagename="",imgpath="";
    Button btn_upload;
    Bitmap imgthumb;
    public static final int MY_PERMISSIONS_ALL=1;
    public static final int MY_PERMISSIONS_IMAGE=2;
    Dialog pop,popattachment,popimage;
    Button btn_galeryattechment,btn_cameraattechmnet,btn_galleryvideo,btn_caemravideoattechment;
    public static final int MY_PERMISSIONS_VIDEO=3;
    public final static String APP_VIDEO_PATH_SD_CARD = "Video";
    public final static String APP_PATH_SD_CARD = "/PrakashPump/";
    String finalvideopath="";
    public static String finalvideopathpass="";
    private Uri fileUri;
    private static final int VIDEO_CAPTURE = 101;
    File mediaFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvideos);

        setTitle("Add Videos");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Videos");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AddVideosActivity.this, MyVideosActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_upload= (Button) findViewById(R.id.btnaddvideo_upload);
        imgview= (ImageView) findViewById(R.id.video_addvideoshowvideo);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(AddVideosActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AddVideosActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(AddVideosActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {


                    ActivityCompat.requestPermissions(AddVideosActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_VIDEO);


                }
                else {

                    popattachment = new Dialog(AddVideosActivity.this);
                    popattachment.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    popattachment.setContentView(R.layout.video_attechment);

                    btn_galeryattechment = (Button) popattachment.findViewById(R.id.btn_videogallery);
                    btn_cameraattechmnet = (Button) popattachment.findViewById(R.id.btn_videocamera_attachment);
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

                            uploadid = 2;
                           // recordvideonotes();
                            popattachment.cancel();
                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                            // fileUri = Uri.fromFile(mediaFile);
                            // intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(intent, VIDEO_CAPTURE);


                        }
                    });
                    btn_galeryattechment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            popattachment.cancel();
                            uploadid = 1;
                            Intent intent = new Intent();
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_PICK);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            startActivityForResult(Intent.createChooser(intent, "Select File To Upload"), SELECT_VIDEO);


                        }
                    });
                }

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK) {

            Uri fileUri = data.getData();
            String path = fileUri.getPath().toString();

               /* if(path == null) {
                    System.out.println("********************Path not found");
                    Toast.makeText(AddVideosActivity.this, "FilePath Not Found", Toast.LENGTH_SHORT).show();
                    finish();

                } else {*/

                    //Toast.makeText(getApplicationContext(),"paths is: "+filepath,Toast.LENGTH_LONG).show();
                    File f = new File(path);
                    if(f.exists())
                    {
                        filename = f.getName().toString();
                        extension = filename.substring(filename.lastIndexOf("."));
//                        String arr[]=filename.split(".");
//                        imagename=arr[0];
//                        imagename=imagename+".jpg";

                        System.out.println("*****" + extension);
                        Bitmap bmThumbnail;
                        bmThumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmThumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        enocdeBase64img = Base64.encodeToString(byteArray,Base64.DEFAULT);

                        imgview.setImageBitmap(bmThumbnail);
                        /////////////////latest code////////////////////
                        File originalFile = new File(path);
                        long fileSizeInBytes = originalFile.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                        long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                        long fileSizeInMB = fileSizeInKB / 1024;
                        if(fileSizeInMB>15)
                        {
                            Toast.makeText(getApplicationContext(),"You cannot upload more than 15 MB video", Toast.LENGTH_LONG).show();

                        }
                        else
                        {

                            try {
                                FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
                                byte[] bytes = new byte[(int)originalFile.length()];
                                //  byte[] bytes = new byte[30000];
                                fileInputStreamReader.read(bytes);
                                //     byte []encode= Base64.encodeToString(bytes,Base64.DEFAULT);
                                encodedBase64 = Base64.encodeToString(bytes,Base64.DEFAULT);
                                String a="";
                                new Submit().execute();

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

                        filepath= getPath(data.getData());
                        if(filepath == null) {
                            finish();
                            System.out.println("+++++++++++++++++++++filenot exists");
                        } else {

                            File fl = new File(filepath);
                            if (fl.exists()) {
                                filename = fl.getName().toString();
                                extension = filename.substring(filename.lastIndexOf("."));
//                                String arr[]=filename.split(".");
//                                imagename=arr[0];
//                                imagename=imagename+".jpg";

                                System.out.println("*****" + extension);
                                Bitmap bmThumbnail;
                                bmThumbnail = ThumbnailUtils.createVideoThumbnail(filepath, MediaStore.Video.Thumbnails.MICRO_KIND);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bmThumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                enocdeBase64img = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                imgview.setImageBitmap(bmThumbnail);
                                /////////////////latest code////////////////////
                                File originalFile2 = new File(filepath);
                                long fileSizeInBytes2 = originalFile2.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                                long fileSizeInKB2 = fileSizeInBytes2 / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                                long fileSizeInMB2 = fileSizeInKB2 / 1024;
                                if (fileSizeInMB2 > 15) {
                                    Toast.makeText(getApplicationContext(), "You cannot upload more than 15 MB video", Toast.LENGTH_LONG).show();

                                } else {

                                    try {
                                        FileInputStream fileInputStreamReader = new FileInputStream(originalFile2);
                                        byte[] bytes = new byte[(int) originalFile2.length()];
                                        //  byte[] bytes = new byte[30000];
                                        fileInputStreamReader.read(bytes);
                                        //     byte []encode= Base64.encodeToString(bytes,Base64.DEFAULT);
                                        encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
                                        String a = "";
                                        new Submit().execute();
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

        }else if(resultCode == RESULT_OK && uploadid ==2){

            Uri fileUri = data.getData();
            String path = fileUri.getPath().toString();


            File f = new File(path);
            if(f.exists())




            {
                filename = f.getName().toString();
                extension = filename.substring(filename.lastIndexOf("."));
//                        String arr[]=filename.split(".");
//                        imagename=arr[0];
//                        imagename=imagename+".jpg";

                System.out.println("*****" + extension);
                Bitmap bmThumbnail;
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmThumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                enocdeBase64img = Base64.encodeToString(byteArray,Base64.DEFAULT);

                imgview.setImageBitmap(bmThumbnail);
                /////////////////latest code////////////////////
                File originalFile = new File(path);
                long fileSizeInBytes = originalFile.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;
                if(fileSizeInMB>15)
                {
                    Toast.makeText(getApplicationContext(),"You cannot upload more than 15 MB video", Toast.LENGTH_LONG).show();

                }
                else
                {

                    try {
                        FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
                        byte[] bytes = new byte[(int)originalFile.length()];
                        //  byte[] bytes = new byte[30000];
                        fileInputStreamReader.read(bytes);
                        //     byte []encode= Base64.encodeToString(bytes,Base64.DEFAULT);
                        encodedBase64 = Base64.encodeToString(bytes,Base64.DEFAULT);
                        String a="";
                        new Submit().execute();

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

                filepath= getPath(data.getData());
                if(filepath == null) {
                    finish();
                    System.out.println("+++++++++++++++++++++filenot exists");
                } else {

                    File fl = new File(filepath);
                    if (fl.exists()) {
                        filename = fl.getName().toString();
                        extension = filename.substring(filename.lastIndexOf("."));
//                                String arr[]=filename.split(".");
//                                imagename=arr[0];
//                                imagename=imagename+".jpg";

                        System.out.println("*****" + extension);
                        Bitmap bmThumbnail;
                        bmThumbnail = ThumbnailUtils.createVideoThumbnail(filepath, MediaStore.Video.Thumbnails.MICRO_KIND);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmThumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        enocdeBase64img = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        imgview.setImageBitmap(bmThumbnail);
                        /////////////////latest code////////////////////
                        File originalFile2 = new File(filepath);
                        long fileSizeInBytes2 = originalFile2.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                        long fileSizeInKB2 = fileSizeInBytes2 / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                        long fileSizeInMB2 = fileSizeInKB2 / 1024;
                        if (fileSizeInMB2 > 15) {
                            Toast.makeText(getApplicationContext(), "You cannot upload more than 15 MB video", Toast.LENGTH_LONG).show();

                        } else {

                            try {
                                FileInputStream fileInputStreamReader = new FileInputStream(originalFile2);
                                byte[] bytes = new byte[(int) originalFile2.length()];
                                //  byte[] bytes = new byte[30000];
                                fileInputStreamReader.read(bytes);
                                //     byte []encode= Base64.encodeToString(bytes,Base64.DEFAULT);
                                encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
                                String a = "";
                                new Submit().execute();
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

        }

    }



    //////////////////////
    private static String base64Encode(byte[] in)       {
        StringBuilder out = new StringBuilder((in.length * 4) / 3);
        int b;
        for (int i = 0; i < in.length; i += 3)  {
            b = (in[i] & 0xFC) >> 2;
            out.append(CODES.charAt(b));
            b = (in[i] & 0x03) << 4;
            if (i + 1 < in.length)      {
                b |= (in[i + 1] & 0xF0) >> 4;
                out.append(CODES.charAt(b));
                b = (in[i + 1] & 0x0F) << 2;
                if (i + 2 < in.length)  {
                    b |= (in[i + 2] & 0xC0) >> 6;
                    out.append(CODES.charAt(b));
                    b = in[i + 2] & 0x3F;
                    out.append(CODES.charAt(b));
                } else  {
                    out.append(CODES.charAt(b));
                    out.append('=');
                }
            } else      {
                out.append(CODES.charAt(b));
                out.append("==");
            }
        }

        return out.toString();
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
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }









////////////////upload video Task/////////////////////////////////////////////////
//

    private class Submit extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddVideosActivity.this);
            progress.setMessage("Please wait..."+"\nWhile Uploading your Video");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SharedPreferences settings = getSharedPreferences("info",
                    MODE_PRIVATE);

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.UPLOAD_VIDEO);

            request.addProperty("Token", Utility.getAuthToken(getApplicationContext()));
         request.addProperty("f", encodedBase64);
            request.addProperty("fileName", filename);

            request.addProperty("imageF",enocdeBase64img);
            request.addProperty("imageName", "demoandroid.JPG");

            SoapSerializationEnvelope mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;
            mySoapEnvelop.setOutputSoapObject(request);
            HttpTransportSE myAndroidHttpTransport = null;
            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);

                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.UPLOAD_VIDEO, mySoapEnvelop);

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

            progress.dismiss();
            if(result !=null) {
                try {

                    SoapObject soapObject = (SoapObject) result.getProperty(0);

                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

                        filename = soapObject.getPropertySafelyAsString("FileName");
                        filepath = soapObject.getPropertySafelyAsString("FilePath");
                        imagename=soapObject.getPropertySafelyAsString("ImageFileName");

                        imgpath=soapObject.getPropertySafelyAsString("ImageFilePath");
                        // Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
                        //   uploadfilenm.setText("");

                        new insertVideoTask().execute();

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

                Toast.makeText(AddVideosActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

//
    ////////////////////////////////////////////////////////////////////////////////////////


    private class insertVideoTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(AddVideosActivity.this);
            progress.setMessage("Please wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SharedPreferences settings = getSharedPreferences("info",
                    MODE_PRIVATE);

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.INSERT_VIDEO);

            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));
            request.addProperty("fileName", filename);
            request.addProperty("filePath", filepath);
            request.addProperty("fileType", extension);
request.addProperty("imageFileName",imagename);
            request.addProperty("imageFilePath",imgpath);
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
                            + Utility.INSERT_VIDEO, mySoapEnvelop);

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

            progress.dismiss();
            if(result != null) {
                try {

                    SoapObject soapObject = (SoapObject) result.getProperty(0);

                    System.out.println(soapObject.getProperty("IsSucceed"));
                    if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

//                    filename=soapObject.getPropertyAsString("FileName");
                        //                  filepath=soapObject.getPropertyAsString("FilePath");
                        Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
                        //   uploadfilenm.setText("");
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                Intent i = new Intent(AddVideosActivity.this, MyVideosActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }, 1000);


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

                Toast.makeText(AddVideosActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

                Intent i = new Intent(AddVideosActivity.this, MyVideosActivity.class);
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
