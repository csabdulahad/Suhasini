package net.abdulahad.suhasini.uiwork;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.data.Key;
import net.abdulahad.suhasini.data.SqlQuery;
import net.abdulahad.suhasini.helper.PrefHelper;
import net.abdulahad.suhasini.helper.ViewHelper;
import net.abdulahad.suhasini.protocol.UIWork;

public class SyncUIW extends UIWork {

    int syncCount = 0;

    /* sync hint views */
    View syncViewBG;
    TextView tvSyncMessage, tvLocalDBVersion;
    ImageView ibSyncAction;

    public SyncUIW(Activity activity) {
        super(activity);
        syncViewBG = activity.findViewById(R.id.sync_hint_bg);
        tvSyncMessage = activity.findViewById(R.id.sync_message);
        tvLocalDBVersion = activity.findViewById(R.id.local_db_version);
        ibSyncAction = activity.findViewById(R.id.sync_action);
    }

    @Override
    public void load(SQLiteDatabase db) {
        syncCount = 0;
        String[] tables = {Key.TABLE_DEPOSIT, Key.TABLE_TRANSACTION};
        for (String table : tables) {
            Cursor cursor = db.rawQuery(SqlQuery.getSyncCountQuery(table), null);

            if (cursor == null || cursor.getCount() < 1) {
                if (cursor != null && !cursor.isClosed()) cursor.close();
                continue;
            }

            cursor.moveToNext();
            syncCount += cursor.getInt(0);
            cursor.close();
        }
    }

    @Override
    public void update() {
        updateLocalDBVersion();

        int syncIcon = syncCount == 0 ? R.drawable.ic_sync_down : R.drawable.ic_sync_up;
        ibSyncAction.setImageResource(syncIcon);

        String syncMessage = syncCount > 0 ? activity.getString(R.string.sync_to_be_done, syncCount) : activity.getString(R.string.sync_no);
        tvSyncMessage.setText(syncMessage);

        if (syncCount < 1) return;

        ViewHelper.flashBG(R.color.warn_start, R.color.warn_end, 700, syncViewBG);

    }

    private void updateLocalDBVersion() {
        int version = PrefHelper.getInt(activity, Key.LOCAL_DB_VERSION, 0);
        tvLocalDBVersion.setText(activity.getString(R.string.local_db_version, version));
    }

    public int getSyncCount() {
        return syncCount;
    }
}
