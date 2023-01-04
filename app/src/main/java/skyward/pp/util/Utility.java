package skyward.pp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import skyward.pp.R;

public class Utility {
    public static final int SUCCESS = 100;
    public static final int FAILURE = SUCCESS + 1;
    public static final int NO_INTERNET = SUCCESS + 2;
    public static String DD_MMM_YYYY = "dd-MMM-yyyy";
    public static String DD_MMM_YYYY_HH_MM = "dd-MMM-yyyy HH:mm";
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
    public static String HH_mm = "HH:mm";
    public static String DD_MM_YYYY = "dd-MM-yyyy";

  //public static String URL = "http://192.168.1.25:5090/Webservice/service.asmx"; //local
   public static String URL = "http://apex.skywardcrm.com/prakashpumpWebservice/Webservice/Service.asmx"; //live
    public static String GET_PRODSUCTCATEGORY="GetProductCategory";
    public static String GET_PRODUCTSUBCATEGORY="GetProductSubCategoriesByCategoryID";
    public static String GET_PRODUCTBYCATERGORYANDSUBCATEGORY="GetProductByCategoryAndSubCategoryID";
    public static String ADDTOCART="InsertUserCart";
    public static String ADDTOWISHLIST="InsertUserWishList";
    public static String GET_ORDERINQUIRYBYUSERID="GetOrderInquiryByUserID";
    public static String GETCARTBYUSER="GetUserCart";
    public static String GETWISHLISTBYUSER="GetUserWishList";
    public static String GLOBALSEARCH="GlobalSearch";
    public static String UPDATE_ORDERSTATUS="UpdatePlaceOrderIsRead";


