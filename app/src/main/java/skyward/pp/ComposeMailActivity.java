package skyward.pp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skyward.pp.util.Utility;

public class ComposeMailActivity extends AppCompatActivity {

    int flag_attachment=0;
    int orderinquiryID;
    String encodedBase64 = null, encodedBase64video;
    String filepath, picturePath, filename ,extension,filepathv,filenamev,extensionv;
    EditText mail_to,mail_from,mail_cc,mail_bcc,mail_subject,mail_compose;
    TextView mail_attachments;
    Dialog pop,popattachment;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_DOC = 1;
    ImageButton audio_attachment,video_attachment,image_attachment,doc_attachment, camera_attachment;
    int uploadid;
    private Bitmap bitmap;
    byte[] bytes, encoded;
    private Bitmap imageBitmap = null;
    private final int REQ_CAMERA = 1000, REQ_ADD_NEW_JOINEE = 1001;
    ImageButton cancelattach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composemail);

        setTitle("Compose");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Compose");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ComposeMailActivity.this, OrderInquiryList.class);
                startActivity(i);
                finish();
            }
        });

        mail_to = (EditText) findViewById(R.id.mail_to);
        mail_cc = (EditText) findViewById(R.id.mail_cc);
        mail_bcc = (EditText) findViewById(R.id.mail_bcc);
        mail_subject = (EditText) findViewById(R.id.mail_subject);
        mail_compose = (EditText) findViewById(R.id.mail_compose);
        mail_attachments = (TextView) findViewById(R.id.mail_attachments);
        cancelattach = (ImageButton) findViewById(R.id.attach_cancel);

        orderinquiryID=Integer.valueOf(getIntent().getStringExtra("ID"));
        mail_to.setText(getIntent().getStringExtra("emailid"));
        mail_subject.setText("Reply - Pump Selection and Order Inquiry");


        cancelattach.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mail_attachments.setText("");
            filepath="";
            encodedBase64="";
            filename="";
            extension="";
            cancelattach.setVisibility(View.INVISIBLE);
            flag_attachment=0;
        }
    });
        cancelattach.setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater menuInflater = getSupportMenuInflater();
        getMenuInflater().inflate(R.menu.menu_composemail, menu);
        // return true;
        // MenuItemCompat.getActionView();

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_attach:

                popattachment = new Dialog(ComposeMailActivity.this);
                popattachment.requestWindowFeature(Window.FEATURE_NO_TITLE);

                popattachment.setContentView(R.layout.attachment_popup);
//                popattachment.setTitle("Attachment");


                video_attachment = (ImageButton) popattachment.findViewById(R.id.btn_videoAttachment);
                image_attachment = (ImageButton) popattachment.findViewById(R.id.btn_imgAttachment);
                doc_attachment = (ImageButton) popattachment.findViewById(R.id.btn_docAttachment);


                popattachment.show();
                Window window = popattachment.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND ;
                window.setAttributes(wlp);



                video_attachment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //FOR CONTACT
                        // startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);

                       /* uploadid = 1;
                        Intent intent = new Intent();
                        intent.setType("video*//*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivityForResult(Intent.createChooser(intent, "Select File To Upload"), SELECT_DOC);*/
                        uploadid =1;
                        Intent intentv = new Intent();
                        intentv.setType("video/*");
                        intentv.setAction(Intent.ACTION_PICK);
                        intentv.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivityForResult(Intent.createChooser(intentv, "Select Video"), SELECT_DOC);


                    }
                });
                image_attachment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
