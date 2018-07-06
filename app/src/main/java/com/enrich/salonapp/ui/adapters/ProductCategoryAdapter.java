package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.Product.ProductCategoryModel;
import com.enrich.salonapp.ui.activities.ProductFilterActivity;
import com.enrich.salonapp.util.EnrichUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.CategoryViewHolder> {

    Activity activity;
    ArrayList<ProductCategoryModel> list;
    LayoutInflater inflater;

    public ProductCategoryAdapter(Activity activity, ArrayList<ProductCategoryModel> list) {
        this.activity = activity;
        this.list = list;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_categories_list_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final ProductCategoryModel model = list.get(position);
        holder.categoryLabel.setText(model.Name);

        if (isInCategoryId(model.Id)) {
            holder.categoryCheckbox.setChecked(true);
        } else {
            holder.categoryCheckbox.setChecked(false);
        }

        holder.categoryCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInCategoryId(model.Id)) {
                    ((ProductFilterActivity) activity).getProductRequestModel().ProductCategoryIds.add(model.Id);
                } else {
                    ((ProductFilterActivity) activity).getProductRequestModel().ProductCategoryIds.remove(new Integer(model.Id));
                }
                notifyDataSetChanged();

                ((ProductFilterActivity) activity).updateSubCategory();
            }
        });
    }

    private boolean isInCategoryId(int id) {
        for (int i = 0; i < ((ProductFilterActivity) activity).getProductRequestModel().ProductCategoryIds.size(); i++) {
            if (((ProductFilterActivity) activity).getProductRequestModel().ProductCategoryIds.get(i) == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category_label)
        TextView categoryLabel;

        @BindView(R.id.category_checkbox)
        CheckBox categoryCheckbox;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
