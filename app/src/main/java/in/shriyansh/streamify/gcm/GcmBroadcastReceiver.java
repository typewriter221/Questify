package in.shriyansh.streamify.gcm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;


/**
 * Created by shriyansh on 9/9/15.
 * TODO : Deprecated
 */

    public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
//        ComponentName comp = new ComponentName(context.getPackageName(),
//                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
//        startWakefulService(context, (intent.setComponent(comp)));
//        setResultCode(Activity.RESULT_OK);
    }
}

