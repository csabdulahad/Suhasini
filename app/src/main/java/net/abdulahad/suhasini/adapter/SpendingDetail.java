package net.abdulahad.suhasini.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.helper.ISOHelper;
import net.abdulahad.suhasini.helper.SwapTextHelper;
import net.abdulahad.suhasini.helper.ViewHelper;
import net.abdulahad.suhasini.library.SwapTextView;
import net.abdulahad.suhasini.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class SpendingDetail extends RecyclerView.Adapter<SpendingDetail.ViewHolder> {

    private Context appContext;
    List<Transaction> transactionList;

    public void setData(ArrayList<Transaction> transactionList) {
        this.transactionList = transactionList;
        notifyDataSetChanged();
    }

    public SpendingDetail() {
        transactionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.model_spending_detail, parent, false);

        if (appContext == null) appContext = view.getContext().getApplicationContext();
        return new SpendingDetail.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        SwapTextHelper.setMoneyToSTV(transaction.amount, false, holder.amount, appContext);
        holder.tag.setText(transaction.tag);
        holder.dateTime.setText(ISOHelper.dateMonHourMin(transaction.getDateTime()));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SwapTextView amount;
        TextView tag, dateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            tag = itemView.findViewById(R.id.tag);
            dateTime = itemView.findViewById(R.id.date_time);
        }

    }

}