    public static final String NAMESPACE = "http://tempuri.org/";
    public static String USER_AUTHENTICATION = "UserAuthentication";
    public static String GET_COUNTRY = "GetCountryCode";
    public static String USER_REGISTERATION = "UserRegistration";
    public static String RESEND_VERIFICATIONCODE = "ResendVerificationCode";
    public static String GET_PUMPDETAILS = "GetPumpDetails";
    public static String ADD_PUMPDETAILS = "InsertPumpDetails";
    public static String GET_SERVICE = "GetServiceType";
    public static String GEt_GUARANTEE = "GetGuaranteeWarrantyByModelNum";
    public static String INSERT_FEEDBACK="InsertFeedback";
    public static String SERVICE_TYPE="GetServiceType";
    public static String INSERT_INQUIRY="InsertServiceInquiry";
    public static String USER_VERIFICATION = "UserVerification";
    public static String FORGOT_PASSVERIFICATIONCODE="ForgotVerificationCode";
    public static String UPLOAD_VIDEO = "UploadVideoFiles";
    public static String LOGOUT = "Logout";
    public static String GET_PROFILE = "GetBasicUserProfile";
    public static String UPDATE_PROFILE = "UpdateUsers";
    public static String UPLOAD_PROFILEIMAGE = "UploadFileForProfileImage";
    public static String INSERT_SERVICETYPE = "InsertServiceType";
    public static String CHANGE_PASSWORD = "ChangePasswordForForgot";
    public static String FORGOTPASS_RESENDVERIFICATION = "ForgotVerificationCode";
    public static String GET_VIDEOS = "GetVideos";
    public static String INSERT_VIDEO = "InsertVideos";
    public static String GET_CATAGORY = "GetCategoriesFromMiscellaneous";
    public static String INSERT_MISCDATA = "InsertMiscellaneousData";
    public static String GET_MISCDATAFROMCATEGORY = "GetMiscellaneousDataByCategory";
    public static String INSERT_PRODUCT = "InsertProduct";
    public static String UPLOAD_PRODUCTIMAGE = "UploadProductImage";
    public static String UPLOAD_PRODUCTVIDEO = "UploadProductVideos";
    public static String GET_PRODUCTSBYCATEGORIES = "GetProductsByCategories";
    public static String GET_CUSTOMER = "GetCustomer";
    public static String GET_CUSTOMERTYPE = "GetCustomerType";
    public static String DELETE_CUSTOMER = "DeleteCustomer";
    public static String UPDATE_CUSTOMER = "UpdateCustomer";
    public static String GET_PRODUCTBYPRODUCTID="GetProductByProductID";
    public static String UPDATEPRODUCT="UpdateProduct";
    public static String DELETEPRODUCT="DeleteProduct";
    public static String GET_TCBYSERVICE="GetTermsandConditionByServiceTypeID";
    public static String INSERT_ORDERINQUIRY="InsertOrderInquiry";
    public static String GET_SERVICEINQUIRYBYID="GetServiceInquiryByID";
    public static String GETALLORDER_INQUIRY="GetAllOrderInquiry";
    public static String GET_ORDERINQUIRYBYID="GetOrderInquiryByID";
    public static String GET_ALLSERVICEINQUIRY="GetAllServiceInquiry";
    public static String ADD_CUSTOMER="UserRegistrationByAdministrator";
    public static String GET_ALLLITERATURE="GetAllLiterature";
    public static String UPLOADLITERATURE="UploadLiterature";
    public static String ORDERINQUIRYREPLY="OrderInquiryReply";
    public static String SERVICEINQUIRYREPLY="ServiceInquiryReply";
    public static String CATEGORYLIST="GetMiscellaneousDataByCategory";
    public static String UPDATE_CATEGORY="UpdateMiscellaneousData";
    public static String GET_ORDERREPLY="GetOrderInquiryReplyByID";
    public static String GET_SUPPORTREPLY="GetServiceInquiryReplyByID";
    public static String GET_LITERATUREBYPRODUCTID="GetLiteratureByProductID";
    public static String INSERT_ORDERREQUEST="InsertOrderRequest";
    public static String UPDATEUSERCART="UpdateUserCart";
    public static String DELETEUSERCARTBYUSERID="DeleteUserCartDetailsByUserID";
    public static String DELETEUSERWISHLISTBYID="DeleteUserWishListDetailsByUserID";
    public static String GET_SERVICE_INQUIRY_BYUSERID="GetServiceInquiryByUserID";
    public static String UPDATE_SHIPINGADDRESS="UpdateShippingAddress";
    public static String GET_PRODUCTBYPRODUCTYPEID="GetProductByProductTypeID";
    public static String GET_PUMPDETAILSBYUSERID="GetPumpDetailsByUserID";
    public static String INSERTMYORDER="InsertUserPlaceOrder";
    public static String INSERTMYORDERDETAILS="InsertUserPlaceOrderDetails";
    public static String GETORDERHISTORY="GetOrderHistoryByUserID";
    public static String GETORDERHISTORYBYORDERID="GetOrderHistoryByOrderID";
    public static String DELETEORDERSUMMAARY="DeleteUserOrderRequestDetailsByUserID";
    public static String GET_PRODUCTIMAGE="GetProductImageByProductID";
    public static String GETORDERSUMMARY="GetOrderSummary";
    public static String GETMYCARTCOUNT="GetUserCartProductCountByUserID";
    public static String GET_PRODUCTTYPE="GetProductType";
    public static String GETAREA="GetAreaByCity";
    public static String GETCITY="GetCityByCountry";
    public static String DELETE_MYORDER="UpdateUserPlaceOrderStatus";
    public static String GET_NOTIFICATION="GetAnnouncments";
    public static String SEND_HELP_QUERIES="SendQueriesMail";
    public static String GET_MODELNO="GetModelNum";
    public static String GETORDER_UPDATESTATUS="GetNotificationOrders";
    public static String GET_OFFERSBYUSERID="GetAllOfferByUserID";
    public static String GET_OFFERSPROMOTIONPRODUCTBYOFFERID="GetAllProductsByOfferID";
    public static String URLFORIMAGE="http://apex.skywardcrm.com/prakashpumpWebservice/";
    public static String GET_OFFERBYPRODUCTANDPRODUCTYPEID="GetAllOfferByProductIDProductTypeID";
    //public static String URLFORIMAGE="http://192.168.1.25:5090/";
    public static final String SOAP_ACTION = "http://tempuri.org/";

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static SoapSerializationEnvelope getSoapSerializationEnvelope(
            SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);

