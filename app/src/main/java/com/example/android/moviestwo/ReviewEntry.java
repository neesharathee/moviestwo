package com.example.android.moviestwo;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewEntry implements Parcelable {

    public static final Creator<ReviewEntry> CREATOR = new Creator<ReviewEntry>() {
        @Override
        public ReviewEntry createFromParcel(Parcel in) {
            return new ReviewEntry(in);
        }

        @Override
        public ReviewEntry[] newArray(int size) {
            return new ReviewEntry[size];
        }
    };

    private String reviewId;
    private String author;
    private String content;
    private String url;

    // Constructor
    public ReviewEntry(String reviewId, String author, String content, String url) {
        this.reviewId = reviewId;
        this.author = author;
        this.content = content;
        this.url = url;

    }

    public String getId() {
        return reviewId;
    }

    public void setId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // Parcelling part
    private ReviewEntry(Parcel in) {
        this.reviewId = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reviewId);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }

    @Override
    public String toString() {
        return "ReviewEntry{" +
                "reviewId='" + reviewId + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

