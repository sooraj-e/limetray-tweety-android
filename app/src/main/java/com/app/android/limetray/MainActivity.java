package com.app.android.limetray;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.app.android.limetray.api.TweetManager;
import com.twitter.sdk.android.core.TwitterException;


public class MainActivity extends ActionBarActivity {
    private ViewPager tabsViewPager = null;
    private ViewPagerAdapter viewPagerAdapter = null;
    private TweetManager tweetManager = null;

    private static final String TAG = MainActivity.class.getName();

    // TODO Check internet connection
    // TODO Orientation change
    // TODO Logo design

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tweetManager = TweetManager.getInstance(this);

        if(!tweetManager.isGuestLogin()){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_loading);

            loginTwitter();
        }else{
            removeActionBarShadow();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            createTabs();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        this.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    private void loginTwitter(){
        tweetManager.setLoginCallbackListener(loginCallbackListener);
        tweetManager.loginAsGuest();
    }

    private TweetManager.LoginCallbackListener loginCallbackListener = new TweetManager.LoginCallbackListener() {
        @Override
        public void onLoginSuccess() {
            recreate();
        }

        @Override
        public void onLoginFailure(TwitterException ex) {
            ex.printStackTrace();
            //Log.e(TAG, "ex");
        }
    };


    private void removeActionBarShadow(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getSupportActionBar().setElevation(0);
        }else{
            setTheme(R.style.NoShadowActionBarTheme);
        }
    }

    private void createTabs() {
        tabsViewPager = (ViewPager) findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabsViewPager.setAdapter(viewPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tweets, container, false);
            return rootView;
        }
    }
}
