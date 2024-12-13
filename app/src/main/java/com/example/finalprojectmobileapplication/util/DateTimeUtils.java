package com.example.finalprojectmobileapplication.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    private static final String DEFAULT_FORMAT_DATE = "dd-MM-yyyy";
    private static final String DEFAULT_FORMAT_DATE_2 = "dd/MM/yyyy, hh:mm a";

    public static String getDateToday() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_FORMAT_DATE, Locale.ENGLISH);
        return df.format(c.getTime());
    }
    public static long getLongCurrentTimeStamp() {
        return convertDateToTimeStamp(getDateToday());
    }

    public static long convertDateToTimeStamp(String strDate) {
        String result = "0";
        if (strDate != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT_DATE, Locale.ENGLISH);
                Date date = format.parse(strDate);
                if (date != null) {
                    long timestamp = date.getTime() / 1000;
                    result = String.valueOf(timestamp);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return Long.parseLong(result);
    }

    public static String convertTimeStampToDate(String strTimeStamp) {
        String result = "";
        if (strTimeStamp != null) {
            try {
                float floatTimestamp = Float.parseFloat(strTimeStamp);
                long timestamp = (long) floatTimestamp;
                SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATE_2, Locale.ENGLISH);
                Date date = (new Date(timestamp));
                result = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
