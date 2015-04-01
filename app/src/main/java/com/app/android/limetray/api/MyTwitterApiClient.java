package com.app.android.limetray.api;

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;

/**
 * Created by blackadmin on 1/4/15.
 */
public class MyTwitterApiClient extends TwitterApiClient {
    public MyTwitterApiClient(Session session) {
        super(session);
    }

    public TweetSearchService getTweetSearchService() {
        return getService(TweetSearchService.class);
    }
}
