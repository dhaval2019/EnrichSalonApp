package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PriceModel implements Parcelable {

    @SerializedName("CurrencyId")
    public double currencyId;
    @SerializedName("Sales")
    public double sales;
    @SerializedName("MembershipPrice")
    public double membershipPrice;
    @SerializedName("Tax")
    public double tax;
    @SerializedName("Final")
    public double _final;
    @SerializedName("Final1")
    public double final1;
    @SerializedName("Discount")
    public double discount;
    @SerializedName("Tip")
    public double tip;
    @SerializedName("SSG")
    public double sSG;
    @SerializedName("RoundingCorrection")
    public double roundingCorrection;

    protected PriceModel(Parcel in) {
        currencyId = in.readInt();
        sales = in.readDouble();
        membershipPrice = in.readDouble();
        tax = in.readInt();
        _final = in.readInt();
        final1 = in.readInt();
        discount = in.readInt();
        tip = in.readInt();
        sSG = in.readInt();
        roundingCorrection = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(currencyId);
        dest.writeDouble(sales);
        dest.writeDouble(membershipPrice);
        dest.writeDouble(tax);
        dest.writeDouble(_final);
        dest.writeDouble(final1);
        dest.writeDouble(discount);
        dest.writeDouble(tip);
        dest.writeDouble(sSG);
        dest.writeDouble(roundingCorrection);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PriceModel> CREATOR = new Creator<PriceModel>() {
        @Override
        public PriceModel createFromParcel(Parcel in) {
            return new PriceModel(in);
        }

        @Override
        public PriceModel[] newArray(int size) {
            return new PriceModel[size];
        }
    };
}
