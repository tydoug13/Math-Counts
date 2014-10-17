package com.education.android.mathcounts;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class TimerHome extends HandlerThread {

	private static final String TAG = "Timer";
	private static final int START_TIMER = 0;
	
	public static TimerHome mTimer;
	
	private static Handler mHandler;
	private static CountDownTimer mCountDownTimer;
	private HomeFragment mActivity;
	
	private String mCurrentTime;
	
	private long mDuration;
	private long mInterval;
	
	public TimerHome(HomeFragment c) {
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
		
		mHandler
			.obtainMessage(START_TIMER)
			.sendToTarget();
	}
	
	private void startTimer() {
		mCountDownTimer = new CountDownTimer(mDuration, mInterval) {

			@Override
			public void onFinish() {
				((HomeActivity)mActivity.getActivity()).getHomePager().setCurrentItem(1, true);
				finish();
			}

			@Override
			public void onTick(long milisecondsLeft) {
				
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
		if(mCountDownTimer != null) {
			mCountDownTimer.cancel();
		}
		if(mHandler != null) {
			mHandler.removeMessages(START_TIMER);
		}
		mHandler = null;
		mCountDownTimer = null;
		mTimer = null;
		
	}
	
	public static TimerHome newInstance(HomeFragment test) {
		mTimer = new TimerHome(test);
		return mTimer;
	}
}
