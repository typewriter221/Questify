package in.shriyansh.streamify.database;

import android.provider.BaseColumns;

/**
 * Created by shriyansh on 6/10/15.
 */
public class DbContract {
    public static final String DB_NAME = "stremify.db";
    public static final int DB_VERSION = 8;

    private static final String INTEGER = " INTEGER ";
    private static final String TEXT = " TEXT ";
    private static final String REAL = " REAL ";
    private static final String NOT_NULL = " NOT NULL ";
    private static final String PRIMARY_KEY = " PRIMARY KEY ";
    private static final String AUTOINCREMENT = " AUTOINCREMENT ";

    public static final class Streams implements BaseColumns {
        public static final String TABLE_STREAMS = "tbl_streams";
        public static final String COLUMN_GLOBAL_ID = "col_global_id";
        public static final String COLUMN_TITLE = "col_title";
        public static final String COLUMN_SUBTITLE = "col_subtitle";
        public static final String COLUMN_DESCRIPTION = "col_description";
        public static final String COLUMN_IMAGE = "col_image";
        public static final String COLUMN_AUTHOR = "col_author";
        public static final String COLUMN_PARENT_BODIES = "col_parent_bodies";
        public static final String COLUMN_POSITION_HOLDERS = "col_position_holders";
        public static final String COLUMN_IS_SUBSCRIBED = "col_is_subscribed";
        public static final String COLUMN_CREATED_AT = "col_create_at";

        public static final int VALUE_SUBSCRIBED = 1;
        public static final int VALUE_NOT_SUBSCRIBED = 0;

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_STREAMS + " ( "
                + _ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + ", "
                + COLUMN_GLOBAL_ID + INTEGER + NOT_NULL + ", "
                + COLUMN_TITLE + TEXT + NOT_NULL + ", "
                + COLUMN_SUBTITLE + TEXT + ", "
                + COLUMN_DESCRIPTION + TEXT + ", "
                + COLUMN_IMAGE + TEXT + ", "
                + COLUMN_AUTHOR + TEXT + ", "
                + COLUMN_PARENT_BODIES + TEXT + ", "
                + COLUMN_POSITION_HOLDERS + TEXT + ", "
                + COLUMN_IS_SUBSCRIBED + INTEGER + ", "
                + COLUMN_CREATED_AT + INTEGER
                + " ); ";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_STREAMS;
    }

    /**
     * Images and past events, points of interest,activities.
     *
     */
    public static final class Notifications implements BaseColumns {
        public static final String TABLE_NOTIFICATIONS = "tbl_notifications";
        public static final String COLUMN_GLOBAL_ID = "col_global_id";
        public static final String COLUMN_TITLE = "col_title";
        public static final String COLUMN_SUBTITLE = "col_subtitle";
        public static final String COLUMN_DESCRIPTION = "col_description";
        public static final String COLUMN_LINK = "col_link";
        public static final String COLUMN_TYPE = "col_type";
        public static final String COLUMN_TIME = "col_time";
        public static final String COLUMN_IMAGE = "col_image";
        public static final String COLUMN_CREATED_AT = "col_created_at";

        public static final String COLUMN_STREAM = "col_stream";
        public static final String COLUMN_TAGS = "col_tags";
        public static final String COLUMN_AUTHOR = "col_author";
        public static final String COLUMN_CONTENTS = "col_contents";

        public static final String COLUMN_LIKES = "col_likes";
        public static final String COLUMN_DISLIKES = "col_dislikes";
        public static final String COLUMN_USER_LIKE_TYPE = "col_user_like_type";

        public static final int VALUE_TYPE_TEXT = 1;
        public static final int VALUE_TYPE_IMAGE = 2;
        public static final int VALUE_TYPE_VIDEO = 3;

        public static final int VALUE_LIKE_TYPE_LIKE = 1;
        public static final int VALUE_LIKE_TYPE_DISLIKE = 2;
        public static final int VALUE_LIKE_TYPE_NEUTRAL = 3;


        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + " ( "
                + _ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + ", "
                + COLUMN_GLOBAL_ID + INTEGER + NOT_NULL + ", "
                + COLUMN_TITLE + TEXT + NOT_NULL + ", "
                + COLUMN_SUBTITLE + TEXT + ", "
                + COLUMN_DESCRIPTION + TEXT + ", "
                + COLUMN_LINK + TEXT + ", "
                + COLUMN_TYPE + INTEGER + ", "
                + COLUMN_TIME + INTEGER + ", "
                + COLUMN_IMAGE + TEXT + ", "
                + COLUMN_STREAM + TEXT + ", "
                + COLUMN_TAGS + TEXT + ", "
                + COLUMN_AUTHOR + TEXT + ", "
                + COLUMN_CONTENTS + TEXT + ", "
                + COLUMN_LIKES + INTEGER + ", "
                + COLUMN_DISLIKES + INTEGER + ", "
                + COLUMN_USER_LIKE_TYPE + INTEGER + ", "
                + COLUMN_CREATED_AT + INTEGER
                + " ); ";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS;
    }

