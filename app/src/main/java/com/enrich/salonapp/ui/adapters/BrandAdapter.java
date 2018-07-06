package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.Product.BrandModel;
import com.enrich.salonapp.ui.activities.ProductFilterActivity;
import com.enrich.salonapp.util.EnrichUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {

    ArrayList<BrandModel> list;
    Activity activity;
    LayoutInflater inflater;

    public BrandAdapter(ArrayList<BrandModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.brand_list_item, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        final BrandModel model = list.get(position);
        holder.brandLabel.setText(model.Name);

        if (isInBrandsId(model.BrandId)) {
            holder.brandCheckbox.setChecked(true);
        } else {
            holder.brandCheckbox.setChecked(false);
        }

        holder.brandCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInBrandsId(model.BrandId)) {
                    ((ProductFilterActivity) activity).getProductRequestModel().BrandIds.add(model.BrandId);
                } else {
                    ((ProductFilterActivity) activity).getProductRequestModel().BrandIds.remove(new Integer(model.BrandId));
                }
                notifyDataSetChanged();
            }
        });
    }

    private boolean isInBrandsId(int id) {
        for (int i = 0; i < ((ProductFilterActivity) activity).getProductRequestModel().BrandIds.size(); i++) {
            if (((ProductFilterActivity) activity).getProductRequestModel().BrandIds.get(i) == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BrandViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.brand_label)
        TextView brandLabel;

        @BindView(R.id.brand_checkbox)
        CheckBox brandCheckbox;

        public BrandViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
