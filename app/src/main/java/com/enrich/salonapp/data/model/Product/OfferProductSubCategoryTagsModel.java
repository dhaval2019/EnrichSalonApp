package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class OfferProductSubCategoryTagsModel implements Parcelable{

    public int OfferProductSubCategoryTagId;
    public int OfferId;
    public int ProductSubCategoryId;

    public OfferProductSubCategoryTagsModel(int offerProductSubCategoryTagId, int offerId, int productSubCategoryId) {
        OfferProductSubCategoryTagId = offerProductSubCategoryTagId;
        OfferId = offerId;
        ProductSubCategoryId = productSubCategoryId;
    }

    protected OfferProductSubCategoryTagsModel(Parcel in) {
        OfferProductSubCategoryTagId = in.readInt();
        OfferId = in.readInt();
        ProductSubCategoryId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(OfferProductSubCategoryTagId);
        dest.writeInt(OfferId);
        dest.writeInt(ProductSubCategoryId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfferProductSubCategoryTagsModel> CREATOR = new Creator<OfferProductSubCategoryTagsModel>() {
        @Override
        public OfferProductSubCategoryTagsModel createFromParcel(Parcel in) {
            return new OfferProductSubCategoryTagsModel(in);
        }

        @Override
        public OfferProductSubCategoryTagsModel[] newArray(int size) {
            return new OfferProductSubCategoryTagsModel[size];
        }
    };
}
