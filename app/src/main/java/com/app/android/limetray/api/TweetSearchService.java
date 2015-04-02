package com.app.android.limetray.api;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Search;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by blackadmin on 1/4/15.
 */
interface TweetSearchService {
    @GET("/1.1/search/tweets.json")
    void search(@Query("q") String searchString, Callback<Search> callbackTweets);

    @GET("/1.1/search/tweets.json")
    void search(@Query("q") String searchString, @Query("since_id") long lastTweetId, Callback<Search> callbackTweets);
}
