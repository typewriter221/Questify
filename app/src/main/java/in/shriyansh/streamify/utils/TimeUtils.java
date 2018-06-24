package in.shriyansh.streamify.utils;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static final Long MILLIS_IN_SECOND
            = TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS);
    public static final Long SECONDS_IN_DAY
            = TimeUnit.SECONDS.convert(1,TimeUnit.DAYS);
    public static final Long SECONDS_IN_WEEK
            = TimeUnit.SECONDS.convert(7, TimeUnit.DAYS);
    public static final double SECONDS_IN_MONTH
            = TimeUnit.SECONDS.convert(30, TimeUnit.DAYS);
    public static final long SECONDS_IN_INDIAN_OFFSET
            = TimeUnit.SECONDS.convert(5, TimeUnit.HOURS)
            + TimeUnit.SECONDS.convert(30, TimeUnit.MINUTES);
    public static final String DB_TIME_FORMAT = "MMM d, h:mm a";
    public static  final String TIME_ZONE_INDIA = "GMT+5.30";

    /**
     * Converts timestamp to "ago" or "to go" human friendly string
     * @param timeStamp
     * @return
     */
    public static String ago(long timeStamp) {
        long timeDifference;
        long unixTime = System.currentTimeMillis() / MILLIS_IN_SECOND;  //get current time in seconds.
        int j;
        String[] periods = {" second", " minute", " hour", " day", " week", " month", " year", " decade"};
        String[] mulperiods = {" seconds", " minutes", " hours", " days", " weeks", " months", " years", " decades"};
        // you may choose to write full time intervals like seconds, minutes, days and so on
        double[] lengths = {60, 60, 24, 7, 4.35, 12, 10};
        timeDifference = unixTime - timeStamp;
        String tense="";
        if(timeDifference<0){
            tense="to go";
            timeDifference=-timeDifference;
        }else{
            tense = "ago";
        }

        for (j = 0; timeDifference >= lengths[j] && j < lengths.length - 1; j++) {
            timeDifference /= lengths[j];
        }
        if(periods[j].contentEquals("second")){
            return "few " + mulperiods[j] + " " + "ago";
        }else{
            if(timeDifference==1){
                return timeDifference + periods[j] + " " + tense;
            }else{
                return timeDifference + mulperiods[j] + " " + tense;
            }
        }
    }
}
