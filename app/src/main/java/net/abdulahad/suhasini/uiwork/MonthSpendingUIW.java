package net.abdulahad.suhasini.uiwork;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.helper.SwapTextHelper;
import net.abdulahad.suhasini.library.SwapTextView;
import net.abdulahad.suhasini.protocol.UIWork;

public class MonthSpendingUIW extends UIWork {

    double totalSpending;
    SwapTextView stvTotalSpending;

    public MonthSpendingUIW(Activity activity) {
        super(activity);
        stvTotalSpending = activity.findViewById(R.id.total_spending);
    }

    @Override
    public void load(SQLiteDatabase db) {

    }

    @Override
    public void update() {
        SwapTextHelper.setMoneyToSTV(totalSpending, false, stvTotalSpending, activity.getApplicationContext());
    }

    public void setTotalSpending(double totalSpending) {
        this.totalSpending = totalSpending;
    }

}
