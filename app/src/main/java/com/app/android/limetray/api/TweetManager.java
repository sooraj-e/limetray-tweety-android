package com.app.android.limetray.api;

import android.content.Context;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;

import java.net.URLEncoder;

import io.fabric.sdk.android.Fabric;

/**
 * Created by blackadmin on 1/4/15.
 */
public class TweetManager {
    private static TweetManager _instance = null;
    private Context context = null;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "rQPwbY0s2BVgcW5xCxs5wHw7w";
    private static final String TWITTER_SECRET = "jlhiDxH5C7T2hK8ycumSdBEngTVpHVRpPl8H51AHs1dMA0Gj1E";

    private TweetManager(Context context){
        this.context = context;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this.context, new Twitter(authConfig));
    }

    public static TweetManager getInstance(Context context){
        if(null == _instance) {
            _instance = new TweetManager(context);
        }

        return _instance;
    }

    private Callback loginCallback = new Callback() {
        @Override
        public void success(Result appSessionResult) {
            System.out.println("Success");
            MyTwitterApiClient mtac = new MyTwitterApiClient(TwitterCore.getInstance().getAppSessionManager().getActiveSession());
            try {
                mtac.getTweetSearchService().search(URLEncoder.encode("limetray", "UTF-8"), new Callback<Search>() {
                    @Override
                    public void success(Result<Search> searchResult) {
                        if(null != searchResult){
                            for(Tweet tweet : searchResult.data.tweets){
                                System.out.println(tweet.id);
                            }
                        }

                    }

                    @Override
                    public void failure(TwitterException ex) {
                        System.out.println("failure: " + ex);

                    }
                });
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        @Override
        public void failure(TwitterException ex) {
            System.out.println("Failure: " + ex);
        }
    };

    public void loginAsGuest() {
        TwitterCore.getInstance().logInGuest(loginCallback);

    }



}
