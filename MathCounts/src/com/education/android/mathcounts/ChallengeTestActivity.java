package com.education.android.mathcounts;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ChallengeTestActivity extends FragmentActivity {
	
	
	public static final String NUMBER_CORRECT = "number_correct";
	public static final String TIME_USED = "time_used";
	public static final String TEST_MODE = "test_mode";
	public static final String SCORE = "score";
	
	public static final int ANIMATION_TIME = 250;
	public static final int ONE_MINUTE = 60000;
	public static final int TWO_MINUTES = 120000;
	
	private HomePager mViewPager;
	private Fragment mCurrentFragment;
	private EditText mAnswerInput;
	private ImageView mChallengePallet;
	private ImageView mIcon;
	private TextView mQuit;
	private TextView mTime;
	private TextView mScoreView;
	private Button submitButton;
	
	private TimerStopwatch mTimerStopwatch;
	private TimerChallenge mTimerAnimation;
	private TimerChallenge mTimerChallenge;
	
	private ImageView boxOne, boxTwo, boxThree, boxFour, boxFive, boxSix, boxSeven, boxEight, boxNine, boxTen;
	
	private int mCurrentQuestion;
	private int mNumberCorrect;
	private int mChoice;
	private int mCount;
	private String mTimeUsed;
	private String mAnswer;
	private String mCurrentTime;
	private int mScore;
	
	private Fragment[] mFragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_challenge_test);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Intent intent = getIntent();
		mChoice = intent.getIntExtra(ChallengeActivity.CHALLENGE_CHOICE, 0);
		
		if(mChoice == ChallengeActivity.TEN_QUESTION_CHALLENGE)
			mCount = 10;
		else 
			mCount = 250;
		
		mFragments = new Fragment[mCount];
		
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
		
		View v;
		
		switch(mChoice) {
		case ChallengeActivity.TEN_QUESTION_CHALLENGE: 
			getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			getActionBar().setCustomView(R.layout.challenge_mode_ten_questions);
			v = getActionBar().getCustomView();
					
			mQuit = (TextView) v.findViewById(R.id.challengeTen_quitText);
			mQuit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FragmentManager fm = getSupportFragmentManager();
					QuitDialogFragment dialog = new QuitDialogFragment();
					dialog.setKey(QuitDialogFragment.FROM_CHALLENGE);
					dialog.setTargetFragment(mCurrentFragment, 0);
					dialog.show(fm, null);
				}
			});
			
			boxOne = (ImageView) v.findViewById(R.id.challengeTen_boxOne);
			boxTwo = (ImageView) v.findViewById(R.id.challengeTen_boxTwo);
			boxThree = (ImageView) v.findViewById(R.id.challengeTen_boxThree);
			boxFour = (ImageView) v.findViewById(R.id.challengeTen_boxFour);
			boxFive = (ImageView) v.findViewById(R.id.challengeTen_boxFive);
			boxSix = (ImageView) v.findViewById(R.id.challengeTen_boxSix);
			boxSeven = (ImageView) v.findViewById(R.id.challengeTen_boxSeven);
			boxEight = (ImageView) v.findViewById(R.id.challengeTen_boxEight);
			boxNine = (ImageView) v.findViewById(R.id.challengeTen_boxNine);
			boxTen = (ImageView) v.findViewById(R.id.challengeTen_boxTen);
			mTime =  (TextView) v.findViewById(R.id.challengeTen_timerText);
			mTimerStopwatch = TimerStopwatch.newInstance(this);
			mTimerStopwatch.getLooper();
			mTimerStopwatch.startTimerThread();
			break;
		case ChallengeActivity.SQUARES_CHALLENGE:
			getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			getActionBar().setCustomView(R.layout.challenge_mode_squares);
			v = getActionBar().getCustomView();
			mScoreView = (TextView) v.findViewById(R.id.challenge_score_keeper); 
			mTime = (TextView) v.findViewById(R.id.challengeSquares_timerText);
			
			mTimerChallenge = new TimerChallenge(this);
			mTimerChallenge.getLooper();
			mTimerChallenge.startTimerThread(ONE_MINUTE, 500);
			
			mQuit = (TextView) v.findViewById(R.id.challengeSquares_quitText);
			mQuit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FragmentManager fm = getSupportFragmentManager();
					QuitDialogFragment dialog = new QuitDialogFragment();
					dialog.setKey(QuitDialogFragment.FROM_CHALLENGE);
					dialog.setTargetFragment(mCurrentFragment, 0);
					dialog.show(fm, null);
				}
			});
			break;
		case ChallengeActivity.PERCENTAGES_CHALLENGE:
			getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			getActionBar().setCustomView(R.layout.challenge_mode_squares);
			v = getActionBar().getCustomView();
			mScoreView = (TextView) v.findViewById(R.id.challenge_score_keeper); 
			mTime = (TextView) v.findViewById(R.id.challengeSquares_timerText);
			
			mTimerChallenge = new TimerChallenge(this);
			mTimerChallenge.getLooper();
			mTimerChallenge.startTimerThread(ONE_MINUTE, 500);
			
			mQuit = (TextView) v.findViewById(R.id.challengeSquares_quitText);
			mQuit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FragmentManager fm = getSupportFragmentManager();
					QuitDialogFragment dialog = new QuitDialogFragment();
					dialog.setKey(QuitDialogFragment.FROM_CHALLENGE);
					dialog.setTargetFragment(mCurrentFragment, 0);
					dialog.show(fm, null);
				}
			});
			break;
		case ChallengeActivity.TWO_MINUTE_DRILL:
			getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			getActionBar().setCustomView(R.layout.challenge_mode_squares);
			v = getActionBar().getCustomView();
			mScoreView = (TextView) v.findViewById(R.id.challenge_score_keeper); 
			mTime = (TextView) v.findViewById(R.id.challengeSquares_timerText);
			
			mTimerChallenge = new TimerChallenge(this);
			mTimerChallenge.getLooper();
			mTimerChallenge.startTimerThread(TWO_MINUTES, 500);
			
			mQuit = (TextView) v.findViewById(R.id.challengeSquares_quitText);
			mQuit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FragmentManager fm = getSupportFragmentManager();
					QuitDialogFragment dialog = new QuitDialogFragment();
					dialog.setKey(QuitDialogFragment.FROM_CHALLENGE);
					dialog.setTargetFragment(mCurrentFragment, 0);
					dialog.show(fm, null);
				}
			});
			break;
		case ChallengeActivity.MULTIPLICATION_MADNESS:
			getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			getActionBar().setCustomView(R.layout.challenge_mode_squares);
			v = getActionBar().getCustomView();
			mScoreView = (TextView) v.findViewById(R.id.challenge_score_keeper); 
			mTime = (TextView) v.findViewById(R.id.challengeSquares_timerText);
			
			mTimerChallenge = new TimerChallenge(this);
			mTimerChallenge.getLooper();
			mTimerChallenge.startTimerThread(ONE_MINUTE, 500);
			
			mQuit = (TextView) v.findViewById(R.id.challengeSquares_quitText);
			mQuit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FragmentManager fm = getSupportFragmentManager();
					QuitDialogFragment dialog = new QuitDialogFragment();
					dialog.setKey(QuitDialogFragment.FROM_CHALLENGE);
					dialog.setTargetFragment(mCurrentFragment, 0);
					dialog.show(fm, null);
				}
			});
			break;
		}
		
		FragmentManager fm = getSupportFragmentManager();
		
		mViewPager = (HomePager) findViewById(R.id.challengeViewPager);
		mViewPager.setPagingEnabled(false);
		
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public Fragment getItem(int pos) {
				ChallengeTestFragment fragment = new ChallengeTestFragment();
				fragment.setCurrentQuestion(pos);
				
				mFragments[pos] = fragment;
				
				return fragment;
			}

			@Override
			public int getCount() {
				return mCount;
			}

			@Override
			public void setPrimaryItem(ViewGroup container, int position,
					Object object) {
				ChallengeTestFragment frag = (ChallengeTestFragment) object;
				mCurrentFragment = frag;
				mCurrentQuestion = position;
				mIcon = frag.getIcon();
				mChallengePallet = frag.getChallengePallet();
				mAnswerInput = frag.getAnswerInput();
				mAnswer = frag.getAnswer();
				super.setPrimaryItem(container, position, object);
			}
			
	
		});
		
		mScore = 0;
		if(mChoice != ChallengeActivity.TEN_QUESTION_CHALLENGE)
			mScoreView.setText("" + 0);
		updateKeyboard();
	}

	public void updateKeyboard() {
		View.OnClickListener numberButtonListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView textView = (TextView) v;
				String working = mAnswerInput.getText().toString();
				String text = textView.getText().toString();
				if(working.length() == 4)
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
				if(working.length() == 4) {
					mAnswerInput.setText("" + working.charAt(0) + working.charAt(1) + working.charAt(2));
				} else if(working.length() == 3) {
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
		
		mNumberCorrect = 0;
		  
		  submitButton = (Button) findViewById(R.id.sushiButton);
		  submitButton.setOnClickListener(new View.OnClickListener() {
		   
		   @Override
		   public void onClick(View v) {
			  v.setEnabled(false);
			   mChallengePallet.setImageDrawable(getResources().getDrawable(R.drawable.challenge_pallet));
			    try {
				     if(mAnswer.equals(mAnswerInput.getText().toString())) {
					      if(mChoice == ChallengeActivity.TEN_QUESTION_CHALLENGE)
					    	  setCorrectIcon();
					      mChallengePallet.setBackgroundColor(Color.rgb(34, 177, 76));
					      mIcon.setImageResource(R.drawable.btn_check_buttonless_on);
					      mNumberCorrect++;
					      mScore += 1000;
					      if(mChoice != ChallengeActivity.TEN_QUESTION_CHALLENGE)
					    	  mScoreView.setText("" + mScore);
				     } else {
					      if(mChoice == ChallengeActivity.TEN_QUESTION_CHALLENGE)
					    	  setIncorrectIcon();
					      mChallengePallet.setBackgroundColor(Color.rgb(255, 0, 0));
					      mIcon.setImageResource(android.R.drawable.ic_delete);
					      mScore = mScore - 250;
					      if(mScore < 0)
					    	  mScore = 0;
					      if(mChoice != ChallengeActivity.TEN_QUESTION_CHALLENGE)
					    	  mScoreView.setText("" + mScore);
				     }
			    } catch(Exception e) {
				     if(mChoice == ChallengeActivity.TEN_QUESTION_CHALLENGE)
					     	setIncorrectIcon();
					     mChallengePallet.setBackgroundColor(Color.rgb(255, 0, 0));
					     mIcon.setImageResource(android.R.drawable.ic_delete);
				    }
			    	mTimerAnimation = TimerChallenge.newInstance(ChallengeTestActivity.this);
			    	mTimerAnimation.getLooper();
			    	mTimerAnimation.startTimerThread(ANIMATION_TIME, 50);
				    mChallengePallet.setPadding(15, 15, 15, 15);
				    
				    if(mChoice == ChallengeActivity.TEN_QUESTION_CHALLENGE) {
					     if(mCurrentQuestion == 9) {
					      finishTest();
					      Intent i = new Intent(getApplicationContext(), ChallengeResultsActivity.class);
					      i.putExtra(NUMBER_CORRECT, mNumberCorrect);
					      i.putExtra(TIME_USED, mTime.getText().toString());
					      i.putExtra(TEST_MODE, mChoice);
					      startActivity(i);
					      finish();
				     }
			    }
			    
			   }
		   
		   
		  });
		
	}
	
	@Override
	public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		QuitDialogFragment dialog = new QuitDialogFragment();
		dialog.setKey(QuitDialogFragment.FROM_CHALLENGE);
		dialog.setTargetFragment(mCurrentFragment, 0);
		dialog.show(fm, null);
	}
	
	public void quitTest() {
		Intent i = new Intent(ChallengeTestActivity.this, ChallengeActivity.class);
		finishTest();
		startActivity(i);
		finish();
	}
		
	private void finishTest() {
		if(mTimerStopwatch != null)
			mTimerStopwatch.finish();
		if(mTimerChallenge != null)
			mTimerChallenge.finish();
	}
	
	private void setCorrectIcon() {
		switch(mCurrentQuestion + 1) {
			case 1:
				boxOne.setImageResource(R.drawable.btn_check_buttonless_on);
				return;
			case 2:
				boxTwo.setImageResource(R.drawable.btn_check_buttonless_on);
				return;
			case 3: 
				boxThree.setImageResource(R.drawable.btn_check_buttonless_on);
				return;
			case 4:
				boxFour.setImageResource(R.drawable.btn_check_buttonless_on);
				return;
			case 5: 
				boxFive.setImageResource(R.drawable.btn_check_buttonless_on);
				return;
			case 6: 
				boxSix.setImageResource(R.drawable.btn_check_buttonless_on);
				return;
			case 7: 
				boxSeven.setImageResource(R.drawable.btn_check_buttonless_on);
				return;
			case 8: 
				boxEight.setImageResource(R.drawable.btn_check_buttonless_on);
				return;
			case 9:
				boxNine.setImageResource(R.drawable.btn_check_buttonless_on);
				return;
			case 10:
				boxTen.setImageResource(R.drawable.btn_check_buttonless_on);
				return;
			default:
				return;
		}
	}
	
	private void setIncorrectIcon() {
		switch(mCurrentQuestion + 1) {
			case 1:
				boxOne.setImageResource(android.R.drawable.ic_delete);
				return;
			case 2:
				boxTwo.setImageResource(android.R.drawable.ic_delete);
				return;
			case 3: 
				boxThree.setImageResource(android.R.drawable.ic_delete);
				return;
			case 4:
				boxFour.setImageResource(android.R.drawable.ic_delete);
				return;
			case 5: 
				boxFive.setImageResource(android.R.drawable.ic_delete);
				return;
			case 6: 
				boxSix.setImageResource(android.R.drawable.ic_delete);
				return;
			case 7: 
				boxSeven.setImageResource(android.R.drawable.ic_delete);
				return;
			case 8: 
				boxEight.setImageResource(android.R.drawable.ic_delete);
				return;
			case 9:
				boxNine.setImageResource(android.R.drawable.ic_delete);
				return;
			case 10:
				boxTen.setImageResource(android.R.drawable.ic_delete);
				return;
			default:
				return;
		}
	}

	public void startResultsActivity() {
		Intent i = new Intent(this, ChallengeResultsActivity.class);
		i.putExtra(SCORE, mScore);
		i.putExtra(TEST_MODE, mChoice);
		startActivity(i);
		finish();
	}

	public void setCurrentTime(String currentTime, boolean b) {
		mTime.setText(currentTime);
		if(b)
			mTime.setTextColor(Color.RED);		
	}

	public void setCurrentTime(String time) {
		mTime.setText(time);
	}
	
	public void animationFinished() {
		mViewPager.setCurrentItem(mCurrentQuestion + 1);
    	submitButton.setEnabled(true);
	}

}
