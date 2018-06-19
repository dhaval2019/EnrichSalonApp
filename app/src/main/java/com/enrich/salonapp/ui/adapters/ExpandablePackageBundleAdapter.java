package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.Package.BaseChildModel;
import com.enrich.salonapp.data.model.Package.PackageBundle;
import com.enrich.salonapp.ui.activities.PackageDetailActivity;
import com.enrich.salonapp.util.EnrichUtils;

import java.util.ArrayList;
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
        parentViewHolder.packageBundlePrice.setText(activity.getResources().getString(R.string.Rs) + " " + model.Price);

        if (!model.Cashbacks.isEmpty()) {
            parentViewHolder.viewServicesButton.setText("View Cashbacks");
        } else if (!model.PackageBundleService.isEmpty()) {
            parentViewHolder.viewServicesButton.setText("View Services");
        } else if (!model.PackageBundleProduct.isEmpty()) {
            parentViewHolder.viewServicesButton.setText("View Products");
        }

        parentViewHolder.packageBundleCount.setText("" + application.getItemQuantity(model));

        parentViewHolder.packageBundleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        childViewHolder.packageBundleChildName.setText(child.getName());
        childViewHolder.packageBundleChildPrice.setText(activity.getResources().getString(R.string.Rs) + " " + child.getPrice());
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
