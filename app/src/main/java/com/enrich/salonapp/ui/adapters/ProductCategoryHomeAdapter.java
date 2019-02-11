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
import com.enrich.salonapp.data.model.Product.ProductCategoryModel;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.ui.activities.ProductActivity;
import com.enrich.salonapp.util.EnrichUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductCategoryHomeAdapter extends RecyclerView.Adapter<ProductCategoryHomeAdapter.ProductCategoryViewHolder>{

    Context context;
    ArrayList<ProductCategoryModel> list;
    LayoutInflater inflater;

    public ProductCategoryHomeAdapter(Context context, ArrayList<ProductCategoryModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ProductCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_sub_category_home_list_item, parent, false);
        return new ProductCategoryHomeAdapter.ProductCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryViewHolder holder, final int position) {
        ProductCategoryModel model = list.get(position);

        holder.subCategoryName.setText(model.Name);

        Picasso.with(context).load(model.ProductCategoryImageUrl).placeholder(R.drawable.placeholder).into(holder.subCategoryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductRequestModel productRequestModel = new ProductRequestModel();
                ArrayList<Integer> subCategoryId = new ArrayList<>();
                subCategoryId.add(list.get(position).Id);
                productRequestModel.ProductCategoryIds = subCategoryId;

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

    class ProductCategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sub_category_image)
        ImageView subCategoryImage;

        @BindView(R.id.sub_category_name)
        TextView subCategoryName;

        public ProductCategoryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
