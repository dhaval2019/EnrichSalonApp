package com.enrich.salonapp.ui.adapters.ProductHomePage;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductHomePageCategoryAdapter extends RecyclerView.Adapter<ProductHomePageCategoryAdapter.ProductHomePageCategoryViewHolder> {

    Context context;
    ArrayList<ProductCategoryModel> list;
    LayoutInflater inflater;

    public ProductHomePageCategoryAdapter(Context context, ArrayList<ProductCategoryModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ProductHomePageCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_home_page_category_list_item, parent, false);
        return new ProductHomePageCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHomePageCategoryViewHolder holder, final int position) {
        final ProductCategoryModel model = list.get(position);

        holder.categoryName.setText(model.Name.toUpperCase());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductRequestModel productRequestModel = new ProductRequestModel();
                ArrayList<Integer> brandId = new ArrayList<>();
                brandId.add(list.get(position).Id);
                productRequestModel.ProductCategoryIds = brandId;

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

    class ProductHomePageCategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_home_page_category_image)
        ImageView categoryImage;

        @BindView(R.id.product_home_page_category_name)
        TextView categoryName;

        public ProductHomePageCategoryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
