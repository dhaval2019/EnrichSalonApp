package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enrich.salonapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VariantRecyclerViewAdapter extends RecyclerView.Adapter<VariantRecyclerViewAdapter.VariantViewHolder> {

    Context context;
    ArrayList<String> list;
    LayoutInflater inflater;

    public VariantRecyclerViewAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public VariantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.variant_list_item, parent, false);
        return new VariantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VariantViewHolder holder, int position) {
        holder.serviceName.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VariantViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.service_name)
        TextView serviceName;

        public VariantViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
