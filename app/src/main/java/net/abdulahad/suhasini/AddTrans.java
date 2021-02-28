package net.abdulahad.suhasini;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.SqlQuery;
import net.abdulahad.suhasini.data.Suhasini;
import net.abdulahad.suhasini.data.TransactionType;
import net.abdulahad.suhasini.dialog.TransTypeDialog;
import net.abdulahad.suhasini.helper.DBHelper;
import net.abdulahad.suhasini.helper.InputChecker;
import net.abdulahad.suhasini.helper.ViewHelper;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.model.Deposit;
import net.abdulahad.suhasini.model.Transaction;
import net.abdulahad.suhasini.protocol.ItemSelectionListener;
import net.abdulahad.suhasini.thread.ExeSupplier;

public class AddTrans extends AppCompatActivity implements ItemSelectionListener {

    private TransTypeDialog transTypeDialog;

    private ImageView ivTransIcon;
    private TextView tvTransIconDetail;
    private EditText etAmount, etTransNote;

    private int transType = TransactionType.MONEY_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        setTitle("Add a transaction");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transTypeDialog = new TransTypeDialog(this);
        transTypeDialog.setDialogTitle("Select Transaction Type");

        ivTransIcon = findViewById(R.id.transaction_type_icon);

        tvTransIconDetail = findViewById(R.id.transaction_type_icon_detail);
        etAmount = findViewById(R.id.amount);
        etTransNote = findViewById(R.id.transaction_note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        ViewHelper.hideSoftKeyboard(this, ViewHelper.getRootView(this));
        addTransaction();
        return true;
    }

    private void addTransaction() {

        if (!InputChecker.isDouble(etAmount, true)) return;
        if (etTransNote.getText().toString().length() < 1) {
            InputChecker.focusAndAnimate(etTransNote);
            etTransNote.setError("Please add a note");
            return;
        }

        double amount = Double.parseDouble(String.valueOf(etAmount.getText()));
        String tag = etTransNote.getText().toString();
        String dateTime = AtomDate.getISOOffsetDateTime();

        ExeSupplier.get().lightBGThread().execute(() -> {
            SQLiteDatabase db = Suhasini.getDB(getApplicationContext());
            try {

                /* first have a look at the pocket what we got */
                Cursor cursor = db.rawQuery(SqlQuery.QUERY_CURRENT_DEPOSIT, null);
                if (cursor == null || !cursor.moveToFirst()) {
                    DBHelper.closeCursor(cursor);
                    throw new Exception("You have no deposit. Please add a deposit first.");
                }
                double currentBalance = cursor.getDouble(0);
                cursor.close();

                /* make sure that transaction doesn't exceed the deposit */
                double newBalance = currentBalance - amount;
                if (Double.compare(newBalance, 0) < 0)
                    throw new Exception("Transaction exceeds the current deposit. Please top up your deposit first.");

                db.beginTransaction();
                /* update the deposit table with this transaction */
                ContentValues values = Deposit.create(newBalance, transType, tag, dateTime);
                values.put(Key.PREVIOUS, currentBalance);
                long result = db.insert(Key.TABLE_DEPOSIT, null, values);

                if (result == -1)
                    throw new Exception("Failed to update the deposit with this transaction.");

                /* insert this transaction */
                ContentValues transaction = Transaction.create(amount, transType, tag, dateTime);
                result = db.insert(Key.TABLE_TRANSACTION, null, transaction);
                if (result == -1) throw new Exception("Failed to add this transaction.");

                db.setTransactionSuccessful();
                showToUI(1, "Transaction has been added.");
            } catch (Exception e) {
                ErrorRegister.register(e, AddTrans.class, db);
                showToUI(-1, e.getMessage());
            } finally {
                DBHelper.endTransaction(db);
            }
        });
    }

    public void showLabelPicker(View view) {
        transTypeDialog.show();
    }

    public void showToUI(int resultCode, String message) {
        ExeSupplier.get().UIThread().execute(() -> {
            if (resultCode == -1) {
                ViewHelper.showSnackBar(message, Snackbar.LENGTH_INDEFINITE, this);
            } else {
                ViewHelper.showToast(message, ViewHelper.getRootView(this));
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(int transactionType) {
        transType = transactionType;
        transTypeDialog.dismiss();
        tvTransIconDetail.setText(TransactionType.getLabel(transactionType));
        ivTransIcon.setImageResource(TransactionType.getIcon(transactionType));

        int color = TransactionType.getColor(transactionType);
        tvTransIconDetail.setTextColor(color);
        ViewHelper.setTint(color, ivTransIcon);
    }

}