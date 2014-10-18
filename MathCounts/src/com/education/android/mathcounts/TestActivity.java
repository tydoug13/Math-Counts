package com.education.android.mathcounts;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	private Timer mTimer;
	private TestManager mTestManager;
	private TextView mTimerTextView;
	private Button mSubmitButton;
	ArrayList<Card> mCardList = new ArrayList<Card>();
	
	private String mOperation;
	
	private boolean isViewed;
	private boolean isSaved;
	
	private int mCurrentPage;
	private int mNumQuestions;
	private EditText mAnswerInput;
	private Fragment mCurrentFragment;
	private TestFinishedFragment mTestFinishedFragment;
	
	private long mId;

	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		
		setContentView(R.layout.fragment_keyboard);
		
		mOperation = intent.getStringExtra(TestManager.OPERATION);
		
		if(mOperation.equals(TestManager.ADDITION)) {
		   getWindow().getDecorView().setBackgroundColor(Color.rgb(255, 193, 193));
		  }
		else if(mOperation.equals(TestManager.SUBTRACTION)) {
		   getWindow().getDecorView().setBackgroundColor(Color.rgb(200, 235, 242));
		  }
		else if(mOperation.equals(TestManager.MULTIPLICATION)) {
		   getWindow().getDecorView().setBackgroundColor(Color.rgb(204, 255, 204));
		  }
		else if(mOperation.equals(TestManager.DIVISION)) {
		   getWindow().getDecorView().setBackgroundColor(Color.rgb(233, 210, 255));
		  }
		
		mNumQuestions = intent.getIntExtra(TestManager.QUESTIONS, -1);
		int difficulty = intent.getIntExtra(TestManager.DIFFICULTY, -1);
		
		mTestManager = TestManager.newInstance(mOperation, mNumQuestions, 	//Creates new instance of test manager based on selected options
				difficulty, getApplicationContext());
		mTestManager.generateQuestions();									//Populates ArrayList<Card> for appropriate test
		
		String time = intent.getStringExtra(TestManager.TIME);
		String name = intent.getStringExtra(TestManager.NAME);
		if(time.equals("\u221E"))
			time = "-";
		
		mTestManager.setTime(time);
		mTestManager.setStudentName(name);
		
		mCardList.addAll(mTestManager.getCards());							//Gets the populated ArrayList<Card>
		
		FragmentManager fm = getSupportFragmentManager();
		
		final ImageView increment = (ImageView) findViewById(R.id.incrementQuestions);
		final ImageView decrement = (ImageView) findViewById(R.id.decrementQuestions);
		
		increment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mCurrentPage < mCardList.size()) {
					mViewPager.setCurrentItem(mCurrentPage + 1); 
				}
			}
		});
		
		decrement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mCurrentPage > 0) {
					mViewPager.setCurrentItem(mCurrentPage - 1);
				}
			}
		});

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setId(R.id.viewPager);
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public Fragment getItem(int pos) {
				if(pos == mCardList.size()) {
					mTestFinishedFragment = TestFinishedFragment.newInstance();
					return mTestFinishedFragment;
				}
				Card card = mCardList.get(pos);
				TestFragment fragment 
					= TestFragment.newInstance(card, mOperation);
				
				return fragment;
			}

			@Override
			public void setPrimaryItem(ViewGroup container, int position,
					Object object) {
				Fragment frag;
				if(position == mCardList.size()) {
					frag = (TestFinishedFragment) object;
				} else {
					frag = (TestFragment) object;
					
					mAnswerInput = ((TestFragment) frag).getAnswerInput();
					mCurrentPage = position;
					mCurrentFragment = frag;
					updateKeyboard();
					
					Card card = mCardList.get(position);	
					card.setViewed(true);
					
					int i = 0;
					do {
						isViewed = mCardList.get(i).isViewed();
						i++;
					} while(isViewed && i < mCardList.size());
					
					mSubmitButton.setEnabled(isViewed);
				}
				View v = frag.getView();
				if(v != null)
					v.invalidate();

				super.setPrimaryItem(container, position, object);
			}

			@Override
			public int getCount() {
				return mCardList.size() + 1;
			}
			
		});
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int state) {}
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				if(position == mCardList.size()) {
					increment.setVisibility(View.INVISIBLE);
					decrement.setVisibility(View.INVISIBLE);
					hideKeyboard(true);
				}
			}
			@Override
			public void onPageSelected(int position) {
				if(position == mCardList.size()) {
					hideKeyboard(true);
					mTestManager.setTestFinished(true);
					int numUnanswered = 0;
					for(int i = 0; i < mCardList.size(); i++) {
						if(mCardList.get(i).getStudentAnswer().equals("")) {
							numUnanswered++;
							if(numUnanswered == 1) 
								mTestFinishedFragment.setFirstUnanswered(i);
						}
					}
					mTestFinishedFragment.setNumUnanswered(numUnanswered);
				} else {
				    if(position == 0) {
				        decrement.setVisibility(View.INVISIBLE);
			        } else {
			       		decrement.setVisibility(View.VISIBLE);
			        }
			       
			        if(position == mCardList.size()) {
			    	   increment.setVisibility(View.INVISIBLE);
			        } else {
			    	    increment.setVisibility(View.VISIBLE);
			        }
					
					hideKeyboard(false);
				}
			}
		});
		
		if(!mTestManager.getTime().equals("-")) {
			mTimer = Timer.newInstance(this);
			mTimer.getLooper();
			mTimer.startTimerThread(Timer.getMilliSeconds(mTestManager.getTime()), 500);
		}
	
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar_layout);
		  
		View v = getActionBar().getCustomView();
		
		mTimerTextView = (TextView) v.findViewById(R.id.timerText);
		
		mSubmitButton = (Button) v.findViewById(R.id.submitButton);
		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startResultsActivity();
			}
		});
		
		TextView quit = (TextView) v.findViewById(R.id.quitText);
		quit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fm = getSupportFragmentManager();
				QuitDialogFragment dialog = new QuitDialogFragment();
				dialog.setTargetFragment(mCurrentFragment, 0);
				dialog.show(fm, null);
			}
		});
	}
	
	public void setCurrentTime(String currentTime, boolean isLate) {
		mTimerTextView.setText(currentTime);
		if(isLate)										//With ten seconds left, text turns red
			mTimerTextView.setTextColor(Color.RED);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(!isSaved) 
			saveTest(true);
	}
	
	
	private void hideKeyboard(boolean isHidden) {
		TableLayout tl = (TableLayout) findViewById(R.id.fragment_keyboard_tableLayout);
		Button delete = (Button) findViewById(R.id.deleteButton);
		Button zero = (Button) findViewById(R.id.zeroButton);
		Button enter = (Button) findViewById(R.id.enterButton);
		if(isHidden) {
			tl.setVisibility(TableLayout.INVISIBLE);
			delete.setVisibility(Button.INVISIBLE);
			zero.setVisibility(Button.INVISIBLE);
			enter.setVisibility(Button.INVISIBLE);
		} else if(!isHidden) {
			tl.setVisibility(TableLayout.VISIBLE);
			delete.setVisibility(Button.VISIBLE);
			zero.setVisibility(Button.VISIBLE);
			enter.setVisibility(Button.VISIBLE);
		}
		
	}
	
	private void saveTest(boolean quitTest) {
		boolean storeDropbox = PreferenceManager
				.getDefaultSharedPreferences(this)
				.getBoolean(SettingsActivity.DROPBOX_STORAGE, false);

		if(!mTestManager.getTime().equals("-")) {							//If timer is turned off, the time string is set to "-"
			String timeLeft = mTimerTextView.getText().toString();
			mTestManager.setTimeLeft(timeLeft);
		} else {
			mTestManager.setTimeLeft("-");
		}
		
		mId = mTestManager.saveResults(quitTest);
		
		if(storeDropbox)
			DropboxUploader.getInstance().uploadResults(mId, getApplicationContext());
		
		if(mTimer != null)
			mTimer.finish();
		
		mTestManager.release();												//After saving test, the instance of TestManager is set to null
		
		isSaved = true;
	}
	
	/**
	 * Implementation of custom keyboard
	 */
	public void updateKeyboard() {
		View.OnClickListener numberButtonListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				String working = mAnswerInput.getText().toString();
				String text = textView.getText().toString();
				if(working.length() == 3)
					return;
				if(working.equals("")) {
					mAnswerInput.setText(text);
				} else {
					mAnswerInput.setText(working + text);
				}
			}
		};
		
		TableLayout tableLayout = (TableLayout) findViewById(R.id.fragment_keyboard_tableLayout);
		int number = 9;
		for(int i = 0; i < tableLayout.getChildCount(); i++) {
			TableRow row = (TableRow) tableLayout.getChildAt(i);
			for(int j = 2; j >= 0; j--) {
				Button button = (Button) row.getChildAt(j);
				button.setText("" + number);
				button.setOnClickListener(numberButtonListener);
				number--;
			}
		}
		
		Button deleteButton = (Button) findViewById(R.id.deleteButton);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String working = mAnswerInput.getText().toString();
				if(working.length() == 3) {
					mAnswerInput.setText("" + working.charAt(0) + working.charAt(1));
				} else if(working.length() == 2) {
					mAnswerInput.setText("" + working.charAt(0));
				} else {
					mAnswerInput.setText("");
				}
			}
		});
		
		Button zeroButton = (Button) findViewById(R.id.zeroButton);
		zeroButton.setOnClickListener(numberButtonListener);
		
		Button enterButton = (Button) findViewById(R.id.enterButton);
		enterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(mCurrentPage + 1, true);
			}
		});
	}
	
	/**
	 * User must confirm before quitting test 
	 * 
	 */
	@Override
	public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		QuitDialogFragment dialog = new QuitDialogFragment();
		dialog.setTargetFragment(mCurrentFragment, 0);
		dialog.show(fm, null);
	}
	
	public void quitTest() {
		saveTest(true);
		Intent i = new Intent(this, HomeActivity.class);
		startActivity(i);
		finish();
	}
	
	/**
	 * This method is called when the timer hits 0 or if they hit submit button
	 */
	public void startResultsActivity() {
		  saveTest(false);									//False because user did not quit test
		  
		  Intent i = new Intent(TestActivity.this, TestResultActivity.class);
		  i.putExtra(TestResultFragment.TEST_ID, mId);
		  i.putExtra(TestResultFragment.IS_FROM_TEST, true);
		  startActivity(i);
		  
		  finish();
	}
	
	public TestManager getTestManager() {
		return mTestManager;
	}
}
