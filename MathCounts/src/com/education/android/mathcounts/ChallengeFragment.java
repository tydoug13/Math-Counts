package com.education.android.mathcounts;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ChallengeFragment extends Fragment {
	
	private TextView mChallengeTitle;
	private TextView mChallengeDescription;
	
	private TextView mHighScoreOne;
	private TextView mHighScoreTwo;
	private TextView mHighScoreThree;
	
	private int mPosition;
	
	public void setPosition(int position) {
		mPosition = position;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_game_card, container, false);

		mChallengeTitle = (TextView) v.findViewById(R.id.challengeTitle);
		mChallengeDescription = (TextView) v.findViewById(R.id.challengeDescription);
		
		switch(mPosition) {
		case 0: 
			mChallengeTitle.setText(getResources().getString(R.string.squares_challenge));
			mChallengeDescription.setText(getResources().getString(R.string.squares_description) + '\n' + '\n' + getResources().getString(R.string.time_colon) + " " + getResources().getString(R.string.one_minute));
			break;
		case 1:
			mChallengeTitle.setText(getResources().getString(R.string. percentages_challenge));
			mChallengeDescription.setText(getResources().getString(R.string.percentages_desciption) + '\n' + '\n' + getResources().getString(R.string.time_colon) + " " + getResources().getString(R.string.one_minute));
			break;
		case 2: 
			mChallengeTitle.setText(getResources().getString(R.string.ten_question_challenge));
			mChallengeDescription.setText(getResources().getString(R.string.ten_questions_description) + '\n' + '\n' + getResources().getString(R.string.time_colon) + " " + getResources().getString(R.string.unlimited));
			break;
		case 3:
			mChallengeTitle.setText(getResources().getString(R.string.multiplication_madness));
			mChallengeDescription.setText(getResources().getString(R.string.multiplication_madness_description) + '\n' + '\n' + getResources().getString(R.string.time_colon) + " " + getResources().getString(R.string.one_minute));
			break;
		case 4: 
			mChallengeTitle.setText(getResources().getString(R.string.two_minute_drill));
			mChallengeDescription.setText(getResources().getString(R.string.two_minute_challenge_description) + '\n' + '\n' + getResources().getString(R.string.time_colon) + " " + getResources().getString(R.string.two_minutes));
			break;
		}
		
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		mHighScoreOne = (TextView) v.findViewById(R.id.highScoreOne);
		mHighScoreTwo = (TextView) v.findViewById(R.id.highScoreTwo);
		mHighScoreThree = (TextView) v.findViewById(R.id.highScoreThree);
		
		String scoreTextOne = "1. " + "\u2014" + "\u2014";
		String scoreTextTwo = "2. " + "\u2014" + "\u2014";
		String scoreTextThree = "3. " + "\u2014" + "\u2014";
		
		Cursor cursor = TestManager.getHighScores(mPosition, getActivity());
		
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
		
		if(nameOne.equals("") && scoreOne.equals("")) {
			nameOne = "1.";
			scoreTextOne = nameOne + "  \u2014" + "\u2014";
		} else if(nameOne.equals("") && !scoreOne.equals("")) {
			nameOne = "1. ";
			scoreTextOne = nameOne + scoreOne;
		} else if(!nameOne.equals("") && !scoreOne.equals("")) {
			scoreTextOne = "1. " + nameOne + "     " + scoreOne;
		}
		
		if(nameTwo.equals("") && scoreTwo.equals("")) {
			nameTwo = "2.";
			scoreTextTwo = nameTwo + "  \u2014" + "\u2014";
		} else if(nameTwo.equals("") && !scoreTwo.equals("")) {
			nameTwo = "2. ";
			scoreTextTwo = nameTwo + scoreTwo;
		} else if(!nameTwo.equals("") && !scoreTwo.equals("")) {
			scoreTextTwo = "2. " + nameTwo + "     " + scoreTwo;
		}
		
		if(nameThree.equals("") && scoreThree.equals("")) {
			nameThree = "3.";
			scoreTextThree = nameThree + "  \u2014" + "\u2014";
		} else if(nameThree.equals("") && !scoreThree.equals("")) {
			nameThree = "3. ";
			scoreTextThree = nameThree + scoreThree;
		} else if(!nameThree.equals("") && !scoreThree.equals("")) {
			scoreTextThree = "3. " + nameThree + "     " + scoreThree;
		}
		
		mHighScoreOne.setText(scoreTextOne);
		mHighScoreTwo.setText(scoreTextTwo);
		mHighScoreThree.setText(scoreTextThree);
		
		return v;
	}

}
