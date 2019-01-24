package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.ServiceViewModel;
import com.enrich.salonapp.data.model.TherapistModel;
import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.activities.ReceiptActivity;
import com.enrich.salonapp.ui.activities.RescheduleActivity;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.enrich.salonapp.ui.contracts.TherapistContract;
import com.enrich.salonapp.ui.presenters.TherapistPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RescheduleServiceListAdapter extends RecyclerView.Adapter implements TherapistContract.View {

    Activity activity;
    LayoutInflater inflater;
    ArrayList<ServiceViewModel> serviceList;
    private TherapistPresenter therapistPresenter;
    private BottomSheetDialog dialog;
    private int pos;
    private EnrichApplication application;
    private RescheduleServiceViewHolder rescheduleServiceViewHolder;

    public RescheduleServiceListAdapter(Activity activity, ArrayList<ServiceViewModel> list) {
        this.activity = activity;
        this.serviceList = list;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        application = (EnrichApplication) activity.getApplicationContext();

        DataRepository dataRepository = Injection.provideDataRepository(activity, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        therapistPresenter = new TherapistPresenter(this, dataRepository);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.reschedule_service_list_item, parent, false);
        return new RescheduleServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        rescheduleServiceViewHolder = (RescheduleServiceViewHolder) holder;

        final ServiceViewModel serviceViewModel = serviceList.get(position);

        rescheduleServiceViewHolder.rescheduleServiceName.setText(serviceViewModel.name);
        rescheduleServiceViewHolder.rescheduleServiceRate.setText(activity.getResources().getString(R.string.rs_symbol) + " " + serviceViewModel.price._final);
        rescheduleServiceViewHolder.rescheduleServiceTherapist.setText(serviceViewModel.therapist.FirstName + " " + serviceViewModel.therapist.LastName);

        if (serviceViewModel.IsAdded || application.hasThisItem(serviceViewModel)) { //cartHostActivity.hasThisItem(model.getId())
            rescheduleServiceViewHolder.rescheduleServiceSelector.setChecked(true);
            serviceViewModel.therapist = application.getServiceById(serviceViewModel.ServiceId).therapistModel;
        } else {
            rescheduleServiceViewHolder.rescheduleServiceSelector.setChecked(false);
//            serviceViewModel.therapist = null;
        }

        rescheduleServiceViewHolder.rescheduleServiceTherapistChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;
                Map<String, String> map = new HashMap<>();
                map.put("CenterId", EnrichUtils.getHomeStore(activity).Id);
                map.put("ServiceId", "" + serviceViewModel.id);
                map.put("forDate", "");
//                if (isHomeSelected) {
                map.put("IsHome", "false");
//                }

                therapistPresenter.getTherapist(activity, map);
            }
        });

        rescheduleServiceViewHolder.rescheduleServiceSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int toggleResponse = application.toggleItem(serviceViewModel);

                if (toggleResponse == 1) {
                    rescheduleServiceViewHolder.rescheduleServiceSelector.setChecked(true);
                } else if (toggleResponse == 0) {
                    rescheduleServiceViewHolder.rescheduleServiceSelector.setChecked(false);
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
                ((RescheduleActivity) activity).updateCart();
//                notifyItemChanged(position, serviceViewModel);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    @Override
    public void showTherapist(TherapistResponseModel model) {
        if (model != null) {
            showTherapists(model.Therapists, pos);
        }
    }

    private void showTherapists(final ArrayList<TherapistModel> list, final int position) {
        View view = inflater.inflate(R.layout.therapist_list_dialog, null);
        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        dialog.setCancelable(false);

        TextView cancel = dialog.findViewById(R.id.therapist_cancel);
        RecyclerView therapistRecyclerView = dialog.findViewById(R.id.therapist_list_recycler_view);

        TherapistListAdapter adapter = new TherapistListAdapter(activity, list, position, this, dialog);
        therapistRecyclerView.setAdapter(adapter);
        therapistRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.removeFromCart(serviceList.get(position));
                notifyItemChanged(position, list.size());
//                notifyChildRangeChanged(parentPosition, childPosition, filteredList.get(parentPosition).ChildServices.size() - childPosition);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void setTherapist(TherapistModel therapistModel, int position) {
        serviceList.get(position).therapist = therapistModel;

//        int toggleResponse = application.toggleItem(serviceList.get(position));
//
//        if (toggleResponse == 1) {
//            rescheduleServiceViewHolder.rescheduleServiceSelector.setChecked(true);
//        } else if (toggleResponse == 0) {
//            rescheduleServiceViewHolder.rescheduleServiceSelector.setChecked(false);
//        } else if (toggleResponse == -1) {
//            new AlertDialog.Builder(activity)
//                    .setMessage(activity.getString(R.string.add_services_to_cart_error))
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
////                                    goToCart();
//                        }
//                    })
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    }).show();
//        }

        ((RescheduleActivity) activity).updateCart();

        notifyItemChanged(position, serviceList.get(position));
//        notifyChildRangeChanged(parentPosition, childPosition, filteredList.get(parentPosition).ChildServices.size() - childPosition);
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

    class RescheduleServiceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reschedule_service_name)
        TextView rescheduleServiceName;

        @BindView(R.id.reschedule_service_rate)
        TextView rescheduleServiceRate;

        @BindView(R.id.reschedule_service_selector)
        CheckBox rescheduleServiceSelector;

        @BindView(R.id.reschedule_service_therapist)
        TextView rescheduleServiceTherapist;

        @BindView(R.id.reschedule_service_therapist_change)
        TextView rescheduleServiceTherapistChange;

        public RescheduleServiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
