package net.abdulahad.suhasini;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.helper.PrefHelper;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* check whether we are logged in with an account */
        boolean loggedIn = PrefHelper.getBoolean(this, Key.LOGGED_IN, false);
        if (!loggedIn) {
            finish();
            startActivity(new Intent(this, Login.class));
            return;
        }

        /* ask for lock if user doesn't have one yet */
        boolean hasLock = PrefHelper.getBoolean(this, Key.KEY_HAS_LOCK, false);
        if (!hasLock) {
            finish();
            startActivity(new Intent(this, NewLockActivity.class));
            return;
        }

        /* load the local db with server database */
        boolean syncedDB = PrefHelper.getInt(this, Key.LOCAL_DB_VERSION, 0) == 0;
        if (syncedDB) {
            finish();
            Intent intent = new Intent(this, SyncActivity.class);
            intent.putExtra(SyncActivity.KEY_SYNC_TYPE, SyncActivity.SYNC_DOWN);
            startActivity(intent);
            return;
        }

        finish();
        startActivity(new Intent(this, UnlockActivity.class));
    }
}