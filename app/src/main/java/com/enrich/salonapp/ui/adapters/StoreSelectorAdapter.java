package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.CenterDetailModel;
import com.enrich.salonapp.data.model.CenterViewModel;
import com.enrich.salonapp.ui.activities.HomeActivity;
import com.enrich.salonapp.ui.activities.StoreSelectorActivity;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreSelectorAdapter extends RecyclerView.Adapter<StoreSelectorAdapter.StoreViewHolder> implements Filterable {

    Context context;
    LayoutInflater inflater;
    ArrayList<CenterViewModel> list;
    private ArrayList<CenterViewModel> filteredList;
    private StoreFilter storeFilter;
    StoreSelectorActivity activity;

    public StoreSelectorAdapter(Context context, ArrayList<CenterViewModel> list, StoreSelectorActivity activity) {
        this.context = context;
        this.list = list;
        this.filteredList = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.store_list_item, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        final CenterViewModel model = filteredList.get(position);

        holder.name.setText(model.Name);
        holder.address.setText(String.format("%s, %s", model.Address1, model.Address2));
        holder.phone.setText(model.Phone1.DisplayNumber);
        holder.storeDistance.setText(String.format("%s km", model.Distance));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CenterDetailModel centerDetailModel = new CenterDetailModel();
                centerDetailModel.Area = model.Address2;
                centerDetailModel.Id = model.Id;
                centerDetailModel.Phone = model.Phone1.Number;
                centerDetailModel.Address = model.Address1 + " " + model.Address2;
                centerDetailModel.Email = model.Email;
                centerDetailModel.Name = model.Name;
                centerDetailModel.CenterType = model.CenterType;
                centerDetailModel.Country = model.Country;
                centerDetailModel.State = model.State;
                centerDetailModel.City = model.City;
                centerDetailModel.ZipCode = model.ZipCode;

                EnrichUtils.saveHomeStore(context, EnrichUtils.newGson().toJson(centerDetailModel));

                Intent intent = new Intent(context, HomeActivity.class);
                activity.startActivity(intent);
                activity.finish();

                if (EnrichUtils.getUserData(activity) != null) {
                    Analytics.with(context).track(Constants.SEGMENT_SELECT_STORE, new Properties()
                            .putValue("user_id", EnrichUtils.getUserData(context).Id)
                            .putValue("mobile", EnrichUtils.getUserData(context).MobileNumber)
                            .putValue("salonid", model.Id)
                            .putValue("salon_name", model.Name)
                            .putValue("location", model.Address1)
                            .putValue("area", model.Address2)
                            .putValue("city", model.City)
                            .putValue("state", model.State.Name)
                            .putValue("zipcode", model.ZipCode));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        if (storeFilter == null)
            storeFilter = new StoreFilter();

        return storeFilter;
    }

    private class StoreFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = list;
                results.count = list.size();
            } else {
                // We perform filtering operation
                ArrayList<CenterViewModel> nPlanetList = new ArrayList<>();

                for (CenterViewModel p : list) {
                    if (p.Name.toUpperCase()
                            .contains(constraint.toString().toUpperCase()) || p.City.toUpperCase()
                            .contains(constraint.toString().toUpperCase()))
                        nPlanetList.add(p);
                }

                filteredList = nPlanetList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                notifyDataSetChanged();
            else {
                filteredList = (ArrayList<CenterViewModel>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    class StoreViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.store_name)
        TextView name;

        @BindView(R.id.store_address)
        TextView address;

        @BindView(R.id.store_phone)
        TextView phone;

        @BindView(R.id.store_distance)
        TextView storeDistance;

        public StoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
