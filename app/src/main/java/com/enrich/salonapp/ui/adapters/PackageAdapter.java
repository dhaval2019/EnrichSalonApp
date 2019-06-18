package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.Package.PackageModel;
import com.enrich.salonapp.ui.activities.PackageDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {

    Context context;
    ArrayList<PackageModel> list;
    LayoutInflater inflater;

    public PackageAdapter(Context context, ArrayList<PackageModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.package_list_item, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PackageViewHolder holder, int position) {
        final PackageModel model = list.get(position);

        holder.title.setText(model.PackageTitle);
        int i = (int) Math.round(model.StartingPrice);
        holder.subTitle.setText("From " + context.getResources().getString(R.string.Rs) + " " + i);
        if (model.Gender.equals("M")) {
            holder.genderImage.setImageResource(R.drawable.human_male);
        } else if (model.Gender.equals("F")) {
            holder.genderImage.setImageResource(R.drawable.human_female);
        } else if (model.Gender.equals("U")) {
            holder.genderImage.setImageResource(R.drawable.human_male_female);
        }

        Picasso.with(context).load(model.PackageImageWideURL).placeholder(R.drawable.placeholder_ext).into(holder.packageImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PackageDetailActivity.class);
                intent.putExtra("CreateOrderPackageBundleModel", model.PackageId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PackageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.sub_title)
        TextView subTitle;

        @BindView(R.id.desc_icon)
        ImageView genderImage;

        @BindView(R.id.package_image)
        ImageView packageImage;

        public PackageViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
