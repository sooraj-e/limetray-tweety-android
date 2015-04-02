package com.app.android.limetray.api;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by blackadmin on 2/4/15.
 */
class TweetUpdateTimer {
    private Timer timer = null;
    private TimerTask timerTask = null;
    private TweetUpdateTimerListener tweetUpdateTimerListener = null;

    private static final long DEFAULT_TIMER_START_TIME = 2L * 1000L;
    private static final long DEFAULT_TIMER_INTERVAL = 60L * 1000L;

    private long timerStart = DEFAULT_TIMER_START_TIME;
    private long timerInterval = DEFAULT_TIMER_INTERVAL;

    void startTimer(TweetUpdateTimerListener tweetUpdateTimerListener) {
        this.tweetUpdateTimerListener = tweetUpdateTimerListener;

        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, timerStart, timerInterval); //
    }

    void setTimerStartTime(long timerStart){
        this.timerStart = timerStart;
    }

    void setTimerInterval(long timerInterval){
        this.timerInterval = timerInterval;
    }

    void stopTimer() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
            tweetUpdateTimerListener = null;
        }
    }

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (null != tweetUpdateTimerListener) {
                    tweetUpdateTimerListener.onTick();
                }
            }
        };
    }

    interface TweetUpdateTimerListener {
        void onTick();
    }
}
