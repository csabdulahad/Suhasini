package net.abdulahad.suhasini.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {

    /* time format 2021-02-13T00:41:05.604Z */
    /* online db user : csabdulahad_android, db pass : 7ZdQ5txxtwaAn3a */

    private static final String DB_NAME = "pocket.db";
    private static final int DB_VERSION = 1;

    public DB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public synchronized void close() {
        super.close();
        Log.d("ahad", "closing db...");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlQuery.CREATE_TABLE_DEPOSIT);
        db.execSQL(SqlQuery.CREATE_TABLE_TRANSACTION);
        db.execSQL(SqlQuery.CREATE_TABLE_ERROR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Key.TABLE_DEPOSIT);
        db.execSQL("DROP TABLE IF EXISTS " + Key.TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + Key.TABLE_ERROR);
        onCreate(db);
    }

}
