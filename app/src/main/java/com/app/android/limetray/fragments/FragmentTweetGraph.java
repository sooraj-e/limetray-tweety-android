package com.app.android.limetray.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.app.android.limetray.R;
import com.app.android.limetray.api.TwitterManager;
import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * Created by blackadmin on 1/4/15.
 */
public class FragmentTweetGraph extends android.support.v4.app.Fragment {
    private Context context = null;
    private TwitterManager twitterManager = null;
    private ProgressBar progressBarLoading = null;
    private LineGraph lineGraphTweets = null;
    private Line lineTweets = null;
    private LinePoint linePointTweet = null;

    private TwitterManager.TweetListener tweetListener = new TwitterManager.TweetListener() {
        @Override
        public void onNewTweet(Tweet tweet) {
            updateGraph(tweet);
        }
    };

    public static FragmentTweetGraph newInstance() {
        FragmentTweetGraph fragmentTweetGraph = new FragmentTweetGraph();
        return fragmentTweetGraph;
    }

    public FragmentTweetGraph() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}

        twitterManager = TwitterManager.getInstance(this.context);
        twitterManager.addTweetListener(tweetListener);

        lineTweets = new Line();
        linePointTweet = new LinePoint();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tweet_graph, container, false);

        progressBarLoading = (ProgressBar) rootView.findViewById(R.id.pbarLoading);
        lineGraphTweets = (LineGraph) rootView.findViewById(R.id.graphTweets);

        List<Tweet> tweetList = twitterManager.getAllTweets();

        if(!tweetList.isEmpty()){
            updateGraph(tweetList);
        }

        return rootView;
    }

    private void updateGraph(List<Tweet> tweetList){
        for(Tweet tweet : tweetList){
            updateGraph(tweet);
        }
    }

    private void updateGraph(final Tweet tweet){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lineGraphTweets.setVisibility(View.VISIBLE);

                if(null != progressBarLoading) {
                    progressBarLoading.setVisibility(View.GONE);
                }


            }
        });
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
        }
    }

}
