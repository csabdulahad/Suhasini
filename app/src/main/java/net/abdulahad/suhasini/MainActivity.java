package net.abdulahad.suhasini;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import net.abdulahad.suhasini.data.Suhasini;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.thread.ExeSupplier;
import net.abdulahad.suhasini.uiwork.CurrentDepositUIW;
import net.abdulahad.suhasini.uiwork.EarningOfMonthUIW;
import net.abdulahad.suhasini.uiwork.MonthSpendingUIW;
import net.abdulahad.suhasini.uiwork.SpendingChartUIW;
import net.abdulahad.suhasini.uiwork.SpendingRatioUIW;
import net.abdulahad.suhasini.uiwork.SyncUIW;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    AtomDate todayAtom;

    SpendingRatioUIW spendingRatioUIW;
    SpendingChartUIW spendingChartUIW;
    CurrentDepositUIW currentDepositUIW;
    MonthSpendingUIW monthSpendingUIW;
    EarningOfMonthUIW earningOfMonthUIW;
    SyncUIW syncUIW;

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

        db = Suhasini.getDB(getApplicationContext());
        todayAtom = new AtomDate();

        spendingRatioUIW = new SpendingRatioUIW(this);
        spendingChartUIW = new SpendingChartUIW(this);
        monthSpendingUIW = new MonthSpendingUIW(this);
        currentDepositUIW = new CurrentDepositUIW(this);
        earningOfMonthUIW = new EarningOfMonthUIW(this);
        syncUIW = new SyncUIW(this);
    }

    private void loadData() {
        ExeSupplier.get().lightBGThread().execute(() -> {
            spendingRatioUIW.load(db);
            spendingChartUIW.load(db);
            currentDepositUIW.load(db);
            earningOfMonthUIW.load(db);
            syncUIW.load(db);

            spendingChartUIW.setTotalSpending(spendingRatioUIW.getTotalSpending());
            monthSpendingUIW.setTotalSpending(spendingRatioUIW.getTotalSpending());

            ExeSupplier.get().UIThread().execute(() -> {
                spendingRatioUIW.update();
                spendingChartUIW.update();

                currentDepositUIW.update();
                monthSpendingUIW.update();
                earningOfMonthUIW.update();
                syncUIW.update();
            });

        });
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

    public void performAction(View view) {
        Intent intent = null;

        String tag = (String) view.getTag();
        if (tag.equals("showTrans")) {
            intent = new Intent(this, TransactionView.class);
        } else if (tag.equals("addTrans")) {
            intent = new Intent(this, AddTrans.class);
        } else if (tag.equals("pocket")) {
            intent = new Intent(this, PocketHistory.class);
        } else if (tag.equals("adjustPoc")) {
            intent = new Intent(this, AdjustDeposit.class);
        } else if (tag.equals("addDeposit")) {
            intent = new Intent(this, AddDeposit.class);
        } else if (tag.equals("performSync")) {
            intent = new Intent(this, SyncActivity.class);
            int syncType = syncUIW.getSyncCount() > 0 ? SyncActivity.SYNC_PUSH : SyncActivity.SYNC_DOWN;
            intent.putExtra(SyncActivity.KEY_SYNC_TYPE, syncType);
        }
        if (intent == null) return;
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public AtomDate getTodayAtom() {
        return todayAtom;
    }

}