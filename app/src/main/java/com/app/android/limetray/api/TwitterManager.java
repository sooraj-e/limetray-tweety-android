package com.app.android.limetray.api;

import android.content.Context;

import com.app.android.limetray.db.TweetDataSource;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by blackadmin on 1/4/15.
 */
public class TwitterManager implements TweetUpdateTimer.TweetUpdateTimerListener{
    private static TwitterManager _instance = null;
    private Context context = null;
    private TweetDataSource tweetDataSource = null;
    private LoginCallbackListener loginCallbackListener = null;
    private List<TweetListener> tweetListenerList = null;
    private String searchString = null;
    private TweetUpdateTimer tweetUpdateTimer = null;
    private AppSession appSession = null;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "rQPwbY0s2BVgcW5xCxs5wHw7w";
    private static final String TWITTER_SECRET = "jlhiDxH5C7T2hK8ycumSdBEngTVpHVRpPl8H51AHs1dMA0Gj1E";

    private TwitterManager(Context context){
        this.context = context;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this.context, new Twitter(authConfig));
        tweetDataSource = new TweetDataSource(context);
        tweetListenerList = new ArrayList<>();

        tweetDataSource.open();

        tweetUpdateTimer = new TweetUpdateTimer();
    }

    public static TwitterManager getInstance(Context context){
        if(null == _instance) {
            _instance = new TwitterManager(context);
        }

        return _instance;
    }

    public void setLoginCallbackListener(LoginCallbackListener loginCallbackListener){
        this.loginCallbackListener = loginCallbackListener;
    }

    public void addTweetListener(TweetListener tweetListener){
        this.tweetListenerList.add(tweetListener);
    }

    public void removeTweetListener(TweetListener tweetListener){
        this.tweetListenerList.remove(tweetListener);
    }

    public List<Tweet> getAllTweets(){
        return tweetDataSource.getAllTweets();
    }

    private Callback callbackLogin = new Callback<AppSession>() {
        @Override
        public void success(Result<AppSession> appSessionResult) {
            if(null != appSessionResult && null != loginCallbackListener){
                appSession = appSessionResult.data;
                loginCallbackListener.onLoginSuccess();
            }
        }

        @Override
        public void failure(TwitterException ex) {
            if(null != loginCallbackListener){
                loginCallbackListener.onLoginFailure(ex);
            }
        }
    };

    public boolean isGuestLogin(){
        return (null != getSession());
    }

    public void loginAsGuest() {
        TwitterCore.getInstance().logInGuest(callbackLogin);
    }

    public void startTweety(String searchString){
        setSearchString(searchString);
        tweetUpdateTimer.startTimer(this);
    }

    public void stopTweety(){
        tweetUpdateTimer.stopTimer();
    }

    public void setSearchString(String searchString) {
        try{
            this.searchString = URLEncoder.encode(searchString, "UTF-8");
        }catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onTick() {
        if(isGuestLogin()) {
            MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(getSession());

            Tweet lastTweet = tweetDataSource.getLastTweet();

            long lastTweetId;
                if (null != lastTweet) {
                    lastTweetId = lastTweet.id;
                    myTwitterApiClient.getTweetSearchService().search(searchString, lastTweetId, callbackTweetSearch);

                }else {
                    // On empty db
                    myTwitterApiClient.getTweetSearchService().search(searchString, callbackTweetSearch);
                }

        }
    }

    private AppSession getSession(){
        //return TwitterCore.getInstance().getAppSessionManager().getActiveSession();
        return appSession;
    }

    private Callback<Search> callbackTweetSearch = new Callback<Search>() {
        @Override
        public void success(Result<Search> searchResult) {
            if (null != searchResult) {
                for (Tweet tweet : searchResult.data.tweets) {
                    if(tweetDataSource.addOrUpdateTweet(tweet)){
                        notifyTweetUpdate(tweet);
                    }
                }
            }
        }

        @Override
        public void failure(TwitterException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    };

    private void notifyTweetUpdate(Tweet tweet){
        for(TweetListener tweetListener : tweetListenerList){
            tweetListener.onNewTweet(tweet);
        }
    }

    public interface LoginCallbackListener {
        void onLoginSuccess();
        void onLoginFailure(TwitterException ex);
    }

    public interface TweetListener {
        void onNewTweet(Tweet tweet);
    }

}
