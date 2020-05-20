package com.example.parcial_v2.Data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MusicContract {

    public final static class MusicEntry implements BaseColumns {

        //name table
        public final static String TABLE_NAME = "Music";

        //columns
        public final static String _iD = BaseColumns._ID;
        public final static String TITLE = "title";
        public final static String AUTOR = "autor";
        public final static String DURATION = "duration";

    }

}
