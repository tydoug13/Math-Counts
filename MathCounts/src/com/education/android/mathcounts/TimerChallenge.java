package com.education.android.mathcounts;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class TimerChallenge extends HandlerThread {

	private static final String TAG = "Timer";
	private static final int START_TIMER = 0;
	
	public static TimerChallenge mTimer;
	
	private Handler mHandler;
	private CountDownTimer mCountDownTimer;
	private ChallengeTestActivity mActivity;
	
	private String mCurrentTime;
	
	private long mDuration;
	private long mInterval;
	
	private boolean isAnimation = false;
	
	public TimerChallenge(ChallengeTestActivity c) {
		super(TAG);
		mActivity = c;
		
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == START_TIMER) {
					startTimer();
				}
			}
		};
	}

	public void startTimerThread(long duration, long interval) {
		mDuration = duration;
		mInterval = interval;
		
		if(mDuration == ChallengeTestActivity.ANIMATION_TIME)
			isAnimation = true;
		
		mHandler
			.obtainMessage(START_TIMER)
			.sendToTarget();
	}
	
	private void startTimer() {
		mCountDownTimer = new CountDownTimer(mDuration, mInterval) {

			@Override
			public void onFinish() {
				if(isAnimation) {
					mActivity.animationFinished();
				} else {
				mCurrentTime = "0:00";
				mActivity.setCurrentTime(mCurrentTime, true);
				mActivity.startResultsActivity();
				}
			}

			@Override
			public void onTick(long milisecondsLeft) {
				if(isAnimation)
					return;
				mCurrentTime = millisecondsToString(milisecondsLeft);
				if(milisecondsLeft <= 11000) 							
					mActivity.setCurrentTime(mCurrentTime, true);		//Late < 10 seconds, true if late
				else
					mActivity.setCurrentTime(mCurrentTime, false);
			}
			
		};
		
		mCountDownTimer.start();
	}
	
	/**
	 * Takes readable time string and returns time in milliseconds
	 * @param time	ex. 1:30
	 * @return time in milliseconds
	 */
	public static long getMilliSeconds(String time) {
		int minutes = 0;
		int seconds = 0;
		for(int i = 0; i < time.length() - 1; i++) {
			if(time.substring(i, i + 1).equals(":")) {
				if(i == 0) {
					seconds = Integer.parseInt(time.substring(i + 1));
				} else {
					minutes = Integer.parseInt(time.substring(0, i));
					seconds = Integer.parseInt(time.substring(i + 1));
				}
			}
		}
		long totalMilliSeconds = (long) ((minutes * 60) + seconds) * 1000;
		return totalMilliSeconds;
	}
	
	/**
	 * Takes total time in milliseconds and returns readable string
	 * @param milliseconds
	 * @return readable time string
	 */
	public static String millisecondsToString(long milliseconds) {
		int min = (int) milliseconds / 60000;
		int sec = ((int) (milliseconds - (min * 60000)) / 1000);
		if(sec == 60) {
			min += 1;
			sec = 0;
		}
		if(sec < 10) {
			return min + ":0" + sec;
		}
		
		return min + ":" + sec;
	}
	
	public void finish() {
		mCountDownTimer.cancel();
		mHandler.removeMessages(START_TIMER);
		
		mCountDownTimer = null;
		mHandler = null;
		mTimer = null;
	}
	
	public static TimerChallenge newInstance(ChallengeTestActivity test) {
		mTimer = new TimerChallenge(test);
		return mTimer;
	}
}
