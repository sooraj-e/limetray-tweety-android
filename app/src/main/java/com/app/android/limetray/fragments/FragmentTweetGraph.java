package com.app.android.limetray.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.app.android.limetray.R;
import com.app.android.limetray.api.TwitterManager;
import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.twitter.sdk.android.core.models.Tweet;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by blackadmin on 1/4/15.
 */
public class FragmentTweetGraph extends android.support.v4.app.Fragment {
    private Context context = null;
    private TwitterManager twitterManager = null;
    private ProgressBar progressBarLoading = null;
    //private LineGraph lineGraphTweets = null;
    private BarGraph barGraph = null;
    private List<Tweet> tweetList = null;
    //private List<String> tweetDateList = null;
    private List<GraphCoords> graphCoordsList = null;

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

        tweetList = new ArrayList<>();
        //tweetDateList = new ArrayList<>();
        graphCoordsList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tweet_graph, container, false);

        progressBarLoading = (ProgressBar) rootView.findViewById(R.id.pbarLoading);
        barGraph = (BarGraph) rootView.findViewById(R.id.graph);
        //barGraph.setuni

        tweetList = twitterManager.getAllTweets();

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
        String createdAt = tweet.createdAt; //Thu Mar 26 16:01:14 +0000 2015
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US); // The mask

        try{
            Date date = dateFormat.parse(createdAt); // parsing the String into a Date using the mask
            Calendar calendar = Calendar.getInstance();
            calendar .setTime(date);
            createdAt = calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        GraphCoords graphCoords = new GraphCoords();
        for(GraphCoords graphCoordExists : graphCoordsList){
            if(createdAt.equalsIgnoreCase(graphCoordExists.getCreatedAt())){
                // updateY
                graphCoordExists.setY(graphCoordExists.getY() + 1);
                graphCoords = graphCoordExists;
            }
        }

        if(null == graphCoords.getCreatedAt()){
            graphCoords.setCreatedAt(createdAt);
            if(!graphCoordsList.isEmpty()) {                //graphCoords.setX(graphCoordsList.get(graphCoordsList.size() - 1).getX() + 2);

            }
            graphCoords.setY(graphCoords.getY() + 1);
            graphCoordsList.add(graphCoords);
        }



        updateUI();

    }

    private void updateUI(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                barGraph.setVisibility(View.VISIBLE);

                if(null != progressBarLoading) {
                    progressBarLoading.setVisibility(View.GONE);
                }

                ArrayList<Bar> barList = new ArrayList<Bar>();
                System.out.println(graphCoordsList.size());
                for(GraphCoords graphCoords : graphCoordsList){
                    Bar bar = new Bar();
                    bar.setColor(Color.parseColor("#99CC00"));
                    bar.setName(graphCoords.createdAt);
                    bar.setValue(graphCoords.getY());
                    barList.add(bar);
                }

                barGraph.setBars(barList);


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


    class GraphCoords implements Serializable{
        private String createdAt = null;
        private int x;
        private int y;

        GraphCoords(){
            this(null, 0, 0);
        }

        GraphCoords(String createdAt, int x, int y) {
            this.createdAt = createdAt;
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
