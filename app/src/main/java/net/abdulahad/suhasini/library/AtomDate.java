package net.abdulahad.suhasini.library;

import androidx.annotation.NonNull;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AtomDate {

    private final int day, month, year, dayOfWeek;

    public AtomDate() {
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    }

    public AtomDate(String sqlDate) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Date.valueOf(sqlDate).getTime());
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    }

    /* @return the date with leading zero where applicable. 02, 30 */
    public String dayLeadByZero() {
        return leadByZero(day);
    }

    /* @return the month with leading zero where applicable. 02, 12 */
    public String monthLeadByZero() {
        return leadByZero(month);
    }

    /* @return the first three letter of the month name. Apr, Mar  */
    public String shortMonth() {
        return monthShortForm(month);
    }

    /* @return the name of the day in full form. Friday, Monday */
    public String fullNameOfDay() {
        switch (dayOfWeek) {
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            default:
                return "Friday";
        }
    }

    /* @return the name of the day in short form. Fri, Mon */
    public String shortDayOfWeek() {
        switch (dayOfWeek) {
            case Calendar.SATURDAY:
                return "Sat";
            case Calendar.SUNDAY:
                return "Sun";
            case Calendar.MONDAY:
                return "Mon";
            case Calendar.TUESDAY:
                return "Tue";
            case Calendar.WEDNESDAY:
                return "Wed";
            case Calendar.THURSDAY:
                return "Thu";
            default:
                return "Fri";
        }
    }

    /* @return the date in English format. 21-02-1994 */
    public String date(String separator) {
        return leadByZero(day) + separator + leadByZero(month) + separator + year;
    }

    /* @return the date in English format. 1994-02-21 */
    public String ISODate() {
        return year + "-" + leadByZero(month) + "-" + leadByZero(day);
    }

    /* @return the date in English format. 1994-02 */
    public String ISOYearMonth() {
        return year + "-" + leadByZero(month);
    }

    /* this methods adds 0 to the number less than 10 */
    private String leadByZero(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }

    public static String getISOOffsetDateTime() {
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private String monthShortForm(int month) {
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            default:
                return "Dec";
        }
    }

    @NonNull
    @Override
    public String toString() {
        return date("-");
    }

}
