package net.abdulahad.suhasini;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.SqlQuery;
import net.abdulahad.suhasini.data.Suhasini;
import net.abdulahad.suhasini.data.TransactionType;
import net.abdulahad.suhasini.helper.DBHelper;
import net.abdulahad.suhasini.helper.InputChecker;
import net.abdulahad.suhasini.helper.ViewHelper;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.model.Deposit;
import net.abdulahad.suhasini.thread.ExeSupplier;

public class AdjustDeposit extends AppCompatActivity {
    private EditText etAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_deposit);

        setTitle("Adjust Your Pocket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etAmount = findViewById(R.id.amount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        ViewHelper.hideSoftKeyboard(this, ViewHelper.getRootView(this));
        saveDeposit();
        return true;
    }


    private void saveDeposit() {
        if (!InputChecker.isDouble(etAmount, false)) return;

        double amount = Double.parseDouble(String.valueOf(etAmount.getText()));
        String dateTime = AtomDate.getISOOffsetDateTime();

        ExeSupplier.get().lightBGThread().execute(() -> {

            double currentDeposit = 0;

            SQLiteDatabase db = Suhasini.getDB(getApplicationContext());
            Cursor cursor = db.rawQuery(SqlQuery.QUERY_CURRENT_DEPOSIT, null);
            if (cursor != null && cursor.moveToNext()) {
                currentDeposit = cursor.getDouble(0);
            }
            DBHelper.closeCursor(cursor);
            if (currentDeposit == 0) {
                showUIMessage("This adjustment makes deposit negative");
                return;
            }

            ContentValues deposit = Deposit.create(amount + currentDeposit, TransactionType.ADJUST_MONEY, "Deposit adjustment", dateTime);
            deposit.put(Key.PREVIOUS, currentDeposit);

            String message = db.insert(Key.TABLE_DEPOSIT, null, deposit) > -1 ? "Deposit adjusted" : "Deposit adjustment was failed";
            showUIMessage(message);
        });

    }

    private void showUIMessage(String message) {
        ExeSupplier.get().UIThread().execute(() -> {
            Toast.makeText(AdjustDeposit.this, message, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

}