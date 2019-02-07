package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.GenericCartModel;
import com.enrich.salonapp.util.HeaderRecyclerViewAdapterV2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

public class CartNewAdapter extends HeaderRecyclerViewAdapterV2 {

    Context context;
    ArrayList<GenericCartModel> list;
    LayoutInflater inflater;

    @Override
    public boolean useHeader() {
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public boolean useFooter() {
        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_list_footer_layout, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_list_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getBasicItemCount() {
        return 0;
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cart_item_name)
        TextView name;

        @BindView(R.id.cart_item_description)
        TextView description;

        @BindView(R.id.cart_item_price)
        TextView price;

        @BindView(R.id.cart_item_therapist)
        TextView therapist;

        @BindView(R.id.cart_item_remove)
        ImageView remove;

        public CartViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.footer_image)
        ImageView footerImage;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
