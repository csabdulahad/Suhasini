package net.abdulahad.suhasini.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.adapter.DatePickerDialogAdapter;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.protocol.ItemSelectionListener;

import java.util.ArrayList;

public class DatePickerDialog  extends AlertDialog {

    RecyclerView recyclerView;
    TextView tvTitle;

    DatePickerDialogAdapter adapter;

    public DatePickerDialog(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pickerk_view, null, false);
        recyclerView = view.findViewById(R.id.dialog_recycler_view);
        tvTitle = view.findViewById(R.id.dialog_title);

        adapter = new DatePickerDialogAdapter((ItemSelectionListener) context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        setView(view);
    }

    public void setDialogTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDateList(ArrayList<AtomDate> atomDates) {
        adapter.setAtomDateList(atomDates);
    }

}

