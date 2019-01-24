package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CampaignRewardResponseModel implements Parcelable {

    public ArrayList<CampaignRewardModel> ProductCampaignReward;
    public ArrayList<CampaignRewardModel> ServiceCampaignReward;
    public ErrorModel Error;

    protected CampaignRewardResponseModel(Parcel in) {
        ProductCampaignReward = in.createTypedArrayList(CampaignRewardModel.CREATOR);
        ServiceCampaignReward = in.createTypedArrayList(CampaignRewardModel.CREATOR);
        Error = in.readParcelable(ErrorModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(ProductCampaignReward);
        dest.writeTypedList(ServiceCampaignReward);
        dest.writeParcelable(Error, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CampaignRewardResponseModel> CREATOR = new Creator<CampaignRewardResponseModel>() {
        @Override
        public CampaignRewardResponseModel createFromParcel(Parcel in) {
            return new CampaignRewardResponseModel(in);
        }

        @Override
        public CampaignRewardResponseModel[] newArray(int size) {
            return new CampaignRewardResponseModel[size];
        }
    };
}
