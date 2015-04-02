package com.app.android.limetray;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;

import com.app.android.limetray.api.TwitterManager;
import com.twitter.sdk.android.core.TwitterException;


public class MainActivity extends ActionBarActivity {
    private ViewPager tabsViewPager = null;
    private ViewPagerAdapter viewPagerAdapter = null;
    private TwitterManager twitterManager = null;

    private static final String TAG = MainActivity.class.getName();

    // TODO Check internet connection
    // TODO Orientation change
    // TODO Logo design
    // TODO Secure keys
    // TODO Close DB
    // TODO Tweets latest should be on the top - Done
    // TODO New Tweet Toast
    // TODO Make offline working
    // TODO Observer model inside fragment

    private TwitterManager.LoginCallbackListener loginCallbackListener = new TwitterManager.LoginCallbackListener() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTypeface();

        twitterManager = TwitterManager.getInstance(this);

        if(!twitterManager.isGuestLogin()){
            setContentView(R.layout.layout_loading);
            loginTwitter();
        }else {
            removeActionBarShadow();
            setContentView(R.layout.activity_main);
            createTabs();
        }
    }

    private void setTypeface(){
        SpannableString spannableString = new SpannableString(getString(R.string.app_name));
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.otf");
        spannableString.setSpan(new CustomTypefaceSpan("", typeface), 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(spannableString);

    }

    @Override
    protected void onResume(){
        super.onResume();
        this.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    private void loginTwitter(){
        twitterManager.setLoginCallbackListener(loginCallbackListener);
        twitterManager.loginAsGuest();
    }

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
    protected void onStop() {
        super.onStop();
        twitterManager.stopTweety();
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
}
