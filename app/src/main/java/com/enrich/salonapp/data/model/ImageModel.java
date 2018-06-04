package com.enrich.salonapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {

    public int previewHeight;
    public int previewWidth;
    public int likes;
    public int favorites;
    public int webformatHeight;
    public int views;
    public int webformatWidth;
    public int comments;
    public int downloads;
    public int imageWidth;
    public int user_id;
    public long id;
    public String user;
    public String type;
    public String userImageURL;
    public int imageHeight;
    public String pageURL;
    public String previewURL;
    public String webformatURL;
    public String tags;


    protected ImageModel (Parcel in) {
        previewHeight = in.readInt();
        previewWidth = in.readInt();
        likes = in.readInt();
        favorites = in.readInt();
        webformatHeight = in.readInt();
        views = in.readInt();
        webformatWidth = in.readInt();
        comments = in.readInt();
        downloads = in.readInt();
        imageWidth = in.readInt();
        user_id = in.readInt();
        id = in.readLong();
        user = in.readString();
        type = in.readString();
        userImageURL = in.readString();
        imageHeight = in.readInt();
        pageURL = in.readString();
        previewURL = in.readString();
        webformatURL = in.readString();
        tags = in.readString();
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
        dest.writeInt(previewHeight);
        dest.writeInt(previewWidth);
        dest.writeInt(likes);
        dest.writeInt(favorites);
        dest.writeInt(webformatHeight);
        dest.writeInt(views);
        dest.writeInt(webformatWidth);
        dest.writeInt(comments);
        dest.writeInt(downloads);
        dest.writeInt(imageWidth);
        dest.writeInt(user_id);
        dest.writeLong(id);
        dest.writeString(user);
        dest.writeString(type);
        dest.writeString(userImageURL);
        dest.writeInt(imageHeight);
        dest.writeString(pageURL);
        dest.writeString(previewURL);
        dest.writeString(webformatURL);
        dest.writeString(tags);
    }

    @Override
    public int describeContents () {
        return 0;
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel (Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray (int size) {
            return new ImageModel[size];
        }
    };
}
