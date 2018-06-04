package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.ServiceViewModel;
import com.enrich.salonapp.data.model.TherapistModel;
import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.activities.CartActivity;
import com.enrich.salonapp.ui.activities.DateSelectorActivity;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.enrich.salonapp.ui.contracts.TherapistContract;
import com.enrich.salonapp.ui.presenters.TherapistPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendedServicesAdapter extends RecyclerView.Adapter<RecommendedServicesAdapter.RecommendServiceVieHolder> implements TherapistContract.View {

    Context context;
    ArrayList<ServiceViewModel> list;
    LayoutInflater inflater;
    EnrichApplication application;
    Dialog dialog;
    DataRepository dataRepository;
    TherapistPresenter therapistPresenter;
    int pos;

    public RecommendedServicesAdapter(Context context, ArrayList<ServiceViewModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        application = (EnrichApplication) context.getApplicationContext();

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(context, mainUiThread, threadExecutor, null);
        therapistPresenter = new TherapistPresenter(this, dataRepository);
    }

    @Override
    public RecommendServiceVieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recommended_services_home_list_item, parent, false);
        return new RecommendServiceVieHolder(view);
    }

    @Override
    public void onBindViewHolder(RecommendServiceVieHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        holder.price.setText("From " + context.getResources().getString(R.string.Rs) + " " + list.get(position).getPrice());
        Picasso.get().load(list.get(position).imagePaths.px200).into(holder.serviceImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> map = new HashMap<>();
                map.put("CenterId", EnrichUtils.getHomeStore(context).Id);
                map.put("ServiceId", list.get(position).id);
                map.put("forDate", "");
                pos = position;

                if (list.get(position).therapist == null) {
                    therapistPresenter.getTherapist(context, map);
//                    getTherapistList(list.get(position).id, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void showTherapist(TherapistResponseModel model) {
        if (model != null) {
            showTherapists(model.Therapists, pos);
        }
    }

    @Override
    public void showToastMessage(String message) {
        EnrichUtils.showLongMessage(getContext(), message);
    }

    @Override
    public void setProgressBar(boolean show) {
        if (show) {
            EnrichUtils.showProgressDialog((Activity) context);
        } else {
            EnrichUtils.cancelCurrentDialog((Activity) context);
        }
    }

    @Override
    public Context getContext() {
        return null;
    }

    private void showTherapists(ArrayList<TherapistModel> list, int position) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.therapist_list_dialog);
        dialog.setCancelable(false);

        TextView cancel = dialog.findViewById(R.id.therapist_cancel);
        RecyclerView therapistRecyclerView = dialog.findViewById(R.id.therapist_list_recycler_view);

        TherapistListAdapter adapter = new TherapistListAdapter(context, list, position, this, dialog);
        therapistRecyclerView.setAdapter(adapter);
        therapistRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.clearCart();
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void setTherapist(TherapistModel therapistModel, int position) {
        list.get(position).therapist = therapistModel;

        int toggleResponse = application.toggleItem(list.get(position));

        notifyItemRangeChanged(0, list.size());
        dialog.dismiss();

        if (!application.isCartEmpty()) {
            showAddMoreServicesDialog();
        }
    }

    private void showAddMoreServicesDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_more_services_dialog);
        dialog.setCancelable(false);

        TextView cancel = dialog.findViewById(R.id.add_more_service_cancel);
        TextView addMoreOk = dialog.findViewById(R.id.add_more_service_ok);
        TextView addMoreCancel = dialog.findViewById(R.id.add_more_service_proceed);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.clearCart();
                dialog.dismiss();
            }
        });

        addMoreOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(context, DateSelectorActivity.class);
                context.startActivity(intent);
            }
        });

        addMoreCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(context, ServiceListActivity.class);
                context.startActivity(intent);
            }
        });

        dialog.show();
    }

    class RecommendServiceVieHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.service_title)
        TextView name;

        @BindView(R.id.service_price)
        TextView price;

        @BindView(R.id.service_image)
        ImageView serviceImage;

        public RecommendServiceVieHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
