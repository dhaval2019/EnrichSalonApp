package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.SlotModel;
import com.enrich.salonapp.ui.activities.DateSelectorActivity;
import com.enrich.salonapp.util.EnrichUtils;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlotsAdapter extends RecyclerView.Adapter<SlotsAdapter.SlotViewHolder> {

    Context context;
    ArrayList<SlotModel> list;
    LayoutInflater inflater;
    DateSelectorActivity dateSelectorActivity;
    int pos = -1;

    public SlotsAdapter(Context context, ArrayList<SlotModel> list, DateSelectorActivity dateSelectorActivity) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dateSelectorActivity = dateSelectorActivity;
    }

    @Override
    public SlotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.slot_list_item, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SlotViewHolder holder, final int position) {
//                    SimpleDateFormat stringToTime = new SimpleDateFormat("hh:mm:ss");
//                    String timeStr = list.get(position).Time.substring(11);
//                    Date time = stringToTime.parse(timeStr);
//                    SimpleDateFormat timeToString = new SimpleDateFormat("hh:mm a");
//                    String dateStr = timeToString.format(time);

        // String Format
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
        LocalTime localTime = LocalTime.parse(list.get(position).Time.substring(11));
        String newDateStr = dateTimeFormatter.format(localTime);

        holder.slotTextView.setText(newDateStr);
        holder.slotTextView.setTag(position);
        if (this.pos == position) {
            holder.slotTextView.setBackgroundResource(R.drawable.gold_bg_gradient_curved);
            holder.slotTextView.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.slotTextView.setBackgroundResource(R.drawable.grey_round_rect_border);
            holder.slotTextView.setTextColor(Color.parseColor("#787878"));
        }

        holder.slotTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlotsAdapter.this.pos = (int) holder.slotTextView.getTag();
                onBindViewHolder(holder, pos);
                notifyDataSetChanged();
                dateSelectorActivity.setDateToReserveSlot(list.get(position).Time);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SlotViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.slot_text_view)
        TextView slotTextView;
        View view;

        @BindView(R.id.time_slot_container)
        RelativeLayout timeSlotContainer;

        public SlotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
