package com.example.okke.data;

import android.os.Parcel;
import android.os.Parcelable;

public class RequestData implements Parcelable {

    private String ImageUrl;
    private String Quality;
    private String Size;
    private String mTwtId;

    protected RequestData(Parcel in) {
        ImageUrl = in.readString();
        Quality = in.readString();
        Size = in.readString();
        mTwtId = in.readString();
    }

    public static final Creator<RequestData> CREATOR = new Creator<RequestData>() {
        @Override
        public RequestData createFromParcel(Parcel in) {
            return new RequestData(in);
        }

        @Override
        public RequestData[] newArray(int size) {
            return new RequestData[size];
        }
    };

    public RequestData(String ImageUrl1, String finalQuality1, String finalSize1, String mJSONObject_twtId) {
        this.ImageUrl = ImageUrl1;
        this.Quality = finalQuality1;
        this.Size = finalSize1;
        this.mTwtId = mJSONObject_twtId;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getQuality() {
        return Quality;
    }

    public String getSize() {
        return Size;
    }

    public String getmTwtId() {
        return mTwtId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ImageUrl);
        parcel.writeString(Quality);
        parcel.writeString(Size);
        parcel.writeString(mTwtId);
    }
}
