package com.education.android.mathcounts;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChallengeResultsActivity extends Activity {
	public static final int SCORE_MULTIPLIER = 1000;
	
	private String mTime;
	private int mNumberCorrect;
	private int mChoice;
	private int mScore;
	private String mName;
	
	TextView score, highScoreOne, highScoreTwo, highScoreThree;
	EditText name;
	Button tryAgainButton;
	
	boolean isSaved;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent i = getIntent();
		if(i.hasExtra(ChallengeTestActivity.TIME_USED))
			mTime = i.getStringExtra(ChallengeTestActivity.TIME_USED);
		
		mNumberCorrect = i.getIntExtra(ChallengeTestActivity.NUMBER_CORRECT, 0);
		mChoice = i.getIntExtra(ChallengeTestActivity.TEST_MODE, 0);

		if(mChoice == ChallengeActivity.TEN_QUESTION_CHALLENGE) {
			int seconds = (int) Timer.getMilliSeconds(mTime) / 1000;
			mScore = (mNumberCorrect * SCORE_MULTIPLIER) - (seconds * 20);
		}
		
		if(i.hasExtra(ChallengeTestActivity.SCORE))
			mScore = i.getIntExtra(ChallengeTestActivity.SCORE, -1);
		
		if(mScore < 0)
			mScore = 0;
		
		if(shouldInsertScore())
			populateHighScoreTextViews();
		else
			populateTextViews();
		
		mName = "";
		

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.result_actionbar);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_main));
		View v = getActionBar().getCustomView();
		TextView homeText = (TextView) v.findViewById(R.id.menu_name); 
		homeText.setText(getResources().getString(R.string.challenge_mode));
		homeText.setTextSize(18);
		homeText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(shouldInsertScore()) {
					TestManager.insertHighScore(mChoice, mScore, mName, getApplicationContext());
					isSaved = true;
				}
				
				Intent i = new Intent(getApplicationContext(), ChallengeActivity.class);
				startActivity(i);
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(shouldInsertScore() && !isSaved) {
			TestManager.insertHighScore(mChoice, mScore, mName, getApplicationContext());
			isSaved = true;
		}
	}

	public boolean shouldInsertScore() {
		return TestManager.shouldInsertScore(mChoice, mScore, getApplicationContext());
	}
	
	private void populateHighScoreTextViews() {
		setContentView(R.layout.challenge_results_high_score_activity);
		
		score = (TextView) findViewById(R.id.challenge_high_score);
		score.setText("" + mScore);
		
		name = (EditText) findViewById(R.id.high_score_name);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		name.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mName = name.getText().toString();
				if(mName == null)
					mName = "";
			}
		});
		
		tryAgainButton = (Button) findViewById(R.id.tryAgainHSButton);
		tryAgainButton.setText(getResources().getString(R.string.submit));
		tryAgainButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(shouldInsertScore()) {
					TestManager.insertHighScore(mChoice, mScore, mName, getApplicationContext());
					isSaved = true;
				}
				
				Intent i = new Intent(getApplicationContext(), ChallengeActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
	
	private void populateTextViews() {
		setContentView(R.layout.challenge_results_activity);
		
		tryAgainButton = (Button) findViewById(R.id.tryAgainButton);
		tryAgainButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), ChallengeTestActivity.class);
				i.putExtra(ChallengeActivity.CHALLENGE_CHOICE, mChoice);
				startActivity(i);
				finish();
			}
		});
		
		score = (TextView) findViewById(R.id.challenge_score);
		score.setText("" + mScore);

		highScoreOne = (TextView) findViewById(R.id.highScoreOne);
		highScoreTwo = (TextView) findViewById(R.id.highScoreTwo);
		highScoreThree = (TextView) findViewById(R.id.highScoreThree);
		
		String scoreTextOne = "1.";
		String scoreTextTwo = "2.";
		String scoreTextThree = "3.";
		
		Cursor cursor = TestManager.getHighScores(mChoice, getApplicationContext());
		
		String nameOne = "";
		String scoreOne = "";
		String nameTwo = "";
		String scoreTwo = "";
		String nameThree = "";
		String scoreThree = "";
		
		if(cursor.getCount() != 0)
			cursor.moveToFirst();
		
		if(!cursor.isAfterLast()) {
			nameOne = cursor.getString(cursor.getColumnIndex("name"));
			scoreOne = "" + cursor.getInt(cursor.getColumnIndex("score"));
			cursor.moveToNext();
		}
		if(!cursor.isAfterLast()) {
			nameTwo = cursor.getString(cursor.getColumnIndex("name"));
			scoreTwo = "" + cursor.getInt(cursor.getColumnIndex("score"));
			cursor.moveToNext();
		}
		if(!cursor.isAfterLast()) {
			nameThree = cursor.getString(cursor.getColumnIndex("name"));
			scoreThree = "" + cursor.getInt(cursor.getColumnIndex("score"));
			cursor.moveToNext();
		}
		
		if(!nameOne.equals("")) {
			scoreTextOne = nameOne + "  "  + scoreOne;
		} else {
			scoreTextOne = scoreOne;
		}
		if(!nameTwo.equals("")) {
			scoreTextTwo = nameTwo + "  "  + scoreTwo;
		} else {
			scoreTextTwo = scoreTwo;
		}
		if(!nameThree.equals("")) {
			scoreTextThree = nameThree + "  "  + scoreThree;
		} else {
			scoreTextThree = scoreThree;
		}
		
		highScoreOne.setText(scoreTextOne);
		highScoreTwo.setText(scoreTextTwo);
		highScoreThree.setText(scoreTextThree);
		
		highScoreOne.setTextColor(Color.rgb(212, 175, 55));
		highScoreTwo.setTextColor(Color.rgb(192, 192, 192));
		highScoreThree.setTextColor(Color.rgb(205, 127, 50));
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, ChallengeActivity.class));
		finish();
		
		super.onBackPressed();
	}
	
}
