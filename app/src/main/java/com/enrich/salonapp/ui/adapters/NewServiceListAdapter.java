package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.crashlytics.android.Crashlytics;
import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.CartItem;
import com.enrich.salonapp.data.model.CenterDetailModel;
import com.enrich.salonapp.data.model.ServiceList.ParentAndNormalServiceListResponseModel;
import com.enrich.salonapp.data.model.ServiceList.SubCategoryModel;
import com.enrich.salonapp.data.model.ServiceViewModel;
import com.enrich.salonapp.data.model.TherapistModel;
import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.enrich.salonapp.ui.activities.ServiceVariantActivity;
import com.enrich.salonapp.ui.contracts.ParentsAndNormalServiceListContract;
import com.enrich.salonapp.ui.contracts.TherapistContract;
import com.enrich.salonapp.ui.presenters.ParentsAndNormalServiceListPresenter;
import com.enrich.salonapp.ui.presenters.TherapistPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.ParentAndNormalServiceComparator;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewServiceListAdapter extends ExpandableRecyclerAdapter<SubCategoryModel, ServiceViewModel, NewServiceListAdapter.SubCategoryViewHolder, NewServiceListAdapter.ParentAndNormalServiceViewHolder> implements TherapistContract.View, Filterable, ParentsAndNormalServiceListContract.View {

    private static final int CHILD_NORMAL = 573;
    private static final int CHILD_PARENT = 574;

    private Activity activity;
    LayoutInflater inflater;
    ArrayList<SubCategoryModel> list;
    private ArrayList<SubCategoryModel> filteredList;
    private ParentAndNormalServiceViewHolder childHolder;
    private SubCategoryViewHolder parentHolder;
    private EnrichApplication application;
    private BottomSheetDialog dialog;
    private int parentPos, childPos;
    private TherapistPresenter therapistPresenter;
    private ParentsAndNormalServiceListPresenter parentsAndNormalServiceListPresenter;
    private String gender;
    private boolean isExpanded;
    private CenterDetailModel centerDetailModel;

    Tracker mTracker;

    boolean isHomeSelected;

    /**
     * Primary constructor. Sets up {@link #mParentList} and {@link #mFlatItemList}.
     * <p>
     * Any changes to {@link #mParentList} should be made on the original instance, and notified via
     * {@link #notifyParentInserted(int)}
     * {@link #notifyParentRemoved(int)}
     * {@link #notifyParentChanged(int)}
     * {@link #notifyParentRangeInserted(int, int)}
     * {@link #notifyChildInserted(int, int)}
     * {@link #notifyChildRemoved(int, int)}
     * {@link #notifyChildChanged(int, int)}
     * methods and not the notify methods of RecyclerView.Adapter.
     *
     * @param parentList List of all parents to be displayed in the RecyclerView that this
     *                   adapter is linked to
     */
    public NewServiceListAdapter(Activity activity, @NonNull ArrayList<SubCategoryModel> parentList, String gender, boolean isHomeSelected, CenterDetailModel centerDetailModel) {
        super(parentList);
        this.activity = activity;
        this.list = parentList;
        this.filteredList = parentList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        application = (EnrichApplication) activity.getApplicationContext();
        this.gender = gender;
        this.isHomeSelected = isHomeSelected;
        this.centerDetailModel = centerDetailModel;

        DataRepository dataRepository = Injection.provideDataRepository(activity, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        therapistPresenter = new TherapistPresenter(this, dataRepository);
        parentsAndNormalServiceListPresenter = new ParentsAndNormalServiceListPresenter(this, dataRepository);
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = inflater.inflate(R.layout.parent_service_list_item, parentViewGroup, false);
        return new SubCategoryViewHolder(view);
    }

    @NonNull
    @Override
    public ParentAndNormalServiceViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = inflater.inflate(R.layout.child_service_list_item, childViewGroup, false);
        return new ParentAndNormalServiceViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull final SubCategoryViewHolder parentViewHolder, final int parentPosition, @NonNull SubCategoryModel parent) {
        parentHolder = parentViewHolder;
        parentViewHolder.setTitle(filteredList.get(parentPosition).Name);

        parentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentPos = parentPosition;
                if (filteredList.get(parentPosition).ChildServices.isEmpty()) {
//                    parentViewHolder.serviceProgress.setTag(parentPosition);
//                    parentViewHolder.serviceProgress.setVisibility(View.VISIBLE);

                    Map<String, String> map = new HashMap<>();
                    map.put("CenterId", centerDetailModel.Id);
                    map.put("SubCategoryId", filteredList.get(parentPosition).SubCategoryId);
                    map.put("Tag", gender);

                    if (EnrichUtils.getUserData(activity) != null)
                        map.put("GuestId", Objects.requireNonNull(EnrichUtils.getUserData(activity)).Id);
                    parentsAndNormalServiceListPresenter.getParentAndNormalServiceList(activity, map);
                } else {
                    if (isExpanded) {
                        collapseParent(parentPosition);
                        isExpanded = false;
                    } else {
                        expandParent(parentPosition);
                        isExpanded = true;
                    }
                }
            }
        });
    }

    @Override
    public void onBindChildViewHolder(@NonNull ParentAndNormalServiceViewHolder childViewHolder, final int parentPosition, final int childPosition, @NonNull ServiceViewModel child) {
        childHolder = childViewHolder;
        final ServiceViewModel model = filteredList.get(parentPosition).getChildList().get(childPosition);

        childHolder.serviceName.setText(model.name);

        if (model.description == null) {
            childHolder.serviceDescription.setVisibility(View.GONE);
        } else {
            childHolder.serviceDescription.setVisibility(View.VISIBLE);
            childHolder.serviceDescription.setText(model.description);
        }

        if (getChildViewType(parentPosition, childPosition) == CHILD_NORMAL) {
            childHolder.mainPrice.setVisibility(View.VISIBLE);
            childHolder.serviceCheckbox.setVisibility(View.VISIBLE);
            childHolder.priceContainer.setVisibility(View.VISIBLE);
            childHolder.parentArrow.setVisibility(View.GONE);

            if (model.IsAdded || application.hasThisItem(model)) { //cartHostActivity.hasThisItem(model.getId())
                childHolder.serviceCheckbox.setChecked(true);
                model.therapist = application.getServiceById(model.ServiceId).therapistModel;
            } else {
                childHolder.serviceCheckbox.setChecked(false);
//                model.therapist = null;
            }

            if (EnrichUtils.getUserData(activity) != null) {
                if (EnrichUtils.getUserData(activity).IsMember == Constants.IS_MEMBER) {
                    childHolder.mainPrice.setText(String.format("%d", (int) model.price.membershipPrice));
                    childHolder.strikePriceContainer.setVisibility(View.VISIBLE);
                    childHolder.strikePrice.setText(String.format("%d", (int) model.price._final));
                } else {
                    childHolder.mainPrice.setText(String.format("%d", (int) model.price._final));
                    childHolder.strikePriceContainer.setVisibility(View.GONE);
                }
            } else {
                childHolder.mainPrice.setText(String.format("%d", (int) model.price._final));
                childHolder.strikePriceContainer.setVisibility(View.GONE);
            }

            if (model.therapist != null) {
                childHolder.serviceTherapistName.setVisibility(View.VISIBLE);
                childHolder.serviceTherapistName.setText(String.format("%s %s", model.therapist.FirstName, model.therapist.LastName));
            } else {
                childHolder.serviceTherapistName.setVisibility(View.GONE);
            }

            childHolder.serviceCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (application.alreadyExist(model)) {
                        model.therapist = null;
                        notifyItemRangeChanged(0, filteredList.get(parentPosition).ChildServices.size());

                        int toggleResponse = application.toggleItem(filteredList.get(parentPosition).ChildServices.get(childPosition));

                        if (toggleResponse == 1) {
                            childHolder.serviceCheckbox.setChecked(true);
                        } else if (toggleResponse == 0) {
                            childHolder.serviceCheckbox.setChecked(false);
                        } else if (toggleResponse == -1) {
                            new AlertDialog.Builder(activity)
                                    .setMessage(activity.getString(R.string.add_services_to_cart_error))
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
//                                    goToCart();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).show();
                        }
                        ((ServiceListActivity) activity).updateCart();
                    } else {
                        parentPos = parentPosition;
                        childPos = childPosition;

                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(model.ServiceId));
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, model.name);
                        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Service");
                        bundle.putString(FirebaseAnalytics.Param.PRICE, String.valueOf(model.price));
                        bundle.putString(FirebaseAnalytics.Param.QUANTITY, String.valueOf(model.getQuantity()));
                        bundle.putString(FirebaseAnalytics.Param.CHECKOUT_STEP, String.valueOf(Constants.SERVICE_CHECKOUT_STEP_SELECT_SERVICE));
                        application.getFirebaseInstance().logEvent(FirebaseAnalytics.Event.CHECKOUT_PROGRESS, bundle);

                        Map<String, String> map = new HashMap<>();
                        map.put("CenterId", centerDetailModel.Id);
                        map.put("ServiceId", "" + model.id);
                        map.put("forDate", "");
                        if (isHomeSelected) {
                            map.put("IsHome", "true");
                        }

                        therapistPresenter.getTherapist(activity, map);
                    }
                }
            });
        } else if (getChildViewType(parentPosition, childPosition) == CHILD_PARENT) {
            childHolder.mainPrice.setVisibility(View.GONE);
            childHolder.serviceCheckbox.setVisibility(View.GONE);
            childHolder.serviceTherapistName.setVisibility(View.GONE);
            childHolder.priceContainer.setVisibility(View.GONE);
            childHolder.parentArrow.setVisibility(View.VISIBLE);

            childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ServiceVariantActivity.class);
                    intent.putExtra("Gender", gender);
                    intent.putExtra("ServiceViewModel", EnrichUtils.newGson().toJson(model));
                    intent.putExtra("SubCategoryId", filteredList.get(parentPosition).SubCategoryId);
                    intent.putExtra("isHomeSelected", isHomeSelected);
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getChildViewType(int parentPosition, int childPosition) {
        Crashlytics.setString("SubCategoryName", filteredList.get(parentPosition).Name);
        if (filteredList.get(parentPosition).ChildServices.get(childPosition).ServiceType.equalsIgnoreCase("Normal")) {
            return CHILD_NORMAL;
        } else if (filteredList.get(parentPosition).ChildServices.get(childPosition).ServiceType.equalsIgnoreCase("Parent")) {
            return CHILD_PARENT;
        } else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new ChildFilter();
    }

    @Override
    public void showTherapist(TherapistResponseModel model) {
        if (model != null) {
            showTherapists(model.Therapists, parentPos, childPos);
        }
    }

    private void showTherapists(ArrayList<TherapistModel> list, final int parentPosition, final int childPosition) {
        View view = inflater.inflate(R.layout.therapist_list_dialog, null);
        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        dialog.setCancelable(false);

        // SEND ANALYTICS
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Therapist List Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        TextView cancel = dialog.findViewById(R.id.therapist_cancel);
        TextView skip = dialog.findViewById(R.id.therapist_dialog_skip);
        RecyclerView therapistRecyclerView = dialog.findViewById(R.id.therapist_list_recycler_view);

        TherapistListAdapter adapter = new TherapistListAdapter(activity, list, parentPosition, childPosition, this, dialog);
        therapistRecyclerView.setAdapter(adapter);
        therapistRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int toggleResponse = application.toggleItem(filteredList.get(parentPosition).ChildServices.get(childPosition));

                if (toggleResponse == 1) {
                    childHolder.serviceCheckbox.setChecked(true);
                } else if (toggleResponse == 0) {
                    childHolder.serviceCheckbox.setChecked(false);
                } else if (toggleResponse == -1) {
                    new AlertDialog.Builder(activity)
                            .setMessage(activity.getString(R.string.add_services_to_cart_error))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
//                                    goToCart();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
                ((ServiceListActivity) activity).updateCart();
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.removeFromCart(filteredList.get(parentPosition).ChildServices.get(childPosition));
                notifyChildRangeChanged(parentPosition, childPosition, filteredList.get(parentPosition).ChildServices.size() - childPosition);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void setTherapist(TherapistModel therapistModel, int parentPosition, int childPosition) {
        filteredList.get(parentPosition).ChildServices.get(childPosition).therapist = therapistModel;

        int toggleResponse = application.toggleItem(filteredList.get(parentPosition).ChildServices.get(childPosition));

        if (EnrichUtils.getUserData(activity) != null) {
            // SEGMENT
            Analytics.with(activity).track(Constants.SEGMENT_SELECT_SERVICE, new Properties()
                    .putValue("user_id", EnrichUtils.getUserData(activity).Id)
                    .putValue("mobile", EnrichUtils.getUserData(activity).MobileNumber)
                    .putValue("service", list.get(parentPosition).ChildServices.get(childPosition).name)
                    .putValue("category", list.get(parentPosition).ChildServices.get(childPosition).CategoryName)
                    .putValue("stylist", therapistModel.DisplayName)
                    .putValue("amount", list.get(parentPosition).ChildServices.get(childPosition).price._final)
                    .putValue("salonid", EnrichUtils.getHomeStore(activity).Id)
                    .putValue("salon_name", EnrichUtils.getHomeStore(activity).Name)
                    .putValue("location", EnrichUtils.getHomeStore(activity).Address)
                    .putValue("area", "")
                    .putValue("city", EnrichUtils.getHomeStore(activity).City)
                    .putValue("state", EnrichUtils.getHomeStore(activity).State == null ? "" : EnrichUtils.getHomeStore(activity).State.Name)
                    .putValue("zipcode", EnrichUtils.getHomeStore(activity).ZipCode));
        }

        if (toggleResponse == 1) {
            childHolder.serviceCheckbox.setChecked(true);
        } else if (toggleResponse == 0) {
            childHolder.serviceCheckbox.setChecked(false);
        } else if (toggleResponse == -1) {
            new AlertDialog.Builder(activity)
                    .setMessage(activity.getString(R.string.add_services_to_cart_error))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
//                                    goToCart();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }

        ((ServiceListActivity) activity).updateCart();

        notifyChildRangeChanged(parentPosition, childPosition, filteredList.get(parentPosition).ChildServices.size() - childPosition);
        dialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void setProgressBar(boolean show) {
        if (show) {
            EnrichUtils.showProgressDialog(activity);
        } else {
            EnrichUtils.cancelCurrentDialog(activity);
        }
    }

    @Override
    public Context getContext() {
        return activity;
    }

    @Override
    public void showParentAndNormalServiceList(ParentAndNormalServiceListResponseModel model) {
        if (!model.ParentAndNormalServiceList.isEmpty()) {

            Collections.sort(model.ParentAndNormalServiceList, new ParentAndNormalServiceComparator());

            for (int i = 0; i < model.ParentAndNormalServiceList.size(); i++) {
                model.ParentAndNormalServiceList.get(i).SubCategoryName = filteredList.get(parentPos).Name;
            }

            filteredList.get(parentPos).ChildServices = model.ParentAndNormalServiceList;
            notifyChildInserted(parentPos, 0);
            expandParent(parentPos);
            isExpanded = true;

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, String.valueOf(filteredList.get(parentPos).Name));
            application.getFirebaseInstance().logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle);
        }
    }

    class SubCategoryViewHolder extends ParentViewHolder<SubCategoryModel, ServiceViewModel> {

        @BindView(R.id.list_item_parent_service_name)
        TextView parentServiceName;

        @BindView(R.id.list_item_parent_service_arrow)
        ImageView parentServiceArrow;

        @BindView(R.id.service_progress)
        ProgressBar serviceProgress;

        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */
        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTitle(String title) {
            parentServiceName.setText(title);
        }
    }

    class ParentAndNormalServiceViewHolder extends ChildViewHolder<ServiceViewModel> {

        @BindView(R.id.service_name)
        TextView serviceName;

        @BindView(R.id.main_price)
        TextView mainPrice;

        @BindView(R.id.service_therapist_name)
        TextView serviceTherapistName; //, oldPrice

        @BindView(R.id.service_checkbox)
        CheckBox serviceCheckbox;

        @BindView(R.id.service_description)
        TextView serviceDescription;

        @BindView(R.id.price_container)
        LinearLayout priceContainer;

        @BindView(R.id.strike_price_container)
        LinearLayout strikePriceContainer;

        @BindView(R.id.main_price_container)
        LinearLayout mainPriceContainer;

        @BindView(R.id.strike_price)
        TextView strikePrice;

        @BindView(R.id.parent_arrow)
        ImageView parentArrow;

        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */
        public ParentAndNormalServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class ChildFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final String filterString = constraint.toString().toLowerCase();

            final FilterResults results = new FilterResults();

            if (filterString.isEmpty()) {
                filteredList = list;
            } else {
                ArrayList<SubCategoryModel> tempParentFilteredList = new ArrayList<>();
                ArrayList<ServiceViewModel> tempChildFilteredList = new ArrayList<>();

                for (SubCategoryModel parentServiceViewModel : list) {
                    if (!parentServiceViewModel.ChildServices.isEmpty()) {
                        for (ServiceViewModel serviceViewModel : parentServiceViewModel.ChildServices) {
                            if (serviceViewModel.name.toLowerCase().contains(filterString.toLowerCase())) {
                                tempChildFilteredList.add(serviceViewModel);
                            }
                        }

                        if (!tempChildFilteredList.isEmpty())
                            tempParentFilteredList.add(new SubCategoryModel(parentServiceViewModel.Id, parentServiceViewModel.SubCategoryId, parentServiceViewModel.SubCategoryOrganizationId, parentServiceViewModel.ImageUrl, parentServiceViewModel.Name, parentServiceViewModel.ParentCategoryId, parentServiceViewModel.Description, parentServiceViewModel.SortOrder, parentServiceViewModel.Code, parentServiceViewModel.Gender, tempChildFilteredList));
                    } else {
                        if (parentServiceViewModel.Name.toLowerCase().contains(filterString.toLowerCase())) {
                            tempParentFilteredList.add(parentServiceViewModel);
                        }
                    }
                }

                filteredList = tempParentFilteredList;
            }

            results.count = filteredList.size();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredList = (ArrayList<SubCategoryModel>) filterResults.values;
            setParentList(filteredList, false);
            notifyDataSetChanged();
        }
    }
}
