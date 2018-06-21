package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.util.OfferHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfferHomeAdapter extends RecyclerView.Adapter<OfferHomeAdapter.OfferHomeViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<OfferModel> list;

    public OfferHomeAdapter(Context context, ArrayList<OfferModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public OfferHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.offer_home_list_item, parent, false);
        return new OfferHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferHomeViewHolder holder, int position) {
        final OfferModel model = list.get(position);

        Picasso.get().load(model.OfferImageURL).placeholder(R.drawable.placeholder_ext).into(holder.offer);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfferHandler.handleOfferRedirection((Activity) context, model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OfferHomeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.offer_image)
        ImageView offer;

        public OfferHomeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
