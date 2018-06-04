package com.enrich.salonapp.util;

import com.enrich.salonapp.data.model.CenterViewModel;

import java.util.Comparator;

public class DistanceComparator implements Comparator<CenterViewModel> {

    @Override
    public int compare(CenterViewModel o1, CenterViewModel o2) {
        if (o1.Distance < o2.Distance) return -1;
        if (o1.Distance > o2.Distance) return 1;
        return 0;
    }
}

