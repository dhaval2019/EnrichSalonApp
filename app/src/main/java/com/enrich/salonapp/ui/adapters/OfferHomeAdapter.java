package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.OfferHandler;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

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

        if (model.IsTimerEnable) {
            holder.offerCountDownContainer.setVisibility(View.VISIBLE);

            try {
                String dateStr = model.ValidTill.substring(0, 10) + " " + model.ValidTill.substring(11);
                SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date endDate = stringToDate.parse(dateStr);
                Date startDate = new Date();

                long numberOfDaysInMilliseconds = (endDate.getTime() - startDate.getTime());

                holder.offerEndDayCountDown.start(numberOfDaysInMilliseconds);
                holder.offerEndDayCountDown.setTag(R.id.offer_end_day_count_down, position);
            } catch (ParseException e) {
                e.printStackTrace();
                holder.offerCountDownContainer.setVisibility(View.GONE);
            }
        } else {
            holder.offerCountDownContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OfferHomeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.offer_image)
        ImageView offer;

        @BindView(R.id.offer_count_down_container)
        RelativeLayout offerCountDownContainer;

        @BindView(R.id.offer_end_day_count_down)
        CountdownView offerEndDayCountDown;

        public OfferHomeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
