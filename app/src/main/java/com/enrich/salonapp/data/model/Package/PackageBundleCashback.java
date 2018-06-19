package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

// CHILD CLASS
public class PackageBundleCashback extends BaseChildModel implements Parcelable {

    public int PackageBundleCashbackId;
    public int CashbackId;
    public String Title;
    public double Cost;
    public int CashBackMode;
    public String ValidTill;
    public String ValidityBasedOn;
    public int ValidityDays;

    protected PackageBundleCashback(Parcel in) {
        PackageBundleCashbackId = in.readInt();
        CashbackId = in.readInt();
        Title = in.readString();
        Cost = in.readDouble();
        CashBackMode = in.readInt();
        ValidTill = in.readString();
        ValidityBasedOn = in.readString();
        ValidityDays = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PackageBundleCashbackId);
        dest.writeInt(CashbackId);
        dest.writeString(Title);
        dest.writeDouble(Cost);
        dest.writeInt(CashBackMode);
        dest.writeString(ValidTill);
        dest.writeString(ValidityBasedOn);
        dest.writeInt(ValidityDays);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageBundleCashback> CREATOR = new Creator<PackageBundleCashback>() {
        @Override
        public PackageBundleCashback createFromParcel(Parcel in) {
            return new PackageBundleCashback(in);
        }

        @Override
        public PackageBundleCashback[] newArray(int size) {
            return new PackageBundleCashback[size];
        }
    };

    @Override
    public String getName() {
        return Title;
    }

    @Override
    public String getPrice() {
        return "" + Cost;
    }
}
