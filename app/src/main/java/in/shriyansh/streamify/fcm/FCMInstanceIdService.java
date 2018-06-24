package in.shriyansh.streamify.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import in.shriyansh.streamify.utils.PreferenceUtils;

/**
 * FCM Instance to receive FCM Token
 *
 */

public class FCMInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "FCM Instance Id Service";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        PreferenceUtils.setStringPreference(this,PreferenceUtils.PREF_FCM_TOKEN,
                refreshedToken);
        Log.d(TAG,refreshedToken);
    }
}
