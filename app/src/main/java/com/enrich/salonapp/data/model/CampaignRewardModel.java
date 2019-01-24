package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CampaignRewardModel implements Parcelable {

    public int Id;
    public int CampaignId;
    public String CampaignName;
    public String RewardName;
    public int RewardValue;
    public int RewardWeightage;
    public String CampaignType;
    public int Count;

    public CampaignRewardModel() {
    }

    protected CampaignRewardModel(Parcel in) {
        Id = in.readInt();
        CampaignId = in.readInt();
        CampaignName = in.readString();
        RewardName = in.readString();
        RewardValue = in.readInt();
        RewardWeightage = in.readInt();
        CampaignType = in.readString();
        Count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeInt(CampaignId);
        dest.writeString(CampaignName);
        dest.writeString(RewardName);
        dest.writeInt(RewardValue);
        dest.writeInt(RewardWeightage);
        dest.writeString(CampaignType);
        dest.writeInt(Count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CampaignRewardModel> CREATOR = new Creator<CampaignRewardModel>() {
        @Override
        public CampaignRewardModel createFromParcel(Parcel in) {
            return new CampaignRewardModel(in);
        }

        @Override
        public CampaignRewardModel[] newArray(int size) {
            return new CampaignRewardModel[size];
        }
    };
}
