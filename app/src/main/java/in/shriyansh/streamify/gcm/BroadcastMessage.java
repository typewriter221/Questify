package in.shriyansh.streamify.gcm;

import android.content.Context;
import android.content.Intent;

/**
 * TODO : Deprecated
 */
public class BroadcastMessage {
 
    public static final String DISPLAY_MESSAGE_ACTION =
            "in.shriyansh.questify.gcm.DISPLAY_MESSAGE";
 
    public static final String EXTRA_MESSAGE = "message";
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void broadcastMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

}
