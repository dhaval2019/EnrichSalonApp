package com.enrich.salonapp.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        final OfferModel model = list.get(position);

        holder.title.setText("" + model.OfferTitle);
        holder.subTitle.setText("" + model.OfferDescription);
        Picasso.with(context).load(model.OfferBannerURL).placeholder(R.drawable.placeholder_ext).into(holder.offer);

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
            } catch (ParseException e) {
                e.printStackTrace();
                holder.offerCountDownContainer.setVisibility(View.GONE);
            }
        } else {
            holder.offerCountDownContainer.setVisibility(View.GONE);
        }
    }

    class OfferViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.offer_image)
        ImageView offer;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.sub_title)
        TextView subTitle;

        @BindView(R.id.offer_end_day_count_down)
        CountdownView offerEndDayCountDown;

        @BindView(R.id.offer_count_down_container)
        RelativeLayout offerCountDownContainer;

        public OfferViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
