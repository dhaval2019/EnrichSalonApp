package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.enrich.salonapp.EnrichApplication;
import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.GenericCartModel;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingSummaryItemAdapter extends RecyclerView.Adapter<BookingSummaryItemAdapter.BookingSummaryViewHolder> {

    Context context;
    ArrayList<GenericCartModel> list;
    LayoutInflater inflater;

    public BookingSummaryItemAdapter(Context context, ArrayList<GenericCartModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public BookingSummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.booking_summary_list_item, parent, false);
        return new BookingSummaryViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(BookingSummaryViewHolder holder, int position) {
        GenericCartModel model = list.get(position);
        holder.item.setText(String.format("%s", model.Name.trim()));
       /* if (type.equalsIgnoreCase("service")) {
            int i = (int)Math.round(model.getPrice() );
            holder.price.setText(String.format("%s %s", context.getResources().getString(R.string.Rs), i));
        }else
         {*/
             holder.price.setText(context.getResources().getString(R.string.Rs)+ (int) Math.round((model.getPrice())*(model.getQuantity())));
     //   }


    }

    class BookingSummaryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.booking_summary_item_label)
        TextView item;

        @BindView(R.id.booking_summary_item_price)
        TextView price;

        public BookingSummaryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
