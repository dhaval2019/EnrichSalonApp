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
import com.enrich.salonapp.data.model.Product.ProductSubCategoryModel;
import com.enrich.salonapp.ui.activities.ProductFilterActivity;
import com.enrich.salonapp.util.EnrichUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductSubCategoryAdapter extends RecyclerView.Adapter<ProductSubCategoryAdapter.SubCategoryViewHolder> {

    Activity activity;
    ArrayList<ProductSubCategoryModel> list = new ArrayList<>();
    LayoutInflater inflater;

    public ProductSubCategoryAdapter(Activity activity, ArrayList<ProductSubCategoryModel> list) {
        this.activity = activity;

        for (int i = 0; i < ((ProductFilterActivity) activity).getProductRequestModel().ProductCategoryIds.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (((ProductFilterActivity) activity).getProductRequestModel().ProductCategoryIds.get(i) == list.get(j).ProductCategory.Id) {
                    this.list.add(list.get(j));
                }
            }
        }
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_sub_categories_list_item, parent, false);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        final ProductSubCategoryModel model = list.get(position);
        holder.subCategoryLabel.setText(model.Name);

        if (isInSubCategoryId(model.Id)) {
            holder.subCategoryCheckbox.setChecked(true);
        } else {
            holder.subCategoryCheckbox.setChecked(false);
        }

        holder.subCategoryCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInSubCategoryId(model.Id)) {
                    ((ProductFilterActivity) activity).getProductRequestModel().ProductSubCategoryIds.add(model.Id);
                } else {
                    ((ProductFilterActivity) activity).getProductRequestModel().ProductSubCategoryIds.remove(new Integer(model.Id));
                }
                notifyDataSetChanged();
            }
        });
    }

    private boolean isInSubCategoryId(int id) {
        for (int i = 0; i < ((ProductFilterActivity) activity).getProductRequestModel().ProductSubCategoryIds.size(); i++) {
            if (((ProductFilterActivity) activity).getProductRequestModel().ProductSubCategoryIds.get(i) == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SubCategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sub_category_label)
        TextView subCategoryLabel;

        @BindView(R.id.sub_category_checkbox)
        CheckBox subCategoryCheckbox;

        public SubCategoryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
