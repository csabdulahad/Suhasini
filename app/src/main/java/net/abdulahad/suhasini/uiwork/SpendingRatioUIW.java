package net.abdulahad.suhasini.uiwork;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.MainActivity;
import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.SpendingRatioDetail;
import net.abdulahad.suhasini.adapter.SpendingAdapter;
import net.abdulahad.suhasini.data.SqlQuery;
import net.abdulahad.suhasini.data.TransactionType;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.model.Spending;
import net.abdulahad.suhasini.protocol.ItemSelectionListener;
import net.abdulahad.suhasini.protocol.UIWork;

import java.util.ArrayList;

public class SpendingRatioUIW extends UIWork implements ItemSelectionListener {

    AtomDate todayAtom;

    SpendingAdapter spendingAdapter;
    ArrayList<Spending> spendingList;

    double totalSpending;

    public SpendingRatioUIW(Activity activity) {
        super(activity);

        todayAtom = ((MainActivity) activity).getTodayAtom();
        spendingList = new ArrayList<>();

        RecyclerView rvSpending = activity.findViewById(R.id.recycler_view_spending_by_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rvSpending.setLayoutManager(layoutManager);

        spendingAdapter = new SpendingAdapter(activity.findViewById(R.id.spending_empty_view), rvSpending);
        spendingAdapter.setListener(this);
        rvSpending.setAdapter(spendingAdapter);
    }

    @Override
    public void load(SQLiteDatabase db) {
        /* get ready for holding data */
        totalSpending = 0;
        spendingList.clear();

        Cursor cursor = db.rawQuery(SqlQuery.QUERY_SPENDING_BY_CATEGORY, new String[]{todayAtom.ISOYearMonth()});
        if (cursor != null && cursor.getCount() < 1) return;

        while (cursor.moveToNext()) {
            Spending spending = new Spending();
            spending.total = cursor.getDouble(cursor.getColumnIndex("total"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            spending.type = type;
            spending.description = TransactionType.getLabel(type);
            spendingList.add(spending);
            totalSpending += spending.total;
        }
        cursor.close();
    }

    @Override
    public void update() {
        spendingAdapter.setData(spendingList, totalSpending);
    }

    public double getTotalSpending() {
        return totalSpending;
    }

    @Override
    public void onItemSelected(int data) {
        Spending spending = spendingAdapter.getSpending(data);
        Intent intent = new Intent(activity, SpendingRatioDetail.class);
        intent.putExtra(SpendingRatioDetail.KEY_SPENDING, spending);
        activity.startActivity(intent);
    }

}
