package net.abdulahad.suhasini.model;

import android.content.ContentValues;
import android.util.Log;

import net.abdulahad.suhasini.data.Key;

import java.sql.Date;
import java.sql.SQLData;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Transaction {

    public int id;
    public double amount;
    public String tag;
    public int type;
    public String dateTime;

    public ZonedDateTime getDateTime() {
        // String input = dateTime.replace( "+0000" , "+00:00" );
        return ZonedDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public static ContentValues create(double amount, int transType, String tag, String dateTime) {
        ContentValues values = new ContentValues();
        values.put(Key.AMOUNT, amount);
        values.put(Key.TYPE, transType);
        values.put(Key.TAG, tag);
        values.put(Key.HAPPENED, dateTime);
        return  values;
    }

}
