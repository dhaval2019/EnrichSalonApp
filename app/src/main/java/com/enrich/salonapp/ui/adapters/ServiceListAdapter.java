package com.enrich.salonapp.ui.adapters;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.ExpandableWrapper;
import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.ParentServiceViewModel;
import com.enrich.salonapp.data.model.ServiceViewModel;
import com.enrich.salonapp.data.model.TherapistModel;
import com.enrich.salonapp.data.model.TherapistResponseModel;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.enrich.salonapp.ui.contracts.TherapistContract;
import com.enrich.salonapp.ui.presenters.TherapistPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceListAdapter extends ExpandableRecyclerAdapter<ParentServiceViewModel, ServiceViewModel, ServiceListAdapter.ParentServiceViewHolder, ServiceListAdapter.ChildServiceVieHolder> implements TherapistContract.View, Filterable {

    private ServiceListActivity activity;
    LayoutInflater inflater;
    ArrayList<ParentServiceViewModel> list;
    private ArrayList<ParentServiceViewModel> filteredList;
    private ChildServiceVieHolder childHolder;
    private EnrichApplication application;
    private BottomSheetDialog dialog;
    private int parentPos, childPos;
    private TherapistPresenter therapistPresenter;

    public ServiceListAdapter(@NonNull ArrayList<ParentServiceViewModel> parentList, ServiceListActivity activity) {
        super(parentList);
        this.activity = activity;
        this.list = parentList;
        this.filteredList = parentList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        application = (EnrichApplication) activity.getApplicationContext();

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        DataRepository dataRepository = Injection.provideDataRepository(activity, mainUiThread, threadExecutor, null);
        therapistPresenter = new TherapistPresenter(this, dataRepository);
    }

    @NonNull
    @Override
    public ParentServiceViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = inflater.inflate(R.layout.parent_service_list_item, parentViewGroup, false);
        return new ParentServiceViewHolder(view);
    }

    @NonNull
    @Override
    public ChildServiceVieHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View view = inflater.inflate(R.layout.child_service_list_item, childViewGroup, false);
        return new ChildServiceVieHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull ParentServiceViewHolder parentViewHolder, int parentPosition, @NonNull ParentServiceViewModel parent) {
        parentViewHolder.setTitle(filteredList.get(parentPosition).name);
    }

    @Override
    public void onBindChildViewHolder(@NonNull ChildServiceVieHolder childViewHolder, final int parentPosition, final int childPosition, @NonNull ServiceViewModel child) {
        childHolder = childViewHolder;
        final ServiceViewModel model = filteredList.get(parentPosition).getChildList().get(childPosition);

        if (model.IsAdded || application.hasThisItem(model)) { //cartHostActivity.hasThisItem(model.getId())
            childHolder.serviceCheckbox.setChecked(true);
        } else {
            childHolder.serviceCheckbox.setChecked(false);
        }

        childHolder.serviceName.setText(model.name);
        childHolder.mainPrice.setText(" " + new DecimalFormat("##.##").format(model.price._final));

        childHolder.serviceCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.therapist != null) {
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
                    activity.updateCart();
                } else {
                    parentPos = parentPosition;
                    childPos = childPosition;

                    Map<String, String> map = new HashMap<>();
                    map.put("CenterId", EnrichUtils.getHomeStore(activity).Id);
                    map.put("ServiceId", model.id);
                    map.put("forDate", "");

                    therapistPresenter.getTherapist(activity, map);
//                    getTherapistList(model.id, parentPosition, childPosition);
                }
            }
        });

        childHolder.viewServiceDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(activity, ServiceDetailsActivity.class);
//                intent.putExtra("ServiceId", model.id);
//                activity.startActivity(intent);
            }
        });

        if (model.therapist != null) {
            childHolder.serviceTherapistName.setVisibility(View.VISIBLE);
            childHolder.serviceTherapistName.setText(model.therapist.Name);
        } else {
            childHolder.serviceTherapistName.setVisibility(View.GONE);
        }
    }

    @Override
    public void showTherapist(TherapistResponseModel model) {
        if (model != null) {
            showTherapists(model.Therapists, parentPos, childPos);
        }
    }

    @Override
    public void showToastMessage(String message) {
        EnrichUtils.showLongMessage(getContext(), message);
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

    private void showTherapists(ArrayList<TherapistModel> list, final int parentPosition, final int childPosition) {
        View view = inflater.inflate(R.layout.therapist_list_dialog, null);
        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);

        dialog.setCancelable(false);

        TextView cancel = dialog.findViewById(R.id.therapist_cancel);
        RecyclerView therapistRecyclerView = dialog.findViewById(R.id.therapist_list_recycler_view);

        TherapistListAdapter adapter = new TherapistListAdapter(activity, list, parentPosition, childPosition, this, dialog);
        therapistRecyclerView.setAdapter(adapter);
        therapistRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

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

        activity.updateCart();

        notifyChildRangeChanged(parentPosition, childPosition, filteredList.get(parentPosition).ChildServices.size() - childPosition);
        dialog.dismiss();
    }

    @Override
    public Filter getFilter() {
        return new ChildFilter();
    }

    class ParentServiceViewHolder extends ParentViewHolder {

        @BindView(R.id.list_item_parent_service_name)
        TextView parentServiceName;

        @BindView(R.id.list_item_parent_service_arrow)
        ImageView parentServiceArrow;

        public ParentServiceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTitle(String title) {
            parentServiceName.setText(title);
        }
    }

    class ChildServiceVieHolder extends ChildViewHolder {

        @BindView(R.id.service_name)
        TextView serviceName;

        @BindView(R.id.main_price)
        TextView mainPrice;

        @BindView(R.id.view_service_details)
        TextView viewServiceDetails;

        @BindView(R.id.service_therapist_name)
        TextView serviceTherapistName; //, oldPrice

        @BindView(R.id.service_checkbox)
        CheckBox serviceCheckbox;

        public ChildServiceVieHolder(View itemView) {
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
                ArrayList<ParentServiceViewModel> tempParentFilteredList = new ArrayList<>();
                ArrayList<ServiceViewModel> tempChildFilteredList = new ArrayList<>();

                for (ParentServiceViewModel parentServiceViewModel : list) {
                    for (ServiceViewModel serviceViewModel : parentServiceViewModel.ChildServices) {
                        if (serviceViewModel.name.toLowerCase().contains(filterString.toLowerCase())) {
                            tempChildFilteredList.add(serviceViewModel);
                        }
                    }
                    if (!tempChildFilteredList.isEmpty())
                        tempParentFilteredList.add(new ParentServiceViewModel(parentServiceViewModel.id, parentServiceViewModel.name, parentServiceViewModel.Description, parentServiceViewModel.CategoryId, parentServiceViewModel.ServiceType, tempChildFilteredList));
                }

                filteredList = tempParentFilteredList;
            }

            results.count = filteredList.size();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredList = (ArrayList<ParentServiceViewModel>) filterResults.values;
            setParentList(filteredList, false);
            notifyDataSetChanged();
        }
    }
}
