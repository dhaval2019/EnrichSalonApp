package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

public class PackageBundleTag implements Parcelable{

    public int PackageBundleTagId;
    public int TagId;
    public String Keyword;

    protected PackageBundleTag(Parcel in) {
        PackageBundleTagId = in.readInt();
        TagId = in.readInt();
        Keyword = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PackageBundleTagId);
        dest.writeInt(TagId);
        dest.writeString(Keyword);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageBundleTag> CREATOR = new Creator<PackageBundleTag>() {
        @Override
        public PackageBundleTag createFromParcel(Parcel in) {
            return new PackageBundleTag(in);
        }

        @Override
        public PackageBundleTag[] newArray(int size) {
            return new PackageBundleTag[size];
        }
    };
}
