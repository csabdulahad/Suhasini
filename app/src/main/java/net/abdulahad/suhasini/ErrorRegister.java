package net.abdulahad.suhasini;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.Suhasini;

public class ErrorRegister {

    public static void register(Exception e, Class houseClass, SQLiteDatabase db) {
        String errorHouse = houseClass.getSimpleName();
        String errorName= e.getClass().getSimpleName();
        String errorMsg = e.getMessage();
        if (errorMsg == null) errorMsg = "No Error Message";

        Log.d("ahad", "ERROR : " + e.getMessage());
        db.execSQL(String.format("INSERT INTO %s(%s, %s, %s) VALUES(?, ?, ?)", Key.TABLE_ERROR, Key.ERROR_HOUSE, Key.ERROR_NAME, Key.ERROR_MSG), new String[]{errorHouse, errorName, errorMsg});
        e.printStackTrace();
    }

}
