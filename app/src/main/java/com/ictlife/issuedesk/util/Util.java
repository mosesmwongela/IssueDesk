package com.ictlife.issuedesk.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {


    public static String formatISODate(String iso_date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            Date date1 = dateFormat.parse(iso_date);

            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return dateFormat2.format(date1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return iso_date;
    }

    public static String formatISOTime(String iso_date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            Date date1 = dateFormat.parse(iso_date);

            SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm a", Locale.US);
            return dateFormat2.format(date1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return iso_date;
    }

    public static String generalFormatDateTime(String time, String date) {
        String date_time = "";
        try {
            date_time = time
                    + ", " +
                    FormatDate(date, "yyyy-MM-dd", "EEE")
                    + " "
                    + FormatDate(date, "yyyy-MM-dd", "dd")
                    + dayOfMonthSuffix(Integer.parseInt(FormatDate(date, "yyyy-MM-dd", "dd")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date_time;
    }

    public static String FormatDate(String date, String format_from, String format_to) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format_from, Locale.US);
            Date date1 = dateFormat.parse(date);

            SimpleDateFormat dateFormat2 = new SimpleDateFormat(format_to, Locale.US);
            return dateFormat2.format(date1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String dayOfMonthSuffix(final int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }
}
