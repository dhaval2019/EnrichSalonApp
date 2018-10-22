package com.enrich.salonapp.data.model.Product;

import android.os.Parcel;
import android.os.Parcelable;

public class OfferProductBrandTagsModel implements Parcelable {

    public int OfferProductBrandTagId;
    public int OfferId;
    public int ProductBrandId;

    public OfferProductBrandTagsModel(int offerProductBrandTagId, int offerId, int productBrandId) {
        OfferProductBrandTagId = offerProductBrandTagId;
        OfferId = offerId;
        ProductBrandId = productBrandId;
    }

    protected OfferProductBrandTagsModel(Parcel in) {
        OfferProductBrandTagId = in.readInt();
        OfferId = in.readInt();
        ProductBrandId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(OfferProductBrandTagId);
        dest.writeInt(OfferId);
        dest.writeInt(ProductBrandId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfferProductBrandTagsModel> CREATOR = new Creator<OfferProductBrandTagsModel>() {
        @Override
        public OfferProductBrandTagsModel createFromParcel(Parcel in) {
            return new OfferProductBrandTagsModel(in);
        }

        @Override
        public OfferProductBrandTagsModel[] newArray(int size) {
            return new OfferProductBrandTagsModel[size];
        }
    };
}
