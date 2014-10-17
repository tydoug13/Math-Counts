package com.education.android.mathcounts;

import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Chronometer;

@SuppressLint("HandlerLeak")
public class TimerStopwatch extends HandlerThread {

	private static final String TAG = "Timer";
	private static final int START_TIMER = 0;
	
	public static TimerStopwatch mTimer;
	
	private static Handler mHandler;
	private static CountDownTimer mCountDownTimer;
	private static Chronometer mChronometer;
	private static java.util.Timer timer;
	private ChallengeTestActivity mActivity;
	
	private int mCurrentMiliSeconds = 0;

	private String mCurrentTime;
	private long mDuration;
	private long mInterval;
	private long runTime;
	
	public TimerStopwatch(ChallengeTestActivity c) {
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

	public void startTimerThread() {
		mHandler
			.obtainMessage(START_TIMER)
			.sendToTarget();
	}
	
	private void startTimer() {
		/*mCountDownTimer = new CountDownTimer(mDuration, mInterval) {

			@Override
			public void onFinish() {
				mActivity.setCurrentTime(mCurrentTime);
				mActivity.startResultsActivity();
			}

			@Override
			public void onTick(long milisecondsLeft) {
				mCurrentMiliSeconds += 1000;
				Log.d("fuckk", "" + milisecondsLeft + " " + mCurrentMiliSeconds);
				mCurrentTime = millisecondsToString(mCurrentMiliSeconds);	
				mActivity.setCurrentTime(mCurrentTime);
			}
			
		};
		
		mCountDownTimer.start();*/
		
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				mCurrentMiliSeconds += 1000;
				runTime = System.currentTimeMillis();
				
					mHandler.post(new Runnable() {
	
						@Override
						public void run() {
							long time = runTime - System.currentTimeMillis();
							mCurrentMiliSeconds += time;
							mCurrentTime = millisecondsToString(mCurrentMiliSeconds);
							mActivity.setCurrentTime(mCurrentTime);
						}
						
					});
			}
			
		}, 0, 1000);
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
		mHandler.removeMessages(START_TIMER);
		timer.cancel();
		mHandler = null;
		mTimer = null;
	}
	
	public static TimerStopwatch newInstance(ChallengeTestActivity test) {
		mTimer = new TimerStopwatch(test);
		timer = new java.util.Timer();
		return mTimer;
	}
}
