package com.example.jeffr.bakingapp.dataobjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Step implements Parcelable {
    private int id;
    private String description;
    private String videoURL;
    private String thumbnialURL;

    public Step(){

    }

    public Step(int id,String description, String videoURL, String thumbnialURL) {
        this.id = id;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnialURL = thumbnialURL;
    }

    public String getThumbnialURL() {
        return thumbnialURL;
    }

    public void setThumbnialURL(String thumbnialURL) {
        this.thumbnialURL = thumbnialURL;
    }

    public String getVideoURL() {

        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.description);
        dest.writeString(this.videoURL);
        dest.writeString(this.thumbnialURL);
    }

    protected Step(Parcel in) {
        this.id = in.readInt();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnialURL = in.readString();
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
