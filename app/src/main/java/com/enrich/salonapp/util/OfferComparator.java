package com.enrich.salonapp.util;

import com.enrich.salonapp.data.model.OfferModel;

import java.util.Comparator;

public class OfferComparator implements Comparator<OfferModel> {
    @Override
    public int compare(OfferModel t, OfferModel t1) {
        return t.Orders-t1.Orders;
    }
}
