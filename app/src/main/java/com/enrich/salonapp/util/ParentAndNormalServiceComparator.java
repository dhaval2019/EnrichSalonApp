package com.enrich.salonapp.util;

import com.enrich.salonapp.data.model.ServiceList.SubCategoryModel;
import com.enrich.salonapp.data.model.ServiceViewModel;

import java.util.Comparator;

public class ParentAndNormalServiceComparator implements Comparator<ServiceViewModel> {
    @Override
    public int compare(ServiceViewModel o1, ServiceViewModel o2) {
        if (o1.sortOrder < o2.sortOrder) return -1;
        if (o1.sortOrder > o2.sortOrder) return 1;
        return 0;
    }
}
