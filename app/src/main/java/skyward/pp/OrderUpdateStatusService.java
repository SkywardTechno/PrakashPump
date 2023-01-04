package skyward.pp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import skyward.pp.ecommerce.MyOrders;
import skyward.pp.util.Utility;

public class OrderUpdateStatusService extends Service {

    private Context context;
    private boolean isRunning;
    private Thread backgroundThread;
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    private NotificationManager mManager;
    Notification notification;
    String OrderID,OrderCode;
    ArrayList<String> orderid;


    public OrderUpdateStatusService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
        System.out.println("inside service");
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            if(Utility.isInternetConnected(context)){
                getNotification task = new getNotification();
                task.execute();
            }

            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;


    }

    class getNotification extends AsyncTask<Void, Void, SoapObject> {
        SoapObject result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected SoapObject doInBackground(Void... arg0) {

            SoapSerializationEnvelope mySoapEnvelop = null;
            HttpTransportSE myAndroidHttpTransport;

            SoapObject request = new SoapObject(Utility.NAMESPACE,
                    Utility.GETORDER_UPDATESTATUS);

            mySoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            mySoapEnvelop.dotNet = true;

            request.addProperty("token", Utility.getAuthToken(getApplicationContext()));

            System.out.println(Utility.URL);
            System.out.println(request);
            mySoapEnvelop.setOutputSoapObject(request);

            try {
                try {
                    myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
                    myAndroidHttpTransport.call(Utility.SOAP_ACTION
                            + Utility.GETORDER_UPDATESTATUS, mySoapEnvelop);

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

            if (result != null) {

                try {

                    if (!Utility.isInternetConnected(getApplicationContext())) {
                       // Toast.makeText(context, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    } else {

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

                            orderid = new ArrayList<>();
                            for (int i = 0; i < count; i++) {
                                SoapObject soapResult = null;
                                soapResult = (SoapObject) result4.getProperty(i);
                                int id = Integer.parseInt(soapResult.getPrimitivePropertyAsString("ID").toString());
                                mManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                                Intent intent1 = new Intent(getApplicationContext(), MyOrders.class);

                                notification = new Notification(R.drawable.logo_eng, getResources().getString(R.string.app_name), System.currentTimeMillis());
                                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                PendingIntent pendingNotificationIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                                //notification.setLatestEventInfo(getApplicationContext(), "AlarmManagerDemo", "This is a test message!", pendingNotificationIntent);

                                //mManager.notify(0, notification);
                                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                                        getApplicationContext()).setSmallIcon(R.drawable.logo_eng)
                                        .setContentTitle(getResources().getString(R.string.app_name))
                                        .setContentText("Your OrderID - "+soapResult.getPrimitivePropertySafelyAsString("OrderCode").toString() + " is \n" + soapResult.getPrimitivePropertySafelyAsString("OrderStatusName").toString()).setSound(alarmSound)
                                        .setAutoCancel(true)
                                        .setSound(alarmSound)
                                        .setContentIntent(pendingNotificationIntent)
                                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

                                mManager.notify(id, mNotifyBuilder.build());

                                orderid.add(soapResult.getPrimitivePropertyAsString("ID").toString());

                            }

                            Utility.saveorderstatusnoti(getApplicationContext(),orderid);

                        } else {
                            orderid = new ArrayList<>();
                            Utility.saveorderstatusnoti(getApplicationContext(),orderid);
                           /* Toast.makeText(getApplicationContext(),
                                    soapObject.getProperty("ErrorMessage").toString(),
                                    Toast.LENGTH_SHORT).show();*/

                        }


                    }

                } catch (NullPointerException e) {

                    e.printStackTrace();

                } catch (ArrayIndexOutOfBoundsException e) {

                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {

              //  Toast.makeText(context, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
