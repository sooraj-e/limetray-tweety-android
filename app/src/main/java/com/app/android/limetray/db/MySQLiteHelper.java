package com.app.android.limetray.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by blackadmin on 25/11/14.
 */
class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "db_tweety";
    private static final int DB_VERSION = 1;

    // Below is the table for list of connected controllers.
    static interface TableTweets {
        public static final String TABLE_TWEETS = "tbl_tweets";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TWEET_JSON = "tweet_json";

        // Database creation sql statement
        public static final String DB_CREATE_TBL_TWEETS = "CREATE TABLE " + TABLE_TWEETS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_TWEET_JSON + " TEXT NOT NULL);";
    }

    MySQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TableTweets.DB_CREATE_TBL_TWEETS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableTweets.TABLE_TWEETS);
        onCreate(sqLiteDatabase);
    }
}
