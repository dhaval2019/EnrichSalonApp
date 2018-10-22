package com.enrich.salonapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.CategoryModel;

import java.util.ArrayList;

public class CategorySpinnerAdapter extends ArrayAdapter<CategoryModel> {

    Context context;
    LayoutInflater inflater;
    ArrayList<CategoryModel> list;

    public CategorySpinnerAdapter(@NonNull Context context, @NonNull ArrayList<CategoryModel> list) {
        super(context, R.layout.category_spinner_list_item, list);
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.category_spinner_dropdown_list_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.category_name);

        name.setText(list.get(position).Name);

        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.category_spinner_list_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.category_name);

        name.setText(list.get(position).Name);

        return convertView;
    }
}
