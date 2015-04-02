package com.app.android.limetray.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.Toast;

import com.app.android.limetray.R;
import com.app.android.limetray.api.TwitterManager;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.List;

/**
 * Created by blackadmin on 1/4/15.
 */
public class FragmentTweets extends android.support.v4.app.Fragment {
    private Context context = null;
    private TwitterManager twitterManager = null;
    private ProgressBar progressBarLoading = null;
    private static final String TWEET_SEARCH_STRING = "limetray";
    private LinearLayout layoutTweets = null;
    private boolean enableNewTweetToast = false;

    private TwitterManager.TweetListener tweetListener = new TwitterManager.TweetListener() {
        @Override
        public void onNewTweet(Tweet tweet) {
            updateUI(tweet);
        }
    };

    public static FragmentTweets newInstance() {
        FragmentTweets fragmentTweets = new FragmentTweets();
        return fragmentTweets;
    }

    public FragmentTweets() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        twitterManager = TwitterManager.getInstance(this.context);
        twitterManager.addTweetListener(tweetListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tweets, container, false);

        progressBarLoading = (ProgressBar) rootView.findViewById(R.id.pbarLoading);

        List<Tweet> tweetList = twitterManager.getAllTweets();
        layoutTweets = (LinearLayout) rootView.findViewById(R.id.layoutTweets);

        if(!tweetList.isEmpty()){
            updateUI(tweetList);
        }

        return rootView;
    }

    private void updateUI(List<Tweet> tweetList){
        for(Tweet tweet : tweetList){
            updateUI(tweet);
        }
    }

    private void updateUI(final Tweet tweet){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(null != progressBarLoading) {
                    progressBarLoading.setVisibility(View.GONE);
                }

                TweetView tweetView = new TweetView(context, tweet, R.style.tw__TweetDarkStyle);

                if(null != layoutTweets){
                    layoutTweets.addView(tweetView, 0);

                    // Empty view
                    View viewDivider = new View(context);
                    viewDivider.setLayoutParams(new LinearLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    viewDivider.setBackgroundColor(Color.rgb(51, 51, 51));
                    layoutTweets.addView(viewDivider, 1);
                }

                if(enableNewTweetToast){
                    Toast.makeText(context, "New Tweet By: " + tweet.user.name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        enableNewTweetToast = true;
        twitterManager.startTweety(TWEET_SEARCH_STRING);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(null != twitterManager && null != tweetListener){
            twitterManager.removeTweetListener(tweetListener);
            twitterManager.closeDb();
        }
    }

}
