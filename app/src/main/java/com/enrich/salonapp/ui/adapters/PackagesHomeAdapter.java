package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.Package.PackageModel;
import com.enrich.salonapp.ui.activities.PackageDetailActivity;
import com.enrich.salonapp.ui.activities.PackagesActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PackagesHomeAdapter extends RecyclerView.Adapter<PackagesHomeAdapter.PackageHomeViewHolder> {

    Context context;
    ArrayList<PackageModel> list;
    LayoutInflater inflater;

    public PackagesHomeAdapter(Context context, ArrayList<PackageModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public PackageHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.package_home_list_item, parent, false);
        return new PackageHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageHomeViewHolder holder, int position) {
        final PackageModel model = list.get(position);

        holder.packageTitle.setText(model.PackageTitle);
        holder.packageSubTitle.setText("From " + context.getResources().getString(R.string.Rs) + model.StartingPrice);
        Picasso.get().load(model.PackageImageURL).placeholder(R.drawable.placeholder).into(holder.packageImageSmall);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PackagesActivity.class);
//                intent.putExtra("CreateOrderPackageBundleModel", model.PackageId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PackageHomeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.package_image_small)
        ImageView packageImageSmall;

        @BindView(R.id.title_subtitle_container)
        LinearLayout titleSubtitleContainer;

        @BindView(R.id.package_title)
        TextView packageTitle;

        @BindView(R.id.package_sub_title)
        TextView packageSubTitle;

        @BindView(R.id.package_desc_icon)
        ImageView packageDescIcon;

        public PackageHomeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
