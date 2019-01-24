package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.ServiceList.SubCategoryModel;
import com.enrich.salonapp.data.model.ServiceViewModel;
import com.enrich.salonapp.data.model.TherapistModel;
import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.enrich.salonapp.ui.activities.ServiceVariantActivity;
import com.enrich.salonapp.ui.contracts.TherapistContract;
import com.enrich.salonapp.ui.presenters.TherapistPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SampleHomeParentAndNormalServiceAdapter extends RecyclerView.Adapter implements TherapistContract.View {

    private static final int CHILD_NORMAL = 573;
    private static final int CHILD_PARENT = 574;

    private Activity activity;
    private ArrayList<ServiceViewModel> filteredServiceList;
    private LayoutInflater inflater;
    private EnrichApplication application;

    private ParentServiceViewHolder parentServiceViewHolder;
    private NormalServiceViewHolder normalServiceViewHolder;
    private String gender;
    private SubCategoryModel subCategoryModel;

    private BottomSheetDialog dialog;

    private TherapistPresenter therapistPresenter;

    Tracker mTracker;

    boolean isHomeSelected;

    private int pos;

    public SampleHomeParentAndNormalServiceAdapter(Activity activity, ArrayList<ServiceViewModel> serviceList, String gender, SubCategoryModel subCategoryModel, boolean isHomeSelected) {
        this.activity = activity;
        this.filteredServiceList = serviceList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.gender = gender;
        this.application = (EnrichApplication) activity.getApplication();
        this.subCategoryModel = subCategoryModel;
        this.isHomeSelected = isHomeSelected;

        DataRepository dataRepository = Injection.provideDataRepository(activity, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        therapistPresenter = new TherapistPresenter(this, dataRepository);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CHILD_NORMAL) {
            View view = inflater.inflate(R.layout.home_normal_service_list_item, parent, false);
            return new NormalServiceViewHolder(view);
        } else if (viewType == CHILD_PARENT) {
            View view = inflater.inflate(R.layout.home_parent_service_list_item, parent, false);
            return new ParentServiceViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ServiceViewModel model = filteredServiceList.get(position);

        if (getItemViewType(position) == CHILD_NORMAL) {
            normalServiceViewHolder = (NormalServiceViewHolder) holder;

            normalServiceViewHolder.serviceName.setText(model.name);
            normalServiceViewHolder.serviceDescription.setText(model.description);

            if (model.IsAdded || application.hasThisItem(model)) { //cartHostActivity.hasThisItem(model.getId())
                normalServiceViewHolder.serviceCheckbox.setChecked(true);
                model.therapist = application.getServiceById(model.ServiceId).therapistModel;
            } else {
                normalServiceViewHolder.serviceCheckbox.setChecked(false);
                model.therapist = null;
            }

            normalServiceViewHolder.mainPrice.setText(" " + (int) model.price.sales);

            if (model.therapist != null) {
                normalServiceViewHolder.serviceTherapistName.setVisibility(View.VISIBLE);
                normalServiceViewHolder.serviceTherapistName.setText(model.therapist.Name);
            } else {
                normalServiceViewHolder.serviceTherapistName.setVisibility(View.GONE);
            }

            normalServiceViewHolder.serviceCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.therapist != null) {
                        model.therapist = null;
                        notifyItemRangeChanged(0, filteredServiceList.size());

                        int toggleResponse = application.toggleItem(filteredServiceList.get(position));

                        if (toggleResponse == 1) {
                            normalServiceViewHolder.serviceCheckbox.setChecked(true);
                        } else if (toggleResponse == 0) {
                            normalServiceViewHolder.serviceCheckbox.setChecked(false);
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
                        pos = position;

                        Map<String, String> map = new HashMap<>();
                        map.put("CenterId", EnrichUtils.getHomeStore(activity).Id);
                        map.put("ServiceId", "" + model.id);
                        map.put("forDate", "");
                        if (isHomeSelected) {
                            map.put("IsHome", "true");
                        }

                        therapistPresenter.getTherapist(activity, map);
                    }
                }
            });
        } else if (getItemViewType(position) == CHILD_PARENT) {
            parentServiceViewHolder = (ParentServiceViewHolder) holder;

            parentServiceViewHolder.homeServiceName.setText(model.name);

            parentServiceViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ServiceVariantActivity.class);
                    intent.putExtra("Gender", gender);
                    intent.putExtra("ServiceViewModel", EnrichUtils.newGson().toJson(model));
                    intent.putExtra("SubCategoryId", subCategoryModel.SubCategoryId);
                    intent.putExtra("isHomeSelected", isHomeSelected);
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return filteredServiceList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (filteredServiceList.get(position).ServiceType.equalsIgnoreCase("Normal")) {
            return CHILD_NORMAL;
        } else {
            return CHILD_PARENT;
        }
    }

    @Override
    public void showTherapist(TherapistResponseModel model) {
        if (model != null) {
            showTherapists(model.Therapists, pos);
        }
    }

    private void showTherapists(ArrayList<TherapistModel> list, final int position) {
        View view = inflater.inflate(R.layout.therapist_list_dialog, null);
        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        dialog.setCancelable(false);

        // SEND ANALYTICS
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Therapist List Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        TextView cancel = dialog.findViewById(R.id.therapist_cancel);
        RecyclerView therapistRecyclerView = dialog.findViewById(R.id.therapist_list_recycler_view);

        TherapistListAdapter adapter = new TherapistListAdapter(activity, list, position, this, dialog);
        therapistRecyclerView.setAdapter(adapter);
        therapistRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.removeFromCart(filteredServiceList.get(position));
                notifyItemRangeChanged(0, filteredServiceList.size());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void setTherapist(TherapistModel therapistModel, int position) {
        filteredServiceList.get(position).therapist = therapistModel;

        int toggleResponse = application.toggleItem(filteredServiceList.get(position));

        if (toggleResponse == 1) {
            normalServiceViewHolder.serviceCheckbox.setChecked(true);
        } else if (toggleResponse == 0) {
            normalServiceViewHolder.serviceCheckbox.setChecked(false);
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

        notifyItemChanged(position);
        notifyDataSetChanged();
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

    class ParentServiceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_service_name)
        TextView homeServiceName;

        @BindView(R.id.home_parent_arrow)
        ImageView homeparentArrow;

        public ParentServiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class NormalServiceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_service_name)
        TextView serviceName;

        @BindView(R.id.home_main_price)
        TextView mainPrice;

        @BindView(R.id.home_service_therapist_name)
        TextView serviceTherapistName; //, oldPrice

        @BindView(R.id.home_service_checkbox)
        CheckBox serviceCheckbox;

        @BindView(R.id.home_service_description)
        TextView serviceDescription;

        @BindView(R.id.home_price_container)
        LinearLayout priceContainer;

        public NormalServiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
