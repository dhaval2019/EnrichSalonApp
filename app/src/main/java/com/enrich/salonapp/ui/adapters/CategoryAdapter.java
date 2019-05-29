package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.CategoryModel;
import com.enrich.salonapp.ui.activities.CategoryActivity;
import com.enrich.salonapp.ui.activities.ServiceListActivity;
import com.enrich.salonapp.util.Constants;
import com.enrich.salonapp.util.EnrichUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<CategoryModel> list;
    int pos;
    BottomSheetDialog loginDialog;
    CategoryActivity categoryActivity;
    boolean isHomeSelected;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> list, CategoryActivity categoryActivity, boolean isHomeSelected) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.categoryActivity = categoryActivity;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_list_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        Picasso.with(context).load(list.get(position).ImageUrl.px400).placeholder(R.drawable.placeholder).into(holder.image);

        String[] handsSplit = list.get(position).Name.split(" ");
        if (handsSplit.length == 3) {
            holder.name.setText(handsSplit[0].toUpperCase() + "\n" + handsSplit[1].toUpperCase() + "\n" + handsSplit[2].toUpperCase());
        } else {
            holder.name.setText(list.get(position).Name.toUpperCase());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnrichUtils.getUserData(context) != null) {
                    Intent intent = new Intent(context, ServiceListActivity.class);
                    intent.putExtra("CategoryListPosition", position);
                    intent.putExtra("isHomeSelected", isHomeSelected);
                    context.startActivity(intent);
                } else {
                    showGenderDialog(position);
                }
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

    private void showGenderDialog(final int position) {
        loginDialog = new BottomSheetDialog(context);
        loginDialog.setContentView(R.layout.gender_selection_dialog);

        loginDialog.setCancelable(true);

        TextView ok = loginDialog.findViewById(R.id.gender_ok);
        TextView cancel = loginDialog.findViewById(R.id.gender_cancel);
        RelativeLayout genderFemaleContainer = loginDialog.findViewById(R.id.gender_female_container);
        RelativeLayout genderMaleContainer = loginDialog.findViewById(R.id.gender_male_container);
        Button loginButton = loginDialog.findViewById(R.id.gender_selector_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;
                categoryActivity.showLoginDialog();
            }
        });

        genderFemaleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ServiceListActivity.class);
                intent.putExtra("CategoryListPosition", position);
                intent.putExtra("isHomeSelected", isHomeSelected);
                intent.putExtra("Gender", Constants.FEMALE);
                context.startActivity(intent);
                loginDialog.dismiss();
            }
        });

        genderMaleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ServiceListActivity.class);
                intent.putExtra("CategoryListPosition", position);
                intent.putExtra("isHomeSelected", isHomeSelected);
                intent.putExtra("Gender", Constants.MALE);
                context.startActivity(intent);
                loginDialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });
        loginDialog.show();
    }

    public void userLoggedIn() {
        if (loginDialog != null) {
            loginDialog.dismiss();
        }
        Intent intent = new Intent(context, ServiceListActivity.class);
        intent.putExtra("CategoryListPosition", pos);
        intent.putExtra("isHomeSelected", isHomeSelected);
        context.startActivity(intent);
    }
}
