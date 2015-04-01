package com.app.android.limetray.api;

import android.content.Context;

import com.app.android.limetray.db.TweetDataSource;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;

import io.fabric.sdk.android.Fabric;

/**
 * Created by blackadmin on 1/4/15.
 */
public class TweetManager {
    private static TweetManager _instance = null;
    private Context context = null;
    private TweetDataSource tweetDataSource = null;
    private LoginCallbackListener loginCallbackListener = null;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "rQPwbY0s2BVgcW5xCxs5wHw7w";
    private static final String TWITTER_SECRET = "jlhiDxH5C7T2hK8ycumSdBEngTVpHVRpPl8H51AHs1dMA0Gj1E";

    private TweetManager(Context context){
        this.context = context;
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this.context, new Twitter(authConfig));
        tweetDataSource = new TweetDataSource(context);
    }

    public static TweetManager getInstance(Context context){
        if(null == _instance) {
            _instance = new TweetManager(context);
        }

        return _instance;
    }

    public void setLoginCallbackListener(LoginCallbackListener loginCallbackListener){
        this.loginCallbackListener = loginCallbackListener;
    }

    private Callback loginCallback = new Callback() {
        @Override
        public void success(Result appSessionResult) {
            if(null != loginCallbackListener){
                loginCallbackListener.onLoginSuccess();
            }

//            MyTwitterApiClient mtac = new MyTwitterApiClient(TwitterCore.getInstance().getAppSessionManager().getActiveSession());
//            try {
//                mtac.getTweetSearchService().search(URLEncoder.encode("limetray", "UTF-8"), new Callback<Search>() {
//                    @Override
//                    public void success(Result<Search> searchResult) {
//                        if(null != searchResult){
//                            for(Tweet tweet : searchResult.data.tweets){
//                                System.out.println(tweet.id);
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void failure(TwitterException ex) {
//                        System.out.println("failure: " + ex);
//
//                    }
//                });
//            }catch (Exception ex){
//                ex.printStackTrace();
//            }
        }

        @Override
        public void failure(TwitterException ex) {
            if(null != loginCallbackListener){
                loginCallbackListener.onLoginFailure(ex);
            }
        }
    };

    public boolean isGuestLogin(){
        return (null != TwitterCore.getInstance().getAppSessionManager().getActiveSession());
    }

    public void loginAsGuest() {
        TwitterCore.getInstance().logInGuest(loginCallback);
    }


    public interface LoginCallbackListener {
        void onLoginSuccess();
        void onLoginFailure(TwitterException ex);
    }

}
