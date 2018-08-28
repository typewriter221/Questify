package in.shriyansh.streamify.network;

/**
 * Created by shriyansh on 17/8/15.
 */
public interface Urls {
    String HOST = "https://sntc-streamify.herokuapp.com//";
    String DROPBOX_URL = "www.dropbox.com";
    String DROPBOX_CONTENT_URL = "dl.dropboxusercontent.com";
    String YOUTUBE_SLATE_URL = "https://img.youtube.com";

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
    String CREATE_TEAM = HOST + "app/create_team";
    String ADD_MEMBERS = HOST + "app/add_members";
    String GET_PAST_TEAMS = HOST + "app/get_my_teams";
    String LIST_ALL_EVENTS = HOST + "app/listAllEvents";
    String LIST_ALL_TEAMS = HOST + "app/listAllTeams";
    String LIST_ALL_STREAMS = HOST + "app/listAllStreams";
    String LOGIN_URL = HOST + "app/login";
    String EVENT_NOTIFICATION_URL = HOST + "app/createEvent";

    String DEV_SHRIYANSH_IMAGE = HOST + "images/shriyansh.jpg";
    String DEV_YASH_IMAGE = HOST + "images/yash.jpg";
    String DEV_HEMANT_IMAGE = HOST + "images/hemant.jpg";
    String DEV_SATYA_IMAGE = HOST + "images/satya.jpg";

    int FCM_TYPE_NOTIFICATION = 1;
    int FCM_TYPE_EVENT = 2;
}
