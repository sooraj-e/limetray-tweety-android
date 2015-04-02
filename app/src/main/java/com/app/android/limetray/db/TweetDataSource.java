package com.app.android.limetray.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blackadmin on 4/12/14.
 */
public class TweetDataSource {
    private SQLiteDatabase sqLiteDatabase = null;
    private MySQLiteHelper mySQLiteHelper = null;
    private String[] allColumns = {MySQLiteHelper.TableTweets.COLUMN_ID, MySQLiteHelper.TableTweets.COLUMN_TWEET_JSON};

    public TweetDataSource(Context context){
        mySQLiteHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        sqLiteDatabase = mySQLiteHelper.getWritableDatabase();
    }

    public void close(){
        mySQLiteHelper.close();
    }

    synchronized public boolean addOrUpdateTweet(Tweet tweet) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.TableTweets.COLUMN_ID, tweet.id);
        values.put(MySQLiteHelper.TableTweets.COLUMN_TWEET_JSON, new Gson().toJson(tweet));

        int updateRows = sqLiteDatabase.update(MySQLiteHelper.TableTweets.TABLE_TWEETS, values
                , MySQLiteHelper.TableTweets.COLUMN_ID + "='" + tweet.id + "'", null);
        if(updateRows > 0) return true;

        long insertId = sqLiteDatabase.insert(MySQLiteHelper.TableTweets.TABLE_TWEETS, null, values);
        return (-1 != insertId);
    }

    public Tweet getTweet(long _id){
        Cursor cursor = sqLiteDatabase.query(MySQLiteHelper.TableTweets.TABLE_TWEETS, allColumns,
                MySQLiteHelper.TableTweets.COLUMN_ID + " = '" + _id + "'", null, null, null, null);
        if(cursor.moveToFirst()) {
            Tweet tweet = cursorToTweet(cursor);
            return tweet;
        }
        return null;
    }

    public Tweet getLastTweet(){
        Cursor cursor = sqLiteDatabase.query(MySQLiteHelper.TableTweets.TABLE_TWEETS, allColumns,
                null, null, null, null, MySQLiteHelper.TableTweets.COLUMN_ID + " DESC", String.valueOf(1));
        if(cursor.moveToFirst()) {
            Tweet tweet = cursorToTweet(cursor);
            return tweet;
        }
        return null;
    }

    public List<Tweet> getAllTweets() {
        List<Tweet> tweetList = new ArrayList<Tweet>();
        Cursor cursor = sqLiteDatabase.query(MySQLiteHelper.TableTweets.TABLE_TWEETS,
                allColumns, null, null, null, null, MySQLiteHelper.TableTweets.COLUMN_ID + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tweet tweet = cursorToTweet(cursor);
            tweetList.add(tweet);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tweetList;
    }

    private Tweet cursorToTweet(Cursor cursor) {
        Tweet tweet = new Gson().fromJson(cursor.getString(1), Tweet.class);
        return tweet;
    }
}
