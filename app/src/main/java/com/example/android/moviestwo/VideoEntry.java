package com.example.android.moviestwo;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoEntry implements Parcelable {

    public static final Creator<VideoEntry> CREATOR = new Creator<VideoEntry>() {
        @Override
        public VideoEntry createFromParcel(Parcel in) {
            return new VideoEntry(in);
        }

        @Override
        public VideoEntry[] newArray(int size) {
            return new VideoEntry[size];
        }
    };

    private String YTVideoKey;
    private String type;
    private String title;

    // Constructor
    public VideoEntry(String YTVideoKey, String type, String title) {
        this.YTVideoKey = YTVideoKey;
        this.type = type;
        this.title = title;
    }

    public String getId() {
        return YTVideoKey;
    }

    public void setId(String YTVideoKey) {
        this.YTVideoKey = YTVideoKey;
    }

    public String getAuthor() {
        return type;
    }

    public void setAuthor(String type) {
        this.type = type;
    }

    public String getContent() {
        return title;
    }

    public void setContent(String title) {
        this.title = title;
    }

    // Parcelling part
    private VideoEntry(Parcel in) {
        this.YTVideoKey = in.readString();
        this.type = in.readString();
        this.title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.YTVideoKey);
        dest.writeString(this.type);
        dest.writeString(this.title);
    }

    @Override
    public String toString() {
        return "VideoEntry{" +
                "YTVideoKey='" + YTVideoKey + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}

