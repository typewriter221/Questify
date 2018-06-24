package in.shriyansh.streamify.network;

/**
 * Created by shriyansh on 17/8/15.
 */
public interface URLs {
    //public static final String IP = "http://foodfanta.com/";
    String HOST = "http://sntc.online/";
    //public static final String HOST = "http://192.168.43.201:8080/";

    String REGISTER_URL = HOST + "app/register";
    String FCM_UPDATE = HOST + "fcm_update";
    String GET_STREAMS = HOST + "app/get_streams";
    String GET_NOTIFICATIONS = HOST + "app/get_notifications";
    String GET_EVENTS = HOST + "app/get_events";
    String SUBSCRIBE_STREAM = HOST + "app/subscribe";
    String UN_SUBSCRIBE_STREAM = HOST + "app/unsubscribe";
    String FEEDBACK = HOST + "app/feedback";
    String APP_FEEDBACK = HOST + "app/app_feedback";
    String POST = HOST + "app/post";
    String PUT_URL = HOST + "put.php";

    String DEV_SHRIYANSH_IMAGE = HOST + "images/shriyansh.jpg";
    String DEV_YASH_IMAGE = HOST + "images/yash.jpg";
    String DEV_HEMANT_IMAGE = HOST + "images/hemant.jpg";
    String DEV_SATYA_IMAGE = HOST + "images/satya.jpg";


    int FCM_TYPE_NOTIFICATION = 1;
    int FCM_TYPE_EVENT = 2;
}
