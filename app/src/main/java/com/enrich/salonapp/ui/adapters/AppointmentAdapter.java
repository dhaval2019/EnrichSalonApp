package com.enrich.salonapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.model.AppointmentModel;
import com.enrich.salonapp.data.model.CancelRequestModel;
import com.enrich.salonapp.data.model.CancelResponseModel;
import com.enrich.salonapp.data.model.CenterDetailModel;
import com.enrich.salonapp.data.model.ServiceViewModel;
import com.enrich.salonapp.data.model.TherapistModel;
import com.enrich.salonapp.data.remote.RemoteDataSource;
import com.enrich.salonapp.di.Injection;
import com.enrich.salonapp.ui.activities.RescheduleActivity;
import com.enrich.salonapp.ui.contracts.CancelAppointmentContract;
import com.enrich.salonapp.ui.presenters.CancelAppointmentsPresenter;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> implements CancelAppointmentContract.View {

    Activity activity;
    LayoutInflater inflater;
    ArrayList<AppointmentModel> list;
    boolean isCurrent;
    boolean isHomeSelected;
    int pos;
    String appGroupId;
    BottomSheetDialog dialog;

    DataRepository dataRepository;
    CancelAppointmentsPresenter cancelAppointmentsPresenter;

    public AppointmentAdapter(Activity activity, ArrayList<AppointmentModel> list, boolean isCurrent) {
        this.activity = activity;
        this.list = list;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isCurrent = isCurrent;

        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();

        dataRepository = Injection.provideDataRepository(activity, mainUiThread, threadExecutor, null);
        cancelAppointmentsPresenter = new CancelAppointmentsPresenter(this, dataRepository);
    }

    @Override
    public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.current_appointment_list_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppointmentViewHolder holder, final int position) {
        final AppointmentModel model = list.get(position);
        try {
            SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat stringToTime = new SimpleDateFormat("hh:mm:ss");

            Date date = stringToDate.parse(model.AppointmentServices.get(0).StartTime);
            Date time = stringToTime.parse(model.AppointmentServices.get(0).StartTime.substring(11, model.AppointmentServices.get(0).StartTime.length()));

            SimpleDateFormat dateToString = new SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat timeToString = new SimpleDateFormat("hh:mm a");

            String dateStr = dateToString.format(date);
            String timeStr = timeToString.format(time);

            holder.appointmentDate.setText(dateStr);
            holder.appointmentTime.setText(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.appointmentDate.setText("-");
            holder.appointmentTime.setText("-");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < model.AppointmentServices.size(); i++) {
            sb.append(model.AppointmentServices.get(i).Service.name + ",");
        }
        String appointmentTitleStr = sb.toString().substring(0, sb.toString().length() - 1);
        holder.appointmentTitle.setText(appointmentTitleStr);
        holder.appointmentStore.setText(model.Center.Name);
        holder.appointmentTherapist.setText("Stylist: " + model.AppointmentServices.get(0).RequestedTherapist.FullName);

        holder.appointmentPrice.setText(activity.getResources().getString(R.string.Rs) + " " + list.get(position).Price._final);

        if (isCurrent) {
            holder.appointmentCancel.setVisibility(View.VISIBLE);
            holder.appointmentReschedule.setVisibility(View.GONE);
        } else {
            holder.appointmentCancel.setVisibility(View.GONE);
            holder.appointmentReschedule.setVisibility(View.VISIBLE);
            holder.appointmentReschedule.setText("REBOOK");
        }

        holder.appointmentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = position;
                showCancelDialog(model.AppointmentGroupId);
            }
        });

        holder.appointmentReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ServiceViewModel> serviceList = new ArrayList<>();
                for (int j = 0; j < model.AppointmentServices.size(); j++) {
                    TherapistModel therapistModel = new TherapistModel();
                    therapistModel.Id = model.AppointmentServices.get(j).RequestedTherapist.Id;
                    therapistModel.FirstName = model.AppointmentServices.get(j).RequestedTherapist.FirstName;
                    therapistModel.LastName = model.AppointmentServices.get(j).RequestedTherapist.LastName;
                    therapistModel.DisplayName = model.AppointmentServices.get(j).RequestedTherapist.DisplayName;

                    model.AppointmentServices.get(j).Service.therapist = therapistModel;
                    serviceList.add(model.AppointmentServices.get(j).Service);

                    if (model.AppointmentServices.get(j).BookingFor == 1) {
                        isHomeSelected = true;
                    } else {
                        isHomeSelected = false;
                    }
                }

                CenterDetailModel centerDetailModel = new CenterDetailModel();
                centerDetailModel.Id = model.Center.Id;
                centerDetailModel.Phone = model.Center.Phone1.Number;
                centerDetailModel.Address = model.Center.Address1 + " " + model.Center.Address2;
                centerDetailModel.Email = model.Center.Email;
                centerDetailModel.Name = model.Center.Name;
                centerDetailModel.CenterType = model.Center.CenterType;

                EnrichUtils.saveHomeStoreForRebook(activity, EnrichUtils.newGson().toJson(centerDetailModel));

                RescheduleServiceList rescheduleServiceList = new RescheduleServiceList();
                rescheduleServiceList.serviceList = serviceList;

                Intent intent = new Intent(activity, RescheduleActivity.class);
                intent.putExtra("isHomeSelected", isHomeSelected);
                intent.putExtra("RescheduleServiceList", EnrichUtils.newGson().toJson(rescheduleServiceList));
                activity.startActivity(intent);
            }
        });
    }

    public class RescheduleServiceList {
        public ArrayList<ServiceViewModel> serviceList;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void appointmentCancelled(CancelResponseModel model) {
        if (model.isAppointmentStatusSet()) {
            Iterator itr = list.iterator();
            while (itr.hasNext()) {
                AppointmentModel appointmentModel = (AppointmentModel) itr.next();
                if (appointmentModel.AppointmentGroupId.equals(appGroupId))
                    itr.remove();
            }
            notifyDataSetChanged();
            dialog.cancel();
        } else {
            EnrichUtils.showMessage(activity, "Appointment Already cancelled.");
        }
    }

    @Override
    public void showToastMessage(String message) {
        EnrichUtils.log(message);
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

    public void updateList(ArrayList<AppointmentModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.appointment_date)
        TextView appointmentDate;

        @BindView(R.id.appointment_time)
        TextView appointmentTime;

        @BindView(R.id.appointment_title)
        TextView appointmentTitle;

        @BindView(R.id.appointment_price)
        TextView appointmentPrice;

        @BindView(R.id.appointment_store)
        TextView appointmentStore;

        @BindView(R.id.appointment_therapist)
        TextView appointmentTherapist;

        @BindView(R.id.appointment_cancel)
        TextView appointmentCancel;

        @BindView(R.id.appointment_reschedule)
        TextView appointmentReschedule;

        public AppointmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void showCancelDialog(final String appointmentGroupId) {
        View view = inflater.inflate(R.layout.cancel_service_dialog, null);
        dialog = new BottomSheetDialog(activity);
        dialog.setContentView(view);
        dialog.setCancelable(true);

        RadioButton reason_1 = dialog.findViewById(R.id.reason_1);
        RadioButton reason_2 = dialog.findViewById(R.id.reason_2);
        RadioButton reason_3 = dialog.findViewById(R.id.reason_3);
        final RadioGroup reasonRadioButtonGrp = dialog.findViewById(R.id.reason_radio_button_grp);
        TextView appointmentCancelButton = dialog.findViewById(R.id.appointment_cancel_button);
        TextView appointmentDontCancelButton = dialog.findViewById(R.id.appointment_dont_cancel_button);

        Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/Montserrat-Regular.ttf");
        reason_1.setTypeface(tf);
        reason_2.setTypeface(tf);
        reason_3.setTypeface(tf);

        appointmentCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = reasonRadioButtonGrp.getCheckedRadioButtonId();

                if (selectedId != -1) {
                    String reasonStr = ((RadioButton) dialog.findViewById(selectedId)).getText().toString();
                    appGroupId = appointmentGroupId;
                    cancelAppointment(reasonStr, appointmentGroupId);
                } else {
                    EnrichUtils.showMessage(activity, "Please select a reason");
                }
            }
        });

        appointmentDontCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void cancelAppointment(String comment, String appointmentGroupId) {
        CancelRequestModel model = new CancelRequestModel();
        model.setComments(comment);
        model.setReasonId(99);
        cancelAppointmentsPresenter.cancelAppointment(activity, RemoteDataSource.HOST + "Catalog/Appointments/" + appointmentGroupId + "/Cancel", model);
    }
}