        return envelope;
    }

    public static HttpTransportSE getHttpTransportSE() {
        HttpTransportSE ht = new HttpTransportSE(URL);
        ht.debug = true;
        ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        return ht;
    }
    /**
     * Display the Toast message on top of the Screen.
     *
     * @param context Context of an activity
     * @param text    Text to be displayed in toast message
     */
    public static void displayToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        // toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    /**
     * Display a simple Alert Dialog with message.
     *
     * @param context Context of an activity
     * @param icon    Reference from drawable to be displayed as Alert Dialog's Icon
     * @param title   Text to be displayed as Alert Dialog's title
     * @param message Text to be displayed as Alert Dialog's message
     */
    public static void displayAlertDialog(Context context, int icon, String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        if (icon != 0) {
            alert.setIcon(icon);
        }
        if (title != null) {
            alert.setTitle(title);
        }
        alert.setMessage(message);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    public static void saveAuthToken(Context context, String authToken) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("authToken", authToken);
        editor.commit();
    }

    public static String getAuthToken(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "Authentication code from Shared " + sharedpreferences.getString("authToken", null));
        return sharedpreferences.getString("authToken", null);
    }


    public static void saveProductCategoryId(Context context, int productid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("productid", productid);
        editor.commit();
    }

    public static int getProductCategoryId(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getInt("productid", 0);
    }

    public static void saveProductSubCategoryId(Context context, int productid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("productidsub", productid);
        editor.commit();
    }
    public static int getOrderId(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getInt("orderid", 0);
    }

    public static void saveOrderId(Context context, int orderid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("orderid", orderid);
        editor.commit();
    }
    public static int getfromdashboardorcategory(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getInt("from", 0);
    }

    public static void savefromdashboardorcategory(Context context, int id) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("from", id);
        editor.commit();
    }
    public static String getmyordergrandtotal(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("grandtotal", null);
    }
    public static void savemyordergrandtotal(Context context, String grandtotal) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("grandtotal", grandtotal);
        editor.commit();
    }
    public static String getorderstatus(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("status", null);
    }
    public static void saveorderstatus(Context context, String status) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("status", status);
        editor.commit();
    }
    public static String getordercurrency(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("currency", null);
    }
    public static void saveordercurrency(Context context, String currency) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("currency", currency);
        editor.commit();
    }

    public static int getProductSubCategoryId(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getInt("productidsub", 0);
    }


    public static void saveProductId(Context context, int productid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("productidecomm", productid);
        editor.commit();
    }

    public static int getProductId(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getInt("productidecomm", 0);
    }
    public static void saveVerificationCode(Context context, String vcode) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("vcode", vcode);
        editor.commit();
    }

    public static String getVerificationCode(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "Verification code from Shared " + sharedpreferences.getString("vcode", null));
        return sharedpreferences.getString("vcode", null);
    }



    public static void saveUserName(Context context, String projectid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("username", projectid);
        editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "project id from shared " + sharedpreferences.getString("projectId", null));
        return sharedpreferences.getString("username", null);
    }


    public static void saveUserNamefordisplay(Context context, String projectid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("usernamedis", projectid);
        editor.commit();
    }

    public static String getUserNamefordisplay(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "project id from shared " + sharedpreferences.getString("projectId", null));
        return sharedpreferences.getString("usernamedis", null);
    }

    public static String getPassword(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "project id from shared " + sharedpreferences.getString("projectId", null));
        return sharedpreferences.getString("password", null);
    }

    public static void savePassword(Context context, String projectid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("password", projectid);
        editor.commit();
    }

    public static void saveUserType(Context context, String usertype) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("usertype", usertype);
        editor.commit();
    }

    public static String getUserType(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "usertype from shared " + sharedpreferences.getString("usertype", null));
        return sharedpreferences.getString("usertype", null);
    }

    public static void saveEmailid(Context context, String emailid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("emailid", emailid);
        editor.commit();
    }

    public static String getEmailid(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "usertype from shared " + sharedpreferences.getString("emailid", null));
        return sharedpreferences.getString("emailid", null);
    }
    public static void saveCategoryName(Context context, String authToken) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("catname", authToken);
        editor.commit();
    }

    public static String getCategoryName(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "Authentication code from Shared " + sharedpreferences.getString("authToken", null));
        return sharedpreferences.getString("catname", null);
    }

    public static void saveIsAdministrator(Context context, String admin) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("admin", admin);
        editor.commit();
    }

    public static String getIsAdministrator(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "usertype from shared " + sharedpreferences.getString("admin", null));
        return sharedpreferences.getString("admin", null);
    }


    public static void saveName(Context context, String name) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public static String getName(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "usertype from shared " + sharedpreferences.getString("name", null));
        return sharedpreferences.getString("name", null);
    }

    public static void savepumpmodelno(Context context, String pmo) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("pmo", pmo);
        editor.commit();
    }

    public static String getpumpmodelno(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "usertype from shared " + sharedpreferences.getString("pmo", null));
        return sharedpreferences.getString("pmo", null);
    }

    public static void savepumpserialno(Context context, String pser) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("pser", pser);
        editor.commit();
    }

    public static String getpumpserialno(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "usertype from shared " + sharedpreferences.getString("pser", null));
        return sharedpreferences.getString("pser", null);
    }


    public static void saveorderstatusnoti(Context context, ArrayList<String> lst) {
        Gson gson = new Gson();
        String json = gson.toJson(lst);
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("gift", json);
        editor.commit();
    }
    public static ArrayList<String> getorderstatusnoti(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        //  Log.v("", "Authentication code from Shared " + sharedpreferences.getString("authToken", null));
        Gson gson = new Gson();
        String json = sharedpreferences.getString("gift",null);
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        ArrayList<String> arrlist=new ArrayList<>();
        arrlist= gson.fromJson(json, type);

        return arrlist;
    }



    /**
     * Get current date in dd/mm/yyyy format
     *
     * @return the current date
     */
    public static String getFormattedDate(int year, int month, int dayOfMonth) throws Exception {
        return getFormattedDate(dayOfMonth + "/" + month + "/" + year, "dd/MM/yyyy");
    }

    public static String getFormattedDate(String strToFormat, String baseFormat) throws Exception {
        DateFormat inFormat = new SimpleDateFormat(baseFormat);
        DateFormat outFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return outFormat.format(inFormat.parse(strToFormat));
    }

    public static String getFormattedTime(int hour, int minute) {
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("hh mm");
        return new SimpleDateFormat("hh:mm aa"). dateFormat.parse(hour+" "+minute);*/
        try {
            DateFormat f1 = new SimpleDateFormat("HH:mm:ss");
            Date d = f1.parse(hour + ":" + minute + ":00");
            DateFormat f2 = new SimpleDateFormat("HH:mm");
            return f2.format(d).toUpperCase();
        } catch (Exception e) {
            return getCurrentTime();
        }

    }

    /**
     * Get current time in hh:mm AM/PM format
     *
     * @return the current time
     */
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        return dateFormat.format(new Date()).toString();
    }

    public static String getFormattedTime(String strToFormat, String inputFormat, String outputFormat) throws Exception {
        DateFormat inFormat = new SimpleDateFormat(inputFormat);
        DateFormat outFormat = new SimpleDateFormat(outputFormat);
        return outFormat.format(inFormat.parse(strToFormat));
    }

    /**
     * @param context Context of an activity which is calling this method
     * @return returns height of device
     */
    public static int getDeviceHeight(Activity context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    /**
     * @param context Context of an activity which is calling this method
     * @return returns width of device
     */
    public static int getDeviceWidth(Activity context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static String getBase64(Bitmap bmp) {

       final int lnth=bmp.getByteCount();
        ByteBuffer dst= ByteBuffer.allocate(lnth);
        bmp.copyPixelsToBuffer( dst);
        byte[] barray=dst.array();
       /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 70, stream);
        byte[] image = stream.toByteArray();*/
        return Base64.encodeToString(barray, Base64.DEFAULT);
    }


    public static void saveorderidorder(Context context, String authToken) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("orderidorder", authToken);
        editor.commit();
    }

    public static String getorderidorder(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "Authentication code from Shared " + sharedpreferences.getString("authToken", null));
        return sharedpreferences.getString("orderidorder", null);
    }
    public static void saveshippingaddress(Context context, String authToken) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("shippingaddress", authToken);
        editor.commit();
    }

    public static String getshippingaddress(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "Authentication code from Shared " + sharedpreferences.getString("authToken", null));
        return sharedpreferences.getString("shippingaddress", null);
    }
    public static void savetotal(Context context, String authToken) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("total", authToken);
        editor.commit();
    }

    public static String gettotal(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "Authentication code from Shared " + sharedpreferences.getString("authToken", null));
        return sharedpreferences.getString("total", null);
    }

    public static void savedisplayquantitity(Context context, int qty) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("displayquantitity", qty);
        editor.commit();
    }

    public static String getemailid(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("emailid", null);
    }

    public static void saveemailid(Context context, String email) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("emailid", email);
        editor.commit();
    }

    public static int getdisplayquantitity(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "Authentication code from Shared " + sharedpreferences.getString("authToken", null));
        return sharedpreferences.getInt("displayquantitity", 1);
    }


    public static void savefromwhichordersummary(Context context, int id) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("fromwhichordersummary", id);
        editor.commit();
    }

    public static int getfromwhichordersummary(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        Log.v("", "Authentication code from Shared " + sharedpreferences.getString("authToken", null));
        return sharedpreferences.getInt("fromwhichordersummary", 1);
    }


    public static int getnoticount(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        //  Log.v("", "Authentication code from Shared " + sharedpreferences.getString("authToken", null));
        return sharedpreferences.getInt("count", 0);
    }


    public static void savenoticount(Context context, int count) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("count", count);
        editor.commit();
    }

    public static void saveOfferId(Context context, int OfferId) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("OfferId", OfferId);
        editor.commit();
    }

    public static int getOfferId(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getInt("OfferId", 0);
    }
    public static String getofferimagepath(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("offerimagepath", null);
    }

    public static void saveofferimagepath(Context context, String offerimagepath) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("offerimagepath", offerimagepath);
        editor.commit();
    }

    public static void saveCallUser(Context context, String cuser) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("cuser", cuser);
        editor.commit();
    }

    public static String getCallUser(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("cuser", null);
    }

    public static void savefromviewproduct(Context context, String fromv) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("fromv", fromv);
        editor.commit();
    }

    public static String getfromviewproduct(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("fromv", null);
    }



    public static void saveaddr1(Context context, String str) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("addr1", str);
        editor.commit();
    }

    public static String getaddr1(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("addr1", null);
    }

    public static void saveaddr2(Context context, String str) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("addr2", str);
        editor.commit();
    }

    public static String getaddr2(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("addr2", null);
    }
    public static void savecountry(Context context, String str) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("country", str);
        editor.commit();
    }

    public static String getcountry(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("country", null);
    }
    public static void savecity(Context context, String str) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("city", str);
        editor.commit();
    }

    public static String getcity(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("city", null);
    }
    public static void savearea(Context context, String str) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("area", str);
        editor.commit();
    }

    public static String getarea(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("area", null);
    }
    public static void savemobileno(Context context, String str) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("mobileno", str);
        editor.commit();
    }

    public static String getmobileno(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("mobileno", null);
    }


    public static void saveemail(Context context, String str) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email", str);
        editor.commit();
    }

    public static String getemail(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("email", null);
    }
    public static void savepincode(Context context, String str) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("pincode", str);
        editor.commit();
    }

    public static String getpincode(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getString("pincode", null);
    }
    public static void savecountryid(Context context, int id) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("countryid", id);
        editor.commit();
    }

    public static int getcountryid(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getInt("countryid", 0);
    }

    public static void savecityid(Context context, int id) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("cityid", id);
        editor.commit();
    }

    public static int getcityid(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getInt("cityid", 0);
    }


    public static void saveareaid(Context context, int id) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("areaid", id);
        editor.commit();
    }

    public static int getareaid(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        return sharedpreferences.getInt("areaid", 0);
    }
}
