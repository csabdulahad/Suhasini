package net.abdulahad.suhasini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.helper.PrefHelper;
import net.abdulahad.suhasini.helper.ViewHelper;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewHelper.goFullScreen(this);
        setContentView(R.layout.activity_config);
        getSupportActionBar().hide();


    }

    public void setCurrency(View view) {
        String tag = (String) view.getTag();
        String currency = (tag.equals("bdt")) ? Key.MONEY_SIGN_BDT : Key.MONEY_SIGN_GBP;
        PrefHelper.putString(getApplicationContext(), Key.KEY_MONEY_SIGN, currency);
        Intent intent = new Intent(this, SyncActivity.class);
        intent.putExtra(SyncActivity.KEY_SYNC_TYPE, SyncActivity.SYNC_DOWN);
        startActivity(intent);
        finish();
    }

}