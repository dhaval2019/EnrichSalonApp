package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.SpinModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WinningSpinRecyclerView extends RecyclerView.Adapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<SpinModel> list;
    int pos;

    public WinningSpinRecyclerView(Context context, ArrayList<SpinModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.winning_spin_item, parent, false);
        return new WinningSpinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WinningSpinViewHolder winningSpinViewHolder = (WinningSpinViewHolder) holder;
        pos = position;

        winningSpinViewHolder.spinCountText.setText("Spin #" + (pos + 1));
        winningSpinViewHolder.spinPointsWon.setText("" + list.get(position).prizeWon);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class WinningSpinViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.spin_count_text)
        TextView spinCountText;

        @BindView(R.id.spin_points_won)
        TextView spinPointsWon;

        public WinningSpinViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
