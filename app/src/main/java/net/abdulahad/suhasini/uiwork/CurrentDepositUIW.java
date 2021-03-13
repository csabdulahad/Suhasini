package net.abdulahad.suhasini.uiwork;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.data.SqlQuery;
import net.abdulahad.suhasini.helper.DBHelper;
import net.abdulahad.suhasini.helper.SwapTextHelper;
import net.abdulahad.suhasini.library.SwapTextView;
import net.abdulahad.suhasini.protocol.UIWork;

public class CurrentDepositUIW extends UIWork {

    double deposit;

    SwapTextView stvDeposit;

    public CurrentDepositUIW(Activity activity) {
        super(activity);
        stvDeposit = activity.findViewById(R.id.total_in_pocket);
    }

    @Override
    public void load(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(SqlQuery.QUERY_CURRENT_DEPOSIT, null);
        if (cursor == null || !cursor.moveToNext()) {
            deposit = 0;
            DBHelper.closeCursor(cursor);
            return;
        }
        deposit = cursor.getDouble(0);
        DBHelper.closeCursor(cursor);
    }

    @Override
    public void update() {
        SwapTextHelper.setMoneyToSTV(deposit, false, stvDeposit, activity.getApplicationContext());
    }
}
