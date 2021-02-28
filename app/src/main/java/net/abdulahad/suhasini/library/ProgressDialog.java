package net.abdulahad.suhasini.library;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.abdulahad.suhasini.R;

public class ProgressDialog extends AlertDialog {

    private final TextView message;

    public ProgressDialog(Context context) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress_dialog, null, false);
        message = view.findViewById(R.id.message);
        setView(view);
    }

    public void setMessage(String text) {
        message.setText(text);
    }

}
