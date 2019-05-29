package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.Product.ProductModel;
import com.enrich.salonapp.ui.activities.ProductDetailActivity;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    public static final int FULL = 7;
    public static final int COMPACT = 9;

    Context context;
    ArrayList<ProductModel> list;
    LayoutInflater inflater;
    int viewType;

    public ProductsAdapter(Context context, ArrayList<ProductModel> list, int viewType) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (this.viewType == FULL) {
            view = inflater.inflate(R.layout.product_list_item, parent, false);
        } else {
            view = inflater.inflate(R.layout.product_list_item_compact, parent, false);
        }
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, final int position) {
        ProductModel model = list.get(position);

        holder.productTitle.setText(model.ProductTitle);

        DecimalFormat format = new DecimalFormat("#,###.##");
        holder.productSubTitle.setText(String.format("%s %s", context.getString(R.string.rs_symbol), format.format(model.ProductAmount)));
//        holder.productSubTitle.setText(model.ProductDescription);

        Picasso.with(context).load(model.ImageURL).placeholder(R.drawable.placeholder).into(holder.productImageSmall);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("ProductId", list.get(position).ProductId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(ArrayList<ProductModel> productList) {
        this.list.clear();
        this.list.addAll(productList);
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<ProductModel> productList) {
        this.list.addAll(productList);
        notifyDataSetChanged();
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_image_small)
        ImageView productImageSmall;

        @BindView(R.id.product_title_subtitle_container)
        LinearLayout productTitleSubtitleContainer;

        @BindView(R.id.product_title)
        TextView productTitle;

        @BindView(R.id.product_sub_title)
        TextView productSubTitle;

        public ProductsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
