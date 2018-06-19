package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.Wallet.WalletHistoryModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletHistoryAdapter extends RecyclerView.Adapter<WalletHistoryAdapter.WalletHistoryViewHolder> {

    Context context;
    ArrayList<WalletHistoryModel> list;
    LayoutInflater inflater;

    public WalletHistoryAdapter(Context context, ArrayList<WalletHistoryModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public WalletHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wallet_list_item, parent, false);
        return new WalletHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletHistoryViewHolder holder, int position) {
        WalletHistoryModel model = list.get(position);

        holder.orderCenter.setText(model.Center.Name);
        holder.orderName.setText(model.OrderTitle);
        holder.walletAmount.setText(context.getResources().getString(R.string.rs_symbol) + " " + model.AppliedAmount);
        holder.orderDate.setText(model.OrderDate);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class WalletHistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.order_name)
        TextView orderName;

        @BindView(R.id.order_center)
        TextView orderCenter;

        @BindView(R.id.wallet_amount)
        TextView walletAmount;

        @BindView(R.id.order_date)
        TextView orderDate;

        public WalletHistoryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
