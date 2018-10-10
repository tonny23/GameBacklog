package com.example.gamebacklog.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@Entity(tableName = "games")
public class Game implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "platform")
    private String platform;
    @ColumnInfo(name = "dateAdded")
    private String dateAdded;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "notes")
    private String notes;

    public Game() {

    }

    public Game(String title, String platform, String status, String notes) {
        this.title = title;
        this.platform = platform;
        this.dateAdded = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        this.status = status;
        this.notes = notes;
    }

    protected Game(Parcel in) {
        id = in.readInt();
        title = in.readString();
        platform = in.readString();
        dateAdded = in.readString();
        status = in.readString();
        notes = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(platform);
        dest.writeString(dateAdded);
        dest.writeString(status);
        dest.writeString(notes);
    }
}
