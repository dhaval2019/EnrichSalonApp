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
import com.enrich.salonapp.data.model.MyPackage.MyPackageModel;
import com.enrich.salonapp.ui.activities.MyPackageDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPackageAdapter extends RecyclerView.Adapter<MyPackageAdapter.MyPackageViewHolder> {

    Context context;
    ArrayList<MyPackageModel> list;
    LayoutInflater inflater;

    public MyPackageAdapter(Context context, ArrayList<MyPackageModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyPackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_package_list_item, parent, false);
        return new MyPackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPackageViewHolder holder, int position) {
        final MyPackageModel model = list.get(position);

        holder.title.setText(model.PackageTitle);
        holder.subTitle.setText(context.getResources().getString(R.string.Rs) + " " + model.StartingPrice);
        if (model.Gender.equals("M")) {
            holder.genderImage.setImageResource(R.drawable.human_male);
        } else if (model.Gender.equals("F")) {
            holder.genderImage.setImageResource(R.drawable.human_female);
        } else if (model.Gender.equals("U")) {
            holder.genderImage.setImageResource(R.drawable.human_male_female);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyPackageDetailActivity.class);
                intent.putExtra("MyPackageDetail", model);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyPackageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.sub_title)
        TextView subTitle;

        @BindView(R.id.desc_icon)
        ImageView genderImage;

        public MyPackageViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
