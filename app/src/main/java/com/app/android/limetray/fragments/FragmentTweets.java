package com.app.android.limetray.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.android.limetray.R;

/**
 * Created by blackadmin on 1/4/15.
 */
public class FragmentTweets extends android.support.v4.app.Fragment {
private Context context = null;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tweets, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
