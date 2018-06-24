package in.shriyansh.streamify.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shriyansh on 11/10/15.
 */
public class Utils {
    public static long convertStringTimeToTimestamp(String dateString,String format){
        DateFormat formatter = new SimpleDateFormat(format);
        Date date;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return date.getTime() / TimeUtils.MILLIS_IN_SECOND;
    }
    public static final String getUsableDropboxUrl(String url){
        return url.replace("www.dropbox.com","dl.dropboxusercontent.com");

    }

    public static final String getYoutubeVideoThumbnailFromId(String videoId){
        return "https://img.youtube.com/vi/"+videoId+"/mqdefault.jpg";
    }

    public static final long  settleTimeZoneDifference(long timstamp){
        return timstamp + TimeUtils.SECONDS_IN_INDIAN_OFFSET;
    }

}
