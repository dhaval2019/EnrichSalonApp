package com.enrich.salonapp.util;

import com.enrich.salonapp.data.model.ServiceList.SubCategoryModel;

import java.util.Comparator;

public class SubCategoryComparator implements Comparator<SubCategoryModel> {
    @Override
    public int compare(SubCategoryModel o1, SubCategoryModel o2) {
        if (o1.SortOrder < o2.SortOrder) return -1;
        if (o1.SortOrder > o2.SortOrder) return 1;
        return 0;
    }
}