/*

                        uploadid = 2;

                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(intent, SELECT_PICTURE);
*/
                        uploadid =2;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PICTURE);
                    }
                });
                doc_attachment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       /* uploadid = 1;
                        uploadFile();*/

                        uploadid =1;
                        Intent intentv = new Intent();
                        intentv.setType("file/*");
                        intentv.setAction(Intent.ACTION_GET_CONTENT);
                        intentv.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivityForResult(Intent.createChooser(intentv, "Select file"), SELECT_DOC);

                    }
                });


               /* Intent i = new Intent(ComposeMailActivity.this, MyProfileActivity.class);
                startActivity(i);*/
                return true;

            case R.id.action_send:
                if (!Utility.isInternetConnected(ComposeMailActivity.this)) {

                    Toast.makeText(ComposeMailActivity.this, "Please Check your Internet Connection and come again!", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000) ;
                }else {
                        init();


                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    public void init() {

        boolean isError = false;
        String errorMsg = "Invalid Data";
        if (mail_to.getText().toString().equals("")) {
            isError = true;
            errorMsg = "Please enter To email Address";
        }
        else{

            boolean chk=isEmailValid(mail_to.getText().toString());
            if(chk==false)
            {
                isError = true;
                errorMsg = "Enter Valid To Email Address";

            }

        }

        if(mail_compose.getText().toString().startsWith(" ")){
            isError = true;
            errorMsg = "Mail Text cannot start with blank space.";
        }

        if(mail_compose.getText().toString().equals("")){
            isError = true;
            errorMsg = "Blank mail cannot be sent";
        }

        if(mail_cc.getText().toString().equals("")) {


        }else{

            boolean chk=isEmailValid(mail_cc.getText().toString());
            if(chk==false)
            {
                isError = true;
                errorMsg = "Enter Valid CC Email Address";

            }

        }

        if(mail_bcc.getText().toString().equals("")) {


        }else{

            boolean chk=isEmailValid(mail_bcc.getText().toString());
            if(chk==false)
            {
                isError = true;
                errorMsg = "Enter Valid BCC Email Address";

            }

        }

        if (isError) {
            Toast.makeText(ComposeMailActivity.this, errorMsg,
                    Toast.LENGTH_LONG).show();
        }else{

            new SendReplyTask().execute();}

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(flag_attachment==0) {
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
                        popattachment.dismiss();
                        extension = filename.substring(filename.lastIndexOf("."));
                        System.out.println("*****" + extension);
                        System.out.println("\n**** Uri :> " + fileUri.toString());
                        System.out.println("\n**** Path :> " + path.toString());


                        try {
                            FileInputStream fileInputStreamReader = new FileInputStream(f);
                            try {
                                bytes = loadFile(f);
                                encodedBase64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
                                mail_attachments.setText(f.getName().toString());
                                cancelattach.setVisibility(View.VISIBLE);
                                flag_attachment=1;

//                            Toast.makeText(getApplicationContext(),encodedBase64,Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ComposeMailActivity.this, "Too much large file to upload!", Toast.LENGTH_SHORT).show();
                            }
                       /* new UploadPhoto().execute();
*/

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
                                popattachment.dismiss();
                                extension = filename.substring(filename.lastIndexOf("."));
                                System.out.println("*****" + extension);

                                try {
                                    FileInputStream fileInputStreamReader = new FileInputStream(fl);
                                    // byte[] bytes = new byte[(int) originalFile2.length()];
                                    //fileInputStreamReader.read(bytes);
                                    bytes = loadFile(fl);
                                    encodedBase64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
                                    mail_attachments.setText(fl.getName().toString());
                                    cancelattach.setVisibility(View.VISIBLE);
                                    flag_attachment=1;


//                                Toast.makeText(getApplicationContext(),encodedBase64,Toast.LENGTH_LONG).show();

                                    String a = "";
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
            } else if (requestCode == SELECT_DOC && resultCode == RESULT_OK && uploadid == 1) {

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
                if (fv.exists()) {
                    filename = fv.getName().toString();
                    popattachment.dismiss();
                    extension = filename.substring(filename.lastIndexOf("."));
                    System.out.println("*****" + extension);
                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(pathvideo, MediaStore.Video.Thumbnails.MICRO_KIND);

/*
                showvideo.setImageBitmap(bmThumbnail);
*/
                    /////////////////latest code////////////////////
                    File originalFile = new File(pathvideo);
                    long fileSizeInBytes = originalFile.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;
                    if (fileSizeInMB > 10) {
                        Toast.makeText(getApplicationContext(), "You cannot upload more than 10 MB video", Toast.LENGTH_LONG).show();

                    } else {

                        try {
                            FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
                            byte[] bytes = new byte[(int) originalFile.length()];
                            //  byte[] bytes = new byte[30000];
                            fileInputStreamReader.read(bytes);
                            //     byte []encode= Base64.encodeToString(bytes,Base64.DEFAULT);
                            encodedBase64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
                            mail_attachments.setText(fv.getName().toString());
                            cancelattach.setVisibility(View.VISIBLE);
                            flag_attachment=1;

//                        Toast.makeText(getApplicationContext(),encodedBase64,Toast.LENGTH_LONG).show();

                            String a = "";
/*
                        new UploadVideo().execute();
*/

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


                } else {

                    filepath = getPath(data.getData());
                    if (filepath == null) {
                        finish();
                        System.out.println("+++++++++++++++++++++filenot exists");
                    } else {

                        File fl = new File(filepath);
                        if (fl.exists()) {
                            filename = fl.getName().toString();
                            popattachment.dismiss();
                            extension = filename.substring(filename.lastIndexOf("."));
                            System.out.println("*****" + extension);
                            Bitmap bmThumbnail;
                            bmThumbnail = ThumbnailUtils.createVideoThumbnail(filepath, MediaStore.Video.Thumbnails.MICRO_KIND);
/*
                        showvideo.setImageBitmap(bmThumbnail);
*/
                            /////////////////latest code////////////////////
                            File originalFile2 = new File(filepath);
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
                                    encodedBase64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
                                    mail_attachments.setText(fl.getName().toString());
                                    cancelattach.setVisibility(View.VISIBLE);
                                    flag_attachment=1;


//                                Toast.makeText(getApplicationContext(),encodedBase64,Toast.LENGTH_LONG).show();
                                    String a = "";
                            /*    new UploadVideo().execute();
//*/
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

            } else {
                System.out.println("Nothing");
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"You can select only one attachment",Toast.LENGTH_LONG).show();
        }
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


    private class SendReplyTask extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //etClientName.setText("");
            progress = new ProgressDialog(ComposeMailActivity.this);
            progress.setMessage("Please wait...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {
            SharedPreferences settings = getSharedPreferences("info",
                    MODE_PRIVATE);

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.ORDERINQUIRYREPLY);

            request.addProperty("token", Utility.getAuthToken(ComposeMailActivity.this));
            request.addProperty("orderInquiryID",orderinquiryID);;
            request.addProperty("toMailID",mail_to.getText().toString());;
            request.addProperty("cc",mail_cc.getText().toString());;
            request.addProperty("bcc",mail_bcc.getText().toString());;
            request.addProperty("subject",  mail_subject.getText().toString());;
            request.addProperty("reply",mail_compose.getText().toString());;
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
                            + Utility.ORDERINQUIRYREPLY, mySoapEnvelop);

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    // System.out.println("XmlPullParserException 0");
                } catch (SocketTimeoutException e) {
                    // System.out.println(e.getClass());
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
            try {

                SoapObject soapObject = (SoapObject) result.getProperty(0);
                System.out.println(soapObject.getProperty("IsSucceed"));
                if (soapObject.getProperty("IsSucceed").toString().equals("true")) {

                    //filename = soapObject.getFileName();
//                        filepath = resp.getFilePath();
//                        filecontenttype = resp.getFileContentType();
                    Toast.makeText(getApplicationContext(), "Replied Successfully", Toast.LENGTH_LONG).show();
                   Intent i = new Intent(ComposeMailActivity.this,OrderInquiryList.class);
                    startActivity(i);
                    finish();



                } else {
                    Toast.makeText(getBaseContext(),
                            soapObject.getProperty("ErrorMessage").toString(),
                            Toast.LENGTH_LONG).show();
                }

            } catch (NullPointerException e) {
               // Toast.makeText(getApplicationContext(), "Please fill proper Data", Toast.LENGTH_LONG).show();
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }  /////

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ComposeMailActivity.this, OrderInquiryList.class);
        startActivity(i);
        finish();
    }
}
