package skyward.pp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }
/*
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "Message arrived", Toast.LENGTH_LONG).show();

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            Intent i = new Intent(context, VerificationcodeActivity.class);
context.startService(i);

        }

    }
*/

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Message arrived", Toast.LENGTH_LONG).show();

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            Intent i = new Intent(context, VerificationcodeActivity.class);
            context.startService(i);

        }
    }
}
