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
import com.enrich.salonapp.data.model.Product.BrandModel;
import com.enrich.salonapp.data.model.Product.ProductCategoryModel;
import com.enrich.salonapp.data.model.Product.ProductRequestModel;
import com.enrich.salonapp.ui.activities.ProductActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductHomePageBrandAdapter extends RecyclerView.Adapter<ProductHomePageBrandAdapter.ProductHomePageBrandViewHolder> {

    Context context;
    ArrayList<BrandModel> list;
    LayoutInflater inflater;

    public ProductHomePageBrandAdapter(Context context, ArrayList<BrandModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ProductHomePageBrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_home_page_category_list_item, parent, false);
        return new ProductHomePageBrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHomePageBrandViewHolder holder, final int position) {
        BrandModel model = list.get(position);

        holder.categoryName.setText(model.Name.toUpperCase());
        Picasso.get().load(model.ImageUrl).placeholder(R.drawable.placeholder_ext).into(holder.categoryImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductRequestModel productRequestModel = new ProductRequestModel();
                ArrayList<Integer> brandId = new ArrayList<>();
                brandId.add(list.get(position).BrandId);
                productRequestModel.BrandIds = brandId;

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

    class ProductHomePageBrandViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_home_page_category_image)
        ImageView categoryImage;

        @BindView(R.id.product_home_page_category_name)
        TextView categoryName;

        public ProductHomePageBrandViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
