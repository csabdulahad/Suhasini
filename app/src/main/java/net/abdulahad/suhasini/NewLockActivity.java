package net.abdulahad.suhasini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.helper.InputChecker;
import net.abdulahad.suhasini.helper.PrefHelper;
import net.abdulahad.suhasini.helper.ViewHelper;

public class NewLockActivity extends AppCompatActivity {

    EditText etPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lock);
        etPin = findViewById(R.id.pin);
    }

    public void setPin(View view) {
        if (!InputChecker.passNumberCheck(etPin, false)) return;
        PrefHelper.putString(this, Key.KEY_LOCK, etPin.getText().toString());
        PrefHelper.putBoolean(this, Key.KEY_HAS_LOCK, true);
        finish();

        Intent intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
    }
}