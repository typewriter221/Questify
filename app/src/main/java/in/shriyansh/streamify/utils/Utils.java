package in.shriyansh.streamify.utils;

import in.shriyansh.streamify.network.Urls;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shriyansh on 11/10/15.
 */
public class Utils implements Urls {
    /**
     * Converts time in given string format to timestamp.
     *
     * @param dateString    Time string
     * @param format        String format
     * @return              Timestamp
     */
    public static long convertStringTimeToTimestamp(String dateString,String format) {
        DateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return date.getTime() / TimeUtils.MILLIS_IN_SECOND;
    }

    /**
     * Gets displayable url from dropbox content url.
     *
     * @param url   Dropbox URL
     * @return      Usable URL
     */
    public static String getUsableDropboxUrl(String url) {
        return url.replace(DROPBOX_URL,DROPBOX_CONTENT_URL);
    }

    /**
     * Gets youtube thumbnail from youtube video Id.
     *
     * @param videoId   Youtube video Id
     * @return          Thumbnail URL
     */
    public static String getYoutubeVideoThumbnailFromId(String videoId) {
        return YOUTUBE_SLATE_URL + "/vi/" + videoId + "/mqdefault.jpg";
    }

    /**
     * Offsets time zone difference in time.
     *
     * @param timestamp     Current timestamp
     * @return              Time with indian time zone offset
     */
    public static long  settleTimeZoneDifference(long timestamp) {
        return timestamp + TimeUtils.SECONDS_IN_INDIAN_OFFSET;
    }
}
