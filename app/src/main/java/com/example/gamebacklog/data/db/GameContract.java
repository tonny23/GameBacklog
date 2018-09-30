package com.example.gamebacklog.data.db;

import android.provider.BaseColumns;

public interface GameContract extends BaseColumns {

    // Labels table name
    public static final String TABLE_NAME = "Games";
    // Labels Table Columns names
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_PLATFORM = "platform";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_STATUS = "status";
    public static final String COLUMN_NAME_NOTES = "notes";
}