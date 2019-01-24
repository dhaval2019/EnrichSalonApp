package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.SpinModel;
import com.enrich.salonapp.ui.activities.BeautyAndBlingSpinActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpinRecyclerViewAdapter extends RecyclerView.Adapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<SpinModel> list;

    String centerId;
    boolean isNewUser;
    String purchaseType;
    private String invoiceId;

    public SpinRecyclerViewAdapter(Context context, ArrayList<SpinModel> list, boolean isNewUser, String centerId, String purchaseType, String invoiceId) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isNewUser = isNewUser;
        this.centerId = centerId;
        this.purchaseType = purchaseType;
        this.invoiceId = invoiceId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.spins_list_item, parent, false);
        return new SpinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        SpinViewHolder spinViewHolder = (SpinViewHolder) holder;
        spinViewHolder.spinNumber.setText("" + list.get(position).number);

        if (list.get(position).isSpinTaken) {
            spinViewHolder.isSpinTaken.setVisibility(View.VISIBLE);
        } else {
            spinViewHolder.isSpinTaken.setVisibility(View.GONE);
        }

        spinViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!list.get(position).isSpinTaken) {
                    Intent intent = new Intent(context, BeautyAndBlingSpinActivity.class);
                    intent.putExtra("IsNewUser", isNewUser);
                    intent.putExtra("SpinIndex", position);
                    intent.putExtra("CenterId", centerId);
                    intent.putExtra("PurchaseType", purchaseType);
                    intent.putExtra("InvoiceId", invoiceId);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SpinViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.spin_number)
        TextView spinNumber;

        @BindView(R.id.is_spin_taken)
        TextView isSpinTaken;

        public SpinViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
