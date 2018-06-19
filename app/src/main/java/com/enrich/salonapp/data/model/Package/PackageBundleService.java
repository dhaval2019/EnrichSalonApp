package com.enrich.salonapp.data.model.Package;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PackageBundleService extends BaseChildModel implements Parcelable {

    public int PackageBundleServiceId;
    public int Id;
    public String ServiceId;
    public String Gender;
    public String Name;
    public int Duration;
    public String PriceText;
    public double SalesPrice;
    public double FinalPrice;
    public double Final1Price;
    public boolean HasVariant;
    public String CategoryId;
    public double DiscountPrice;
    public double TipPrice;
    public double SSGPrice;
    public double RoundingCorrection;
    public String CenterId;
    public boolean PopularService;
    public ArrayList<PackageBundleServiceCenter> Center;
    public ArrayList<PackageBundleCity> PackageBundleCity;

    protected PackageBundleService(Parcel in) {
        PackageBundleServiceId = in.readInt();
        Id = in.readInt();
        ServiceId = in.readString();
        Gender = in.readString();
        Name = in.readString();
        Duration = in.readInt();
        PriceText = in.readString();
        SalesPrice = in.readDouble();
        FinalPrice = in.readDouble();
        Final1Price = in.readDouble();
        HasVariant = in.readByte() != 0;
        CategoryId = in.readString();
        DiscountPrice = in.readDouble();
        TipPrice = in.readDouble();
        SSGPrice = in.readDouble();
        RoundingCorrection = in.readDouble();
        CenterId = in.readString();
        PopularService = in.readByte() != 0;
        Center = in.createTypedArrayList(PackageBundleServiceCenter.CREATOR);
        PackageBundleCity = in.createTypedArrayList(com.enrich.salonapp.data.model.Package.PackageBundleCity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PackageBundleServiceId);
        dest.writeInt(Id);
        dest.writeString(ServiceId);
        dest.writeString(Gender);
        dest.writeString(Name);
        dest.writeInt(Duration);
        dest.writeString(PriceText);
        dest.writeDouble(SalesPrice);
        dest.writeDouble(FinalPrice);
        dest.writeDouble(Final1Price);
        dest.writeByte((byte) (HasVariant ? 1 : 0));
        dest.writeString(CategoryId);
        dest.writeDouble(DiscountPrice);
        dest.writeDouble(TipPrice);
        dest.writeDouble(SSGPrice);
        dest.writeDouble(RoundingCorrection);
        dest.writeString(CenterId);
        dest.writeByte((byte) (PopularService ? 1 : 0));
        dest.writeTypedList(Center);
        dest.writeTypedList(PackageBundleCity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageBundleService> CREATOR = new Creator<PackageBundleService>() {
        @Override
        public PackageBundleService createFromParcel(Parcel in) {
            return new PackageBundleService(in);
        }

        @Override
        public PackageBundleService[] newArray(int size) {
            return new PackageBundleService[size];
        }
    };

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public String getPrice() {
        return "" + FinalPrice;
    }
}
