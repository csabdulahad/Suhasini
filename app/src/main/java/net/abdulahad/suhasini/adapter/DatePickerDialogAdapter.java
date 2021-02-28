package net.abdulahad.suhasini.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.helper.CircularTextView;
import net.abdulahad.suhasini.library.AtomDate;
import net.abdulahad.suhasini.protocol.ItemSelectionListener;

import java.util.ArrayList;

public class DatePickerDialogAdapter extends RecyclerView.Adapter<DatePickerDialogAdapter.ViewHolder> {

    private final ItemSelectionListener listener;
    ArrayList<AtomDate> atomDateList;

    public DatePickerDialogAdapter(ItemSelectionListener listener) {
        this.listener = listener;
        atomDateList = new ArrayList<>();
    }

    public void setAtomDateList(ArrayList<AtomDate> atomDateList) {
        this.atomDateList = atomDateList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.model_dialog_date_pick, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AtomDate atomDate = atomDateList.get(position);
        holder.date.setText(atomDate.dayLeadByZero());
        holder.date.setTag(position);
    }

    @Override
    public int getItemCount() {
        return atomDateList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dialog_date);

            CircularTextView circularTextView = (CircularTextView) date;
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemSelected(Integer.parseInt(v.getTag().toString()));
                }
            });
        }

    }

}
