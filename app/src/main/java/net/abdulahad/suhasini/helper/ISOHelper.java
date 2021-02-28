package net.abdulahad.suhasini.helper;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class ISOHelper {

    public static String getISODate() {
        SimpleDateFormat format = new SimpleDateFormat("Y-MM-d", Locale.getDefault());
        return format.format(System.currentTimeMillis());
    }

    public static String getISODate(int year, int month, int dayOfMonth) {
        month++;
        String mon = (month < 9) ? "0" + month : String.valueOf(month);
        String day = (dayOfMonth < 9) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
        return year + "-" + mon + "-" + day;
    }

    public static String getISODate(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        return getISODate(year, month, dayOfMonth);
    }

    public static String getTime(ZonedDateTime zonedDateTime) {
        return DateTimeFormatter.ofPattern("hh:mm a").format(zonedDateTime);
    }

    public static String dateMonHourMin(ZonedDateTime zonedDateTime) {
        return DateTimeFormatter.ofPattern("dd MMM\n hh:mm a").format(zonedDateTime);
    }

    public static String fullyQualifiedDate(Calendar calendar) {
        return new SimpleDateFormat("E, dd MMM, yyyy", Locale.getDefault()).format(calendar.getTimeInMillis());
    }

    public static String getDate(Calendar calendar) {
        return new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(calendar.getTimeInMillis());
    }

    public static String getDay(Calendar calendar) {
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTimeInMillis());
    }

}
