
package com.twittzel.Hassan.data.api.result;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Parcelable{

    private String url;

    private String szie;

    private String quality;
    public final static Creator<Datum> CREATOR = new Creator<Datum>() {


        public Datum createFromParcel(Parcel in) {
            return new Datum(in);
        }

        public Datum[] newArray(int size) {
            return (new Datum[size]);
        }

    }
    ;

    protected Datum(Parcel in) {
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.szie = ((String) in.readValue((String.class.getClassLoader())));
        this.quality = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Datum() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSzie() {
        return szie;
    }

    public void setSzie(String szie) {
        this.szie = szie;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(url);
        dest.writeValue(szie);
        dest.writeValue(quality);
    }

    public int describeContents() {
        return  0;
    }

}
