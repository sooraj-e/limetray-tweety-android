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
import android.widget.Toast;

import com.app.android.limetray.api.TwitterManager;
import com.twitter.sdk.android.core.TwitterException;


public class MainActivity extends ActionBarActivity {
    private ViewPager tabsViewPager = null;
    private ViewPagerAdapter viewPagerAdapter = null;
    private TwitterManager twitterManager = null;

    private static final String TAG = MainActivity.class.getName();

    // TODO Check internet connection - Done
    // TODO Orientation change - Only portrait for now - Done
    // TODO Logo design - Done
    // TODO Secure keys - Done
    // TODO Close DB - Done
    // TODO Tweets latest should be on the top - Done
    // TODO New Tweet Toast - Done
    // TODO Make offline working - Done
    // TODO Observer model inside fragment - Next Version

    private TwitterManager.LoginCallbackListener loginCallbackListener = new TwitterManager.LoginCallbackListener() {
        @Override
        public void onLoginSuccess() {
            createViewpager();
        }

        @Override
        public void onLoginFailure(TwitterException ex) {
            ex.printStackTrace();
            Toast.makeText(MainActivity.this, "Login Failed, Retry later !!!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTypeface();

        twitterManager = TwitterManager.getInstance(this);

        if (Util.isInternetAvailable(this) && !twitterManager.isGuestLogin()) {
            setContentView(R.layout.layout_loading);
            loginTwitter();
        } else {
            createViewpager();
        }
    }

    private void createViewpager() {
        removeActionBarShadow();
        setContentView(R.layout.activity_main);
        createTabs();
    }

    private void setTypeface() {
        SpannableString spannableString = new SpannableString(getString(R.string.app_name));
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Gotham-Medium.otf");
        spannableString.setSpan(new CustomTypefaceSpan("", typeface), 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(spannableString);

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        if (!Util.isInternetAvailable(this)) {
            Toast.makeText(this, "No Internet Conneciton Found !!!", Toast.LENGTH_LONG).show();
        }
    }

    private void loginTwitter() {
        twitterManager.setLoginCallbackListener(loginCallbackListener);
        twitterManager.loginAsGuest();
    }

    private void removeActionBarShadow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }
//        else{
//            setTheme(R.style.NoShadowActionBarTheme);
//        }
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
