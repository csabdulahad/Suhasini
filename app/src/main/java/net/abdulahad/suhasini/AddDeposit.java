package net.abdulahad.suhasini;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

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

public class AddDeposit extends AppCompatActivity {

    EditText etAmount, etTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deposit);
        setTitle("Add a deposit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etAmount = findViewById(R.id.amount);
        etTag = findViewById(R.id.note);
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
/*

        UrlHelper urlHelper = new UrlHelper(Key.URL_DEPOSIT_ADD);
         urlHelper.addQueryParam("amount", amount);
         urlHelper.addQueryParam("tag", tag);

        ExeSupplier.get().lightBGThread().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(urlHelper.getUrl()).build();

                try (Response response = client.newCall(request).execute()) {

                    JSONObject data = new JSONObject(response.body().string());

                    int responseCode = data.getInt("code");

                    ExeSupplier.get().UIThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (responseCode == 1) {
                                finish();
                                Toast.makeText(AddDepositActivity.this, "Deposit has been successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(ViewHelper.getRootView(AddDepositActivity.this), "Failed to add transaction", BaseTransientBottomBar.LENGTH_INDEFINITE).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
*/
    }

    private void saveDeposit() {
        if (!InputChecker.isDouble(etAmount, true)) return;
        if (etTag.getText().toString().length() < 1) {
            InputChecker.focusAndAnimate(etTag);
            etTag.setError("Please add a note");
            return;
        }

        double amount = Double.parseDouble(String.valueOf(etAmount.getText()));
        String tag = etTag.getText().toString();
        String dateTime = AtomDate.getISOOffsetDateTime();

        ExeSupplier.get().lightBGThread().execute(() -> {

            double currentDeposit = 0;

            SQLiteDatabase db = Suhasini.getDB(getApplicationContext());
            Cursor cursor = db.rawQuery(SqlQuery.QUERY_CURRENT_DEPOSIT, null);
            if (cursor != null && cursor.moveToNext()) {
                currentDeposit = cursor.getDouble(0);
            }
            DBHelper.closeCursor(cursor);

            ContentValues deposit = Deposit.create(amount + currentDeposit, TransactionType.DEPOSIT, tag, dateTime);
            deposit.put(Key.PREVIOUS, currentDeposit);

            String message = db.insert(Key.TABLE_DEPOSIT, null, deposit) > -1 ? "Deposit added" : "Failed";

            ExeSupplier.get().UIThread().execute(() -> {
                ViewHelper.showToast(message, etAmount);
                finish();
            });
        });

    }

}