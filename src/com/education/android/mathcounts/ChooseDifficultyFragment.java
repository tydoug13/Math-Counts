package com.education.android.mathcounts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseDifficultyFragment extends Fragment {

	private HomeActivity mActivity;
	private String mOperation;
	private String mDifficulty;
	
	private int mPosition;
	
	private String mOperationSign;
	private int mFirstNum;
	private int mSecondNum;
	private Button mNextButton;
	private TextView mLevelTextView;
	
	ImageView card;
	TextView firstNum;
	TextView secondNum;
	TextView separator;
	TextView operationText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mPosition = 0;
		
		mActivity = (HomeActivity) getActivity();
		mOperation = mActivity.getOperation();
		mDifficulty = getDifficulty();
		mActivity.setLevel(mDifficulty);
		
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		setHasOptionsMenu(true);
		
		View v = inflater.inflate(R.layout.fragment_choose_difficulty, null);
		mLevelTextView = (TextView) v.findViewById(R.id.chooseLevel);
		
		final TextView tvDifficulty = (TextView) v.findViewById(R.id.difficultyNum);
		tvDifficulty.setText(mDifficulty);
		
		ImageView increment = (ImageView) v.findViewById(R.id.increment);
		increment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPosition++;
				mDifficulty = getDifficulty(); 
				mActivity.setLevel(mDifficulty);
				
				tvDifficulty.setText(mDifficulty);
				setupExample(v);
			}
		});
		
		ImageView decrement = (ImageView) v.findViewById(R.id.decrement);
		decrement.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPosition--;
				mDifficulty = getDifficulty(); 
				mActivity.setLevel(mDifficulty);
				
				tvDifficulty.setText(mDifficulty);
				setupExample(v);
			}
		});
		
		card = (ImageView) v.findViewById(R.id.cardImage);
		firstNum = (TextView) v.findViewById(R.id.firstNum);
		secondNum = (TextView) v.findViewById(R.id.secondNum);
		separator = (TextView) v.findViewById(R.id.separator);
		operationText = (TextView) v.findViewById(R.id.operation);
		
		setupExample(v);
		
		mNextButton = (Button) v.findViewById(R.id.next);
		mNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.getViewPager().setCurrentItem(3);
			}
		});
		
		populate();
		
		return v;
	}
	
	public void setupExample(View v) {
		int level;
		if(mDifficulty.equals(getResources().getString(R.string.easy))) {
			level = 1;
		} else if(mDifficulty.equals(getResources().getString(R.string.medium))) {
			level = 2;
		} else if(mDifficulty.equals(getResources().getString(R.string.hard))) {
			level = 3;
		} else if(mDifficulty.equals(getResources().getString(R.string.expert))) {
			level = 4;
		} else if(mDifficulty.equals(getResources().getString(R.string.all))) {
			level = 13;
		} else {
			level = Integer.parseInt(mDifficulty);
		}
		
		int[] sample = TestManager.getSampleQuestion(mOperation, level);
		mFirstNum = sample[0];
		mSecondNum = sample[1];
		
		if(mOperation.equals(TestManager.ADDITION))
			mOperationSign = "+";
		else if(mOperation.equals(TestManager.SUBTRACTION))
			mOperationSign = "-";
		else if(mOperation.equals(TestManager.MULTIPLICATION))
			mOperationSign = "x";
		else if(mOperation.equals(TestManager.DIVISION))
			mOperationSign = "\u00F7";
		
		card.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		
		float widthUnit  = (float) (card.getMeasuredWidth()/1000.0);
		float heightUnit = (float) (card.getMeasuredHeight()/1000.0);
		
		firstNum.setText("" + mFirstNum);
		secondNum.setText("" + mSecondNum);
		
		firstNum.setTranslationX(-140*widthUnit);
		firstNum.setTranslationY(100*heightUnit);
		firstNum.setTextColor(Color.BLACK);
		
		secondNum.setTranslationX(-140*widthUnit);
		secondNum.setTranslationY(75*heightUnit);
		secondNum.setTextColor(Color.BLACK);
		
		if(String.valueOf(mSecondNum).length() == 3) {
			operationText.setText(mOperationSign);
			operationText.setTranslationX(60*widthUnit);
		} else {
			operationText.setText(mOperationSign);
			operationText.setTranslationX(150*widthUnit);
		}
		
		if(mOperation.equals(TestManager.SUBTRACTION)) {
			operationText.setTranslationY(85*heightUnit);
			operationText.setTextSize(80);
		} else {
			operationText.setTranslationY(70*heightUnit);
		}
		
		separator.setTranslationY(70*heightUnit);
		separator.setWidth((int) (850*widthUnit));
	}
	
	private String getDifficulty() {
		if(mOperation.equals(TestManager.ADDITION)) {
			String[] array = mActivity.getResources().getStringArray(R.array.addition_difficulty);
			if(mPosition < 0)
				mPosition = array.length - 1;
			else
				mPosition = mPosition % array.length;
			
			return array[mPosition];
		} else if(mOperation.equals(TestManager.SUBTRACTION)) {
			String[] array = mActivity.getResources().getStringArray(R.array.subtraction_difficulty);
			if(mPosition < 0)
				mPosition = array.length - 1;
			else
				mPosition = mPosition % array.length;
			
			return array[mPosition];			
		} else if(mOperation.equals(TestManager.MULTIPLICATION)) {
			String[] array = mActivity.getResources().getStringArray(R.array.multiplication_difficulty);
			if(mPosition < 0)
				mPosition = array.length - 1;
			else
				mPosition = mPosition % array.length;
			
			return array[mPosition];
		} else if(mOperation.equals(TestManager.DIVISION)) {
			String[] array = mActivity.getResources().getStringArray(R.array.division_difficulty);
			if(mPosition < 0)
				mPosition = array.length - 1;
			else
				mPosition = mPosition % array.length;
			
			return array[mPosition];
		}
		
		return "";
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			mActivity.getViewPager().setCurrentItem(1);
			return true;
		}
		return false;
	}
	
	public void populate() {
		mOperation = mActivity.getOperation();
		if(mOperation == null) return;
		if(mOperation.equals(TestManager.ADDITION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_red));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_addition));
			mLevelTextView.setText(getResources().getString(R.string.choose_difficulty));
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_red);
		}
		else if(mOperation.equals(TestManager.SUBTRACTION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_blue));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_subtraction));
			mLevelTextView.setText(getResources().getString(R.string.choose_difficulty));
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_blue);
		}
		else if(mOperation.equals(TestManager.MULTIPLICATION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_green));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_multiplication));
			mLevelTextView.setText(getResources().getString(R.string.choose_multiplier));
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_green);
		}
		else if(mOperation.equals(TestManager.DIVISION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_purple));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_division));
			mLevelTextView.setText(getResources().getString(R.string.choose_divider));
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_purple);
		}
	}
}
