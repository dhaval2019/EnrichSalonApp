package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthenticationModel implements Parcelable {

    @SerializedName("access_token")
    @Expose
    public String accessToken;
    @SerializedName("token_type")
    @Expose
    public String tokenType;
    @SerializedName("expires_in")
    @Expose
    public int expiresIn;
    @SerializedName("refresh_token")
    @Expose
    public String refreshToken;
    @SerializedName("UserName")
    @Expose
    public String userName;
    @SerializedName("IsAuthenticated")
    @Expose
    public String isAuthenticated;
    @SerializedName("AccountName")
    @Expose
    public String accountName;
    @SerializedName("OrganizationId")
    @Expose
    public String organizationId;
    @SerializedName("CenterId")
    @Expose
    public String centerId;
    @SerializedName("UserId")
    @Expose
    public String userId;
    @SerializedName("TimeZoneId")
    @Expose
    public String timeZoneId;
    @SerializedName("CultureId")
    @Expose
    public String cultureId;
    @SerializedName("CurrencyId")
    @Expose
    public String currencyId;
    @SerializedName("CenterName")
    @Expose
    public String centerName;
    @SerializedName("RoleName")
    @Expose
    public String roleName;
    @SerializedName("AppId")
    @Expose
    public String appId;
    @SerializedName("refresh")
    @Expose
    public String refresh;
    @SerializedName("issued")
    @Expose
    public String issued;
    @SerializedName("expires")
    @Expose
    public String expires;

    public AuthenticationModel() {
    }


    protected AuthenticationModel(Parcel in) {
        accessToken = in.readString();
        tokenType = in.readString();
        expiresIn = in.readInt();
        refreshToken = in.readString();
        userName = in.readString();
        isAuthenticated = in.readString();
        accountName = in.readString();
        organizationId = in.readString();
        centerId = in.readString();
        userId = in.readString();
        timeZoneId = in.readString();
        cultureId = in.readString();
        currencyId = in.readString();
        centerName = in.readString();
        roleName = in.readString();
        appId = in.readString();
        refresh = in.readString();
        issued = in.readString();
        expires = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessToken);
        dest.writeString(tokenType);
        dest.writeInt(expiresIn);
        dest.writeString(refreshToken);
        dest.writeString(userName);
        dest.writeString(isAuthenticated);
        dest.writeString(accountName);
        dest.writeString(organizationId);
        dest.writeString(centerId);
        dest.writeString(userId);
        dest.writeString(timeZoneId);
        dest.writeString(cultureId);
        dest.writeString(currencyId);
        dest.writeString(centerName);
        dest.writeString(roleName);
        dest.writeString(appId);
        dest.writeString(refresh);
        dest.writeString(issued);
        dest.writeString(expires);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AuthenticationModel> CREATOR = new Creator<AuthenticationModel>() {
        @Override
        public AuthenticationModel createFromParcel(Parcel in) {
            return new AuthenticationModel(in);
        }

        @Override
        public AuthenticationModel[] newArray(int size) {
            return new AuthenticationModel[size];
        }
    };
}
