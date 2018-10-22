package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.data.model.Product.ProductSubCategoryModel;
import com.enrich.salonapp.ui.activities.ProductActivity;
import com.enrich.salonapp.util.EnrichUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductSubCategoryHomeAdapter extends RecyclerView.Adapter<ProductSubCategoryHomeAdapter.ProductSubCategoryViewHolder> {

    Context context;
    ArrayList<ProductSubCategoryModel> list;
    LayoutInflater inflater;

    public ProductSubCategoryHomeAdapter(Context context, ArrayList<ProductSubCategoryModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ProductSubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_sub_category_home_list_item, parent, false);
        return new ProductSubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSubCategoryViewHolder holder, final int position) {
        ProductSubCategoryModel model = list.get(position);

        holder.subCategoryName.setText(model.Name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductRequestModel productRequestModel = new ProductRequestModel();
                ArrayList<Integer> subCategoryId = new ArrayList<>();
                subCategoryId.add(list.get(position).Id);
                productRequestModel.ProductSubCategoryIds = subCategoryId;

                EnrichUtils.log(EnrichUtils.newGson().toJson(productRequestModel));

                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("ProductRequestModel", productRequestModel);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductSubCategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sub_category_image)
        ImageView subCategoryImage;

        @BindView(R.id.sub_category_name)
        TextView subCategoryName;

        public ProductSubCategoryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
