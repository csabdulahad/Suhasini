package net.abdulahad.suhasini.uiwork;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.abdulahad.suhasini.MainActivity;
import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.data.SqlQuery;
import net.abdulahad.suhasini.helper.DBHelper;
import net.abdulahad.suhasini.helper.SwapTextHelper;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.library.SwapTextView;
import net.abdulahad.suhasini.protocol.UIWork;

public class EarningOfMonthUIW extends UIWork {

    double totalEarning;

    SwapTextView stvTotalEarning;
    AtomDate todayAtom;

    public EarningOfMonthUIW(Activity activity) {
        super(activity);
        stvTotalEarning = activity.findViewById(R.id.total_earning);
        todayAtom = ((MainActivity) activity).getTodayAtom();
    }

    @Override
    public void load(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(SqlQuery.QUERY_EARNING_BY_MONTH, new String[]{todayAtom.ISOYearMonth()});
        if (cursor == null || !cursor.moveToNext()) {
            totalEarning = 0;
            DBHelper.closeCursor(cursor);
            return;
        }
        totalEarning = cursor.getDouble(0);
        DBHelper.closeCursor(cursor);
    }

    @Override
    public void update() {
        SwapTextHelper.setMoneyToSTV(totalEarning, false, stvTotalEarning, activity.getApplicationContext());
    }
}
