package skyward.pp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SinchReceiver extends BroadcastReceiver {
    public SinchReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        System.out.println("Inside Receiver");
        Intent background = new Intent(context,SinchService.class);
        context.startService(background);
    }
}
