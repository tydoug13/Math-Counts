package com.education.android.mathcounts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestFinishedFragment extends Fragment {
	
	public static final String NUM_UNANSWERED_QUESTIONS = "unanswered";

	private int mNumUnanswered;
	private int mFirstUnanswered;
	private TextView mNumSkipped;
	private TextView mFinishedPrompt;
	private Button mGoBack;
	private LinearLayout mWrongPrompt;
	private ViewPager mViewPager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_test_finished, container, false);
		mNumSkipped = (TextView) v.findViewById(R.id.numSkipped);
		mGoBack = (Button) v.findViewById(R.id.submit);
		mFinishedPrompt = (TextView) v.findViewById(R.id.pressSubmit);
		mWrongPrompt = (LinearLayout) v.findViewById(R.id.wrong_prompt);
		
		mViewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);
		
		mGoBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mViewPager.setCurrentItem(mFirstUnanswered);
			}
		});
		
		
		return v;
	}

	/**
	 * 
	 * @param numUnanswered number of unanswered questions from the test
	 * @return 
	 */
	public static TestFinishedFragment newInstance() {
		return new TestFinishedFragment();
	}
	
	public void setNumUnanswered(int numUnanswered) {
		mNumUnanswered = numUnanswered;
		mNumSkipped.setText(mNumUnanswered + "");
		if(mNumUnanswered == 0) {
			mWrongPrompt.setVisibility(View.INVISIBLE);
			mFinishedPrompt.setVisibility(View.VISIBLE);
			mFinishedPrompt.setText(getResources().getString(R.string.press_submit));
		}
	}
	
	public void setFirstUnanswered(int firstUnanswered) {
		mFirstUnanswered = firstUnanswered;
	}

}