    /**
     * Notifications.
     */
    public static final class Contents implements BaseColumns {
        public static final String TABLE_CONTENTS = "tbl_contents";
        public static final String COLUMN_GLOBAL_ID = "col_global_id";
        public static final String COLUMN_PARENT_ID = "col_parent_id";
        public static final String COLUMN_TITLE = "col_title";
        public static final String COLUMN_TEXT = "col_text";
        public static final String COLUMN_IMAGE = "col_image";
        public static final String COLUMN_VIDEO_ID = "col_video_id";
        public static final String COLUMN_TYPE = "col_type";
        public static final String COLUMN_URL = "col_url";
        public static final String COLUMN_STREAM = "col_stream";
        public static final String COLUMN_CREATED_AT = "col_create_at";

        public static final int VALUE_TYPE_IMAGE = 2;
        public static final int VALUE_TYPE_VIDEO = 3;

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTENTS + " ( "
                + _ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + ", "
                + COLUMN_GLOBAL_ID + INTEGER + NOT_NULL + ", "
                + COLUMN_PARENT_ID + INTEGER + NOT_NULL + ", "
                + COLUMN_TITLE + TEXT + NOT_NULL + ", "
                + COLUMN_TEXT + TEXT + ", "
                + COLUMN_TYPE + INTEGER + ", "
                + COLUMN_IMAGE + TEXT + ", "
                + COLUMN_VIDEO_ID + TEXT + ", "
                + COLUMN_URL + TEXT + ", "
                + COLUMN_STREAM + TEXT + ", "
                + COLUMN_CREATED_AT + INTEGER
                + " ); ";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_CONTENTS;
    }

    /**
     * Events.
     */
    public static final class Events implements BaseColumns {
        public static final String TABLE_EVENTS = "tbl_events";
        public static final String COLUMN_GLOBAL_ID = "col_global_id";
        public static final String COLUMN_TITLE = "col_title";
        public static final String COLUMN_SUBTITLE = "col_subtitle";
        public static final String COLUMN_DESCRIPTION = "col_description";
        public static final String COLUMN_IMAGE = "col_image";
        public static final String COLUMN_TIME = "col_time";
        public static final String COLUMN_CREATED_AT = "col_created_at";

        public static final String COLUMN_STREAM = "col_stream";
        public static final String COLUMN_TAGS = "col_tags";
        public static final String COLUMN_AUTHOR = "col_author";
        public static final String COLUMN_VENUE = "col_venue";

        public static final String COLUMN_IS_USER_ATTENDING = "col_is_user_attending";
        public static final String COLUMN_ATTENDING_COUNT = "col_attending_count";


        public static final int VALUE_USER_ATTENDING = 1;
        public static final int VALUE_USER_NOT_ATTENDING = 2;
        public static final int VALUE_USER_NEUTRAL = 3;

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_EVENTS + " ( "
                + _ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + ", "
                + COLUMN_GLOBAL_ID + INTEGER + NOT_NULL + ", "
                + COLUMN_TITLE + TEXT + NOT_NULL + ", "
                + COLUMN_SUBTITLE + TEXT + ", "
                + COLUMN_DESCRIPTION + TEXT + ", "
                + COLUMN_IMAGE + TEXT + ", "
                + COLUMN_TIME + INTEGER + ", "
                + COLUMN_STREAM + TEXT + ", "
                + COLUMN_TAGS + TEXT + ", "
                + COLUMN_AUTHOR + TEXT + ", "
                + COLUMN_VENUE + TEXT + ", "
                + COLUMN_IS_USER_ATTENDING + INTEGER + ", "
                + COLUMN_ATTENDING_COUNT + INTEGER + ", "
                + COLUMN_CREATED_AT + INTEGER
                + " ); ";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_EVENTS;
    }
}
