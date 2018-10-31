package com.enrich.salonapp.ui.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.TherapistModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TherapistListAdapter extends RecyclerView.Adapter<TherapistListAdapter.TherapistViewHolder> {

    Context context;
    ArrayList<TherapistModel> list;
    LayoutInflater inflater;
    int childPos, parentPos;
    NewServiceListAdapter serviceListAdapterNew;
    RecommendedServicesAdapter recommendedServicesAdapter;
    VariantRecyclerViewAdapter variantRecyclerViewAdapter;
    HomeParentAndNormalServiceAdapter homeParentAndNormalServiceAdapter;
    Dialog dialog;

    public TherapistListAdapter(Context context, ArrayList<TherapistModel> list, int parentPosition, int childPosition, NewServiceListAdapter serviceListAdapterNew, Dialog dialog) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.childPos = childPosition;
        this.parentPos = parentPosition;
        this.serviceListAdapterNew = serviceListAdapterNew;
        this.dialog = dialog;
    }

    public TherapistListAdapter(Context context, ArrayList<TherapistModel> list, int position, VariantRecyclerViewAdapter variantRecyclerViewAdapter, Dialog dialog) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.parentPos = position;
        this.variantRecyclerViewAdapter = variantRecyclerViewAdapter;
        this.dialog = dialog;
    }

    public TherapistListAdapter(Context context, ArrayList<TherapistModel> list, int position, RecommendedServicesAdapter recommendedServicesAdapter, Dialog dialog) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.childPos = position;
        this.recommendedServicesAdapter = recommendedServicesAdapter;
        this.dialog = dialog;
    }

    public TherapistListAdapter(Context context, ArrayList<TherapistModel> list, int position, HomeParentAndNormalServiceAdapter homeParentAndNormalServiceAdapter, Dialog dialog) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.childPos = position;
        this.homeParentAndNormalServiceAdapter = homeParentAndNormalServiceAdapter;
        this.dialog = dialog;
    }

    @Override
    public TherapistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.therapist_list_item, parent, false);
        return new TherapistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TherapistViewHolder holder, final int position) {
        final TherapistModel model = list.get(position);

        holder.therapistName.setText(model.Name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceListAdapterNew != null)
                    serviceListAdapterNew.setTherapist(model, parentPos, childPos);

                if (recommendedServicesAdapter != null)
                    recommendedServicesAdapter.setTherapist(model, childPos);

                if (variantRecyclerViewAdapter != null)
                    variantRecyclerViewAdapter.setTherapist(model, parentPos);

                if (homeParentAndNormalServiceAdapter != null)
                    homeParentAndNormalServiceAdapter.setTherapist(model, parentPos);

                dialog.cancel();
            }
        });

        holder.therapistRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceListAdapterNew != null)
                    serviceListAdapterNew.setTherapist(model, parentPos, childPos);

                if (recommendedServicesAdapter != null)
                    recommendedServicesAdapter.setTherapist(model, childPos);

                if (variantRecyclerViewAdapter != null)
                    variantRecyclerViewAdapter.setTherapist(model, parentPos);
                dialog.cancel();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TherapistViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.therapist_name)
        TextView therapistName;

        @BindView(R.id.therapist_radio_button)
        RadioButton therapistRadioButton;

        public TherapistViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
