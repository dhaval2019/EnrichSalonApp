package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.GenericCartModel;
import com.enrich.salonapp.ui.activities.CartActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    ArrayList<GenericCartModel> list;
    LayoutInflater inflater;
    EnrichApplication application;
    CartActivity activity;

    public CartAdapter(Context context, ArrayList<GenericCartModel> list, Activity activity) {
        this.activity = (CartActivity) activity;
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        application = (EnrichApplication) context.getApplicationContext();
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_list_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        final GenericCartModel model = list.get(position);
        holder.name.setText(model.Name);

        if (model.getSlotTime() != null) {
            try {
                SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat stringToTime = new SimpleDateFormat("hh:mm:ss");

                Date date = stringToDate.parse(model.getSlotTime());
                Date time = stringToTime.parse(model.getSlotTime().substring(11, model.getSlotTime().length()));

                SimpleDateFormat dateToString = new SimpleDateFormat("dd MMM, yyyy");
                SimpleDateFormat timeToString = new SimpleDateFormat("hh:mm");

                String dateStr = dateToString.format(date);
                String timeStr = timeToString.format(time);

                holder.description.setText(dateStr + " @" + timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
                holder.description.setVisibility(View.GONE);
            }
        } else {
            holder.description.setVisibility(View.GONE);
        }

        holder.price.setText(context.getResources().getString(R.string.Rs) + " " + model.getPrice());
        holder.therapist.setText(model.getTherapistModel().Name);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.removeFromCartBasedOnId(model.Id);
                notifyDataSetChanged();
                activity.updatePriceAndQuantityView();
                if (application.isCartEmpty())
                    activity.onBackPressed();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
}
