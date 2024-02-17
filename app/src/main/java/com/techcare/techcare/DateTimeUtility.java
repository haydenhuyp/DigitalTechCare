package com.techcare.techcare;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtility {
    /**
     * Get the current date and time in the format "MMM dd, yyyy. hh:mm a"
     * @return the current date and time
     */
    public static String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy. hh:mm a");
        return simpleDateFormat.format(calendar.getTime());
    }
}
