package com.education.android.mathcounts;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseTestLengthFragment extends Fragment {

	private HomeActivity mActivity;
	
	private int mQuestionPos;
	private int mTimePos;
	private String mOperation;
	
	private String mNumQuestions;
	private String mTime;
	
	private Button mNextButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mActivity = (HomeActivity) getActivity();
		
		mQuestionPos = 0;
		mTimePos = 0;
		
		mNumQuestions = getNumQuestions();
		mTime = getTime();
		
		mActivity.setNumQuestions(mNumQuestions);
		mActivity.setTime(mTime);
		
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		setHasOptionsMenu(true);
		
		View v = inflater.inflate(R.layout.fragment_choose_test_length, null);
		
		final TextView tvNumQuestions = (TextView) v.findViewById(R.id.numQuestions);
		tvNumQuestions.setText(mNumQuestions);
		
		ImageView incrementQ = (ImageView) v.findViewById(R.id.incrementQuestions);
		incrementQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mQuestionPos++;
				mNumQuestions = getNumQuestions();
				tvNumQuestions.setText(mNumQuestions);
				mActivity.setNumQuestions(mNumQuestions);
			}
		});
		
		ImageView decrementQ = (ImageView) v.findViewById(R.id.decrementQuestions);
		decrementQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mQuestionPos--;
				mNumQuestions = getNumQuestions();
				tvNumQuestions.setText(mNumQuestions);
				mActivity.setNumQuestions(mNumQuestions);
			}
		});
		
		final TextView tvTime = (TextView) v.findViewById(R.id.amountTime);
		tvTime.setText(mTime);
		
		ImageView incrementT = (ImageView) v.findViewById(R.id.incrementTime);
		incrementT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTimePos++;
				mTime = getTime();
				tvTime.setText(mTime);
				mActivity.setTime(mTime);
			}
		});
		
		ImageView decrementT = (ImageView) v.findViewById(R.id.decrementTime);
		decrementT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTimePos--;
				mTime = getTime();
				tvTime.setText(mTime);
				mActivity.setTime(mTime);
			}
		});
		
		mNextButton = (Button) v.findViewById(R.id.next);
		mNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.beginTest();
			}
		});
		
		populate();
		
		return v;
	}

	private String getNumQuestions() {
		String[] array = mActivity.getResources().getStringArray(R.array.question_numbers);
		if(mQuestionPos < 0)
			mQuestionPos = array.length - 1;
		else
			mQuestionPos = mQuestionPos % array.length;
		
		mNumQuestions = array[mQuestionPos];
		
		return mNumQuestions;
	}
	
	private String getTime() {
		String[] array = mActivity.getResources().getStringArray(R.array.timer_values);
		if(mTimePos < 0)
			mTimePos = array.length - 1;
		else
			mTimePos = mTimePos % array.length;
		
		mTime = array[mTimePos];
		
		return mTime;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			mActivity.getViewPager().setCurrentItem(2);
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
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_red);
		}
		else if(mOperation.equals(TestManager.SUBTRACTION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_blue));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_subtraction));
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_blue);
		}
		else if(mOperation.equals(TestManager.MULTIPLICATION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_green));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_multiplication));
			//mLevelTextView.setText(getResources().getString(R.string.multiplier));
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_green);
		}
		else if(mOperation.equals(TestManager.DIVISION)) {
			getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_purple));
			getActivity().getActionBar().setTitle(" " + getResources().getString(R.string.button_division));
			//mLevelTextView.setText(getResources().getString(R.string.divisor));
			mNextButton.setBackgroundResource(R.drawable.button_shape_start_test_purple);
		}
	}
}
