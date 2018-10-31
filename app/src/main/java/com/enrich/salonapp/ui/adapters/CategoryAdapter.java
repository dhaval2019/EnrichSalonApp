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
import com.enrich.salonapp.data.model.CategoryModel;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<CategoryModel> list;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.category_list_item, parent, false);
            return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        Picasso.get().load(list.get(position).ImageUrl.px400).placeholder(R.drawable.placeholder).into(holder.image);

        String[] handsSplit = list.get(position).Name.split(" ");
        if (handsSplit.length == 3) {
            holder.name.setText(handsSplit[0].toUpperCase() + "\n" + handsSplit[1].toUpperCase() + "\n" + handsSplit[2].toUpperCase());
        } else {
            holder.name.setText(list.get(position).Name.toUpperCase());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ServiceListActivity.class);
                intent.putExtra("CategoryList", list);
                intent.putExtra("CategoryListPosition", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cat_image)
        ImageView image;

        @BindView(R.id.cat_name)
        TextView name;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
