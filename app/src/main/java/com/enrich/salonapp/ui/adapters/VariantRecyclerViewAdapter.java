package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.ServiceViewModel;
import com.enrich.salonapp.data.model.TherapistModel;
import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.enrich.salonapp.ui.activities.ServiceVariantActivity;
import com.enrich.salonapp.ui.contracts.TherapistContract;
import com.enrich.salonapp.ui.presenters.ParentsAndNormalServiceListPresenter;
import com.enrich.salonapp.ui.presenters.TherapistPresenter;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VariantRecyclerViewAdapter extends RecyclerView.Adapter<VariantRecyclerViewAdapter.VariantViewHolder> implements TherapistContract.View {

    Activity activity;
    ArrayList<ServiceViewModel> variantList;
    LayoutInflater inflater;
    private EnrichApplication application;
    private TherapistPresenter therapistPresenter;
    int pos;
    private BottomSheetDialog dialog;
    VariantViewHolder holder;

    Tracker mTracker;
    boolean isHomeSelected;

    public VariantRecyclerViewAdapter(Activity activity, ArrayList<ServiceViewModel> list, boolean isHomeSelected) {
        this.activity = activity;
        this.variantList = list;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isHomeSelected = isHomeSelected;
        application = (EnrichApplication) activity.getApplicationContext();

        DataRepository dataRepository = Injection.provideDataRepository(activity, MainUiThread.getInstance(), ThreadExecutor.getInstance(), null);
        therapistPresenter = new TherapistPresenter(this, dataRepository);
    }

    @NonNull
    @Override
    public VariantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.variant_list_item, parent, false);
        return new VariantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VariantViewHolder holder, final int position) {
        this.holder = holder;
        final ServiceViewModel model = variantList.get(position);

        holder.serviceName.setText(variantList.get(position).getName());
        if (model.description != null) {
            holder.serviceDescription.setText(model.description);
        } else {
            holder.serviceDescription.setVisibility(View.GONE);
        }

        if (model.IsAdded || application.hasThisItem(model)) { //cartHostActivity.hasThisItem(model.getId())
            holder.serviceCheckbox.setChecked(true);
            model.therapist = application.getServiceById(model.ServiceId).therapistModel;
        } else {
            holder.serviceCheckbox.setChecked(false);
            model.therapist = null;
        }

        if (EnrichUtils.getUserData(activity).IsMember == Constants.IS_MEMBER) {
            holder.mainPrice.setText(String.format("%d", (int) model.price._final));
            holder.strikePriceContainer.setVisibility(View.VISIBLE);
            holder.strikePrice.setText(String.format("%d", (int) model.price.sales));
        } else {
            holder.mainPrice.setText(String.format("%d", (int) model.price.sales));
            holder.strikePriceContainer.setVisibility(View.GONE);
        }

        holder.serviceCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.therapist != null) {
                    model.therapist = null;
                    notifyItemRangeChanged(0, variantList.size());

                    int toggleResponse = application.toggleItem(variantList.get(position));

                    if (toggleResponse == 1) {
                        holder.serviceCheckbox.setChecked(true);
                    } else if (toggleResponse == 0) {
                        holder.serviceCheckbox.setChecked(false);
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
                    ((ServiceVariantActivity) activity).updateCart();
                } else {
                    pos = position;

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

        if (model.therapist != null) {
            holder.serviceTherapistName.setVisibility(View.VISIBLE);
            holder.serviceTherapistName.setText(model.therapist.Name);
        } else {
            holder.serviceTherapistName.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return variantList.size();
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
        TextView skip = dialog.findViewById(R.id.therapist_dialog_skip);
        RecyclerView therapistRecyclerView = dialog.findViewById(R.id.therapist_list_recycler_view);

        TherapistListAdapter adapter = new TherapistListAdapter(activity, list, position, this, dialog);
        therapistRecyclerView.setAdapter(adapter);
        therapistRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int toggleResponse = application.toggleItem(variantList.get(position));

                if (toggleResponse == 1) {
                    holder.serviceCheckbox.setChecked(true);
                } else if (toggleResponse == 0) {
                    holder.serviceCheckbox.setChecked(false);
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

                ((ServiceVariantActivity) activity).updateCart();
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.removeFromCart(variantList.get(pos));
                notifyItemRangeChanged(pos, variantList.size());
//                notifyChildRangeChanged(parentPosition, childPosition, filteredList.get(parentPosition).ChildServices.size() - childPosition);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void setTherapist(TherapistModel therapistModel, int position) {
        variantList.get(position).therapist = therapistModel;

        int toggleResponse = application.toggleItem(variantList.get(position));

        if (toggleResponse == 1) {
            holder.serviceCheckbox.setChecked(true);
        } else if (toggleResponse == 0) {
            holder.serviceCheckbox.setChecked(false);
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

        ((ServiceVariantActivity) activity).updateCart();

        notifyItemRangeChanged(position, variantList.size());
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

    class VariantViewHolder extends RecyclerView.ViewHolder {

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

        public VariantViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
