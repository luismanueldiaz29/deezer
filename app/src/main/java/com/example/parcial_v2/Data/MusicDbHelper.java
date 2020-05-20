package com.example.parcial_v2.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class MusicDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 1;

    public MusicDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MUSIC_TABLE = "CREATE TABLE "+ MusicContract.MusicEntry.TABLE_NAME+"("
                + MusicContract.MusicEntry._iD+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MusicContract.MusicEntry.TITLE+" TEXT NOT NULL, "
                + MusicContract.MusicEntry.AUTOR+" TEXT NOT NULL, "
                + MusicContract.MusicEntry.DURATION+" TEXT NOT NULL );";
        db.execSQL(SQL_CREATE_MUSIC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
