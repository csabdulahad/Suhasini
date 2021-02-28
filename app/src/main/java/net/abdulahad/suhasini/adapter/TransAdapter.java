package net.abdulahad.suhasini.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.data.TransactionType;
import net.abdulahad.suhasini.helper.ISOHelper;
import net.abdulahad.suhasini.helper.SwapTextHelper;
import net.abdulahad.suhasini.helper.ViewHelper;
import net.abdulahad.suhasini.library.SwapTextView;
import net.abdulahad.suhasini.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransAdapter extends RecyclerView.Adapter<TransAdapter.ViewHolder> {

    private final Context appContext;

    RecyclerView recyclerView;
    View emptyView;
    List<Transaction> transactionList;

    public void setData(ArrayList<Transaction> transactionList) {
        this.transactionList = transactionList;
        notifyDataSetChanged();

        int listShow = transactionList.size() > 0 ? View.VISIBLE : View.GONE;
        int emptyViewShow = transactionList.size() > 0 ? View.GONE : View.VISIBLE;

        recyclerView.setVisibility(listShow);
        emptyView.setVisibility(emptyViewShow);
    }

    public TransAdapter(View emptyView, RecyclerView recyclerView) {
        transactionList = new ArrayList<>();
        this.emptyView = emptyView;
        this.recyclerView = recyclerView;

        appContext = emptyView.getContext().getApplicationContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.model_trans, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        SwapTextHelper.setMoneyToSTV(transaction.amount, false, holder.amount, appContext);
        holder.icon.setImageResource(TransactionType.getIcon(transaction.type));
        ViewHelper.setTint(TransactionType.getColor(transaction.type), holder.icon);
        holder.tag.setText(transaction.tag);
        holder.time.setText(ISOHelper.getTime(transaction.getDateTime()));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public ImageView icon;
        public TextView tag, time;
        public SwapTextView amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            icon = itemView.findViewById(R.id.trans_icon);
            amount = itemView.findViewById(R.id.trans_amount);
            tag = itemView.findViewById(R.id.trans_tag);
            time = itemView.findViewById(R.id.trans_time);
        }

    }

}
