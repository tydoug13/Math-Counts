package com.education.android.mathcounts;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class TestFragment extends Fragment {
	
	private String mFirstNum;
	private String mSecondNum;
	private String mOperation;
	private String mOperationSign;
	private int mQuestionNum;
	private Card mCard;
	private EditText answerBox;
	private EditText mAnswerInput;
	private View mAnswerFilter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mOperation = getArguments().getString(TestManager.OPERATION);
		mQuestionNum = getArguments().getInt(Card.QUESTION_NUM);
		mCard = ((TestActivity) getActivity()).getTestManager().getCard(mQuestionNum);
		
		mFirstNum = String.valueOf(mCard.getFirstNum());
		mSecondNum = String.valueOf(mCard.getSecondNum());
		
		
		if(mOperation.equals(TestManager.ADDITION))
			mOperationSign = "+";
		else if(mOperation.equals(TestManager.SUBTRACTION))
			mOperationSign = "-";
		else if(mOperation.equals(TestManager.MULTIPLICATION))
			mOperationSign = "x";
		else if(mOperation.equals(TestManager.DIVISION))
			mOperationSign = "\u00F7";
		setRetainInstance(true);
	}

	@TargetApi(16)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.card, container, false);
		
		
		ImageView card = (ImageView) v.findViewById(R.id.cardImage);
		card.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		
		float widthUnit  = (float) (card.getMeasuredWidth()/1000.0);
		float heightUnit = (float) (card.getMeasuredHeight()/1000.0);
		
		TextView firstNum = (TextView) v.findViewById(R.id.firstNum);
		firstNum.setText(mFirstNum);
		
		TextView secondNum = (TextView) v.findViewById(R.id.secondNum);
		secondNum.setText(mSecondNum);
		
		TextView questionNum = (TextView) v.findViewById(R.id.questionNum);
		questionNum.setText("#" + mQuestionNum);
		questionNum.setTranslationX(50*widthUnit);
		questionNum.setTranslationY(25*heightUnit);
		
		firstNum.setTranslationX(-140*widthUnit);
		firstNum.setTranslationY(100*heightUnit);
		firstNum.setTextColor(Color.BLACK);
		
		secondNum.setTranslationX(-140*widthUnit);
		secondNum.setTranslationY(75*heightUnit);
		secondNum.setTextColor(Color.BLACK);
		
		TextView operationText;
		if(mSecondNum.length() == 3) {
			operationText = (TextView) v.findViewById(R.id.operation);
			operationText.setText(mOperationSign);
			operationText.setTranslationX(60*widthUnit);
		} else {
			operationText = (TextView) v.findViewById(R.id.operation);
			operationText.setText(mOperationSign);
			operationText.setTranslationX(150*widthUnit);
		}
		
		if(mOperation.equals(TestManager.SUBTRACTION)) {
			operationText.setTranslationY(85*heightUnit);
			operationText.setTextSize(80);
		} else {
			operationText.setTranslationY(70*heightUnit);
		}
		
		answerBox = (EditText) v.findViewById(R.id.answerBox);
		if(answerBox.getText() == null) {
			answerBox.setText("");
		}
		answerBox.setWidth((int) (850*widthUnit));
		answerBox.setTranslationY(-20*heightUnit);
		
		mAnswerFilter = v.findViewById(R.id.answerBoxFilter);
		if(TestManager.getInstance() != null) {
			if(TestManager.getInstance().isTestFinished() && answerBox.getText().toString().equals("")) {
				mAnswerFilter.setVisibility(View.VISIBLE);
			} else {
				mAnswerFilter.setVisibility(View.INVISIBLE);
			}
		}
		answerBox.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(answerBox.getWindowToken(), 0);
				return true;
			}
			
		});
		answerBox.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(!arg1 || TestManager.getInstance() == null)
					return;
				
				if(TestManager.getInstance().isTestFinished() && answerBox.getText().toString().equals("")) {
					mAnswerFilter.setVisibility(View.VISIBLE);
				} else {
					mAnswerFilter.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		answerBox.addTextChangedListener(new TextWatcher() {
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
				if(!answerBox.getText().toString().equals("")) {
					mAnswerFilter.setVisibility(View.INVISIBLE);
				}
				mCard.setStudentAnswer(answerBox.getText().toString());
			}
		});
		
		answerBox.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if(arg1 == EditorInfo.IME_ACTION_DONE) {
					answerBox.setImeOptions(EditorInfo.IME_ACTION_NONE);
					return true;
				}
				return false;
			}
			
		});
		answerBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(answerBox.getWindowToken(), 0);
			}
		});
		mAnswerInput = answerBox;
		
		TextView separator = (TextView) v.findViewById(R.id.separator);
		separator.setTranslationY(70*heightUnit);
		separator.setWidth((int) (850*widthUnit));
		
		//Changes action bar color depending on operation
		int sdk = android.os.Build.VERSION.SDK_INT;
		if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			if(mOperation.equals(TestManager.ADDITION)) {
				getActivity().getActionBar()
					.getCustomView().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_red));
				v.setBackgroundColor(Color.rgb(255, 193, 193));
			} else if(mOperation.equals(TestManager.SUBTRACTION)) {
				getActivity().getActionBar()
					.getCustomView().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_blue));
				v.setBackgroundColor(Color.rgb(200, 235, 242));
			} else if(mOperation.equals(TestManager.MULTIPLICATION)) {
				getActivity().getActionBar()
					.getCustomView().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_green));
				v.setBackgroundColor(Color.rgb(204, 255, 204));
			} else if(mOperation.equals(TestManager.DIVISION)) {
				getActivity().getActionBar()
					.getCustomView().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_purple));
				v.setBackgroundColor(Color.rgb(233, 210, 255));
			}
		} else {
			if(mOperation.equals(TestManager.ADDITION)) {
				getActivity().getActionBar().getCustomView().setBackground(getResources().getDrawable(R.drawable.action_bar_red));
				v.setBackgroundColor(Color.rgb(255, 193, 193));
			} else if(mOperation.equals(TestManager.SUBTRACTION)) {
				getActivity().getActionBar().getCustomView().setBackground(getResources().getDrawable(R.drawable.action_bar_blue));
				v.setBackgroundColor(Color.rgb(200, 235, 242));
			} else if(mOperation.equals(TestManager.MULTIPLICATION)) {
				getActivity().getActionBar().getCustomView().setBackground(getResources().getDrawable(R.drawable.action_bar_green));
				v.setBackgroundColor(Color.rgb(204, 255, 204));
			} else if(mOperation.equals(TestManager.DIVISION)) {
				getActivity().getActionBar().getCustomView().setBackground(getResources().getDrawable(R.drawable.action_bar_purple));
				v.setBackgroundColor(Color.rgb(233, 210, 255));
			}
		}
		
		return v;
	}
	
	public static TestFragment newInstance(Card card, String operation) {
		TestFragment testFragment = new TestFragment();
		Bundle args = new Bundle();
		
		args.putInt(Card.QUESTION_NUM, card.getQuestionNum());
		args.putString(TestManager.OPERATION, operation);
		
		testFragment.setArguments(args);
		
		return testFragment;
	}
	
	public EditText getAnswerInput() {
		return mAnswerInput;
	}
	
	public View getFilter() {
		return mAnswerFilter;
	}
}
