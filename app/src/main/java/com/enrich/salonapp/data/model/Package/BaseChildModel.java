package com.enrich.salonapp.data.model.Package;

import android.os.Parcelable;

// BASE CHILD CLASS (ABSTRACT)
public abstract class BaseChildModel implements Parcelable{

    public abstract String getName();

    public abstract String getPrice();
}
