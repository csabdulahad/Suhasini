package net.abdulahad.suhasini.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {

    public static void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) cursor.close();
    }

    public static void endTransaction(SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase.inTransaction()) sqLiteDatabase.endTransaction();
    }

}
