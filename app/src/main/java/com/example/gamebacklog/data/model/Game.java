package com.example.gamebacklog.data.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Game implements Serializable {

    // Labels table name
    public static final String TABLE = "games";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PLATFORM = "platform";
    public static final String KEY_DATE = "date";
    public static final String KEY_STATUS = "status";
    public static final String KEY_NOTES = "notes";

    private int id;
    private String title;
    private String platform;
    private String dateAdded;
    private String status;
    private String notes;

    public Game() {

    }

    public Game(String title, String platform, String status, String notes) {
        this(-1, title, platform, status, notes);
        this.dateAdded = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    public Game(int id, String title, String platform, String status, String notes) {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.platform = platform;
        this.status = status;
    }

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
}
