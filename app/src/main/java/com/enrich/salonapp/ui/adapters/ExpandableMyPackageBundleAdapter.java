package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.MyPackage.MyPackageBundleModel;
import com.enrich.salonapp.data.model.Package.BaseChildModel;
import com.enrich.salonapp.data.model.Package.PackageBundle;
import com.enrich.salonapp.ui.activities.MyPackageDetailActivity;
import com.enrich.salonapp.util.EnrichUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandableMyPackageBundleAdapter extends ExpandableRecyclerAdapter<MyPackageBundleModel, BaseChildModel, ExpandableMyPackageBundleAdapter.MyPackageBundleParentViewHolder, ExpandableMyPackageBundleAdapter.MyPackageBundleChildViewHolder> {

    MyPackageDetailActivity activity;
    List<MyPackageBundleModel> list;
    LayoutInflater inflater;
    EnrichApplication application;

    public ExpandableMyPackageBundleAdapter(MyPackageDetailActivity activity, @NonNull List<MyPackageBundleModel> list) {
        super(list);
        this.activity = activity;
        this.list = list;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        application = (EnrichApplication) activity.getApplicationContext();
    }

    @NonNull
    @Override
    public MyPackageBundleParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = inflater.inflate(R.layout.my_package_bundle_parent_list_item, parentViewGroup, false);
        return new ExpandableMyPackageBundleAdapter.MyPackageBundleParentViewHolder(view);
    }

    @NonNull
    @Override
    public MyPackageBundleChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = inflater.inflate(R.layout.package_bundle_child_list_item, childViewGroup, false);
        return new ExpandableMyPackageBundleAdapter.MyPackageBundleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull ExpandableMyPackageBundleAdapter.MyPackageBundleParentViewHolder parentViewHolder, final int parentPosition, @NonNull MyPackageBundleModel parent) {
        final MyPackageBundleModel model = list.get(parentPosition);

        parentViewHolder.packageBundleName.setText(model.Name);
        parentViewHolder.packageBundlePrice.setText(activity.getResources().getString(R.string.Rs) + " " + model.Price);

        if (!model.PackageBundleService.isEmpty()) {
            parentViewHolder.viewServicesButton.setText("View Services");
        } else if (!model.PackageBundleProduct.isEmpty()) {
            parentViewHolder.viewServicesButton.setText("View Products");
        }

        parentViewHolder.packageBundleCount.setText(model.RemainingQuantity + "/" + model.IntialQuantity);
    }

    @Override
    public void onBindChildViewHolder(@NonNull ExpandableMyPackageBundleAdapter.MyPackageBundleChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull BaseChildModel child) {
        childViewHolder.packageBundleChildName.setText(child.getName());
        childViewHolder.packageBundleChildPrice.setText(activity.getResources().getString(R.string.Rs) + " " + child.getPrice());
    }

    class MyPackageBundleParentViewHolder extends ParentViewHolder<PackageBundle, BaseChildModel> {

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

        @BindView(R.id.package_bundle_count)
        TextView packageBundleCount;

        public MyPackageBundleParentViewHolder(@NonNull View itemView) {
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

    class MyPackageBundleChildViewHolder extends ChildViewHolder<BaseChildModel> {

        @BindView(R.id.package_bundle_child_name)
        TextView packageBundleChildName;

        @BindView(R.id.package_bundle_child_price)
        TextView packageBundleChildPrice;

        public MyPackageBundleChildViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
