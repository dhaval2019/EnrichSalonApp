package com.enrich.salonapp.data.model.Product;

public class ProductRequestModel {

    public int Index;
    public int[] ProductCategoryIds = new int[]{};
    public int[] BrandIds = new int[]{};
    public int Sort;
    public double MinAmount;
    public double MaxAmount;
    public int Gender;
    public int[] ProductSubCategoryIds = new int[]{};
}
