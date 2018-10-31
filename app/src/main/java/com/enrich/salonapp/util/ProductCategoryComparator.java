package com.enrich.salonapp.util;

import com.enrich.salonapp.data.model.Product.ProductCategoryModel;

import java.util.Comparator;

public class ProductCategoryComparator implements Comparator<ProductCategoryModel> {
    @Override
    public int compare(ProductCategoryModel lhs, ProductCategoryModel rhs) {
        return lhs.Order - rhs.Order;
    }
}
