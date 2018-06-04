package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.AppointmentModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppointmentHomeAdapter extends RecyclerView.Adapter<AppointmentHomeAdapter.AppointmentViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<AppointmentModel> list;

    public AppointmentHomeAdapter(Context context, ArrayList<AppointmentModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.appointment_home_list_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppointmentViewHolder holder, int position) {
        AppointmentModel model = list.get(position);
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.appointment_date)
        TextView appointmentDate;

        @BindView(R.id.appointment_time)
        TextView appointmentTime;

        @BindView(R.id.appointment_title)
        TextView appointmentTitle;

        @BindView(R.id.appointment_store)
        TextView appointmentStore;

        @BindView(R.id.appointment_therapist)
        TextView appointmentTherapist;

        public AppointmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
