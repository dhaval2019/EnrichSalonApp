package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class OfferProductCategoryTagsModel implements Parcelable{

    public int OfferProductCategoryTagId;
    public int OfferId;
    public int ProductCategoryId;

    public OfferProductCategoryTagsModel(int offerProductCategoryTagId, int offerId, int productCategoryId) {
        OfferProductCategoryTagId = offerProductCategoryTagId;
        OfferId = offerId;
        ProductCategoryId = productCategoryId;
    }

    protected OfferProductCategoryTagsModel(Parcel in) {
        OfferProductCategoryTagId = in.readInt();
        OfferId = in.readInt();
        ProductCategoryId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(OfferProductCategoryTagId);
        dest.writeInt(OfferId);
        dest.writeInt(ProductCategoryId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfferProductCategoryTagsModel> CREATOR = new Creator<OfferProductCategoryTagsModel>() {
        @Override
        public OfferProductCategoryTagsModel createFromParcel(Parcel in) {
            return new OfferProductCategoryTagsModel(in);
        }

        @Override
        public OfferProductCategoryTagsModel[] newArray(int size) {
            return new OfferProductCategoryTagsModel[size];
        }
    };
}
