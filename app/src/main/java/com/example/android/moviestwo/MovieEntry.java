package com.example.android.moviestwo;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieEntry implements Parcelable {

    public static final Creator<MovieEntry> CREATOR = new Creator<MovieEntry>() {
        @Override
        public MovieEntry createFromParcel(Parcel in) {
            return new MovieEntry(in);
        }

        @Override
        public MovieEntry[] newArray(int size) {
            return new MovieEntry[size];
        }
    };

    private String movieId;
    private String title;
    private String posterUrl;
    private String synopsis;
    private String rating;
    private String releaseDate;

    // Constructor
    public MovieEntry(String movieId, String title, String posterUrl,
                      String synopsis, String rating, String releaseDate) {
        this.movieId = movieId;
        this.title = title;
        this.posterUrl = posterUrl;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return movieId;
    }

    public void setId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return posterUrl;
    }

    public void setPoster(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    // Parcelling part
    private MovieEntry(Parcel in) {
        this.movieId = in.readString();
        this.title = in.readString();
        this.posterUrl = in.readString();
        this.synopsis = in.readString();
        this.rating = in.readString();
        this.releaseDate = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieId);
        dest.writeString(this.title);
        dest.writeString(this.posterUrl);
        dest.writeString(this.synopsis);
        dest.writeString(this.rating);
        dest.writeString(this.releaseDate);
    }

    @Override
    public String toString() {
        return "MovieEntry{" +
                "movieId='" + movieId + '\'' +
                ", title='" + title + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", rating='" + rating + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}

