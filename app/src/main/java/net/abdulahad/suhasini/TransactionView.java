package net.abdulahad.suhasini;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.adapter.TransAdapter;
import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.SqlQuery;
import net.abdulahad.suhasini.data.Suhasini;
import net.abdulahad.suhasini.dialog.DatePickerDialog;
import net.abdulahad.suhasini.helper.CircularTextView;
import net.abdulahad.suhasini.helper.SwapTextHelper;
import net.abdulahad.suhasini.helper.ViewHelper;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.library.SwapTextView;
import net.abdulahad.suhasini.model.Transaction;
import net.abdulahad.suhasini.protocol.ItemSelectionListener;
import net.abdulahad.suhasini.thread.ExeSupplier;

import java.util.ArrayList;

public class TransactionView extends AppCompatActivity implements ItemSelectionListener {

    RecyclerView recyclerView;
    TransAdapter adapter;

    ArrayList<Transaction> transactionList;
    ArrayList<AtomDate> atomDates;
    AtomDate today;

    int dateIndex;

    DatePickerDialog datePickerDialog;

    CircularTextView dayMonth;

    double totalSpending;
    float exchangeRate;
    TextView day;
    SwapTextView tvTotalSpending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transaction_view);
        setTitle("Transactions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        exchangeRate = Suhasini.getCurrencyRate(getApplicationContext());

        atomDates = new ArrayList<>();
        transactionList = new ArrayList<>();
        today = new AtomDate();

        getTransHappenedDate();

        datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setTitle("Pick a date");

        dayMonth = findViewById(R.id.day_month);
        day = findViewById(R.id.day);
        tvTotalSpending = findViewById(R.id.total_spending);
        recyclerView = findViewById(R.id.trans_list);

        adapter = new TransAdapter(findViewById(R.id.empty_view), recyclerView);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    private void loadData() {
        ExeSupplier.get().lightBGThread().execute(() -> {

            if (atomDates.size() < 1) return;

            // load the selected YEAR-MONTH-DAY
            AtomDate atomDate = atomDates.get(dateIndex);

            SQLiteDatabase db = Suhasini.getDB(getApplicationContext());
            Cursor cursor = db.rawQuery(SqlQuery.QUERY_TRANS_ON_SPEC_DATE, new String[]{atomDate.ISODate()});
            if (cursor == null || cursor.getCount() < 1) return;

            transactionList.clear();
            totalSpending = 0;

            while (cursor.moveToNext()) {
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(Key.ID)));
                transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(Key.AMOUNT)));
                transaction.setType(cursor.getInt(cursor.getColumnIndex(Key.TYPE)));
                transaction.setTag(cursor.getString(cursor.getColumnIndex(Key.TAG)));
                transaction.setDateTime(cursor.getString(cursor.getColumnIndex(Key.HAPPENED)));
                transactionList.add(transaction);
                totalSpending += transaction.amount;
            }
            cursor.close();

            ExeSupplier.get().UIThread().execute(() -> {
                adapter.setData(transactionList);
                updateDateTimeUI();
            });
        });
    }

    public void previous(View view) {
        if (dateIndex - 1 < 0) {
            ViewHelper.showToast("No previous transaction", view);
            return;
        }
        dateIndex--;
        loadData();
    }

    public void next(View view) {
        if (dateIndex + 1 >= atomDates.size()) {
            ViewHelper.showToast("No next transaction", view);
            return;
        }
        dateIndex++;
        loadData();
    }

    private void updateDateTimeUI() {
        AtomDate atomDate = atomDates.get(dateIndex);
        String dayMon = "<big>" + atomDate.dayLeadByZero() + "</big><br><small>" + atomDate.shortMonth() + "</small>";
        ViewHelper.setHtmlTextToTextView(dayMonth, dayMon);
        day.setText(atomDate.fullNameOfDay());

        SwapTextHelper.setMoneyToSTV(totalSpending, false, tvTotalSpending, getApplicationContext());
    }

    public void getTransHappenedDate() {
        ExeSupplier.get().lightBGThread().execute(() -> {
            SQLiteDatabase db = Suhasini.getDB(getApplicationContext());
            Cursor cursor = db.rawQuery(SqlQuery.QUERY_TRANS_HAPPENED_ON, new String[]{today.ISOYearMonth()});
            if (cursor == null || cursor.getCount() < 1) return;

            while (cursor.moveToNext()) {
                AtomDate atomDate = new AtomDate(cursor.getString(0));
                atomDates.add(atomDate);
            }
            cursor.close();

            dateIndex = 0;

            ExeSupplier.get().UIThread().execute(() -> {
                datePickerDialog.setDateList(atomDates);
                loadData();
            });
        });
    }

    public void showDatePickerDialog(View view) {
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(int date) {
        datePickerDialog.dismiss();
        dateIndex = date;
        loadData();
    }
}