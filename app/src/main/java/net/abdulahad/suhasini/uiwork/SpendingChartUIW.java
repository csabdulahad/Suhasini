package net.abdulahad.suhasini.uiwork;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.abdulahad.suhasini.MainActivity;
import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.data.SqlQuery;
import net.abdulahad.suhasini.helper.DBHelper;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.library.LineChart;
import net.abdulahad.suhasini.protocol.UIWork;

import java.util.ArrayList;

public class SpendingChartUIW extends UIWork {

    double totalSpending;

    LineChart lineChart;
    AtomDate todayAtom;

    ArrayList<Double> spendingOfDay;

    public SpendingChartUIW(Activity activity) {
        super(activity);
        lineChart = activity.findViewById(R.id.spending_chart);
        lineChart.setBarColor(activity.getColor(R.color.secondary));
        spendingOfDay = new ArrayList<>();
        todayAtom = ((MainActivity) activity).getTodayAtom();
    }

    @Override
    public void load(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(SqlQuery.QUERY_SPENDING_BY_DAY_OF_MONTH, new String[]{todayAtom.ISOYearMonth()});
        if (cursor == null || cursor.getCount() < 1) {
            DBHelper.closeCursor(cursor);
            return;
        }

        spendingOfDay.clear();
        while (cursor.moveToNext()) {
            double amount = cursor.getDouble(1);
            spendingOfDay.add(amount);
        }
    }

    @Override
    public void update() {
        ArrayList<Float> progressList = new ArrayList<>();
        double unitSpending = totalSpending / 100;
        for (double amount : spendingOfDay)
            progressList.add((float) ((float) amount / unitSpending));
        lineChart.setProgress(progressList);
    }

    public void setTotalSpending(double totalSpending) {
        this.totalSpending = totalSpending;
    }

}
