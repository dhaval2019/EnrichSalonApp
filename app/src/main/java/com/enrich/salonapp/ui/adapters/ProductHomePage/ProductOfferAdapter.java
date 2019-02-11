package com.enrich.salonapp.ui.adapters.ProductHomePage;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.enrich.salonapp.R;
import com.enrich.salonapp.data.model.OfferModel;
import com.enrich.salonapp.util.OfferHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductOfferAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<OfferModel> list;
    private LayoutInflater inflater;

    public ProductOfferAdapter(Context context, ArrayList<OfferModel> list) {
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View myImageLayout = inflater.inflate(R.layout.product_offer_list_item, container, false);
        ImageView myImage = myImageLayout.findViewById(R.id.banner_image);

        Picasso.with(context).load(list.get(position).OfferImageURL).placeholder(R.drawable.placeholder_ext).into(myImage);

        container.addView(myImageLayout, 0);

        myImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfferHandler.handleOfferRedirection((Activity) context, list.get(position));
            }
        });

        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
