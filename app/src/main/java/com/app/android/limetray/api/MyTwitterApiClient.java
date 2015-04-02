package com.app.android.limetray.api;

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;

/**
 * Created by blackadmin on 1/4/15.
 */
class MyTwitterApiClient extends TwitterApiClient {
    MyTwitterApiClient(Session session) {
        super(session);
    }

    TweetSearchService getTweetSearchService() {
        return getService(TweetSearchService.class);
    }
}
