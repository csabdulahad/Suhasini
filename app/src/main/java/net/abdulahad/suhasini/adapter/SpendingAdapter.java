package net.abdulahad.suhasini.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.data.TransactionType;
import net.abdulahad.suhasini.helper.NumberFormatHelper;
import net.abdulahad.suhasini.helper.SwapTextHelper;
import net.abdulahad.suhasini.library.SwapTextView;
import net.abdulahad.suhasini.model.Spending;
import net.abdulahad.suhasini.protocol.ItemSelectionListener;

import java.util.ArrayList;

public class SpendingAdapter extends RecyclerView.Adapter<SpendingAdapter.ViewHolder> {

    private final Context appContext;

    private ItemSelectionListener listener;

    private final View emptyView;
    private final RecyclerView recyclerView;

    private ArrayList<Spending> spendingList;
    private double unitSpending = 1;

    public SpendingAdapter(View emptyView, RecyclerView recyclerView) {
        this.appContext = emptyView.getContext().getApplicationContext();
        spendingList = new ArrayList<>();
        this.emptyView = emptyView;
        this.recyclerView = recyclerView;
    }

    public void setData(ArrayList<Spending> spendingList, double totalSpending) {
        this.spendingList = spendingList;
        notifyDataSetChanged();

        int listShow = spendingList.size() > 0 ? View.VISIBLE : View.GONE;
        int emptyViewShow = spendingList.size() > 0 ? View.GONE : View.VISIBLE;

        if (totalSpending > 0) unitSpending = totalSpending / 100;
        else unitSpending = 1;

        for (Spending spending : spendingList) spending.unitSpending = unitSpending;

        recyclerView.setVisibility(listShow);
        emptyView.setVisibility(emptyViewShow);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.model_spending_category_hori, parent, false);
        return new SpendingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Spending spending = spendingList.get(position);
        SwapTextHelper.setMoneyToSTV(spending.total, false, holder.amount, appContext);
        holder.description.setText(spending.description);
        holder.icon.setImageResource(TransactionType.getIcon(spending.type));

        if (unitSpending > 0)
            holder.percent.setText(NumberFormatHelper.getPercent(spending.total / unitSpending));

        int[] colors = new int[]{TransactionType.getColor(spending.type), Color.BLACK};
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        drawable.setGradientRadius(180);
        holder.bg.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return spendingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SwapTextView amount;
        public TextView description, percent;
        public ImageView icon;

        public View bg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bg = itemView;
            amount = itemView.findViewById(R.id.amount);
            description = itemView.findViewById(R.id.description);
            percent = itemView.findViewById(R.id.percent);
            icon = itemView.findViewById(R.id.icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemSelected(getAdapterPosition());
                    }
                }
            });
        }
    }

    public Spending getSpending(int adapterPos) {
        if (spendingList == null || spendingList.isEmpty() || adapterPos > spendingList.size() - 1)
            return null;
        return spendingList.get(adapterPos);
    }

    public void setListener(ItemSelectionListener listener) {
        this.listener = listener;
    }

}
