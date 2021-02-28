package net.abdulahad.suhasini.dialog;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.adapter.TransTypeAdapter;
import net.abdulahad.suhasini.protocol.ItemSelectionListener;

public class TransTypeDialog extends AlertDialog {

    RecyclerView recyclerView;
    TextView tvTitle;

    public TransTypeDialog(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pickerk_view, null, false);
        recyclerView = view.findViewById(R.id.dialog_recycler_view);
        tvTitle = view.findViewById(R.id.dialog_title);

        TransTypeAdapter adapter = new TransTypeAdapter((ItemSelectionListener) context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        setView(view);
    }

    public void setDialogTitle(String title) {
        tvTitle.setText(title);
    }

}
