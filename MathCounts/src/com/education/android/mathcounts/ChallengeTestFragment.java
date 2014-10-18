package com.education.android.mathcounts;

import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ChallengeTestFragment extends Fragment {
	private int mChoice;
	private boolean isCorrect;
	
	private int[][] mFourQuestions;
	private String[][] mSixQuestions;
	private String[] mSingleStringArray;
	private int[][] mDoubleIntArray;
	
	private ImageView mChallengePallet;
	private ImageView mIcon;
	private TextView mNumber;
	private TextView mQuestion;
	private TextView mExponent;
	private EditText mAnswerInput;
	
	private int mLastNum;
	
	private int mCurrentQuestion = 1;
	private String mAnswer;
	
	Random rand = new Random();
	
	TimerStopwatch timer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getActivity().getIntent();
		mChoice = intent.getIntExtra(ChallengeActivity.CHALLENGE_CHOICE, 0); 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.challenge_test_pallet, container, false);
		
		mChallengePallet = (ImageView) v.findViewById(R.id.challenge_pallet);
		mIcon = (ImageView) v.findViewById(R.id.correct_icon);
		mNumber = (TextView) v.findViewById(R.id.challenge_number);
		mNumber.setText("#" + (mCurrentQuestion + 1));
		mQuestion = (TextView) v.findViewById(R.id.challenge_question);
		mExponent = (TextView) v.findViewById(R.id.challenge_exponent);
		mAnswerInput = (EditText) v.findViewById(R.id.challenge_answer);
		
		mAnswerInput.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		switch(mChoice) {
			case ChallengeActivity.TEN_QUESTION_CHALLENGE:
				setTenQuestions();
				if(mCurrentQuestion < mFourQuestions.length) {
					String operation = "";
					switch(mCurrentQuestion) {
					case 0:	
						operation = " + ";
						break;
					case 1: 
						operation = " - ";
						break;
					case 2:
						operation = " x ";
						break;
					case 3: 
						operation = " / ";
						break;
					}
					mQuestion.setText(mFourQuestions[mCurrentQuestion][0] + operation + mFourQuestions[mCurrentQuestion][1] + " =");
					mAnswer = Integer.toString(mFourQuestions[mCurrentQuestion][2]);
				} else if(mCurrentQuestion < mFourQuestions.length + mSixQuestions.length) {
					if(mCurrentQuestion == 9) {
						ImageView squareRoot = (ImageView) v.findViewById(R.id.square_root);
						squareRoot.setVisibility(View.VISIBLE);
						mQuestion.setText(mSixQuestions[mCurrentQuestion - mFourQuestions.length][0] + "");
					} else {
						mQuestion.setText(mSixQuestions[mCurrentQuestion - mFourQuestions.length][0] + " =");
					}
					mAnswer = mSixQuestions[mCurrentQuestion - mFourQuestions.length][1];
				}
				break;
			case ChallengeActivity.MULTIPLICATION_MADNESS:
				if(rand.nextBoolean()) {
					mSingleStringArray = NumberGenerator.getMultiplicationMadness();
					mQuestion.setText(mSingleStringArray[0]);
					mAnswer = mSingleStringArray[1];
				} else {
					mDoubleIntArray = NumberGenerator.getChallengeMultiplication(1);
					mQuestion.setText(mDoubleIntArray[0][0] + " x " + mDoubleIntArray[0][1] + " =");
					mAnswer = Integer.toString(mDoubleIntArray[0][2]);
				}
				break;
			case ChallengeActivity.SQUARES_CHALLENGE:
				int[] squares;
				if(rand.nextBoolean()) {
					do {
						squares = NumberGenerator.getSquare();
					} while(squares[0] == mLastNum);
					mLastNum = squares[0];
				
					mQuestion.setText("" + squares[0]);
					mExponent.setVisibility(View.VISIBLE);
				} else {
					do {
						squares = NumberGenerator.getSquareRoot();
					} while(squares[0] == mLastNum);
					mLastNum = squares[0];
					
					mQuestion.setText(squares[0] + "");
					ImageView squareRoot = (ImageView) v.findViewById(R.id.square_root);
					squareRoot.setVisibility(View.VISIBLE);
				}

				
				mAnswer = Integer.toString(squares[1]);
				break;
			case ChallengeActivity.PERCENTAGES_CHALLENGE:
				int[] percent = NumberGenerator.getPercentage();
				
				mQuestion.setText(percent[1] + "% " + getResources().getString(R.string.of) + " " + percent[0] + " =");
				mAnswer = Integer.toString(percent[2]);
				break;
			case ChallengeActivity.TWO_MINUTE_DRILL:
				int[] drill = NumberGenerator.getTwoMinuteDrill(mCurrentQuestion);
				String operation = null;
				switch(mCurrentQuestion % 4) {
				case 0: operation = "+";
					break;
				case 1: operation = "-";
					break;
				case 2: operation = "x";
					break;
				case 3: operation = "/";
					break;
				}
				mQuestion.setText(drill[0] + " " + operation + " " + drill[1] + " =");
				mAnswer = Integer.toString(drill[2]);
				break;
		}
		
		return v;
	}
	
	@Override
	public void onDetach() {
		onDestroy();
	}
	
	public void setTenQuestions() {
		mFourQuestions = NumberGenerator.getOperationsChallengeMode();
		setSixQuestions();
	}

	public void setSixQuestions() {
		mSixQuestions = NumberGenerator.getOrderChallengeMode();
	}
	
	public EditText getAnswerInput() {
		return mAnswerInput;
	}
	
	public String getAnswer() {
		return mAnswer;
	}
	
	public ImageView getChallengePallet() {
		return mChallengePallet;
	}
	
	public void setCurrentQuestion(int position) {
		mCurrentQuestion = position;
	}
	
	public ImageView getIcon() {
		return mIcon;
	}
}