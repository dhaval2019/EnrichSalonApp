package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.util.OfferHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<OfferModel> list;

    public OfferAdapter(Context context, ArrayList<OfferModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.offer_list_item, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        final OfferModel model = list.get(position);

        holder.title.setText("" + model.OfferTitle);
        holder.subTitle.setText("" + model.OfferDescription);
        Picasso.get().load(model.OfferBannerURL).into(holder.offer);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfferHandler.handleOfferRedirection((Activity) context, model);
            }
        });
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.offer_image)
        ImageView offer;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.sub_title)
        TextView subTitle;

        public OfferViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
