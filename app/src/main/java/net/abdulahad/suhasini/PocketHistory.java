package net.abdulahad.suhasini;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.adapter.DepositAdapter;
import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.SqlQuery;
import net.abdulahad.suhasini.data.Suhasini;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.model.Deposit;
import net.abdulahad.suhasini.thread.ExeSupplier;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PocketHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    DepositAdapter adapter;

    ArrayList<Deposit> depositList;
    AtomDate today;

    public class BinarySpacesItemDecorator extends RecyclerView.ItemDecoration {

        private final int space;

        public BinarySpacesItemDecorator(int dimenId) {
            this.space = getResources().getDimensionPixelSize(dimenId);
        }

        @Override
        public void getItemOffsets(Rect outRect, @NotNull View view, RecyclerView parent, @NotNull RecyclerView.State state) {

            outRect.top = space;
            outRect.bottom = 0;


            boolean evenPos = parent.getChildLayoutPosition(view) % 2 == 0;
            // Add top margin only for the first item to avoid double space between items
            if (evenPos) {
                outRect.right = space / 2;
                outRect.left = space;
            } else {
                outRect.left = space / 2;
                outRect.right = space;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket_view);
        setTitle("Pocket history");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        depositList = new ArrayList<>();
        today = new AtomDate();

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new DepositAdapter(findViewById(R.id.empty_view), recyclerView);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new BinarySpacesItemDecorator(R.dimen.spacing));
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pocket_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.action_add_deposit) {
            Intent intent = new Intent(this, AddDeposit.class);
            startActivity(intent);
        } else if (menuId == android.R.id.home) {
            finish();
        } else if(menuId == R.id.action_adjust_deposit) {
            Intent intent = new Intent(this, AdjustDeposit.class);
            startActivity(intent);
        }
        return true;
    }

    private void setDataToAdapter(ArrayList<Deposit> depositList) {
        ExeSupplier.get().UIThread().execute(() -> {
            adapter.setData(depositList);
        });
    }

    private void loadData() {

        ExeSupplier.get().lightBGThread().execute(() -> {

            SQLiteDatabase db = Suhasini.getDB(getApplicationContext());
            Cursor cursor = db.rawQuery(SqlQuery.QUERY_POCKET_HISTORY, new String[]{today.ISOYearMonth()});
            if (cursor == null || cursor.getCount() < 1) {
                Log.d("ahad", "empty");
                if (depositList.size() > 0) depositList.clear();
                setDataToAdapter(depositList);
                return;
            }

            depositList.clear();
            while (cursor.moveToNext()) {
                Deposit deposit = new Deposit();
                deposit.setId(cursor.getInt(cursor.getColumnIndex("id")));
                deposit.setPrevious(cursor.getDouble(cursor.getColumnIndex("previous")));
                deposit.setCurrent(cursor.getDouble(cursor.getColumnIndex("current")));
                deposit.setTag(cursor.getString(cursor.getColumnIndex("tag")));
                deposit.setDateTime(cursor.getString(cursor.getColumnIndex("happened")));
                deposit.setTransactionType(cursor.getInt(cursor.getColumnIndex(Key.TRANSACTION_TYPE)));
                deposit.setCalculated(cursor.getInt(cursor.getColumnIndex("calculated")));
                Log.d("ahad", deposit.tag);
                depositList.add(deposit);
            }
            cursor.close();

            setDataToAdapter(depositList);
        });
    }

}