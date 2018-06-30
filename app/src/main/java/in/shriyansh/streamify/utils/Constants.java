package in.shriyansh.streamify.utils;

import in.shriyansh.streamify.BuildConfig;

import java.util.concurrent.TimeUnit;

/**
 * Created by shriyansh on 9/9/15.
 */
public class Constants {
    public static final String YOUTUBE_DEVELOPER_KEY = BuildConfig.YoutubeApiKey;
    public static final String GOOGLE_MAPS_ANDROID_API_KEY = BuildConfig.GoogleMapAndroidApiKey;

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.shriyansh.sntc.gcm.DISPLAY_MESSAGE";

    public static final String LARAVEL_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final int INSTRUCTIONS_RECORD_ID = 100000;

    public static final long SPLASH_SCREEN_DURATION = TimeUnit.MILLISECONDS.convert(1L,
        TimeUnit.SECONDS);

    /**
     * Constants for Volley.
     */
    public static final String HTTP_HEADER_CONTENT_TYPE_KEY = "Content-Type";
    public static final String HTTP_HEADER_CONTENT_TYPE_JSON = "application/json";
    public static final int HTTP_INITIAL_TIME_OUT = 10000;
    public static final int HTTP_RETRIES = 0;

    public static final String FEEDBACK_PARAM_USER_ID = "user_id";
    public static final String FEEDBACK_PARAM_TEXT = "text";

    public static final String STREAM_PARAM_USER_ID = "user_id";

    public static final String NOTIFICATION_PARAM_USER_ID = "user_id";
    public static final String NOTIFICATION_PARAM_LAST_NOTIFICATION_ID = "last_notification_id";

    public static final String EVENT_PARAM_USER_ID = "user_id";
    public static final String EVENT_PARAM_LAST_EVENT_ID = "last_event_id";

    public static final String POST_PARAM_USER_ID = "user_id";
    public static final String POST_PARAM_TYPE = "type";
    public static final String POST_PARAM_TITLE = "title";
    public static final String POST_PARAM_CONTENT = "content";

    public static final String STREAM_FEEDBACK_PARAM_USER_ID = "user_id";
    public static final String STREAM_FEEDBACK_PARAM_STREAM_ID = "stream_id";
    public static final String STREAM_FEEDBACK_PARAM_TEXT = "text";

    public static final String STREAM_SUBSCRIBE_PARAM_USER_ID = "user_id";
    public static final String STREAM_SUBSCRIBE_PARAM_STREAM_ID = "stream_id";

    /**
     * Intent extra keys.
     */
    public static final String INTENT_EXTRA_KEY_CALENDAR_BEGIN_TIME = "beginTime";
    public static final String INTENT_EXTRA_KEY_CALENDAR_END_TIME = "endTime";
    public static final String INTENT_EXTRA_KEY_CALENDAR_ALL_DAY = "allDay";
    public static final String INTENT_EXTRA_KEY_CALENDAR_TITLE = "title";
    public static final String INTENT_EXTRA_KEY_CALENDAR_DESCRIPTION = "description";
    public static final String INTENT_EXTRA_KEY_CALENDAR_EVENT_LOCATION = "eventLocation";

    /**
     * JSON Constants for response.
     */
    public static final String RESPONSE_STATUS_KEY = "status";
    public static final String RESPONSE_STATUS_VALUE_OK = "OK";

    public static final String JSON_KEY_AUTHOR_NAME = "name";
    public static final String JSON_KEY_AUTHOR_EMAIL = "email";
    public static final String JSON_KEY_AUTHOR_IMAGE_URL = "image";
    public static final String JSON_KEY_AUTHOR_CONTACT = "contact";

    public static final String JSON_KEY_LOCATION_NAME = "name";
    public static final String JSON_KEY_LOCATION_ADDRESS = "address";
    public static final String JSON_KEY_LOCATION_DESCRIPTION = "description";
    public static final String JSON_KEY_LOCATION_LATITUDE = "latitude";
    public static final String JSON_KEY_LOCATION_LONGITUDE = "longitude";

    public static final String JSON_KEY_STREAM_TITLE = "title";

    public static final String JSON_KEY_POSITION_HOLDER_NAME = "name";
    public static final String JSON_KEY_POSITION_HOLDER_EMAIL = "email";
    public static final String JSON_KEY_POSITION_HOLDER_IMAGE_URL = "image";
    public static final String JSON_KEY_POSITION_HOLDER_CONTACT = "contact";
    public static final String JSON_KEY_POSITION_HOLDER_POST = "position";

    public static final String JSON_KEY_PARENT_NAME = "name";
}
