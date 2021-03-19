package net.abdulahad.suhasini;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.adapter.SpendingDetail;
import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.SqlQuery;
import net.abdulahad.suhasini.data.Suhasini;
import net.abdulahad.suhasini.data.TransactionType;
import net.abdulahad.suhasini.helper.DBHelper;
import net.abdulahad.suhasini.helper.SwapTextHelper;
import net.abdulahad.suhasini.helper.ViewHelper;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.library.SimPro;
import net.abdulahad.suhasini.library.SwapTextView;
import net.abdulahad.suhasini.model.Spending;
import net.abdulahad.suhasini.model.Transaction;
import net.abdulahad.suhasini.thread.ExeSupplier;

import java.util.ArrayList;

public class SpendingRatioDetail extends AppCompatActivity {

    public static final String KEY_SPENDING = "spending";

    ArrayList<Transaction> transactionList;

    ImageView spendingIcon;
    SwapTextView totalSpending;
    TextView spendingDetail;
    SimPro spendingPercent;

    RecyclerView recyclerView;
    SpendingDetail adapter;

    Spending spending;

    float progress;
    AtomDate today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        today = new AtomDate();

        setContentView(R.layout.activity_spendiing_ratio_detail);
        setTitle("Spending Detail");
        getSupportActionBar().setSubtitle(today.fullMonth());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spending = (Spending) getIntent().getSerializableExtra(KEY_SPENDING);
        progress = (float) (spending.total / spending.unitSpending);

        transactionList = new ArrayList<>();

        spendingIcon = findViewById(R.id.spending_icon);
        totalSpending = findViewById(R.id.total_spending);
        spendingDetail = findViewById(R.id.spending_detail);

        spendingPercent = findViewById(R.id.spending_percent);
        spendingPercent.setOnClickListener(v -> spendingPercent.setProgressWithAnimation(progress));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new SpendingDetail();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(itemDecoration);

        SwapTextHelper.setMoneyToSTV(spending.total, false, totalSpending, this);
        spendingDetail.setText(TransactionType.getLabel(spending.type));

        int iconId = TransactionType.getIcon(spending.type);
        spendingIcon.setImageResource(iconId);
        ViewHelper.setTint(TransactionType.getColor(spending.type), spendingIcon);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        spendingPercent.setProgressWithAnimation(progress);
        loadSpendingOfSameType();
    }

    private void loadSpendingOfSameType() {
        SQLiteDatabase db = Suhasini.getDB(this);
        ExeSupplier.get().lightBGThread().execute(() -> {
            Cursor cursor = db.rawQuery(SqlQuery.QUERY_SPENDING_OF_TYPE_BY_MONTH, new String[]{String.valueOf(spending.type), today.ISOYearMonth()});
            if (cursor == null || cursor.getCount() < 1) return;

            transactionList.clear();
            while (cursor.moveToNext()) {
                Transaction transaction = new Transaction();
                transaction.amount = cursor.getDouble(cursor.getColumnIndex(Key.AMOUNT));
                transaction.tag = cursor.getString(cursor.getColumnIndex(Key.TAG));
                transaction.setDateTime(cursor.getString(cursor.getColumnIndex(Key.HAPPENED)));
                transaction.type = cursor.getInt(cursor.getColumnIndex(Key.TYPE));
                transactionList.add(transaction);
            }
            DBHelper.closeCursor(cursor);
            setSpendingDataToAdapter();
        });
    }

    private void setSpendingDataToAdapter() {
        ExeSupplier.get().UIThread().execute(() -> {
            adapter.setData(transactionList);
        });
    }

}