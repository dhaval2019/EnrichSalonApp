package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.Wallet.WalletModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {

    Context context;
    List<WalletModel> list;
    LayoutInflater inflater;

    public WalletAdapter(Context context, List<WalletModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wallet_list_item, parent, false);
        return new WalletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletViewHolder holder, int position) {
        WalletModel model = list.get(position);

        holder.walletName.setText(model.Walletfor);
        holder.walletAmount.setText(context.getResources().getString(R.string.rs_symbol) + " " + (int)Math.round(model.Amount));

        try {
            SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd");
            Date tempDate = stringToDate.parse(model.WalletValidityDate.substring(0, 10));

            SimpleDateFormat dateToString = new SimpleDateFormat("dd MMM, yyyy");

            holder.walletValidityDate.setText(dateToString.format(tempDate));

        } catch (Exception e) {
            e.printStackTrace();
            holder.walletValidityDate.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class WalletViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.wallet_name)
        TextView walletName;

        @BindView(R.id.wallet_amount)
        TextView walletAmount;

        @BindView(R.id.wallet_validity_date)
        TextView walletValidityDate;

        public WalletViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
