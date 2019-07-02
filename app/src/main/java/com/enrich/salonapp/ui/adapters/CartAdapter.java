package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.CartItem;
import com.enrich.salonapp.data.model.GenericCartModel;
import com.enrich.salonapp.ui.activities.CartActivity;
import com.enrich.salonapp.ui.activities.PackageDetailActivity;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

import static com.enrich.salonapp.data.model.CartItem.CART_TYPE_SUB_PACKAGE;
import static com.enrich.salonapp.data.model.Package.PackageBundle.BUNDLE_ITEM_TYPE_CASHBACK;
import static com.enrich.salonapp.data.model.Package.PackageBundle.BUNDLE_ITEM_TYPE_PRODUCT;
import static com.enrich.salonapp.data.model.Package.PackageBundle.BUNDLE_ITEM_TYPE_SERVICE;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.cart_list_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder recyclerViewHolder, int position) {
        CartViewHolder holder = (CartViewHolder) recyclerViewHolder;
        final GenericCartModel model = list.get(position);

        if (model.getCartItemType() == GenericCartModel.CART_TYPE_SERVICES) {
            holder.name.setText(String.format("%s %s x %d", model.SubCategoryName, model.Name.trim(), list.get(position).Quantity));

            if (model.getTherapistModel() != null) {
                holder.therapist.setText(String.format("%s %s", model.getTherapistModel().FirstName, model.getTherapistModel().LastName));
            } else {
                holder.therapist.setVisibility(View.GONE);
            }
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

            if (EnrichUtils.getUserData(activity).IsMember == Constants.IS_MEMBER) {
                holder.price.setText(String.format("%s %d", context.getResources().getString(R.string.Rs), (int)Math.round( model.getMembershipPrice())));
            }else {
                holder.price.setText(String.format("%s %d", context.getResources().getString(R.string.Rs), (int)Math.round( model.getPrice())));
            }

            holder.deliveryInformation.setVisibility(View.GONE);
            holder.deliveryPeriod.setVisibility(View.GONE);

        } else if (model.getCartItemType() == GenericCartModel.CART_TYPE_SUB_PACKAGE) { // FOR PACKAGES
            holder.deliveryInformation.setVisibility(View.VISIBLE);
            holder.deliveryPeriod.setVisibility(View.VISIBLE);
            holder.packageRatingBar.setRating((float) PackageDetailActivity.packageBundleSelected.get(position).BundleRatings);

            holder.deliveryInformation.setText(model.DeliveryInformation);
            holder.deliveryPeriod.setText(model.DeliveryPeriod);

            if (model.getPackageBundleItemType() == BUNDLE_ITEM_TYPE_SERVICE) {

                holder.name.setText(model.Name);
                holder.therapist.setText(String.format("%d Service(s)", model.getPackageBundleItemCount()));
                holder.description.setText(String.format("Quantity: %d", model.Quantity));
                int i = (int)Math.round(model.getPrice() * model.Quantity);
                holder.price.setText(String.format("%s %s", context.getResources().getString(R.string.Rs), i));

            } else if (model.getPackageBundleItemType() == BUNDLE_ITEM_TYPE_PRODUCT) {

                holder.name.setText(model.Name);
                holder.therapist.setText(String.format("%d Product(s)", model.getPackageBundleItemCount()));
                holder.description.setText(String.format("Quantity: %d", model.Quantity));
                int i = (int)Math.round( model.getPrice() * model.Quantity);
                holder.price.setText(String.format("%s %s", context.getResources().getString(R.string.Rs),i ));

            } else if (model.getPackageBundleItemType() == BUNDLE_ITEM_TYPE_CASHBACK) {
                holder.name.setText(model.Name);
                holder.therapist.setVisibility(View.GONE);
                holder.description.setText(String.format("Quantity: %d", model.Quantity));
                int i = (int)Math.round(  model.getPrice() * model.Quantity);
                holder.price.setText(String.format("%s %s", context.getResources().getString(R.string.Rs), i));
            } else {
                holder.therapist.setVisibility(View.GONE);
            }
        } else if (model.getCartItemType() == GenericCartModel.CART_TYPE_PRODUCTS) {
            holder.name.setText(model.Name);
            holder.therapist.setVisibility(View.GONE);
            holder.description.setText(String.format("Quantity: %d", model.Quantity));
            int i = (int)Math.round( model.getPrice() * model.Quantity);
            holder.price.setText(String.format("%s %s", context.getResources().getString(R.string.Rs), i));

            holder.deliveryInformation.setVisibility(View.GONE);
            holder.deliveryPeriod.setVisibility(View.GONE);
        } else {
            holder.therapist.setVisibility(View.GONE);
        }

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

        @BindView(R.id.package_rating_bar)
        RatingBar packageRatingBar;

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


        @BindView(R.id.cart_item_delivery_period)
        TextView deliveryPeriod;

        @BindView(R.id.cart_item_delivery_information)
        TextView deliveryInformation;

        public CartViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
