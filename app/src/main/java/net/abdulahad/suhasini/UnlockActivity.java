package net.abdulahad.suhasini;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.helper.PrefHelper;
import net.abdulahad.suhasini.helper.ViewHelper;

public class UnlockActivity extends AppCompatActivity {

    String storedPin;
    int length;

    EditText etUnlock;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_unlock);

        snackbar = Snackbar.make(ViewHelper.getRootView(this), "Incorrect PIN", BaseTransientBottomBar.LENGTH_SHORT);

        storedPin = PrefHelper.getString(this, Key.KEY_LOCK, null);
        length = storedPin == null ? -1 : storedPin.length();

        etUnlock = findViewById(R.id.pin);
        etUnlock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int inputLength = s.toString().length();
                if (inputLength != length) return;

                ViewHelper.hideSoftKeyboard(UnlockActivity.this, ViewHelper.getRootView(UnlockActivity.this));

                if (!checkPin(s.toString())) {
                    flipSnack();
                } else {
                    finish();
                    Intent intent = new Intent(UnlockActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewHelper.requestFocusAndKeyboard(etUnlock);
    }

    private boolean checkPin(String pin) {
        return pin.equals(storedPin);
    }


    private void flipSnack() {
        if (snackbar.isShown()) {
            snackbar.dismiss();
        }
        snackbar.show();
    }

}