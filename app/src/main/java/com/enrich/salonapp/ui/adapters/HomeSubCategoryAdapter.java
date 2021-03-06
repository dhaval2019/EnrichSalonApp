package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.ServiceList.SubCategoryModel;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeSubCategoryAdapter extends RecyclerView.Adapter<HomeSubCategoryAdapter.HomeSubCategoryViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<SubCategoryModel> list;
    boolean isHomeSelected;

    public HomeSubCategoryAdapter(Context context, ArrayList<SubCategoryModel> list, boolean isHomeSelected) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isHomeSelected = isHomeSelected;
    }

    @NonNull
    @Override
    public HomeSubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_home_list_item, parent, false);
        return new HomeSubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeSubCategoryViewHolder holder, final int position) {
        Picasso.with(context).load(list.get(position).ImageUrl.px200).placeholder(R.drawable.placeholder).into(holder.image);

        String[] handsSplit = list.get(position).Name.split(" ");
        if (handsSplit.length == 3) {
            holder.name.setText(String.format("%s\n%s\n%s", handsSplit[0].toUpperCase(), handsSplit[1].toUpperCase(), handsSplit[2].toUpperCase()));
        } else {
            holder.name.setText(list.get(position).Name.toUpperCase());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ServiceListActivity.class);
                intent.putExtra("CategoryListPosition", position);
                intent.putExtra("isHomeSelected", isHomeSelected);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HomeSubCategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cat_image)
        ImageView image;

        @BindView(R.id.cat_name)
        TextView name;

        public HomeSubCategoryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
