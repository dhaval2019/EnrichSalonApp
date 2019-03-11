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
import com.enrich.salonapp.ui.activities.ProductSubCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductSubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ProductSubCategoryModel> list;
    LayoutInflater inflater;

    public ProductSubCategoryAdapter(Context context, ArrayList<ProductSubCategoryModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.product_sub_category_list_item, viewGroup, false);
        return new ProductSubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ProductSubCategoryViewHolder holder = (ProductSubCategoryViewHolder) viewHolder;

        ProductSubCategoryModel model = list.get(position);

        holder.productSubCategoryName.setText(model.Name);

        Picasso.with(context).load(model.ProductCategory.ProductCategoryImageUrl).placeholder(R.drawable.placeholder).into(holder.productSubCategoryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductRequestModel productRequestModel = new ProductRequestModel();
                ArrayList<Integer> categoryId = new ArrayList<>();
                ArrayList<Integer> subCategoryId = new ArrayList<>();
                categoryId.add(list.get(position).ProductCategory.Id);
                subCategoryId.add(list.get(position).Id);
                productRequestModel.ProductCategoryIds = categoryId;
                productRequestModel.ProductSubCategoryIds = subCategoryId;

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

        ImageView productSubCategoryImage;
        TextView productSubCategoryName;

        public ProductSubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            productSubCategoryImage = itemView.findViewById(R.id.product_sub_category_image);
            productSubCategoryName = itemView.findViewById(R.id.product_sub_category_name);
        }
    }
}
