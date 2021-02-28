
package net.abdulahad.suhasini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.abdulahad.suhasini.helper.ViewHelper;
import net.abdulahad.suhasini.model.Result;
import net.abdulahad.suhasini.thread.ExeSupplier;

public class SyncActivity extends AppCompatActivity {

    public static final String KEY_SYNC_TYPE = "syncType";
    public static final int SYNC_DOWN = 100;
    public static final int SYNC_PUSH = 400;

    private Updater updater;

    private ImageView syncIcon;
    private TextView syncMessage;
    private TextView syncHint;
    private ProgressBar syncProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ViewHelper.goFullScreen(this);
        setContentView(R.layout.activity_sync);

        getSupportActionBar().hide();

        /* make sure we have got a sync type before starting the syncing */
        Intent intent = getIntent();
        if (!intent.hasExtra(KEY_SYNC_TYPE)) {
            Toast.makeText(this, "Sync type was missing", Toast.LENGTH_SHORT).show();
            finish();
        }
        int syncType = intent.getIntExtra(KEY_SYNC_TYPE, SYNC_DOWN);

        /* set the correct image */
        syncIcon = findViewById(R.id.sync_icon);
        int syncIconId = syncType == SYNC_DOWN ? R.drawable.down : R.drawable.up;
        syncIcon.setImageResource(syncIconId);

        /* set correct sync message */
        syncMessage = findViewById(R.id.sync_message);
        int syncMsgId = syncType == SYNC_DOWN ? R.string.sync_title_down : R.string.sync_title_push;
        syncMessage.setText(getString(syncMsgId));

        syncHint = findViewById(R.id.sync_hint);
        syncProgress = findViewById(R.id.sync_progress);

        updater = new Updater(getApplicationContext());
        sync(syncType);
    }

    private void sync(int syncType) {
        ExeSupplier.get().lightBGThread().execute(() -> {
            updater.updateBDTRate();
            Result result = syncType == SYNC_DOWN ? updater.syncDown() : updater.pushUpdate();
            ExeSupplier.get().UIThread().execute(() -> runResult(result));
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please, stay on the screen", Toast.LENGTH_SHORT).show();
    }

    private void runResult(Result result) {

        /* get correct icon id based on result & set it to image view */
        int iconId, syncTitleId;

        int resultCode = result.code;
        if (resultCode == Result.CODE_SUCCESS) {
            iconId = R.drawable.done;
            syncTitleId = R.string.sync_msg_success;
        } else if (resultCode == Result.CODE_ERROR) {
            iconId = R.drawable.cross;
            syncTitleId = R.string.sync_msg_failed;
        } else {
            iconId = R.drawable.happy;
            syncTitleId = R.string.sync_msg_neutral;
        }

        syncIcon.setImageResource(iconId);
        syncMessage.setText(getString(syncTitleId));
        syncHint.setText(result.msg);

        syncProgress.setVisibility(View.GONE);
        findViewById(R.id.sync_next).setVisibility(View.VISIBLE);
    }

    public void next(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}