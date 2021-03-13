package net.abdulahad.suhasini.protocol;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

public abstract class UIWork {

    protected Activity activity;

    public UIWork(Activity activity) {
        this.activity = activity;
    }

    public abstract void load(SQLiteDatabase db);

    public abstract void update();

}
