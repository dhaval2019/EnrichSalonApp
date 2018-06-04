package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TherapistModel implements Parcelable {

    public String Id;
    public String Name;
    public String FirstName;
    public String LastName;
    public String MiddleName;
    public String NickName;
    public String DisplayName;
    public int Gender;
    public String Email;
    public String BaseCenterId;
    public ImagePathsModel ImagePaths;

    protected TherapistModel(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        MiddleName = in.readString();
        NickName = in.readString();
        DisplayName = in.readString();
        Gender = in.readInt();
        Email = in.readString();
        BaseCenterId = in.readString();
        ImagePaths = in.readParcelable(ImagePathsModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Name);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(MiddleName);
        dest.writeString(NickName);
        dest.writeString(DisplayName);
        dest.writeInt(Gender);
        dest.writeString(Email);
        dest.writeString(BaseCenterId);
        dest.writeParcelable(ImagePaths, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TherapistModel> CREATOR = new Creator<TherapistModel>() {
        @Override
        public TherapistModel createFromParcel(Parcel in) {
            return new TherapistModel(in);
        }

        @Override
        public TherapistModel[] newArray(int size) {
            return new TherapistModel[size];
        }
    };
}
