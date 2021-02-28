package net.abdulahad.suhasini.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.data.TransactionType;
import net.abdulahad.suhasini.helper.ViewHelper;
import net.abdulahad.suhasini.protocol.ItemSelectionListener;

public class TransTypeAdapter extends RecyclerView.Adapter<TransTypeAdapter.ViewHolder> {

    private final ItemSelectionListener listener;

    public TransTypeAdapter(ItemSelectionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.model_label, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransTypeAdapter.ViewHolder holder, int position) {
        position += TransactionType.OFFSET_FOR_TYPE;
        int colorId = TransactionType.getColor(position);
        holder.icon.setImageResource(TransactionType.getIcon(position));
        holder.icon.setTag(String.valueOf(position));
        ViewHelper.setTint(colorId, holder.icon);
        holder.label.setText(TransactionType.getLabel(position));
        holder.label.setTextColor(colorId);
    }

    @Override
    public int getItemCount() {
        return TransactionType.numOfType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView label;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            label = itemView.findViewById(R.id.label);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemSelected(Integer.parseInt((String) icon.getTag()));
                }
            });
        }
    }

}
