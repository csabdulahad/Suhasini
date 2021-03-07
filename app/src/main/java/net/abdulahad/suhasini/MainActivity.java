package net.abdulahad.suhasini;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.adapter.SpendingAdapter;
import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.SqlQuery;
import net.abdulahad.suhasini.data.Suhasini;
import net.abdulahad.suhasini.data.TransactionType;
import net.abdulahad.suhasini.helper.DBHelper;
import net.abdulahad.suhasini.helper.PrefHelper;
import net.abdulahad.suhasini.helper.SwapTextHelper;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.library.LineChart;
import net.abdulahad.suhasini.library.SwapTextView;
import net.abdulahad.suhasini.model.Spending;
import net.abdulahad.suhasini.thread.ExeSupplier;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SpendingAdapter spendingAdapter;

    SwapTextView stvDeposit, stvTotalEarning, stvTotalSpending;

    AtomDate todayAtom;

    /* sync hint views */
    View syncViewBG;
    TextView tvSyncMessage, tvLocalDBVersion;
    ImageView ibSyncAction;

    private LineChart lineChart;

    private double totalEarning;
    private double deposit;
    private double totalSpending;

    private ArrayList<Spending> spendingList;
    private ArrayList<Double> spendingOfDay;
    private int syncCount = 0;

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_main);

        lineChart = findViewById(R.id.spending_chart);

        syncViewBG = findViewById(R.id.sync_hint_bg);
        tvSyncMessage = findViewById(R.id.sync_message);
        tvLocalDBVersion = findViewById(R.id.local_db_version);
        ibSyncAction = findViewById(R.id.sync_action);

        stvDeposit = findViewById(R.id.total_in_pocket);

        stvTotalSpending = findViewById(R.id.total_spending);
        stvTotalEarning = findViewById(R.id.total_earning);

        todayAtom = new AtomDate();
        spendingList = new ArrayList<>();
        spendingOfDay = new ArrayList<>();

        RecyclerView rvSpending = findViewById(R.id.recycler_view_spending_by_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvSpending.setLayoutManager(layoutManager);

        spendingAdapter = new SpendingAdapter(findViewById(R.id.spending_empty_view), rvSpending);
        rvSpending.setAdapter(spendingAdapter);
    }

    private void loadData() {
        ExeSupplier.get().lightBGThread().execute(() -> {
            SQLiteDatabase db = Suhasini.getDB(getApplicationContext());
            loadEarningOfMonth(db);
            loadCurrentDeposit(db);
            loadSyncCount(db);
            loadSpending(db);
            loadSpendingChart(db);
            ExeSupplier.get().UIThread().execute(() -> {
                updateAtAGlanceUI();
                updateSyncUI();
                updateSpendingChart();
                updateLocalDBVersion();
            });
        });
    }

    private void updateLocalDBVersion() {
        int version = PrefHelper.getInt(this, Key.LOCAL_DB_VERSION, 0);
        tvLocalDBVersion.setText(getString(R.string.local_db_version, version));
    }

    private void loadSpendingChart(SQLiteDatabase db) {
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

    private void loadEarningOfMonth(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(SqlQuery.QUERY_EARNING_BY_MONTH, new String[]{todayAtom.ISOYearMonth()});
        if (cursor == null || !cursor.moveToNext()) {
            totalEarning = 0;
            DBHelper.closeCursor(cursor);
            return;
        }
        totalEarning = cursor.getDouble(0);
        DBHelper.closeCursor(cursor);
    }

    private void loadCurrentDeposit(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(SqlQuery.QUERY_CURRENT_DEPOSIT, null);
        if (cursor == null || !cursor.moveToNext()) {
            deposit = 0;
            DBHelper.closeCursor(cursor);
            return;
        }
        deposit = cursor.getDouble(0);
        DBHelper.closeCursor(cursor);
    }

    private void loadSyncCount(SQLiteDatabase db) {
        syncCount = 0;
        String[] tables = {Key.TABLE_DEPOSIT, Key.TABLE_TRANSACTION};
        for (String table : tables) {
            Cursor cursor = db.rawQuery(SqlQuery.getSyncCountQuery(table), null);

            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null && !cursor.isClosed()) cursor.close();
                continue;
            }

            cursor.moveToNext();
            syncCount += cursor.getInt(0);
            cursor.close();
        }
    }

    private void updateSyncUI() {
        int syncIcon = syncCount == 0 ? R.drawable.ic_sync_down : R.drawable.ic_sync_up;
        ibSyncAction.setImageResource(syncIcon);

        String syncMessage = syncCount > 0 ? getString(R.string.sync_to_be_done, syncCount) : getString(R.string.sync_no);
        tvSyncMessage.setText(syncMessage);

        if (syncCount < 1) return;

        ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(syncViewBG,
                "backgroundColor",
                new ArgbEvaluator(),
                getColor(R.color.warn_start),
                getColor(R.color.warn_end));
        backgroundColorAnimator.setDuration(700);
        backgroundColorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        backgroundColorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        backgroundColorAnimator.start();
    }

    private void updateSpendingChart() {
        ArrayList<LineChart.Progress> progressList = new ArrayList<>();
        double unitSpending = totalSpending / 100;
        for (double amount : spendingOfDay)
            progressList.add(new LineChart.Progress((float) ((float) amount / unitSpending), getColor(R.color.secondary)));
        lineChart.setProgress(progressList);
    }

    private void updateAtAGlanceUI() {
        SwapTextHelper.setMoneyToSTV(deposit, false, stvDeposit, getApplicationContext());
        spendingAdapter.setData(spendingList, totalSpending);
        SwapTextHelper.setMoneyToSTV(totalSpending, false, stvTotalSpending, getApplicationContext());

        SwapTextHelper.setMoneyToSTV(totalEarning, false, stvTotalEarning, getApplicationContext());
    }

    private void loadSpending(SQLiteDatabase db) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.action_exit) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showTransaction(View view) {
        Intent intent = new Intent(this, TransactionView.class);
        startActivity(intent);
    }

    public void addTransaction(View view) {
        Intent intent = new Intent(this, AddTrans.class);
        startActivity(intent);
    }

    public void showPocket(View view) {
        Intent intent = new Intent(this, PocketHistory.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void performSync(View view) {
        Intent intent = new Intent(this, SyncActivity.class);
        int syncType = syncCount > 0 ? SyncActivity.SYNC_PUSH : SyncActivity.SYNC_DOWN;
        intent.putExtra(SyncActivity.KEY_SYNC_TYPE, syncType);
        startActivity(intent);
    }

    public void adjustPocket(View view) {
        startActivity(new Intent(this, AdjustDeposit.class));
    }

    public void addDeposit(View view) {
        startActivity(new Intent(this, AddDeposit.class));
    }

}