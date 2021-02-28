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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import net.abdulahad.suhasini.R;
import net.abdulahad.suhasini.data.TransactionType;
import net.abdulahad.suhasini.helper.ISOHelper;
import net.abdulahad.suhasini.helper.SwapTextHelper;
import net.abdulahad.suhasini.library.SwapTextView;
import net.abdulahad.suhasini.model.Deposit;

import java.util.ArrayList;

public class DepositAdapter extends RecyclerView.Adapter<DepositAdapter.ViewHolder> {

    private final View emptyView;
    private final RecyclerView recyclerView;

    ArrayList<Deposit> depositList;
    AlertDialog alertDialog;

    Context appContext;

    public DepositAdapter(View emptyView, RecyclerView recyclerView) {
        depositList = new ArrayList<>();
        this.emptyView = emptyView;
        this.recyclerView = recyclerView;


        /* do we need to use money sign swapping on tapping the amount */
        appContext = emptyView.getContext().getApplicationContext();

        alertDialog = new AlertDialog.Builder(emptyView.getContext()).setTitle("Detail").create();
    }

    public void setData(ArrayList<Deposit> depositList) {
        this.depositList = depositList;
        notifyDataSetChanged();

        int listShow = depositList.size() > 0 ? View.VISIBLE : View.GONE;
        int emptyViewShow = depositList.size() > 0 ? View.GONE : View.VISIBLE;

        recyclerView.setVisibility(listShow);
        emptyView.setVisibility(emptyViewShow);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.model_pocket_history_hori, parent, false);
        return new ViewHolder(view);
    }

    /* returns the difference in negative number */
    private double getSpendingDiffNeg(Deposit deposit) {
        return Math.min(deposit.current, deposit.previous) - Math.max(deposit.current, deposit.previous);
    }

    /* returns the difference in positive number */
    private double getSpendingDiffPos(Deposit deposit) {
        return Math.abs(deposit.current - deposit.previous);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Deposit deposit = depositList.get(position);

        double previous;

        boolean spending = deposit.transactionType > TransactionType.ADJUST_MONEY;
        if (spending) {
            /* we are subtracting max from min to have the negative sign and the expense as well */
            previous = getSpendingDiffNeg(deposit);
        } else { // either normal deposit addition or an adjustment for deposit
            boolean adjustment = deposit.transactionType == TransactionType.ADJUST_MONEY;
            if (adjustment) { // adjustment
                boolean negativeAdjustment = Double.compare(deposit.current, deposit.previous) == -1;
                if (negativeAdjustment) previous = getSpendingDiffNeg(deposit);
                else previous = getSpendingDiffPos(deposit);
            } else { // normal deposit
                previous = getSpendingDiffPos(deposit);
            }
        }

        holder.icon.setImageResource(TransactionType.getIcon(deposit.transactionType));

        /* set amount & current amount to the SwapTextView */
        SwapTextHelper.setMoneyToSTV(deposit.current, false, holder.current, appContext);
        SwapTextHelper.setMoneyToSTV(previous, true, holder.amount, appContext);

        holder.bg.setTag(deposit.tag);

        int[] colors = new int[]{TransactionType.getColor(deposit.transactionType), Color.BLACK};
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        drawable.setGradientRadius(180);
        holder.bg.setBackground(drawable);

        holder.dateTime.setText(ISOHelper.dateMonHourMin(deposit.getDateTime()));
    }

    @Override
    public int getItemCount() {
        return depositList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View bg;
        public ImageView icon;
        public TextView dateTime;
        public SwapTextView amount, current;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.bg);
            icon = itemView.findViewById(R.id.icon);
            amount = itemView.findViewById(R.id.amount);
            current = itemView.findViewById(R.id.current);
            dateTime = itemView.findViewById(R.id.date_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            alertDialog.setMessage((CharSequence) bg.getTag());
            alertDialog.show();
        }
    }

}
