package net.abdulahad.suhasini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.helper.InputChecker;
import net.abdulahad.suhasini.helper.PrefHelper;
import net.abdulahad.suhasini.helper.ViewHelper;

public class UnlockActivity extends AppCompatActivity {

    EditText etUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        etUnlock = findViewById(R.id.pin);
    }

    public void unlock(View view) {
        ViewHelper.hideSoftKeyboard(this, ViewHelper.getRootView(this));
        if (!InputChecker.passNumberCheck(etUnlock, false)) return;
        String pin = etUnlock.getText().toString();
        String storedPin = PrefHelper.getString(this, Key.KEY_LOCK, null);
        if (pin.equals(storedPin)) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(ViewHelper.getRootView(this), "Incorrect PIN", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

}