package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.Package.BaseChildModel;
import com.enrich.salonapp.data.model.Package.PackageBundle;
import com.enrich.salonapp.ui.activities.PackageDetailActivity;
import com.enrich.salonapp.util.EnrichUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandablePackageBundleAdapter extends ExpandableRecyclerAdapter<PackageBundle, BaseChildModel, ExpandablePackageBundleAdapter.PackageBundleParentViewHolder, ExpandablePackageBundleAdapter.PackageBundleChildViewHolder> {

    PackageDetailActivity activity;
    List<PackageBundle> list;
    LayoutInflater inflater;
    EnrichApplication application;

    public ExpandablePackageBundleAdapter(PackageDetailActivity activity, @NonNull List<PackageBundle> list) {
        super(list);
        this.activity = activity;
        this.list = list;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        application = (EnrichApplication) activity.getApplicationContext();
    }

    @NonNull
    @Override
    public PackageBundleParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = inflater.inflate(R.layout.package_bundle_parent_list_item, parentViewGroup, false);
        return new PackageBundleParentViewHolder(view);
    }

    @NonNull
    @Override
    public PackageBundleChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = inflater.inflate(R.layout.package_bundle_child_list_item, childViewGroup, false);
        return new PackageBundleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull PackageBundleParentViewHolder parentViewHolder, final int parentPosition, @NonNull PackageBundle parent) {
        final PackageBundle model = list.get(parentPosition);

        parentViewHolder.packageBundleName.setText(model.Name);
        parentViewHolder.packageBundleSubTitle.setText(model.SubTitle);

        double tempValue = Double.valueOf(model.getPrice());
        parentViewHolder.packageBundlePrice.setText("Price: " + activity.getResources().getString(R.string.Rs) + " " + (int) tempValue);

        parentViewHolder.viewServicesButton.setText("View Details");
        parentViewHolder.packageBundleCount.setText("" + application.getItemQuantity(model));

        try {
            SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd");
            Date date = stringToDate.parse(model.ValidTillDate);

            SimpleDateFormat dateToString = new SimpleDateFormat("dd/MM/yyyy");
            parentViewHolder.packageBundleValidity.setText("Offer ends in " + EnrichUtils.printDifference(new Date(), date)[0] + " days");
        } catch (Exception e) {
            e.printStackTrace();
        }

        parentViewHolder.packageBundleUsageValidity.setText("Usage Validity: " + model.ValidityDays + " days");
        parentViewHolder.packageRatingBar.setRating((float) model.BundleRatings);
        parentViewHolder.numberOfPeopleBought.setText(model.PackageBundleSold + " packages sold");

        parentViewHolder.packageBundleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.setMaxQuantityAllowed(model.Quantity);
                if (application.alreadyExist(model)) {
                    application.increaseQuantity(model);
                    activity.updateCart();
                    notifyParentChanged(parentPosition);
                } else {
                    application.addToCart(model);
                    model.Count++;
                    activity.updateCart();
                    notifyParentChanged(parentPosition);
                }
            }
        });

        parentViewHolder.packageBundleRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (application.alreadyExist(model)) {
                    application.decreaseQuantity(model);
                    activity.updateCart();
                    notifyParentChanged(parentPosition);
                } else {
                    application.removeFromCart(model);
                    activity.updateCart();
                    notifyParentChanged(parentPosition);
                }
            }
        });
    }

    @Override
    public void onBindChildViewHolder(@NonNull PackageBundleChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull BaseChildModel child) {

        if (!list.get(parentPosition).Cashbacks.isEmpty()) {
            childViewHolder.packageBundleChildPrice.setVisibility(View.VISIBLE);
        } else if (!list.get(parentPosition).packageBundleService.isEmpty()) {
            childViewHolder.packageBundleChildPrice.setVisibility(View.GONE);
        } else if (!list.get(parentPosition).packageBundleProduct.isEmpty()) {
            childViewHolder.packageBundleChildPrice.setVisibility(View.GONE);
        }
        childViewHolder.packageBundleChildName.setText(child.getName());

        double tempValue = Double.valueOf(child.getPrice());
        childViewHolder.packageBundleChildPrice.setText(activity.getResources().getString(R.string.Rs) + " " + (int) tempValue);
    }

    class PackageBundleParentViewHolder extends ParentViewHolder<PackageBundle, BaseChildModel> {

        @BindView(R.id.package_bundle_name)
        TextView packageBundleName;

        @BindView(R.id.package_bundle_price)
        TextView packageBundlePrice;

        @BindView(R.id.package_bundle_validity)
        TextView packageBundleValidity;

        @BindView(R.id.view_services_button)
        TextView viewServicesButton;

        @BindView(R.id.package_bundle_parent_container)
        RelativeLayout packageBundleParentContainer;

        @BindView(R.id.package_bundle_details_container)
        LinearLayout packageBundleDetailsContainer;

        @BindView(R.id.package_bundle_remove)
        ImageView packageBundleRemove;

        @BindView(R.id.package_bundle_add)
        ImageView packageBundleAdd;

        @BindView(R.id.package_bundle_count)
        TextView packageBundleCount;

        @BindView(R.id.package_bundle_sub_title)
        TextView packageBundleSubTitle;

        @BindView(R.id.package_bundle_usage_validity)
        TextView packageBundleUsageValidity;

        @BindView(R.id.package_rating_bar)
        RatingBar packageRatingBar;

        @BindView(R.id.number_of_people_bought)
        TextView numberOfPeopleBought;

        public PackageBundleParentViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EnrichUtils.log("ITEM VIEW");
                }
            });

            packageBundleParentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EnrichUtils.log("OUTER PARENT");
                }
            });

            packageBundleDetailsContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EnrichUtils.log("INNER PARENT");
                }
            });

            viewServicesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isExpanded()) {
                        expandView();
                    } else {
                        collapseView();
                    }
                }
            });
        }
    }

    class PackageBundleChildViewHolder extends ChildViewHolder<BaseChildModel> {

        @BindView(R.id.package_bundle_child_name)
        TextView packageBundleChildName;

        @BindView(R.id.package_bundle_child_price)
        TextView packageBundleChildPrice;

        public PackageBundleChildViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
