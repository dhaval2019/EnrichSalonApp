package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

public class PackageBundleBrand implements Parcelable{

    public int PackageBundleBrandId;
    public int BrandId;

    protected PackageBundleBrand(Parcel in) {
        PackageBundleBrandId = in.readInt();
        BrandId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PackageBundleBrandId);
        dest.writeInt(BrandId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageBundleBrand> CREATOR = new Creator<PackageBundleBrand>() {
        @Override
        public PackageBundleBrand createFromParcel(Parcel in) {
            return new PackageBundleBrand(in);
        }

        @Override
        public PackageBundleBrand[] newArray(int size) {
            return new PackageBundleBrand[size];
        }
    };
}
