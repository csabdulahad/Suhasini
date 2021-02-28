package net.abdulahad.suhasini.model;

import android.content.ContentValues;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.TransactionType;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Random;

public class Deposit {

    public int id, calculated;
    public double previous, current;
    public String tag;
    public String dateTime;
    public int transactionType;

    public ZonedDateTime getDateTime() {
        String input = dateTime.replace("+0000", "+00:00");
        return OffsetDateTime.parse(input).toZonedDateTime();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCalculated(int calculated) {
        this.calculated = calculated;
    }

    public void setPrevious(double previous) {
        this.previous = previous;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public static ContentValues create(double amount, int transactionType, String tag, String dateTime) {
        ContentValues values = new ContentValues();
        values.put(Key.CURRENT, amount);
        values.put(Key.TAG, tag);
        values.put(Key.TRANSACTION_TYPE, transactionType);
        values.put(Key.HAPPENED, dateTime);
        return  values;
    }

}
