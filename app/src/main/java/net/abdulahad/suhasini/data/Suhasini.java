package net.abdulahad.suhasini.data;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.abdulahad.suhasini.helper.PrefHelper;

public class Suhasini extends Application {

    private static SQLiteDatabase DB;

    private static SQLiteDatabase validateDB(Context context) {
        if (DB == null) DB = new DB(context).getWritableDatabase();
        return DB;
    }

    public static SQLiteDatabase getDB(Context appContext) {
        return validateDB(appContext);
    }

    public static float getCurrencyRate(Context appContext) {
        return PrefHelper.getFloat(appContext, Key.KEY_EXCHANGE_RATE, Key.DEFAULT_EXCHANGE_RATE);
    }

    public static String getMoneySign(Context appContext) {
        return PrefHelper.getString(appContext, Key.KEY_MONEY_SIGN, Key.MONEY_SIGN_GBP);
    }

    public static double getAllowance(Context appContext) {
        return PrefHelper.getFloat(appContext, Key.KEY_ALLOWANCE, Key.DEFAULT_ALLOWANCE);
    }

}
