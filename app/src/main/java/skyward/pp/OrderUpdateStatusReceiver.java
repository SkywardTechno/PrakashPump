package skyward.pp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OrderUpdateStatusReceiver extends BroadcastReceiver {
    public OrderUpdateStatusReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent background = new Intent(context,OrderUpdateStatusService.class);
        context.startService(background);
    }
}
